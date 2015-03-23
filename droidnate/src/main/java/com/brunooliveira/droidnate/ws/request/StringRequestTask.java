package com.brunooliveira.droidnate.ws.request;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.brunooliveira.droidnate.util.DroidnateUtil;
import com.brunooliveira.droidnate.ws.enums.MediaType;
import com.brunooliveira.droidnate.ws.enums.RequestType;
import com.brunooliveira.droidnate.ws.helper.WebServicesHelper;
import com.brunooliveira.droidnate.ws.response.StringResponseHandler;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;

public class StringRequestTask<T> extends AsyncTask<T, Void, String> {

    private Request request;
	private RequestType requestType;
	private String url;
	private MediaType produces;
	private MediaType consumes;
	private Map<String, Object> params;
	private StringResponseHandler stringHandler;
	private Class<T> returnType;
	private ProgressDialog dialog;

	@SuppressWarnings("unchecked")
	public StringRequestTask(Request request) {
        this.request = request;
		this.requestType = request.getType();
		this.url = request.getResourcePath() + request.getUrl();
		this.produces = request.getProduces();
		this.consumes = request.getConsumes();
		this.params = request.getParams();
		this.returnType = (Class<T>) request.getReturnType();
		this.dialog = request.isWaitingDialog() ? new ProgressDialog(request.getContext()) : null;
	}

    public void setStringHandler(StringResponseHandler stringHandler) {
        this.stringHandler = stringHandler;
    }

    @Override
	protected void onPreExecute() {
		if (dialog != null) {
			dialog.setMessage(WebServicesHelper.getWaitingText());
			dialog.show();
		}
	}

	@SuppressLint("DefaultLocale")
	@SuppressWarnings("unchecked")
	@Override
	protected String doInBackground(T... params) {
		T model = params.length > 0 ? params[0] : null;
		try {
			switch (requestType) {
			case GET:
				HttpGet get = new HttpGet(new URI(WebServicesHelper.getAbsoluteURL(url, StringRequestTask.this.params)));
                Log.i("URL", WebServicesHelper.getAbsoluteURL(url, StringRequestTask.this.params));
                WebServicesHelper.setLastRequest(request);
				get.setHeader("Content-type", "application/json");
                /*if (WebServicesHelper.getAbsoluteURL(url, StringRequestTask.this.params).contains("method=applicabilityControl")) {
                    get.setHeader("Cookie", "JSESSIONID=" + ManagerPreferences.getString(request.getContext(), ManagerPreferences.JSESSIONID));
                }
                String httpProxy = Settings.Global.getString(request.getContext().getContentResolver(), Settings.Global.HTTP_PROXY);
                if (ManagerPreferences.getBoolean(request.getContext(), ManagerPreferences.NEED_PROXY) && httpProxy != null && !httpProxy.equals("")) {
                    Header bs = new BasicScheme().authenticate(
                            new UsernamePasswordCredentials(ManagerPreferences.getString(request.getContext(), ManagerPreferences.USERNAME_PROXY), ManagerPreferences.getString(request.getContext(), ManagerPreferences.PASSWORD_PROXY)),
                            get);
                    get.addHeader("Proxy-Authorization", bs.getValue());
                }*/
                DefaultHttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(get);
                if (response.getStatusLine() != null && response.getStatusLine().getStatusCode() == 407) return "407 - Proxy Authentication Required";
                //if (httpProxy == null || httpProxy.equals("")) ManagerPreferences.set(request.getContext(), ManagerPreferences.NEED_PROXY, false);
                HttpEntity entity = response.getEntity();
				if (entity != null) {
                    InputStream inStream = entity.getContent();
                    if (WebServicesHelper.getAbsoluteURL(url, StringRequestTask.this.params).contains("method=login")) {
                        List<Cookie> cookies = client.getCookieStore().getCookies();
                        String jSessionId = "";
                        for (Cookie cookie : cookies) {
                            if (cookie.getName().equals("JSESSIONID")) jSessionId = cookie.getValue();
                        }
                        //LogUtils.writeLine(request.getContext(), "cookie", "JSESSIONID=" + jSessionId, "OK");
                        return DroidnateUtil.convertStreamToString(inStream) + " [JSESSIONID=" + jSessionId + "]";
                    }
                    return DroidnateUtil.convertStreamToString(inStream);
				}
				return null;
			case POST:
				HttpPost post = new HttpPost(new URI(WebServicesHelper.getAbsoluteURL(url)));
				post.setHeader("Content-type", "application/" + produces.name().toLowerCase());
				if (consumes.equals(MediaType.JSON)) {
					StringEntity sEntity = new StringEntity(new GsonBuilder().create().toJson(model), "UTF-8");
					post.setEntity(sEntity);
				} else {
					// TODO StringEntity to XML
				}
				response = new DefaultHttpClient().execute(post);
				entity = response.getEntity();
				if (entity != null) {
                    InputStream inStream = entity.getContent();
                    return DroidnateUtil.convertStreamToString(inStream);
				}
				return null;
			case PUT:
				HttpPut put = new HttpPut(new URI(WebServicesHelper.getAbsoluteURL(url, StringRequestTask.this.params)));
				put.setHeader("Content-type", "application/" + produces.name().toLowerCase());
				if (consumes.equals(MediaType.JSON)) {
					StringEntity sEntity = new StringEntity(new GsonBuilder().create().toJson(model), "UTF-8");
					put.setEntity(sEntity);
				} else {
					// TODO StringEntity to XML
				}
				response = new DefaultHttpClient().execute(put);
				entity = response.getEntity();
				if (entity != null) {
                    InputStream inStream = entity.getContent();
                    return DroidnateUtil.convertStreamToString(inStream);
				}
				return null;
			default:
				cancel(true);
				return null;
			}
		} catch (Exception e) {
            Log.e("Droidnate Error", Log.getStackTraceString(e));
            //LogUtils.writeLine(request.getContext(), "download", "ERROR", Log.getStackTraceString(e));
			cancel(true);
			return null;
		}
	}

	@Override
	protected void onPostExecute(String result) {
		if (dialog != null && dialog.isShowing()) dialog.dismiss();
		if (stringHandler != null) stringHandler.onSuccess(result);
	}

	@Override
	protected void onCancelled() {
		if (stringHandler != null) stringHandler.onFailure("");
	}

}