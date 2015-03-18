package com.brunooliveira.droidnate.ws.request;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;

import com.brunooliveira.droidnate.util.DroidnateUtil;
import com.brunooliveira.droidnate.ws.enums.MediaType;
import com.brunooliveira.droidnate.ws.enums.RequestType;
import com.brunooliveira.droidnate.ws.helper.WebServicesHelper;
import com.brunooliveira.droidnate.ws.response.ModelResponseHandler;
import com.google.gson.GsonBuilder;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.InputStream;
import java.net.URI;
import java.util.Map;

public class ModelRequestTask<T> extends AsyncTask<T, Void, T> {

    private Request request;
	private RequestType requestType;
	private String url;
	private MediaType produces;
	private MediaType consumes;
	private Map<String, Object> params;
	private ModelResponseHandler<T> modelHandler;
	private Class<T> returnType;
	private ProgressDialog dialog;

	@SuppressWarnings("unchecked")
	public ModelRequestTask(Request request) {
        this.request = request;
		this.requestType = request.getType();
		this.url = request.getResourcePath() + request.getUrl();
		this.produces = request.getProduces();
		this.consumes = request.getConsumes();
		this.params = request.getParams();
		this.returnType = (Class<T>) request.getReturnType();
		this.dialog = request.isWaitingDialog() ? new ProgressDialog(request.getContext()) : null;
	}

	public void setModelHandler(ModelResponseHandler<T> modelHandler) {
		this.modelHandler = modelHandler;
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
	protected T doInBackground(T... params) {
		T model = params.length > 0 ? params[0] : null;
		try {
			switch (requestType) {
			case GET:
				HttpGet get = new HttpGet(new URI(WebServicesHelper.getAbsoluteURL(url, ModelRequestTask.this.params)));
                Log.i("URL", WebServicesHelper.getAbsoluteURL(url, ModelRequestTask.this.params));
                WebServicesHelper.setLastRequest(request);
                get.setHeader("Content-type", "application/json");
                String httpProxy = Settings.Global.getString(request.getContext().getContentResolver(), Settings.Global.HTTP_PROXY);
                /*if (ManagerPreferences.getBoolean(request.getContext(), ManagerPreferences.NEED_PROXY) && httpProxy != null && !httpProxy.equals("")) {
                    Header bs = new BasicScheme().authenticate(
                            new UsernamePasswordCredentials(ManagerPreferences.getString(request.getContext(), ManagerPreferences.USERNAME_PROXY), ManagerPreferences.getString(request.getContext(), ManagerPreferences.PASSWORD_PROXY)),
                            get);
                    get.addHeader("Proxy-Authorization", bs.getValue());
                }*/
                HttpResponse response = new DefaultHttpClient().execute(get);
                /*if (httpProxy == null || httpProxy.equals("")) ManagerPreferences.set(request.getContext(), ManagerPreferences.NEED_PROXY, false);*/
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream inStream = entity.getContent();
					if (produces.equals(MediaType.JSON)) return (T) new GsonBuilder().create().fromJson(DroidnateUtil.convertStreamToString(inStream), returnType);
                    Serializer serializer = new Persister();
                    return serializer.read(returnType, DroidnateUtil.convertStreamToString(inStream));
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
					if (produces.equals(MediaType.JSON)) return (T) new GsonBuilder().create().fromJson(DroidnateUtil.convertStreamToString(inStream), model.getClass());
					// TODO Return with XML
					return null;
				}
				return null;
			case PUT:
				HttpPut put = new HttpPut(new URI(WebServicesHelper.getAbsoluteURL(url, ModelRequestTask.this.params)));
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
					if (produces.equals(MediaType.JSON)) return (T) new GsonBuilder().create().fromJson(DroidnateUtil.convertStreamToString(inStream), model.getClass());
					// TODO Return with XML
					return null;
				}
				return null;
			default:
				cancel(true);
				return null;
			}
		} catch (Exception e) {
            Log.e("Droidnate Error", Log.getStackTraceString(e));
            /*LogUtils.writeLine(request.getContext(), "download", "ERROR", Log.getStackTraceString(e));*/
			cancel(true);
			return null;
		}
	}

	@Override
	protected void onPostExecute(T result) {
		if (dialog != null && dialog.isShowing()) dialog.dismiss();
		if (modelHandler != null) modelHandler.onSuccess(result);
	}

	@Override
	protected void onCancelled() {
		if (modelHandler != null) modelHandler.onFailure("");
	}

}