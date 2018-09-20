package com.wession.scim.resource;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

import com.wession.common.DateUtil;
import com.wession.common.ScimUtils;
import com.wession.encrypt.encryptSHA256;
import com.wession.scim.intf.schemas_name;

import net.minidev.json.JSONObject;

public class Meta extends JsonModel  implements Serializable, schemas_name {
	
	public Meta(String resourceType) {
		obj = new JSONObject();
		obj.put(_meta_resource_type, resourceType);
		// UTC 형식, ISO8601 표준, Java8 지원 API
		obj.put(_meta_created, DateUtil.getDateTimeUTC());
		obj.put(_meta_version, "W/\"abcdef1234567890-000\"");
	}
	
	public Meta(JSONObject meta) {
		obj = meta;
	}
	
	public void setLocation(String id) {
		obj.put(_meta_location, ScimUtils.makeRef(getRefer(), id, obj.getAsString(_meta_resource_type)));
	}
	
	public void setLastModify(JSONObject target) {
		obj.put(_meta_last_modified, ZonedDateTime.now( ZoneOffset.UTC ).withNano( 0 ).toString());
		setVersion(target);
	}
	
	private void setVersion(JSONObject target) {
		if (target == null) return;
		
		String version = obj.getAsString(_meta_version);
		int startpoint = version.indexOf("-");
		int endpoint = version.indexOf("\"", startpoint);
		int Iver = 0;
		if (startpoint > 0 && startpoint < endpoint) {
			String ver = version.substring(startpoint+1, endpoint);
			Iver = Integer.parseInt(ver) + 1;
		}
		
		String json = target.toJSONString();
		String hash = halfHash(json);
		
		obj.put(_meta_version, "W/\"" + hash + String.format("-%03d\"", Iver));
	}
	

	private String halfHash(String str) {

		String hash = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte byteData[] = md.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length/2; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
			hash = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			hash = null;
		}
		return hash;
	}


}
