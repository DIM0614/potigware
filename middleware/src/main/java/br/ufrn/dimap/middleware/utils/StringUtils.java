package br.ufrn.dimap.middleware.utils;

public class StringUtils {
    private StringUtils(){}

    public static String getFileName(String filePath){
        String raw = filePath.replace(".class", "");
        String path[] = raw.split("/");
        return path[ path.length - 1 ];
    }
}
