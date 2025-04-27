package ru.minesweeper.client.view.gui.screens;

import ru.minesweeper.client.view.gui.GameView;
import ru.minesweeper.client.view.gui.animations.screens.Sides;
import ru.minesweeper.client.view.gui.animations.text.TextAnim;
import ru.minesweeper.client.view.gui.components.buttons.ButtonCreator;
import ru.minesweeper.client.view.gui.components.buttons.VisualMenuButtonTextable;
import ru.minesweeper.client.view.gui.components.labels.LabelSimple;
import ru.minesweeper.client.view.gui.components.labels.LabelTextable;
import ru.minesweeper.client.view.gui.components.labels.LabelCreator;
import ru.minesweeper.observer.Notifications;
import ru.minesweeper.observer.Observer;
import ru.minesweeper.server.model.Difficulties;
import ru.minesweeper.server.model.entities.datatransfer.DataTransferObject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static ru.minesweeper.client.view.gui.animations.buttons.AnimToButton.offMoused;

public class ScreenLobbyMenu extends Screen implements Observer {
    GameView gameView;

    // Размеры и отступы
    final int SPACE_THICKNESS_BETWEEN_BUTTONS = 20;
    final int MIN_BUTTON_WIDTH = 100;
    final int MIN_BUTTON_HEIGHT = 30;
    final int MIN_LABEL_WIDTH = 200;
    final int MIN_LABEL_HEIGHT = 60;

    final int ADAPTIVE_SCALE_MENU_BUTTON_X = 3;
    final int ADAPTIVE_SCALE_MENU_BUTTON_Y = 10;

    final int ADAPTIVE_SCALE_MENU_LABEL_X = 3;
    final int ADAPTIVE_SCALE_MENU_LABEL_Y = 5;

    boolean isScreenSwap=false;

    ButtonCreator buttonCreator;
    LabelCreator labelCreator;

    LabelTextable mainName;
    JPanel connectedPanelShell;
    JPanel connectedPanel;

    public ScreenLobbyMenu(GameView gameView) {
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
        GridBagConstraints constraintsPlayers = new GridBagConstraints();
        constraintsPlayers.weighty = 0.4; // Больший вес для списка игроков
        constraintsPlayers.gridwidth = GridBagConstraints.REMAINDER;
        constraintsPlayers.fill = GridBagConstraints.BOTH;
        constraintsPlayers.insets = new Insets(10, 0, 10, 0);

        // Констрейнты для кнопок
        GridBagConstraints constraintsMenu = new GridBagConstraints();
        constraintsMenu.weighty = 0.3; // Меньший вес для кнопок
        constraintsMenu.weightx = 1;
        constraintsMenu.fill = GridBagConstraints.BOTH;
        constraintsMenu.anchor = GridBagConstraints.CENTER;

        this.add(createNameLabel(), constraintsLabel);
        this.add(createConnectedPlayersPanelShell(new ArrayList<String>()), constraintsPlayers); // Добавляем панель игроков
        this.add(createButtons(), constraintsMenu);
    }

    private LabelTextable createNameLabel() {
        mainName = labelCreator.createLabelText("Lobby", this, ADAPTIVE_SCALE_MENU_LABEL_X, ADAPTIVE_SCALE_MENU_LABEL_Y);
        return mainName;
    }

    public JPanel createButtons() {
        JPanel buttonsPanel = new JPanel();


        //устанавливаем прозрачность BoxLayout на "прозрачно" чтобы не перекрывал задний фон главной JPanel
        buttonsPanel.setOpaque(false);

        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));

        VisualMenuButtonTextable playButton = buttonCreator.createButtonText("Play", this::startGame, this, ADAPTIVE_SCALE_MENU_BUTTON_X, ADAPTIVE_SCALE_MENU_BUTTON_Y);
        VisualMenuButtonTextable backGameButton = buttonCreator.createButtonText("Back", this::backToMenu, this, ADAPTIVE_SCALE_MENU_BUTTON_X, ADAPTIVE_SCALE_MENU_BUTTON_Y);

        // Добавляем кнопки в панель
        buttonsPanel.add(playButton);
        buttonsPanel.add(Box.createVerticalStrut(SPACE_THICKNESS_BETWEEN_BUTTONS));
        buttonsPanel.add(backGameButton);

        return buttonsPanel;
    }

    private void startGame(TextAnim<VisualMenuButtonTextable> anim, VisualMenuButtonTextable button) {
        gameView.createEmptyField(Difficulties.HARD);
        offMoused(anim, button);
    }


    private JPanel createConnectedPlayersPanelShell(ArrayList<String> nickNames) {

        connectedPanelShell = new JPanel(new GridBagLayout());
        connectedPanelShell.setOpaque(false);

        updateConnectedPanel(nickNames);

        return connectedPanelShell;
    }


    private void updateConnectedPanel(ArrayList<String> nickNames) {

        if (connectedPanel!=null) {
            connectedPanelShell.remove(connectedPanel);
        }

        connectedPanel = new JPanel();
        connectedPanel.setLayout(new BoxLayout(connectedPanel, BoxLayout.Y_AXIS));
        connectedPanel.setBackground(gameView.getColorSidePanel());
        connectedPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        LabelSimple title = labelCreator.createLabelSimple("Players:", this, ADAPTIVE_SCALE_MENU_LABEL_X/2, ADAPTIVE_SCALE_MENU_LABEL_Y/2);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        connectedPanel.add(Box.createVerticalGlue());
        connectedPanel.add(title);

        if (nickNames.isEmpty()) {
            LabelSimple connectedLabel = labelCreator.createLabelSimple("No players connected", this, ADAPTIVE_SCALE_MENU_LABEL_X/3, ADAPTIVE_SCALE_MENU_LABEL_Y/3);
            title.setHorizontalAlignment(SwingConstants.CENTER);

            connectedPanel.add(Box.createVerticalStrut(20));
            connectedPanel.add(connectedLabel);
        }
        else {
            for (String nickName : nickNames) {
                LabelSimple connectedLabel = labelCreator.createLabelSimple(nickName, this, ADAPTIVE_SCALE_MENU_LABEL_X/3, ADAPTIVE_SCALE_MENU_LABEL_Y/3);
                title.setHorizontalAlignment(SwingConstants.CENTER);

                connectedPanel.add(Box.createVerticalStrut(20));
                connectedPanel.add(connectedLabel);
            }
        }

        connectedPanel.add(Box.createVerticalGlue());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        connectedPanelShell.add(connectedPanel, gbc);
    }



    public void updateNicknames(ArrayList<String> playerNicknamesOnServer) {
        updateConnectedPanel(playerNicknamesOnServer);
    }

    private void backToMenu(TextAnim<VisualMenuButtonTextable> anim, VisualMenuButtonTextable button) {
        gameView.stopHost();
        gameView.disconnect();
        offMoused(anim, button);
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
