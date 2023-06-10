package com.graphy.emailSender;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


@Service
@Slf4j
public class EmailSenderService {

    private static final String CSV_PATH = "email_log.csv";
    private final String subject = "Your Subject";
    private final String message = "Your Email Message";
    private static final int PAGE_SIZE = 100; // Number of users to fetch per page

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    public void startSendingEmails() {
        int pageNumber = 0;
        Page<User> userPage = userRepository.findAll(PageRequest.of(pageNumber, PAGE_SIZE));
        while (!userPage.isEmpty()) {
            userPage.stream().forEach(user->sendMailToUser(user));
            pageNumber++;
            userPage = userRepository.findAll(PageRequest.of(pageNumber, PAGE_SIZE));
        }
    }

    private void sendMailToUser(User user) {
        try {
            emailService.sendEmail(user.getEmail(), subject, message);
            writeToCSV(user.getEmail());
            System.out.println("Email has been sent to: " + user.getEmail());
        } catch (MessagingException e) {
            System.out.println("Email sending failed to " + user.getEmail());
            log.info(e.getMessage());
        }
    }

    private void writeToCSV(String emailid) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_PATH, true))) {
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            writer.println(emailid + "- Time:" + time);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
