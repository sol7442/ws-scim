package com.wowsanta.scim.service.agent;

import static spark.Spark.halt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.eclipse.jetty.util.UrlEncoded;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wowsanta.scim.client.RESTClient;
import com.wowsanta.scim.exception.SCIMError;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.message.SCIMFindRequest;
import com.wowsanta.scim.obj.SCIMSystem;
import com.wowsanta.scim.protocol.ClientReponse;
import com.wowsanta.scim.protocol.ClientRequest;
import com.wowsanta.scim.protocol.FrontReqeust;
import com.wowsanta.scim.protocol.FrontResponse;
import com.wowsanta.scim.protocol.OutputMapperRequest;
import com.wowsanta.scim.protocol.ResponseState;
import com.wowsanta.scim.repository.AbstractSCIMRepository;
import com.wowsanta.scim.repository.RepositoryOutputMapper;
import com.wowsanta.scim.repository.SCIMRepositoryController;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.resource.user.LoginUser;
import com.wowsanta.scim.resource.worker.Worker;
import com.wowsata.util.json.WowsantaJson;
import com.wowsata.util.net.WowsantaURLEncoder;

import oracle.net.aso.c;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.utils.IOUtils;

public class AgentService {

	Logger logger = LoggerFactory.getLogger(AgentService.class);
	
	public static Route runRemoteScheduler() {
		// TODO Auto-generated method stub
		return null;
	}

	public Route getSystemInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	public Route setSystemInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	public Route setRepositoryInfo() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				try {
					String systemId = request.params(":systemId");
					SCIMSystem system = SCIMRepositoryManager.getInstance().getSystemRepository().getSystemById(systemId);
					if(system == null) {
						SCIMError error = SCIMError.invalidValue;
						error.addDetail("system ID not found : " + systemId);
						
						throw new SCIMException(error);
					}
					
					JsonObject jsonObject = new JsonParser().parse(request.body()).getAsJsonObject();
					
					
					String system_url = system.getSystemUrl();
					String call_url   = system_url + "/config/repository";
							
					Worker worker = findWorker(request);
					RESTClient clinent = new RESTClient(worker);
					
					String result = clinent.post2(call_url,jsonObject);
					

					
					return "OK";
					
				}catch(SCIMException e) {
					return e.getError();
				}
			}
		};
	}
	
	private Worker findWorker(Request request) {
		LoginUser login_user = request.session().attribute("loginUser");
		Worker worker = new Worker();
		worker.setWorkerId(login_user.getUserId());
		worker.setWorkerType(login_user.getType().toString());
		return worker;
	}

	public Route getLibrary() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				String systemId = request.params(":systemId");
				SCIMSystem system = SCIMRepositoryManager.getInstance().getSystemRepository().getSystemById(systemId);
				String system_url = system.getSystemUrl();
				String call_url   = system_url + "/config/library";
				
				String scim_library = System.getProperty("scim.library");
				
				Path library_path = Paths.get(scim_library);
				File library_dir  = library_path.toFile();
