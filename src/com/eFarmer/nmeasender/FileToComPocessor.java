package com.eFarmer.nmeasender;

public class FileToComPocessor {

    NmeaFileReader NmeaFileReader = new NmeaFileReader();
    GetSettings GetSettings = new GetSettings();
    ComPort comPort = new ComPort(GetSettings.getPortNumber(), GetSettings.getBaudRate(), GetSettings.getDataBits(), GetSettings.getStopBits(), GetSettings.getParity());

    public void RunByteSendCycle() throws InterruptedException {
        try {
            while (comPort != null) {
                NmeaFileReader.GetDataLine();
                comPort.WriteBytes(NmeaFileReader.ByteLine);
                System.out.println(NmeaFileReader.LastLine);

                if (NmeaFileReader.LastLine.contains("GGA")) {
                    Thread.sleep(1000 / GetSettings.getMessageFrequency());
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
