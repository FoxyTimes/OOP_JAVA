package client.view.screens;

import client.view.ChatView;
import client.view.Colors;
import client.view.components.buttons.ButtonCreator;
import client.view.components.buttons.ButtonSimple;
import client.view.components.labels.LabelCreator;
import client.view.components.labels.LabelSimple;
import client.view.components.panels.MessagePanel;
import client.view.components.textfields.TextFieldCreator;
import client.view.components.textfields.TextFieldSimple;
import logger.ChatLogger;
import protocolparser.ClientCommand;
import protocolparser.Serial.command.Command;
import server.UserInfo;
import server.UsersContext;
import server.model.ChatContext;
import server.model.ChatMessage;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class ScreenChat extends Screen {
    ChatView chatView;

    // Размеры и отступы
    final int SPACE_THICKNESS_BETWEEN_BUTTONS = 20;
    final int MIN_BUTTON_WIDTH = 100;
    final int MIN_BUTTON_HEIGHT = 30;
    final int MIN_LABEL_WIDTH = 150;
    final int MIN_LABEL_HEIGHT = 30;
    final int MIN_TEXT_FIELD_WIDTH = 200;
    final int MIN_TEXT_FIELD_HEIGHT = 60;

    final int MAX_TEXT_FIELD_WIDTH = 200;
    final int MAX_TEXT_FIELD_HEIGHT = 60;
    final int MAX_BUTTON_WIDTH = 250;
    final int MAX_BUTTON_HEIGHT = 60;

    final int ADAPTIVE_SCALE_BUTTON_X = 3;
    final int ADAPTIVE_SCALE_BUTTON_Y = 10;

    final int ADAPTIVE_SCALE_LABEL_X = 3;
    final int ADAPTIVE_SCALE_LABEL_Y = 5;

    final int ADAPTIVE_SCALE_TEXT_FIELD_X = 3;
    final int ADAPTIVE_SCALE_TEXT_FIELD_Y = 15;

    String[] colors = {
            "#e6194b", // красный
            "#3cb44b", // зелёный
            "#ffe119", // жёлтый
            "#4363d8", // синий
            "#f58231", // оранжевый
            "#911eb4", // фиолетовый
            "#46f0f0", // бирюзовый
            "#f032e6", // розовый
            "#bcf60c", // салатовый
            "#fabebe"  // светло-розовый
    };

    //создатели стандартных компонентов
    ButtonCreator buttonCreator;
    LabelCreator labelCreator;
    TextFieldCreator textFieldCreator;

    ButtonSimple menuButton;

    JPanel chatPanel;
    JScrollPane scrollPane;
    TextFieldSimple textFieldMessage;

    HashMap<Integer, MessagePanel> messagePanels = new HashMap<>();

    int minMessageId=Integer.MAX_VALUE;

    public ScreenChat(ChatView chatView) {
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



    private JPanel createTextField() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        textFieldMessage = textFieldCreator.createTextFieldSimple("Message", this, ADAPTIVE_SCALE_TEXT_FIELD_X, ADAPTIVE_SCALE_TEXT_FIELD_Y);
        textFieldMessage.addActionListener(e -> send());
        textFieldMessage.setMaximumSize(new Dimension(Integer.MAX_VALUE, MAX_TEXT_FIELD_HEIGHT));

        centerPanel.add(Box.createVerticalStrut(SPACE_THICKNESS_BETWEEN_BUTTONS/4));
        centerPanel.add(textFieldMessage);
        centerPanel.add(Box.createVerticalStrut(SPACE_THICKNESS_BETWEEN_BUTTONS/4));

        centerPanel.setBackground(Colors.getBackgroundColor());
        return centerPanel;
    }

    private JPanel createButtons() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // Создание кнопок
        ButtonSimple sendButton = buttonCreator.createButtonSimple(">", this::send, this, ADAPTIVE_SCALE_BUTTON_X, ADAPTIVE_SCALE_BUTTON_Y);

        sendButton.setMaximumSize(new Dimension(MAX_BUTTON_WIDTH, MAX_BUTTON_HEIGHT));

        // Добавление кнопок с отступами
        centerPanel.add(Box.createVerticalStrut(SPACE_THICKNESS_BETWEEN_BUTTONS/4));
        centerPanel.add(sendButton);
        centerPanel.add(Box.createVerticalStrut(SPACE_THICKNESS_BETWEEN_BUTTONS/4));

        centerPanel.setMaximumSize(new Dimension(sendButton.getPreferredSize().width, sendButton.getPreferredSize().height));
        centerPanel.setPreferredSize(new Dimension(sendButton.getPreferredSize().width, sendButton.getPreferredSize().height));
        sendButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        sendButton.setBackground(Colors.getBackgroundColor());
        centerPanel.setBackground(Colors.getBackgroundColor());
        return centerPanel;
    }

    private JScrollPane createChatPanel() {
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(chatPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setBackground(Colors.getBackgroundColor());

        scrollPane.getVerticalScrollBar().addAdjustmentListener(e -> {
            JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
            int extent = scrollBar.getModel().getExtent();
            int maximum = scrollBar.getMaximum();
            int value = scrollBar.getValue();

            if (!e.getValueIsAdjusting() && value + extent >= maximum) {
                requestMessages();
            }
        });

        chatPanel.add(Box.createVerticalStrut(SPACE_THICKNESS_BETWEEN_BUTTONS));
        scrollPane.setBackground(Colors.getBackgroundColor());
        chatPanel.setBackground(Colors.getBackgroundColor());
        return scrollPane;
    }

    private MessagePanel createLabelPanel(ChatMessage chatMessage, int id) {
        if (id < minMessageId) {
            minMessageId = id;
        }


        MessagePanel panel = new MessagePanel(id);
        if (chatMessage.getSender().getClientID()==-1) {
            panel.setBackground(Colors.getSystemMessageColor());
        }
        else {
            panel.setBackground(Colors.getMessageColor());
        }
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy"); // формат, например "часы:минуты:секунды день.месяц.год"
        String formattedDate = formatter.format(chatMessage.getDate());

        // Время и имя отправителя
        long seed = Math.abs(chatMessage.getSender().getClientID());
        String color = colors[(int) (seed % colors.length)];

        String name = chatMessage.getSender().getName();
        String coloredName = "<font color='" + color + "'>" + name + "</font>";

        JLabel metaLabel = new JLabel("<html>[" + formattedDate + "] " + coloredName + ":</html>");
        metaLabel.setFont(chatView.getMainFont().deriveFont(Font.BOLD, 10));
        metaLabel.setForeground(Color.DARK_GRAY);
        metaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Текст сообщения
        JTextArea messageArea = getMessageArea(chatMessage.getMessageText());
        messageArea.setAlignmentX(Component.LEFT_ALIGNMENT);


        panel.add(metaLabel);
        panel.add(Box.createVerticalStrut(SPACE_THICKNESS_BETWEEN_BUTTONS));
        panel.add(messageArea);

        // Установка ширины панели, высота автоматически подстраивается под содержимое
        panel.setMinimumSize(new Dimension(400, messageArea.getPreferredSize().height+metaLabel.getPreferredSize().height+SPACE_THICKNESS_BETWEEN_BUTTONS*2));
        panel.setMaximumSize(new Dimension(400, messageArea.getPreferredSize().height+metaLabel.getPreferredSize().height+SPACE_THICKNESS_BETWEEN_BUTTONS*2));
        return panel;
    }

    private JPanel createTopButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        menuButton = buttonCreator.createButtonSimple("\u2630", this::openMenu, this, ADAPTIVE_SCALE_BUTTON_X, ADAPTIVE_SCALE_BUTTON_Y);
        menuButton.setMaximumSize(new Dimension(MAX_BUTTON_WIDTH/3, Integer.MAX_VALUE));
        panel.add(menuButton);
        panel.setBackground(Colors.getBackgroundColor());
        menuButton.setBackground(Colors.getBackgroundColor());
        return panel;
    }

    private void openMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBackground(Colors.getBackgroundColor());
        JMenuItem itemProfile = new JMenuItem("Профиль");
        //itemProfile.addActionListener(e -> showProfile());
        itemProfile.setPreferredSize(new Dimension(MAX_BUTTON_WIDTH, itemProfile.getPreferredSize().height));
        itemProfile.setFont(chatView.getMainFont().deriveFont(Font.BOLD, 10));
        JMenuItem itemSettings = new JMenuItem("Пользователи");
        itemSettings.addActionListener(e -> requestUsersList());
        itemSettings.setFont(chatView.getMainFont().deriveFont(Font.BOLD, 10));
        JMenuItem itemLogout = new JMenuItem("Выйти");
        itemLogout.addActionListener(e -> logout());
        itemLogout.setFont(chatView.getMainFont().deriveFont(Font.BOLD, 10));
        popupMenu.add(itemProfile);
        popupMenu.add(itemSettings);
        popupMenu.addSeparator();
        popupMenu.add(itemLogout);

        popupMenu.show(menuButton, 0, menuButton.getHeight());
    }

    private void logout() {
        chatView.logout();
    }


    private JTextArea getMessageArea(String messageText) {
        JTextArea messageArea = new JTextArea(messageText);
        messageArea.setFont(chatView.getMainFont().deriveFont(Font.PLAIN, 24));
        messageArea.setForeground(Color.BLACK);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setEditable(false);
        messageArea.setOpaque(false);
        messageArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));


        messageArea.setSize(new Dimension(380, Short.MAX_VALUE));
        Dimension preferredSize = messageArea.getPreferredSize();
        messageArea.setPreferredSize(new Dimension(380, preferredSize.height));
        return messageArea;
    }

    private void send() {
        Command command = new Command.Builder().setCommandName(ClientCommand.MESSAGE).setMessage(textFieldMessage.getText()).build();
        chatView.sendCommand(command);
        textFieldMessage.setText("");
    }

    private void requestUsersList() {
        chatView.requestUsersList();
    }


    public void createCenterPanel() {
        setBackground(Colors.getBackgroundColor());


        setLayout(new BorderLayout());

        add(createTopButtonPanel(), BorderLayout.NORTH);

        add(createChatPanel(), BorderLayout.CENTER);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.X_AXIS));
        center.add(Box.createHorizontalStrut(SPACE_THICKNESS_BETWEEN_BUTTONS/4));
        center.add(createTextField());
        center.add(Box.createHorizontalStrut(SPACE_THICKNESS_BETWEEN_BUTTONS/4));
        center.add(createButtons());
        center.add(Box.createHorizontalStrut(SPACE_THICKNESS_BETWEEN_BUTTONS/4));

        center.setBackground(Colors.getBackgroundColor());

        add(center, BorderLayout.SOUTH);
    }

    private void requestMessages() {
        chatView.requestMessages(minMessageId);
    }

    public void updateChat(ChatContext chatContext) {
        if (chatContext == null) return;

        ArrayList<ChatMessage> messages = new ArrayList<>(chatContext.getMessagesList());
        messages.sort(Comparator.comparingInt(ChatMessage::getId));

        for (ChatMessage message : messages) {
            int id = message.getId();


            if (messagePanels.containsKey(id)) {
                continue;
            }

            MessagePanel panel = createLabelPanel(message, id);

            messagePanels.put(id, panel);
            insertMessagePanelSorted(panel, id, chatView.getClientId()==message.getSender().getClientID());
        }

        chatPanel.revalidate();
        chatPanel.repaint();
        scrollPane.revalidate();
        scrollPane.repaint();
    }

    private void insertMessagePanelSorted(MessagePanel newPanel, int newId, boolean isMineMessage) {
        int insertIndex = 0;

        boolean found = false;

        for (int i = 0; i < chatPanel.getComponentCount(); i++) {
            Component wrapper = chatPanel.getComponent(i);
            if (wrapper instanceof JPanel) {
                for (int j=0; j!=((JPanel) wrapper).getComponentCount(); j++) {
                    Component comp = ((JPanel) wrapper).getComponent(j);
                    if (comp instanceof MessagePanel panel) {
                        int id = panel.getId();
                        if (newId > id) {
                            found=true;
                        }
                        break;
                    }
                }
            }
            if (found) {
                break;
            }
            insertIndex++;
        }

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.X_AXIS));

        if (isMineMessage) {
            wrapper.add(Box.createHorizontalGlue());
            wrapper.add(newPanel);
        }
        else {
            wrapper.add(newPanel);
            wrapper.add(Box.createHorizontalGlue());
        }
        wrapper.setBackground(Colors.getBackgroundColor());
        chatPanel.add(Box.createVerticalStrut(SPACE_THICKNESS_BETWEEN_BUTTONS), insertIndex);
        chatPanel.add(wrapper, insertIndex);
    }


    public JPanel createUserNamePanel(UserInfo userInfo) {
        JPanel userNamePanel = new JPanel();

        LabelSimple userNameLabel = labelCreator.createLabelSimple(userInfo.getName(), this, ADAPTIVE_SCALE_LABEL_X, ADAPTIVE_SCALE_LABEL_Y);

        userNamePanel.add(userNameLabel);
        userNameLabel.setBackground(Colors.getBackgroundColor());
        userNamePanel.setBackground(Colors.getBackgroundColor());
        return userNamePanel;
    }

    public void openRequestedUsersList(UsersContext usersContext) {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBackground(Colors.getBackgroundColor());
        LabelSimple popupNameLabel = labelCreator.createLabelSimple("list of users: ", this, ADAPTIVE_SCALE_LABEL_X, ADAPTIVE_SCALE_LABEL_Y);
        popupNameLabel.setBackground(Colors.getBackgroundColor());
        popupMenu.add(popupNameLabel);
        popupMenu.add(Box.createHorizontalStrut(SPACE_THICKNESS_BETWEEN_BUTTONS));

        for (UserInfo userInfo : usersContext.getClients().values()) {
            if (userInfo.getClientID()>=0) {
                popupMenu.add(Box.createHorizontalStrut(SPACE_THICKNESS_BETWEEN_BUTTONS));
                popupMenu.add(createUserNamePanel(userInfo));
            }
        }

        popupMenu.show(this, this.getWidth()/2-popupMenu.getPreferredSize().width/2, 0);
    }
}