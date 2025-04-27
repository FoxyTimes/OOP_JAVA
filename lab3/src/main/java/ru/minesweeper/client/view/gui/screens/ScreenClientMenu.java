package ru.minesweeper.client.view.gui.screens;

import ru.minesweeper.client.view.gui.GameView;
import ru.minesweeper.client.view.gui.animations.screens.Sides;
import ru.minesweeper.client.view.gui.animations.text.TextAnim;
import ru.minesweeper.client.view.gui.components.buttons.ButtonCreator;
import ru.minesweeper.client.view.gui.components.buttons.VisualMenuButtonTextable;
import ru.minesweeper.client.view.gui.components.labels.LabelTextable;
import ru.minesweeper.client.view.gui.components.labels.LabelCreator;
import ru.minesweeper.client.view.gui.components.textfields.TextFieldCreator;
import ru.minesweeper.client.view.gui.components.textfields.TextFieldSimple;
import ru.minesweeper.observer.Notifications;
import ru.minesweeper.observer.Observer;
import ru.minesweeper.server.model.entities.datatransfer.DataTransferObject;

import javax.swing.*;
import java.awt.*;

import static ru.minesweeper.client.view.gui.animations.buttons.AnimToButton.offMoused;

public class ScreenClientMenu extends Screen implements Observer {
    GameView gameView;

    // Размеры и отступы
    final int SPACE_THICKNESS_BETWEEN_BUTTONS = 20;
    final int MIN_BUTTON_WIDTH = 100;
    final int MIN_BUTTON_HEIGHT = 30;
    final int MIN_LABEL_WIDTH = 200;
    final int MIN_LABEL_HEIGHT = 60;
    final int MIN_FIELD_WIDTH = 100;
    final int MIN_FIELD_HEIGHT = 30;

    final int ADAPTIVE_SCALE_MENU_BUTTON_X = 3;
    final int ADAPTIVE_SCALE_MENU_BUTTON_Y = 10;

    final int ADAPTIVE_SCALE_MENU_LABEL_X = 3;
    final int ADAPTIVE_SCALE_MENU_LABEL_Y = 5;

    boolean isScreenSwap=false;

    ButtonCreator buttonCreator;
    LabelCreator labelCreator;
    TextFieldCreator textFieldCreator;

    LabelTextable mainName;


    TextFieldSimple hostField;
    TextFieldSimple portField;
    public ScreenClientMenu(GameView gameView) {
        this.gameView=gameView;
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
        this.textFieldCreator = new TextFieldCreator(
                gameView,
                MIN_FIELD_WIDTH,
                MIN_FIELD_HEIGHT
        );
        putHostMenu();
    }


    private void putHostMenu() {
        this.setLayout(new GridBagLayout());
        setBackground(gameView.getColorBackground());

        // Констрейнты для заголовка
        GridBagConstraints constraintsLabel = new GridBagConstraints();
        constraintsLabel.weighty = 0.3; // Меньший вес для заголовка
        constraintsLabel.anchor = GridBagConstraints.NORTH;
        constraintsLabel.gridwidth = GridBagConstraints.REMAINDER;
        constraintsLabel.insets = new Insets(20, 0, 0, 0);

        // Констрейнты для списка игроков
        GridBagConstraints constraintsConnectionData = new GridBagConstraints();
        constraintsConnectionData.weighty = 0.4; // Больший вес для списка игроков
        constraintsConnectionData.gridwidth = GridBagConstraints.REMAINDER;
        constraintsConnectionData.fill = GridBagConstraints.BOTH;
        constraintsConnectionData.insets = new Insets(10, 0, 10, 0);

        // Констрейнты для кнопок
        GridBagConstraints constraintsMenu = new GridBagConstraints();
        constraintsMenu.weighty = 0.3; // Меньший вес для кнопок
        constraintsMenu.weightx = 1;
        constraintsMenu.fill = GridBagConstraints.BOTH;
        constraintsMenu.anchor = GridBagConstraints.CENTER;

        this.add(createNameLabel(), constraintsLabel);
        this.add(createConnectionDataPanel(), constraintsConnectionData); // Добавляем панель игроков
        this.add(createButtons(), constraintsMenu);
    }

    private LabelTextable createNameLabel() {
        mainName = labelCreator.createLabelText("Prepare", this, ADAPTIVE_SCALE_MENU_LABEL_X, ADAPTIVE_SCALE_MENU_LABEL_Y);
        return mainName;
    }

    public JPanel createButtons() {
        JPanel buttonsPanel = new JPanel();


        //устанавливаем прозрачность BoxLayout на "прозрачно" чтобы не перекрывал задний фон главной JPanel
        buttonsPanel.setOpaque(false);

        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));

        VisualMenuButtonTextable playSoundGameButton = buttonCreator.createButtonText("Play", this::play, this, ADAPTIVE_SCALE_MENU_BUTTON_X, ADAPTIVE_SCALE_MENU_BUTTON_Y);
        VisualMenuButtonTextable backGameButton = buttonCreator.createButtonText("Back", this::backToMenu, this, ADAPTIVE_SCALE_MENU_BUTTON_X, ADAPTIVE_SCALE_MENU_BUTTON_Y);

        // Добавляем кнопки в панель
        buttonsPanel.add(playSoundGameButton);
        buttonsPanel.add(Box.createVerticalStrut(SPACE_THICKNESS_BETWEEN_BUTTONS));
        buttonsPanel.add(backGameButton);

        return buttonsPanel;
    }



    private JPanel createConnectionDataPanel() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);


        JPanel connectedPanel = new JPanel(new GridLayout(2, 1, 0, 10)); // 10px вертикальный промежуток
        connectedPanel.setOpaque(false);

        hostField = textFieldCreator.createTextFieldSimple("host", this, ADAPTIVE_SCALE_MENU_BUTTON_X, ADAPTIVE_SCALE_MENU_BUTTON_Y);
        portField = textFieldCreator.createTextFieldSimple("port (usually 5556)", this, ADAPTIVE_SCALE_MENU_BUTTON_X, ADAPTIVE_SCALE_MENU_BUTTON_Y);

        connectedPanel.add(hostField);
        connectedPanel.add(portField);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        panel.add(connectedPanel, gbc);

        return panel;
    }

    private void backToMenu(TextAnim<VisualMenuButtonTextable> anim, VisualMenuButtonTextable button) {
        offMoused(anim, button);
        if (!isScreenSwap) {
            gameView.swapScreenToAnotherScreen(this, ScreensE.SCREEN_MULTI_MENU, Sides.DOWN);
        }
    }

    private void play(TextAnim<VisualMenuButtonTextable> anim, VisualMenuButtonTextable button) {
        gameView.connect(getHost(), getPort());
        offMoused(anim, button);
        if (!isScreenSwap) {
            gameView.swapScreenToAnotherScreen(this, ScreensE.SCREEN_LOBBY_MENU, Sides.UP);
        }
    }

    public String getHost() {
        return hostField.getText();
    }

    public int getPort() {
        return Integer.parseInt(portField.getText());
    }

    @Override
    public void stopTimers() {
        stopMainNameTimer();
    }

    public void stopMainNameTimer() {
        mainName.stopColorChange();
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
