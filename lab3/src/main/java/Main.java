import ru.minesweeper.Difficulties;
import ru.minesweeper.controller.GameController;
import ru.minesweeper.model.GameModel;
import ru.minesweeper.view.gui.GameView;

public class Main {
    public static void main(String[] args) {
        GameModel gameCore = new GameModel();
        GameController controller = new GameController(gameCore);
        GameView window = new GameView(controller);

        gameCore.addObserver(window);

        controller.createEmptyField(Difficulties.NONE);

    }
}
