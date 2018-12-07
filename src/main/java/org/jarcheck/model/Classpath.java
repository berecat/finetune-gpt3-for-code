package org.jarcheck.model;

import org.jarcheck.utils.MultiMap;

import java.util.*;

/**
 * Represents a set of JAR files found on a Java classpath.
 */
public class Classpath {

	/**
	 * List of JAR files on the classpath.
	 */
	private final List<JarFile> jarFiles;

	/**
	 * Fast lookup map for JAR files given the file name.
	 */
	private final Map<String, JarFile> jarFilesMap = new HashMap<>();

	/**
	 * Fast lookup map for class definitions given the class name.
	 */
	private final MultiMap<String, ClassDef> classDefsMap = new MultiMap<>();

	/**
	 * Create a new classpath with the given JAR files.
	 *
	 * @param jarFiles JAR files
	 * @throws IllegalArgumentException If <code>jarFiles</code> is <code>null</code>.
	 */
	public Classpath(List<JarFile> jarFiles) {
		if (jarFiles == null) throw new IllegalArgumentException("jarFiles");
		this.jarFiles = new ArrayList<>(jarFiles);

		// for every JAR file ...
		this.jarFiles.forEach(jarFile -> {

			// add JAR file to fast lookup map
			jarFilesMap.put(jarFile.getFileName(), jarFile);

			// for every class definition in this JAR file ...
			jarFile.getClassDefs().forEach(classDef -> {

				// add class definition to fast lookup map
				classDefsMap.add(classDef.getClassName(), classDef);
			});
		});
	}

	/**
	 * Returns an unmodifiable list of JAR files.
	 *
	 * @return JAR files
	 */
	public List<JarFile> getJarFiles() {
		return Collections.unmodifiableList(jarFiles);
	}

	/**
	 * Get the JAR file with the given file name,
	 * or <code>null</code> if the JAR file is not found in this classpath.
	 *
	 * @param fileName File name
	 * @return JAR file, or <code>null</code>
	 */
	public JarFile getJarFile(String fileName) {
		return jarFilesMap.get(fileName);
	}

	/**
	 * Get the class definitions with the given class name,
	 * or <code>null</code> if the class is not found in any JAR file.
	 *
	 * @param className Class name
	 * @return Class definitions, or <code>null</code>
	 */
	public Set<ClassDef> getClassDefs(String className) {
		return classDefsMap.getValues(className);
	}

	@Override
	public String toString() {
		return String.format("Classpath[%d]", jarFiles.size());
	}

}