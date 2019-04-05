package com.wowsanta.scim.service.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.Date;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wowsanta.scim.SCIMSystemManager;
import com.wowsanta.scim.exception.SCIMError;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.repository.SCIMRepository;
import com.wowsanta.scim.repository.SCIMRepositoryManager;
import com.wowsanta.scim.repository.SCIMResourceRepository;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.utils.IOUtils;

public class ConfigService {
	Logger logger = LoggerFactory.getLogger(ConfigService.class);
	
	public Route getSystemInfo() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				
				
				return null;
			}
		};
	}

	public Route setSystemInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	public Route getRepositoryInfo() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				SCIMRepository repository = (SCIMRepository) SCIMRepositoryManager.getInstance().getResourceRepository();
				
				logger.info("resource repository {} ", repository.tojson(false));
				
				return repository;
			}
		};
	}

	public Route setRepositoryInfo() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				return null;
//				try {
//					JsonObject jsonObject = new JsonParser().parse(request.body()).getAsJsonObject();
//					String class_name = jsonObject.get("className").getAsString();
//					SCIMRepository newRepository = (SCIMRepository) Class.forName(class_name).newInstance();
//					newRepository.fromJson(jsonObject);
//					
//					System.out.println("newRepository" + newRepository) ;
//					newRepository.initialize();
//					if(newRepository.validate()) {
//						SCIMRepositoryManager.getInstance().setResourceRepository((SCIMResourceRepository) newRepository);
//						SCIMRepositoryManager.getInstance().save();
//						
//					}else {
//						throw new SCIMException("Repository Validate Failed",SCIMError.InternalServerError);
//					}
//					
//					return newRepository;
//				}catch (Exception e) {
//					SCIMError error = SCIMError.InternalServerError;
//					error.addDetail(e.getMessage());
//					return error;
//				}
			}
		};
	}

	public Route patchLibrary() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				try {					
					
					File system_path = new File(System.getProperty("user.dir"));
					File scim_libray_path = new File(System.getProperty("app.dist"));
					
					File system_root_path = system_path.getParentFile();
					File libray_temp_path = new File(system_root_path.getCanonicalFile() + File.separator + "temp");
					File libray_backup_dir = new File(libray_temp_path + File.separator + new Date().getTime());		
					
					request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement(libray_temp_path.getCanonicalPath()));
				    
                    Collection<Part> parts = null;
                    try {
                        parts = request.raw().getParts();
                    } catch (IOException | ServletException e2) {
                        e2.printStackTrace();
                    }
                    
                    if(!libray_backup_dir.exists()) {
                    	libray_backup_dir.mkdirs();
                    }

                    logger.info("system_path       : {}", system_path);
                    logger.info("scim_libray_path  : {}", scim_libray_path);
                    logger.info("system_root_path  : {}", system_root_path);
                    logger.info("libray_temp_path  : {}", libray_temp_path);
                    logger.info("libray_backup_dir : {}", libray_backup_dir);

                    try {
	                    for (Part part : parts) {
	                    	
	                        logger.info("library patch file : {} ", part);
	                    	File library_old_file = new File (scim_libray_path + File.separator + part.getSubmittedFileName());
	                    	if(library_old_file.exists()) {
	                    		File backup_file = new File(libray_backup_dir + File.separator + part.getSubmittedFileName());
	                    		Files.copy(Paths.get(library_old_file.toURI()), Paths.get(backup_file.toURI()), StandardCopyOption.REPLACE_EXISTING);
	                    	}
	                    	
	                        File library_new_file = new File (scim_libray_path + File.separator + part.getSubmittedFileName());
	                        	
	                    	InputStream inputStream = part.getInputStream();
	                        OutputStream outputStream = new FileOutputStream(library_new_file);
	                        IOUtils.copy(inputStream, outputStream);
	                        outputStream.close();
	                        
	                        logger.info("library patch : {} ", library_new_file.getCanonicalPath());
	                    }
                    }catch(Exception e) {
                    	e.printStackTrace();
                    }
					
					return "OK";
				}catch (Exception e) {
					SCIMError error = SCIMError.InternalServerError;
					error.addDetail(e.getMessage());
					return error;
				}
			}
		};
	}

	public Route getSystemConfigList() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				File config_dir = new File("../config");
				return config_dir.listFiles();				
			}
		};
	}

	public Route getSystemConfigFile() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				String fileName = request.params(":fileName").replaceAll("\\+"," ");
				logger.info("config fileName >> : {}", fileName);
				
				File config_dir = new File("../config"); 
				File[] config_files = config_dir.listFiles();
				File request_file = null;
				for (File file : config_files) {
					if(file.getName().equals(fileName)) {
						logger.debug("find config fileName >> : {}", fileName);
						request_file = file;
						break;
					}
				}
				
				if(request_file != null) {
					logger.debug("response config fileName >> : {}", fileName);
					
			        response.header("Content-Disposition", String.format("attachment; filename=", fileName));
			        response.raw().setContentType("application/octet-stream");
			        response.raw().setContentLength((int) request_file.length());
			        OutputStream outputStream = response.raw().getOutputStream();
			        outputStream.write(Files.readAllBytes(request_file.toPath()));
			        outputStream.flush();	
				}
				
				return response.raw();
			}
		};
	}

	public Route updateSystemConfigFile() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				try {

					File system_path = new File(System.getProperty("user.dir"));
					File scim_config_path = new File("../config");

					File system_root_path = system_path.getParentFile();
					File config_temp_path = new File(system_root_path.getCanonicalFile() + File.separator + "temp");
					File config_backup_dir = new File(config_temp_path + File.separator + new Date().getTime());
					request.attribute("org.eclipse.jetty.multipartConfig",
							new MultipartConfigElement(config_temp_path.getCanonicalPath()));

					logger.info("system_path       : {}", system_path);
					logger.info("scim_config_path  : {}", scim_config_path);
					logger.info("system_root_path  : {}", system_root_path);
					logger.info("config_temp_path  : {}", config_temp_path);
					logger.info("config_backup_dir : {}", config_backup_dir);

					if (!config_backup_dir.exists()) {
						config_backup_dir.mkdirs();
					}

					Collection<Part> parts = null;
					parts = request.raw().getParts();
					for (Part part : parts) {
						logger.debug(" - config patch part : {} ", part);
						File library_old_file = new File(
								scim_config_path + File.separator + part.getName());
						if (library_old_file.exists()) {
							File backup_file = new File(
									config_backup_dir + File.separator + part.getName());
							Files.copy(Paths.get(library_old_file.toURI()), Paths.get(backup_file.toURI()),
									StandardCopyOption.REPLACE_EXISTING);
						}

						File config_new_file = new File(
								scim_config_path + File.separator + part.getName());

						InputStream inputStream = part.getInputStream();
						OutputStream outputStream = new FileOutputStream(config_new_file);
						IOUtils.copy(inputStream, outputStream);
						outputStream.close();

						logger.info("config patch : {} ", config_new_file.getCanonicalPath());
					}
					
					return "Ok";

				} catch (Exception e) {
					e.printStackTrace();
					return "Fail";
				}
			}
		};
	}
}
