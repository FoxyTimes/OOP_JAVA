package ru.minesweeper.view.gui.components.labels;

import ru.minesweeper.view.gui.GameView;
import ru.minesweeper.view.gui.adaptive.buttons.AdaptiveToBut;
import ru.minesweeper.view.gui.adaptive.labels.AdaptiveToLab;
import ru.minesweeper.view.gui.animations.text.TextAnim;
import ru.minesweeper.view.gui.components.buttons.MenuButtonCreator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MenuLabelCreator {
    GameView gameView;
    final int minWidth;
    final int minHeight;


    public MenuLabelCreator(GameView gameView, int minWidth, int minHeight) {
        this.gameView = gameView;
        this.minWidth = minWidth;
        this.minHeight = minHeight;
    }

    public LabelSimple createLabelSimple(String text, JPanel screenFrom, int scaleX, int scaleY) {
        LabelSimple labelSimple = new LabelSimple(text);
        labelSimple.setFont(gameView.getMainFont().deriveFont(Font.PLAIN, 48));

        setupButtonResizeListener(labelSimple, screenFrom, scaleX, scaleY);

        return labelSimple;
    }

    public LabelTextable createLabelText(String text, JPanel screenFrom, int scaleX, int scaleY) {
        LabelTextable labelTextable = new LabelTextable(text);
        labelTextable.setFont(gameView.getMainFont().deriveFont(Font.PLAIN, 48));

        TextAnim<LabelTextable> labelAnim = new TextAnim<>(labelTextable, text);
        labelTextable.setTextAnim(labelAnim);

        labelAnim.startColorChange(text);
        setupButtonResizeListener(labelTextable, screenFrom, scaleX, scaleY);

        return labelTextable;
    }

    private void setupButtonResizeListener(JLabel label, JPanel screenFrom, int scaleX, int scaleY) {
        screenFrom.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                AdaptiveToLab.adjustLabelSize(label, gameView, minWidth, minHeight, scaleX, scaleY);
            }
        });
    }
}
