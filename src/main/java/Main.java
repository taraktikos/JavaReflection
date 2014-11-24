
public class Main {

    public static void main(String[] args) {
        String className = "java.util.List";
        if (args.length > 0) {
            className = args[0];
        }
        Class c;
        try {
            c = Class.forName(className);
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found");
            return;
        }
        try {
            Implementor impl = new Implementor(c);
            impl.generateClass();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

}
