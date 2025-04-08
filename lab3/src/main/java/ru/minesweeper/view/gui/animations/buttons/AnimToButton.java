package ru.minesweeper.view.gui.animations.buttons;

import ru.minesweeper.view.gui.animations.text.TextAnim;
import ru.minesweeper.view.gui.components.buttons.VisualMenuButtonTextable;

import javax.swing.*;
import java.awt.*;

public class AnimToButton {
    //базовый цвет
    static final int RED = 93;
    static final int GREEN = 155;
    static final int BLUE = 155;

    static final Color COLOR_BORDERS = new Color(RED-10, GREEN-10, BLUE+10);

    //толщина бордера при наведении мыши
    static final int BORDER_THICKNESS = 5;

    static public void onMoused(JButton button) {
        button.setBorder(BorderFactory.createLineBorder(COLOR_BORDERS, BORDER_THICKNESS));
    }

    static public void offMoused(JButton button) {
        button.setBorder(BorderFactory.createEmptyBorder());
    }

    static public void onMoused(TextAnim<VisualMenuButtonTextable> anim, VisualMenuButtonTextable button, String text) {
        if(anim.isOn()) {
            offMoused(anim, button);
        }
        anim.startColorChange(text);
        button.setBorder(BorderFactory.createLineBorder(COLOR_BORDERS, BORDER_THICKNESS));
    }

    static public void offMoused(TextAnim<VisualMenuButtonTextable> anim, VisualMenuButtonTextable button) {
        anim.stopColorChange();
        button.setBorder(BorderFactory.createEmptyBorder());
    }
}
