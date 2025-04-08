package ru.minesweeper.view.gui.components.labels;

import ru.minesweeper.view.gui.animations.text.TextAnim;
import ru.minesweeper.view.gui.animations.text.textable.Textable;

import javax.swing.*;

public class LabelTextable extends JLabel implements Textable {
    TextAnim<LabelTextable> textAnim;

    public LabelTextable(String text) {
        super(text);
    }

    public void stopColorChange() {
        textAnim.stopColorChange();
    }

    public void setTextAnim(TextAnim<LabelTextable> textAnim) {
        this.textAnim = textAnim;
    }

    @Override
    public void putText(String text) {
        this.setText(text);
    }


}
