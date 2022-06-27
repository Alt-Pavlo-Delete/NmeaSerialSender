package com.eFarmer.nmeasender;

/* This class is used to work with COM port JSerialComm lib.
 * 1.Opens new COM port instance
 * 2.Writes bytes to COM.
 * 3.Close COM port on finnalize.
 */

import com.fazecast.jSerialComm.SerialPort;
import java.util.ArrayList;

public class ComPort {

    private SerialPort newPortInstance;

    public ArrayList<String> getPortsList(){
        SerialPort[] ComPortsAvailable = SerialPort.getCommPorts();
        ArrayList<String> PortsList = new ArrayList<String>();
        for (SerialPort Port : ComPortsAvailable){
            PortsList.add(Port.getSystemPortName().concat(" ("+String.valueOf(Port)+")"));
        }
        return PortsList;
    }

    public void openComPort(String portNumber, int newBaudRate, int newDataBits, int newStopBits, int newParity) {
        if (newPortInstance == null) {
            newPortInstance = SerialPort.getCommPort(portNumber); // Checks if port was already opened but method called twice.
        }
            try {
                newPortInstance.openPort();
                newPortInstance.setComPortParameters(newBaudRate, newDataBits, newStopBits, newParity);

                if (newPortInstance.isOpen() == false) {
                    System.out.println("!!!! COM port opening is FAILED !!!!");
                    throw new RuntimeException("COM port opening is FAILED");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
    }

    public void WriteBytes(byte[] bytes){
        try {
    newPortInstance.writeBytes(bytes, bytes.length);
       } catch (RuntimeException ex) {
            ex.printStackTrace();
        }
    }

    public void ClosePort(){
        try {
            newPortInstance.closePort();
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        }
    }

}
