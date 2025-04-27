package ru.minesweeper.client.view.gui.animations.screens;

import ru.minesweeper.client.view.gui.screens.Screen;

import javax.swing.*;
import java.awt.*;

public class AlertScreen { ;
    int sizeAlert;
    Color colorAlert;
    Color colorWithoutAlert;
    Color colorNow;
    Timer timer;

    JFrame frame;
    Screen screen;

    boolean isShowing=false;
    boolean isActive=false;

    public AlertScreen(int SizeAlert, Color colorAlert, JFrame frame) {
        this.sizeAlert = SizeAlert;
        this.colorAlert = colorAlert;
        this.frame = frame;
        this.colorNow = colorAlert;
    }

    public void createAlert(Screen screen, Color withoutAlert) {
        this.screen = screen;
        this.colorWithoutAlert = withoutAlert;
        timer = new Timer(500, e -> startAlert());
        timer.start();
        isActive=true;
    }

    public boolean isAlert() {
        return isActive;
    }

    private void startAlert() {
        if (isShowing) {
            colorNow = colorWithoutAlert;
            isShowing = false;
        }
        else {
            colorNow = colorAlert;
            isShowing = true;
        }
        screen.setBorder(BorderFactory.createLineBorder(colorNow, sizeAlert));
    }

    public void stopAlert() {
        if (timer!=null&&timer.isRunning()) {
            timer.stop();
        }
        if (screen!=null) {
            screen.setBorder(BorderFactory.createLineBorder(colorWithoutAlert, sizeAlert));
        }
        isActive=false;
    }
}
