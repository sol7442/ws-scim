package com.wowsanta.scim.attribute;

import java.time.Instant;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.schema.SCIMAttributeSchema;
import com.wowsanta.scim.schema.SCIMDefinitions;

public class DefaultAttributeFactory {
	public static Attribute createAttribute(SCIMAttributeSchema attributeSchema, AbstractAttribute attribute) throws SCIMException {

		attribute.setMutability(attributeSchema.getMutability());
		attribute.setRequired(attributeSchema.getRequired());
		attribute.setReturned(attributeSchema.getReturned());
		attribute.setCaseExact(attributeSchema.getCaseExact());
		attribute.setMultiValued(attributeSchema.getMultiValued());
		attribute.setDescription(attributeSchema.getDescription());
		attribute.setUniqueness(attributeSchema.getUniqueness());
		attribute.setURI(attributeSchema.getUri());

		try {
			if (attribute instanceof SimpleAttribute) {
				return createSimpleAttribute(attributeSchema, (SimpleAttribute) attribute);
			} else {
				attribute.setType(attributeSchema.getType());
			}
			return attribute;
		} catch (SCIMException e) {
			String error = "Unknown attribute schema.";
			throw new SCIMException(error);
		}
	}

	protected static SimpleAttribute createSimpleAttribute(SCIMAttributeSchema attributeSchema,	SimpleAttribute simpleAttribute) throws SCIMException {
		if (simpleAttribute.getValue() != null) {
			if (isAttributeDataTypeValid(simpleAttribute.getValue(), attributeSchema.getType())) {
				simpleAttribute.setType(attributeSchema.getType());
				return simpleAttribute;
			}
		}
		return simpleAttribute;
	}

	protected static boolean isAttributeDataTypeValid(Object attributeValue, SCIMDefinitions.DataType attributeDataType) {
		switch (attributeDataType) {
		case STRING:
			return attributeValue instanceof String;
		case BOOLEAN:
			return attributeValue instanceof Boolean;
		case DECIMAL:
			return attributeValue instanceof Double;
		case INTEGER:
			return attributeValue instanceof Integer;
		case DATE_TIME:
			return attributeValue instanceof Instant;
		case BINARY:
			return attributeValue instanceof Byte[];
		case REFERENCE:
			return attributeValue instanceof String;
		case COMPLEX:
			return attributeValue instanceof String;
		}
		return false;
	}

}
