package com.wowsanta.scim.repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.wowsanta.scim.schema.SCIMDefinitions;

/**
 * This defines the attributes schema as in SCIM Spec.
 */

public class AttributeSchema  {

	private transient static Logger logger = LoggerFactory.getLogger(AttributeSchema.class);
		
	private String uri;
	private String name;
	private SCIMDefinitions.DataType type;
	private Boolean multiValued;
	private String description;
	private Boolean required;
	private Boolean caseExact;
	private String defaultValue;
	private DataMapper dataMapper;
	
	private ResourceColumn resourceColumn;
	private SCIMDefinitions.Mutability mutability;
	private SCIMDefinitions.Returned returned;
	private SCIMDefinitions.Uniqueness uniqueness;
	private ArrayList<AttributeSchema> subAttributes;
	private ArrayList<String> canonicalValues;
	private ArrayList<SCIMDefinitions.ReferenceType> referenceTypes;

	public AttributeSchema(AttributeSchema attributeSchema) {
		this.uri 			= attributeSchema.uri;
		this.name 			= attributeSchema.name;
		this.type 			= attributeSchema.type;
		this.multiValued 	= attributeSchema.multiValued;
		this.description 	= attributeSchema.description;
		this.required 		= attributeSchema.required;
		this.caseExact 		= attributeSchema.caseExact;
		this.mutability 	= attributeSchema.mutability;
		this.returned 		= attributeSchema.returned;
		this.uniqueness 	= attributeSchema.uniqueness;
		this.subAttributes 	= attributeSchema.subAttributes;
		this.canonicalValues= attributeSchema.canonicalValues;
		this.referenceTypes = attributeSchema.referenceTypes;
	}
	public AttributeSchema(String uri, String name, SCIMDefinitions.DataType type, Boolean multiValued,
			String description, Boolean required, Boolean caseExact, SCIMDefinitions.Mutability mutability,
			SCIMDefinitions.Returned returned, SCIMDefinitions.Uniqueness uniqueness, ArrayList<String> canonicalValues,
			ArrayList<SCIMDefinitions.ReferenceType> referenceTypes, ArrayList<AttributeSchema> subAttributes) {
		this.uri = uri;
		this.name = name;
		this.type = type;
		this.multiValued = multiValued;
		this.description = description;
		this.required = required;
		this.caseExact = caseExact;
		this.mutability = mutability;
		this.returned = returned;
		this.uniqueness = uniqueness;
		this.subAttributes = subAttributes;
		this.canonicalValues = canonicalValues;
		this.referenceTypes = referenceTypes;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SCIMDefinitions.DataType getType() {
		return type;
	}

	public void setType(SCIMDefinitions.DataType type) {
		this.type = type;
	}

	public Boolean getMultiValued() {
		return multiValued;
	}

	public void setMultiValued(Boolean multiValued) {
		this.multiValued = multiValued;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public Boolean getCaseExact() {
		return caseExact;
	}

	public void setCaseExact(Boolean caseExact) {
		this.caseExact = caseExact;
	}

	public SCIMDefinitions.Mutability getMutability() {
		return mutability;
	}

	public void setMutability(SCIMDefinitions.Mutability mutability) {
		this.mutability = mutability;
	}

	public SCIMDefinitions.Returned getReturned() {
		return returned;
	}

	public void setReturned(SCIMDefinitions.Returned returned) {
		this.returned = returned;
	}

	public SCIMDefinitions.Uniqueness getUniqueness() {
		return uniqueness;
	}

	public void setUniqueness(SCIMDefinitions.Uniqueness uniqueness) {
		this.uniqueness = uniqueness;
	}

	public ArrayList<AttributeSchema> getSubAttributes() {
		return subAttributes;
	}

	public void setSubAttributes(ArrayList<AttributeSchema> subAttributes) {
		this.subAttributes = subAttributes;
	}
	public AttributeSchema findSubAttribute(String name) {
		for (AttributeSchema attribute : this.subAttributes) {
			if(attribute.getName().equals(name)) {
				return attribute;
			}
		}
		return null;
	}
	public ArrayList<String> getCanonicalValues() {
		return canonicalValues;
	}

	public void setCanonicalValues(ArrayList<String> canonicalValues) {
		this.canonicalValues = canonicalValues;
	}

	public ArrayList<SCIMDefinitions.ReferenceType> getReferenceTypes() {
		return referenceTypes;
	}

	public void setReferenceTypes(ArrayList<SCIMDefinitions.ReferenceType> referenceTypes) {
		this.referenceTypes = referenceTypes;
	}

	public void save(String file_name) throws IOException {
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(new File(file_name)),
				StandardCharsets.UTF_8);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		gson.toJson(this, writer);
		writer.flush();
		writer.close();
	}

	public String toJson() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}

	public static Object load(String file_name) throws FileNotFoundException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonReader reader = new JsonReader(new FileReader(file_name));
		return gson.fromJson(reader, AttributeSchema.class);
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public void setResourceColumn(ResourceColumn column) {
		this.resourceColumn = column;
		logger.info("set {}-{}", this.name, column.getName() );
	}
	public ResourceColumn getResourceColumn() {
		return this.resourceColumn;
	}
	public DataMapper getDataMapper() {
		return dataMapper;
	}
	public void setDataMapper(DataMapper dataMapper) {
		this.dataMapper = dataMapper;
	}
}
