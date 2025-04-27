package ru.minesweeper.client.view.gui.components.textfields;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class TextFieldSimple extends JTextField implements TextFieldStandartSettings {
    private String hintText;
    private boolean hintShowing;

    public TextFieldSimple(String hintText) {
        super(hintText);
        this.hintText = hintText;
        this.hintShowing = true;
        setupHintBehavior();
    }

    @Override
    public void makeVisual(Font font, Color color) {
        // Выравниваем по центру
        setAlignmentX(Component.CENTER_ALIGNMENT);
        // Устанавливаем шрифт
        setFont(font);
        // Ставим цвет фона
        setBackground(color);
        // Устанавливаем цвет текста (для hint и обычного текста)
        updateTextColor();

        // Дополнительные настройки для лучшего вида
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.darker()),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private void setupHintBehavior() {
        setForeground(Color.GRAY);

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (hintShowing) {
                    setText("");
                    hintShowing = false;
                    setForeground(getBackground().brighter());
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setText(hintText);
                    hintShowing = true;
                    setForeground(Color.GRAY);
                }
            }
        });
    }

    private void updateTextColor() {
        if (hintShowing) {
            setForeground(Color.GRAY);
        } else {
            setForeground(getBackground().brighter());
        }
    }

    @Override
    public String getText() {
        return hintShowing ? "" : super.getText();
    }
}