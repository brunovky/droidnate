package com.brunooliveira.droidnate.ws.request;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.brunooliveira.droidnate.util.DroidnateUtil;
import com.brunooliveira.droidnate.ws.enums.MediaType;
import com.brunooliveira.droidnate.ws.helper.WebServicesHelper;
import com.brunooliveira.droidnate.ws.response.ListModelResponseHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListModelRequestTask<T> extends AsyncTask<Void, Void, List<T>> {

	private String url;
	private MediaType produces;
	private Map<String, Object> params;
	private Class<T> returnType;
	private ListModelResponseHandler<T> modelHandler;
	private ProgressDialog dialog;

	@SuppressWarnings("unchecked")
	public ListModelRequestTask(Request request) {
		this.url = request.getResourcePath() + request.getUrl();
		this.produces = request.getProduces();
		this.params = request.getParams();
		this.returnType = (Class<T>) request.getReturnType();
		this.dialog = request.isWaitingDialog() ? new ProgressDialog(request.getContext()) : null;
	}
	
	public void setModelHandler(ListModelResponseHandler<T> modelHandler) {
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
	@Override
	protected List<T> doInBackground(Void... params) {
		try {
			HttpGet get = new HttpGet(new URI(WebServicesHelper.getAbsoluteURL(url, ListModelRequestTask.this.params)));
			get.setHeader("Content-type", "application/json");
			HttpResponse response = new DefaultHttpClient().execute(get);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inStream = entity.getContent();
				if (produces.equals(MediaType.JSON)) {
					List<T> list = new ArrayList<T>();
					JsonArray array = new JsonParser().parse(DroidnateUtil.convertStreamToString(inStream)).getAsJsonArray();
					Gson gson = new GsonBuilder().create();
					for (JsonElement element : array) {
						list.add(gson.fromJson(element, returnType));
					}
					return list;
				}
				Serializer serializer = new Persister();
                return (List<T>) serializer.read(returnType, DroidnateUtil.convertStreamToString(inStream));
			}
			return null;
		} catch (Exception e) {
			Log.e("Droidnate Error :: WebServices", e.getMessage());
			cancel(true);
			return null;
		}
	}

	@Override
	protected void onPostExecute(List<T> result) {
		if (dialog != null && dialog.isShowing()) dialog.dismiss();
		if (modelHandler != null) modelHandler.onSuccess(result);
	}

	@Override
	protected void onCancelled() {
		if (modelHandler != null) modelHandler.onFailure("");
	}

}