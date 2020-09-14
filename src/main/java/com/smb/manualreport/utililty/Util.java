package com.smb.manualreport.utililty;

import com.alibaba.fastjson.JSON;
import com.smb.manualreport.bean.ApiReturn;
import com.smb.manualreport.bean.ElementLog;
import com.smb.manualreport.config.ApiConfig;
import com.smb.manualreport.service.DispatchInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Util {

    private static Logger logger = LoggerFactory.getLogger(Util.class);

    public static void mailReportFailAlarm(String notifyStr, ElementLog elementLog){
        List<String> toList = Arrays.asList(notifyStr.split(","));
        String from = "smartboss3621@gmail.com";
        String host = "smtp.gmail.com";
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", 587);
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");
        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("smartboss3621@gmail.com", "Csie3621");
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from, "SmartBOSS"));
            for (String to : toList) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            }
            message.setSubject("[ManualReportSystem] Call SmartBOSS API to Report Fail");
            message.setContent("<h1>Fail Record</h1>"
                    + "<p>Worker: " + elementLog.getWorker_code()
                    + "</p><p>Machine: " + elementLog.getMachine_code()
                    + "</p><p>Materail: " + elementLog.getElement_code()
                    + "</p><p>Finish Count: " + elementLog.getFinish_number()
                    + "</p><p>Start Time: " + elementLog.getStart_datetime()
                    + "</p><p>Finish Time: " + elementLog.getFinish_datetime()
                    +"</p>", "text/html;charset=utf-8");
            logger.info(">>> Sending...");
            Transport.send(message);
            logger.info(">>> Sent message successfully....");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void reportToSmartboss(String content, String apiUrl) throws Exception {
        String method = "smb/api/newElement";
//        try {
            URL restUrl = new URL(apiUrl + method);
            HttpURLConnection conn = (HttpURLConnection) restUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            PrintStream ps = new PrintStream(conn.getOutputStream());
            ps.print(content);
            ps.close();

            // 送出的動作
//            conn.getInputStream();
            BufferedReader br = new BufferedReader((new InputStreamReader(conn.getInputStream())));
            String line;
            while((line = br.readLine()) != null){
                ApiReturn ar = JSON.parseObject(line, ApiReturn.class);
                if(ar.getRetStatus().equals("success")){
                    logger.info(">>> Report to SmartBOSS api success!!!");
                } else if (ar.getRetStatus().equals("error")){
                    logger.error(ar.getRetMessage());
                }
            }
            br.close();
            conn.disconnect();
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error("!!!!! Please fix the miss report record !!!!!");
//        }
    }
}
