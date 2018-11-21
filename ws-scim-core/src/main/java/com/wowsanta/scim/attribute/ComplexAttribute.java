package com.wowsanta.scim.attribute;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.schema.SCIMAttributeSchema;
import com.wowsanta.scim.schema.SCIMDefinitions;

public class ComplexAttribute extends AbstractAttribute {
	private static final long serialVersionUID = -5394309004806950570L;
	protected Map<String, Attribute> subAttributesList = new HashMap<String, Attribute>();

	public ComplexAttribute(SCIMAttributeSchema schema) {
		super(schema);
	}

	public Map<String, Attribute> getSubAttributesList() {
		return subAttributesList;
	}
	public void setSubAttributesList(Map<String, Attribute> subAttributesList) {
		this.subAttributesList = subAttributesList;
	}

	public void addSubAttribute(Attribute subAttribute)  {
		subAttributesList.put(subAttribute.getName(), subAttribute);
	}
	public Attribute getSubAttribute(String attributeName)  {
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
	
	public boolean isNull() {
		return this.subAttributesList.size() == 0 ;
	}
	
	
	@Override
	public JsonElement encode(boolean nullable) {
		JsonObject json = new JsonObject();
		for (Attribute value : this.subAttributesList.values()) {
			json.add(value.getName(),value.encode(nullable));
		}
		return json;
	}

	public static Attribute create(SCIMAttributeSchema attr_schema) {
		ComplexAttribute attribute = new ComplexAttribute(attr_schema);
		for(SCIMAttributeSchema sub_schema :  attr_schema.getSubAttributes()) {
			Attribute subAttribute = null;
			if(sub_schema.getMultiValued()) {
				subAttribute = MultiValuedAttribute.create(sub_schema);
			}else {
				if(sub_schema.getType() == SCIMDefinitions.DataType.COMPLEX){
					subAttribute = ComplexAttribute.create(sub_schema);
				}else {
					subAttribute = SimpleAttribute.create(sub_schema);
				}
			}
			attribute.addSubAttribute(subAttribute);
		}
		
		return attribute;
	}
}
