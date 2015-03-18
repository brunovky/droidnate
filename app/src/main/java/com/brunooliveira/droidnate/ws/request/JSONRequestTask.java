package com.brunooliveira.droidnate.ws.request;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.brunooliveira.droidnate.util.DroidnateUtil;
import com.brunooliveira.droidnate.ws.enums.MediaType;
import com.brunooliveira.droidnate.ws.enums.RequestType;
import com.brunooliveira.droidnate.ws.helper.WebServicesHelper;
import com.brunooliveira.droidnate.ws.response.JSONResponseHandler;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.net.URI;
import java.util.Map;

public class JSONRequestTask<T> extends AsyncTask<T, Void, JsonObject> {

	private RequestType requestType;
	private String url;
	private MediaType produces;
	private MediaType consumes;
	private Map<String, Object> params;
	private JSONResponseHandler jsonHandler;
	private ProgressDialog dialog;

	public JSONRequestTask(Request request) {
		this.requestType = request.getType();
		this.url = request.getResourcePath() + request.getUrl();
		this.produces = request.getProduces();
		this.consumes = request.getConsumes();
		this.params = request.getParams();
		this.dialog = request.isWaitingDialog() ? new ProgressDialog(request.getContext()) : null;
	}
	
	public void setJsonHandler(JSONResponseHandler jsonHandler) {
		this.jsonHandler = jsonHandler;
	}

	@Override
	protected void onPreExecute() {
		if (dialog != null) {
			dialog.setMessage(WebServicesHelper.getWaitingText());
			dialog.show();
		}
	}

	@Override
	protected JsonObject doInBackground(T... params) {
		T model = params.length > 0 ? params[0] : null;
		try {
			switch (requestType) {
			case GET:
				HttpGet get = new HttpGet(new URI(WebServicesHelper.getAbsoluteURL(url, JSONRequestTask.this.params)));
				get.setHeader("Content-type", "application/json");
				HttpResponse response = new DefaultHttpClient().execute(get);
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream inStream = entity.getContent();
					if (produces.equals(MediaType.JSON)) return (JsonObject) new JsonParser().parse(DroidnateUtil.convertStreamToString(inStream));
					return null;
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
					if (produces.equals(MediaType.JSON)) return (JsonObject) new JsonParser().parse(DroidnateUtil.convertStreamToString(inStream));
					return null;
				}
				return null;
			case PUT:
				HttpPut put = new HttpPut(new URI(WebServicesHelper.getAbsoluteURL(url, JSONRequestTask.this.params)));
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
					if (produces.equals(MediaType.JSON)) return (JsonObject) new JsonParser().parse(DroidnateUtil.convertStreamToString(inStream));
					return null;
				}
				return null;
			case DELETE:
				HttpDelete delete = new HttpDelete(new URI(WebServicesHelper.getAbsoluteURL(url, JSONRequestTask.this.params)));
				delete.setHeader("Content-type", "application/" + produces.name().toLowerCase());
				response = new DefaultHttpClient().execute(delete);
				entity = response.getEntity();
				if (entity != null) {
					InputStream inStream = entity.getContent();
					if (produces.equals(MediaType.JSON)) return (JsonObject) new JsonParser().parse(DroidnateUtil.convertStreamToString(inStream));
					return null;
				}
				return null;
			}
			return null;
		} catch (Exception e) {
			Log.e("Droidnate Error :: WebServices", e.getMessage());
			cancel(true);
			return null;
		}
	}

	@Override
	protected void onPostExecute(JsonObject result) {
		if (dialog != null && dialog.isShowing()) dialog.dismiss();
		if (jsonHandler != null) jsonHandler.onSuccess(result);
	}

	@Override
	protected void onCancelled() {
		if (jsonHandler != null) jsonHandler.onFailure("");
	}

}