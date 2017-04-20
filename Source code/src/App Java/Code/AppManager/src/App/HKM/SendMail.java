package App.HKM;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author Crunchify.com
 * 
 */

public class SendMail {

	static Properties mailServerProperties;
	static Session getMailSession;
	static MimeMessage generateMailMessage;
	public static String emailBody;
	public static String[] CC = { "14521153@gm.uit.edu.vn", "14520340@gm.uit.edu.vn", "14520581@gm.uit.edu.vn",
			"14520358@gm.uit.edu.vn" };

	public SendMail(String error) throws AddressException, MessagingException {
		error = error.trim();
		emailBody = "<h1 style='text-align: center;'><span style='background-color: #800080; color: #ffffff;'><strong>_Warring_</strong></span></h1><p><span style='color: #000000;'><strong>Warring: Can't connect services mongodb</strong></span></p><p><span style='color: #000000;'><strong>Error: <span style='color: #ff0000;'>"
				+ error
				+ "</span></strong></span></p><p><span style='color: #000000;'><strong>by: <span style='background-color: #ffff00;'> _System_</span><br /></strong></span></p>";
		emailBody = emailBody.trim();
		generateAndSendEmail();
		System.out.println("\n\n ===> Your Java Program has just sent an Email successfully. Check your email..");
	}

	public static void generateAndSendEmail() throws AddressException, MessagingException {

		// Step1
		System.out.println("\n 1st ===> setup Mail Server Properties..");
		mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", "587");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");
		System.out.println("Mail Server Properties have been setup successfully..");

		// Step2
		System.out.println("\n\n 2nd ===> get Mail Session..");
		getMailSession = Session.getDefaultInstance(mailServerProperties, null);
		generateMailMessage = new MimeMessage(getMailSession);
		generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress("anhtuanpro312@gmail.com"));
		for (String CCs : CC) {
			generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(CCs));

		}
		generateMailMessage.setSubject("Warring By Server");
		generateMailMessage.setContent(emailBody, "text/html");
		System.out.println("Mail Session has been created successfully..");

		// Step3
		System.out.println("\n\n 3rd ===> Get Session and Send mail");
		Transport transport = getMailSession.getTransport("smtp");

		// Enter your correct gmail UserID and Password
		// if you have 2FA enabled then provide App Specific Password
		transport.connect("smtp.gmail.com", "vuongductuanktmt", "thaocute");
		transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
		transport.close();
	}
}