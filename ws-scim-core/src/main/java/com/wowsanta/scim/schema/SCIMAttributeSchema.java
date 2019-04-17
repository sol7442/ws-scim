/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wowsanta.scim.schema;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

/**
 * This defines the attributes schema as in SCIM Spec.
 */

public class SCIMAttributeSchema implements Serializable {

	private static final long serialVersionUID = 5576548761169044638L;

	private String uri;
	private String name;
	private SCIMDefinitions.DataType type;
	private Boolean multiValued;
	private String description;
	private Boolean required;
	private Boolean caseExact;
	private SCIMDefinitions.Mutability mutability;
	private SCIMDefinitions.Returned returned;
	private SCIMDefinitions.Uniqueness uniqueness;
	private ArrayList<SCIMAttributeSchema> subAttributes;
	private ArrayList<String> canonicalValues;
	private ArrayList<SCIMDefinitions.ReferenceType> referenceTypes;

	public SCIMAttributeSchema(String uri, String name, SCIMDefinitions.DataType type, Boolean multiValued,
			String description, Boolean required, Boolean caseExact, SCIMDefinitions.Mutability mutability,
			SCIMDefinitions.Returned returned, SCIMDefinitions.Uniqueness uniqueness, ArrayList<String> canonicalValues,
			ArrayList<SCIMDefinitions.ReferenceType> referenceTypes, ArrayList<SCIMAttributeSchema> subAttributes) {
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

	public ArrayList<SCIMAttributeSchema> getSubAttributes() {
		return subAttributes;
	}

	public void setSubAttributes(ArrayList<SCIMAttributeSchema> subAttributes) {
		this.subAttributes = subAttributes;
	}
	public SCIMAttributeSchema findSubAttribute(String name) {
		for (SCIMAttributeSchema attribute : this.subAttributes) {
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
		return gson.fromJson(reader, SCIMAttributeSchema.class);
	}
}
