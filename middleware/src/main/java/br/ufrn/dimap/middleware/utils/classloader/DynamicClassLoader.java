package br.ufrn.dimap.middleware.utils.classloader;

import br.ufrn.dimap.middleware.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * Basic implementation of a dynamic classloader.
 * <br/>
 * Allows to load and reload classes in runtime.
 *
 * @author Daniel Smith
 */
public class DynamicClassLoader extends ClassLoader {

    /**
     * Constant for the class file extension.
     */
    public static final String CLASS_FILE_EXTENSION = ".class";

    private static DynamicClassLoader instance;

    private DynamicClassLoader(ClassLoader parent){
        super(parent);
    }

    /**
     * Singleton access method for the DynamicClassLoader.
     *
     * @return a DynamicClassLoader instance.
     */
    public static DynamicClassLoader getDynamicClassLoader(){
        if(instance == null){
            synchronized (DynamicClassLoader.class){
                if(instance == null){
                    ClassLoader parentClassloader = DynamicClassLoader.class.getClassLoader();
                    instance = new DynamicClassLoader(parentClassloader);
                }
            }
        }

        return instance;
    }

    /**
     *  Load a class from a .class file.
     *
     * @param className the class name
     * @param filePath the location of the .class file.
     *
     * @return the class after the loading in the classloader.
     *
     * @throws IllegalArgumentException if the classname is null.
     * @throws IllegalArgumentException if the filepath is null.
     * @throws IllegalArgumentException if the filepath references a non .class file.
     */
    public Class loadClassFromFile(String className, String filePath) {
        Objects.requireNonNull(className, "Classname must not be null.");

        IOUtils.validateFilepath(filePath, CLASS_FILE_EXTENSION);

        Class clazz = loadFromClassLoader(className);
        if(clazz != null){
            return clazz;
        }

        try {
            byte[] classData = IOUtils.readAllBytesFromFile(filePath);

            return defineClass(className, classData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Load the class from the inputstream.
     *
     * @param stream the inputStream that represents the .class file to be read.
     * @param className the class name.
     * @return the defined class in classloader.
     *
     * @throws IOException if occurs any error during the stream reading.
     * @throws IllegalArgumentException if the classname is null.
     */
    public Class loadClassFromInputStream(InputStream stream, String className) throws IOException {
        Objects.requireNonNull(className, "Classname must not be null.");

        Class clazz = loadFromClassLoader(className);

        if(clazz != null){
            return clazz;
        }

        return defineClass(className, IOUtils.readBytesFromInputStream(stream));
    }

    /**
     * Load a class and return it if available in the classloader.
     *
     * @param className the class name.
     *
     * @return the class if available. Null if the class is not available in the classloader.
     */
    private Class loadFromClassLoader(String className){
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * Defines the class in the classloader.
     *
     * @param className the class name of the class to be defined.
     * @param classData the byte array containing the class file data.
     *
     * @return the class after loaded in the class loader.
     */
    private Class defineClass(String className, byte[] classData) {
        if(classData == null){
            throw new IllegalArgumentException("The argument classData must not be null.");
        }

        if(classData.length == 0){
            throw new IllegalArgumentException("The classData length must not be 0.");
        }

        return defineClass(className,classData, 0, classData.length);
    }

}
