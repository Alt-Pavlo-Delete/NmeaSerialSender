package com.eFarmer.nmeasender;

import com.sun.tools.jconsole.JConsolePlugin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.PipedInputStream;

public class GuiClass extends JFrame {
    private SettingsContainer SettingsContainer = new SettingsContainer();
    private FileToComPocessor FileToComProcessor;
    private PipedInputStream pipedInputStream = new PipedInputStream();
    private JFrame mainFrame;
    private JPanel mainPanel;
    private JToolBar mainBar;
    private JComboBox comPortBox;
    private JComboBox frequencyBox;
    private JComboBox baudrateBox;
    private JComboBox parityBox;
    private JTextField filePathField;
    private JTextArea mainTextArea;
    private JButton mainButton;
    private ComPort ComPort;


    public GuiClass() throws IOException {

        ComPort = new ComPort();
        String[] PortList = ComPort.getPortsList().toArray(new String[0]);
        mainFrame = new JFrame();
        mainPanel = new JPanel();
        mainBar = new JToolBar(JToolBar.HORIZONTAL);
        comPortBox = new JComboBox(PortList);
        frequencyBox = new JComboBox(SettingsContainer.FreqList);
        baudrateBox = new JComboBox(SettingsContainer.BaudList);
        parityBox = new JComboBox(SettingsContainer.ParityList);
        filePathField = new JTextField();
        mainTextArea = new JTextArea();
        mainButton = new JButton("SEND");

        // --------------- Main Frame ---------------
        mainFrame.setTitle("Nmea to serial sender");
        mainFrame.setSize(500, 600);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setContentPane(mainPanel);
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.add(mainBar, BorderLayout.NORTH);
        mainFrame.add(filePathField, BorderLayout.CENTER);
        mainFrame.add(mainTextArea);
        mainFrame.add(mainButton,BorderLayout.SOUTH);

        mainPanel.setBackground(Color.white);
        mainPanel.setLayout(new BorderLayout());

        // --------------- Top Bar ---------------
        mainBar.setFloatable(false);
        mainBar.setSize(new Dimension(490,30));
        mainBar.add(comPortBox);
        mainBar.add(parityBox);
        mainBar.add(baudrateBox);
        mainBar.add(frequencyBox);
        mainBar.setVisible(true);

        comPortBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String FullPortName = String.valueOf(comPortBox.getSelectedItem());
                SettingsContainer.setPortNumber(FullPortName.substring(0,4));
            }
        });

        parityBox.setSelectedIndex(0);
        SettingsContainer.setDataParityStop(SettingsContainer.ParityList[parityBox.getSelectedIndex()]);
        parityBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SettingsContainer.setDataParityStop(SettingsContainer.ParityList[parityBox.getSelectedIndex()]);
            }
        });

        baudrateBox.setSelectedIndex(7);
        SettingsContainer.setBaudRate(SettingsContainer.BaudList[(baudrateBox.getSelectedIndex())]);
        baudrateBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SettingsContainer.setBaudRate(SettingsContainer.BaudList[(baudrateBox.getSelectedIndex())]);
            }
        });

        frequencyBox.setSelectedIndex(2);
        SettingsContainer.setMessageFrequency(SettingsContainer.FreqList[frequencyBox.getSelectedIndex()]);
        frequencyBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SettingsContainer.setMessageFrequency(SettingsContainer.FreqList[frequencyBox.getSelectedIndex()]);
            }
        });

        filePathField.setBounds(3,35,482,30);
        filePathField.setEditable(true);
        filePathField.setVisible(true);
        filePathField.setText("Enter path to NMEA log here");
        filePathField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                filePathField.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                SettingsContainer.setNmeaPath(filePathField.getText());
            }
        });

        // --------------- Text Area ---------------
        mainTextArea.setBounds(3, 70,480,450);
        mainTextArea.setEditable(false);
        mainTextArea.setVisible(true);
        mainTextArea.setLineWrap(false);
        mainTextArea.setColumns(1);
        mainTextArea.setBackground(Color.lightGray);

        // --------------- Button ---------------
        mainButton.setBounds(3,525,480,35);
        mainButton.setVisible(true);
        mainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (SettingsContainer.getPausedStatus()==false){
                    SettingsContainer.setPausedStatus(true);
                    mainButton.setText("SEND");
                } else {
                    SettingsContainer.setPausedStatus(false);
                    mainButton.setText("PAUSE");
                    FileToComProcessor = new FileToComPocessor();

                    try {
                        FileToComProcessor.RunByteSendCycle();
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
            }
        });


    }
}
