package br.ufrn.dimap.middleware.utils.compiler;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;

public class JavaCompilerUtils {
    /**
     * Compile a .java into the respective folder.
     *
     * @param rootPath
     * @param fileName
     */
    public static void compile(String rootPath, String fileName) {
        File root = new File(rootPath);
        File sourceFile = new File(root, fileName);
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, sourceFile.getPath());
    }
}
