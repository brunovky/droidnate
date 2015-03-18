package com.brunooliveira.droidnate.ws.request;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.brunooliveira.droidnate.ws.enums.RequestType;
import com.brunooliveira.droidnate.ws.helper.WebServicesHelper;
import com.brunooliveira.droidnate.ws.response.ToastResponseHandler;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.net.URI;
import java.util.Map;

public class ToastNoEntityRequestTask extends AsyncTask<Void, Void, Void> {

	private RequestType requestType;
	private String url;
	private Map<String, Object> params;
	private ToastResponseHandler toastHandler;
	private ProgressDialog dialog;

	public ToastNoEntityRequestTask(Request request) {
		this.requestType = request.getType();
		this.url = request.getResourcePath() + request.getUrl();
		this.params = request.getParams();
		this.dialog = request.isWaitingDialog() ? new ProgressDialog(request.getContext()) : null;
	}
	
	public void setToastHandler(ToastResponseHandler toastHandler) {
		this.toastHandler = toastHandler;
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
	protected Void doInBackground(Void... params) {
		try {
			switch (requestType) {
			case GET:
				HttpGet get = new HttpGet(new URI(WebServicesHelper.getAbsoluteURL(url, ToastNoEntityRequestTask.this.params)));
				get.setHeader("Content-type", "application/json");
				new DefaultHttpClient().execute(get);
				return null;
			case DELETE:
				HttpDelete delete = new HttpDelete(new URI(WebServicesHelper.getAbsoluteURL(url, ToastNoEntityRequestTask.this.params)));
				new DefaultHttpClient().execute(delete);
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
		if (toastHandler != null) toastHandler.onSuccess(result);
	}

	@Override
	protected void onCancelled() {
		if (toastHandler != null) toastHandler.onFailure("");
	}

}