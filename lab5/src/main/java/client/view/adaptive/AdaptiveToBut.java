package client.view.adaptive;

import client.view.ChatView;

import javax.swing.*;
import java.awt.*;

public class AdaptiveToBut {
    private static final int MIN_FONT_SIZE = 8;
    private static final int MAX_FONT_SIZE = 48;
    static public void adjustButtonSize(JButton button, ChatView chatView, int minWidth, int minHeight, int scaleX, int scaleY) {

        Dimension windowSize = chatView.getFrame().getSize();


        int buttonWidth = (int) (windowSize.getWidth() / scaleX);
        int buttonHeight = (int) (windowSize.getHeight() / scaleY);

        // Рассчитываем размер шрифта на основе размеров кнопки
        int fontSize = Math.max(MIN_FONT_SIZE, Math.min(Math.min(buttonWidth / 11, buttonHeight / 3), MAX_FONT_SIZE));

        button.setFont(chatView.getMainFont().deriveFont(Font.PLAIN, fontSize));

        // Устанавливаем размер кнопок
        button.setPreferredSize(new Dimension(Math.max(buttonWidth, minWidth), Math.max(buttonHeight, minHeight)));
        button.revalidate();
    }

    
}
