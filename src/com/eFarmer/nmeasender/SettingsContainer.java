package com.eFarmer.nmeasender;

import java.io.*;

public class SettingsContainer {
    public String[] FreqList = {"1Hz", "5Hz", "10Hz", "15Hz", "20Hz"};
    public String[] BaudList = {"1200","2400","4800","9600","19200","38400","57600","115200"};
    public String[] ParityList = {"8N1","7N1","6N1","5N1","4N1","8N3","7N3","6N3"};
   volatile private String nmeaPath;
   volatile private String PortNumber;
   volatile private String BaudRate;
   volatile private String DataParityStop;
   volatile private String MessageFrequency;
   volatile private Boolean PausedStatus = true;

   private static SettingsContainer instance;

   public final static SettingsContainer getInstance(){
       if (instance == null){
           synchronized (SettingsContainer.class){
               if (instance == null){
                   instance = new SettingsContainer();
               }
           }
       }
       return instance;
   }

   private SettingsContainer(){   }

   synchronized public void setNmeaPath(String nmeaPath){this.nmeaPath = nmeaPath;
    }
   synchronized public void setPortNumber(String PortNumber) {this.PortNumber = PortNumber;}
   synchronized public void setBaudRate(String BaudRate) {this.BaudRate = BaudRate;}
   synchronized public void setDataParityStop(String DataParityStop) {this.DataParityStop = DataParityStop;}
   synchronized public void setMessageFrequency(String MessageFrequency) {this.MessageFrequency = MessageFrequency;}
   synchronized public void setPausedStatus(Boolean PausedStatus) {this.PausedStatus = PausedStatus;}

   public String getPortNumber() {
    if (PortNumber.contains("COM")){
        return PortNumber;
        }
    System.out.println("!!!! Invalid COM name. COM* is expected. !!!!");
    throw new RuntimeException("Invalid COM name. COM* is expected.");
    }

    public int getBaudRate() {
       if (BaudRate.contains("1200") || BaudRate.contains("2400") || BaudRate.contains("4800") || BaudRate.contains("9600") || BaudRate.contains("19200") || BaudRate.contains("38400") || BaudRate.contains("57600") || BaudRate.contains("115200")) {
           return Integer.parseInt(BaudRate);
       }
       System.out.println("!!!! Invalid baud rate settings. 1200,2400,4800,9600,19200,38400,57600,115200 allowed. !!!!");
    throw new RuntimeException("Invalid baud rate settings. 1200,2400,4800,9600,19200,38400,57600,115200 allowed.");
    }

    public int getDataBits() {
        return Integer.parseInt(String.valueOf(DataParityStop.charAt(0)));
    }

    public int getParity() {
       if (DataParityStop.contains("N")|| DataParityStop.contains("n")){
           return 0; // NO_PARITY
       } else if (DataParityStop.contains("O")|| DataParityStop.contains("o")){
            return 1; // ODD_PARITY
        } else if (DataParityStop.contains("E")|| DataParityStop.contains("e")){
           return 2; // EVEN_PARITY
       } else {
           System.out.println("!!!! Invalid parity. Only N(no), O(Odd), E(Even) are allowed. !!!!");
           throw new RuntimeException("Invalid parity. Only N(no), O(Odd), E(Even) are allowed.");
       }
    }

    public int getStopBits() {
        return Integer.parseInt(String.valueOf(DataParityStop.charAt(2)));
    }

    public int getMessageFrequency(){
        if (MessageFrequency.length() != 0 && MessageFrequency.contains("Hz")) {

            return Integer.parseInt(MessageFrequency.substring(0, (MessageFrequency.indexOf("Hz"))));
        }
        System.out.println("!!!! Invalid message frequency Hz. 1-10Hz is allowed. !!!!");
        throw new RuntimeException("Invalid message frequency Hz. 1-10Hz is allowed.");
    }

    public String getNmeaPath() {
       if (nmeaPath.contains(".txt")||nmeaPath.contains(".nmea")) {
           return nmeaPath;
       }
       System.out.println("!!!! Nmea file path should contain .txt or .nmea !!!!");
       throw new RuntimeException("Nmea file path should contain .txt or .nmea ");
    }

    public Boolean getPausedStatus(){
     return PausedStatus;
    }
}
