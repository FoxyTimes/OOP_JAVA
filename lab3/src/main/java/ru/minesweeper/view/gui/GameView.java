package ru.minesweeper.view.gui;

import ru.minesweeper.Difficulties;
import ru.minesweeper.model.entities.Field;
import ru.minesweeper.model.entities.datatransfer.DataTransferObject;
import ru.minesweeper.observer.Notifications;
import ru.minesweeper.controller.GameController;
import ru.minesweeper.observer.Observer;
import ru.minesweeper.view.gui.animations.screens.SwapScreen;
import ru.minesweeper.view.gui.animations.screens.Sides;
import ru.minesweeper.view.gui.components.buttons.ImageMenuButton;
import ru.minesweeper.view.gui.components.buttons.VisualFieldButton;
import ru.minesweeper.view.gui.components.buttons.VisualMenuButtonTextable;
import ru.minesweeper.view.gui.screens.*;
import ru.minesweeper.view.gui.sounds.PlaySounds;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

public class GameView implements Observer {
    GameController controller;

    JFrame frame = new JFrame();

    GameMenu gameMenu;
    GameProcess gameProcess;
    GameOptionsMenu optionsMenu;
    GameDifficultyMenu difficultyMenu;
    RecordMenu recordMenu;
    GameMultiPlayer multiMenu;

    SwapScreen swapScreen;

    Font customFont = null;

    PlaySounds soundTrack;
    PlaySounds soundPressed;

    BufferedImage flagTexture;
    BufferedImage bombTexture;
    BufferedImage recordTexture;
    BufferedImage blueColorTexture;
    BufferedImage yellowColorTexture;
    BufferedImage greenColorTexture;

    int red=93, green=155, blue=155;

    Color colorBackground = new Color(red, green, blue);
    Color colorButton = new Color(red+10, green+10, blue+10);
    Color colorSidePanel = new Color(red, green, blue+20);
    Color colorOpenedFieldButton = new Color(red+10, green+15, blue+30);

    public GameView(GameController controller) {
        Toolkit.getDefaultToolkit().getSystemEventQueue().push(new TimedEventQueue());
        this.controller = controller;

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        setIcon("/textures/AppIcon.png");
        loadFont("/fonts/PressStart2P.ttf");

        soundTrack = new PlaySounds();
        soundTrack.setFile("/sounds/soundtrack.wav");
        soundTrack.loop();

        soundPressed = new PlaySounds();
        soundPressed.setFile("/sounds/pressed.wav");

        loadTextures();

        swapScreen = new SwapScreen();

        gameMenu = new GameMenu(this);
        gameProcess = new GameProcess(this);
        optionsMenu = new GameOptionsMenu(this);
        difficultyMenu = new GameDifficultyMenu(this);
        multiMenu = new GameMultiPlayer(this);
        recordMenu = new RecordMenu(this);

        swapScreen.addObserver(gameMenu);
        swapScreen.addObserver(optionsMenu);
        swapScreen.addObserver(difficultyMenu);
        swapScreen.addObserver(gameProcess);
        swapScreen.addObserver(multiMenu);
        swapScreen.addObserver(recordMenu);

        frame.setVisible(true);
    }

    public void setPalette(int red, int green, int blue) {
        this.red=red;
        this.green=green;
        this.blue=blue;
        colorBackground = new Color(red, green, blue);
        colorButton = new Color(red+10, green+10, blue+10);
        colorSidePanel = new Color(red, green, blue+20);
        colorOpenedFieldButton = new Color(red+10, green+15, blue+30);

        updateColors();

    }

    public void updateColors() {
        updateButtonsColors();
    }

    private void updateButtonsColors() {
        gameMenu.stopTimers();
        optionsMenu.stopTimers();
        difficultyMenu.stopTimers();
        multiMenu.stopTimers();
        recordMenu.stopTimers();


        gameMenu = new GameMenu(this);
        optionsMenu = new GameOptionsMenu(this);
        difficultyMenu = new GameDifficultyMenu(this);
        recordMenu = new RecordMenu(this);
        multiMenu = new GameMultiPlayer(this);
        gameProcess = new GameProcess(this);

        frame.setContentPane(optionsMenu);
    }

    public Color getColorBackground() {
        return colorBackground;
    }

    public Color getColorButton() {
        return colorButton;
    }

    public Color getColorSidePanel() {
        return colorSidePanel;
    }

    public Color getColorOpenedFieldButton() {
        return colorOpenedFieldButton;
    }

