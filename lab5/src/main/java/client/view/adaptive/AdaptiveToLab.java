package client.view.adaptive;

import client.view.ChatView;

import javax.swing.*;
import java.awt.*;

public class AdaptiveToLab {
    static public void adjustLabelSize(JLabel label, ChatView chatView, int minWidth, int minHeight, int scaleX, int scaleY) {

        Dimension windowSize = chatView.getFrame().getSize();

        int labelWidth = (int) (windowSize.getWidth() / scaleX);
        int labelHeight = (int) (windowSize.getHeight() / scaleY);

        int minFontSize = 8; // Минимальный размер шрифта
        int maxFontSize = 48; // Максимальный размер шрифта

        // Рассчитываем размер шрифта на основе размеров кнопки
        int fontSize = Math.max(minFontSize, Math.min(Math.min(labelWidth / 11, labelHeight / 2), maxFontSize));

        label.setFont(chatView.getMainFont().deriveFont(Font.PLAIN, fontSize));

        // Устанавливаем размер кнопок
        label.setMaximumSize(new Dimension(Math.max(labelWidth, minWidth), Math.max(labelHeight, minHeight)));
        label.setSize(new Dimension(Math.max(labelWidth, minWidth), Math.max(labelHeight, minHeight)));
        label.revalidate();
    }
}
