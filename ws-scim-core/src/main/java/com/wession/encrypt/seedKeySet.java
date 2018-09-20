package com.wession.encrypt;

import java.util.HashMap;
import java.util.Vector;

public class seedKeySet {
	public static final HashMap<String, String> map = new HashMap<String, String>();
	public static final Vector<String> navi = new Vector <String>();
	static {
		// 향후 여기 부분을 어렵게 만들어서 관리해야 함
		for (int i=0; i<100; i++) {
			String makeKey = Integer.toString(1001+i);
			String makeSeedKey = "seedkey" + (20130001+i) + "%@";
			
			map.put(makeKey, makeSeedKey);
			navi.add(makeKey);
		}
	}
	
	public static String findKey(int idx) {
		if ((idx>=0) && (idx<100))  {
			return map.get(navi.get(idx));
		} else {
			System.out.println("key index is out of boundary");
			return null;
		}
	}
	
}
