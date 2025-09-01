package client.view.components.buttons;

import javax.swing.*;
import java.awt.*;

public class ButtonSimple extends JButton {
    public ButtonSimple() {
        super();
    }
    public ButtonSimple(String text) {
        super(text);
    }

    public ButtonSimple(Font font, Color color) {
        //устанавливаем шрифт
        setFont(font);
        //ставим цвет
        setBackground(color);
        //удаляем обводку при нажатии
        setFocusPainted(false);
    }

}
