package com.brunooliveira.droidnate.ws.helper;

import com.brunooliveira.droidnate.ws.request.Request;

import java.util.Map;

public final class WebServicesHelper {

    private static String protocol;
	private static String serverName;
	private static String port;
	private static String projectName;
	private static String waitingText;

    private static Request lastRequest;

    public static void setProtocol(String protocol) {
        WebServicesHelper.protocol = protocol;
    }

    public static void setServerName(String serverName) {
		WebServicesHelper.serverName = serverName;
	}
	
	public static void setPort(String port) {
		WebServicesHelper.port = port;
	}

	public static void setProjectName(String projectName) {
		WebServicesHelper.projectName = projectName;
	}
	
	public static String getWaitingText() {
		return waitingText;
	}
	
	public static void setWaitingText(String waitingText) {
		WebServicesHelper.waitingText = waitingText;
	}

    public static void setLastRequest(Request lastRequest) {
        WebServicesHelper.lastRequest = lastRequest;
    }

    public static String getAbsoluteURL(String url) {
		String absoluteURL = protocol + "://" + serverName + (port != null ? ":" + port : "");
		if (projectName != null) absoluteURL += "/" + (projectName.endsWith("/") ? projectName : projectName + "/") + url;
		else absoluteURL += "/" + url;
		return absoluteURL;
	}
	
	public static String getAbsoluteURL(String url, Map<String, Object> params) {
		String absoluteURL = getAbsoluteURL(url);
		if (params != null) {			
			for (String key : params.keySet()) {
				absoluteURL = absoluteURL.replace("{" + key + "}", params.get(key).toString());
			}
		}
		return absoluteURL;
	}

    public static String getAbsoluteURL() {
        String absoluteURL = getAbsoluteURL(lastRequest.getResourcePath() + lastRequest.getUrl());
        Map<String, Object> params = lastRequest.getParams();
        if (params != null) {
            for (String key : params.keySet()) {
                absoluteURL = absoluteURL.replace("{" + key + "}", params.get(key).toString());
            }
        }
        return absoluteURL;
    }

}