import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.calc.arguments.BaseArgument;
import ru.calc.context.Context;
import ru.calc.errors.InstructionExecuteException;
import ru.calc.errors.NoSuchInstructionException;
import ru.calc.instructions.BaseInst;
import ru.calc.factory.Factory;
import ru.calc.parsers.ArgsParser;
import ru.calc.parsers.InstParser;

import java.io.*;
import java.util.List;

public class Calc {
    private static final Logger logger = LogManager.getLogger(Calc.class);
    private final Context context = new Context();

    public void start(String fileName) {
        Reader reader;
        try {
            reader = new InputStreamReader(new FileInputStream(fileName));
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found, turn to console input:");
            reader = new InputStreamReader(System.in);
        }

        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            // создаем парсеры
            InstParser instParser = new InstParser(bufferedReader);
            ArgsParser argsParser = new ArgsParser();

            // создаем фабрику
            Factory factory = new Factory();

            // главный цикл по командам
            while (true) {
                // парсим строку
                List<String> line = instParser.parseLine();
                if (line.isEmpty()) {
                    break;
                }

                // создаем инструкцию
                BaseInst instruction;
                try {
                    instruction = factory.createInst(line.removeFirst());
                }
                catch (NoSuchInstructionException e) {
                    logger.error(e);
                    continue;
                }

                // парсим аргументы
                List<BaseArgument<?>> arguments = argsParser.parseArgs(line);

                try {
                    instruction.execute(arguments, context);
                }
                catch (InstructionExecuteException e) {
                    logger.error(e);
                }
            }
        }
        catch (IOException e) {
            logger.error("error while reading the file: " + e.getLocalizedMessage());
        }
        try {
            reader.close();
        }
        catch (IOException e) {
            logger.error("error while closing the file: " + e.getLocalizedMessage());
        }
    }

}
