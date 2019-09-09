package com.jfxserialcommunicator.interfaces;

import com.fazecast.jSerialComm.SerialPort;
import java.util.HashMap;

public interface ISerialPortHelper {
    // Open and Start Event Listener Serial Port
    boolean PortOpen();

    void PortOpen(String portName);

    void PortOpen(String portName, int Baund, int Parity, int DataBits,  int StopBits);

    // Stop and Close Serial Port
    void PortClose();

    // Returns the active port name list
    HashMap<String, SerialPort> PortList(boolean isRefresh);

    boolean IsOpen();
}
