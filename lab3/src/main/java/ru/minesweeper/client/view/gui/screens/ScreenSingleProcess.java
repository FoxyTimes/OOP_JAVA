package ru.minesweeper.client.view.gui.screens;

import ru.minesweeper.server.model.entities.datatransfer.DataTransferObject;
import ru.minesweeper.observer.Notifications;

import ru.minesweeper.observer.Observer;
import ru.minesweeper.client.view.gui.GameView;
import ru.minesweeper.client.view.gui.adaptive.AdaptiveToBut;
import ru.minesweeper.client.view.gui.animations.screens.Sides;
import ru.minesweeper.client.view.gui.animations.text.TextAnim;
import ru.minesweeper.client.view.gui.components.labels.LabelTextable;
import ru.minesweeper.client.view.gui.components.buttons.VisualMenuButtonTextable;
import ru.minesweeper.client.view.gui.components.buttons.ButtonCreator;
import ru.minesweeper.client.view.gui.components.buttons.VisualFieldButtonForSingle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

import static ru.minesweeper.client.view.gui.animations.buttons.AnimToButton.offMoused;

public class ScreenSingleProcess extends Screen implements Observer {
    GameView gameView;


    final int MIN_BUTTON_WIDTH = 120;
    final int MIN_BUTTON_HEIGHT = 60;

    final int ADAPTIVE_SCALE_MENU_BUTTON_X = 8;
    final int ADAPTIVE_SCALE_MENU_BUTTON_Y = 11;

    boolean isScreenSwap=false;
    boolean isFieldCreated=false;

    LabelTextable labelWithTime;

    JPanel fieldPanel;
    JPanel sidePanel;

    ArrayList<ArrayList<VisualFieldButtonForSingle>> buttons;

    ButtonCreator buttonCreator;

    int time=0;

    private JPanel glassPanel; // Наш кастомный glassPane
    private JLabel gameOverLabel;
    public ScreenSingleProcess(GameView gameView) {
        super();
        this.gameView = gameView;
        this.buttonCreator = new ButtonCreator(
                gameView,
                MIN_BUTTON_WIDTH,
                MIN_BUTTON_HEIGHT
        );
        setLayout(new BorderLayout());
        initGlassPane(); // Инициализируем glassPane
        putGameProcess();
    }

