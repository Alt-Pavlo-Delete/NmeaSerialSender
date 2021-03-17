package com.eFarmer.nmeasender;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class GuiClass {
    private final SettingsContainer SettingsContainer = com.eFarmer.nmeasender.SettingsContainer.getInstance();
    private final FileToComPocessor comProcessorThread = new FileToComPocessor();
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
    private static GuiClass instance;

    public final static GuiClass getInstance(){
        if (instance == null){
            synchronized (GuiClass.class){
                if (instance == null){
                    try {
                        instance = new GuiClass();
                    }catch (IOException ex){
                        ex.printStackTrace();
                    }
                }
            }
        }
        return instance;
    }

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

        try {
            comPortBox.setSelectedIndex(0);
            SettingsContainer.setPortNumber((String.valueOf(comPortBox.getSelectedItem()).substring(0, 4)));
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            parityBox.setSelectedIndex(0);
            SettingsContainer.setDataParityStop(SettingsContainer.ParityList[parityBox.getSelectedIndex()]);
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            baudrateBox.setSelectedIndex(7);
            SettingsContainer.setBaudRate(SettingsContainer.BaudList[(baudrateBox.getSelectedIndex())]);
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            frequencyBox.setSelectedIndex(2);
            SettingsContainer.setMessageFrequency(SettingsContainer.FreqList[frequencyBox.getSelectedIndex()]);
        } catch (Exception e){
            e.printStackTrace();
        }

        // --------------- Cooser Button ---------------
        cooseFile.setBounds(380, 35, 100, 30);
        cooseFile.setVisible(true);

        // --------------- Text Field ---------------
        filePathField.setBounds(3,35,375,30);
        filePathField.setEditable(false);
        filePathField.setText("Select path to NMEA log ->");
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

        cooseFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comProcessorThread.CloseNmeaReader(); //If user changing file in PAUSE state -> close Buffered reader if its available;
                fileChooser.showOpenDialog(fileChooser);
                if (fileChooser.getSelectedFile() != null) {
                    filePathField.setText(fileChooser.getSelectedFile().getAbsolutePath());
                    SettingsContainer.setNmeaPath(fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });

        mainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                SettingsContainer.setNmeaPath(filePathField.getText());
                if (!SettingsContainer.getPausedStatus()){
                    SettingsContainer.setPausedStatus(true);
                    comProcessorThread.CloseComPort();
                    cooseFile.setEnabled(true);
                    comPortBox.setEnabled(true);
                    baudrateBox.setEnabled(true);
                    parityBox.setEnabled(true);
                    frequencyBox.setEnabled(true);
                    cooseFile.setEnabled(true);
                    mainButton.setText("SEND");
                    System.out.println("--------------- PAUSED ---------------");

                } else {
                    SettingsContainer.setPausedStatus(false);
                    cooseFile.setEnabled(false);
                    comPortBox.setEnabled(false);
                    baudrateBox.setEnabled(false);
                    parityBox.setEnabled(false);
                    frequencyBox.setEnabled(false);
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

    //Called by FileToComProcessor if end of nmea file is detected.
    public void endOfTransmission(){
        SettingsContainer.setPausedStatus(true);
        cooseFile.setEnabled(true);
        comPortBox.setEnabled(true);
        baudrateBox.setEnabled(true);
        parityBox.setEnabled(true);
        frequencyBox.setEnabled(true);
        cooseFile.setEnabled(true);
        mainButton.setText("SEND");
    }
}
