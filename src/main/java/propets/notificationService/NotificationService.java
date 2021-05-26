package propets.notificationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import propets.model.Email;


@Component
@RefreshScope
public class NotificationService {
	
	private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
	
	@Autowired
    JavaMailSender emailSender;
	
	@KafkaListener(topics = "${emailTopic}", groupId = "foo")
	public void sendEmail(Email email) {
		logger.info("email received from kafka: " + email);
		SimpleMailMessage simpleMailMessage =constructEmail(email.getSubject(),email.getBody(),email.getEmailAdress());
		emailSender.send(simpleMailMessage);
	}
	
	private SimpleMailMessage constructEmail(String subject, String body, String email) {
	    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
	    simpleMailMessage.setSubject(subject);
	    simpleMailMessage.setText(body);
	    simpleMailMessage.setTo(email);
	    return simpleMailMessage;
	}
	
	public JavaMailSender getEmailSender() {
		return emailSender;
	}
	
	public void setEmailSender(JavaMailSender emailSender) {
		this.emailSender = emailSender;
	}
}
