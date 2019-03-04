package com.wowsanta.scim.service;

import static com.wowsanta.scim.server.JsonUtil.json_parse;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.gson.JsonObject;
import com.wowsanta.scim.exception.SCIMError;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.obj.SCIMAdmin;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.policy.impl.DefaultPasswordPoilcy;
import com.wowsanta.scim.resource.SCIMProviderRepository;
import com.wowsanta.scim.resource.SCIMRepositoryManager;
import com.wowsanta.scim.resource.SCIMResourceGetterRepository;
import com.wowsanta.scim.resource.SCIMServerResourceRepository;
import com.wowsanta.scim.resource.user.LoginUserType;

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

	public Route createAdmin() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				SCIMAdmin admin = null;
				try{
					admin  = json_parse(request.body(),SCIMAdmin.class);
					
					SCIMProviderRepository system_repository = (SCIMProviderRepository) SCIMRepositoryManager.getInstance().getSystemRepository();
					SCIMResourceGetterRepository resource_repository = (SCIMResourceGetterRepository) SCIMRepositoryManager.getInstance().getResourceRepository();

					if(system_repository.getAdmin(admin.getAdminId()) != null) {
						throw new SCIMException("이미 존재하는 관리자 입니다.",SCIMError.uniqueness);
					};
					
					admin.setAdminType(LoginUserType.ADMIN.toString());
					
					DefaultPasswordPoilcy policy = new DefaultPasswordPoilcy();
					admin.setPassword(policy.encrypt(admin.getAdminId()));
					admin.setPwExpireTime(policy.getPasswordExpireTime(new Date()));
					
					system_repository.createAdmin(admin);
					
					response.status(200);
					return admin;
				}catch(SCIMException e) {
					SCIMLogger.error("Create Admin FAILED : -- : {} ", e);
					response.status(e.getError().getStatus());
					
					return e.getError();
				}catch (Exception e) {
					SCIMLogger.error("Create Admin FAILED : -- : {} ", e);
					response.status(SCIMError.InternalServerError.getStatus());
					return SCIMError.InternalServerError;
				}finally {
					SCIMLogger.audit("Create Admin  : {} ", admin);
				}
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
