package com.wowsanta.scim.service.account;

import java.util.List;

import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.repository.SCIMServerResourceRepository;
import com.wowsanta.scim.resource.SCIMAuditData;

import spark.Request;
import spark.Response;
import spark.Route;

public class AccountService {

	public static Route getSystemAccount() {
		return new Route() {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				String systemId = request.params(":systemId");
				
				SCIMServerResourceRepository  resource_repository = (SCIMServerResourceRepository) SCIMRepositoryManager.getInstance().getResourceRepository();
				
				List<SCIMUser> user_list = resource_repository.getSystemUsersBysystemIdWidthPage(systemId);
				
				System.out.println("user_list : " + user_list.size());
				
				return user_list;
			}
		};
	}

	public static Route getAccountHistory() {
		//userId
		// TODO Auto-generated method stub
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				String userId = request.params(":userId");
				
				SCIMServerResourceRepository  resource_repository = (SCIMServerResourceRepository) SCIMRepositoryManager.getInstance().getResourceRepository();
				
				List<SCIMAuditData> account_history_list = resource_repository.getAccountHistoryByUsrIdWidthPage(userId);
				
				System.out.println("account_history_list : " + account_history_list.size());
				
				return account_history_list;
			}
		};
	}

}
