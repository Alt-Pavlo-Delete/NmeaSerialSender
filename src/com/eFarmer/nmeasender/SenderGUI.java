package com.eFarmer.nmeasender;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SenderGUI extends JFrame{

    private JFrame frame = new JFrame();
    private JPanel MainFrame;
    private JButton pauseButton;
    private JButton startButton;
    private JTextField FilePathField;
    private JComboBox ComPortBox;
    private JComboBox FrequencyBox;
    private JComboBox BaudrateBox;
    private JComboBox SetParityBox;
    private JTextArea textArea;

    private SettingsContainer SettingsContainer = new SettingsContainer();

    public boolean PauseState = false;

    public SenderGUI() {
        frame.setTitle("Nmea to serial sender");
        frame.setSize(500, 600);
        frame.setContentPane(MainFrame);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        FilePathField.setEditable(true);
        FilePathField.setText("Enter path to NMEA log here");
        FilePathField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SettingsContainer.setNmeaPath(FilePathField.getText());
            }
        });

        pauseButton.setText("PAUSE");
        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (PauseState = false){
                    PauseState = true;
                    pauseButton.setText("CONTINUE");
                } else {
                    PauseState = false;
                    pauseButton.setText("PAUSE");
                }
            }
        });

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });

        ComPortBox = new JComboBox();


    }

}