    public void loadFont(String path) {
        try (InputStream fontStream = getClass().getResourceAsStream(path)) {
            assert fontStream != null;
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
        } catch (Exception e) {
            System.out.println("Font not found");
            throw new RuntimeException();
        }
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(customFont);
    }

    public Font getMainFont() {
        return customFont;
    }

    public void exitGame() {
        frame.dispose();
        System.exit(0);
    }

    public void setIcon(String path) {
        URL iconURL = getClass().getResource(path);
        if (iconURL != null) {
            ImageIcon icon = new ImageIcon(iconURL);
            frame.setIconImage(icon.getImage());
            return;
        }
        throw new RuntimeException("Icon not found");
    }

    public JFrame getFrame() {
        return frame;
    }

    public void openGameMenuFirstTime() {
        frame.setContentPane(gameMenu);
        frame.revalidate();
        frame.repaint();
    }

    public void screenSwapToGameProcess(JPanel old, Sides side) {
        swapScreen.animateScreenSwap(old, gameProcess, side, frame);
    }

    public void screenSwapToGameMenu(JPanel old, Sides side) {
        swapScreen.animateScreenSwap(old, gameMenu, side, frame);
    }

    public void screenSwapToOptionsMenu(JPanel old, Sides side) {
        swapScreen.animateScreenSwap(old, optionsMenu, side, frame);
    }

    public void screenSwapToDifficultyMenu(JPanel old, Sides side) {
        swapScreen.animateScreenSwap(old, difficultyMenu, side, frame);
    }

    public void screenSwapToRecordMenu(GameMenu old, Sides side) {
        swapScreen.animateScreenSwap(old, recordMenu, side, frame);
    }

    public void screenSwapToMultiMenu(GameMenu old, Sides side) {
        swapScreen.animateScreenSwap(old, multiMenu, side, frame);
    }


    public void loadWindow() {
        openGameMenuFirstTime();
    }

    public void createEmptyField(Difficulties difficulty) {
        controller.createEmptyField(difficulty);
    }

    public void createFillField(int startX, int startY) {
        controller.createFillField(startX, startY);
    }

    public void openCell(int x, int y) {
        controller.openCell(x, y);
    }

    public PlaySounds getSoundTrack() {
        return soundTrack;
    }

    public PlaySounds getSoundPressed() {
        return soundPressed;
    }

    public void loadTextures() {
        try {
            // Загрузка текстуры флага (положите файл в resources)
            flagTexture = ImageIO.read(Objects.requireNonNull(getClass().getResource("/textures/flag.png")));
            bombTexture = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/textures/bomb.png")));
            recordTexture = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/textures/record.png")));
            greenColorTexture = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/textures/green_color.png")));
            blueColorTexture = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/textures/blue_color.png")));
            yellowColorTexture = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/textures/yellow_color.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getRecordTexture() {
        return recordTexture;
    }

    public BufferedImage getGreenColorTexture() {
        return greenColorTexture;
    }

    public BufferedImage getBlueColorTexture() {
        return blueColorTexture;
    }

    public BufferedImage getYellowColorTexture() {
        return yellowColorTexture;
    }

    public void terminateGame() {
        gameProcess = new GameProcess(this);
        stopTimer();
    }

    public void stopTimer() {
        controller.GameOver();
    }

    public BufferedImage getFlagTexture() {
        return flagTexture;
    }
    public BufferedImage getBombTexture() {
        return bombTexture;
    }

    public void gameWin(int time) {
        controller.sendRecord(time);
        stopTimer();
    }

    public void loadRecords() {
        if (!recordMenu.isRecordsCashed()) {
            controller.updateRecords();
        }
    }

    @Override
    public void notify(Notifications notification, DataTransferObject data) {
        System.out.println(notification);
        switch (notification) {
            //запуск приложения
            case GAME_EXECUTED:
                loadWindow();
                break;
            //запуск игры
            case GAME_STARTED:
                gameProcess.createEmptyField(data);
                break;
            //открытие клеток
            case GAME_CHANGED:
                gameProcess.changeField(data);
                break;
            //взрыв
            case GAME_RUINED:
                gameProcess.showBombs(data);
                gameProcess.gameLose();
                break;
            //победа
            case GAME_WINED:
                gameProcess.changeField(data);
                gameProcess.showBombs(data);
                gameProcess.gameWin();
                break;
            case TIME_UPDATED:
                gameProcess.setTime(data.getTime());
                break;
            case RECORDS_UPDATED:
                recordMenu.updateRecords(data.getRecords());
        }
    }

}
