package com.emailgw.master.Campaigns;

import com.emailgw.master.command.CommandHandler;
import com.emailgw.master.serialCommHandler.SerialManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.management.timer.Timer;
import java.util.concurrent.TimeUnit;


public class Campaign {
    private String campCode;
    private String starttime;
    private String stoptime;
    public String imei;
    public String dest;
    public int t1;
    public int t2;
    public int currentT ;
    private boolean isRunning;

    public String getStarttime() {
        return starttime;
    }

    public String getStoptime() {
        return stoptime;
    }

    public Campaign(String campCode,String starttime,String stoptime, String imei, String dest,int t1, int t2){
        this.currentT = 0;
        this.campCode = campCode;
        this.starttime = starttime;
        this.stoptime = stoptime;
        this.imei = imei;
        this.dest = dest;
        this.t1 = t1;
        this.t2 = t2;

    }

//    @Scheduled(fixedRate = 5000, initialDelay = 18000) // 1 minutes
//    public void checkCommand(){
//        commandHandler.makeCall("356538047278501","84976643224");
//
//        try {
//            TimeUnit.SECONDS.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        commandHandler.dropCall("356538047278501");
//
//    }


}
