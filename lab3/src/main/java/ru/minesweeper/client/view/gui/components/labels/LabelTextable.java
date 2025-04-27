package ru.minesweeper.client.view.gui.components.labels;

import ru.minesweeper.client.view.gui.animations.text.TextAnim;
import ru.minesweeper.client.view.gui.animations.text.textable.Textable;

import javax.swing.*;

public class LabelTextable extends JLabel implements Textable {
    TextAnim<LabelTextable> textAnim;
    String text;

    public LabelTextable(String text) {
        super(text);
    }

    public void stopColorChange() {
        textAnim.stopColorChange();
    }

    public void startColorChange() {
        textAnim.startColorChange();
    }

    public void setTextAnim(TextAnim<LabelTextable> textAnim) {
        this.textAnim = textAnim;
    }

    @Override
    public void putText(String text) {
        this.setText(text);
    }


}
