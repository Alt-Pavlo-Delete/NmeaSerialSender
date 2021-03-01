package com.eFarmer.nmeasender;

import java.io.*;

public class SettingsContainer {
    public String[] FreqList = {"1Hz", "5Hz", "10Hz"};
    public String[] BaudList = {"1200","2400","4800","9600","19200","38400","57600","115200"};
    public String[] ParityList = {"8N1","7N1","6N1","5N1","4N1","8N3","7N3","6N3"};
   private String NmeaPath;
   private String PortNumber;
   private String BaudRate;
   private String DataParityStop;
   private String MessageFrequency;
   private Boolean PausedStatus = true;

   public void setNmeaPath(String NmeaPath){
       this.NmeaPath = NmeaPath;
   }
   public void setPortNumber(String PortNumber) {this.PortNumber = PortNumber;}
   public void setBaudRate(String BaudRate) {this.BaudRate = BaudRate;}
   public void setDataParityStop(String DataParityStop) {this.DataParityStop = DataParityStop;}
   public void setMessageFrequency(String MessageFrequency) {this.MessageFrequency = MessageFrequency;}
   public void setPausedStatus(Boolean PausedStatus) {this.PausedStatus = PausedStatus;}

   public String getPortNumber() {
    if (PortNumber.contains("COM")){
        return PortNumber;
        }
    throw new RuntimeException("Invalid COM settings. COM* is expected.");
    }

    public int getBaudRate() {
       if (BaudRate.contains("1200") || BaudRate.contains("2400") || BaudRate.contains("4800") || BaudRate.contains("9600") || BaudRate.contains("19200") || BaudRate.contains("38400") || BaudRate.contains("57600") || BaudRate.contains("115200")) {
           return Integer.parseInt(BaudRate);
       }
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
        throw new RuntimeException("Invalid message frequency Hz. 1-10Hz is allowed.");
    }

    public String getNmeaPath() {
       if (NmeaPath.contains(".nmea")){
           return NmeaPath;
       }
       if (NmeaPath.contains(".txt")){
            return NmeaPath;
        }
       throw new RuntimeException("Invalid NMEA file name .nmea or .txt is expected.");
    }

    public Boolean getPausedStatus(){
     return PausedStatus;
    }
}
