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
    private JComboBox COMPort;
    private JComboBox Frequency;
    private JComboBox Baudrate;
    private JComboBox SetParity;
    private JTextArea textArea1;

    public boolean PauseState = false;
    public String filePath;
    public int PortNumber = 8;
    public int BaudRate = 115200;
    public int DataBits = 8;
    public int StopBits = 1;
    public int Parity = 0;
    public int messageFrequency = 5; // 5, 10 Hz

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
        filePath = FilePathField.getText();
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


    }

}
