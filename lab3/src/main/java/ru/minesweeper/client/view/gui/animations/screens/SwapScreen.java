package ru.minesweeper.client.view.gui.animations.screens;

import ru.minesweeper.client.view.gui.screens.Screen;
import ru.minesweeper.server.model.entities.datatransfer.DataTransferObject;
import ru.minesweeper.observer.Notifications;
import ru.minesweeper.observer.Observable;
import ru.minesweeper.observer.Observer;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class SwapScreen implements Observable {
    private final LinkedList<Observer> observers = new LinkedList<>();
    private final Timer animationTimer;

    private static final int SWAP_SPEED = 80;
    private static final int FPS = 80;

    private Screen currentOldScreen;
    private Screen currentNewScreen;
    private Sides currentSide;
    private JFrame currentFrame;

    public SwapScreen() {
        animationTimer = new Timer(1000/FPS, e -> {
            switch (currentSide) {
                case RIGHT:
                    animateRight(currentOldScreen, currentNewScreen);
                    break;
                case LEFT:
                    animateLeft(currentOldScreen, currentNewScreen);
                    break;
                case DOWN:
                    animateDown(currentOldScreen, currentNewScreen);
                    break;
                case UP:
                    animateUp(currentOldScreen, currentNewScreen);
                    break;
            }
            if (isAnimationComplete(currentNewScreen, currentSide)) {
                ((Timer) e.getSource()).stop();
                onComplete(currentFrame, currentNewScreen);
            }
        });
    }

    public void animateScreenSwap(Screen oldScreen, Screen newScreen, Sides side, JFrame frame) {
        notifyObservers(Notifications.SWAP_STARTED, null);
        System.out.println("SWAP STARTED");
        int width = frame.getWidth();
        int height = frame.getHeight();

        // Сохраняем текущие параметры анимации
        this.currentOldScreen = oldScreen;
        this.currentNewScreen = newScreen;
        this.currentSide = side;
        this.currentFrame = frame;

        // Устанавливаем размеры для панелей
        oldScreen.setSize(width, height);
        newScreen.setSize(width, height);

        // Устанавливаем начальное положение в зависимости от направления
        switch (side) {
            case RIGHT:
                oldScreen.setLocation(0, 0);
                newScreen.setLocation(width, 0);
                break;
            case LEFT:
                oldScreen.setLocation(0, 0);
                newScreen.setLocation(-width, 0);
                break;
            case DOWN:
                oldScreen.setLocation(0, 0);
                newScreen.setLocation(0, height);
                break;
            case UP:
                oldScreen.setLocation(0, 0);
                newScreen.setLocation(0, -height);
                break;
        }

        oldScreen.setVisible(true);
        newScreen.setVisible(true);

        // Создаем контейнер с null layout
        JPanel container = new JPanel(null);
        container.setPreferredSize(new Dimension(width, height));
        frame.setContentPane(container);

        // Добавляем экраны в контейнер
        container.add(oldScreen);
        container.add(newScreen);

        // Запускаем анимацию
        animationTimer.start();
    }

    private void onComplete(JFrame frame, JPanel newScreen) {
        notifyObservers(Notifications.SWAP_SUCCESS, null);
        frame.setContentPane(newScreen);
        frame.revalidate();
        frame.repaint();

        // Очищаем ссылки
        currentOldScreen = null;
        currentNewScreen = null;
        currentSide = null;
        currentFrame = null;
    }

    private void animateRight(JPanel oldScreen, JPanel newScreen) {
        oldScreen.setLocation(oldScreen.getX() - SWAP_SPEED, 0);
        newScreen.setLocation(newScreen.getX() - SWAP_SPEED, 0);
    }

    private void animateLeft(JPanel oldScreen, JPanel newScreen) {
        oldScreen.setLocation(oldScreen.getX() + SWAP_SPEED, 0);
        newScreen.setLocation(newScreen.getX() + SWAP_SPEED, 0);
    }

    private void animateDown(JPanel oldScreen, JPanel newScreen) {
        oldScreen.setLocation(0, oldScreen.getY() - SWAP_SPEED);
        newScreen.setLocation(0, newScreen.getY() - SWAP_SPEED);
    }

    private void animateUp(JPanel oldScreen, JPanel newScreen) {
        oldScreen.setLocation(0, oldScreen.getY() + SWAP_SPEED);
        newScreen.setLocation(0, newScreen.getY() + SWAP_SPEED);
    }

    private boolean isAnimationComplete(JPanel newScreen, Sides side) {
        switch (side) {
            case RIGHT:
                return newScreen.getX() <= 0;
            case LEFT:
                return newScreen.getX() >= 0;
            case DOWN:
                return newScreen.getY() <= 0;
            case UP:
                return newScreen.getY() >= 0;
            default:
                return false;
        }
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Notifications notification, DataTransferObject data) {
        for (Observer observer : observers) {
            observer.notify(notification, data);
        }
    }
}