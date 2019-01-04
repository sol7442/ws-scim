package com.wession.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

public class KryoUtil {

	public static Logger systemLog = new WessionLog().getSystemLog();
	
	public static synchronized void writeFiles(File file,  JSONArray jsonarry) throws IOException {
		Kryo kryo = new Kryo();
		kryo.register(String.class);
		
		String saved = jsonarry.toJSONString();
		systemLog.info("* SAVED RESOURCES - " + file.getName() + ":"+ jsonarry.size());
		
		Output output = new Output(new FileOutputStream(file));
		kryo.writeClassAndObject(output, saved);
		output.close();
	}

	public static synchronized JSONArray readFiles(File file) throws IOException  {
		Kryo kryo = new Kryo();
		kryo.register(String.class);
		
		Input input = new Input(new FileInputStream(file));
		String saved = (String) kryo.readClassAndObject(input);
		JSONArray retobj = (JSONArray) JSONValue.parse(saved);

		systemLog.info("* LOADED RESOURCES - " + file.getName() + ":"+ retobj.size());

		input.close();
		return retobj;
	}
	
}
