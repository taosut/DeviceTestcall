package com.emailgw.master.serialCommHandler;


import com.fazecast.jSerialComm.SerialPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Objects;


public class UartPort implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(UartPort.class);
    private int PortNumber;
    public SerialPort comPort;
    public String IMEI;


    public UartPort(int PortNumber, SerialPort serialPort){
        this.PortNumber = PortNumber;
        this.comPort = serialPort;
        this.comPort.setBaudRate(115200 );
        this.comPort.openPort();
        this.comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 0);
        if(SendATCommand("AT")){
            logger.warn("Port {} is OK to AT command",this.PortNumber);
            this.initPort();
        }else {
            logger.warn("Port {} is NotOK",this.PortNumber);
            this.closePort();
        }

    }
    public Boolean SendATCommand(String at){
        String send = at + (char)0x0D + (char)0x0A;
        byte[] send_byte = send.getBytes();
        this.comPort.writeBytes(send_byte,at.length()+2);
        byte[] readBuffer = new byte[1024];
        int numRead = comPort.readBytes(readBuffer, readBuffer.length);
        String returnMess = convertBuffer(readBuffer,numRead);
        return returnMess.toUpperCase().contains("OK");
    }
    public String SendCommand(String at){
        logger.debug("Send Command {}",at);
        String send = at + (char)0x0D + (char)0x0A;
        byte[] send_byte = send.getBytes();
        this.comPort.writeBytes(send_byte,at.length()+2);
        byte[] readBuffer = new byte[1024];
        int numRead = comPort.readBytes(readBuffer, readBuffer.length);
        String returnMess = convertBuffer(readBuffer,numRead);
        return returnMess;
    }

    public String convertBuffer(byte[]buff, int len){
        String ret = "";
        for (int i = 0; i <len; ++i){
            ret += (char) buff[i];
        }
        return ret;
    }
    public void initPort(){
        //Disable Echo in this Port
        if (SendATCommand("ATE0")) {
            logger.debug("Disable Echo for Port :{}",PortNumber);
        }else{
            logger.debug("Cannot Disable Echo for Port :{}",PortNumber);
        };
        serialHandler listener = new serialHandler(this.PortNumber);
        this.comPort.addDataListener(listener);
        this.IMEI = decodeImei(this.SendCommand("AT+CGSN"));
        logger.debug("IMEI PORT{} {} = {}",PortNumber,comPort.getDescriptivePortName(),IMEI);
    }

    public void closePort() {
        //Disable Echo in this Port
        this.comPort.removeDataListener();
        this.comPort.closePort();
        logger.debug("Close",PortNumber);
    }
    public String  decodeImei(String string){
        String outStr = string.replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("OK", "");
        return outStr;
    }



    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        UartPort that = (UartPort) o;
        if (that.IMEI == null) return false;
        if (Objects.equals(this.IMEI,that.IMEI)) return true;
        return true;
    }

}
