package com.emailgw.master.web.rest;


import com.emailgw.master.CallingSim.SimCard;
import com.emailgw.master.CallingSim.SimCardRepository;
import com.emailgw.master.command.CommandHandler;
import com.emailgw.master.email.EmailHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping(value = "/test", method = RequestMethod.POST)
public class TestResources {
    private static final Logger logger = LoggerFactory.getLogger(TestResources.class);

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    EmailHandler emailHandler;

    @Autowired
    private SimCardRepository simCardRepository;

    @Autowired
    private CommandHandler commandHandler;

    @GetMapping("/sendEmail")
    public String test1() {
//        sendSimpleEmail.check("mail.viettel.com.vn","xxxx","tad_antifraud@viettel.com.vn","H@1lamgi");

        return emailHandler.listMail.toString();
    }

    @GetMapping("/addItem/{item}")
    public String additem(@PathVariable("item") String item) {
        emailHandler.checkEmail();
        return "";
    }
    @GetMapping("/addsimcard/{imei}/{msisdn}")
    public String additem2(@PathVariable("imei") String imei,@PathVariable("msisdn") String msisdn) {
        SimCard simCard = new SimCard(imei,msisdn);
        simCardRepository.save(simCard);
        return "";
    }

    @GetMapping("/makecall/{imei}/{msisdn}")
    public String mkcall(@PathVariable("imei") String imei,@PathVariable("msisdn") String msisdn) {
        commandHandler.makeCall(imei,msisdn);
        return "";
    }
}
