import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.*;
import java.util.HashSet;

public class Implementor {

    private Class classEntity;

    public Implementor(Class cl) throws RuntimeException{
        classEntity = cl;
        if (Modifier.isFinal(cl.getModifiers())) {
            throw new RuntimeException("Class is final");
        }
    }

    public void generateClass(OutputStream output) throws IOException{
        TypeVariable[] typeVariables = classEntity.getTypeParameters();
        String newName = classEntity.getSimpleName() + "Impl";
        StringBuilder result = new StringBuilder("public class " + newName);
        String types = parametersToString(typeVariables);
        result.append(types);
        result.append(" ");
        result.append(classEntity.isInterface() ? "implements" : "extends");
        result.append(" ");
        result.append(classEntity.getCanonicalName());
        result.append(types);
        result.append(" {\n");

        HashSet<Method> methods = new HashSet<>();
        addAllMethods(classEntity, methods);
        output.write(result.toString().getBytes());
        for (Method method: methods) {
            output.write(generateMethod(method).getBytes());
        }
        output.write("}".getBytes());
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

    private void addAllMethods(Class<?> baseClass, HashSet<Method> methods) {
        for (Method method: baseClass.getDeclaredMethods()) {
            if (Modifier.isAbstract(method.getModifiers())) {
                methods.add(method);
            }
        }
        if (baseClass.getSuperclass() != null) {
            addAllMethods(baseClass.getSuperclass(), methods);
        }
    }

    String correctTypeString(Type type) {
        if (type instanceof Class) {
            return ((Class) (type)).getCanonicalName();
        } else {
            return type.toString();
        }
    }

    private String generateMethod(Method method) {
        StringBuilder result = new StringBuilder();
        result.append("\t");
        result.append(Modifier.toString(method.getModifiers()).replace(" abstract", ""));
        result.append(" ");
        String typeParameters = parametersToString(method.getTypeParameters());
        if (typeParameters.length() > 0) {
            typeParameters += " ";
        }
        result.append(typeParameters);
        result.append(correctTypeString(method.getGenericReturnType()));
        result.append(" ");
        result.append(method.getName());
        result.append("(");
        Type[] types = method.getGenericParameterTypes();
        for (int i = 0; i < types.length; i++) {
            result.append(correctTypeString(types[i]));
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
        return result.toString();
    }
}
