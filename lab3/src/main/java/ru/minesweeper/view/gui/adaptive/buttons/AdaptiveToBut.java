package ru.minesweeper.view.gui.adaptive.buttons;

import ru.minesweeper.view.gui.GameView;

import javax.swing.*;
import java.awt.*;

public class AdaptiveToBut {
    static final int MIN_FONT_SIZE = 8;
    static final int MAX_FONT_SIZE = 48;
    static public void adjustButtonSize(JButton button, GameView gameView, int minWidth, int minHeight, int scaleX, int scaleY) {

        Dimension windowSize = gameView.getFrame().getSize();


        int buttonWidth = (int) (windowSize.getWidth() / scaleX);
        int buttonHeight = (int) (windowSize.getHeight() / scaleY);

        // Рассчитываем размер шрифта на основе размеров кнопки
        int fontSize = Math.max(MIN_FONT_SIZE, Math.min(Math.min(buttonWidth / 11, buttonHeight / 3), MAX_FONT_SIZE));

        button.setFont(gameView.getMainFont().deriveFont(Font.PLAIN, fontSize));

        // Устанавливаем размер кнопок
        button.setMaximumSize(new Dimension(Math.max(buttonWidth, minWidth), Math.max(buttonHeight, minHeight)));
        button.setPreferredSize(new Dimension(Math.max(buttonWidth, minWidth), Math.max(buttonHeight, minHeight)));
        button.revalidate();
    }

    
}
