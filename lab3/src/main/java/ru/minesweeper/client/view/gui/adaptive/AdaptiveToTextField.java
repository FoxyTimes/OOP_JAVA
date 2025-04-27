package ru.minesweeper.client.view.gui.adaptive;

import ru.minesweeper.client.view.gui.GameView;
import ru.minesweeper.client.view.gui.components.textfields.TextFieldSimple;

import javax.swing.*;
import java.awt.*;

public class AdaptiveToTextField {
    static public void adjustTextFieldSize(TextFieldSimple fieldSimple, GameView gameView, int minWidth, int minHeight, int scaleX, int scaleY) {

        Dimension windowSize = gameView.getFrame().getSize();

        int fieldSimpleWidth = (int) (windowSize.getWidth() / scaleX);
        int fieldSimpleHeight = (int) (windowSize.getHeight() / scaleY);

        int minFontSize = 8; // Минимальный размер шрифта
        int maxFontSize = 48; // Максимальный размер шрифта

        // Рассчитываем размер шрифта на основе размеров кнопки
        int fontSize = Math.max(minFontSize, Math.min(Math.min(fieldSimpleWidth / 11, fieldSimpleHeight / 2), maxFontSize));

        fieldSimple.setFont(gameView.getMainFont().deriveFont(Font.PLAIN, fontSize));

        // Устанавливаем размер кнопок
        fieldSimple.setMaximumSize(new Dimension(Math.max(fieldSimpleWidth, minWidth), Math.max(fieldSimpleHeight, minHeight)));
        fieldSimple.setSize(new Dimension(Math.max(fieldSimpleWidth, minWidth), Math.max(fieldSimpleHeight, minHeight)));
        fieldSimple.revalidate();
    }
}
