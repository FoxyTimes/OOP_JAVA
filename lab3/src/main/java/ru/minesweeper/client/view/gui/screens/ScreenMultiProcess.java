package ru.minesweeper.client.view.gui.screens;

import ru.minesweeper.client.view.gui.components.buttons.VisualFieldButtonForMulti;
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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

import static ru.minesweeper.client.view.gui.animations.buttons.AnimToButton.offMoused;

public class ScreenMultiProcess extends Screen implements Observer {
    GameView gameView;


    final int MIN_BUTTON_WIDTH = 120;
    final int MIN_BUTTON_HEIGHT = 60;

    final int ADAPTIVE_SCALE_MENU_BUTTON_X = 8;
    final int ADAPTIVE_SCALE_MENU_BUTTON_Y = 11;

    final int SIDE_PANEL_MAX_WIDTH = 150;

    boolean isScreenSwap=false;
    boolean isFieldCreated=false;

    LabelTextable labelWithTimePlayer1;
    LabelTextable labelWithTimePlayer2;

    JPanel fieldPanel;

    JPanel sidePanelPlayer1;
    JPanel sidePanelPlayer2;

    ArrayList<ArrayList<VisualFieldButtonForMulti>> buttons;

    ButtonCreator buttonCreator;


    private JPanel glassPanel; // Наш кастомный glassPane
    private JLabel gameOverLabel;
    public ScreenMultiProcess(GameView gameView) {
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

    public void gameLose(String winner) {
        showGameOver(winner + " wins", Color.RED);
        gameView.stopTimer();
    }

    public void gameWin(String winner) {
        showGameOver(winner + " wins", Color.GREEN);
        gameView.gameWin();
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

    public void hideGameOver() {
        JRootPane rootPane = SwingUtilities.getRootPane(this);
        if (rootPane != null) {
            glassPanel.setVisible(false);
        }
    }

    private void putGameProcess() {
        setBackground(gameView.getColorBackground());
        setLayout(new BorderLayout());
    }

    public JPanel createSidePanel(int numberOfPlayer, String nickNamePlayer1, String nickNamePlayer2) {
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BorderLayout());
        sidePanel.setBackground(gameView.getColorSidePanel());

        sidePanel.setMaximumSize(new Dimension(SIDE_PANEL_MAX_WIDTH, Integer.MAX_VALUE));
        sidePanel.setPreferredSize(new Dimension(SIDE_PANEL_MAX_WIDTH, getHeight()));

        VisualMenuButtonTextable backGameButton = buttonCreator.createButtonText("Back", this::backToMenu, this, ADAPTIVE_SCALE_MENU_BUTTON_X, ADAPTIVE_SCALE_MENU_BUTTON_Y);

        if (numberOfPlayer == 1) {
            JPanel player1Panel = new JPanel(new BorderLayout());
            player1Panel.setOpaque(false);

            LabelTextable labelWithNickNamePlayer1 = new LabelTextable(nickNamePlayer1);
            labelWithNickNamePlayer1.setFont(gameView.getMainFont().deriveFont(Font.PLAIN, 16));
            labelWithNickNamePlayer1.setHorizontalAlignment(SwingConstants.CENTER);
            player1Panel.add(labelWithNickNamePlayer1, BorderLayout.NORTH);

            labelWithTimePlayer1 = new LabelTextable("00:00");
            labelWithTimePlayer1.setFont(gameView.getMainFont().deriveFont(Font.PLAIN, 18));
            labelWithTimePlayer1.setHorizontalAlignment(SwingConstants.CENTER);
            player1Panel.add(labelWithTimePlayer1, BorderLayout.SOUTH);


            player1Panel.setBorder(BorderFactory.createEmptyBorder(25, 0, 25, 0));
            sidePanel.add(backGameButton, BorderLayout.NORTH);
            sidePanel.add(player1Panel, BorderLayout.SOUTH);;
        }
        else if (numberOfPlayer == 2) {
            JPanel player2Panel = new JPanel(new BorderLayout());
            player2Panel.setOpaque(false);

            LabelTextable labelWithNickNamePlayer2 = new LabelTextable(nickNamePlayer2);
            labelWithNickNamePlayer2.setFont(gameView.getMainFont().deriveFont(Font.PLAIN, 16));
            labelWithNickNamePlayer2.setHorizontalAlignment(SwingConstants.CENTER);
            player2Panel.add(labelWithNickNamePlayer2, BorderLayout.NORTH);

            labelWithTimePlayer2 = new LabelTextable("00:00");
            labelWithTimePlayer2.setFont(gameView.getMainFont().deriveFont(Font.PLAIN, 18));
            labelWithTimePlayer2.setHorizontalAlignment(SwingConstants.CENTER);
            player2Panel.add(labelWithTimePlayer2, BorderLayout.SOUTH);

            player2Panel.setBorder(BorderFactory.createEmptyBorder(25, 0, 25, 0));
            sidePanel.add(backGameButton, BorderLayout.NORTH);
            sidePanel.add(player2Panel, BorderLayout.SOUTH);;
        }

        return sidePanel;
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
        gameView.disconnect();
        offMoused(anim, button);
        gameView.terminateGame();
        remove(fieldPanel);
    }

    public JPanel getGlassPanel() {
        return glassPanel;
    }

    public GameView getGameView() {
        return gameView;
    }

    public void setTimePlayer1(int time) {
        int minutes = time / 60;
        int seconds = time % 60;
        String toSet = String.format("%02d:%02d", minutes, seconds);
        labelWithTimePlayer1.setText(toSet);
    }

    public void setTimePlayer2(int time) {
        int minutes = time / 60;
        int seconds = time % 60;
        String toSet = String.format("%02d:%02d", minutes, seconds);
        labelWithTimePlayer2.setText(toSet);
    }

    public void createEmptyField(DataTransferObject data) {
        if (data.getPlayerNicknamesOnServer().size() > 1) {
            sidePanelPlayer1 = createSidePanel(1, data.getPlayerNicknamesOnServer().get(0), data.getPlayerNicknamesOnServer().get(1));
            add(sidePanelPlayer1, BorderLayout.WEST);
            sidePanelPlayer2 = createSidePanel(2, data.getPlayerNicknamesOnServer().get(0), data.getPlayerNicknamesOnServer().get(1));
            add(sidePanelPlayer2, BorderLayout.EAST);
        }



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
                VisualFieldButtonForMulti button = new VisualFieldButtonForMulti(this, gameView.getFlagTexture(), gameView.getBombTexture());
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
