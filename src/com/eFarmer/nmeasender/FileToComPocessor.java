package com.eFarmer.nmeasender;

public class FileToComPocessor implements Runnable {
    private NmeaFileReader NmeaFileReader;
    private final SettingsContainer getSettingsContainer = com.eFarmer.nmeasender.SettingsContainer.getInstance();
    private ComPort comPort = new ComPort();

    public void start(){
        Thread processorThread = new Thread(this::run);
        processorThread.start();
    }

    @Override
    public void run() {

                if (NmeaFileReader == null){
                   NmeaFileReader = new NmeaFileReader();
                }

                comPort.openComPort(getSettingsContainer.getPortNumber(), getSettingsContainer.getBaudRate(), getSettingsContainer.getDataBits(), getSettingsContainer.getStopBits(), getSettingsContainer.getParity());

                while (comPort != null && !getSettingsContainer.getPausedStatus()) {
                        NmeaFileReader.GetDataLine();
                        comPort.WriteBytes(NmeaFileReader.ByteLine);

                        if (NmeaFileReader.LastLine.contains("null")) {
                            System.out.println("---- Reached the END of the NMEA file ----");
                            GuiClass.getInstance().endOfTransmission();
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
        if (NmeaFileReader !=null) {
            NmeaFileReader.CloseReader();
            NmeaFileReader = null;
        }
    }
}
