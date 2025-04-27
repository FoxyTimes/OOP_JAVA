package ru.minesweeper.client.view.gui.components.buttons;

import ru.minesweeper.client.view.gui.GameView;
import ru.minesweeper.client.view.gui.animations.text.TextAnim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static ru.minesweeper.client.view.gui.animations.buttons.AnimToButton.offMoused;
import static ru.minesweeper.client.view.gui.animations.buttons.AnimToButton.onMoused;

public interface ButtonStandartSettings {


    void makeVisual(Font font, Color color);

    default void standartSettingsImage(Font font, Color color, ImageMenuButton button, GameView gameView) {
        //визуал
        makeVisual(font, color);
        //анимации при наведении
        button.addMouseListener(new MouseAdapter() {
            //при наведении на кнопку
            @Override
            public void mouseEntered(MouseEvent me) {
                onMoused(button, gameView.getColorBorder());
            }

            //при уведении с кнопки
            @Override
            public void mouseExited(MouseEvent me) {
                offMoused(button);
            }
        });
        button.setBorder(BorderFactory.createEmptyBorder());
        //звук при нажатии
        button.addActionListener(e -> gameView.getSoundPressed().play());
    }

    default void standartSettingsText(Font font, Color color, VisualMenuButtonTextable button, GameView gameView, TextAnim<VisualMenuButtonTextable> buttonAnim, String text) {

        //визуал
        makeVisual(font, color);
        //анимации при наведении
        button.addMouseListener(new MouseAdapter() {
            //при наведении на кнопку
            @Override
            public void mouseEntered(MouseEvent me) {
                onMoused(buttonAnim, button, text, gameView.getColorBorder());
                System.out.println(gameView.getColorBorder());
            }
            //при уведении с кнопки
            @Override
            public void mouseExited(MouseEvent me) {
                offMoused(buttonAnim, button);
            }
        });
        button.setBorder(BorderFactory.createEmptyBorder());
        //звук при нажатии
        button.addActionListener(e -> gameView.getSoundPressed().play());
    }
}
