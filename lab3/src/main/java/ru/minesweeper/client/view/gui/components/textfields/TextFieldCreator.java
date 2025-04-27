package ru.minesweeper.client.view.gui.components.textfields;

import ru.minesweeper.client.view.gui.GameView;
import ru.minesweeper.client.view.gui.adaptive.AdaptiveToTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class TextFieldCreator {
    GameView gameView;
    final int minWidth;
    final int minHeight;


    public TextFieldCreator(GameView gameView, int minWidth, int minHeight) {
        this.gameView = gameView;
        this.minWidth = minWidth;
        this.minHeight = minHeight;
    }

    public TextFieldSimple createTextFieldSimple(String text, JPanel screenFrom, int scaleX, int scaleY) {
        TextFieldSimple fieldSimple = new TextFieldSimple(text);
        fieldSimple.standartSettingsTextField(gameView.getMainFont().deriveFont(Font.PLAIN, 48), gameView.getColorSidePanel());

        setupButtonResizeListener(fieldSimple, screenFrom, scaleX, scaleY);

        return fieldSimple;
    }


    private void setupButtonResizeListener(TextFieldSimple fieldSimple, JPanel screenFrom, int scaleX, int scaleY) {
        screenFrom.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                AdaptiveToTextField.adjustTextFieldSize(fieldSimple, gameView, minWidth, minHeight, scaleX, scaleY);
            }
        });
    }
}
