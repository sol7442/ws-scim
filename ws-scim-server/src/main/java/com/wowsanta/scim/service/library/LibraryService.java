package com.wowsanta.scim.service.library;

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

import spark.Request;
import spark.Response;
import spark.Route;
import spark.utils.IOUtils;

public class LibraryService {

	Logger logger = LoggerFactory.getLogger(LibraryService.class);

	public Route getLibraryList() {
		return null;
	}

	public Route updateLibrary() {
		return new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {

				try {

					File system_path = new File(System.getProperty("user.dir"));
					File scim_libray_path = new File(System.getProperty("app.dist"));

					File system_root_path = system_path.getParentFile();
					File libray_temp_path = new File(system_root_path.getCanonicalFile() + File.separator + "temp");
					File libray_backup_dir = new File(libray_temp_path + File.separator + new Date().getTime());
					request.attribute("org.eclipse.jetty.multipartConfig",
							new MultipartConfigElement(libray_temp_path.getCanonicalPath()));

					logger.info("system_path       : {}", system_path);
					logger.info("scim_libray_path  : {}", scim_libray_path);
					logger.info("system_root_path  : {}", system_root_path);
					logger.info("libray_temp_path  : {}", libray_temp_path);
					logger.info("libray_backup_dir : {}", libray_backup_dir);

					if (!libray_backup_dir.exists()) {
						libray_backup_dir.mkdirs();
					}

					Collection<Part> parts = null;
					parts = request.raw().getParts();
					for (Part part : parts) {
						logger.debug(" - library patch part : {} ", part);
						File library_old_file = new File(
								scim_libray_path + File.separator + part.getName());
						if (library_old_file.exists()) {
							File backup_file = new File(
									libray_backup_dir + File.separator + part.getName());
							Files.copy(Paths.get(library_old_file.toURI()), Paths.get(backup_file.toURI()),
									StandardCopyOption.REPLACE_EXISTING);
						}

						File library_new_file = new File(
								scim_libray_path + File.separator + part.getName());

						InputStream inputStream = part.getInputStream();
						OutputStream outputStream = new FileOutputStream(library_new_file);
						IOUtils.copy(inputStream, outputStream);
						outputStream.close();

						logger.info("library patch : {} ", library_new_file.getCanonicalPath());
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
