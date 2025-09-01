package client.view.components.textfields;

import client.view.ChatView;
import client.view.Colors;
import client.view.adaptive.AdaptiveToTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class TextFieldCreator {
    private final ChatView chatView;
    private final int minWidth;
    private final int minHeight;


    public TextFieldCreator(ChatView chatView, int minWidth, int minHeight) {
        this.chatView = chatView;
        this.minWidth = minWidth;
        this.minHeight = minHeight;
    }

    public TextFieldSimple createTextFieldSimple(String text, JPanel screenFrom, int scaleX, int scaleY) {
        TextFieldSimple fieldSimple = new TextFieldSimple(text);
        fieldSimple.standartSettingsTextField(chatView.getMainFont().deriveFont(Font.PLAIN, 48), Colors.getMessageColor());

        setupButtonResizeListener(fieldSimple, screenFrom, scaleX, scaleY);

        return fieldSimple;
    }


    private void setupButtonResizeListener(TextFieldSimple fieldSimple, JPanel screenFrom, int scaleX, int scaleY) {
        screenFrom.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                SwingUtilities.invokeLater(() -> AdaptiveToTextField.adjustTextFieldSize(fieldSimple, chatView, minWidth, minHeight, scaleX, scaleY));
            }
        });
    }
}
