package client.view;

import client.ClientController;
import client.view.screens.ScreenChat;
import client.view.screens.ScreenConnection;
import com.formdev.flatlaf.FlatDarkLaf;
import logger.ChatLogger;
import observer.Observer;
import protocolparser.ClientCommand;
import protocolparser.Serial.command.Command;
import protocolparser.Serial.response.Response;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class ChatView implements Observer {
    ClientController controller = new ClientController();

    JFrame frame = new JFrame();

    Font customFont = null;

    ScreenConnection screenConnection;
    ScreenChat screenChat;

    long clientId=-2;
    public ChatView() {
        controller.addObserver(this);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        loadFont("/fonts/PressStart2P-Regular.ttf");

        loadWindows();

        frame.setVisible(true);
    }


    private void loadWindows() {
        try {
            FlatDarkLaf.setup();
        }
        catch (Exception e) {
            ChatLogger.logger.error("Failed to setup FlatDarkLaf");
        }


        screenConnection = new ScreenConnection(this);
        screenChat = new ScreenChat(this);
        frame.setContentPane(screenConnection);
    }

    public JFrame getFrame() {
        return frame;
    }

    public void sendCommand(Command command) {
        controller.sendCommand(command);
    }

    public void setupConnection(String ip, int port, Command command) {
        controller.setupConnection(ip, port, command);
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



    public void swapToChat() {
        SwingUtilities.invokeLater(() -> {
            frame.setContentPane(screenChat);
            frame.revalidate();
            frame.repaint();
        });
    }

    public void swapToConnection() {
        frame.setContentPane(screenConnection);
        frame.revalidate();
        frame.repaint();
    }

    @Override
    public void sendNotify(Response response) {
        switch (response.getResponseName()) {
            case REGISTRATION:
                SwingUtilities.invokeLater(this::swapToChat);
                SwingUtilities.invokeLater(() -> {
                clientId = response.getClientID();});
                break;
            case CHAT, REQUESTED_CHAT:
                if (clientId!=-1) {
                    SwingUtilities.invokeLater(() -> {screenChat.updateChat(response.getEvent().getChatContext());});
                }
                break;
            case DISCONNECT:
                SwingUtilities.invokeLater(this::swapToConnection);
                break;
            case REQUESTED_LIST:
                SwingUtilities.invokeLater(() -> {screenChat.openRequestedUsersList(response.getEvent().getUsersContext());});
                break;
        }
    }

    public void logout() {
        controller.closeConnection();
        clientId = -2;
        screenChat = new ScreenChat(this);
    }

    public void requestMessages(int minMessageId) {
        controller.sendCommand(new Command.Builder()
                .setCommandName(ClientCommand.REQUEST_CHAT)
                .setMinMessageId(minMessageId)
                .build());
    }

    public void requestUsersList() {
        controller.sendCommand(new Command.Builder()
                .setCommandName(ClientCommand.LIST)
                .build());
    }

    public long getClientId() {
        return clientId;
    }
}
