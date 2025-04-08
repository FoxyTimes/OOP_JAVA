package ru.minesweeper.view.gui.screens;

import ru.minesweeper.model.entities.datatransfer.DataTransferObject;
import ru.minesweeper.observer.Notifications;
import ru.minesweeper.observer.Observer;
import ru.minesweeper.view.gui.GameView;
import ru.minesweeper.view.gui.animations.screens.Sides;
import ru.minesweeper.view.gui.animations.text.TextAnim;
import ru.minesweeper.view.gui.components.buttons.MenuButtonCreator;
import ru.minesweeper.view.gui.components.buttons.VisualMenuButtonTextable;
import ru.minesweeper.view.gui.components.labels.LabelSimple;
import ru.minesweeper.view.gui.components.labels.LabelTextable;
import ru.minesweeper.view.gui.components.labels.MenuLabelCreator;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.IOException;
import java.util.List;

import static ru.minesweeper.view.gui.animations.buttons.AnimToButton.offMoused;

public class RecordMenu extends JPanel implements Observer {
    GameView gameView;

    // Размеры и отступы
    final int SPACE_THICKNESS_BETWEEN_BUTTONS = 10;
    final int MIN_BUTTON_WIDTH = 100;
    final int MIN_BUTTON_HEIGHT = 30;
    final int MIN_LABEL_WIDTH = 200;
    final int MIN_LABEL_HEIGHT = 60;

    final int ADAPTIVE_SCALE_MENU_BUTTON_X = 3;
    final int ADAPTIVE_SCALE_MENU_BUTTON_Y = 10;

    final int ADAPTIVE_SCALE_RECORD_BUTTON_X = 12;
    final int ADAPTIVE_SCALE_RECORD_BUTTON_Y = 7;

    final int ADAPTIVE_SCALE_MENU_LABEL_X = 3;
    final int ADAPTIVE_SCALE_MENU_LABEL_Y = 5;

    boolean isRecords=false;
    boolean isScreenSwap=false;

    MenuButtonCreator menuButtonCreator;
    MenuLabelCreator menuLabelCreator;


    JPanel recordsTable;
    LabelTextable mainName;
    public RecordMenu(GameView gameView) {
        this.gameView=gameView;
        this.menuButtonCreator = new MenuButtonCreator(
                gameView,
                MIN_BUTTON_WIDTH,
                MIN_BUTTON_HEIGHT
        );
        this.menuLabelCreator = new MenuLabelCreator(
              gameView,
              MIN_LABEL_WIDTH,
              MIN_LABEL_HEIGHT
        );
        putMainMenu();
    }

    public void stopTimers() {
        stopMainNameTimer();
    }

    public void stopMainNameTimer() {
        mainName.stopColorChange();
    }

