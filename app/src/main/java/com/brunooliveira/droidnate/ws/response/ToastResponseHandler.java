package com.brunooliveira.droidnate.ws.response;

import android.content.Context;
import android.widget.Toast;

public class ToastResponseHandler implements IResponseHandler<Object> {

	private String successMessage;
	private String errorMessage;
	private Context context;

	public ToastResponseHandler(Context context, String successMessage, String errorMessage) {
		this.context = context;
		this.successMessage = successMessage;
		this.errorMessage = errorMessage;
	}

	@Override
	public void onSuccess(Object result) {
		Toast.makeText(context, successMessage, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onFailure(String errorMsg) {
		Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
	}

}