package com.brunooliveira.droidnate.helper;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.brunooliveira.droidnate.exception.DroidnateException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	private static String databaseName = "";
	private static String databasePath = "";
    private static int databaseVersion = 1;
    private static List<String> sqlToBeExecuted;
    private static List<String> sqlToBeUpdated;
	private Context context;

    private static final String DATA_DIR = "/data/data/";
    private static final String DB_DIR = "/databases/";

	/**
	 * Create a helper object to create, open, and/or manage a database.
	 * @param context to use to open or create the database
	 */
	public DatabaseHelper(Context context) {
        super(context, databasePath = DATA_DIR + context.getPackageName()
				+ DB_DIR + databaseName, null, databaseVersion);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
        if (sqlToBeExecuted != null) {
            for (String sql : sqlToBeExecuted) {
                try {
                    Log.i(DatabaseHelper.class.getSimpleName(), "Creating table [" + sql.split(" TABLE ")[1] + "].");
                    db.execSQL(sql);
                    Log.i(DatabaseHelper.class.getSimpleName(), sql);
                } catch (SQLException e) {
                    DroidnateException exception = new DroidnateException(DatabaseHelper.class.getSimpleName(), e);
                    exception.showLog();
                }
            }
        }
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            if (sqlToBeUpdated != null) {
                for (String sql : sqlToBeUpdated) {
                    try {
                        Log.i(DatabaseHelper.class.getSimpleName(), (sql.contains("CREATE TABLE") ? "Creating" : "Updating") + " table [" + sql.split(" TABLE ")[1] + "].");
                        db.execSQL(sql);
                        Log.i(DatabaseHelper.class.getSimpleName(), sql);
                    } catch (SQLException e) {
                        DroidnateException exception = new DroidnateException(DatabaseHelper.class.getSimpleName(), e);
                        exception.showLog();
                    }
                }
            }
        }
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
			e.showLog();
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
            Log.i(DatabaseHelper.class.getSimpleName(), "Copying database from assets folder to [" + databasePath + "]");
			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);
			}
			myOutput.flush();
			myOutput.close();
			myInput.close();
		} catch (FileNotFoundException e) {
			return;
		} catch (Exception e) {
            throw new DroidnateException(DatabaseHelper.class.getSimpleName(), e);
        }
	}

	/**
	 * Indicates the database name being used.
	 * @param databaseName the new database name
	 */
	public static void setDatabaseName(String databaseName) {
		DatabaseHelper.databaseName = databaseName;
	}

    public static void setDatabaseVersion(int databaseVersion) {
        DatabaseHelper.databaseVersion = databaseVersion;
    }

    public static void addSqlToBeExecuted(String sql) {
        if (sqlToBeExecuted == null) sqlToBeExecuted = new ArrayList<>();
        sqlToBeExecuted.add(sql);
    }

    public static void addSqlToBeUpdated(String sql) {
        if (sqlToBeUpdated == null) sqlToBeUpdated = new ArrayList<>();
        sqlToBeUpdated.add(sql);
    }

}