package client.view.components.labels;

import client.view.ChatView;
import client.view.adaptive.AdaptiveToLab;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class LabelCreator {
    ChatView chatView;
    final int minWidth;
    final int minHeight;


    public LabelCreator(ChatView chatView, int minWidth, int minHeight) {
        this.chatView = chatView;
        this.minWidth = minWidth;
        this.minHeight = minHeight;
    }

    public LabelSimple createLabelSimple(String text, JPanel screenFrom, int scaleX, int scaleY) {
        LabelSimple labelSimple = new LabelSimple(text);
        labelSimple.setFont(chatView.getMainFont().deriveFont(Font.PLAIN, 48));

        setupLabelResizeListener(labelSimple, screenFrom, scaleX, scaleY);

        return labelSimple;
    }


    private void setupLabelResizeListener(JLabel label, JPanel screenFrom, int scaleX, int scaleY) {
        screenFrom.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                SwingUtilities.invokeLater(() -> AdaptiveToLab.adjustLabelSize(label, chatView, minWidth, minHeight, scaleX, scaleY));
            }
        });
    }
}
