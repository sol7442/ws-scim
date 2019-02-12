package com.ehyundai.message;


import org.junit.Test;

import com.ehyundai.im.User;
import com.ehyundai.object.EhyunDaiScimObjectTest;
import com.wowsanta.scim.message.SCIMBulkOperastion;
import com.wowsanta.scim.message.SCIMBulkRequest;
import com.wowsanta.scim.resource.SCIMResouceFactory;
import com.wowsanta.scim.schema.SCIMDefinitions;

public class EhyunDaiScimMessageTest extends EhyunDaiScimObjectTest {

	static{
		SCIMResouceFactory res_factory = SCIMResouceFactory.getInstance();
		res_factory.setUserClass(User.USER_URI, User.class.getCanonicalName());
	}
	
	@Test
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
		
		SCIMBulkOperastion op1 = new SCIMBulkOperastion();
		op1.setMethod(SCIMDefinitions.MethodType.PUT.toString());
		op1.setData(user1);
		op1.setBulkId("asfasdfa");
		
		SCIMBulkOperastion op2 = new SCIMBulkOperastion();
		
		op2.setMethod(SCIMDefinitions.MethodType.DELETE.toString());
		op2.setData(user2);
		op2.setBulkId("asfasdfaee");
		
		request.AddOperation(op1);
		request.AddOperation(op2);
		
		return request;
	}
}
