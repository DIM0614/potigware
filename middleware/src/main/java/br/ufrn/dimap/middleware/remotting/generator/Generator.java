package br.ufrn.dimap.middleware.remotting.generator;

import br.ufrn.dimap.middleware.identification.AbsoluteObjectReference;
import br.ufrn.dimap.middleware.remotting.impl.ClientProxy;
import br.ufrn.dimap.middleware.remotting.impl.UnsyncRequestor;
import br.ufrn.dimap.middleware.remotting.interfaces.Callback;
import br.ufrn.dimap.middleware.remotting.interfaces.InvocationAsynchronyPattern;
import br.ufrn.dimap.middleware.remotting.interfaces.Requestor;
import br.ufrn.dimap.middleware.remotting.impl.Invocation;
import br.ufrn.dimap.middleware.remotting.interfaces.Invoker;

import com.squareup.javapoet.*;
import com.squareup.javapoet.MethodSpec.Builder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.lang.model.element.Modifier;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * This class generate interfaces, client proxies and invokers.
 *
 * @author Vinícius Campos
 * @author Artur Curinga
 */

public class Generator {

    private static String generateInterface(JSONObject file, Path path, String packageName) throws IOException, ClassNotFoundException {
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

        return interfaceName;
    }

    private static String generateProxy(JSONObject file, Path path, String packageName) throws IOException, ClassNotFoundException {
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
                    .addStatement("return " + getCastType(methodReturn) + " r.request(absoluteObjectReference,\"" + methodName + "\"," + stringParams + ")")
                    .addJavadoc(methodDescription)
                    .addException(ClassName.get("", "br.ufrn.dimap.middleware.remotting.impl.RemoteError"))
                    .build();

            MethodSpec msCallback = MethodSpec.methodBuilder(methodName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(void.class)
                    .addParameters(parameters)
                    .addParameter(Callback.class, "callback")
                    .addStatement("r.request(absoluteObjectReference,\"" + methodName + "\",callback," + stringParams + ")")
                    .addJavadoc(methodDescriptionCallback)
                    .addException(ClassName.get("", "br.ufrn.dimap.middleware.remotting.impl.RemoteError"))
                    .build();

            MethodSpec msAsync = MethodSpec.methodBuilder(methodName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(Object.class)
                    .addParameters(parameters)
                    .addParameter(InvocationAsynchronyPattern.class, "invocationAsyncPattern")
                    .addStatement("return r.request(absoluteObjectReference,\"" + methodName + "\",invocationAsyncPattern," + stringParams + ")")
                    .addJavadoc(methodDescriptionAsync)
                    .addException(ClassName.get("", "br.ufrn.dimap.middleware.remotting.impl.RemoteError"))
                    .build();

            ((ArrayList<MethodSpec>) methods).add(ms);
            ((ArrayList<MethodSpec>) methods).add(msCallback);
            ((ArrayList<MethodSpec>) methods).add(msAsync);
        }

        // Creating field requestor
        FieldSpec r = FieldSpec.builder(Requestor.class, "r")
                .addModifiers(Modifier.PRIVATE)
                .build();

        // Defining constructor of class
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(AbsoluteObjectReference.class, "absoluteObjectReference")
                .addStatement("super(absoluteObjectReference)")
                .addStatement("this.r = new $T()", UnsyncRequestor.class)
                .build();

        String proxyName = "Client" + className;

        TypeSpec classType = TypeSpec.classBuilder(proxyName)
                .addModifiers(Modifier.PUBLIC)
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
        return proxyName;
    }
    
    private static String generateInvoker(JSONObject file, Path path, String packageName) throws IOException, ClassNotFoundException {
    	String className = (String )file.get("name");
        String classDescription = (String )file.get("description");

        Builder invoke = MethodSpec.methodBuilder("invoke")
        		.returns(Object.class)
    		   	.addModifiers(Modifier.PUBLIC)
    		   	.addParameter(Invocation.class, "invocation")
                .addException(ClassName.get("", "br.ufrn.dimap.middleware.remotting.impl.RemoteError"))
        		.addStatement("Object[] params = invocation.getInvocationData().getActualParams()");
        
        JSONArray operations = (JSONArray) file.get("operations");
        Iterable<MethodSpec> methods = new ArrayList<MethodSpec>();
        for(int i = 0; i < operations.size(); ++i){
            JSONObject method        = (JSONObject) operations.get(i);
            String methodName        = (String) method.get("name");
            String methodDescription = (String) method.get("description");
            String methodReturn      = (String) method.get("return");
            
            String methodInReturn    = "return " + methodName + "(";
            
            invoke.beginControlFlow("if (invocation.getInvocationData().getOperationName().equals( \"" + methodName + "\" ))" );
            	  
            JSONArray params = (JSONArray) method.get("params");
            Iterable<ParameterSpec> parameters = new ArrayList<ParameterSpec>();
            for (int j = 0; j < params.size(); j++) {
                JSONObject param        = (JSONObject) params.get(j);
                String paramName        = (String) param.get("name");
                String paramType        = (String) param.get("type");
                String paramDescription = (String) param.get("description");            
               
                if(j != params.size() -1)
                	methodInReturn += " (" + paramType + ") params[" + j + "], ";
                else
                	methodInReturn += " (" + paramType + ") params[" + j + "] ";
                methodDescription += "\n@param " + paramName + " " + paramDescription;

                ParameterSpec ps = ParameterSpec.builder(getType(paramType), paramName).build();
                ((ArrayList<ParameterSpec>) parameters).add(ps);
                
                
            }
            	methodInReturn += ")";
            invoke.addStatement(methodInReturn)
                  .endControlFlow();

            methodDescription += "\n@return " + methodReturn;

            MethodSpec ms = MethodSpec.methodBuilder(methodName)
                    .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                    .returns(getType(methodReturn))
                    .addParameters(parameters)
                    .addJavadoc(methodDescription)
                    .addException(ClassName.get("", "br.ufrn.dimap.middleware.remotting.impl.RemoteError")) // change the real package name of class exception.RemoteError
                    .build();
            ((ArrayList<MethodSpec>) methods).add(ms);
        }

        invoke.addStatement("return null");
        
        // Creating fields of aor and requestor
        FieldSpec id = FieldSpec.builder(Integer.class, "id")
                .addModifiers(Modifier.PRIVATE)
                .build();

        // Defining constructor of class
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Integer.class, "id")
                .addStatement("this.id = id")
                .build();
        
        MethodSpec getId = MethodSpec.methodBuilder("getId")
        				             .addModifiers(Modifier.PUBLIC)
        				             .returns(Integer.class)
        				             .addStatement("return id")
        				             .build();
        
        MethodSpec setId = MethodSpec.methodBuilder("setId")
	             .addModifiers(Modifier.PUBLIC)
	             .addParameter(Integer.class, "id")
	             .addStatement("this.id =  id")
	             .build();

        String invokerName = className + "Invoker";

        TypeSpec classType = TypeSpec.classBuilder(invokerName)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addField(id)
                .addMethod(constructor)
                .addMethod(getId)
                .addMethod(setId)
                .addMethods(methods)
                .addMethod(invoke.build())
                .addJavadoc(classDescription)
                .addSuperinterface(ClassName.get("", className))
                .addSuperinterface(Invoker.class)
                .build();

        JavaFile javaFile = JavaFile.builder(packageName, classType)
                .build();

        javaFile.writeTo(path);
        return invokerName;
    }
    
