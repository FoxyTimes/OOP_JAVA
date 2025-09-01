package ru.factory.view.gui;

import com.formdev.flatlaf.FlatDarkLaf;
import ru.factory.controller.FactoryController;
import ru.factory.model.Jobs;
import ru.factory.model.transfer.DataObject;
import ru.factory.observer.Notifications;
import ru.factory.observer.Observer;
import ru.factory.view.gui.components.CustomProgressBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class Screen implements Observer {
    Color backgroundColor = new Color(51, 63, 88);
    Color foregroundProgressColor = new Color(74, 122, 150);
    Color backgroundProgressColor = new Color(251, 187, 173);
    Color textColor = new Color(130, 130, 130);

    FactoryController controller;

    JFrame frame = new JFrame();

    ArrayList<CustomProgressBar> listOfProgressBars = new ArrayList<>();

    JPanel mainPanel;
    JLabel totalCars;
    JLabel toProduce;
    public Screen(FactoryController controller) {
        this.controller = controller;
        loadWindow();
    }

    public void loadWindow() {
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                shutDown();
                frame.dispose();
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        putComponents();

        frame.setVisible(true);
    }

    public void shutDown() {
        controller.shutDown();
    }

    public void putComponents() {
        try {
            FlatDarkLaf.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(backgroundColor);

        JPanel progressPanel = new JPanel();
        progressPanel.setOpaque(false);
        progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));
        //прогресс бар для кузовов
        progressPanel.add(createProgressPanel("Bodies"));
        //прогресс бар для моторов
        progressPanel.add(createProgressPanel("Motors"));
        //прогресс бар для аксессуаров
        progressPanel.add(createProgressPanel("Accessory"));
        //машины
        progressPanel.add(createProgressPanel("Cars"));
        mainPanel.add(progressPanel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.X_AXIS));
        infoPanel.setOpaque(false);
        totalCars = new JLabel("Total Cars: 0");
        infoPanel.add(totalCars);

        mainPanel.add(infoPanel, BorderLayout.SOUTH);
        toProduce = new JLabel("To Produce: 0");

        infoPanel.add(toProduce);

        frame.setContentPane(mainPanel);
    }

    public void putSliders(ArrayList<Integer> countOfSuppliers, ArrayList<Integer> timesToCreation) {
        JPanel slidersPanel = new JPanel();
        slidersPanel.setOpaque(false);
        slidersPanel.setLayout(new BoxLayout(slidersPanel, BoxLayout.X_AXIS));

        slidersPanel.add(createScrollPanel("BodySupplier", countOfSuppliers.get(Jobs.BODY.getValue()), Jobs.BODY, timesToCreation.get(Jobs.BODY.getValue())));
        slidersPanel.add(createScrollPanel("MotorSupplier", countOfSuppliers.get(Jobs.MOTORS.getValue()), Jobs.MOTORS, timesToCreation.get(Jobs.MOTORS.getValue())));
        slidersPanel.add(createScrollPanel("AccessorySupplier", countOfSuppliers.get(Jobs.ACCESSORY.getValue()), Jobs.ACCESSORY, timesToCreation.get(Jobs.ACCESSORY.getValue())));
        slidersPanel.add(createScrollPanel("Worker", countOfSuppliers.get(Jobs.WORKER.getValue()), Jobs.WORKER, timesToCreation.get(Jobs.WORKER.getValue())));
        slidersPanel.add(createScrollPanel("Dealer", countOfSuppliers.get(Jobs.DEALER.getValue()), Jobs.DEALER, timesToCreation.get(Jobs.DEALER.getValue())));

        mainPanel.add(slidersPanel, BorderLayout.CENTER);
    }

    public JScrollPane createScrollPanel(String title, int numberOfSliders, Jobs job, int timeToCreation) {
        JPanel sliders = new JPanel();
        sliders.setOpaque(false);
        sliders.setLayout(new BoxLayout(sliders, BoxLayout.Y_AXIS));
        for (int i = 0; i < numberOfSliders; i++) {
            sliders.add(createSlider(title + " " + i, 0, 10000, i, job, timeToCreation));
        }

        JScrollPane scrollPane = new JScrollPane(sliders);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


        return scrollPane;
    }

    public JPanel createSlider(String title, int min, int max, int id, Jobs job, int timeToCreation) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setOpaque(false);
        // Добавляем заголовок
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(textColor);
        panel.add(titleLabel);

        // Создаем и добавляем слайдер
        JSlider slider = new JSlider(min, max);
        slider.setOpaque(false);
        slider.setValue(timeToCreation);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setMinimum(1);
        panel.add(slider);

        // Создаем label для отображения текущего значения
        JLabel valueLabel = new JLabel(String.valueOf(slider.getValue()));
        valueLabel.setForeground(textColor);
        valueLabel.setPreferredSize(new Dimension(40, 20)); // Фиксированная ширина

        // Добавляем слушатель изменений значения слайдера
        slider.addChangeListener(e -> {
            valueLabel.setText(String.valueOf(slider.getValue()));
            valueWasChanged(id, slider.getValue(), job);
        });

        panel.add(valueLabel);
        return panel;
    }

    public void valueWasChanged(int id, int value, Jobs job) {
        controller.valueWasChanged(id, value, job);
    }

    public JPanel createProgressPanel(String title) {
        JPanel barPanel = new JPanel();
        barPanel.setLayout(new BoxLayout(barPanel, BoxLayout.X_AXIS));
        CustomProgressBar progressBar = createProgressBar();
        listOfProgressBars.add(progressBar);
        JLabel progressLabel = createProgressLabel(title);
        barPanel.add(progressLabel);
        barPanel.add(progressBar);

        return barPanel;
    }

    public JLabel createProgressLabel(String text) {
        JLabel progressLabel = new JLabel(text);
        progressLabel.setForeground(textColor);
        return progressLabel;
    }

    public CustomProgressBar createProgressBar() {
        CustomProgressBar progressBar = new CustomProgressBar();

        progressBar.setStringPainted(true);
        progressBar.setForeground(foregroundProgressColor);
        progressBar.setBackground(backgroundProgressColor);
        progressBar.setTextColor(textColor);

        return progressBar;
    }


    public void updateProgress(ArrayList<Integer> fullnessOfStorage, ArrayList<Integer> maxSizeOfStorage) {
        if (fullnessOfStorage.size() < listOfProgressBars.size()) {
            throw new RuntimeException("to low storages");
        }

        for (int i=0; i<listOfProgressBars.size(); i++) {
            listOfProgressBars.get(i).setValue(fullnessOfStorage.get(i));
            listOfProgressBars.get(i).setString(fullnessOfStorage.get(i) + "/" + maxSizeOfStorage.get(i));
            listOfProgressBars.get(i).setMaximum(maxSizeOfStorage.get(i));
        }
    }


    @Override
    synchronized public void sendNotify(Notifications notification, DataObject data) {
        switch (notification) {
            case PROGRESS_UPDATED: {
                SwingUtilities.invokeLater(() -> {
                    updateProgress(data.getFullnessOfStorage(), data.getMaxSizeOfStorage());
                    totalCars.setText("Total cars: " + String.valueOf(data.getTotalCars()) + " ");
                });
                break;
            }
            case LOAD_INFO: {
                SwingUtilities.invokeLater(() -> {
                    putSliders(data.getCountOfDifferentJobs(), data.getTimesToCreation());
                });
                break;
            }
            case PRODUCE_CAR_UPDATED: {
                SwingUtilities.invokeLater(() -> {
                    toProduce.setText("To Produce: " + data.getToProduce());
                });
                break;
            }
        }
    }
}
