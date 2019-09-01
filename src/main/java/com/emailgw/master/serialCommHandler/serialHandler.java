package com.emailgw.master.serialCommHandler;

import com.emailgw.master.TestpointHelper;
import com.emailgw.master.email.EmailHandler;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortMessageListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
        String out = convertBuffer(delimitedMessage,delimitedMessage.length);
        logger.warn("Received data from port: {} size {}, mess: {}: ",portnum,delimitedMessage.length,out);
        logger.warn("UDSSD: {}",decodeUSSD2(out));
//        byte[] ussdHex = decodeUSSD2(out);
//        logger.warn("USSD hex = {}",toHexString(ussdHex));
//        try {
//            logger.warn("USSD String = {}",ucs2ToUTF8(ussdHex));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
    }

    public serialHandler (int portnum){
        logger.warn("Add listener to port {}",portnum);
        this.portnum = portnum;
    }

    public String convertBuffer(byte[]buff, int len){
        String ret = "";
        for (int i = 0; i <len; ++i){
            ret += (char) buff[i];
        }
        return ret.replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("OK", "");
    }

    public byte[] decodeUSSD(String input){
        String pattern = "^.*\"(.*?)\".*$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(input);


        if (m.find( )){
            logger.warn("group1 {}",m.group(1));
            logger.warn("group0 {}",m.group(0));
            return toByteArray( m.group(1));
        }else{
            return null;
        }
    }

    public String decodeUSSD2(String input){
        int first = input.indexOf('"');

        if(first >0){
            int last = input.indexOf('"',first +1);
            return input.substring(first,last) ;
        }
        logger.warn("could not decode USSD");
        return "";
    }

    public String ucs2ToUTF8(byte[] ucs2Bytes) throws UnsupportedEncodingException {

        String unicode = new String(ucs2Bytes, "UTF-16");

        String utf8 = new String(unicode.getBytes("UTF-8"), "Cp1252");

        return utf8;
    }

    public static byte[] toByteArray(String s) {
        return DatatypeConverter.parseHexBinary(s);
    }
    public static String toHexString(byte[] array) {
        return DatatypeConverter.printHexBinary(array);
    }
}
