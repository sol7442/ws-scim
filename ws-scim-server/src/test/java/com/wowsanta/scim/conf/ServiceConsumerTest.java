package com.wowsanta.scim.conf;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import com.wowsanta.scim.resource.Target;
import com.wowsanta.scim.service.ServiceConsumer;


public class ServiceConsumerTest {
	private final String config_file_name = "../config/service-consumer.json";
	
	@Test
	public void make() {
		ServiceConsumer targets = new ServiceConsumer();
		Target t1 = new Target();
		t1.setName("GroupWare");
		t1.setName("GW");
		
		Target t2 = new Target();
		t2.setName("그룹포탈");
		t2.setCode("GP");
		Target t3 = new Target();
		t3.setName("재경시스템");
		t3.setCode("JLK");
		Target t31 = new Target();
		t31.setName("재무시스템");
		t31.setCode("JK1");
		Target t32 = new Target();
		t32.setName("경영시스템");
		t32.setCode("JK2");
		
		
		targets.putTarget(t1);
		targets.putTarget(t2);
		targets.putTarget(t31);
		targets.putTarget(t32);
		try {
			System.out.println(targets);
			targets.save(this.config_file_name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void load() {
		try {
			ServiceConsumer targets = ServiceConsumer.load(this.config_file_name);
			System.out.println(targets.toJson());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
