package ru.calc.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

public class InstParser {
        BufferedReader reader;
        public InstParser(Reader reader) {
                this.reader = new BufferedReader(reader);
        }

        public List<String> parseLine() throws IOException {
                List<String> arguments = new LinkedList<>();
                String line = reader.readLine();

                if (line != null) {
                        String[] tokens = line.split("\\s+");
                        for (String token : tokens) {
                                arguments.add(token);
                        }

                }
                return arguments;
        }

}
