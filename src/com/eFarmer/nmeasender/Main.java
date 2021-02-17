package com.eFarmer.nmeasender;


public class Main {

    public static void main(String[] args) throws InterruptedException{

        FileToComPocessor FileToComPocessor = new FileToComPocessor();
        FileToComPocessor.RunByteSendCycle();
        FileToComPocessor.FinallizeSendCycle();

    }
}
