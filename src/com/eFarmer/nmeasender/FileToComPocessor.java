package com.eFarmer.nmeasender;

import java.io.File;

public class FileToComPocessor {

    NmeaFileReader NmeaFileReader = new NmeaFileReader();
    private SettingsContainer getSettingsContainer = new SettingsContainer();
    ComPort comPort = new ComPort();

    public FileToComPocessor() {
        comPort.openComPort(getSettingsContainer.getPortNumber(), getSettingsContainer.getBaudRate(), getSettingsContainer.getDataBits(), getSettingsContainer.getStopBits(), getSettingsContainer.getParity());
        ;
    }

    public void RunByteSendCycle() throws InterruptedException {
        try {
            while (comPort != null) {
                NmeaFileReader.GetDataLine();
                comPort.WriteBytes(NmeaFileReader.ByteLine);
                System.out.println(NmeaFileReader.LastLine);

                if (NmeaFileReader.LastLine.contains("GGA")) {
                    Thread.sleep(1000 / getSettingsContainer.getMessageFrequency());
                }
            }
        } catch (NullPointerException nullEx){
            System.out.println("---- Reached the end of NMEA file ----");
        }
    }

    public void FinallizeSendCycle() {
        System.out.println("Closing PORTS");
        comPort.ClosePort();
        NmeaFileReader.CloseReader();
    }
}
