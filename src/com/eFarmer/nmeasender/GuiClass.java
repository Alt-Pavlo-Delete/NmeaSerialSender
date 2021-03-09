package com.eFarmer.nmeasender;

import com.sun.tools.jconsole.JConsolePlugin;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class GuiClass {
    private SettingsContainer SettingsContainer = com.eFarmer.nmeasender.SettingsContainer.getInstance();
    private FileToComPocessor comProcessorThread = new FileToComPocessor();
    private PrintStream outStream;
    private final JFrame mainFrame;
    private final JPanel mainPanel;
    private final JToolBar mainBar;
    private final JComboBox comPortBox;
    private final JComboBox frequencyBox;
    private final JComboBox baudrateBox;
    private final JComboBox parityBox;
    private final JTextField filePathField;
    private final JTextArea mainTextArea;
    private final JButton mainButton;
    private ComPort ComPort = new ComPort();

    public GuiClass() throws IOException {
        String[] PortList = ComPort.getPortsList().toArray(new String[0]);
        mainFrame = new JFrame();
        mainPanel = new JPanel();
        mainFrame.setContentPane(mainPanel);
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

        comPortBox.setSelectedIndex(0);
        SettingsContainer.setPortNumber((String.valueOf(comPortBox.getSelectedItem()).substring(0,4)));

        parityBox.setSelectedIndex(0);
        SettingsContainer.setDataParityStop(SettingsContainer.ParityList[parityBox.getSelectedIndex()]);

        baudrateBox.setSelectedIndex(7);
        SettingsContainer.setBaudRate(SettingsContainer.BaudList[(baudrateBox.getSelectedIndex())]);

        frequencyBox.setSelectedIndex(2);
        SettingsContainer.setMessageFrequency(SettingsContainer.FreqList[frequencyBox.getSelectedIndex()]);


        // --------------- Text Field ---------------
        filePathField.setBounds(3,35,482,30);
        filePathField.setEditable(true);
        filePathField.setVisible(true);
        filePathField.setText("Enter path to NMEA log here");


        // --------------- Text Area ---------------
        mainTextArea.setBounds(3, 70,480,450);
        mainTextArea.setEditable(false);
        mainTextArea.setVisible(true);
        mainTextArea.setLineWrap(true);
        mainTextArea.setColumns(1);
        mainTextArea.setBackground(Color.lightGray);

        // --------------- Stream to TextArea ---------------
        outStream = new PrintStream(new OutputStream() {

            @Override
            public void write(int b) throws IOException {
                if (mainTextArea.getLineCount() >= 25){mainTextArea.setText(null);}
                mainTextArea.setText(mainTextArea.getText() + String.valueOf((char)b));
            }
        });
        System.setOut(outStream);
        //System.setErr(outStream); ENABLE TO REDIRECT ERROR MESSAGES TO MAIN TEXT AREA OF GUI


        // --------------- Button ---------------
        mainButton.setBounds(3,525,480,35);
        mainButton.setVisible(true);


        // --------------- LISTENERS ---------------
        comPortBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String FullPortName = String.valueOf(comPortBox.getSelectedItem());
                SettingsContainer.setPortNumber(FullPortName.substring(0,4));
            }
        });

        parityBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SettingsContainer.setDataParityStop(SettingsContainer.ParityList[parityBox.getSelectedIndex()]);
            }
        });

        baudrateBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SettingsContainer.setBaudRate(SettingsContainer.BaudList[(baudrateBox.getSelectedIndex())]);
            }
        });

        frequencyBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SettingsContainer.setMessageFrequency(SettingsContainer.FreqList[frequencyBox.getSelectedIndex()]);
            }
        });

        filePathField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (filePathField.getText().equals("Enter path to NMEA log here")){
                    filePathField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                SettingsContainer.setNmeaPath(filePathField.getText());
            }
        });

        mainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comPortBox.setEnabled(false);
                frequencyBox.setEnabled(false);
                parityBox.setEnabled(false);
                baudrateBox.setEnabled(false);
                filePathField.setEditable(false);

                SettingsContainer.setNmeaPath(filePathField.getText());
                if (SettingsContainer.getPausedStatus()==false){
                    SettingsContainer.setPausedStatus(true);
                    mainButton.setText("SEND");
                    System.out.println("--------------- PAUSED ---------------");
                } else {
                    SettingsContainer.setPausedStatus(false);
                    mainButton.setText("PAUSE");

                    comProcessorThread.start();
                    /*
                    Former RunSendByteCycle method of FileToComProcessor class.
                    1.Opens port.
                    2.Gets line via NmeaFileReader.
                    3.Writes bytes via ComPort Class.
                    4.Finalizing.
                    */
                }
            }
        });

    }
}
