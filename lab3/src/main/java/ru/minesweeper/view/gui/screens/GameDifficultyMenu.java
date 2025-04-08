package ru.minesweeper.view.gui.screens;

import ru.minesweeper.Difficulties;
import ru.minesweeper.observer.Notifications;
import ru.minesweeper.model.entities.datatransfer.DataTransferObject;
import ru.minesweeper.observer.Observer;
import ru.minesweeper.view.gui.GameView;
import ru.minesweeper.view.gui.animations.screens.Sides;
import ru.minesweeper.view.gui.animations.text.TextAnim;
import ru.minesweeper.view.gui.components.buttons.VisualMenuButtonTextable;
import ru.minesweeper.view.gui.components.labels.LabelTextable;
import ru.minesweeper.view.gui.components.buttons.MenuButtonCreator;
import ru.minesweeper.view.gui.components.labels.MenuLabelCreator;

import javax.swing.*;
import java.awt.*;


import static ru.minesweeper.view.gui.animations.buttons.AnimToButton.offMoused;

public class GameDifficultyMenu extends JPanel implements Observer {
    GameView gameView;

    //расстояние между кнопками
    final int SPACE_THICKNESS_BETWEEN_BUTTONS = 10;
    //минимальные размеры кнопок
    final int MIN_BUTTON_WIDTH = 100;
    final int MIN_BUTTON_HEIGHT = 30;

    final int MIN_LABEL_WIDTH = 200;
    final int MIN_LABEL_HEIGHT = 60;

    final int ADAPTIVE_SCALE_MENU_BUTTON_X = 3;
    final int ADAPTIVE_SCALE_MENU_BUTTON_Y = 10;

    final int ADAPTIVE_SCALE_MENU_LABEL_X = 3;
    final int ADAPTIVE_SCALE_MENU_LABEL_Y = 5;

    MenuButtonCreator menuButtonCreator;
    MenuLabelCreator menuLabelCreator;

    LabelTextable mainName;
    boolean isScreenSwap = false;
    public GameDifficultyMenu(GameView gameView) {
        this.gameView = gameView;
        menuButtonCreator = new MenuButtonCreator(
                gameView,
                MIN_BUTTON_WIDTH,
                MIN_BUTTON_HEIGHT
        );
        menuLabelCreator = new MenuLabelCreator(
                gameView,
                MIN_LABEL_WIDTH,
                MIN_LABEL_HEIGHT
        );
        putOptionsMenu();
    }

    public void stopTimers() {
        stopMainNameTimer();
    }

    public void stopMainNameTimer() {
        mainName.stopColorChange();
    }

    public JPanel createButtons() {
        JPanel buttonsPanel = new JPanel();


        //устанавливаем прозрачность BoxLayout на "прозрачно" чтобы не перекрывал задний фон главной JPanel
        buttonsPanel.setOpaque(false);

        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));

        // Создание кнопок
        VisualMenuButtonTextable easyGameButton = menuButtonCreator.createButtonText("Easy", this::startEasyGame, this, ADAPTIVE_SCALE_MENU_BUTTON_X, ADAPTIVE_SCALE_MENU_BUTTON_Y);
        VisualMenuButtonTextable normalGameButton = menuButtonCreator.createButtonText("Normal", this::startNormalGame, this, ADAPTIVE_SCALE_MENU_BUTTON_X, ADAPTIVE_SCALE_MENU_BUTTON_Y);
        VisualMenuButtonTextable hardGameButton = menuButtonCreator.createButtonText("Hard", this::startHardGame, this, ADAPTIVE_SCALE_MENU_BUTTON_X, ADAPTIVE_SCALE_MENU_BUTTON_Y);
        VisualMenuButtonTextable backGameButton = menuButtonCreator.createButtonText("Back", this::backToMenu, this, ADAPTIVE_SCALE_MENU_BUTTON_X, ADAPTIVE_SCALE_MENU_BUTTON_Y);

        // Добавляем кнопки в панель
        buttonsPanel.add(Box.createHorizontalStrut(SPACE_THICKNESS_BETWEEN_BUTTONS));
        buttonsPanel.add(easyGameButton);
        buttonsPanel.add(Box.createHorizontalStrut(SPACE_THICKNESS_BETWEEN_BUTTONS));
        buttonsPanel.add(normalGameButton);
        buttonsPanel.add(Box.createHorizontalStrut(SPACE_THICKNESS_BETWEEN_BUTTONS));
        buttonsPanel.add(hardGameButton);
        buttonsPanel.add(Box.createHorizontalStrut(SPACE_THICKNESS_BETWEEN_BUTTONS));
        buttonsPanel.add(backGameButton);
        buttonsPanel.add(Box.createHorizontalStrut(SPACE_THICKNESS_BETWEEN_BUTTONS));

        return buttonsPanel;

    }

    private void startEasyGame(TextAnim<VisualMenuButtonTextable> anim, VisualMenuButtonTextable button) {
        offMoused(anim, button);
        if (!isScreenSwap) {
            gameView.createEmptyField(Difficulties.EASY);
            gameView.screenSwapToGameProcess(this, Sides.UP);
        }
    }

    private void startNormalGame(TextAnim<VisualMenuButtonTextable> anim, VisualMenuButtonTextable button) {
        offMoused(anim, button);
        if (!isScreenSwap) {
            gameView.createEmptyField(Difficulties.NORMAL);
            gameView.screenSwapToGameProcess(this, Sides.UP);
        }
    }
    private void startHardGame(TextAnim<VisualMenuButtonTextable> anim, VisualMenuButtonTextable button) {
        offMoused(anim, button);
        if (!isScreenSwap) {
            gameView.createEmptyField(Difficulties.HARD);
            gameView.screenSwapToGameProcess(this, Sides.UP);
        }
    }

    private void backToMenu(TextAnim<VisualMenuButtonTextable> anim, VisualMenuButtonTextable button) {
        offMoused(anim, button);
        if (!isScreenSwap) {
            gameView.screenSwapToGameMenu(this, Sides.DOWN);
        }
    }

    private LabelTextable createNameLabel() {
        mainName = menuLabelCreator.createLabelText("Difficulty", this, ADAPTIVE_SCALE_MENU_LABEL_X, ADAPTIVE_SCALE_MENU_LABEL_Y);
        return mainName;
    }

    private void putOptionsMenu() {
        this.setLayout(new GridBagLayout());
        setBackground(gameView.getColorBackground());

        GridBagConstraints constraintsLabel = new GridBagConstraints();
        constraintsLabel.gridx = 0;
        constraintsLabel.gridy = 0;
        constraintsLabel.weightx = 1.0;
        constraintsLabel.weighty = 1.0;
        constraintsLabel.insets = new Insets(30, 0, 0, 0);
        constraintsLabel.anchor = GridBagConstraints.NORTH;

        // Центрирование панели кнопок
        GridBagConstraints constraintsMenu = new GridBagConstraints();
        constraintsMenu.gridx = 0;
        constraintsMenu.gridy = 1;
        constraintsMenu.weightx = 1.0;
        constraintsMenu.weighty = 1.0;
        constraintsMenu.fill = GridBagConstraints.BOTH;
        constraintsMenu.anchor = GridBagConstraints.CENTER;



        add(createNameLabel(), constraintsLabel);
        add(createButtons(), constraintsMenu);
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
