package client.view.adaptive;


import client.view.ChatView;
import client.view.components.textfields.TextFieldSimple;

import java.awt.*;

public class AdaptiveToTextField {
    static public void adjustTextFieldSize(TextFieldSimple fieldSimple, ChatView chatView, int minWidth, int minHeight, int scaleX, int scaleY) {

        Dimension windowSize = chatView.getFrame().getSize();

        int fieldSimpleWidth = (int) (windowSize.getWidth() / scaleX);
        int fieldSimpleHeight = (int) (windowSize.getHeight() / scaleY);

        int minFontSize = 8; // Минимальный размер шрифта
        int maxFontSize = 48; // Максимальный размер шрифта

        // Рассчитываем размер шрифта на основе размеров кнопки
        int fontSize = Math.max(minFontSize, Math.min(Math.min(fieldSimpleWidth / 11, fieldSimpleHeight / 2), maxFontSize));

        fieldSimple.setFont(chatView.getMainFont().deriveFont(Font.PLAIN, fontSize));

        // Устанавливаем размер кнопок
        fieldSimple.setPreferredSize(new Dimension(Math.max(fieldSimpleWidth, minWidth), Math.max(fieldSimpleHeight, minHeight)));
        fieldSimple.revalidate();
    }
}
