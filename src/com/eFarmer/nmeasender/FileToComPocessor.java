package com.eFarmer.nmeasender;

import java.io.File;
import java.io.PipedOutputStream;
import java.io.PrintStream;

public class FileToComPocessor {

    private NmeaFileReader NmeaFileReader = new NmeaFileReader();
    private SettingsContainer getSettingsContainer = new SettingsContainer();
    private ComPort comPort = new ComPort();

    public void RunByteSendCycle() throws InterruptedException {
        comPort.openComPort(getSettingsContainer.getPortNumber(), getSettingsContainer.getBaudRate(), getSettingsContainer.getDataBits(), getSettingsContainer.getStopBits(), getSettingsContainer.getParity());

        try {
            while (comPort != null && getSettingsContainer.getPausedStatus()==false) {
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