    public JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        mainName = menuLabelCreator.createLabelText("Records", this, ADAPTIVE_SCALE_MENU_LABEL_X, ADAPTIVE_SCALE_MENU_LABEL_Y);
        titlePanel.add(Box.createHorizontalGlue());
        titlePanel.add(mainName);
        titlePanel.add(Box.createHorizontalGlue());
        titlePanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 60, 0));
        return titlePanel;
    }

    public JPanel createRecordsTable(Map<String, List<Integer>> records) {
        recordsTable = new JPanel();
        recordsTable.setOpaque(false);
        recordsTable.setLayout(new BorderLayout());

        if (records.isEmpty()) {
            JPanel centerPanel = new JPanel(new GridBagLayout());
            centerPanel.setOpaque(false);
            LabelSimple noRecordsLabel = menuLabelCreator.createLabelSimple("No records yet", this, ADAPTIVE_SCALE_MENU_LABEL_X, ADAPTIVE_SCALE_MENU_LABEL_Y);
            noRecordsLabel.setFont(gameView.getMainFont().deriveFont(Font.PLAIN, 18));
            noRecordsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            centerPanel.add(noRecordsLabel);
            recordsTable.add(centerPanel, BorderLayout.CENTER);
            return recordsTable;
        }
        // Главная панель для колонок
        JPanel columnsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        columnsPanel.setOpaque(false);
        columnsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Создаем колонки для каждой сложности
        createDifficultyColumn(columnsPanel, "EASY", records.get("EASY"));
        createDifficultyColumn(columnsPanel, "NORMAL", records.get("NORMAL"));
        createDifficultyColumn(columnsPanel, "HARD", records.get("HARD"));


        recordsTable.add(columnsPanel, BorderLayout.CENTER);

        return recordsTable;
    }

    private void createDifficultyColumn(JPanel parentPanel, String difficulty, List<Integer> times) {
        JPanel columnPanel = new JPanel();
        columnPanel.setLayout(new BoxLayout(columnPanel, BoxLayout.Y_AXIS));
        columnPanel.setOpaque(false);
        columnPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        // Заголовок колонки
        JLabel difficultyLabel = new JLabel(difficulty);
        difficultyLabel.setFont(gameView.getMainFont().deriveFont(Font.PLAIN, 18));
        difficultyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        difficultyLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        columnPanel.add(difficultyLabel);

        // Если для этой сложности нет рекордов
        if (times == null || times.isEmpty()) {
            JLabel noRecordsLabel = new JLabel("No records");
            noRecordsLabel.setFont(gameView.getMainFont().deriveFont(Font.PLAIN, 18));
            noRecordsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            columnPanel.add(noRecordsLabel);
        } else {
            // Добавляем каждое время
            int position = 1;
            for (Integer time : times) {
                JLabel recordLabel = new JLabel(position + ". " + formatTime(time));
                recordLabel.setFont(gameView.getMainFont().deriveFont(Font.PLAIN, 18));
                recordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                recordLabel.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
                columnPanel.add(recordLabel);

                // Ограничиваем количество отображаемых рекордов (например, топ-10)
                if (position >= 30) break;
                position++;
            }
        }

        parentPanel.add(columnPanel);
    }

    // Вспомогательный метод для форматирования времени в MM:SS
    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    public JPanel createBackButtonPanel() {
        JPanel backButtonPanel = new JPanel();
        backButtonPanel.setLayout(new BoxLayout(backButtonPanel, BoxLayout.X_AXIS));
        backButtonPanel.setOpaque(false);

        VisualMenuButtonTextable backButton = menuButtonCreator.createButtonText("Back", this::backToMenu, this, ADAPTIVE_SCALE_MENU_BUTTON_X, ADAPTIVE_SCALE_MENU_BUTTON_Y);
        backButtonPanel.add(Box.createHorizontalGlue());
        backButtonPanel.add(backButton);
        backButtonPanel.add(Box.createHorizontalGlue());
        backButtonPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 30, 0));

        return backButtonPanel;
    }


    private void backToMenu(TextAnim<VisualMenuButtonTextable> anim, VisualMenuButtonTextable button) {
        offMoused(anim, button);
        if (!isScreenSwap) {
            gameView.screenSwapToGameMenu(this, Sides.UP);
        }
    }

    public void putMainMenu() {
        setLayout(new BorderLayout());
        setBackground(gameView.getColorBackground());

        // 1. Заголовок (верх)
        add(createTitlePanel(), BorderLayout.NORTH);

        // 2. Кнопка "Назад" (низ)
        add(createBackButtonPanel(), BorderLayout.SOUTH);
    }


    @Override
    public void notify(Notifications notification, DataTransferObject data) {
        switch (notification) {
            case SWAP_STARTED:
                isScreenSwap = true;
                break;
            case SWAP_SUCCESS:
                isScreenSwap = false;
                break;
        }
    }

    public void updateRecords(Map<String, List<Integer>> records) {
        // Удаляем старую таблицу
        if (recordsTable != null) {
            remove(recordsTable);
        }
        // Создаем новую
        recordsTable = createRecordsTable(records);
        // Добавляем обновленную таблицу
        add(recordsTable, BorderLayout.CENTER);
        isRecords=true;
    }

    public boolean isRecordsCashed() {
        return isRecords;
    }

}
