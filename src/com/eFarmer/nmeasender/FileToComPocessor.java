package com.eFarmer.nmeasender;

/* This class implements interaction of ComPort and NmeaFileReader classes.
 */

public class FileToComPocessor implements Runnable {
    private NmeaFileReader NmeaFileReader;
    private final SettingsContainer getSettingsContainer = com.eFarmer.nmeasender.SettingsContainer.getInstance();
    private ComPort comPort = new ComPort();
    private GuiClass guiInstance;

    public FileToComPocessor(GuiClass instanceShare){
        guiInstance = instanceShare;
    }

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

                //This cycle sends data bytes to COM port. If cycle is not paused by user and COM successfully opened.
                while (comPort != null && !getSettingsContainer.getPausedStatus()) { // Pause command from GUI is stored as a flag in SettingsContainer singleton.
                        NmeaFileReader.GetDataLine();
                        comPort.WriteBytes(NmeaFileReader.ByteLine);

                        if (NmeaFileReader.LastLine.contains("null")) { //Finalize in the end of log file.
                            System.out.println("---- Reached the END of the NMEA file ----");
                            guiInstance.endOfTransmission();
                            CloseComPort();
                            CloseNmeaReader();
                            break;
                        }

                        System.out.println(NmeaFileReader.LastLine);
                        
                        try {
                            if (NmeaFileReader.LastLine.contains("GGA")) {
                                Thread.sleep(1000 / getSettingsContainer.getMessageFrequency()); // Provides GGA string with required Frequency.
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
            NmeaFileReader.CloseReader(); //null - on first start, !null= if this class was called again after first start and new reader is required (on file path change).
            NmeaFileReader = null;
        }
    }
}
