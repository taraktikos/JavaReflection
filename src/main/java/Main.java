

public class Main {

    public static void main(String[] args) {
        Class c;
        try {
            c = Class.forName("java.util.Collection");
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found");
            return;
        }
        Implementor impl = new Implementor(c);
        impl.print();
    }

}
