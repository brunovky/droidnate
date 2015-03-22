package com.brunooliveira.droidnate.fluent;

import com.brunooliveira.droidnate.ws.helper.WebServicesHelper;

public final class WebServicesConfig {

    public static final String PROTOCOL_HTTP = "http";
    public static final String PROTOCOL_HTTPS = "https";

    public WebServicesConfig protocol(String protocol) {
        WebServicesHelper.setProtocol(protocol);
        return this;
    }

	public WebServicesConfig serverName(String serverName) {
		WebServicesHelper.setServerName(serverName);
		return this;
	}

	public WebServicesConfig port(String port) {
		WebServicesHelper.setPort(port);
		return this;
	}

	public WebServicesConfig projectName(String projectName) {
		WebServicesHelper.setProjectName(projectName);
		return this;
	}

	public WebServicesConfig waitingText(String waitingText) {
		WebServicesHelper.setWaitingText(waitingText);
		return this;
	}

}