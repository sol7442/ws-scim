package com.wowsanta.scim.repository;

import java.util.Date;
import java.util.List;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.obj.SCIMResource2;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.resource.SCIMAuditData;
import com.wowsanta.scim.resource.SCIMResourceSetterRepository;

public interface SCIMServerResourceRepository extends SCIMResourceGetterRepository, SCIMResourceSetterRepository {
	public SCIMUser getSystemUser(String systemId, String userId) throws SCIMException;
	public SCIMUser createSystemUser(String systemId, SCIMUser resource) throws SCIMException;
	public SCIMUser updateSystemUser(String systemId, SCIMUser resource) throws SCIMException;
	public void deleteSystemUser(String systemId, String userId) throws SCIMException;
	public List<SCIMUser> getSystemUserByDate(String system_id,Date from, Date to)throws SCIMException;
	public void clearSystemUser(String systemId) throws SCIMException;
	public void clearSystemUserProfile(String systemId) throws SCIMException;
	public void clearUser()throws SCIMException;
	public void clearUserProfile()throws SCIMException;
	public void createSystemDummyUser(String system_id, SCIMResource2 user)throws SCIMException;
	public void updateSystemDummyUser(String system_id, SCIMResource2 user)throws SCIMException;
	public List<SCIMUser> getSystemUsersBysystemIdWidthPage(String systemId)throws SCIMException;
	public List<SCIMAuditData> getAccountHistoryByUsrIdWidthPage(String userId)throws SCIMException;
	
}
