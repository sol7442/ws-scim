package com.ehyundai.object;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.wowsanta.scim.obj.SCIMUserMeta;

public class EhyunDaiScimObjectTest {

	@Test
	public void user_profile_test() {
		Map<String,String> profile = new HashMap<String,String>();
		
		profile.put("value",null);
		System.out.println(profile);
		System.out.println(profile.get("value"));
		
	}
	//@Test
	public void user_object_test() {
		System.out.println("create user object and parse >>>>>>>>>>>>>>>>>>>>>>>>>>");
		User user = create_user_test (
				"12344",
				"12344_name",
				true,
				"12344_no",
				"dep","pos","job","1234@test.com");//new User();
		
		
		SCIMUserMeta meta = new SCIMUserMeta();
		meta.setCreated(new Date());
		meta.setLastModified(new Date());
		//meta.setVersion("skdajfdsj");
		user.setMeta(meta);
		
		System.out.println(user.toString(true));
		System.out.println("=======================");
		System.out.println(user);
		System.out.println("=======================");
		
		User p_user = new User();
		p_user.parse(user.toString());
		System.out.println(p_user.toString(true));
	}
	
	public User create_user_test(String id, String name, boolean yn, String empno, String dep, String pos, String job, String mail){
		User user = new User();
		
		user.setId(id);
		user.setUserName(name);
		user.setActive(yn);
		
		user.setEmployeeNumber(empno);
		user.setDepartment(dep);

		user.setPosition(pos);
		user.setJob(job);
		user.setJoinDate(new Date());
		user.setRetireDate(new Date());
		
		user.seteMail(mail);
		
		return user;
	}
}
