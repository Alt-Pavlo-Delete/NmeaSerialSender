package com.eFarmer.nmeasender;

import com.fazecast.jSerialComm.SerialPort;
import java.util.ArrayList;

import java.util.Arrays;


public class ComPort {

    private SerialPort newPortInstance;
    public String[] ComPortsAvailable;

    public ComPort(){
        SerialPort[] ComPorts = SerialPort.getCommPorts();
        ArrayList<String> portNames = new ArrayList<>();
        for (SerialPort port : ComPorts) {
            portNames.add(port.getSystemPortName());
        }
        ComPortsAvailable = portNames.toArray(new String[] {});
    }

    public void openComPort(String portNumber, int newBaudRate, int newDataBits, int newStopBits, int newParity){
        newPortInstance = SerialPort.getCommPort(portNumber);
        try {
            newPortInstance.openPort();
            newPortInstance.setComPortParameters(newBaudRate, newDataBits, newStopBits, newParity);
            if (newPortInstance.isOpen() == false){
                throw new RuntimeException("COM port opening is FAILED");
            }
        } catch (RuntimeException ex) {
            throw new RuntimeException("COM port opening is FAILED");
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
