package ru.minesweeper.view.gui.screens;

import ru.minesweeper.observer.Notifications;
import ru.minesweeper.model.entities.datatransfer.DataTransferObject;
import ru.minesweeper.observer.Observer;
import ru.minesweeper.view.gui.GameView;

import ru.minesweeper.view.gui.animations.screens.Sides;
import ru.minesweeper.view.gui.animations.text.TextAnim;
import ru.minesweeper.view.gui.components.buttons.ImageMenuButton;
import ru.minesweeper.view.gui.components.buttons.VisualMenuButtonTextable;
import ru.minesweeper.view.gui.components.labels.LabelTextable;
import ru.minesweeper.view.gui.components.buttons.MenuButtonCreator;
import ru.minesweeper.view.gui.components.labels.MenuLabelCreator;

import javax.swing.*;
import java.awt.*;

import static ru.minesweeper.view.gui.animations.buttons.AnimToButton.offMoused;

public class GameOptionsMenu extends JPanel implements Observer {
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

    boolean isScreenSwap=false;
    public GameOptionsMenu(GameView gameView) {
        this.gameView = gameView;

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

        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));

        VisualMenuButtonTextable playSoundGameButton = menuButtonCreator.createButtonText("Toggle Sounds", this::toggleSound, this, ADAPTIVE_SCALE_MENU_BUTTON_X, ADAPTIVE_SCALE_MENU_BUTTON_Y);
        VisualMenuButtonTextable playMusicGameButton = menuButtonCreator.createButtonText("Toggle Music", this::toggleMusic, this, ADAPTIVE_SCALE_MENU_BUTTON_X, ADAPTIVE_SCALE_MENU_BUTTON_Y);
        VisualMenuButtonTextable backGameButton = menuButtonCreator.createButtonText("Back", this::backToMenu, this, ADAPTIVE_SCALE_MENU_BUTTON_X, ADAPTIVE_SCALE_MENU_BUTTON_Y);

        // Добавляем кнопки в панель
        buttonsPanel.add(playSoundGameButton);
        buttonsPanel.add(Box.createVerticalStrut(SPACE_THICKNESS_BETWEEN_BUTTONS));
        buttonsPanel.add(playMusicGameButton);
        buttonsPanel.add(Box.createVerticalStrut(SPACE_THICKNESS_BETWEEN_BUTTONS));
        buttonsPanel.add(backGameButton);
        buttonsPanel.add(Box.createVerticalStrut(SPACE_THICKNESS_BETWEEN_BUTTONS));
        buttonsPanel.add(createColorButtons());
        return buttonsPanel;
    }


    public JPanel createColorButtons() {
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 3, 0, 0)); // Без внутренних отступов
        buttonsPanel.setOpaque(false);

        // Внешний контейнер для добавления отступов
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(buttonsPanel, BorderLayout.CENTER);

        // Добавляем отступы слева и справа
        wrapper.setBorder(BorderFactory.createEmptyBorder(30, 30,
                30, 30));

        ImageMenuButton blueColorButton = menuButtonCreator.createButtonImage(
                gameView.getBlueColorTexture(), this::setPaletteBlue, this,
                ADAPTIVE_SCALE_MENU_BUTTON_X, ADAPTIVE_SCALE_MENU_BUTTON_Y);

        ImageMenuButton greenColorButton = menuButtonCreator.createButtonImage(
                gameView.getGreenColorTexture(), this::setPaletteGreen, this,
                ADAPTIVE_SCALE_MENU_BUTTON_X, ADAPTIVE_SCALE_MENU_BUTTON_Y);

        ImageMenuButton yellowColorButton = menuButtonCreator.createButtonImage(
                gameView.getYellowColorTexture(), this::setPaletteYellow, this,
                ADAPTIVE_SCALE_MENU_BUTTON_X, ADAPTIVE_SCALE_MENU_BUTTON_Y);

        buttonsPanel.add(blueColorButton);
        buttonsPanel.add(greenColorButton);
        buttonsPanel.add(yellowColorButton);

        return wrapper;
    }

    private void setPaletteBlue(ImageMenuButton button) {
        gameView.setPalette(93, 155, 155);
    }

    private void setPaletteGreen(ImageMenuButton button) {
        gameView.setPalette(106, 153, 78);
    }

    private void setPaletteYellow(ImageMenuButton button) {
        gameView.setPalette(238, 150, 75);
    }

    private void backToMenu(TextAnim<VisualMenuButtonTextable> anim, VisualMenuButtonTextable button) {
        offMoused(anim, button);
        if (!isScreenSwap) {
            gameView.screenSwapToGameMenu(this, Sides.LEFT);
        }
    }

    private LabelTextable createNameLabel() {
        mainName = menuLabelCreator.createLabelText("Options", this, ADAPTIVE_SCALE_MENU_LABEL_X, ADAPTIVE_SCALE_MENU_LABEL_Y);
        return mainName;
    }

    private void toggleSound(TextAnim<VisualMenuButtonTextable> anim, VisualMenuButtonTextable button) {
        gameView.getSoundPressed().toggleBase();
    }

    private void toggleMusic(TextAnim<VisualMenuButtonTextable> anim, VisualMenuButtonTextable button) {
        gameView.getSoundTrack().toggleLoop();
    }
    private void putOptionsMenu() {
        this.setLayout(new GridBagLayout());
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

        this.add(createNameLabel(), constraintsLabel);
        this.add(createButtons(), constraintsMenu);
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
