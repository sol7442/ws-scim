package com.wession.scim.test;

import java.util.Iterator;
import net.minidev.json.JSONObject;

public class ThreadSafeTest {
	public static void main(String[] args) throws InterruptedException {

		for (int i = 0; i<100; i++) {
			
		Thread search = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				SignletoneTest ss = SignletoneTest.getInstance();
				Iterator itor = ss.getIterator();
				
				for (int i=0; i<ss.getSize(); i++) {
					JSONObject json = (JSONObject) ss.get(i);
//					System.out.println("[" + i + "][" + json.getAsNumber("id") + "]");
					try {
						Thread.sleep(0);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println("END search : " + ss.getSize());
			}

		});
		
		Thread search2 = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				SignletoneTest ss = SignletoneTest.getInstance();
				Iterator itor = ss.getIterator();
				
				while (itor.hasNext()) {
					JSONObject json = (JSONObject) itor.next();
//					System.out.println("[" + json.getAsNumber("id") + "]");
					try {
						Thread.sleep(0);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

		});

		Thread add = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				SignletoneTest ss = SignletoneTest.getInstance();
				int i = 0;
				while (i < 100) {
					JSONObject json = new JSONObject();
					json.put("id", System.currentTimeMillis());
					ss.addArray(json);
					System.out.print(".");
					i++;
					try {
						Thread.sleep(0);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				System.out.println("END add : " + ss.getSize());
			}

		});
		
		Thread account = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Account ac = new Account();
				ac.main(null);
			}

		});
		System.out.println("add start ===================== ");
		
		
			add.start();
			search.start();
			account.start();
		}
		System.out.println("1st search start ===================== ");

	}
}
