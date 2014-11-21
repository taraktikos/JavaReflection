import com.sun.org.apache.xpath.internal.operations.Mod;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Implementor {

    private Class classEntity;

    private static Pattern p = Pattern.compile("\\w+\\.");

    private final String methodTemplate = "" +
            "\tpublic @type @name(@parameters) {\n" +
                "\t\treturn @default\n" +
            "\t}\n";

    public Implementor(Class cl) throws RuntimeException{
        classEntity = cl;
        if (Modifier.isFinal(cl.getModifiers())) {
            throw new RuntimeException("Class is final");
        }
    }

    private String parametersToString(TypeVariable[] typeVariables) {
        StringBuilder result = new StringBuilder();
        if (typeVariables.length > 0) {
            result.append("<");
            for (int i = 0; i < typeVariables.length; i++) {
                result.append(typeVariables[i].toString());
                if (i != typeVariables.length - 1) {
                    result.append(", ");
                }
            }
            result.append(">");
        }
        return result.toString();
    }

    private String generateMethods() {
        StringBuilder result = new StringBuilder();
        //todo recursion
        for (Method method: classEntity.getDeclaredMethods()) {
            result.append("\t");
            result.append(Modifier.toString(method.getModifiers()).replace(" abstract", ""));
            result.append(" ");
            String typeParameters = parametersToString(method.getTypeParameters());
            if (typeParameters.length() > 0) {
                typeParameters += " ";
            }
            result.append(typeParameters);
            result.append(method.getGenericReturnType().toString().replace("[Ljava.lang.Object;", "java.lang.Object[]").replace("class ", ""));
            result.append(" ");
            result.append(method.getName());
            result.append("(");
            Type[] types = method.getGenericParameterTypes();
            for (int i = 0; i < types.length; i++) {
                result.append(types[i].toString().replace("class ", ""));
                result.append(" arg");
                result.append(i);
                if (i != types.length - 1) {
                    result.append(", ");
                }
            }
            result.append(") ");
            Class<?>[] exceptionTypes = method.getExceptionTypes();
            if (exceptionTypes.length > 0) {
                result.append("throws ");
                for (int i = 0; i < exceptionTypes.length; i++) {
                    result.append(exceptionTypes[i].toString());
                    if (i != types.length - 1) {
                        result.append(", ");
                    }
                }
                result.append(" ");
            }
            result.append("{\n");
            result.append("\t\treturn");
            if (!method.getReturnType().equals(Void.TYPE)) {
                if (method.getReturnType().equals(Boolean.TYPE)) {
                    result.append(" false");
                } else if (method.getReturnType().isPrimitive()) {
                    result.append(" 0");
                } else {
                    result.append(" null");
                }
            }
            result.append(";");
            result.append("\n\t}\n");
//            if (Modifier.isAbstract(method.getModifiers())) {
//
//            } else {
//
//            }
        }
        return result.toString();
    }

    private void generateClass() {
        TypeVariable[] typeVariables = classEntity.getTypeParameters();
        StringBuilder result = new StringBuilder("public class " + classEntity.getSimpleName() + "Impl");
        String types = parametersToString(typeVariables);
        result.append(types);
        result.append(" ");
        result.append(classEntity.isInterface() ? "implements" : "extends");
        result.append(" ");
        result.append(classEntity.getCanonicalName());
        result.append(types);
        result.append(" {\n");
        result.append(generateMethods());


        result.append("}");
        //System.out.println(result);
        PrintWriter writer = null;
        try {
            String path = "./src/main/java/" + classEntity.getSimpleName() + "Impl.java";
            File f = new File(path);
            f.createNewFile();
            writer = new PrintWriter(path);
            writer.print(result);
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void print() {
        generateClass();
        String className = p.matcher(classEntity.getName()).replaceAll("");
        String newClassName = className + "Impl";
        if (classEntity.isInterface()) {
            newClassName += " implements ";
        } else {
            newClassName += " extends ";
        }
        newClassName += className;
        StringBuilder result = new StringBuilder("public class " + newClassName + " {\n");
        Arrays.asList(classEntity.getConstructors()).forEach(
            constructor -> p.matcher(constructor.toString()).replaceAll("")
        );

        for (Method method: classEntity.getDeclaredMethods()) {
            if (Modifier.isPrivate(method.getModifiers())) {
                continue;
            }
            String methodString = methodTemplate;
            methodString = methodString.replace("@name", method.getName());
            result.append(methodString);
            //result
//            System.out.println(p.matcher(method.toString()).replaceAll(""));
//            System.out.println(method.getDefaultValue());
//            System.out.println(method.getReturnType());
//            System.out.println(Arrays.toString(method.getParameters()));
        }
        result.append("}");
        System.out.println(result);



    }
}
