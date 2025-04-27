package ru.minesweeper.client.view.gui;

import ru.minesweeper.client.view.gui.animations.screens.AlertScreen;
import ru.minesweeper.server.model.Difficulties;
import ru.minesweeper.client.ClientController;
import ru.minesweeper.client.view.gui.screens.*;
import ru.minesweeper.server.model.entities.datatransfer.DataTransferObject;
import ru.minesweeper.observer.Notifications;
import ru.minesweeper.observer.Observer;
import ru.minesweeper.client.view.gui.animations.screens.SwapScreen;
import ru.minesweeper.client.view.gui.animations.screens.Sides;
import ru.minesweeper.client.view.gui.sounds.PlaySounds;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Objects;

public class GameView implements Observer {
    //temp
    //String host = "localhost";
    //int port = 5555;

    ClientController controller;
    JFrame frame = new JFrame();

    ScreenGameMenu screenGameMenu;
    ScreenSingleProcess screenSingleProcess;
    ScreenOptionsMenu optionsMenu;
    ScreenDifficultyMenu difficultyMenu;
    ScreenRecordMenu screenRecordMenu;
    ScreenMultiMenu multiMenu;
    ScreenLobbyMenu lobbyMenu;
    ScreenClientMenu clientMenu;
    ScreenMultiProcess screenMultiProcess;

    Screen currentScreen;

    SwapScreen swapScreen;
    AlertScreen alertScreen;

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
    Color colorButton = new Color(red-10, green+10, blue+10);
    Color colorSidePanel = new Color(red, green, blue+20);
    Color colorOpenedFieldButton = new Color(red+10, green+15, blue+30);
    Color colorBorder = new Color(red-10, green-10, blue+10);

    public GameView() throws IOException {
        //дебаг фича
        Toolkit.getDefaultToolkit().getSystemEventQueue().push(new TimedEventQueue());

        controller = new ClientController();
        controller.addObserver(this);

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
        alertScreen = new AlertScreen(5, Color.YELLOW, frame);

        //все экраны
        screenGameMenu = new ScreenGameMenu(this);
        screenSingleProcess = new ScreenSingleProcess(this);
        optionsMenu = new ScreenOptionsMenu(this);
        difficultyMenu = new ScreenDifficultyMenu(this);
        screenRecordMenu = new ScreenRecordMenu(this);
        multiMenu = new ScreenMultiMenu(this);
        lobbyMenu = new ScreenLobbyMenu(this);
        clientMenu = new ScreenClientMenu(this);
        screenMultiProcess = new ScreenMultiProcess(this);

        //фоновый режим
        safeStopScreen(screenSingleProcess);
        safeStopScreen(optionsMenu);
        safeStopScreen(difficultyMenu);
        safeStopScreen(screenRecordMenu);
        safeStopScreen(multiMenu);
        safeStopScreen(lobbyMenu);
        safeStopScreen(clientMenu);
        safeStopScreen(screenMultiProcess);

        currentScreen = screenGameMenu;

        //для анимаций
        swapScreen.addObserver(screenGameMenu);
        swapScreen.addObserver(optionsMenu);
        swapScreen.addObserver(difficultyMenu);
        swapScreen.addObserver(screenSingleProcess);
        swapScreen.addObserver(multiMenu);
        swapScreen.addObserver(screenRecordMenu);
        swapScreen.addObserver(lobbyMenu);
        swapScreen.addObserver(clientMenu);
        swapScreen.addObserver(screenMultiProcess);

        frame.setVisible(true);

        loadWindow();
    }

    public void setPalette(int red, int green, int blue) {
        this.red=red;
        this.green=green;
        this.blue=blue;
        colorBackground = new Color(red, green, blue);
        colorButton = new Color(red-10, green+10, blue+10);
        colorSidePanel = new Color(red, green, blue+20);
        colorOpenedFieldButton = new Color(red+10, green+15, blue+30);
        colorBorder = new Color(red-10, green-10, blue+10);
        //colorSideTurnPanel = new Color(red+20, green+20, blue+20);

        updateColors();

    }

