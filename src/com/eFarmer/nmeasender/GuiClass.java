package com.eFarmer.nmeasender;

import javax.swing.*;
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
    private final JFileChooser fileChooser;
    private final JButton cooseFile;
    private final JTextArea mainTextArea;
    private final JButton mainButton;
    private ComPort ComPort = new ComPort();

    public GuiClass() throws IOException {
        String[] PortList = ComPort.getPortsList().toArray(new String[0]);
        mainFrame = new JFrame();
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainFrame.setContentPane(mainPanel);
        mainBar = new JToolBar(JToolBar.HORIZONTAL);
        comPortBox = new JComboBox(PortList);
        frequencyBox = new JComboBox(SettingsContainer.FreqList);
        baudrateBox = new JComboBox(SettingsContainer.BaudList);
        parityBox = new JComboBox(SettingsContainer.ParityList);
        filePathField = new JTextField();
        cooseFile = new JButton("Select file");
        fileChooser = new JFileChooser();
        mainTextArea = new JTextArea();
        mainButton = new JButton("SEND");

        // --------------- Main Frame ---------------
        mainFrame.setTitle("Nmea to serial sender");
        mainFrame.setSize(500, 600);
        mainFrame.setResizable(false);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.add(mainBar);
        mainFrame.add(filePathField);
        mainFrame.add(cooseFile);
        mainFrame.add(mainTextArea);
        mainFrame.add(mainButton);
        mainFrame.setVisible(true);

        mainPanel.setBackground(Color.white);
        mainPanel.setLayout(new BorderLayout());

        // --------------- Top Bar ---------------
        mainBar.setFloatable(false);
        mainBar.setSize(new Dimension(485,30));
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

        // --------------- Cooser Button ---------------
        cooseFile.setBounds(380, 35, 100, 30);
        cooseFile.setVisible(true);

        // --------------- Text Field ---------------
        filePathField.setBounds(3,35,375,30);
        filePathField.setEditable(true);
        filePathField.setText("Enter path to NMEA log here");
        filePathField.setVisible(true);


        // --------------- Text Area ---------------
        mainTextArea.setBounds(3, 70,480,450);
        mainTextArea.setEditable(false);
        mainTextArea.setLineWrap(true);
        mainTextArea.setColumns(1);
        mainTextArea.setBackground(Color.lightGray);
        mainTextArea.setVisible(true);

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
                System.out.println("COM PORT SELECTED: " +FullPortName.substring(0,4));
            }
        });

        parityBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SettingsContainer.setDataParityStop(SettingsContainer.ParityList[parityBox.getSelectedIndex()]);
                System.out.println("PARITY SET: " +SettingsContainer.ParityList[parityBox.getSelectedIndex()]);
            }
        });

        baudrateBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SettingsContainer.setBaudRate(SettingsContainer.BaudList[(baudrateBox.getSelectedIndex())]);
                System.out.println("Baudrate set: " +SettingsContainer.BaudList[(baudrateBox.getSelectedIndex())]);
            }
        });

        frequencyBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SettingsContainer.setMessageFrequency(SettingsContainer.FreqList[frequencyBox.getSelectedIndex()]);
                System.out.println("Message frequency set: " +SettingsContainer.FreqList[frequencyBox.getSelectedIndex()]);
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

        cooseFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.showOpenDialog(fileChooser);
                if (fileChooser.getSelectedFile() != null) {
                    filePathField.setText(fileChooser.getSelectedFile().getAbsolutePath());
                    SettingsContainer.setNmeaPath(fileChooser.getSelectedFile().getAbsolutePath());
                    System.out.println("FILE SELECTED: "+fileChooser.getSelectedFile().getAbsolutePath());
                }
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
                cooseFile.setEnabled(false);

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
