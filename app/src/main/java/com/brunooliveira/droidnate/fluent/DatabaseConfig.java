package com.brunooliveira.droidnate.fluent;

import com.brunooliveira.droidnate.helper.DatabaseHelper;

public final class DatabaseConfig {
	
	public DatabaseConfig defaultPackage(String defaultPackage) {
		DatabaseHelper.setDefaultPackage(defaultPackage);
		return this;
	}
	
	public DatabaseConfig dbName(String dbName) {
		DatabaseHelper.setDatabaseName(dbName);
		return this;
	}

}