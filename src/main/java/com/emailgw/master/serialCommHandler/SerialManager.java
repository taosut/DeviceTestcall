package com.emailgw.master.serialCommHandler;

import com.fazecast.jSerialComm.SerialPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.Objects;


@Component
@Configuration
public class SerialManager {
    public ArrayList<UartPort> uartPorts;
    private static final Logger logger = LoggerFactory.getLogger(SerialManager.class);
    private int totalPort;
    public boolean initialing;

    public SerialManager(){
       reInit();
    }
    private Boolean addToList(UartPort uartPort){
            for (UartPort item:this.uartPorts){
                if(Objects.equals(item.IMEI,uartPort.IMEI)){
                    return false;
                }
            }
        uartPorts.add(uartPort);
        return true;
    }
    public void reInit(){
        logger.error("Reinit Port!!!!");
        uartPorts = new ArrayList<>();
        initialing = false;
        SerialPort[] listport =  SerialPort.getCommPorts();
        this.totalPort = listport.length;
        for (int i = 0;i<listport.length;i++){
            UartPort uartPort = new UartPort(i,listport[i]);
            if(uartPort.IMEI!=null) {
                if(!addToList(uartPort)){
                    uartPort.closePort();
                }
            }
        }
        logger.warn("Total ADD {}",uartPorts.size());
        initialing = true;
        logger.info("Init Complete");
    }

    @Scheduled(fixedRate = 3600, initialDelay = 18000) // 3600 seconds
    public void healcheckPort(){
        SerialPort[] listport =  SerialPort.getCommPorts();
        if (totalPort != listport.length){
            reInit();
            return;
        }
        for(UartPort item: uartPorts){
            if (!item.comPort.isOpen()){
                reInit();
                return;
            }
        }
    }

}
