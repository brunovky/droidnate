package com.brunooliveira.droidnate.ws.response;

public interface IResponseHandler<T> {
	
	void onSuccess(T result);
	
	void onFailure(String errorMsg);
	
}