//				for (File libray_file : library_dir.listFiles()) {
//				}
				
				return library_dir.list();
			}
		};
	}
	
	public Route patchLibrary_old() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				try {
					String systemId = request.params(":systemId");
					SCIMSystem system = SCIMRepositoryManager.getInstance().getSystemRepository().getSystemById(systemId);
					
					File system_path = new File(System.getProperty("user.dir"));
					
					File system_root_path = system_path.getParentFile();
					File libray_temp_path = new File(system_root_path.getCanonicalFile() + File.separator + "temp");
                    
					String system_url = system.getSystemUrl();
					
					logger.info("old patchLibrary : {}", systemId );
					request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement(libray_temp_path.getCanonicalPath()));
                    Collection<Part> parts = request.raw().getParts();
                    
                    Map<String, byte[]> buffer_map = new HashMap<String, byte[]>();
	                for (Part part : parts) {
						logger.info(" - patchLibrary : {}", part.getSubmittedFileName() );

	                	byte[] buffer = IOUtils.toByteArray(part.getInputStream());
	                	buffer_map.put(part.getSubmittedFileName(), buffer);
	                }
	                
					Worker worker = null;
					if(worker == null) {
						worker = new Worker();
						worker.setWorkerId("sys-operator");
						worker.setWorkerType("ADMIN");
						logger.info("- default worker >> {} ", worker.getWorkerId());
					}
					
					Path old_patch_path = Paths.get("../old_patch");
					File[] file_list = old_patch_path.toFile().listFiles();
					for (File file : file_list) {
						logger.info("old_patch << -- {} ", file.getName());
					}
					
					String patch_url   = system_url + "/config/library";
					logger.info("sendFile url : {} ", patch_url);
					RESTClient clinent = new RESTClient(worker);
					clinent.patch(patch_url, file_list);
					
					
					return "success";
				}catch (Exception e) {
					logger.error("LIBRARY PATCH FAILED ",e);
				}
				
				return "failed";
			}
		};
	}
	public Route patchLibrary() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				
				try {
					String systemId = request.params(":systemId");
					SCIMSystem system = SCIMRepositoryManager.getInstance().getSystemRepository().getSystemById(systemId);
					
					File system_path = new File(System.getProperty("user.dir"));
					
					File system_root_path = system_path.getParentFile();
					File libray_temp_path = new File(system_root_path.getCanonicalFile() + File.separator + "temp");
					
					
					logger.info("patchLibrary : {}", systemId );
					request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement(libray_temp_path.getCanonicalPath()));
                    Collection<Part> parts = request.raw().getParts();
                    
                    Map<String, byte[]> buffer_map = new HashMap<String, byte[]>();
	                for (Part part : parts) {
						logger.info(" - patchLibrary : {}", part.getSubmittedFileName() );

	                	byte[] buffer = IOUtils.toByteArray(part.getInputStream());
	                	buffer_map.put(part.getSubmittedFileName(), buffer);
	                }
                    
					String system_url = system.getSystemUrl();
					
					Worker worker = null;//findWorker(request);
					if(worker == null) {
						worker = new Worker();
						worker.setWorkerId("sys-operator");
						worker.setWorkerType("Admin");
					}
					
					
					String post_url   = system_url + "/library/";
					logger.info("sendFile url : {} ", post_url);
					RESTClient clinent = new RESTClient(worker);
					clinent.sendFile(post_url,buffer_map);
					
					return "success";
				}catch (Exception e) {
					logger.error("LIBRARY PATCH FAILED ",e);
				}
				
				return "failed";
			}
		};
	}

	public Route setLibrary() {
		// TODO Auto-generated method stub
		return null;
	}

	public Route removeLibrary() {
		// TODO Auto-generated method stub
		return null;
	}

	public Route getLogFileList() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				FrontResponse front_response = new FrontResponse();
				RESTClient client = null;
				try {
					String systemId = request.params(":systemId");
					SCIMSystem system = SCIMRepositoryManager.getInstance().getSystemRepository().getSystemById(systemId);
					String system_url = system.getSystemUrl();
					String call_url   = system_url + "/log/list";
					
					Worker worker = findWorker(request);
					client = new RESTClient(worker);
					ClientReponse client_response = client.get(call_url);
					if(client_response != null) {
						front_response.setData(client_response.getData());
						front_response.setState(ResponseState.Success);	
					}else {
						front_response.setState(ResponseState.Fail);
						front_response.setMessage("service response is null");
					}
				}catch (Exception e) {
					front_response.setState(ResponseState.Fail);;
					front_response.setMessage(e.getMessage());
					logger.error("getConfigFileList failed : " + e.getMessage(),e);
				}finally {
					client.close();
				}
				return front_response;
			}
		};
	}

	public Route getLogFile() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				
				try {
					String systemId = request.params(":systemId");
					String fileName = request.params(":fileName");
					
					logger.info("systemId : {}, fileName : {}",systemId,fileName);
					
					SCIMSystem system = SCIMRepositoryManager.getInstance().getSystemRepository().getSystemById(systemId);
					String system_url = system.getSystemUrl();
					String call_url   = system_url + "/log/file/" + UrlEncoded.encodeString(fileName);
					
					logger.info("call url : {}",call_url);
					
					Worker worker = findWorker(request);
					RESTClient clinent = new RESTClient(worker);
				
					HttpResponse http_res = clinent.getFile(call_url);
					HttpEntity entity = http_res.getEntity();
				    if (entity != null) {
				    	
				        response.header("Content-Disposition", String.format("attachment; filename=", fileName));
				        response.raw().setContentType("application/octet-stream");
				        response.raw().setContentLength((int) entity.getContentLength());
				        
				        entity.writeTo(response.raw().getOutputStream());
				    }
			        
				}catch (Exception e) {
					e.printStackTrace();
				}
				
				return response.raw();
			}
		};
	}

	public Route getConfigFileList() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				FrontResponse front_response = new FrontResponse();
				RESTClient client = null;
				try {
					String systemId = request.params(":systemId");
					
					SCIMSystem system = SCIMRepositoryManager.getInstance().getSystemRepository().getSystemById(systemId);
					String system_url = system.getSystemUrl();
					String call_url   = system_url + "/config/list";
					Worker worker = findWorker(request);
					client = new RESTClient(worker);
					
					ClientReponse client_response = client.get(call_url);
					if(client_response != null) {
						front_response.setData(client_response.getData());
						front_response.setState(ResponseState.Success);	
					}else {
						front_response.setState(ResponseState.Fail);
						front_response.setMessage("service response is null");
					}
				}catch (Exception e) {
					front_response.setState(ResponseState.Fail);;
					front_response.setMessage(e.getMessage());
					logger.error("getConfigFileList failed : " + e.getMessage(),e);
				}finally {
					client.close();
				}
				
				return front_response;
			}
		};
	}

	public Route getConfigFile() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				try {
					String systemId = request.params(":systemId");
					String fileName = request.params(":fileName");
					
					logger.info("systemId : {}, fileName : {}",systemId,fileName);
					
					SCIMSystem system = SCIMRepositoryManager.getInstance().getSystemRepository().getSystemById(systemId);
					String system_url = system.getSystemUrl();
					String call_url   = system_url + "/config/file/" + UrlEncoded.encodeString(fileName);
					
					logger.info("call url : {}",call_url);
					
					Worker worker = findWorker(request);
					RESTClient clinent = new RESTClient(worker);
				
					HttpResponse http_res = clinent.getFile(call_url);
					HttpEntity entity = http_res.getEntity();
				    if (entity != null) {
				    	
				        response.header("Content-Disposition", String.format("attachment; filename=", fileName));
				        response.raw().setContentType("application/octet-stream");
				        response.raw().setContentLength((int) entity.getContentLength());
				        
				        entity.writeTo(response.raw().getOutputStream());
				    }
			        
				}catch (Exception e) {
					e.printStackTrace();
				}
				
				return response.raw();
			}
		};
	}

	public Route patchConfigFile() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				try {
					String systemId = request.params(":systemId");
					SCIMSystem system = SCIMRepositoryManager.getInstance().getSystemRepository().getSystemById(systemId);
					
					File system_path = new File(System.getProperty("user.dir"));
					
					File system_root_path = system_path.getParentFile();
					File libray_temp_path = new File(system_root_path.getCanonicalFile() + File.separator + "temp");
					
					
					logger.info("patchConfigFile : {}", systemId );
					request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement(libray_temp_path.getCanonicalPath()));
                    Collection<Part> parts = request.raw().getParts();
                    
                    Map<String, byte[]> buffer_map = new HashMap<String, byte[]>();
	                for (Part part : parts) {
						logger.info(" - patchConfigFile : {}", part.getSubmittedFileName() );

	                	byte[] buffer = IOUtils.toByteArray(part.getInputStream());
	                	buffer_map.put(part.getSubmittedFileName(), buffer);
	                }
                    
					String system_url = system.getSystemUrl();
					
					Worker worker = null;//findWorker(request);
					if(worker == null) {
						worker = new Worker();
						worker.setWorkerId("sys-operator");
						worker.setWorkerType("Admin");
					}
					
					String post_url   = system_url + "/config/";
					logger.info("sendFile url : {} ", post_url);
					RESTClient clinent = new RESTClient(worker);
					clinent.sendFile(post_url,buffer_map);
					
					return "success";
				}catch (Exception e) {
					logger.error("CONFIG PATCH FAILED ",e);
				}
				
				return "failed";
			}
		};
	}

	public Route getTableList() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				FrontResponse front_response = new FrontResponse();
				RESTClient client = null;
				try {
					String systemId = request.params(":systemId");
					SCIMSystem system = SCIMRepositoryManager.getInstance().getSystemRepository().getSystemById(systemId);
					String system_url = system.getSystemUrl();
					String call_url   = system_url + "/repository/table/list";
					
					
					Worker worker = findWorker(request);
					client = new RESTClient(worker);
						
					ClientReponse client_response = client.get(call_url);
					if(client_response != null) {
						front_response.setData(client_response.getData());
						front_response.setState(ResponseState.Success);	
					}else {
						front_response.setState(ResponseState.Fail);
						front_response.setMessage("service response is null");
					}
				}catch (Exception e) {
					front_response.setState(ResponseState.Fail);;
					front_response.setMessage(e.getMessage());
					logger.error("getConfigFileList failed : " + e.getMessage(),e);
				}finally {
					client.close();
				}
				
				return front_response;
			}
		};
	}

	public Route getColumnList() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				FrontResponse front_response = new FrontResponse();
				RESTClient client = null;
				try {
					
					FrontReqeust front_request =  FrontReqeust.parse(request.body()); 
					
					String systemId  = front_request.getParams().get("systemId");
					String tableName = front_request.getParams().get("tableName");
					//String keyColumn = front_request.getParams().get("keyColumn");
					
					SCIMSystem system = SCIMRepositoryManager.getInstance().getSystemRepository().getSystemById(systemId);
					String system_url = system.getSystemUrl();
					String call_url   = system_url + "/repository/table/column/list" + "/" + tableName ;//+ "/" + keyColumn;
					
					Worker worker = findWorker(request);
					client = new RESTClient(worker);
					
					try {
						ClientReponse client_response = client.get(call_url);
						if(client_response.getState() == ResponseState.Success) {
							front_response.setData(client_response.getData());
							front_response.setState(ResponseState.Success);;
						}else {
							front_response.setMessage(client_response.getMessage());
							front_response.setState(ResponseState.Fail);;
						}						
					}catch (Exception e) {
						front_response.setMessage(e.getMessage());
						front_response.setState(ResponseState.Fail);;
						
						logger.error("{}",e.getMessage(),e);
					}
					
				}catch (Exception e) {
					front_response.setState(ResponseState.Fail);;
					front_response.setMessage(e.getMessage());
					logger.error("getConfigFileList failed : " + e.getMessage(),e);
				}finally {
					client.close();
				}
				
				return front_response;
			}
		};
	}

	public Route getSchemOutputMapper() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				FrontResponse front_response = new FrontResponse();
				RESTClient client = null;

				try {
					String systemId = request.params("systemId");
					SCIMSystem system = SCIMRepositoryManager.getInstance().getSystemRepository().getSystemById(systemId);
					String system_url = system.getSystemUrl();
					String call_url   = system_url + "/schema/mapper/output";
					
					Worker worker = findWorker(request);
					client = new RESTClient(worker);
					ClientReponse client_response = client.get(call_url);
					if(client_response != null) {
						
						
						front_response.setData(client_response.getData());
						front_response.setState(ResponseState.Success);	
					}else {
						front_response.setState(ResponseState.Fail);
						front_response.setMessage("service response is null");
					}
					
				}catch (Exception e) {
					logger.error("{}",e.getMessage(), e);
					front_response.setState(ResponseState.Fail);
					front_response.setMessage(e.getMessage());
				}
				return front_response;
			}
		};
	}
	
	public Route updateSchemOutputMapper() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				FrontResponse front_response = new FrontResponse();
				RESTClient client = null;

				try {
					String systemId = request.params("systemId");
					
					OutputMapperRequest mapper_request = WowsantaJson.parse(request.body(),OutputMapperRequest.class);
					
					
					SCIMSystem system = SCIMRepositoryManager.getInstance().getSystemRepository().getSystemById(systemId);
					String system_url = system.getSystemUrl();
					String call_url   = system_url + "/schema/mapper/output";
					
					Worker worker = findWorker(request);
					client = new RESTClient(worker);
					
					front_response.setState(ResponseState.Fail);
					front_response.setMessage("service response is null");

					ClientReponse client_response = client.post(call_url, mapper_request,ClientReponse.class, "UTF-8");

					if(client_response != null) {
						front_response.setData(client_response.getData());
						front_response.setState(ResponseState.Success);	
					}else {
						front_response.setState(ResponseState.Fail);
						front_response.setMessage("service response is null");
					}
					
				}catch (Exception e) {
					logger.error("{}",e.getMessage(), e);
					front_response.setState(ResponseState.Fail);
					front_response.setMessage(e.getMessage());
				}
				return front_response;
			}
		};
	}

	public Route getSchemInputMapper() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				FrontResponse front_response = new FrontResponse();
				RESTClient client = null;

				try {
					String systemId = request.params("systemId");
					SCIMSystem system = SCIMRepositoryManager.getInstance().getSystemRepository().getSystemById(systemId);
					String system_url = system.getSystemUrl();
					String call_url   = system_url + "/schema/mapper/input";
					
					Worker worker = findWorker(request);
					client = new RESTClient(worker);
					ClientReponse client_response = client.get(call_url);
					if(client_response != null) {
						front_response.setData(client_response.getData());
						front_response.setState(ResponseState.Success);	
					}else {
						front_response.setState(ResponseState.Fail);
						front_response.setMessage("service response is null");
					}
					
				}catch (Exception e) {
					logger.error("{}",e.getMessage(), e);
					front_response.setState(ResponseState.Fail);
					front_response.setMessage(e.getMessage());
				}
				return front_response;
			}
		};
	}

	public Route getQueryResult() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				FrontResponse front_response = new FrontResponse();
				RESTClient client = null;

				try {
					//String systemId = request.params("systemId");
					FrontReqeust front_request = FrontReqeust.parse(request.body());
					logger.info("getQueryResult : {}",front_request);

					String systemId = front_request.getParam("systemId");
					String query = front_request.getParam("query");
					logger.info("systemId : {}",systemId);
					logger.info("query    : {}",query);
					
					SCIMSystem system = SCIMRepositoryManager.getInstance().getSystemRepository().getSystemById(systemId);
					if(system == null) {
						throw new Exception("system not found - system id : " + systemId);
					}
					
					String system_url = system.getSystemUrl();
					
					String call_url   = system_url + "/repository/query";
					Worker worker = findWorker(request);
					client = new RESTClient(worker);
					
					try {
						ClientRequest client_request = new ClientRequest();
						client_request.putParam("query", query);
						
						ClientReponse client_response = client.post(call_url,client_request);
						if(client_response != null) {
							List<Map<String,Object>> result = (List<Map<String,Object>>)client_response.getData();
							for (Map<String, Object> map : result) {
								Set<String> keys = map.keySet();
								for (String key : keys) {
									logger.debug("key : {} : {} ",key, map.get(key));
								}
							}
							
							front_response.setData(result);
							front_response.setState(ResponseState.Success);	
						}else {
							front_response.setState(ResponseState.Fail);
							front_response.setMessage("service response is null");
						}
					}catch (Exception e) {
						front_response.setState(ResponseState.Fail);
						front_response.setMessage(e.getMessage());
						
						logger.error("{}",e.getMessage(),e);
					}
					
				}catch (Exception e) {
					logger.error("{}",e.getMessage(), e);
					front_response.setState(ResponseState.Fail);
					front_response.setMessage(e.getMessage());
				}
				return front_response;
			}
		};
	}


}
