package com.brunooliveira.droidnate.ws.request;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.brunooliveira.droidnate.ws.helper.WebServicesHelper;
import com.brunooliveira.droidnate.ws.response.SimpleResponseHandler;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;

import java.net.URI;
import java.util.Map;

public class DeleteNoReturnsRequestTask extends AsyncTask<Void, Void, Void> {

	private String url;
	private Map<String, Object> params;
	private SimpleResponseHandler simpleHandler;
	private ProgressDialog dialog;

	public DeleteNoReturnsRequestTask(Request request) {
		this.url = request.getResourcePath() + request.getUrl();
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
	protected Void doInBackground(Void... params) {
		try {
			HttpDelete delete = new HttpDelete(new URI(WebServicesHelper.getAbsoluteURL(url, DeleteNoReturnsRequestTask.this.params)));
			new DefaultHttpClient().execute(delete);
			return null;
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