

public class Main {
    public static void main(String[] args) {
        Calc calc = new Calc();
        if (args.length != 0) {
            calc.start(args[0]);
        }
        else {
            calc.start("");
        }
    }
}
