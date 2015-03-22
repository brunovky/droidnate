package com.brunooliveira.droidnate.fluent;

import com.brunooliveira.droidnate.helper.DatabaseHelper;

public final class DatabaseConfig {
	
	public DatabaseConfig dbName(String dbName) {
		DatabaseHelper.setDatabaseName(dbName);
		return this;
	}

    public DatabaseConfig dbVersion(int dbVersion) {
		DatabaseHelper.setDatabaseVersion(dbVersion);
		return this;
	}

}