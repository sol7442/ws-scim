package com.wowsanta.scim.attribute;


import java.io.Serializable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.schema.SCIMDefinitions;


public interface Attribute extends Serializable {

    public String getURI();

    public String getName();

    public SCIMDefinitions.DataType getType();

    public Boolean getMultiValued();

    public String getDescription();

    public Boolean getCaseExact();

    public SCIMDefinitions.Mutability getMutability();

    public SCIMDefinitions.Returned getReturned();

    public SCIMDefinitions.Uniqueness getUniqueness();

    public Boolean getRequired();

    public Attribute getSubAttribute(String attributeName) throws SCIMException;

    public void deleteSubAttributes() throws SCIMException;

	public JsonElement encode(boolean nullable);

}
