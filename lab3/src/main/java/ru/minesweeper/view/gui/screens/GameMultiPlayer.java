package ru.minesweeper.view.gui.screens;

import ru.minesweeper.model.entities.datatransfer.DataTransferObject;
import ru.minesweeper.observer.Notifications;
import ru.minesweeper.observer.Observer;
import ru.minesweeper.view.gui.GameView;
import ru.minesweeper.view.gui.animations.screens.Sides;
import ru.minesweeper.view.gui.animations.text.TextAnim;
import ru.minesweeper.view.gui.components.buttons.MenuButtonCreator;
import ru.minesweeper.view.gui.components.buttons.VisualMenuButtonTextable;
import ru.minesweeper.view.gui.components.labels.LabelTextable;
import ru.minesweeper.view.gui.components.labels.MenuLabelCreator;

import javax.swing.*;
import java.awt.*;

import static ru.minesweeper.view.gui.animations.buttons.AnimToButton.offMoused;

public class GameMultiPlayer extends JPanel implements Observer {
    GameView gameView;

    // Размеры и отступы
    final int SPACE_THICKNESS_BETWEEN_BUTTONS = 10;
    final int MIN_BUTTON_WIDTH = 100;
    final int MIN_BUTTON_HEIGHT = 30;
    final int MIN_LABEL_WIDTH = 200;
    final int MIN_LABEL_HEIGHT = 60;

    final int ADAPTIVE_SCALE_MENU_BUTTON_X = 3;
    final int ADAPTIVE_SCALE_MENU_BUTTON_Y = 10;

    final int ADAPTIVE_SCALE_RECORD_BUTTON_X = 12;
    final int ADAPTIVE_SCALE_RECORD_BUTTON_Y = 7;

    final int ADAPTIVE_SCALE_MENU_LABEL_X = 3;
    final int ADAPTIVE_SCALE_MENU_LABEL_Y = 5;

    boolean isScreenSwap=false;

    MenuButtonCreator menuButtonCreator;
    MenuLabelCreator menuLabelCreator;

    LabelTextable mainName;
    public GameMultiPlayer(GameView gameView) {
        this.gameView=gameView;
        this.menuButtonCreator = new MenuButtonCreator(
                gameView,
                MIN_BUTTON_WIDTH,
                MIN_BUTTON_HEIGHT
        );
        this.menuLabelCreator = new MenuLabelCreator(
                gameView,
                MIN_LABEL_WIDTH,
                MIN_LABEL_HEIGHT
        );
        putMainMenu();
    }

    public void stopTimers() {
        stopMainNameTimer();
    }

    public void stopMainNameTimer() {
        mainName.stopColorChange();
    }


    public void putMainMenu() {
        setLayout(new GridBagLayout());
        setBackground(gameView.getColorBackground());

        //настройка для расположения лейбла
        GridBagConstraints constraintsLabel = new GridBagConstraints();
        constraintsLabel.weighty = 1;
        constraintsLabel.anchor = GridBagConstraints.NORTH;
        constraintsLabel.gridwidth = GridBagConstraints.REMAINDER;
        constraintsLabel.insets = new Insets(20, 0, 0, 0);

        // Устанавливаем панель кнопок на основной контейнер
        GridBagConstraints constraintsMenu = new GridBagConstraints();
        constraintsMenu.weighty = 1;
        constraintsMenu.weightx = 1;
        constraintsMenu.fill = GridBagConstraints.BOTH;
        constraintsMenu.anchor = GridBagConstraints.CENTER;

        this.add(createLabel(), constraintsLabel);
        this.add(createButton(), constraintsMenu);
    }

    public JPanel createButton() {
        JPanel buttonsPanel = new JPanel();


        //устанавливаем прозрачность BoxLayout на "прозрачно" чтобы не перекрывал задний фон главной JPanel
        buttonsPanel.setOpaque(false);

        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));

        VisualMenuButtonTextable backGameButton = menuButtonCreator.createButtonText("Back", this::backToMenu, this, ADAPTIVE_SCALE_MENU_BUTTON_X, ADAPTIVE_SCALE_MENU_BUTTON_Y);

        // Добавляем кнопки в панель
        buttonsPanel.add(Box.createVerticalStrut(SPACE_THICKNESS_BETWEEN_BUTTONS));
        buttonsPanel.add(backGameButton);

        return buttonsPanel;
    }

    public JPanel createLabel() {
        JPanel labelPanel = new JPanel();
        labelPanel.setOpaque(false);
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.X_AXIS));
        labelPanel.add(Box.createHorizontalGlue());
        mainName = menuLabelCreator.createLabelText("Multiplayer", this, ADAPTIVE_SCALE_MENU_LABEL_X, ADAPTIVE_SCALE_MENU_LABEL_Y);
        labelPanel.add(mainName);
        labelPanel.add(Box.createHorizontalGlue());
        return labelPanel;
    }

    private void backToMenu(TextAnim<VisualMenuButtonTextable> anim, VisualMenuButtonTextable button) {
        offMoused(anim, button);
        if (!isScreenSwap) {
            gameView.screenSwapToGameMenu(this, Sides.RIGHT);
        }
    }

    @Override
    public void notify(Notifications notification, DataTransferObject data) {
        switch (notification) {
            case SWAP_STARTED:
                isScreenSwap = true;
                break;
            case SWAP_SUCCESS:
                isScreenSwap = false;
                break;
        }
    }

}
