package client.view.screens;

import client.view.ChatView;
import client.view.Colors;
import client.view.components.buttons.ButtonCreator;
import client.view.components.buttons.ButtonSimple;
import client.view.components.labels.LabelCreator;
import client.view.components.textfields.TextFieldCreator;
import client.view.components.textfields.TextFieldSimple;
import logger.ChatLogger;
import protocolparser.ClientCommand;
import protocolparser.Serial.command.Command;

import javax.swing.*;
import java.awt.*;


public class ScreenConnection extends Screen {
    ChatView chatView;

    // Размеры и отступы
    final int SPACE_THICKNESS_BETWEEN_BUTTONS = 20;
    final int MIN_BUTTON_WIDTH = 100;
    final int MIN_BUTTON_HEIGHT = 30;
    final int MIN_LABEL_WIDTH = 150;
    final int MIN_LABEL_HEIGHT = 30;
    final int MIN_TEXT_FIELD_WIDTH = 200;
    final int MIN_TEXT_FIELD_HEIGHT = 60;

    final int ADAPTIVE_SCALE_BUTTON_X = 3;
    final int ADAPTIVE_SCALE_BUTTON_Y = 10;

    final int ADAPTIVE_SCALE_LABEL_X = 3;
    final int ADAPTIVE_SCALE_LABEL_Y = 5;

    final int ADAPTIVE_SCALE_TEXT_FIELD_X = 3;
    final int ADAPTIVE_SCALE_TEXT_FIELD_Y = 15;

    boolean isScreenSwap = false;

    //создатели стандартных компонентов
    ButtonCreator buttonCreator;
    LabelCreator labelCreator;
    TextFieldCreator textFieldCreator;

    TextFieldSimple textFieldIP;
    TextFieldSimple textFieldPort;
    TextFieldSimple textFieldName;
    public ScreenConnection(ChatView chatView) {
        super();
        this.chatView = chatView;

        this.buttonCreator = new ButtonCreator(
                chatView,
                MIN_BUTTON_WIDTH,
                MIN_BUTTON_HEIGHT
        );
        this.labelCreator = new LabelCreator(
                chatView,
                MIN_LABEL_WIDTH,
                MIN_LABEL_HEIGHT
        );
        this.textFieldCreator = new TextFieldCreator(
                chatView,
                MIN_TEXT_FIELD_WIDTH,
                MIN_TEXT_FIELD_HEIGHT
        );

        createCenterPanel();

        this.validate();
        this.repaint();
    }


    public void connect() {
        try {
            Command command = new Command.Builder()
                    .setCommandName(ClientCommand.LOGIN)
                    .setName(textFieldName.getText())
                    .setType("test")
                    .build();
            chatView.setupConnection(textFieldIP.getText(), Integer.parseInt(textFieldPort.getText()), command);
        }
        catch (NumberFormatException e) {
            ChatLogger.logger.error("Could not parse port number");
        }

    }


    private JPanel createButtons() {
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setOpaque(false);
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));

        // Создание кнопок
        ButtonSimple connectButton = buttonCreator.createButtonSimple("connect", this::connect, this, ADAPTIVE_SCALE_BUTTON_X, ADAPTIVE_SCALE_BUTTON_Y);
        connectButton.setBackground(Colors.getBackgroundColor());
        // Кнопка получает фокус вместо текстового поля
        SwingUtilities.invokeLater(connectButton::requestFocusInWindow);

        // Добавление кнопок с отступами
        buttonsPanel.add(Box.createVerticalStrut(SPACE_THICKNESS_BETWEEN_BUTTONS));
        buttonsPanel.add(connectButton);
        buttonsPanel.add(Box.createVerticalStrut(SPACE_THICKNESS_BETWEEN_BUTTONS));

        centerPanel.add(buttonsPanel);
        centerPanel.setBackground(Colors.getBackgroundColor());
        return centerPanel;
    }


    private JPanel createNameLabel() {
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JLabel nameLabel = labelCreator.createLabelSimple("Connect to chat", this, ADAPTIVE_SCALE_LABEL_X, ADAPTIVE_SCALE_LABEL_Y);
        centerPanel.add(nameLabel);

        return centerPanel;
    }

    private JPanel createTextFieldIpPort() {
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JPanel textFieldPanel = new JPanel();
        textFieldPanel.setLayout(new BoxLayout(textFieldPanel, BoxLayout.X_AXIS));
        textFieldIP = textFieldCreator.createTextFieldSimple("IP", this, ADAPTIVE_SCALE_TEXT_FIELD_X, ADAPTIVE_SCALE_TEXT_FIELD_Y);
        textFieldPanel.add(textFieldIP);
        textFieldPanel.add(Box.createHorizontalStrut(SPACE_THICKNESS_BETWEEN_BUTTONS));
        textFieldPort = textFieldCreator.createTextFieldSimple("Port", this, ADAPTIVE_SCALE_TEXT_FIELD_X, ADAPTIVE_SCALE_TEXT_FIELD_Y);
        textFieldPanel.add(textFieldPort);

        centerPanel.add(textFieldPanel);
        centerPanel.setBackground(Colors.getBackgroundColor());
        textFieldPanel.setBackground(Colors.getBackgroundColor());
        return centerPanel;
    }

    private JPanel createTextFieldUsername() {
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JPanel textFieldPanel = new JPanel();
        textFieldPanel.setLayout(new BoxLayout(textFieldPanel, BoxLayout.X_AXIS));
        textFieldName = textFieldCreator.createTextFieldSimple("Username", this, ADAPTIVE_SCALE_TEXT_FIELD_X, ADAPTIVE_SCALE_TEXT_FIELD_Y);
        textFieldPanel.add(textFieldName);

        centerPanel.add(textFieldPanel);

        centerPanel.setBackground(Colors.getBackgroundColor());
        textFieldPanel.setBackground(Colors.getMessageColor());
        return centerPanel;
    }


    private void createCenterPanel() {
        setBackground(Colors.getBackgroundColor());
        setLayout(new BorderLayout());

        JPanel labelPanel = createNameLabel();
        labelPanel.setBorder(BorderFactory.createEmptyBorder(SPACE_THICKNESS_BETWEEN_BUTTONS*2, 0, 0, 0));
        add(labelPanel, BorderLayout.NORTH);
        labelPanel.setBackground(Colors.getBackgroundColor());
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(createTextFieldUsername());
        center.add(Box.createVerticalStrut(SPACE_THICKNESS_BETWEEN_BUTTONS));
        center.add(createTextFieldIpPort());
        center.add(Box.createVerticalStrut(SPACE_THICKNESS_BETWEEN_BUTTONS));
        center.add(createButtons());
        center.add(Box.createVerticalStrut(SPACE_THICKNESS_BETWEEN_BUTTONS));

        center.setBackground(Colors.getBackgroundColor());

        add(center, BorderLayout.SOUTH);
    }
}