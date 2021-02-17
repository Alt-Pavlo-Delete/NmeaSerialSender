package com.eFarmer.nmeasender;

import java.io.*;
import java.util.Properties;

public class GetSettings {
   private String NmeaPath;
   private String PortNumber;
   private String BaudRate;
   private String DataParityStop;
   private String MessageFrequency;

   public GetSettings(){
       try {
           File SettingsFile = new File("Settings.txt");
           FileReader SettingsReader = new FileReader(SettingsFile);
           Properties Settings = new Properties();
           Settings.load(SettingsReader);
           NmeaPath = Settings.getProperty("NmeaPath");
           PortNumber = Settings.getProperty("PortNumber");
           BaudRate = Settings.getProperty("BaudRate");
           DataParityStop = Settings.getProperty("DataParityStop");;
           MessageFrequency = Settings.getProperty("MessageFrequency");
           System.out.println(PortNumber + " " + BaudRate +" "+ DataParityStop + " "+  MessageFrequency);
           SettingsReader.close();

       } catch (IOException ioEx){
           ioEx.printStackTrace();
       }
   }

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
        if (MessageFrequency.length() != 0 && MessageFrequency.length() <= 4) {
            return Integer.parseInt(MessageFrequency.substring(0, 2));
        }
        throw new RuntimeException("Invalid message frequency Hz. 1-100Hz is allowed.");
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
}