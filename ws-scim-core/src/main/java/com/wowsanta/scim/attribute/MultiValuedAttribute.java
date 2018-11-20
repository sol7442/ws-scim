
package com.wowsanta.scim.attribute;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.schema.SCIMAttributeSchema;
import com.wowsanta.scim.schema.SCIMDefinitions;

public class MultiValuedAttribute extends AbstractAttribute {
	private static final long serialVersionUID = 409670857717798949L;
	protected List<Attribute> attributeValues = new ArrayList<Attribute>();
    protected List<Object> attributePrimitiveValues = new ArrayList<Object>();
    
	public static Attribute create(SCIMAttributeSchema attr_schema) {
		MultiValuedAttribute attribute = new MultiValuedAttribute(attr_schema.getName());
		if(attr_schema.getType() == SCIMDefinitions.DataType.COMPLEX) {
			for (SCIMAttributeSchema sub_attri_schema : attr_schema.getSubAttributes()) {
				Attribute sub_attribute = null;
				if(sub_attri_schema.getMultiValued()) {
					sub_attribute = MultiValuedAttribute.create(sub_attri_schema);
				}else {
					//System.out.println(attr_schema.getMultiValued() + "..vvv..." + sub_attri_schema.getType());
					if(sub_attri_schema.getType() == SCIMDefinitions.DataType.COMPLEX) {
						
					}else {
						sub_attribute = SimpleAttribute.create(sub_attri_schema);
					}
				}
				if(sub_attribute != null) {
					attribute.addAttribute(sub_attribute);
				}
			}
			
//			System.out.println("o00...." + attr_schema.getSubAttributes());
			
//			for (SCIMAttributeSchema attr_schema : schema.getAttributes().values()) {
//				Attribute attribute = null;
//				if(attr_schema.getMultiValued()){
//					
//				}
		}else {
			System.out.println("obb...." + attr_schema.getSubAttributes());
		}
		return attribute;
	}
	
    public MultiValuedAttribute(String attributeName, List<Attribute> attributeValues) {
        this.name = attributeName;
        this.attributeValues = attributeValues;
    }
    public  MultiValuedAttribute(){}

    public MultiValuedAttribute(String attributeName) {
        this.name = attributeName;
    }

    public List<Attribute> getAttributeValues() {
        return attributeValues;
    }

    public void addAttribute(Attribute attribute) {
        this.attributeValues.add(attribute);
    }
    
    public void setAttributeValues(List<Attribute> attributeValues) {
        this.attributeValues = attributeValues;
    }
    public Attribute getSubAttribute(String attributeName) throws SCIMException {
        throw new SCIMException("getSubAttribute method not supported by MultiValuedAttribute.");
    }

    public void deleteSubAttributes() throws SCIMException {
        attributeValues.clear();
    }
    public void deletePrimitiveValues() throws SCIMException {
        attributePrimitiveValues.clear();
    }
    public void setComplexValueWithSetOfSubAttributes(Map<String, Attribute> subAttributes) {
        ComplexAttribute complexValue = new ComplexAttribute();
        complexValue.setSubAttributesList(subAttributes);
        this.attributeValues.add(complexValue);
    }
    public List<Object> getAttributePrimitiveValues() {
        return attributePrimitiveValues;
    }
    public void addAttributePrimitive(Object object) {
    	this.attributePrimitiveValues.add(object);
    }
    public void setAttributePrimitiveValues(List<Object> attributePrimitiveValues) {
        this.attributePrimitiveValues = attributePrimitiveValues;
    }
    public void setAttributeValue(Attribute attributeValue) {
        attributeValues.add(attributeValue);
    }
    public void setAttributePrimitiveValue(Object obj) {
        attributePrimitiveValues.add(obj);
    }
    
	@Override
	public JsonElement encode(boolean nullable) {
		JsonArray array = new JsonArray();
//		for (Attribute attribute : this.attributeValues) {
//			JsonObject obj = new JsonObject();
//			attribute.encode(obj,nullable);
//			array.add(obj);
//		}
//		
//		for(Object obj : this.attributePrimitiveValues){
//			array.add(obj.toString());
//		}
//		root.add(this.name,array);
		return array;
	}
}
