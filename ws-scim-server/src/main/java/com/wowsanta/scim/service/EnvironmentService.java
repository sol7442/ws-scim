package com.wowsanta.scim.service;

import java.util.List;

import com.wowsanta.scim.obj.SCIMAdmin;
import com.wowsanta.scim.resource.SCIMProviderRepository;
import com.wowsanta.scim.resource.SCIMRepositoryManager;
import com.wowsanta.scim.resource.SCIMServerResourceRepository;

import spark.Request;
import spark.Response;
import spark.Route;

public class EnvironmentService {

	public static Route getAllAdmin() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				System.out.println("get all admins");
				SCIMProviderRepository system_repository = (SCIMProviderRepository) SCIMRepositoryManager.getInstance().getSystemRepository();
				return system_repository.getAdminList();
			}
		};
	}

	public static Route getAdmin() {
		return new Route() {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

	public static Route createAdmin() {
		return new Route() {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

	public static Route updateAdmin() {
		return new Route() {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

	public static Route deleteAdmin() {
		return new Route() {
			
			@Override
			public Object handle(Request request, Response response) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}
}