    private static Type getType(String type) throws ClassNotFoundException {
        String parts[] = type.split("\\[", 2);
        if(parts.length == 1)
            return Class.forName("java.lang." + getObjectType(type));
        else{
            String base = getObjectType(parts[0]);
            int count = parts[1].length() - parts[1].replace("]",  "").length();
            String squares = "";
            for(int i = 0; i < count; ++i)
                squares += "[";
            return Class.forName(squares + "Ljava.lang." + base + ";");
        }
    }

    private static String getCastType(String type){
        String parts[] = type.split("\\[", 2);
        if(parts.length == 1)
            return "(" + getObjectType(type) + ")";
        else{
            return "(" + getObjectType(parts[0]) + "[" + parts[1] + ")";
        }
    }

    private static String getObjectType(String baseType){
        switch (baseType){
            case "int":
                return "Integer";
            case "float":
                return "Float";
            case "bool":
                return "Boolean";
            case "char":
                return "Character";
            case "string":
                return "String";
            default:
                return "Void";
        }
    }

    /**
     *
     *
     */
    public static class GeneratedFilesInfo {
        final String interfName;
        final String proxyName;
        final String invokerName;

        public GeneratedFilesInfo(final String interfName, final String proxyName, final String invokerName) {
            this.interfName = interfName;
            this.proxyName = proxyName;
            this.invokerName = invokerName;
        }

        public String getInterfName() {
            return interfName;
        }

        public String getProxyName() {
            return proxyName;
        }

        public String getInvokerName() {
            return invokerName;
        }
    }

    /**
     * Method to generate the interface, client proxy and invoker for a specific description interface.
     *
     * @param interfaceDescriptionURL the path of interface description
     * @param pathToSave path to save the files
     * @param packageName package of files
     * @throws IOException
     * @throws ParseException
     */
    public static GeneratedFilesInfo generateFiles(String interfaceDescriptionURL, String pathToSave, String packageName) throws IOException, ParseException, ClassNotFoundException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(interfaceDescriptionURL));
        JSONObject jsonObject = (JSONObject) obj;
        Path path = Paths.get(pathToSave);
        String interfName = generateInterface(jsonObject, path, packageName);
        String proxyName = generateProxy(jsonObject, path, packageName);
        String invokerName = generateInvoker(jsonObject, path, packageName);
        return new GeneratedFilesInfo(interfName, proxyName, invokerName);
    }
}