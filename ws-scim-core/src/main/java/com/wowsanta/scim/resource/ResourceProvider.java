/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.wowsanta.scim.resource;


import java.util.List;



import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.wowsanta.scim.exception.SCIMException;


public interface ResourceProvider
{
    String SERVLET_CONTEXT_ATTRIBUTE_KEY = "ESCIMO_PROVIDER";


    void init() throws SCIMException;
    void stop();

    JsonObject getResource( String id ) throws SCIMException;
    JsonObject getJsonSchemaById( String id );
    List<JsonObject> getJsonSchemas();
    JsonObject addResource( String jsonData ) throws SCIMException;
    void deleteResource( String id) throws SCIMException;
    JsonObject putResource( String id, String jsonData ) throws SCIMException;
    JsonObject patchResource( String id, String jsonData ) throws SCIMException;
    JsonObject search( String filter, String attributes ) throws SCIMException;
    String authenticate( String userName, String password ) throws SCIMException;
    void setAllowAuthorizedUsers( boolean allowAuthorizedUsers );
    List<String> getResourceUris();
    boolean isAllowAuthorizedUsers();
    JsonArray getAllResourceTypesSchema( String servletCtxPath );
    JsonObject getResourceTypeSchema( String servletCtxPath, String resName );
}
