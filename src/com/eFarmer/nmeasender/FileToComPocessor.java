package com.eFarmer.nmeasender;

import java.io.File;
import java.io.PipedOutputStream;
import java.io.PrintStream;

public class FileToComPocessor implements Runnable {
    private Thread processorThread;
    private NmeaFileReader NmeaFileReader;
    private SettingsContainer getSettingsContainer = com.eFarmer.nmeasender.SettingsContainer.getInstance();
    private ComPort comPort = new ComPort();

    public void start(){
        processorThread = new Thread(this::run);
        processorThread.start();
    }

    @Override
    public void run() {
                if (NmeaFileReader == null){
                   NmeaFileReader = new NmeaFileReader();
                }

                comPort.openComPort(getSettingsContainer.getPortNumber(), getSettingsContainer.getBaudRate(), getSettingsContainer.getDataBits(), getSettingsContainer.getStopBits(), getSettingsContainer.getParity());

                while (comPort != null && getSettingsContainer.getPausedStatus() == false) {
                        NmeaFileReader.GetDataLine();
                        comPort.WriteBytes(NmeaFileReader.ByteLine);

                        if (NmeaFileReader.LastLine.contains("null")) {
                            System.out.println("---- Reached the END of the NMEA file ----");
                            CloseComPort();
                            CloseNmeaReader();
                            break;
                        }

                        System.out.println(NmeaFileReader.LastLine);

                        try {
                            if (NmeaFileReader.LastLine.contains("GGA")) {
                                Thread.sleep(1000 / getSettingsContainer.getMessageFrequency());
                            }
                        } catch (InterruptedException interruptedEx) {
                            interruptedEx.printStackTrace();
                        }
                }
            }

    public void CloseComPort() {
        comPort.ClosePort();
    }

    public void CloseNmeaReader() {
        NmeaFileReader.CloseReader();
    }

}
