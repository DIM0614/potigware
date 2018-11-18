package br.ufrn.dimap.middleware.utils.compiler;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class that provide static methods to deal with the Java Compiler.
 *
 * @author Daniel Smith
 * @author vitorgreati
 */
public class JavaCompilerUtils {

    /**
     * Constant that maps the result when the compiler completes with success.
     */
    public static final int COMPILER_SUCCESS = 0;

    /**
     * Compile a .java into the respective folder.
     *
     * @param rootPath the root path where the classes was generated.
     * @param fileNames the files to be compiled.
     */
    public static void compile(String rootPath, String... fileNames) {
        File root = new File(rootPath);

        List<String> strings = Arrays.asList(fileNames);

        List<String> filesToCompile = strings.stream()
                .map(string -> new File(root, string).getPath())
                .collect(Collectors.toList());

        int result = getJavaCompiler().run(null, null, System.out, filesToCompile.toArray(new String[filesToCompile.size()]));

        if(result != COMPILER_SUCCESS){
            throw new IllegalStateException("The compiler returned an error. Verify your source code.");
        }

    }

    /**
     * Returns the java compiler. Doesn't return null.
     *
     * @return a java compiler.
     */
    private static JavaCompiler getJavaCompiler() {
        return ToolProvider.getSystemJavaCompiler();
    }
}
