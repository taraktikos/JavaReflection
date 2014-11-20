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

    public Implementor(Class cl) {
        classEntity = cl;
        System.out.println(p.matcher(cl.getName()).replaceAll(""));
    }

    public void print() {
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

        for (Method method: classEntity.getMethods()) {
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
       // System.out.println(methodTemplate);
    }
}
