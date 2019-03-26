package com.wowsanta.scim.repository;



import com.google.gson.JsonObject;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.schema.SCIMResourceTypeSchema;

public interface SCIMResourceRepository {
	public void setUserSchema(SCIMResourceTypeSchema userSchema);
	public void setGroupSchema(SCIMResourceTypeSchema groupSchema) throws SCIMException;
	
}
