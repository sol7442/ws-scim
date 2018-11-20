package com.wowsanta.scim.attribute;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wowsanta.scim.exception.SCIMException;

public class ComplexAttribute extends AbstractAttribute {
	private static final long serialVersionUID = -5394309004806950570L;
	protected Map<String, Attribute> subAttributesList = new HashMap<String, Attribute>();

	public ComplexAttribute(String name) {
		this.name = name;
	}

	public ComplexAttribute() {
		
	}
	public Map<String, Attribute> getSubAttributesList() {
		return subAttributesList;
	}
	public void setSubAttributesList(Map<String, Attribute> subAttributesList) {
		this.subAttributesList = subAttributesList;
	}

	public Attribute getSubAttribute(String attributeName) throws SCIMException {
		if (subAttributesList.containsKey(attributeName)) {
			return subAttributesList.get(attributeName);
		} else {
			return null;
		}
	}
	public void deleteSubAttributes() throws SCIMException {
		subAttributesList.clear();
	}
	public void removeSubAttribute(String attributeName) {
		if (subAttributesList.containsKey(attributeName)) {
			subAttributesList.remove(attributeName);
		}
	}
	public boolean isSubAttributeExist(String attributeName) {
		return subAttributesList.containsKey(attributeName);
	}
	public void setSubAttribute(Attribute subAttribute) throws SCIMException {
		subAttributesList.put(subAttribute.getName(), subAttribute);
	}
	
	@Override
	public JsonElement encode(boolean nullable) {
		JsonObject json = new JsonObject();
		for (Attribute value : this.subAttributesList.values()) {
			json.add(value.getName(),value.encode(nullable));
		}
		return json;
	}
}
