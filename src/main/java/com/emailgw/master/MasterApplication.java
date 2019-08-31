package com.emailgw.master;

import com.emailgw.master.config.ApplicationProperties;
import com.emailgw.master.config.DefaultProfileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetAddress;
import java.net.UnknownHostException;

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties({ApplicationProperties.class})
public class MasterApplication {
    private static final Logger log = LoggerFactory.getLogger(MasterApplication.class);
    private final Environment env;
    public MasterApplication(Environment env) {
        this.env = env;
    }
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MasterApplication.class);
//        serialPort serialPort1 = new serialPort(1);
//        serialPort serialPort2 = new serialPort(2);
//        serialPort serialPort3 = new serialPort(3);
//        serialPort serialPort0 = new serialPort(0);
        DefaultProfileUtil.addDefaultProfile(app);
//        app.run(args);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        String serverPort = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (StringUtils.isBlank(contextPath)) {
            contextPath = "/";
        }
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\t{}://localhost:{}{}\n\t" +
                        "External: \t{}://{}:{}{}\n\t" +
                        "Profile(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                protocol,
                serverPort,
                contextPath,
                protocol,
                hostAddress,
                serverPort,
                contextPath,
                env.getActiveProfiles());
    }



}
