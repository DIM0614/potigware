package br.ufrn.dimap.middleware.remotting.generator;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.remotting.impl.ClientProxy;
import br.ufrn.dimap.middleware.remotting.impl.UnsyncRequestor;
import br.ufrn.dimap.middleware.remotting.interfaces.Callback;
import br.ufrn.dimap.middleware.remotting.interfaces.InvocationAsynchronyPattern;
import br.ufrn.dimap.middleware.remotting.interfaces.Requestor;
import com.squareup.javapoet.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * This class generate interfaces, client proxies and invokers.
 *
 * @author Vinícius Campos
 */

public class Generator {

    public void generateInterface(JSONObject file, Path path, String packageName) throws IOException {
        String interfaceName = (String )file.get("name");
        String interfaceDescription = (String )file.get("description");

        JSONArray operations = (JSONArray) file.get("operations");
        Iterable<MethodSpec> methods = new ArrayList<MethodSpec>();
        for(int i = 0; i < operations.size(); ++i){
            JSONObject method = (JSONObject) operations.get(i);
            String methodName = (String) method.get("name");
            String methodDescription = (String) method.get("description");
            String methodReturn = (String) method.get("return");

            JSONArray params = (JSONArray) method.get("params");
            Iterable<ParameterSpec> parameters = new ArrayList<ParameterSpec>();
            for (int j = 0; j < params.size(); j++) {
                JSONObject param = (JSONObject) params.get(j);
                String paramName = (String) param.get("name");
                String paramType = (String) param.get("type");
                String paramDescription = (String) param.get("description");

                methodDescription += "\n@param " + paramName + " " + paramDescription;

                ParameterSpec ps = ParameterSpec.builder(getType(paramType), paramName).build();
                ((ArrayList<ParameterSpec>) parameters).add(ps);
            }

            methodDescription += "\n@return " + methodReturn;

            MethodSpec ms = MethodSpec.methodBuilder(methodName)
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .returns(getType(methodReturn))
                    .addParameters(parameters)
                    .addJavadoc(methodDescription)
                    .addException(ClassName.get("", "br.ufrn.dimap.middleware.remotting.impl.RemoteError"))
                    .build();
            ((ArrayList<MethodSpec>) methods).add(ms);
        }

        TypeSpec interfaceType = TypeSpec.interfaceBuilder(interfaceName)
                .addModifiers(Modifier.PUBLIC)
                .addMethods(methods)
                .addJavadoc(interfaceDescription)
                .build();

        JavaFile javaFile = JavaFile.builder(packageName, interfaceType)
                .build();

        javaFile.writeTo(path);
    }

    public void generateProxy(JSONObject file, Path path, String packageName) throws IOException {
        String className = (String )file.get("name");
        String classDescription = (String )file.get("description");

        JSONArray operations = (JSONArray) file.get("operations");
        Iterable<MethodSpec> methods = new ArrayList<>();
        for(int i = 0; i < operations.size(); ++i){
            JSONObject method = (JSONObject) operations.get(i);
            String methodName = (String) method.get("name");
            String methodDescription = (String) method.get("description");
            String methodReturn = (String) method.get("return");

            String methodDescriptionParam = "";

            JSONArray params = (JSONArray) method.get("params");
            Iterable<ParameterSpec> parameters = new ArrayList<ParameterSpec>();
            String stringParams = "";
            for (int j = 0; j < params.size(); j++) {
                JSONObject param = (JSONObject) params.get(j);
                String paramName = (String) param.get("name");
                String paramType = (String) param.get("type");
                String paramDescription = (String) param.get("description");

                methodDescriptionParam += "\n@param " + paramName + " " + paramDescription;

                ParameterSpec ps = ParameterSpec.builder(getType(paramType), paramName).build();
                ((ArrayList<ParameterSpec>) parameters).add(ps);

                stringParams += paramName;
                if(j + 1 < params.size())
                    stringParams += ",";
            }

            String methodDescriptionCallback = methodDescription + "\n@param callback" + methodDescriptionParam;
            String methodDescriptionAsync = methodDescription + "\n@param invocationAsyncPattern" + methodDescriptionParam + "\n@return Object";
            methodDescription += methodDescriptionParam + "\n@return " + methodReturn;

            MethodSpec ms = MethodSpec.methodBuilder(methodName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(getType(methodReturn))
                    .addParameters(parameters)
                    .addStatement("return " + getCastType(methodReturn) + " r.request(aor,\"" + methodName + "\"," + stringParams + ")")
                    .addJavadoc(methodDescription)
                    .addException(ClassName.get("", "br.ufrn.dimap.middleware.remotting.impl.RemoteError"))
                    .build();

            MethodSpec msCallback = MethodSpec.methodBuilder(methodName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(getType("void"))
                    .addParameters(parameters)
                    .addParameter(Callback.class, "callback")
                    .addStatement("r.request(aor,\"" + methodName + "\",callback," + stringParams + ")")
                    .addJavadoc(methodDescriptionCallback)
                    .addException(ClassName.get("", "br.ufrn.dimap.middleware.remotting.impl.RemoteError"))
                    .build();

            MethodSpec msAsync = MethodSpec.methodBuilder(methodName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(Object.class)
                    .addParameters(parameters)
                    .addParameter(InvocationAsynchronyPattern.class, "invocationAsyncPattern")
                    .addStatement("return r.request(aor,\"" + methodName + "\",invocationAsyncPattern," + stringParams + ")")
                    .addJavadoc(methodDescriptionAsync)
                    .addException(ClassName.get("", "br.ufrn.dimap.middleware.remotting.impl.RemoteError"))
                    .build();

            ((ArrayList<MethodSpec>) methods).add(ms);
            ((ArrayList<MethodSpec>) methods).add(msCallback);
            ((ArrayList<MethodSpec>) methods).add(msAsync);
        }

        // Creating fields of aor and requestor
        FieldSpec aor = FieldSpec.builder(AbsoluteObjectReference.class, "aor")
                .addModifiers(Modifier.PRIVATE)
                .build();
        FieldSpec r = FieldSpec.builder(Requestor.class, "r")
                .addModifiers(Modifier.PRIVATE)
                .build();

        // Defining constructor of class
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AbsoluteObjectReference.class, "aor")
                .addStatement("super(aor)")
                .addStatement("this.r = new $T()", UnsyncRequestor.class)
                .build();

        TypeSpec classType = TypeSpec.classBuilder("Client" + className)
                .addModifiers(Modifier.PUBLIC)
                .addField(aor)
                .addField(r)
                .addMethod(constructor)
                .addMethods(methods)
                .addJavadoc(classDescription)
                .superclass(ClientProxy.class)
                .addSuperinterface(ClassName.get("", className))
                .build();

        JavaFile javaFile = JavaFile.builder(packageName, classType)
                .build();

        javaFile.writeTo(path);
    }

    private Type getType(String type){
        if(type.equals("int")){
            return Integer.class;
        }else if(type.equals("float")){
            return Float.class;
        }else if(type.equals("boolean")){
            return Boolean.class;
        }else if(type.equals("string")){
            return String.class;
        }else if(type.equals("char")){
            return Character.class;
        }

        return void.class;
    }

    private String getCastType(String type){
        if(type.equals("int")){
            return "(Integer)";
        }else if(type.equals("float")){
            return "(Float)";
        }else if(type.equals("boolean")){
            return "(Boolean)";
        }else if(type.equals("string")){
            return "(String)";
        }else if(type.equals("char")){
            return "(Character)";
        }

        return "(Void)";
    }

}