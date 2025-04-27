package ru.minesweeper.client.view.gui.components.textfields;

import ru.minesweeper.client.view.gui.GameView;
import ru.minesweeper.client.view.gui.animations.text.TextAnim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static ru.minesweeper.client.view.gui.animations.buttons.AnimToButton.offMoused;
import static ru.minesweeper.client.view.gui.animations.buttons.AnimToButton.onMoused;

public interface TextFieldStandartSettings {


    void makeVisual(Font font, Color color);

    default void standartSettingsTextField(Font font, Color color) {
        makeVisual(font, color);
    }
}
