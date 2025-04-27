package ru.calc.context;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Stack;

public class Context {
    private Stack<Double> stack = new Stack<Double>();
    private HashMap<String, Double> map = new HashMap<String, Double>();


    public double popStack() throws EmptyStackException {
        return stack.pop();
    }

    public boolean isEmptyStack() {
        return stack.isEmpty();
    }

    public int getSizeStack() {
        return stack.size();
    }

    public void pushStack(double value) {
        stack.push(value);
    }

    public double getFirstStack() {
        return stack.peek();
    }

    public boolean isDefine(String key) {
        return map.containsKey(key);
    }
    public void putDefine(String key, double value) {
        map.put(key, value);
    }

    public double getDefine(String key) {
        return map.get(key);
    }
}
