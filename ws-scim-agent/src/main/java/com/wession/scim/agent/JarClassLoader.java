package com.wession.scim.agent;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarClassLoader extends ClassLoader { 
	private List<String> loadedClasses;

	public JarClassLoader(String jarPath) throws FileNotFoundException, IOException {
		loadedClasses = new LinkedList<String>();
		loadClasses(jarPath);
	}

	public List<String> getLoadedClasses() {
		return loadedClasses;
	}

	private void loadClasses(String path) throws FileNotFoundException, IOException {
		JarFile jarFile = null;
		try {
			jarFile = new JarFile(path);

			Enumeration<JarEntry> entries = jarFile.entries();
			JarEntry entry = null;
			while (entries.hasMoreElements()) {
				entry = entries.nextElement();
				InputStream jarInputStream = null;
				try {
					String className = getClassName(entry);

					if (!className.contains("META-INF")) {
						jarInputStream = new BufferedInputStream(jarFile.getInputStream(entry));
						byte[] data = new byte[(int) entry.getSize()];

						// jarInputStream.read(data);

						int left = data.length;
						int pos = 0;
						while (left > 0) {
							int n = jarInputStream.read(data, pos, left);
							left -= n;
							pos += n;
						}

						defineClass(className, data, 0, data.length);
						this.loadedClasses.add(className);
					} else {
						System.out.println("Except Jar File : " + className);
					}
				} // end of try
				finally {
					if (null != jarInputStream)
						try {
							jarInputStream.close();
						} catch (Exception e) {
							/* ignore */} finally {
							jarInputStream = null;
						}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jarFile != null)
				try {
					jarFile.close();
				} catch (Exception e) {
					/* ignore */ } finally {
					jarFile = null;
				}
		}
	}

	private String getClassName(JarEntry classEntry) {
		return classEntry.getName().replace(".class", "").replace("/", ".");
	}
}