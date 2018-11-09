package br.ufrn.dimap.middleware.remotting.generator;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AppGenerator {
    public static void main(String[] args) {
        Generator generator = new Generator();

        JSONParser parser = new JSONParser();
        File directory = new File("./");
        try {
            String jsonURL = "/home/vinihcampos/Desktop/example.json";
            String interfaceURL = directory.getCanonicalPath() + "/src/main/java/";
            Object obj = parser.parse(new FileReader(jsonURL));
            JSONObject jsonObject = (JSONObject) obj;
            Path path = Paths.get(interfaceURL);
            generator.generateInterface(jsonObject, path, "br.ufrn.dimap.middleware.remotting.generator");
            generator.generateClass(jsonObject, path, "br.ufrn.dimap.middleware.remotting.generator");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
