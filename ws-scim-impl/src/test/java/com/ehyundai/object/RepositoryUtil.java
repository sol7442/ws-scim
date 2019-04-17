package com.ehyundai.object;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.ehyundai.object.User;
import com.ehyundai.sso.ObjectConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wowsanta.scim.obj.SCIMUserMeta;
import com.wowsanta.scim.repository.DataMapper;
import com.wowsanta.scim.util.Random;

public class RepositoryUtil {

	@Test
	public void method_call_test() {
		
		// sso object convert
		ObjectConverter converter = new ObjectConverter();
		
		try {
			Class[] params = {Object.class};
			Method method = ObjectConverter.class.getMethod("convert", params);
			String result = (String) method.invoke(converter,"test");
			
			
			
			DataMapper gr_path_mapper = new DataMapper();
			gr_path_mapper.setId("DIV");
			gr_path_mapper.setClassName("com.wowsanta.scim.repository.impl.DefaultDataMapper");
			
			String[] out_paramse = {Object.class.getCanonicalName()};
			gr_path_mapper.setOutMethod("convert");
			//gr_path_mapper.setOutMethod(method);
		
			//colum_exgrouppath.setMapper(gr_path_mapper);
			GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
			builder.setPrettyPrinting();			
			Gson gson = builder.create();
			System.out.println(gson.toJson(gr_path_mapper));
			
			System.out.println(gr_path_mapper);
			
			System.out.println(result);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		
	}
	
	
//	public static List<User> createUsers(int size) {
//		
//		List<User> user_list = new ArrayList<User>();
//		for (int i = 0; i < size; i++) {
//			User user = new User();
//			user.setId(Random.number(10000000, 99999999));
//			
//			user.setEmployeeNumber(user.getId());
//			user.setUserName(Random.name());
//			user.setActive(Random.yn_boolean(90));
//			user.setPosition(Random.position());
//			user.setJob(Random.job());
//
//			user.seteMail(user.getEmployeeNumber() + "@ehyundai.com");
//
//			user.setCompanyCode("현대백화점");			
//			user.setDivision(Random.group());
//			user.setDepartment(user.getDivision() + "/" + Random.department());
//			
//			Date join_date   = Random.beforeYears(1);
//			Date retire_date = null;
//			Date create_date = join_date;//Random.beforeYears(1);
//			Date modify_date = null;
//			
//			user.setLastAccessDate(Random.date(join_date, new Date()));
//			user.setMeta(new SCIMUserMeta());
//			if (user.isActive()) {
//				if(Random.yn_boolean(10)) {	// 신입사원 비율
//					modify_date = create_date;
//				}else {
//					modify_date = Random.date(create_date, new Date());
//				}
//				
//			}else {
//				if(Random.yn_boolean(40)) { // 비 활성화된 사용자 중 40 % 1년 안에 퇴사한 사람  
//					retire_date   = Random.beforeYears(1);
//					modify_date   = retire_date;
//				}else {
//					retire_date   = Random.date(create_date, new Date());
//					modify_date   = retire_date;
//				}
//			}
//			
//			user.setJoinDate(join_date);
//			user.setRetireDate(retire_date);
//			user.getMeta().setCreated(create_date);
//			user.getMeta().setLastModified(modify_date);
//			
//			user_list.add(user);
//		}
//		
//		return user_list;
//	}
}
