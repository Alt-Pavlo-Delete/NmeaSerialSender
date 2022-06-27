package com.eFarmer.nmeasender;
/* This class is used to work with log file.
 * 1. Creates buffered string reader on init.
 * 2. Reads string line / casts string line to bytes.
 * 3. Closes buffer on finalize.
 */

import java.io.*;
import java.nio.charset.Charset;

public class NmeaFileReader {

    public String LastLine;
    public byte[] ByteLine; //use if write to COM method requires byte array.
    private BufferedReader StringReader;
    private SettingsContainer SettingsContainer = com.eFarmer.nmeasender.SettingsContainer.getInstance();

    public NmeaFileReader() {
        try {
            StringReader = new BufferedReader(new FileReader(SettingsContainer.getNmeaPath()));
        }
        catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
    }


    public String GetDataLine(){
        try {
            LastLine = StringReader.readLine()+"\n"; //+\n is required by FieldBee Navigation NMEA parser lib.
            ByteLine = LastLine.getBytes(Charset.forName("ASCII")); //use if write to COM method requires byte array.
        }
        catch (IOException ioEx) {
            ioEx.printStackTrace();

        }
        return LastLine;
    }


    public void CloseReader(){
        try {
          StringReader.close();
        }
        catch (IOException ioEx){
            ioEx.printStackTrace();
        }
    }

}
