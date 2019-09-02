package com.emailgw.master.command;

import com.emailgw.master.CallingSim.SimCardRepository;
import com.emailgw.master.email.EmailHandler;
import com.emailgw.master.serialCommHandler.SerialManager;
import com.emailgw.master.serialCommHandler.UartPort;
import com.fazecast.jSerialComm.SerialPort;
import jssc.SerialPortList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;



@Component
@Configuration
public class CommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(CommandHandler.class);

    @Autowired
    private SerialManager serialManager;

    public void makeCall(String imei, String Called){
        for (UartPort port: serialManager.uartPorts){
            if(Objects.equals(port.IMEI,imei)){
                port.SendUBCommand("ATD"+Called+";");
            }
        }
    }
    public void dropCall(String imei){
        for (UartPort port: serialManager.uartPorts){
            if(Objects.equals(port.IMEI,imei)){
                port.SendUBCommand("AT+CHUP");
            }
        }
    }

}
