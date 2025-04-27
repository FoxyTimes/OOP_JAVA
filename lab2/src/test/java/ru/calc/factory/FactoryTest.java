package ru.calc.factory;

import ru.calc.errors.NoSuchInstructionException;
import ru.calc.instructions.BaseInst;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class FactoryTest {

    private Factory factory;

    @BeforeEach
    void setUp() throws IOException {
        this.factory = new Factory();
    }

    @Test
    void testFactory() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        BaseInst instruction = this.factory.createInst("PUSH");
        BaseInst instruction2 = this.factory.createInst("PUSH");
        assertEquals(instruction, instruction2);
        try {
            BaseInst instruction3 = this.factory.createInst("undefended");
            fail("завершилось без ошибки на undefended инструкцию");
        }
        catch (NoSuchInstructionException e) {}
    }

}
