package ru.minesweeper.view.gui.adaptive.labels;

import ru.minesweeper.view.gui.GameView;

import javax.swing.*;
import java.awt.*;

public class AdaptiveToLab {
    static public void adjustLabelSize(JLabel label, GameView gameView, int minWidth, int minHeight, int scaleX, int scaleY) {

        Dimension windowSize = gameView.getFrame().getSize();

        int labelWidth = (int) (windowSize.getWidth() / scaleX);
        int labelHeight = (int) (windowSize.getHeight() / scaleY);

        int minFontSize = 8; // Минимальный размер шрифта
        int maxFontSize = 48; // Максимальный размер шрифта

        // Рассчитываем размер шрифта на основе размеров кнопки
        int fontSize = Math.max(minFontSize, Math.min(Math.min(labelWidth / 11, labelHeight / 2), maxFontSize));

        label.setFont(gameView.getMainFont().deriveFont(Font.PLAIN, fontSize));

        // Устанавливаем размер кнопок
        label.setMaximumSize(new Dimension(Math.max(labelWidth, minWidth), Math.max(labelHeight, minHeight)));
        label.setSize(new Dimension(Math.max(labelWidth, minWidth), Math.max(labelHeight, minHeight)));
        label.revalidate();
    }
}
