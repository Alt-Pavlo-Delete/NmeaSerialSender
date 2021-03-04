package com.eFarmer.nmeasender;

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
            LastLine = StringReader.readLine()+"\n";
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
