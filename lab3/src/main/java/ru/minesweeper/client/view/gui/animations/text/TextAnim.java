package ru.minesweeper.client.view.gui.animations.text;

import ru.minesweeper.client.view.gui.animations.text.textable.Textable;

import javax.swing.*;

public class TextAnim<T extends Textable> {
    T mainName;
    Timer timer;

    final int TIMER_DELAY = 150;
    int currentLetterIndex=0;
    String baseText;

    boolean isOn = false;

    public TextAnim(T mainName, String baseText) {
        this.baseText = baseText;
        this.mainName = mainName;
    }

    public void startColorChange() {
        timer = new Timer(TIMER_DELAY, e -> changeLetterColor(baseText));
        timer.start();
        isOn = true;
    }

    public void stopColorChange() {
        timer.stop();
        mainName.putText(baseText);
        currentLetterIndex=0;
        isOn = false;
    }

    public boolean isOn() {
        return isOn;
    }

    private void changeLetterColor(String text) {
        StringBuilder coloredText = new StringBuilder();


        coloredText.append("<html><nobr>");
        // Проходим по каждому символу в строке
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            // Меняем цвет только на определенной букве
            if (i == currentLetterIndex) {
                coloredText.append("<span style='color: red;'>").append(c).append("</span>");
            } else {
                coloredText.append(c);
            }
        }

        coloredText.append("</nobr></html>");

        mainName.putText(coloredText.toString());
        currentLetterIndex = (currentLetterIndex + 1) % text.length();
    }
}
