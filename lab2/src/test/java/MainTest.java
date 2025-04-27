import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class MainTest {
    @Test
    void testMain() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        try {
            String[] args = new String[0];
            Main.main(args);
        }
        catch (Exception e) {
            fail("ошибка в мейне");
        }
        try {
            String[] args = new String[1];
            args[0] = "1";
            Main.main(args);
        }
        catch (Exception e) {
            fail("ошибка в мейне");
        }
    }
}
