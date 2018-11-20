
package com.wowsanta.scim.attribute;

import java.time.Instant;
import java.util.Date;
import java.util.Set;

import org.codehaus.janino.Java.Instanceof;

import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.schema.SCIMAttributeSchema;
import com.wowsanta.scim.schema.SCIMDefinitions;

import net.minidev.json.JSONObject;

public class SimpleAttribute extends AbstractAttribute {
	private static final long serialVersionUID = 3307670169212954126L;
	private Object value;

	public SimpleAttribute(String attributeName) {
		this.name = attributeName;
	}
	
	public SimpleAttribute(String attributeName, Object value) {
		this.name = attributeName;
		this.value = value;
	}

	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
	public Attribute getSubAttribute(String attributeName){
		return null;
	}
	public void deleteSubAttributes(){
	}

	public String getStringValue() throws SCIMException{
		if (this.type.equals(SCIMDefinitions.DataType.STRING)) {
			return (String) value;
		} else {
			throw new SCIMException("Mismatch in requested data type");
		}
	}
	public Date getDateValue() throws SCIMException {
		Instant instant = getInstantValue();
		return instant != null ? new Date(instant.toEpochMilli()) : null;
	}
	public Instant getInstantValue() throws SCIMException {
		if (this.type.equals(SCIMDefinitions.DataType.DATE_TIME)) {
			return (Instant) this.value;
		} else {
			throw new SCIMException("Datatype doesn\'t match the datatype of the attribute value");
		}
	}

	public Boolean getBooleanValue() throws SCIMException {
		if (this.type.equals(SCIMDefinitions.DataType.BOOLEAN)) {
			return (Boolean) this.value;
		} else {
			throw new SCIMException("Datatype doesn\'t match the datatype of the attribute value");
		}
	}
	public void updateValue(Object value) throws SCIMException {
		this.value = value;
	}
	
	public String toJson() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\"");
		buffer.append(this.name);
		buffer.append("\"");
		buffer.append(":");
		buffer.append("\"");
		buffer.append(this.value);
		buffer.append("\"");
		return this.name + ":" + this.value;
	}
	
	@Override
	public JsonElement encode(boolean nullable) {
//		if(this.value != null) {
//			if (this.value instanceof Instant) {
//				Instant time_value = (Instant) this.value;
//				root.addProperty(this.name, time_value.toString());
//			}else {
//				root.addProperty(this.name, this.value.toString());
//			}	
//		}else {
//			if(nullable) {
//				root.addProperty(this.name, "");
//			}
//		}
		
		JsonObject json = new JsonObject();
		if(nullable) {
			json.addProperty(this.name, "");
		}
		return json;
	}

	public static Attribute create(SCIMAttributeSchema attr_schema) {
		SimpleAttribute attribute = new SimpleAttribute(attr_schema.getName());
		return attribute;
	}
}
