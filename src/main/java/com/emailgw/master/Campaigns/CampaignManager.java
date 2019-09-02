package com.emailgw.master.Campaigns;


import com.emailgw.master.command.CommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Component
@Configuration
public class CampaignManager {
    public ArrayList<Campaign> campaigns;
    @Autowired
    private CommandHandler commandHandler;

    public CampaignManager(){
        campaigns =  new ArrayList<Campaign>();
    }


    @Scheduled(fixedRate = 1000, initialDelay = 18000) // 1 minutes
    public void runCampaign(){
        for (Campaign item: campaigns){
            if(checkValidCampaign(item)){
                if(item.currentT == item.t1){
                    commandHandler.makeCall(item.imei,item.dest);
                }
                if(item.currentT == item.t2){
                    //Drop here
                    commandHandler.dropCall(item.imei);
                    item.currentT = 0;
                }
                item.currentT ++;

            }
        }
    }

    private boolean checkValidCampaign(Campaign campaign){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = LocalDateTime.parse(campaign.getStarttime(), formatter);
        LocalDateTime stopTime = LocalDateTime.parse(campaign.getStoptime(), formatter);
        if(now.isAfter(startTime) &now.isBefore(stopTime)){
            return true;
        }else{
            return false;
        }
    }
}
