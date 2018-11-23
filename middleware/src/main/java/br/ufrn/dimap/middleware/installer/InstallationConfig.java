package br.ufrn.dimap.middleware.installer;

/**
 *
 * Class that holds the configurations for the middleware installer component.
 *
 * @author Daniel Smith
 */
public class InstallationConfig {
    /**
     * Represents the location where the compiled files are placed.
     */
    public static final String DEFAULT_COMPILER_TARGET_PACKAGE = "generated";

    public static String getClasspathLocation(String targetDir) {
        return String.format("%s/%s/", targetDir, DEFAULT_COMPILER_TARGET_PACKAGE);
    }

    public static String getTargetDir() {
        return String.format("%s/src/main/java/", System.getProperty("user.dir"));
    }

    public static String getClassFileLocation(String targetDir, String classname) {
        return String.format("file:%s/%s/%s.class", targetDir, DEFAULT_COMPILER_TARGET_PACKAGE, classname);
    }

    public static String getClassname(String classname) {
        return String.format("%s.%s", DEFAULT_COMPILER_TARGET_PACKAGE, classname);
    }

    public static String getSourcecodeFilePath(String className) {
        return String.format("%s/%s.java", DEFAULT_COMPILER_TARGET_PACKAGE, className);
    }

    public static String getClassResource(Class<?> klass) {
        return klass.getClassLoader().getResource(
                klass.getName().replace('.', '/') + ".class").toString();
    }
}
