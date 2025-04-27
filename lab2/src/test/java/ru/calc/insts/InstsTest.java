package ru.calc.insts;

import ru.calc.arguments.BaseArgument;
import ru.calc.arguments.NameArgument;
import ru.calc.arguments.NumberArgument;
import ru.calc.context.Context;
import ru.calc.errors.*;
import ru.calc.instructions.BaseInst;
import ru.calc.factory.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InstsTest {

    private Factory factory;
    private Context context;

    @BeforeEach
    void setUp() throws IOException {
        this.factory = new Factory();
        this.context = new Context();
    }

    @Test
    void testPushInstRight() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        BaseInst instruction = this.factory.createInst("PUSH");
        try {
            List<BaseArgument<?>> arguments = new ArrayList<>();
            BaseArgument<Double> testArgument = new NumberArgument(10);
            arguments.add(testArgument);
            instruction.execute(arguments, context);
            assertEquals(10, context.getFirstStack());

            arguments.clear();
            BaseArgument<String> testArgument2 = new NameArgument("a");
            arguments.add(testArgument2);
            context.putDefine("a", 1.0);
            instruction.execute(arguments, context);
        } catch (Exception e) {
            fail("Тест завершился с исключением: " + e.getMessage());
        }
    }

    @Test
    void testPushInstWithErrors() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        BaseInst instruction = this.factory.createInst("PUSH");
        try {
            List<BaseArgument<?>> arguments = new ArrayList<>();
            BaseArgument<String> testArgument = new NameArgument("a");
            arguments.add(testArgument);
            instruction.execute(arguments, context);
            assertEquals(10, context.getFirstStack());
            fail("Ошибка на undefined переменную");
        }
        catch (NotDefinedVariableException e) {}
        try {
            List<BaseArgument<?>> arguments = new ArrayList<>();
            instruction.execute(arguments, context);
            assertEquals(10, context.getFirstStack());
            fail("Ошибка на undefined переменную");
        }
        catch (ArgumentsException e) {}
    }

    @Test
    void testPopInstRight() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        BaseInst instruction = this.factory.createInst("POP");
        try {
            List<BaseArgument<?>> arguments = new ArrayList<>();
            context.pushStack(1);
            instruction.execute(arguments, context);
            if (!context.isEmptyStack()) {
                fail("стек не пуст");
            }
        } catch (Exception e) {
            fail("Тест завершился с исключением: " + e.getMessage());
        }
    }


    @Test
    void testPopInstWithErrors() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        BaseInst instruction = this.factory.createInst("POP");
        try {
            List<BaseArgument<?>> arguments = new ArrayList<>();
            instruction.execute(arguments, context);
            fail("pop без ошибок");
        } catch (StackPopException e) {}
    }

    @Test
    void testPlusInstRight() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        BaseInst instruction = this.factory.createInst("+");
        try {
            List<BaseArgument<?>> arguments = new ArrayList<>();
            context.pushStack(1);
            context.pushStack(1);
            instruction.execute(arguments, context);
            assertEquals(2, context.getFirstStack());
            if (context.getSizeStack()!=1) {
                fail("неправильный размер стека");
            }
        } catch (Exception e) {
            fail("Тест завершился с исключением: " + e.getMessage());
        }
    }

    @Test
    void testPlusInstWithErrors() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        BaseInst instruction = this.factory.createInst("+");
        try {
            List<BaseArgument<?>> arguments = new ArrayList<>();
            instruction.execute(arguments, context);
            fail("pop без ошибок");
        } catch (StackPopException e) {}
    }

    @Test
    void testMinInstRight() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        BaseInst instruction = this.factory.createInst("-");
        try {
            List<BaseArgument<?>> arguments = new ArrayList<>();
            context.pushStack(1);
            context.pushStack(1);
            instruction.execute(arguments, context);
            assertEquals(0, context.getFirstStack());
            if (context.getSizeStack()!=1) {
                fail("неправильный размер стека");
            }
        } catch (Exception e) {
            fail("Тест завершился с исключением: " + e.getMessage());
        }
    }

    @Test
    void testMinInstWithErrors() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        BaseInst instruction = this.factory.createInst("-");
        try {
            List<BaseArgument<?>> arguments = new ArrayList<>();
            instruction.execute(arguments, context);
            fail("pop без ошибок");
        } catch (StackPopException e) {}
    }

    @Test
    void testMulInstRight() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        BaseInst instruction = this.factory.createInst("*");
        try {
            List<BaseArgument<?>> arguments = new ArrayList<>();
            context.pushStack(2);
            context.pushStack(3);
            instruction.execute(arguments, context);
            assertEquals(6, context.getFirstStack());
            if (context.getSizeStack()!=1) {
                fail("неправильный размер стека");
            }
        } catch (Exception e) {
            fail("Тест завершился с исключением: " + e.getMessage());
        }
    }

    @Test
    void testMulInstWithErrors() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        BaseInst instruction = this.factory.createInst("*");
        try {
            List<BaseArgument<?>> arguments = new ArrayList<>();
            assertThrows(StackPopException.class, () -> {
                instruction.execute(arguments, context);
            });
            fail("pop без ошибок");
        } catch (StackPopException e) {}
    }

    @Test
    void testDelInstRight() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        BaseInst instruction = this.factory.createInst("/");
        try {
            List<BaseArgument<?>> arguments = new ArrayList<>();
            context.pushStack(2);
            context.pushStack(4);
            instruction.execute(arguments, context);
            assertEquals(2, context.getFirstStack());
            if (context.getSizeStack()!=1) {
                fail("неправильный размер стека");
            }
        } catch (Exception e) {
            fail("Тест завершился с исключением: " + e.getMessage());
        }
    }

    @Test
    void testDelInstWithErrors() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        BaseInst instruction = this.factory.createInst("/");
        try {
            List<BaseArgument<?>> arguments = new ArrayList<>();
            instruction.execute(arguments, context);
            fail("pop без ошибок");
        } catch (StackPopException e) {}
        try {
            List<BaseArgument<?>> arguments = new ArrayList<>();
            context.pushStack(0);
            context.pushStack(0);
            instruction.execute(arguments, context);
            fail("деление на ноль без ошибки");
        } catch (DivisionByZeroException e) {}
    }

    @Test
    void testDefInstRight() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        BaseInst instruction = this.factory.createInst("DEFINE");
        try {
            List<BaseArgument<?>> arguments = new ArrayList<>();
            BaseArgument<String> argument = new NameArgument("a");
            BaseArgument<Double> argument2 = new NumberArgument(2);
            arguments.add(argument);
            arguments.add(argument2);
            instruction.execute(arguments, context);
            assertEquals(2, context.getDefine("a"));
        } catch (Exception e) {
            fail("Тест завершился с исключением: " + e.getMessage());
        }
    }

    @Test
    void testDefInstWithErrors() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        BaseInst instruction = this.factory.createInst("DEFINE");
        try {
            List<BaseArgument<?>> arguments = new ArrayList<>();
            instruction.execute(arguments, context);
            fail("pop без ошибок");
        } catch (ArgumentsException e) {}
        try {
            List<BaseArgument<?>> arguments = new ArrayList<>();
            BaseArgument<String> argument = new NameArgument("a");
            BaseArgument<String> argument2 = new NameArgument("b");
            arguments.add(argument);
            arguments.add(argument2);
            instruction.execute(arguments, context);
            fail("неправильные типы прошли Define");
        } catch (FalseTypeException e) {}
    }

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @Test
    void testPrintInstRight() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        BaseInst instruction = this.factory.createInst("PRINT");
        System.setOut(new PrintStream(outputStream));
        try {
            List<BaseArgument<?>> arguments = new ArrayList<>();
            context.pushStack(4);
            instruction.execute(arguments, context);
            assertEquals(4.0 + "\r\n", outputStream.toString());
        } catch (Exception e) {
            fail("Тест завершился с исключением: " + e.getMessage());
        }
    }

    @Test
    void testPrintInstWithErrors() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        BaseInst instruction = this.factory.createInst("PRINT");
        try {
            List<BaseArgument<?>> arguments = new ArrayList<>();
            instruction.execute(arguments, context);
            fail("pop без ошибок");
        } catch (EmptyStackContextException e) {}
    }

    @Test
    void testSqrtInstRight() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        BaseInst instruction = this.factory.createInst("SQRT");
        System.setOut(new PrintStream(outputStream));
        try {
            List<BaseArgument<?>> arguments = new ArrayList<>();
            context.pushStack(4);
            instruction.execute(arguments, context);
            assertEquals(2.0, context.getFirstStack());
            if (context.getSizeStack()!=1) {
                fail("неправильный размер стека");
            }
        } catch (Exception e) {
            fail("Тест завершился с исключением: " + e.getMessage());
        }
    }

    @Test
    void testSqrtInstWithErrors() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        BaseInst instruction = this.factory.createInst("SQRT");
        try {
            List<BaseArgument<?>> arguments = new ArrayList<>();
            instruction.execute(arguments, context);
            fail("pop без ошибок");
        } catch (StackPopException e) {}
        try {
            List<BaseArgument<?>> arguments = new ArrayList<>();
            context.pushStack(-4);
            instruction.execute(arguments, context);
            fail("pop без ошибок");
        } catch (SqrtNegativeException e) {}
    }
}
