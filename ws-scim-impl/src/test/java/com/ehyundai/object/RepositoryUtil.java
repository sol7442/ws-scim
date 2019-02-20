package com.ehyundai.object;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ehyundai.object.User;
import com.wowsanta.scim.obj.SCIMUserMeta;
import com.wowsanta.scim.util.Random;

public class RepositoryUtil {

	public static List<User> createUsers(int size) {
		
		List<User> user_list = new ArrayList<User>();
		for (int i = 0; i < size; i++) {
			User user = new User();
			user.setId(Random.number(10000000, 99999999));
			
			user.setEmployeeNumber(user.getId());
			user.setUserName(Random.name());
			user.setActive(Random.yn_boolean(90));
			user.setPosition(Random.position());
			user.setJob(Random.job());

			user.seteMail(user.getEmployeeNumber() + "@ehyundai.com");

			//user.setCompanyCode("현대백화점");			
			
			user.setCompanyCode("현대백화점");			
			user.setDivision(Random.group());
			
			//user.setDepartment(Random.department());
			
			Date join_date   = Random.beforeYears(1);
			Date retire_date = null;
			Date create_date = join_date;//Random.beforeYears(1);
			Date modify_date = null;
			
			user.setMeta(new SCIMUserMeta());
			if (user.isActive()) {
				if(Random.yn_boolean(10)) {	// 신입사원 비율
					modify_date = create_date;
				}else {
					modify_date = Random.date(create_date, new Date());
				}
				
			}else {
				if(Random.yn_boolean(40)) { // 비 활성화된 사용자 중 40 % 1년 안에 퇴사한 사람  
					retire_date   = Random.beforeYears(1);
					modify_date   = retire_date;
				}else {
					retire_date   = Random.date(create_date, new Date());
					modify_date   = retire_date;
				}
			}
			
			user.setJoinDate(join_date);
			user.setRetireDate(retire_date);
			user.getMeta().setCreated(create_date);
			user.getMeta().setLastModified(modify_date);
			
			user_list.add(user);
		}
		
		return user_list;
	}
}
