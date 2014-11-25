import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Main {

    public static void main(String[] args) throws IOException{
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
        try (
            OutputStream outputStream = new ByteArrayOutputStream()
        ) {
            Implementor impl = new Implementor(c);
            impl.generateClass(outputStream);
            System.out.println(outputStream.toString());
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

}
