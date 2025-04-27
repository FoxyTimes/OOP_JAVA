package ru.minesweeper.client.view.gui.components.buttons;

import javax.swing.*;
import java.awt.*;

public class VisualMenuButton extends JButton implements ButtonStandartSettings {
    public VisualMenuButton() {
        super();
    }
    public VisualMenuButton(String text) {
        super(text);
    }



    @Override
    public void makeVisual(Font font, Color color) {
        //выравнимаем по центру
        setAlignmentX(Component.CENTER_ALIGNMENT);
        //устанавливаем шрифт
        setFont(font);
        //ставим цвет
        setBackground(color);
        //удаляем обводку при нажатии
        setFocusPainted(false);
    }
}
