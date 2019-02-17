package com.wowsanta.scim.service.scim.v2.bulk;

import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.json.SCIMJsonObject;
import com.wowsanta.scim.message.SCIMBulkOperation;
import com.wowsanta.scim.message.SCIMBulkRequest;
import com.wowsanta.scim.message.SCIMBulkResponse;
import com.wowsanta.scim.message.SCIMError;
import com.wowsanta.scim.obj.SCIMResource;
import com.wowsanta.scim.obj.SCIMUser;
import com.wowsanta.scim.resource.SCIMLocationFactory;
import com.wowsanta.scim.resource.SCIMRepository;
import com.wowsanta.scim.resource.SCIMRepositoryManager;
import com.wowsanta.scim.resource.SCIMResourceRepository;
import com.wowsanta.scim.schema.SCIMConstants;
import com.wowsanta.scim.schema.SCIMDefinitions;
import com.wowsanta.scim.service.SCIMService;

public class BlukService implements SCIMService {

	private SCIMRepositoryManager repository_manager = SCIMRepositoryManager.getInstance();

	@Override
	public SCIMJsonObject execute(SCIMJsonObject request) {
				
		SCIMBulkOperation result = null;//= (SCIMBulkOperation) request;
		if (request instanceof SCIMBulkOperation) {
			SCIMBulkOperation operation = (SCIMBulkOperation) request;
			
			String path = operation.getPath();
			String method = operation.getMethod();
			
			if(path.equals(SCIMConstants.USER_ENDPOINT)) {
				if(SCIMDefinitions.MethodType.PUT.toString().equals(method)) {
					result = addUser(operation);
				}
				else if(SCIMDefinitions.MethodType.PATCH.toString().equals(method)) {
					//result = deleteUser(operation); ignore
					result = addUser(operation);
				}
				else if(SCIMDefinitions.MethodType.POST.toString().equals(method)) {
					//result = deleteUser(operation); ignore
					result = addUser(operation);
				}
				else if(SCIMDefinitions.MethodType.DELETE.toString().equals(method)) {
					result = deleteUser(operation);
				}else {
					System.out.println("--------un");
				}
				
			}else {
			
			}
		}
		
		return result;
	}

	private SCIMBulkOperation deleteUser(SCIMBulkOperation operation) {
		SCIMBulkOperation result = new SCIMBulkOperation(operation);
		
		SCIMResource resource = operation.getData();
		SCIMResourceRepository resource_repository = repository_manager.getResourceRepository();
		try {
			SCIMUser user = (SCIMUser) resource;
			resource_repository.deleteUser(user.getId());
			result.setLocation(SCIMLocationFactory.getInstance().get(user));
			result.setStatus(SCIMConstants.SatusConstants.SCUESS_CODE);
			
		} catch (SCIMException e) {
			SCIMError error = new SCIMError();
			
			if (e.getCode() == SCIMRepository.RESULT_DUPLICATE_ENTRY) {
				result.setStatus(SCIMConstants.SatusConstants.uniqueness);
				
				error.setStatus(SCIMConstants.SatusConstants.uniqueness);
				error.setScimType(SCIMDefinitions.ErrorType.uniqueness.toString());
				error.setDetail(SCIMConstants.SatusConstants.uniqueness_DETAIL);
				
			} else {
				//bulk_resp.addError(bulkId, method, "400", null, "invalidSyntax", "Request is unparsable, syntactically incorrect, or violates schema.");
				result.setStatus(SCIMConstants.SatusConstants.invalidSyntax);
				
				error.setStatus(SCIMConstants.SatusConstants.invalidSyntax);
				error.setScimType(SCIMDefinitions.ErrorType.invalidSyntax.toString());
				error.setDetail(SCIMConstants.SatusConstants.invalidSyntax_DETAIL);
			}
			
			result.setResponse(error);
			
		}finally {
			
		}	
		
		return result;
	}

	private SCIMBulkOperation addUser(SCIMBulkOperation operation) {
		SCIMBulkOperation result = new SCIMBulkOperation(operation);
		
		SCIMResource resource = operation.getData();
		SCIMResourceRepository resource_repository = repository_manager.getResourceRepository();
		try {
			SCIMUser user = resource_repository.createUser((SCIMUser) resource);
			result.setLocation(SCIMLocationFactory.getInstance().get(user));
			result.setStatus(SCIMConstants.SatusConstants.SCUESS_CODE);
			
		} catch (SCIMException e) {
			if (e.getCode() == SCIMRepository.RESULT_DUPLICATE_ENTRY) {
				result.setStatus(SCIMConstants.SatusConstants.uniqueness);
				SCIMError error = new SCIMError();
			
				error.setStatus(SCIMConstants.SatusConstants.uniqueness);
				error.setScimType(SCIMDefinitions.ErrorType.uniqueness.toString());
				error.setDetail(SCIMConstants.SatusConstants.uniqueness_DETAIL);
				
				result.setResponse(error);
			} else {
				e.printStackTrace();
			}
		}finally {
			
		}	
		
		return result;
	}

}
