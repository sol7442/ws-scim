package com.ehyundai.gw;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.quartz.JobExecutionContext;

import com.ehyundai.im.User;
import com.wowsanta.scim.SCIMSystemManager;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.obj.DefaultUserMeta;
import com.wowsanta.scim.resource.SCIMRepository;
import com.wowsanta.scim.resource.SCIMRepositoryManager;
import com.wowsanta.scim.resource.SCIMResourceRepository;
import com.wowsanta.scim.resource.SCIMUser;
import com.wowsanta.scim.scheduler.SCIMJob;

public class ReconciliationJob_GW extends SCIMJob {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2799856505493920258L;

	@Override
	public void doExecute(JobExecutionContext context) {
		
	}

	@Override
	public void beforeExecute(JobExecutionContext context) {
		SCIMLogger.proc("ReconciliationJob_GW : {} ", new Date());
		
		try {
			SCIMResourceRepository res_repo = SCIMRepositoryManager.getInstance().getResourceRepository();
			
			Calendar cal = Calendar.getInstance();
			Date to = cal.getTime();
			cal.add(Calendar.DATE, -10);
			Date from = cal.getTime();
			
			List<SCIMUser> user_list = res_repo.getUsers(from, to);
			for (SCIMUser scimUser : user_list) {
				User user = (User)scimUser;
				
				if(user.getActive().equals("N")) { // deleate user
					System.out.println("DEL : ["+((DefaultUserMeta)user.getMeta()).getCreated()+"]" + "["+((DefaultUserMeta)user.getMeta()).getLastModified()+"]" + "["+user.getRetireDate()+"]" );
				}else {
					if(user.getActive().equals("Y") && ((DefaultUserMeta)user.getMeta()).getCreated().equals(((DefaultUserMeta)user.getMeta()).getLastModified())) {
						System.out.println("NEW : ["+((DefaultUserMeta)user.getMeta()).getCreated()+"]" + "["+((DefaultUserMeta)user.getMeta()).getLastModified()+"]" + "["+user.getRetireDate()+"]" );
					}else {
						System.out.println("MOD : ["+((DefaultUserMeta)user.getMeta()).getCreated()+"]" + "["+((DefaultUserMeta)user.getMeta()).getLastModified()+"]" + "["+user.getRetireDate()+"]" );
					}	
				}
			}
			
			System.out.println(((User)user_list.get(0)).toString(true));
			System.out.println("====[" + user_list.size() + "]");
		} catch (SCIMException e1) {
			
		}
	}

	@Override
	public void afterExecute(JobExecutionContext context) {
		SCIMLogger.proc("ReconciliationJob_GW : {} ", new Date());
	}

}
