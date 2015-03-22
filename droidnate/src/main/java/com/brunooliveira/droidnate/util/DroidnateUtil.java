package com.brunooliveira.droidnate.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public final class DroidnateUtil {

	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
                reader.close();
                is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	public static String getParams(Map<String, String> params) {
		StringBuilder sb = new StringBuilder();
		for (String key : params.keySet()) {
			sb.append(key + "=" + params.get(key) + "&");
		}
		String str = sb.toString();
		return str.length() > 0 ? str.substring(0, str.lastIndexOf("&")) : "";
	}

}