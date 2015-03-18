package com.brunooliveira.droidnate.fluent;

public final class DroidnateConfig {
	
	public static DatabaseConfig db() {
		return new DatabaseConfig();
	}
	
	public static WebServicesConfig ws() {
		return new WebServicesConfig();
	}

}