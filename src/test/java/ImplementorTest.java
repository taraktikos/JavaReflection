import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ImplementorTest {

    Implementor implementor;

    @Before
    public void setUp() throws Exception {
        implementor = new Implementor(Object.class);
    }

    @Test
    public void testCorrectTypeString() throws Exception {
        assertEquals("java.lang.Object[]", this.implementor.correctTypeString(new Object[2].getClass()));
        assertEquals("java.lang.String[][]", this.implementor.correctTypeString(new String[2][2].getClass()));
        assertEquals("int[]", this.implementor.correctTypeString(new int[1].getClass()));
        assertEquals("boolean[]", this.implementor.correctTypeString(new boolean[1].getClass()));
    }

}