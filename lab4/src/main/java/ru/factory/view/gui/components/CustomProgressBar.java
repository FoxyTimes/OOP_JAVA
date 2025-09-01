package ru.factory.view.gui.components;

import javax.swing.*;
import java.awt.*;

public class CustomProgressBar extends JProgressBar {
    private Color textColor = Color.BLACK;

    public CustomProgressBar() {
        super();
        setStringPainted(true);
        setString("0/0");
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        boolean oldPaintString = isStringPainted();
        setStringPainted(false);

        super.paintComponent(g);

        setStringPainted(oldPaintString);

        if (isStringPainted()) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Получаем текущий текст (например, "50%")
            String progressText = getString();

            // Настраиваем шрифт и цвет текста
            g2d.setFont(getFont());
            g2d.setColor(textColor);

            // Рассчитываем позицию текста по центру
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(progressText);
            int textX = (getWidth() - textWidth) / 2;
            int textY = (getHeight() / 2) + fm.getAscent() / 2 - 2;

            // Рисуем текст
            g2d.drawString(progressText, textX, textY);

            g2d.dispose();
        }
    }
}
