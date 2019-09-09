package com.jfxserialcommunicator.core;

import com.fazecast.jSerialComm.SerialPort;
import com.jfxserialcommunicator.interfaces.ISerialPortHelper;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import jdk.nashorn.internal.objects.annotations.Constructor;

import java.util.HashMap;

public class SerialPortHelper implements ISerialPortHelper {

    private String portName;

    private int portBaund;

    private int portParity;

    private int portDataBits;

    private int portStopBits;

    private SerialPort _selectedPort;
    private HashMap<String, SerialPort> _serialPortList;

    /*
    * Getter & Setter Methods
    * */
    //public String getPortName(){
    //    return this.portName;
    //}

    public void setPortName(String portName){
        this.portName = portName;

        if(!isNullOrEmpty(this.portName)){
            if(_serialPortList.get(this.portName) != null){
                _selectedPort = _serialPortList.get(this.portName);
            }
            else
                _selectedPort = null;
        }
        else
            _selectedPort = null;
    }

    //public int getPortBaund() {
    //    return portBaund;
    //}

    public void setPortBaund(int portBaund) {
        this.portBaund = portBaund;
    }

    //public int getPortParity() {
    //    return portParity;
    //}

    public void setPortParity(int portParity) {
        this.portParity = portParity;
    }

    //public int getPortDataBits() {
    //    return portDataBits;
    //}

    public void setPortDataBits(int portDataBits) {
        this.portDataBits = portDataBits;
    }

    //public int getPortStopBits() {
    //    return portStopBits;
    //}

    public void setPortStopBits(int portStopBits) {
        this.portStopBits = portStopBits;
    }

    public SerialPort getPort(){
        return this._selectedPort;
    }

    /*
    * Getter & Setter Methods Finish
    * */

    public SerialPortHelper(){
        _serialPortList = new HashMap<>();
    }

    @Override
    public boolean PortOpen(){
        try {
            if(_selectedPort != null){

                if(IsOpen()){
                    return true;
                }

                _selectedPort.setBaudRate(portBaund);
                _selectedPort.setParity(portParity);
                _selectedPort.setNumDataBits(portDataBits);
                _selectedPort.setNumStopBits(portStopBits);

                _selectedPort.openPort();
                return true;
            }
        }
        catch (Exception e){ e.printStackTrace();}

        return false;
    }

    @Override
    public void PortOpen(String portName) {
        if(!IsOpen()){
            if(_serialPortList.get(portName) != null){
                _selectedPort = _serialPortList.get(portName);
                _selectedPort.openPort();
            }
        }
    }

    @Override
    public void PortOpen(String portName, int Baund, int Parity, int DataBits,  int StopBits){
        if(!IsOpen()){
            if(_serialPortList.get(portName) != null){
                _selectedPort = _serialPortList.get(portName);
                _selectedPort.setBaudRate(Baund);
                _selectedPort.setParity(Parity);
                _selectedPort.setNumDataBits(DataBits);
                _selectedPort.setNumStopBits(StopBits);

                _selectedPort.openPort();
            }
        }
    }

    @Override
    public void PortClose() {
        if(IsOpen()){
            _selectedPort.removeDataListener();
            _selectedPort.closePort();
        }
    }

    @Override
    public HashMap<String, SerialPort> PortList(boolean isRefresh) {
        if(isRefresh){
            try {
                for (SerialPort serialPort : SerialPort.getCommPorts()) {
                    _serialPortList.put(serialPort.getSystemPortName(), serialPort);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return _serialPortList;
    }

    @Override
    public boolean IsOpen() {
        if(_selectedPort != null)
            return _selectedPort.isOpen();
        else
            return false;
    }

    // https://www.programiz.com/java-programming/examples/string-empty-null
    public boolean isNullOrEmpty(String str) {
        if(str != null && !str.trim().isEmpty())
            return false;
        return true;
    }
}
