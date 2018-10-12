package com.wowsanta.scim.conf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class JsonObject {
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
}
