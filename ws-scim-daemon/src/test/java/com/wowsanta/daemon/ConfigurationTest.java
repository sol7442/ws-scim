package com.wowsanta.daemon;


import org.junit.Test;
import com.wowsata.util.json.JsonException;

public class ConfigurationTest {

	final String config_file_name = "../config/daemon_config.json";
	
	@Test
	public void config_save_test() {
		try {
			DaemonConfiguration.getInstance().save(config_file_name);
		} catch (JsonException e) {
			e.printStackTrace();
		}
		
	}
//	@Test
	public void config_load_file_test() {
		
		try {
			System.out.println(DaemonConfiguration.loadFromFile(config_file_name).tojson(true));
		} catch (JsonException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void config_load_string_test() {
		try {
			//System.out.println();
			DaemonConfiguration config = DaemonConfiguration.loadFromFile(config_file_name);
			
			System.out.println(config.tojson(false));
			
			System.out.println(DaemonConfiguration.loadFromString(config.tojson(false)).tojson(true));
			
		} catch (JsonException e) {
			e.printStackTrace();
		}
	}
}
