package client.view.components.buttons;

import client.view.ChatView;
import client.view.adaptive.AdaptiveToBut;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ButtonCreator {
    private final ChatView chatView;
    private final int minWidth;
    private final int minHeight;

    public ButtonCreator(ChatView chatView, int minWidth, int minHeight) {
        this.chatView = chatView;
        this.minWidth = minWidth;
        this.minHeight = minHeight;
    }


    private void setupButtonResizeListener(JButton button, JPanel screenFrom, int scaleX, int scaleY) {
        screenFrom.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                SwingUtilities.invokeLater(() -> AdaptiveToBut.adjustButtonSize(button, chatView, minWidth, minHeight, scaleX, scaleY));
            }
        });
    }

    public ButtonSimple createButtonSimple(String text, ButtonSimpleAction action, JPanel screenFrom, int scaleX, int scaleY) {
        ButtonSimple buttonSimple = new ButtonSimple(text);
        buttonSimple.setFont(chatView.getMainFont().deriveFont(Font.PLAIN, 48));

        if (action != null) {
            buttonSimple.addActionListener(
                    e -> {
                        action.execute();
                    }
            );
        }

        buttonSimple.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        setupButtonResizeListener(buttonSimple, screenFrom, scaleX, scaleY);



        return buttonSimple;
    }


    @FunctionalInterface
    public interface ButtonSimpleAction {
        void execute();
    }

}