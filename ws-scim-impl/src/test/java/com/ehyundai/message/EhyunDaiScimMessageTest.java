package com.ehyundai.message;


import org.junit.Test;

import com.ehyundai.object.EhyunDaiScimObjectTest;
import com.ehyundai.object.User;
import com.wowsanta.scim.message.SCIMBulkOperation;
import com.wowsanta.scim.message.SCIMBulkRequest;
import com.wowsanta.scim.resource.SCIMResouceFactory;
import com.wowsanta.scim.schema.SCIMDefinitions;

public class EhyunDaiScimMessageTest extends EhyunDaiScimObjectTest {

	static{
		SCIMResouceFactory res_factory = SCIMResouceFactory.getInstance();
		res_factory.setUserClass(User.USER_URI, User.class.getCanonicalName());
	}
	
	//@Test
	public void bulk_request_test(){
		System.out.println("create bulk request and parse >>>>>>>>>>>>>>>>>>>>>>>>>>");
		SCIMBulkRequest request = create_bulk_request();
		
		System.out.println(request.toString(true));
		System.out.println("=========================");
		System.out.println(request.toString());
		System.out.println("=========================");
		
//		SCIMBulkRequest p_request = new SCIMBulkRequest();
//		p_request.parse(request.toString());
//		System.out.println(p_request.toString(true));
	}
	
	public SCIMBulkRequest create_bulk_request(){
		SCIMBulkRequest request = new SCIMBulkRequest();
		request.setSourecSystemId("sys-scim-gw");
		request.setDirectSystemId("sys-scim-im");
		
		User user1 = create_user_test (
				"12344",
				"12344_name",
				true,
				"12344_no",
				"dep","pos","job","1234@test.com");
		
		User user2 = create_user_test (
				"12424",
				"12424_name",
				true,
				"12424_no",
				"dep","pos","job","12424@test.com");
		
		SCIMBulkOperation op1 = new SCIMBulkOperation();
		op1.setMethod(SCIMDefinitions.MethodType.PUT.toString());
		op1.setData(user1);
		op1.setBulkId("asfasdfa");
		
		SCIMBulkOperation op2 = new SCIMBulkOperation();
		
		op2.setMethod(SCIMDefinitions.MethodType.DELETE.toString());
		op2.setData(user2);
		op2.setBulkId("asfasdfaee");
		
		request.addOperation(op1);
		request.addOperation(op2);
		
		return request;
	}
}
