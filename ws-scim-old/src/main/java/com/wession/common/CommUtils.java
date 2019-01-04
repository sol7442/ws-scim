package com.wession.common;

import java.io.FileWriter;
import java.io.IOException;

public class CommUtils {

	public static Object NVL(Object o) {
		return o == null ? "" : o;
	}

	public static boolean isEmpty(Object o) {
		return o == null || o.equals("") ? true : false;
	}

	public static boolean isNotEmpty(Object o) {
		return !isEmpty(o);
	}

	public static void saveFile(String str, String save_path) {
		FileWriter file = null;
		try {
			file = new FileWriter(save_path);
			file.write(str);
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
