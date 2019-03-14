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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wowsanta.scim.SCIMSystemManager;
import com.wowsanta.scim.exception.SCIMError;
import com.wowsanta.scim.exception.SCIMException;
import com.wowsanta.scim.log.SCIMLogger;
import com.wowsanta.scim.resource.SCIMRepository;
import com.wowsanta.scim.resource.SCIMRepositoryManager;
import com.wowsanta.scim.resource.SCIMResourceRepository;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.utils.IOUtils;

public class ConfigService {
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
				SCIMResourceRepository resource_repository = (SCIMResourceRepository) SCIMRepositoryManager.getInstance().getResourceRepository();
				
				System.out.println(" resource_reposityr " + resource_repository.toString());
				
				return resource_repository;
			}
		};
	}

	public Route setRepositoryInfo() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				
				try {
					JsonObject jsonObject = new JsonParser().parse(request.body()).getAsJsonObject();
					String class_name = jsonObject.get("className").getAsString();
					SCIMRepository newRepository = (SCIMRepository) Class.forName(class_name).newInstance();
					newRepository.fromJson(jsonObject);
					
					System.out.println("newRepository" + newRepository) ;
					newRepository.initialize();
					if(newRepository.validate()) {
						SCIMRepositoryManager.getInstance().setResourceRepository((SCIMResourceRepository) newRepository);
						SCIMRepositoryManager.getInstance().save();
						
					}else {
						throw new SCIMException("Repository Validate Failed",SCIMError.InternalServerError);
					}
					
					return newRepository;
				}catch (Exception e) {
					SCIMError error = SCIMError.InternalServerError;
					error.addDetail(e.getMessage());
					return error;
				}
			}
		};
	}

	public Route patchLibrary() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				try {					
					
					File system_path = new File(System.getProperty("user.dir"));
					File scim_libray_path = new File(System.getProperty("scim.library"));
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

                    SCIMLogger.sys("system_path       : {}", system_path);
                    SCIMLogger.sys("scim_libray_path  : {}", scim_libray_path);
                    SCIMLogger.sys("system_root_path  : {}", system_root_path);
                    SCIMLogger.sys("libray_temp_path  : {}", libray_temp_path);
                    SCIMLogger.sys("libray_backup_dir : {}", libray_backup_dir);

                    try {
	                    for (Part part : parts) {
	                    	
	                        SCIMLogger.sys("library patch file : {} ", part);
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
	                        
	                        SCIMLogger.sys("library patch : {} ", library_new_file.getCanonicalPath());
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
}
