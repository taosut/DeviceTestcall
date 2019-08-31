package com.emailgw.master.serialCommHandler;

import com.emailgw.master.TestpointHelper;
import com.emailgw.master.email.EmailHandler;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortMessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class serialHandler implements SerialPortMessageListener {
    private static final Logger logger = LoggerFactory.getLogger(EmailHandler.class);
    private int portnum;


    @Override
    public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_RECEIVED; }

    @Override
    public byte[] getMessageDelimiter() { return new byte[] { (byte)0x0D, (byte)0x0A }; }

    @Override
    public boolean delimiterIndicatesEndOfMessage() { return true; }

    @Override
    public void serialEvent(SerialPortEvent event)
    {
        byte[] delimitedMessage = event.getReceivedData();
        logger.warn("Received data from port: {} size {}, mess: {}: ",portnum,delimitedMessage.length,convertBuffer(delimitedMessage,delimitedMessage.length));

    }

    public serialHandler (int portnum){
        this.portnum = portnum;
    }

    public String convertBuffer(byte[]buff, int len){
        String ret = "";
        for (int i = 0; i <len; ++i){
            ret += (char) buff[i];
        }
        return ret.replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("OK", "");
    }

}
