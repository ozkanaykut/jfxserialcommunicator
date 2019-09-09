package com.jfxserialcommunicator.core;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class SerialPortEvent_DataListener implements SerialPortDataListener{

    private TextArea txtConsole;
    //private SerialPort serialPort;

    //public SerialPortEvent_DataListener(SerialPort serialPortObject ,TextArea txtConsoleObject) {
    public SerialPortEvent_DataListener(TextArea txtConsoleObject) {

        // TODO Auto-generated constructor stub
        //serialPort = serialPortObject;
        txtConsole = txtConsoleObject;

        //serialPort.addDataListener(this);
    }


    @Override
    public int getListeningEvents() {
        // TODO Auto-generated method stub
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }

    @Override
    public void serialEvent(SerialPortEvent event)
    {
        try {

            if(event.getEventType() != SerialPort.LISTENING_EVENT_DATA_RECEIVED)
                return;

            byte[] readSerialDataByte = event.getReceivedData();
            String readSerialDataString = new String(readSerialDataByte);
            //System.out.print("Received Data : " + readSerialDataString);

            Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    //txtConsole.appendText( "Received Data: " + readSerialDataString);
                    txtConsole.appendText(readSerialDataString);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}