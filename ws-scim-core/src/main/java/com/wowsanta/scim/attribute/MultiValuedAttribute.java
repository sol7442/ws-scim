
package com.wowsanta.scim.attribute;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wowsanta.scim.exception.SCIMException;

public class MultiValuedAttribute extends AbstractAttribute {
	private static final long serialVersionUID = 409670857717798949L;
	protected List<Attribute> attributeValues = new ArrayList<Attribute>();
    protected List<Object> attributePrimitiveValues = new ArrayList<Object>();
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
    public void setAttributePrimitiveValues(List<Object> attributePrimitiveValues) {
        this.attributePrimitiveValues = attributePrimitiveValues;
    }
    public void setAttributeValue(Attribute attributeValue) {
        attributeValues.add(attributeValue);
    }
    public void setAttributePrimitiveValue(Object obj) {
        attributePrimitiveValues.add(obj);
    }

}
