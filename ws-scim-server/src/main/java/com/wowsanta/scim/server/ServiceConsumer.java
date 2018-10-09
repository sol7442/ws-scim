package com.wowsanta.scim.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.wowsanta.scim.SystemManager;
import com.wowsanta.scim.resource.Target;

public class ServiceConsumer {

	private static ServiceConsumer instance;
	private Map<String,Target> targets = new HashMap<String, Target>();
	
	
	public static ServiceConsumer getInstance() {
		if(instance == null) {
			instance = new ServiceConsumer();
			
		}
		return instance;
	}
	public static ServiceConsumer load(String file_name) throws FileNotFoundException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonReader reader = new JsonReader(new FileReader(file_name));
		
		ServiceConsumer sc = gson.fromJson(reader,ServiceConsumer.class);
		SystemManager.getInstance().setServiceConsumer(sc);
		
		return sc; 
	}
	
	public void save(String file_name) throws IOException {
	
		OutputStreamWriter writer = new OutputStreamWriter(
				new FileOutputStream(
						new File(file_name)),StandardCharsets.UTF_8);
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		gson.toJson(this,writer);
		writer.flush();
		writer.close();
	}

	public String toJson() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}

	public Map<String,Target> getTargets() {
		return targets;
	}

	public void setTargets(Map<String,Target> targets) {
		this.targets = targets;
	}
	
	public Target getTarget(String name) {
		return this.targets.get(name);
	}
	public void putTarget(Target target) {
		this.targets.put(target.getName(),target);
	}
}
