package ru.minesweeper.view.gui.components.buttons;

import ru.minesweeper.view.gui.animations.text.textable.Textable;

import javax.swing.*;

public class VisualMenuButtonTextable extends VisualMenuButton implements Textable {

    public VisualMenuButtonTextable(String text) {
        super(text);
    }

    @Override
    public void putText(String text) {
        setText(text);
    }

}
