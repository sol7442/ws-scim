package com.wowsanta.scim.obj;


public class SCIMAttribute {
	  private final AttributeDescriptor attributeDescriptor;
	  private final SCIMAttributeValue[] values;

	  public SCIMAttribute(AttributeDescriptor attributeDescriptor, SCIMAttributeValue[] values) {
		  this.attributeDescriptor = attributeDescriptor;
		  this.values = values;
	  }

	public AttributeDescriptor getAttributeDescriptor() {
		return attributeDescriptor;
	}

	public SCIMAttributeValue[] getValues() {
		return values;
	}
}
