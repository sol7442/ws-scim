package com.ehyundai.sso;

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.ehyundai.im.Meta;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.wowsanta.scim.repo.rdb.AbstractRDBRepository;
import com.wowsanta.scim.resource.Group;
import com.wowsanta.scim.resource.SCIMUser;
import com.wowsanta.scim.schema.SCIMResourceTypeSchema;

public class SSORepositoryManager extends AbstractRDBRepository {

	public static SSORepositoryManager load(String file_name) throws FileNotFoundException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonReader reader = new JsonReader(new FileReader(file_name));
		return gson.fromJson(reader,SSORepositoryManager.class);
	}
	
	@Override
	public SCIMUser createUser(SCIMUser user) {
		
		Meta meta = (Meta)user.getMeta();
		System.out.println(meta.getVersion());
		
		return null;
	}

	@Override
	public SCIMUser getUser(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SCIMUser updateUser(SCIMUser updatedUser) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteUser(String userId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGroupSchema(SCIMResourceTypeSchema groupSchema) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Group createGroup(Group group) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Group getGroup(String groupId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Group updateGroup(Group group) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteGroup(String groupId) {
		// TODO Auto-generated method stub
		
	}
}
