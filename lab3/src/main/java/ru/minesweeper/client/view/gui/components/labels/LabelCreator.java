package ru.minesweeper.client.view.gui.components.labels;

import ru.minesweeper.client.view.gui.GameView;
import ru.minesweeper.client.view.gui.adaptive.AdaptiveToLab;
import ru.minesweeper.client.view.gui.animations.text.TextAnim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class LabelCreator {
    GameView gameView;
    final int minWidth;
    final int minHeight;


    public LabelCreator(GameView gameView, int minWidth, int minHeight) {
        this.gameView = gameView;
        this.minWidth = minWidth;
        this.minHeight = minHeight;
    }

    public LabelSimple createLabelSimple(String text, JPanel screenFrom, int scaleX, int scaleY) {
        LabelSimple labelSimple = new LabelSimple(text);
        labelSimple.setFont(gameView.getMainFont().deriveFont(Font.PLAIN, 48));

        setupLabelResizeListener(labelSimple, screenFrom, scaleX, scaleY);

        return labelSimple;
    }

    public LabelTextable createLabelText(String text, JPanel screenFrom, int scaleX, int scaleY) {
        LabelTextable labelTextable = new LabelTextable(text);
        labelTextable.setFont(gameView.getMainFont().deriveFont(Font.PLAIN, 48));

        TextAnim<LabelTextable> labelAnim = new TextAnim<>(labelTextable, text);
        labelTextable.setTextAnim(labelAnim);

        labelAnim.startColorChange();
        setupLabelResizeListener(labelTextable, screenFrom, scaleX, scaleY);

        return labelTextable;
    }

    private void setupLabelResizeListener(JLabel label, JPanel screenFrom, int scaleX, int scaleY) {
        screenFrom.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                AdaptiveToLab.adjustLabelSize(label, gameView, minWidth, minHeight, scaleX, scaleY);
            }
        });
    }
}