    private void initGlassPane() {
        glassPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                // Полупрозрачный черный фон
                g.setColor(new Color(0, 0, 0, 90));
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        glassPanel.setLayout(new BoxLayout(glassPanel, BoxLayout.Y_AXIS));
        glassPanel.setOpaque(false);


        gameOverLabel = new JLabel("GAME OVER");
        gameOverLabel.setFont(gameView.getMainFont().deriveFont(Font.BOLD, 48));
        gameOverLabel.setForeground(Color.RED);
        gameOverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        VisualMenuButtonTextable restartButton = buttonCreator.createButtonText("BACK", this::backToMenu, this, ADAPTIVE_SCALE_MENU_BUTTON_X, ADAPTIVE_SCALE_MENU_BUTTON_Y);

        Component verticalStrut = Box.createVerticalStrut(20);


        glassPanel.add(Box.createVerticalGlue());
        glassPanel.add(gameOverLabel);
        glassPanel.add(verticalStrut);
        glassPanel.add(restartButton);
        glassPanel.add(Box.createVerticalGlue());



        restartButton.setPreferredSize(new Dimension(MIN_BUTTON_WIDTH, MIN_BUTTON_HEIGHT));

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                AdaptiveToBut.adjustButtonSize(restartButton, gameView, MIN_BUTTON_WIDTH, MIN_BUTTON_HEIGHT, ADAPTIVE_SCALE_MENU_BUTTON_X, ADAPTIVE_SCALE_MENU_BUTTON_Y);
            }
        });
    }

    public void gameLose() {
        showGameOver("GAME OVER", Color.RED);
        gameView.stopTimer();
    }

    public void gameWin() {
        showGameOver("YOU WIN!!!", Color.GREEN);
        gameView.gameWin(time);
    }


    private void showGameOver(String gameOver, Color color) {
        // Получаем rootPane и устанавливаем наш glassPane
        gameOverLabel.setText(gameOver);
        gameOverLabel.setForeground(color);
        JRootPane rootPane = SwingUtilities.getRootPane(this);
        if (rootPane != null) {
            rootPane.setGlassPane(glassPanel);
            glassPanel.setVisible(true);
        }
    }

    private void hideGameOver() {
        JRootPane rootPane = SwingUtilities.getRootPane(this);
        if (rootPane != null) {
            glassPanel.setVisible(false);
        }
    }

    private void putGameProcess() {
        setBackground(gameView.getColorBackground());
        setLayout(new BorderLayout());
    }

    public void createSidePanel(JPanel container) {
        sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.X_AXIS));
        sidePanel.setBackground(gameView.getColorSidePanel());


        labelWithTime = new LabelTextable("00:00");
        labelWithTime.setFont(gameView.getMainFont().deriveFont(Font.PLAIN, 18));


        VisualMenuButtonTextable backGameButton = buttonCreator.createButtonText("Back", this::backToMenu, this, ADAPTIVE_SCALE_MENU_BUTTON_X, ADAPTIVE_SCALE_MENU_BUTTON_Y);

        sidePanel.add(Box.createHorizontalStrut(10));
        sidePanel.add(backGameButton);
        sidePanel.add(Box.createHorizontalGlue());
        sidePanel.add(labelWithTime);
        sidePanel.add(Box.createHorizontalStrut(10));


        sidePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        sidePanel.setPreferredSize(new Dimension(sidePanel.getPreferredSize().width, 40));

        container.add(sidePanel, BorderLayout.NORTH);
    }


    public void showBombs(DataTransferObject data) {
        for (int i = 0; i < data.getData().getWidth(); i++) {
            for (int j = 0; j < data.getData().getHeight(); j++) {
                buttons.get(i).get(j).changeType(data.getData().getCell(i,j), j, i, true);
            }
        }
        repaint();
    }

    private void backToMenu(TextAnim<VisualMenuButtonTextable> anim, VisualMenuButtonTextable button) {
        if (glassPanel.isVisible()) {
            hideGameOver();
        }
        gameView.stopHost();
        offMoused(anim, button);
        gameView.terminateGame();
        remove(fieldPanel);
        if (!isScreenSwap) {
            gameView.swapScreenToAnotherScreen(this, ScreensE.SCREEN_GAME_MENU,Sides.DOWN);
        }
    }

    public GameView getGameView() {
        return gameView;
    }

    public void setTime(int time) {
        int minutes = time / 60;
        int seconds = time % 60;
        String toSet = String.format("%02d:%02d", minutes, seconds);
        labelWithTime.setText(toSet);

        this.time = time;
    }

    public void createEmptyField(DataTransferObject data) {

        createSidePanel(this);

        // Панель с полем
        fieldPanel = new JPanel();
        fieldPanel.setOpaque(false);

        // Размеры поля
        int width = data.getData().getWidth();
        int height = data.getData().getHeight();

        // Лейаут для поля
        GridLayout gridLayout = new GridLayout(width, height);
        fieldPanel.setLayout(gridLayout);

        // Заполнение поля пустыми клетками
        buttons = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            buttons.add(new ArrayList<>());
            for (int j = 0; j < height; j++) {
                VisualFieldButtonForSingle button = new VisualFieldButtonForSingle(this, gameView.getFlagTexture(), gameView.getBombTexture());
                button.makeVisual(gameView.getMainFont().deriveFont(Font.PLAIN, 10), gameView.getColorButton());
                int finalI = i;
                int finalJ = j;
                button.addActionListener(e -> gameView.createFillField(finalJ, finalI));
                buttons.get(i).add(button);
                fieldPanel.add(buttons.get(i).get(j));
            }
        }

        // Добавляем fieldPanel в центр основного контейнера
        add(fieldPanel, BorderLayout.CENTER);
        //hideFiled();
        isFieldCreated = true;
    }

    // Остальные методы остаются без изменений
    public void changeField(DataTransferObject data) {
        for (int i = 0; i < data.getData().getWidth(); i++) {
            for (int j = 0; j < data.getData().getHeight(); j++) {
                buttons.get(i).get(j).changeType(data.getData().getCell(i,j), j, i, false);
            }
        }
        repaint();
    }

    public void openCell(int x, int y) {
        gameView.openCell(x, y);
    }

    /*public void hideFiled() {
        fieldPanel.setVisible(false);
    }

    public void showField() {
        fieldPanel.setVisible(true);
    }*/

    @Override
    public void notify(Notifications notification, DataTransferObject data) {
        switch (notification) {
            case SWAP_STARTED:
                isScreenSwap=true;
                break;
            case SWAP_SUCCESS:
                isScreenSwap=false;
                break;
        }
    }

    @Override
    public void stopTimers() {
        //
    }


}
