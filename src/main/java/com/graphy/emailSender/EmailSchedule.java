package com.graphy.emailSender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EmailSchedule {
    @Autowired
    private EmailSenderService emailSenderService;

    @Scheduled(fixedRate = 21600000)
    public void sendEmails() {
        emailSenderService.startSendingEmails();
    }
}
