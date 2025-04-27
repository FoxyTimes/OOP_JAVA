import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalcTest {
    private Calc calc;

    @BeforeEach
    void setUp() {
        calc = new Calc();
    }

    @Test
    void testCalc() {
        // Пример теста, проверяющего корректное поведение с валидным файлом
        try {
            calc.start("");
        } catch (Exception e) {
            fail("Тест завершился с исключением: " + e.getMessage());
        }

        try {
            calc.start("src/test.txt");
        } catch (Exception e) {
            fail("Тест завершился с исключением: " + e.getMessage());
        }
    }
}