    public void updateColors() {
        updateButtonsColors();
    }

    private void updateButtonsColors() {
        screenGameMenu.stopTimers();
        optionsMenu.stopTimers();
        difficultyMenu.stopTimers();
        multiMenu.stopTimers();
        screenRecordMenu.stopTimers();
        lobbyMenu.stopTimers();
        clientMenu.stopTimers();
        screenSingleProcess.stopTimers();
        screenMultiProcess.stopTimers();

        swapScreenToAnotherScreen(optionsMenu, ScreensE.SCREEN_OPTIONS_MENU, Sides.RIGHT);
    }

    public Color getColorBorder() {
        return colorBorder;
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

    public void safeStopScreen(Screen screen) {
        screen.stopTimers();
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
        frame.setContentPane(screenGameMenu);
        frame.revalidate();
        frame.repaint();
    }

    public void swapScreenToAnotherScreen(Screen oldScreen, ScreensE newScreen, Sides side) {
        try {
            Field screenField = getClass().getDeclaredField(newScreen.getName());
            //screenField.setAccessible(true);

            Screen newScreenInstance = createNewScreenInstance(newScreen);

            screenField.set(this, newScreenInstance);
            currentScreen = newScreenInstance;
            safeStopScreen(oldScreen);

            swapScreen.animateScreenSwap(oldScreen, currentScreen, side, frame);
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }

    private Screen createNewScreenInstance(ScreensE screenType) {
        switch (screenType) {
            case SCREEN_GAME_MENU:
                return new ScreenGameMenu(this);
            case SCREEN_SINGLE_PROCESS:
                return new ScreenSingleProcess(this);
            case SCREEN_OPTIONS_MENU:
                return new ScreenOptionsMenu(this);
            case SCREEN_DIFFICULTY_MENU:
                return new ScreenDifficultyMenu(this);
            case SCREEN_RECORD_MENU:
                return new ScreenRecordMenu(this);
            case SCREEN_MULTI_MENU:
                return new ScreenMultiMenu(this);
            case SCREEN_LOBBY_MENU:
                return new ScreenLobbyMenu(this);
            case SCREEN_CLIENT_MENU:
                return new ScreenClientMenu(this);
            case SCREEN_MULTI_PROCESS:
                return new ScreenMultiProcess(this);
            default:
                throw new IllegalArgumentException("Unknown screen type: " + screenType);
        }
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
        screenSingleProcess = new ScreenSingleProcess(this);
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

    public void gameWin() {
        stopTimer();
    }

    public void loadRecords() {
        if (!screenRecordMenu.isRecordsCashed()) {
            controller.updateRecords();
        }
    }

    public void singlePlayer() {
        controller.madeHost("localhost", 5555);
    }

    public void  multiPlayer() {
        controller.madeHost("localhost", 5556);
        controller.connectToLobby(multiMenu.getNickname());
    }

    public void stopHost() {
        controller.stopHost();
    }

    public void connect(String host, int port) {
        controller.executeConnection(host, port);
        controller.connectToLobby(multiMenu.getNickname());
    }

    public void disconnect() {
        screenMultiProcess = new ScreenMultiProcess(this);
        controller.disconnectFromLobby(multiMenu.getNickname());
        controller.disconnect();
    }

    @Override
    public void notify(Notifications notification, DataTransferObject data) {
        switch (notification) {
            //запуск приложения
            case HOST_STARTED:
                if (currentScreen == multiMenu) {
                    swapScreenToAnotherScreen(multiMenu, ScreensE.SCREEN_LOBBY_MENU, Sides.UP);
                }
                if (currentScreen == screenGameMenu) {
                    swapScreenToAnotherScreen(screenGameMenu, ScreensE.SCREEN_DIFFICULTY_MENU, Sides.UP);
                }
                break;
            case LOBBY_NICKNAMES_UPDATED:
                lobbyMenu.updateNicknames(data.getPlayerNicknamesOnServer());
                break;
            case HOST_DISCONNECTED:
                alertScreen.stopAlert();
                if (screenMultiProcess.getGlassPanel().isVisible()) {
                    screenMultiProcess.hideGameOver();
                }
                swapScreenToAnotherScreen(screenMultiProcess, ScreensE.SCREEN_GAME_MENU, Sides.DOWN);
                break;
            //запуск игры
            case GAME_STARTED:
                if (currentScreen == lobbyMenu) {
                    swapScreenToAnotherScreen(currentScreen, ScreensE.SCREEN_MULTI_PROCESS, Sides.UP);
                    screenMultiProcess.createEmptyField(data);
                }
                else {
                    screenSingleProcess.createEmptyField(data);
                }
                break;
            //открытие клеток
            case GAME_CHANGED_I_HOST:
                if (currentScreen == screenSingleProcess) {
                    screenSingleProcess.changeField(data);
                }
                else if (currentScreen == screenMultiProcess) {
                    screenMultiProcess.changeField(data);
                    System.out.println(currentScreen);
                    if (data.getTurn()) {
                        if (!alertScreen.isAlert()) {
                            alertScreen.createAlert(screenMultiProcess, colorBackground);
                        }
                    }
                    else {
                        alertScreen.stopAlert();
                    }
                }
                break;
            case GAME_CHANGED_I_CLIENT:
                if (currentScreen == screenMultiProcess) {
                    screenMultiProcess.changeField(data);
                    if (!data.getTurn()) {
                        if (!alertScreen.isAlert()) {
                            alertScreen.createAlert(screenMultiProcess, colorBackground);
                        }
                    }
                    else {
                        alertScreen.stopAlert();
                    }
                }
                break;
            //взрыв
            case GAME_RUINED_I_HOST:
                if (currentScreen == screenSingleProcess) {
                    screenSingleProcess.showBombs(data);
                    screenSingleProcess.gameLose();
                }
                else if (currentScreen == screenMultiProcess) {
                    screenMultiProcess.showBombs(data);
                    if (data.getTurn()) {
                        screenMultiProcess.gameWin(data.getWinner());
                    }
                    else {
                        screenMultiProcess.gameLose(data.getWinner());
                    }
                }
                alertScreen.stopAlert();
                break;
            case GAME_RUINED_I_CLIENT:
                screenMultiProcess.showBombs(data);
                if (data.getTurn()) {
                    screenMultiProcess.gameLose(data.getWinner());
                }
                else {
                    screenMultiProcess.gameWin(data.getWinner());
                }
                alertScreen.stopAlert();
                break;
            //победа
            case GAME_WINED_I_HOST:
                if (currentScreen == screenSingleProcess) {
                    screenSingleProcess.changeField(data);
                    screenSingleProcess.showBombs(data);
                }
                else if (currentScreen == screenMultiProcess) {
                    screenMultiProcess.changeField(data);
                    screenMultiProcess.showBombs(data);
                    if (data.getTurn()) {
                        screenMultiProcess.gameLose(data.getWinner());
                    }
                    else {
                        screenMultiProcess.gameWin(data.getWinner());
                    }
                }
                alertScreen.stopAlert();
                break;
            case GAME_WINED_I_CLIENT:
                screenMultiProcess.changeField(data);
                screenMultiProcess.showBombs(data);
                if (data.getTurn()) {
                    screenMultiProcess.gameWin(data.getWinner());
                }
                else {
                    screenMultiProcess.gameLose(data.getWinner());
                }
                alertScreen.stopAlert();
                break;
            case TIME_UPDATED:
                if (currentScreen == screenSingleProcess) {
                    screenSingleProcess.setTime(data.getTimePlayer1());
                }
                else if (currentScreen == screenMultiProcess) {
                    screenMultiProcess.setTimePlayer1(data.getTimePlayer1());
                    screenMultiProcess.setTimePlayer2(data.getTimePlayer2());
                }
                break;
            case RECORDS_UPDATED:
                screenRecordMenu.updateRecords(data.getRecords());
                break;
        }
    }


}
