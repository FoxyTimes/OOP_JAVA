import ru.factory.controller.FactoryController;
import ru.factory.view.gui.Screen;

public class Main {
    public static void main(String[] args) {
        FactoryController factoryController = new FactoryController();
        String configFile="config.properties";
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-c")) {
                if (i + 1 < args.length) {
                    configFile = args[i+1];
                }
            }
        }

        Screen screen = new Screen(factoryController);
        factoryController.start(configFile);
        factoryController.getModel().addObserver(screen);
        factoryController.loadData();
    }
}