package br.ufrn.dimap.middleware.remotting.generator;
import org.json.simple.parser.ParseException;
import java.io.IOException;

public class AppGenerator {

    public static final String INTERFACE_DESCRIPTION = "/home/vinihcampos/Desktop/example.json";
    public static final String PATH_TO_SAVE = "/home/vinihcampos/Documents/git-projects/github/middleware/middleware/src/main/java/";
    public static final String PACKAGE_NAME = "br.ufrn.dimap.middleware.remotting.generator";


    public static void main(String[] args) {
        try {
            Generator.generateFiles(INTERFACE_DESCRIPTION, PATH_TO_SAVE, PACKAGE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
