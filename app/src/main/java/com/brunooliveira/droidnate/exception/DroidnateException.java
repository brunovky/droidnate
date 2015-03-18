package com.brunooliveira.droidnate.exception;

import android.util.Log;

import com.brunooliveira.droidnate.dao.DAO;
import com.brunooliveira.droidnate.helper.DatabaseHelper;

/**
 * Identifies any errors that come from opening the database ({@link DatabaseHelper} class) or any of the basic operations (insert, update, delete or select) from the database ({@link DAO} class).
 * <br><br>
 * 
 * @author	Bruno Oliveira
 * @since	v.1
 *
 */
public class DroidnateException extends Exception {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a new {@link DroidnateException} with the specified detail message and cause.
	 * @param msg the detail message (which is saved for later retrieval by the {@code Throwable.getMessage()} method).
	 * @param throwable the cause (which is saved for later retrieval by the {@code Throwable.getCause()} method). (A {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public DroidnateException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

	/**
	 * Displays an error message through the {@link android.util.Log} using the detailed message and the cause, if not {@code null}.
	 */
	public void showLog() {
		Log.e(getMessage(), getCause() != null ? getCause().getLocalizedMessage() : getLocalizedMessage());
	}

}