package ru.minesweeper.client.view.gui.screens;

import ru.minesweeper.client.view.gui.components.textfields.TextFieldCreator;
import ru.minesweeper.server.model.entities.datatransfer.DataTransferObject;
import ru.minesweeper.observer.Notifications;
import ru.minesweeper.observer.Observer;
import ru.minesweeper.client.view.gui.GameView;
import ru.minesweeper.client.view.gui.animations.screens.Sides;
import ru.minesweeper.client.view.gui.animations.text.TextAnim;
import ru.minesweeper.client.view.gui.components.buttons.ButtonCreator;
import ru.minesweeper.client.view.gui.components.buttons.VisualMenuButtonTextable;
import ru.minesweeper.client.view.gui.components.labels.LabelTextable;
import ru.minesweeper.client.view.gui.components.labels.LabelCreator;

import javax.swing.*;
import java.awt.*;

import static ru.minesweeper.client.view.gui.animations.buttons.AnimToButton.offMoused;

public class ScreenMultiMenu extends Screen implements Observer {
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
    TextFieldCreator fieldCreator;

    LabelTextable mainName;


    private JTextField nicknameField;
    public ScreenMultiMenu(GameView gameView) {
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
        this.fieldCreator = new TextFieldCreator(
                gameView,
                MIN_FIELD_WIDTH,
                MIN_FIELD_HEIGHT
        );
        putMainMenu();
    }

    @Override
    public void stopTimers() {
        stopMainNameTimer();
    }


    public void stopMainNameTimer() {
        mainName.stopColorChange();
    }


    public void putMainMenu() {
        setLayout(new GridBagLayout());
        setBackground(gameView.getColorBackground());

        GridBagConstraints constraintsLabel = new GridBagConstraints();
        constraintsLabel.weighty = 1;
        constraintsLabel.anchor = GridBagConstraints.NORTH;
        constraintsLabel.gridwidth = GridBagConstraints.REMAINDER;
        constraintsLabel.insets = new Insets(20, 0, 0, 0);

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

        buttonsPanel.setOpaque(false);
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));

        nicknameField = fieldCreator.createTextFieldSimple("nickname", this, ADAPTIVE_SCALE_MENU_BUTTON_X, ADAPTIVE_SCALE_MENU_BUTTON_Y);

        VisualMenuButtonTextable HostButton = buttonCreator.createButtonText("Host", this::hostSwap, this, ADAPTIVE_SCALE_MENU_BUTTON_X, ADAPTIVE_SCALE_MENU_BUTTON_Y);
        VisualMenuButtonTextable ClientButton = buttonCreator.createButtonText("Client", this::clientSwap, this, ADAPTIVE_SCALE_MENU_BUTTON_X, ADAPTIVE_SCALE_MENU_BUTTON_Y);
        VisualMenuButtonTextable backGameButton = buttonCreator.createButtonText("Back", this::backToMenu, this, ADAPTIVE_SCALE_MENU_BUTTON_X, ADAPTIVE_SCALE_MENU_BUTTON_Y);

        buttonsPanel.add(Box.createVerticalStrut(SPACE_THICKNESS_BETWEEN_BUTTONS));
        buttonsPanel.add(nicknameField);
        buttonsPanel.add(Box.createVerticalStrut(SPACE_THICKNESS_BETWEEN_BUTTONS));
        buttonsPanel.add(HostButton);
        buttonsPanel.add(Box.createVerticalStrut(SPACE_THICKNESS_BETWEEN_BUTTONS));
        buttonsPanel.add(ClientButton);
        buttonsPanel.add(Box.createVerticalStrut(SPACE_THICKNESS_BETWEEN_BUTTONS));
        buttonsPanel.add(backGameButton);
        buttonsPanel.add(Box.createVerticalStrut(SPACE_THICKNESS_BETWEEN_BUTTONS));
        return buttonsPanel;
    }

    public String getNickname() {
        return nicknameField.getText().trim();
    }

    private void hostSwap(TextAnim<VisualMenuButtonTextable> anim, VisualMenuButtonTextable button) {
        gameView.multiPlayer();
        offMoused(anim, button);
    }

    private void clientSwap(TextAnim<VisualMenuButtonTextable> anim, VisualMenuButtonTextable button) {
        offMoused(anim, button);
        if (!isScreenSwap) {
            gameView.swapScreenToAnotherScreen(this, ScreensE.SCREEN_CLIENT_MENU,Sides.UP);
        }
    }

    public JPanel createLabel() {
        JPanel labelPanel = new JPanel();
        labelPanel.setOpaque(false);
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.X_AXIS));
        labelPanel.add(Box.createHorizontalGlue());
        mainName = labelCreator.createLabelText("Multiplayer", this, ADAPTIVE_SCALE_MENU_LABEL_X, ADAPTIVE_SCALE_MENU_LABEL_Y);
        labelPanel.add(mainName);
        labelPanel.add(Box.createHorizontalGlue());
        return labelPanel;
    }

    private void backToMenu(TextAnim<VisualMenuButtonTextable> anim, VisualMenuButtonTextable button) {
        offMoused(anim, button);
        if (!isScreenSwap) {
            gameView.swapScreenToAnotherScreen(this, ScreensE.SCREEN_GAME_MENU,Sides.RIGHT);
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
