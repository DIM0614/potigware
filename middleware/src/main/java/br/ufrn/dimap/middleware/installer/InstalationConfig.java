package br.ufrn.dimap.middleware.installer;

public class InstalationConfig {
    public static String getClasspathLocation(String targetDir) {
        return String.format("%s/%s/", targetDir, ClientInstaller.DEFAULT_COMPILER_TARGET_PACKAGE);
    }

    public static String getTargetDir() {
        return String.format("%s/middleware/src/main/java/", System.getProperty("user.dir"));
    }

    public static String getClassFileLocation(String targetDir, String classname) {
        return String.format("file:%s/%s/%s.class", targetDir, ClientInstaller.DEFAULT_COMPILER_TARGET_PACKAGE, classname);
    }

    public static String getClassname(String classname) {
        return String.format("%s.%s", ClientInstaller.DEFAULT_COMPILER_TARGET_PACKAGE, classname);
    }

    public static String getSourcecodeFilePath(String className) {
        return String.format("%s/%s.java", ClientInstaller.DEFAULT_COMPILER_TARGET_PACKAGE, className);
    }

    public static String getClassResource(Class<?> klass) {
        return klass.getClassLoader().getResource(
                klass.getName().replace('.', '/') + ".class").toString();
    }
}
