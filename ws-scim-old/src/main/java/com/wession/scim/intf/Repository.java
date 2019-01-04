/**
 * 
 */
package com.wession.scim.intf;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.minidev.json.JSONArray;

/**
 * @author EJLee
 *
 */
public abstract class Repository implements IRepository {
	private static Repository repo;
	private JSONArray users;
	private JSONArray groups;
	
	Lock reentrantLock = new ReentrantLock();
	
	private static class Singleton {
		private static final Repository instance = null;
	}
	
	public Repository () {
		users = new JSONArray();
		groups = new JSONArray();
	}
	
	public static synchronized  Repository getInstance() {
		return Singleton.instance;
	}

	public int getSize(String resource) {
		return 0;
	}
	
}
