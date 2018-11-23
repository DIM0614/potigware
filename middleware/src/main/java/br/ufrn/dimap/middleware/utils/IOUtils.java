package br.ufrn.dimap.middleware.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class IOUtils {

    private IOUtils(){}

    public static byte[] readBytesFromInputStream(InputStream input) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int data = input.read();
        while(data != -1){
            buffer.write(data);
            data = input.read();
        }

        input.close();

        return buffer.toByteArray();
    }

    public static byte[] readAllBytesFromFile(String fileUrl) throws IOException {
        URL myUrl = new URL(fileUrl);

        URLConnection connection = myUrl.openConnection();
        InputStream input = connection.getInputStream();

        return IOUtils.readBytesFromInputStream(input);
    }

    public static void validateFilepath(String filePath, String extension) {
        if(filePath == null){
            throw new IllegalArgumentException("Filepath must not be null.");
        }

        if(!filePath.endsWith(extension)){
            throw new IllegalArgumentException("The file extension must be .class");
        }
    }
}
