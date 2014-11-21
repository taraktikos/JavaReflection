import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Integer> list = new ListImpl<>();
        Class c;
        try {
            c = Class.forName("java.util.AbstractList");
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found");
            return;
        }
        try {
            Implementor impl = new Implementor(c);
            impl.print();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

}
