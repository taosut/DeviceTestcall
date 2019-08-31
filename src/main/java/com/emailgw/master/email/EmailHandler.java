package com.emailgw.master.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

@Component
@Configuration
public class EmailHandler {
    private static final Logger logger = LoggerFactory.getLogger(EmailHandler.class);
    @Value("${EmailGW.EmailHandler.pathEmailList}")
    private String pathEmailList;
    public ArrayList<String> listMail;

    @Value("${EmailGW.EmailHandler.host}")
    private String host;

    @Value("${EmailGW.EmailHandler.user}")
    private String user;

    @Value("${EmailGW.EmailHandler.pass}")
    private String pass;


    public boolean pushToList(String title){
        if(!this.listMail.contains(title)) {
            listMail.add(title);
            try (FileWriter writer = new FileWriter(pathEmailList, true);
                 BufferedWriter bw = new BufferedWriter(writer)) {
                bw.newLine();
                bw.write(title);
                logger.info("New title {} insert OK",title);
                return true;
            } catch (IOException e) {
                System.err.format("IOException: %s%n", e);
            }
        }

        return false;
    }
    public void checkEmail(){
        try {
            //create properties field
            Properties properties = new Properties();

            properties.put("mail.pop3.host", host);
            properties.put("mail.pop3.port", "995");
            properties.put("mail.pop3.starttls.enable", "true");
            Session emailSession = Session.getDefaultInstance(properties);

            //create the POP3 store object and connect with the pop server
            Store store = emailSession.getStore("pop3s");

            store.connect(host, user, pass);

            //create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            // retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.getMessages();
            logger.warn("Total = {}",messages.length);
            for (int i = 0, n = messages.length; i < n; i++) {
                Message message = messages[i];
                if(this.pushToList(message.getSubject())){
                    logger.info("Has new Email, title = {}",message.getSubject());
                }
            }

            //close the store and folder objects
            emailFolder.close(false);
            store.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @PostConstruct
    public void init() {
        ArrayList<String> result = new ArrayList<>();
        try (Scanner s = new Scanner(new FileReader(pathEmailList))) {
            while (s.hasNext()) {
                result.add(s.nextLine());
            }
            logger.info("Load Email List Completed!!!!");
            this.listMail = result;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
