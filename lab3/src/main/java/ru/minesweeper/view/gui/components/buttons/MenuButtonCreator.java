package ru.minesweeper.view.gui.components.buttons;

import ru.minesweeper.view.gui.GameView;
import ru.minesweeper.view.gui.adaptive.buttons.AdaptiveToBut;
import ru.minesweeper.view.gui.animations.text.TextAnim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

public class MenuButtonCreator {
    private final GameView gameView;
    private final int minWidth;
    private final int minHeight;

    public MenuButtonCreator(GameView gameView, int minWidth, int minHeight) {
        this.gameView = gameView;
        this.minWidth = minWidth;
        this.minHeight = minHeight;
    }
    public ImageMenuButton createButtonImage(BufferedImage texture, ImageButtonAction action, JPanel screenFrom, int scaleX, int scaleY) {
        ImageMenuButton button = new ImageMenuButton(texture);
        button.standartSettingsImage(
                gameView.getMainFont().deriveFont(Font.PLAIN, 24),
                gameView.getColorButton(),
                button,
                gameView
        );

        // Добавление обработчика действий
        if (action != null) {
            button.addActionListener(e -> {
                action.execute(button);
            });
        }

        // Настройка адаптивности
        setupButtonResizeListener(button, screenFrom, scaleX, scaleY);

        return button;
    }

    public VisualMenuButtonTextable createButtonText(String text, TextButtonAction action, JPanel screenFrom, int scaleX, int scaleY) {
        VisualMenuButtonTextable button = new VisualMenuButtonTextable(text);
        TextAnim<VisualMenuButtonTextable> anim = new TextAnim<>(button, text);

        // Настройка внешнего вида
        button.standartSettingsText(
                gameView.getMainFont().deriveFont(Font.PLAIN, 24),
                gameView.getColorButton(),
                button,
                gameView,
                anim,
                text
        );

        // Добавление обработчика действий
        if (action != null) {
            button.addActionListener(e -> {
                action.execute(anim, button);
            });
        }

        // Настройка адаптивности
        setupButtonResizeListener(button, screenFrom, scaleX, scaleY);

        return button;
    }

    private void setupButtonResizeListener(JButton button, JPanel screenFrom, int scaleX, int scaleY) {
        screenFrom.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                AdaptiveToBut.adjustButtonSize(button, gameView, minWidth, minHeight, scaleX, scaleY);
            }
        });
    }

    @FunctionalInterface
    public interface TextButtonAction {
        void execute(TextAnim<VisualMenuButtonTextable> anim, VisualMenuButtonTextable button);
    }


    @FunctionalInterface
    public interface ImageButtonAction {
        void execute(ImageMenuButton button);
    }
}