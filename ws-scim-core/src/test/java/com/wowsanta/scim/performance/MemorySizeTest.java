package com.wowsanta.scim.performance;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.junit.Test;

import com.wowsanta.scim.resource.User;
import com.wowsanta.scim.util.ObjectSizeFetcher;

public class MemorySizeTest {

	@Test
	public void object_memory_size() {
		User user = new User();
		user.setId("--------");
		for(int j=0;j<100;j++) {
			user.setAttribute(String.valueOf(j), String.valueOf(new Random().nextLong()));
		}
		
		System.err.println("user :  " + ObjectSizeFetcher.getObjectSize(user));
	}
	//@Test
	public void map_memory_size() {
		ConcurrentMap<String,Object> map = new ConcurrentHashMap<String, Object>();
		
		System.gc();
		System.out.println("full gc");
		long a = Runtime.getRuntime().freeMemory();
		for(int i=0; i<10000;i++) {
			User user = new User();
			user.setId(String.valueOf(i));
			for(int j=0;j<100;j++) {
				user.setAttribute(String.valueOf(j), String.valueOf(new Random().nextLong()));
			}
			map.putIfAbsent(String.valueOf(i),user);
		}
		long b = Runtime.getRuntime().freeMemory();
		
		System.out.println("1 memory size : " + a);
		System.out.println("2 memory size : " + b);
		System.out.println("used memory size : " + (a - b));
	}
}
