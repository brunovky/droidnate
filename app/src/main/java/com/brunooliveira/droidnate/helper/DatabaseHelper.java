package com.brunooliveira.droidnate.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.brunooliveira.droidnate.exception.DroidnateException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Is a subclass of {@link android.database.sqlite.SQLiteOpenHelper}, a helper class to manage database creation and version management.
 *
 * <p>You must identify the name of the main package of the project, besides the database name being used.
 *
 * <p class="note"><strong>Note: </strong>The database file must be in <strong>"assets"</strong> project folder.
 * <br><br>
 *
 * @author	Bruno Oliveira
 * @see		android.database.sqlite.SQLiteOpenHelper
 * @since	v.1
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	private static String defaultPackage = "";
	private static String databaseName = "";
	private static String databasePath = "";
	private Context context;

	/**
	 * Create a helper object to create, open, and/or manage a database.
	 * @param context to use to open or create the database
	 */
	@SuppressLint("SdCardPath")
	public DatabaseHelper(Context context) {
		super(context, "/data/data/" + defaultPackage
				+ "/databases/" + databaseName, null, 1);
		databasePath = "/data/data/" + defaultPackage
				+ "/databases/" + databaseName;
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	/**
	 * Open and return a database.
	 * <p>It checks if there is a database in the "data" device folder, if it does not, it copies the database file of "assets" project folder to the "data" device folder and then open and returns a database.
	 * @return a database object valid
	 * @throws DroidnateException if the database cannot be opened or if there was some problem in the copy of the database file
	 */
	public SQLiteDatabase getDatabase() {
		if (checkdatabase()) {
			return SQLiteDatabase.openDatabase(databasePath, null,
					SQLiteDatabase.OPEN_READWRITE);
		}
		this.getReadableDatabase();
		try {
			copyDatabase();
			return SQLiteDatabase.openDatabase(databasePath, null,
					SQLiteDatabase.OPEN_READWRITE);
		} catch (DroidnateException e) {
			Log.e(e.getMessage(), e.getCause().getMessage());
			return null;
		}
	}

	private boolean checkdatabase() {
		boolean checkdb = false;
		try {
			File dbfile = new File(databasePath);
			checkdb = dbfile.exists();
		} catch (SQLiteException e) {
		}
		return checkdb;
	}

	private void copyDatabase() throws DroidnateException {
		try {
			InputStream myInput = context.getAssets().open(databaseName);
			OutputStream myOutput = new FileOutputStream(databasePath);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);
			}
			myOutput.flush();
			myOutput.close();
			myInput.close();
		} catch (Exception e) {
			throw new DroidnateException("Droidnate Error :: Helper", e);
		}
	}

	/**
	 * Indicates the main package name of the project.
	 * @param defaultPackage the new main package name of the project
	 */
	public static void setDefaultPackage(String defaultPackage) {
		DatabaseHelper.defaultPackage = defaultPackage;
	}

	/**
	 * Indicates the database name being used.
	 * @param databaseName the new database name
	 */
	public static void setDatabaseName(String databaseName) {
		DatabaseHelper.databaseName = databaseName;
	}

}