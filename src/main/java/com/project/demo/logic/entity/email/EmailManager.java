package com.project.demo.logic.entity.email;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Component;

@Component
public class EmailManager {
    private static final Dotenv dotenv = Dotenv.configure().filename("sendgrid.env").load();
    private static final String apiKey = dotenv.get("SENDGRID_API_KEY");
    private static final String fromEmail = dotenv.get("SENDGRID_FROM_EMAIL");

    public String sendEmail(String toEmail, String subject, String body) {
        Email from = new Email(fromEmail);
        Email to = new Email(toEmail);
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());

        } catch (Exception e) {
            e.printStackTrace();
            return "Error sending email: " + e.getMessage();
        }
        return "Email sent successfully to " + toEmail;
    }

}
