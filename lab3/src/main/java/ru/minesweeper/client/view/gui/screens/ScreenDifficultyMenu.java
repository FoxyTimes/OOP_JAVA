package ru.minesweeper.client.view.gui.screens;

import ru.minesweeper.server.model.Difficulties;
import ru.minesweeper.observer.Notifications;
import ru.minesweeper.server.model.entities.datatransfer.DataTransferObject;
import ru.minesweeper.observer.Observer;
import ru.minesweeper.client.view.gui.GameView;
import ru.minesweeper.client.view.gui.animations.screens.Sides;
import ru.minesweeper.client.view.gui.animations.text.TextAnim;
import ru.minesweeper.client.view.gui.components.buttons.VisualMenuButtonTextable;
import ru.minesweeper.client.view.gui.components.labels.LabelTextable;
import ru.minesweeper.client.view.gui.components.buttons.ButtonCreator;
import ru.minesweeper.client.view.gui.components.labels.LabelCreator;

import javax.swing.*;
import java.awt.*;


import static ru.minesweeper.client.view.gui.animations.buttons.AnimToButton.offMoused;

public class ScreenDifficultyMenu extends Screen implements Observer {
    GameView gameView;

    //расстояние между кнопками
    final int SPACE_THICKNESS_BETWEEN_BUTTONS = 20;
    //минимальные размеры кнопок
    final int MIN_BUTTON_WIDTH = 100;
    final int MIN_BUTTON_HEIGHT = 30;

    final int MIN_LABEL_WIDTH = 200;
    final int MIN_LABEL_HEIGHT = 60;

    final int ADAPTIVE_SCALE_MENU_BUTTON_X = 3;
    final int ADAPTIVE_SCALE_MENU_BUTTON_Y = 10;

    final int ADAPTIVE_SCALE_MENU_LABEL_X = 3;
    final int ADAPTIVE_SCALE_MENU_LABEL_Y = 5;

    ButtonCreator buttonCreator;
    LabelCreator labelCreator;

    LabelTextable mainName;
    boolean isScreenSwap = false;
    public ScreenDifficultyMenu(GameView gameView) {
        this.gameView = gameView;
        buttonCreator = new ButtonCreator(
                gameView,
                MIN_BUTTON_WIDTH,
                MIN_BUTTON_HEIGHT
        );
        labelCreator = new LabelCreator(
                gameView,
                MIN_LABEL_WIDTH,
                MIN_LABEL_HEIGHT
        );
        putOptionsMenu();
    }

    @Override
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
        VisualMenuButtonTextable easyGameButton = buttonCreator.createButtonText("Easy", this::startEasyGame, this, ADAPTIVE_SCALE_MENU_BUTTON_X, ADAPTIVE_SCALE_MENU_BUTTON_Y);
        VisualMenuButtonTextable normalGameButton = buttonCreator.createButtonText("Normal", this::startNormalGame, this, ADAPTIVE_SCALE_MENU_BUTTON_X, ADAPTIVE_SCALE_MENU_BUTTON_Y);
        VisualMenuButtonTextable hardGameButton = buttonCreator.createButtonText("Hard", this::startHardGame, this, ADAPTIVE_SCALE_MENU_BUTTON_X, ADAPTIVE_SCALE_MENU_BUTTON_Y);
        VisualMenuButtonTextable backGameButton = buttonCreator.createButtonText("Back", this::backToMenu, this, ADAPTIVE_SCALE_MENU_BUTTON_X, ADAPTIVE_SCALE_MENU_BUTTON_Y);

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
            gameView.swapScreenToAnotherScreen(this, ScreensE.SCREEN_SINGLE_PROCESS,Sides.UP);
        }
    }

    private void startNormalGame(TextAnim<VisualMenuButtonTextable> anim, VisualMenuButtonTextable button) {
        offMoused(anim, button);
        if (!isScreenSwap) {
            gameView.createEmptyField(Difficulties.NORMAL);
            gameView.swapScreenToAnotherScreen(this, ScreensE.SCREEN_SINGLE_PROCESS,Sides.UP);
        }
    }
    private void startHardGame(TextAnim<VisualMenuButtonTextable> anim, VisualMenuButtonTextable button) {
        offMoused(anim, button);
        if (!isScreenSwap) {
            gameView.createEmptyField(Difficulties.HARD);
            gameView.swapScreenToAnotherScreen(this, ScreensE.SCREEN_SINGLE_PROCESS,Sides.UP);
        }
    }

    private void backToMenu(TextAnim<VisualMenuButtonTextable> anim, VisualMenuButtonTextable button) {
        gameView.stopHost();
        offMoused(anim, button);
        if (!isScreenSwap) {
            gameView.swapScreenToAnotherScreen(this, ScreensE.SCREEN_GAME_MENU,Sides.DOWN);
        }
    }

    private LabelTextable createNameLabel() {
        mainName = labelCreator.createLabelText("Difficulty", this, ADAPTIVE_SCALE_MENU_LABEL_X, ADAPTIVE_SCALE_MENU_LABEL_Y);
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
