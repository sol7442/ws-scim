package com.wession.scim;

import java.util.Vector; 

import com.wession.scim.intf.schemas_name;

public class Const implements schemas_name {

	public final static String schemas_v20_user = "urn:ietf:params:scim:schemas:core:2.0:User";
	public final static String schemas_v20_user_ext = "urn:ietf:params:scim:schemas:extension:enterprise:2.0:User";
	public final static String schemas_v20_group ="urn:ietf:params:scim:schemas:core:2.0:Group";
	public final static String schemas_v20_list_response ="urn:ietf:params:scim:api:messages:2.0:ListResponse";
	public final static String schemas_v20_search_request ="urn:ietf:params:scim:api:messages:2.0:SearchRequest";
	public final static String schemas_v20_patch ="urn:ietf:params:scim:api:messages:2.0:PatchOp";
	public final static String schemas_v20_error ="urn:ietf:params:scim:api:messages:2.0:Error";
	public final static String schemas_v20_bulk_request ="urn:ietf:params:scim:api:messages:2.0:BulkRequest";
	public final static String schemas_v20_bulk_response ="urn:ietf:params:scim:api:messages:2.0:BulkResponse";
	
	public static Vector <String> OPERATOR = new Vector<String>();
	public static Vector <String> UserAttrName = new Vector<String>();
	static {
		OPERATOR.add("eq");
		OPERATOR.add("ne");
		OPERATOR.add("co");
		OPERATOR.add("sw");
		OPERATOR.add("ew");
		OPERATOR.add("gt");
		OPERATOR.add("ge");
		OPERATOR.add("lt");
		OPERATOR.add("le");
		OPERATOR.add("pr"); // present - has value
		OPERATOR.add("np"); // absence - has not value
		OPERATOR.add("nc"); // not contains
		OPERATOR.add("ns"); // not startwith
		OPERATOR.add("nw"); // not endwith
		
		
		UserAttrName.add("userName");
		UserAttrName.add("externalId");
		UserAttrName.add("displayName");
		UserAttrName.add("userType");
		UserAttrName.add("title");
		UserAttrName.add("active");
		UserAttrName.add("groups");
		UserAttrName.add("emails");
		UserAttrName.add("display");
		UserAttrName.add("members");
		UserAttrName.add("type");
		UserAttrName.add("value");
	}
}
