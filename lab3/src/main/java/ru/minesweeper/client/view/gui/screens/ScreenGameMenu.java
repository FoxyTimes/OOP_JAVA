package ru.minesweeper.client.view.gui.screens;

import ru.minesweeper.observer.Notifications;
import ru.minesweeper.server.model.entities.datatransfer.DataTransferObject;
import ru.minesweeper.observer.Observer;
import ru.minesweeper.client.view.gui.GameView;
import ru.minesweeper.client.view.gui.animations.screens.Sides;

import ru.minesweeper.client.view.gui.components.buttons.VisualMenuButtonTextable;
import ru.minesweeper.client.view.gui.components.labels.LabelTextable;
import ru.minesweeper.client.view.gui.animations.text.TextAnim;
import ru.minesweeper.client.view.gui.components.buttons.ImageMenuButton;
import ru.minesweeper.client.view.gui.components.buttons.ButtonCreator;
import ru.minesweeper.client.view.gui.components.labels.LabelCreator;

import javax.swing.*;
import java.awt.*;


import static ru.minesweeper.client.view.gui.animations.buttons.AnimToButton.offMoused;

public class ScreenGameMenu extends Screen implements Observer {
    GameView gameView;

    // Размеры и отступы
    final int SPACE_THICKNESS_BETWEEN_BUTTONS = 20;
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

    boolean isScreenSwap = false;

    //создатели стандартных компонентов
    ButtonCreator buttonCreator;
    LabelCreator labelCreator;

    // Компоненты
    LabelTextable mainName;
    public ScreenGameMenu(GameView gameView) {
        super();
        this.gameView = gameView;

        this.buttonCreator = new ButtonCreator(
                gameView,
                MIN_BUTTON_WIDTH,
                MIN_BUTTON_HEIGHT
        );
        this.labelCreator = new LabelCreator(
                gameView,
                MIN_LABEL_WIDTH,
                MIN_LABEL_HEIGHT
        );

        putMainMenu();

        this.validate();
        this.repaint();
    }

    private void chooseDifficulty(TextAnim<VisualMenuButtonTextable> anim, VisualMenuButtonTextable button) {
        gameView.singlePlayer();
        offMoused(anim, button);
    }

    private void exitGame(TextAnim<VisualMenuButtonTextable> anim, VisualMenuButtonTextable button) {
        offMoused(anim, button);
        gameView.exitGame();
    }

    private void optionsSwap(TextAnim<VisualMenuButtonTextable> anim, VisualMenuButtonTextable button) {
        offMoused(anim, button);
        if (!isScreenSwap) {
            gameView.swapScreenToAnotherScreen(this, ScreensE.SCREEN_OPTIONS_MENU,Sides.RIGHT);
        }
    }

    private void recordSwap(ImageMenuButton button) {
        offMoused(button);
        if (!isScreenSwap) {
            gameView.swapScreenToAnotherScreen(this, ScreensE.SCREEN_RECORD_MENU,Sides.DOWN);
        }
        gameView.loadRecords();
    }

    private void MultiSwap(TextAnim<VisualMenuButtonTextable> anim, VisualMenuButtonTextable button) {
        offMoused(anim, button);
        if (!isScreenSwap) {
            gameView.swapScreenToAnotherScreen(this, ScreensE.SCREEN_MULTI_MENU,Sides.LEFT);
        }
    }

    private void putButtons(JPanel container) {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setOpaque(false);
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));

        // Создание кнопок
        VisualMenuButtonTextable startGameButton = buttonCreator.createButtonText("Singleplayer", this::chooseDifficulty, this, ADAPTIVE_SCALE_MENU_BUTTON_X, ADAPTIVE_SCALE_MENU_BUTTON_Y);
        VisualMenuButtonTextable multiGameButton = buttonCreator.createButtonText("Multiplayer", this::MultiSwap, this, ADAPTIVE_SCALE_MENU_BUTTON_X, ADAPTIVE_SCALE_MENU_BUTTON_Y);
        VisualMenuButtonTextable optionsGameButton = buttonCreator.createButtonText("Options", this::optionsSwap, this, ADAPTIVE_SCALE_MENU_BUTTON_X, ADAPTIVE_SCALE_MENU_BUTTON_Y);
        VisualMenuButtonTextable exitGameButton = buttonCreator.createButtonText("Exit", this::exitGame, this, ADAPTIVE_SCALE_MENU_BUTTON_X, ADAPTIVE_SCALE_MENU_BUTTON_Y);

        // Добавление кнопок с отступами
        buttonsPanel.add(startGameButton);
        buttonsPanel.add(Box.createVerticalStrut(SPACE_THICKNESS_BETWEEN_BUTTONS));
        buttonsPanel.add(multiGameButton);
        buttonsPanel.add(Box.createVerticalStrut(SPACE_THICKNESS_BETWEEN_BUTTONS));
        buttonsPanel.add(optionsGameButton);
        buttonsPanel.add(Box.createVerticalStrut(SPACE_THICKNESS_BETWEEN_BUTTONS));
        buttonsPanel.add(exitGameButton);

        // Центрирование панели кнопок
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        container.add(buttonsPanel, gbc);
    }

    @Override
    public void stopTimers() {
        stopMainLabelTimer();
    }

    private void putNameLabel(JPanel container) {

        mainName = labelCreator.createLabelText("MineSweeper", this, ADAPTIVE_SCALE_MENU_LABEL_X, ADAPTIVE_SCALE_MENU_LABEL_Y);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(30, 0, 0, 0);
        gbc.anchor = GridBagConstraints.NORTH;
        container.add(mainName, gbc);
    }

    public void stopMainLabelTimer() {
        mainName.stopColorChange();
    }


    private ImageMenuButton createRecordsButton() {
        return buttonCreator.createButtonImage(
                gameView.getRecordTexture(),
                this::recordSwap,
                this,
                ADAPTIVE_SCALE_RECORD_BUTTON_X,
                ADAPTIVE_SCALE_RECORD_BUTTON_Y
        );
    }

    public JPanel createCenterPanel() {
        // Основной контейнер для центрирования
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        // Добавление компонентов
        putNameLabel(centerPanel);
        putButtons(centerPanel);
        return centerPanel;
    }


    private void putMainMenu() {
        this.setLayout(new GridBagLayout());
        setBackground(gameView.getColorBackground());

        // Размещение центральной панели
        GridBagConstraints constraintsMenu = new GridBagConstraints();
        constraintsMenu.gridx = 0;
        constraintsMenu.gridy = 0;
        constraintsMenu.weightx = 1.0;
        constraintsMenu.weighty = 1.0;
        constraintsMenu.fill = GridBagConstraints.BOTH;

        this.add(createCenterPanel(), constraintsMenu);


        GridBagConstraints constraintsRecord = new GridBagConstraints();
        constraintsRecord.gridx = 0;
        constraintsRecord.gridy = 0;
        constraintsRecord.weightx = 1.0;
        constraintsRecord.weighty = 1.0;
        constraintsRecord.anchor = GridBagConstraints.SOUTHEAST;
        constraintsRecord.insets = new Insets(0, 0, 20, 20);

        // Кнопка рекордов
        this.add(createRecordsButton(), constraintsRecord);
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