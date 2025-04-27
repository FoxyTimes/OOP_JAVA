package ru.minesweeper.client.view.gui.components.buttons;

import ru.minesweeper.client.view.gui.animations.text.textable.Textable;

public class VisualMenuButtonTextable extends VisualMenuButton implements Textable {

    public VisualMenuButtonTextable(String text) {
        super(text);
    }

    @Override
    public void putText(String text) {
        setText(text);
    }

}
