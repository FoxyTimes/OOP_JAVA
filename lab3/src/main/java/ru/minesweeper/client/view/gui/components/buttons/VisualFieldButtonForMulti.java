package ru.minesweeper.client.view.gui.components.buttons;

import ru.minesweeper.client.view.gui.screens.ScreenMultiProcess;
import ru.minesweeper.server.model.entities.cell.Cell;
import ru.minesweeper.server.model.entities.cell.Conditions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class VisualFieldButtonForMulti extends JButton implements ButtonStandartSettings {

    private final BufferedImage flagTexture;
    private final BufferedImage bombTexture;

    boolean isFlagged = false;
    boolean isShowBomb = false;

    ScreenMultiProcess parent;

    MouseAdapter flagSetter;
    public VisualFieldButtonForMulti(ScreenMultiProcess parent, BufferedImage flagTexture, BufferedImage bombTexture) {
        super();
        this.parent = parent;
        this.flagTexture = flagTexture;
        this.bombTexture = bombTexture;
    }

    public void changeType(Cell cell, int x, int y, boolean isShowBomb) {
        for (ActionListener al : getActionListeners()) {
            removeActionListener(al);
        }
        removeMouseListener(flagSetter);

        if (cell.getCondition()==Conditions.closedWithBomb) {
            this.isShowBomb = isShowBomb;
            if (!isShowBomb) {
                initMouseListener();
                this.addActionListener(e -> {
                    if (!isFlagged) {
                        parent.openCell(x, y);
                    }
                });
            }

        }
        else if (cell.getCondition()==Conditions.closedWithoutBomb) {
            if (!isShowBomb) {
                initMouseListener();
                this.addActionListener(e -> {
                    if (!isFlagged) {
                        parent.openCell(x, y);
                    }
                });
            }
        }

        else if (cell.getCondition()==Conditions.opened) {

            setEnabled(false);

            UIManager.put("Button.disabledText", Color.WHITE);
            UIManager.put("Button.disabledBackground", parent.getGameView().getColorOpenedFieldButton());

            setBackground(parent.getGameView().getColorOpenedFieldButton());
            setForeground(Color.WHITE);
            if (cell.getCountBombs()!=0) {
                setText(String.valueOf(cell.getCountBombs()));
            }
            /*else {
                setEnabled(false);
            }*/
            //TODO
        }
    }

    private void initMouseListener() {
        addMouseListener(flagSetter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    if (!isFlagged) {
                        isFlagged = true;
                    }
                    else {
                        isFlagged = false;
                    }

                }
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Рисуем фон
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());

        // Рисуем текст вручную
        g.setColor(getForeground());
        FontMetrics fm = g.getFontMetrics();
        String text = getText();

        // Если установлен флаг и есть текстура - рисуем иконку
        if (isFlagged && flagTexture != null) {
            Image scaledImage = flagTexture.getScaledInstance(getWidth()/2, getHeight(), Image.SCALE_SMOOTH);
            g.drawImage(scaledImage, 0, 0, this);
        }

        if (isShowBomb) {
            Image scaledImage = bombTexture.getScaledInstance(getWidth()/2, getHeight(), Image.SCALE_SMOOTH);
            g.drawImage(scaledImage, getWidth()/2, 0, this);
        }

        // Проверяем, помещается ли текст
        if (fm.stringWidth(text) <= getWidth()) {
            int x = (getWidth() - fm.stringWidth(text)) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g.drawString(text, x, y);
        } else {
            // Если не помещается — уменьшаем шрифт или обрезаем
            Font smallerFont = getFont().deriveFont(8f); // Например, уменьшаем шрифт
            g.setFont(smallerFont);
            FontMetrics newFm = g.getFontMetrics();
            int x = (getWidth() - newFm.stringWidth(text)) / 2;
            int y = (getHeight() - newFm.getHeight()) / 2 + newFm.getAscent();
            g.drawString(text, x, y);
        }
    }

    @Override
    public void makeVisual(Font font, Color color) {
        //выравнимаем по центру
        setAlignmentX(Component.CENTER_ALIGNMENT);
        //устанавливаем шрифт
        setFont(font);
        //ставим цвет
        setBackground(color);
        //удаляем обво  дку при нажатии
        setFocusPainted(false);

        setForeground(color.brighter());
    }
}
