package ru.minesweeper.client.view.gui.animations.buttons;

import ru.minesweeper.client.view.gui.animations.text.TextAnim;
import ru.minesweeper.client.view.gui.components.buttons.VisualMenuButtonTextable;

import javax.swing.*;
import java.awt.*;

public class AnimToButton {

    //толщина бордера при наведении мыши
    static final int BORDER_THICKNESS = 5;

    static public void onMoused(JButton button, Color colorBorders) {
        button.setBorder(BorderFactory.createLineBorder(colorBorders, BORDER_THICKNESS));
    }

    static public void offMoused(JButton button) {
        button.setBorder(BorderFactory.createEmptyBorder());
    }

    static public void onMoused(TextAnim<VisualMenuButtonTextable> anim, VisualMenuButtonTextable button, String text, Color colorBorders) {
        if(anim.isOn()) {
            offMoused(anim, button);
        }
        anim.startColorChange();
        button.setBorder(BorderFactory.createLineBorder(colorBorders, BORDER_THICKNESS));
    }

    static public void offMoused(TextAnim<VisualMenuButtonTextable> anim, VisualMenuButtonTextable button) {
        anim.stopColorChange();
        button.setBorder(BorderFactory.createEmptyBorder());
    }
}
