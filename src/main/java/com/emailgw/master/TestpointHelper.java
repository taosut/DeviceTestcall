package com.emailgw.master;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class TestpointHelper {


    public String  decodeImei(String string){
        String outStr = string.replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("OK", "");
        return outStr;
    }

    public String convertBuffer(byte[]buff, int len){
        String ret = "";
        for (int i = 0; i <len; ++i){
            ret += (char) buff[i];
        }
        return ret;
    }

}
