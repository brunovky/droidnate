package com.brunooliveira.droidnate.ws.request;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.brunooliveira.droidnate.ws.enums.MediaType;
import com.brunooliveira.droidnate.ws.enums.RequestType;
import com.brunooliveira.droidnate.ws.helper.WebServicesHelper;
import com.brunooliveira.droidnate.ws.response.SimpleResponseHandler;
import com.google.gson.GsonBuilder;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.net.URI;
import java.util.Map;

public class NoReturnsRequestTask<T> extends AsyncTask<T, Void, Void> {

	private RequestType requestType;
	private String url;
	private MediaType consumes;
	private Map<String, Object> params;
	private SimpleResponseHandler simpleHandler;
	private ProgressDialog dialog;

	public NoReturnsRequestTask(Request request) {
		this.requestType = request.getType();
		this.url = request.getResourcePath() + request.getUrl();
		this.consumes = request.getConsumes();
		this.params = request.getParams();
		this.dialog = request.isWaitingDialog() ? new ProgressDialog(request.getContext()) : null;
	}
	
	public void setSimpleHandler(SimpleResponseHandler simpleHandler) {
		this.simpleHandler = simpleHandler;
	}

	@Override
	protected void onPreExecute() {
		if (dialog != null) {
			dialog.setMessage(WebServicesHelper.getWaitingText());
			dialog.show();
		}
	}

	@SuppressLint("DefaultLocale")
	@Override
	protected Void doInBackground(T... params) {
		T model = params.length > 0 ? params[0] : null;
		try {
			switch (requestType) {
            case GET:
                HttpGet get = new HttpGet(new URI(WebServicesHelper.getAbsoluteURL(url, NoReturnsRequestTask.this.params)));
                Log.i("URL", WebServicesHelper.getAbsoluteURL(url, NoReturnsRequestTask.this.params));
                get.setHeader("Content-type", "application/json");
                new DefaultHttpClient().execute(get);
                return null;
			case POST:
				HttpPost post = new HttpPost(new URI(WebServicesHelper.getAbsoluteURL(url)));
				if (consumes.equals(MediaType.JSON)) {
					StringEntity sEntity = new StringEntity(new GsonBuilder().create().toJson(model), "UTF-8");
					post.setEntity(sEntity);
				} else {
					// TODO StringEntity to XML
				}
				new DefaultHttpClient().execute(post);
				return null;
			case PUT:
				HttpPut put = new HttpPut(new URI(WebServicesHelper.getAbsoluteURL(url, NoReturnsRequestTask.this.params)));
				if (consumes.equals(MediaType.JSON)) {
					StringEntity sEntity = new StringEntity(new GsonBuilder().create().toJson(model), "UTF-8");
					put.setEntity(sEntity);
				} else {
					// TODO StringEntity to XML
				}
				new DefaultHttpClient().execute(put);
				return null;
			default:
				cancel(true);
				return null;
			}
		} catch (Exception e) {
			Log.e("Droidnate Error :: WebServices", e.getMessage());
			cancel(true);
			return null;
		}
	}

	@Override
	protected void onPostExecute(Void result) {
		if (dialog != null && dialog.isShowing()) dialog.dismiss();
		simpleHandler.onSuccess(result);
	}

	@Override
	protected void onCancelled() {
		simpleHandler.onFailure("");
	}

}