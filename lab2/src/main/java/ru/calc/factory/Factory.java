package ru.calc.factory;

import ru.calc.errors.NoSuchInstructionException;
import ru.calc.instructions.BaseInst;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Factory {

    private Map<String, BaseInst> cashed_insts = new HashMap<>();
    private Map<String, String> config;


    public Map<String, String> openConfig(String fileName) throws IOException {
        Properties config = new Properties();
        try (InputStream is = Factory.class.getResourceAsStream(fileName)) {
            assert is != null;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            config.load(br);
            Map<String, String> map = new HashMap<>();
            for (String key : config.stringPropertyNames()) {
                map.put(key, config.getProperty(key));
            }
            return map;
        }
    }

    public Factory() throws IOException {
        this.config = openConfig("/config.properties");
    }

    public BaseInst createInst(String name) {


        if (cashed_insts.get(name) != null) {
            return cashed_insts.get(name);
        }

        try {
            Class<?> clazz = Class.forName(config.get(name));
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            BaseInst inst = (BaseInst) constructor.newInstance();
            cashed_insts.put(name, inst);
            return inst;
        } catch (NullPointerException | ClassNotFoundException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException | InvocationTargetException | ClassCastException e) {
            throw new NoSuchInstructionException("Undefiended instruction");
        }
    }
}
