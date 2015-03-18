package com.brunooliveira.droidnate.ws.request;

import android.content.Context;

import java.util.Map;

public class RequestBuilder {
	
	private Request request;
	
	public RequestBuilder(Context context) {
		this.request = new Request(context);
	}
	
	public RequestBuilder entity(Object entity) {
		this.request.setEntity(entity);
		return this;
	}
	
	public RequestBuilder waitingDialog() {
		this.request.setWaitingDialog(true);
		return this;
	}
	
	public RequestBuilder params(Map<String, Object> params) {
		this.request.setParams(params);
		return this;
	}
	
	public RequestBuilder param(String key, Object value) {
		this.request.addParam(key, value);
		return this;
	}

    public RequestBuilder parallel() {
        this.request.setParallel(true);
        return this;
    }
	
	public Request create() {
		return this.request;
	}

}