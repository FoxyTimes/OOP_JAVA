package ru.minesweeper.view.gui.components.buttons;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageMenuButton extends JButton implements ButtonStandartSettings {
    private BufferedImage texture;
    private Image scaledImage; // Кэшируем масштабированное изображение
    private int lastWidth = -1;
    private int lastHeight = -1;

    public ImageMenuButton(BufferedImage texture) {
        super();
        this.texture = texture;
        setOpaque(true);
    }

    @Override
    protected void paintComponent(Graphics g) {

        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }


        if (texture != null && (scaledImage == null ||
                getWidth() != lastWidth ||
                getHeight() != lastHeight)) {
            scaleImage();
        }


        if (scaledImage != null) {
            // Рассчитываем область для изображения с учетом бордюра
            Insets insets = getInsets();
            int x = insets.left;
            int y = insets.top;
            int width = getWidth() - insets.left - insets.right;
            int height = getHeight() - insets.top - insets.bottom;

            g.drawImage(scaledImage, x, y, width, height, this);
        }
    }

    private void scaleImage() {
        if (texture == null) return;

        Insets insets = getInsets();
        int width = getWidth() - insets.left - insets.right;
        int height = getHeight() - insets.top - insets.bottom;

        if (width > 0 && height > 0) {
            scaledImage = texture.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            lastWidth = getWidth();
            lastHeight = getHeight();
        }
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        // При изменении размера перемасштабируем
        scaleImage();
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

        setForeground(color.brighter());
    }
}
