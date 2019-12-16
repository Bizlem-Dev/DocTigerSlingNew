package com.service;




import java.util.Date;
import java.util.Properties;
 
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
 
public class SampleJavaMailCCAndBcc {
	
	private static final String MAIL_SERVER = "smtp";
	private static final String SMTP_HOST_NAME = "smtp.gmail.com";
    private static final int SMTP_HOST_PORT = 587;
    private static final String USER_NAME = "doctigertest";  // GMail user name (just the part before "@gmail.com")
    private static final String PASSWORD = "doctiger@123"; // GMail password
// "doctigertest@gmail.com"); doctiger@123
	
	public static void main(String[] args) {
	
        // Message info       
        String[] to = { "tejal.jabade@bizlem.com" ,"anagha.rane@bizlem.com"}; // list of recipient email addresses
        String[] cc={ "anagha.rane@bizlem.com" ,"vivek@bizlem.com"};
        String[] bcc={ "abhishek.tiwari@bizlem.com","tejal.jabade@bizlem.com" };
        String subject = "Java Send Mail Attachement Example";
        String body = "Welcome to Java Mail!<h1>Hello</h1>";		
		
		try {
			sendFromGMail(to, cc, bcc, subject, body);
			System.out.println("Email Sent....!");
		} catch (Exception ex) {
			System.out.println("Could not send email....!");
			ex.printStackTrace();
		}
	}
 
	private static void sendFromGMail(String[] to, String[] cc, String[] bcc, String subject, String body) {
 
//---------------------------------------------STEP 1---------------------------------------------
    	
    	System.out.println("\n 1st ===> Setup SMTP Mail Server Properties..!");
    	
    	// Get system properties
        Properties properties = System.getProperties();
        
        // Setup mail server        
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", SMTP_HOST_NAME);
        properties.put("mail.smtp.user", USER_NAME);
        properties.put("mail.smtp.password", PASSWORD);
        properties.put("mail.smtp.port", SMTP_HOST_PORT);
        properties.put("mail.smtp.auth", "true");
        
      //---------------------------------------------STEP 2---------------------------------------------
 
        
     	System.out.println("\n\n 2nd ===> Get Mail Session..");
        // Get the Session object.
     	
     	// creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USER_NAME, PASSWORD);
            }
        };
        
        Session session = Session.getInstance(properties, auth);        
 
        // Create a default MimeMessage object.
        MimeMessage message = new MimeMessage(session);
 
        try {
        	
        	//---------------------------------------------
        	
        	// Set From: header field of the header.
            message.setFrom(new InternetAddress(USER_NAME));
            
          //---------------------------------------------
            
            InternetAddress[] toAddress = new InternetAddress[to.length];
 
            // To get the array of toaddresses
            for( int i = 0; i < to.length; i++ ) {
                toAddress[i] = new InternetAddress(to[i]);
            }
            
            // Set To: header field of the header.
            for( int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }
            
            InternetAddress[] ccAddress = new InternetAddress[cc.length];
            
            // To get the array of ccaddresses
            for( int i = 0; i < cc.length; i++ ) {
                ccAddress[i] = new InternetAddress(cc[i]);
            }
            
            // Set cc: header field of the header.
            for( int i = 0; i < ccAddress.length; i++) {
                message.addRecipient(Message.RecipientType.CC, ccAddress[i]);
            }
            
            InternetAddress[] bccAddress = new InternetAddress[bcc.length];
            
            // To get the array of bccaddresses
            for( int i = 0; i < bcc.length; i++ ) {
                bccAddress[i] = new InternetAddress(bcc[i]);
            }
            
            // Set bcc: header field of the header.
            for( int i = 0; i < bccAddress.length; i++) {
                message.addRecipient(Message.RecipientType.BCC, bccAddress[i]);
            }
            
            // Set Subject: header field
            message.setSubject(subject);
                                  
            // Now set the date to actual message
            message.setSentDate(new Date());
            
            // Now set the actual message
            message.setContent(body,"text/html");         
            
            

//            if (attachments!=null){
//                for (int co=0; co<attachments.length; co++){
//                	message = new MimeBodyPart();
//                    File file = new File(appPath+attachments[co]);
//                    DataSource datasource = new FileDataSource(file);
//                    bodypart.setDataHandler(new DataHandler(datasource));
//                    bodypart.setFileName(file.getName());
//                    multipart.addBodyPart(bodypart);
//                }
//            }else {
//            	
//            }
            
//            message.setContent(multipart);
            
          //---------------------------------------------STEP 3---------------------------------------------
            
    		System.out.println("\n\n 3rd ===> Get Session and Send Mail");
            // Send message
            Transport transport = session.getTransport(MAIL_SERVER);
            transport.connect(SMTP_HOST_NAME, SMTP_HOST_PORT, USER_NAME, PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            System.out.println("Sent Message Successfully....");
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
        
      //---------------------------------------------------------------------------------------------------
        
    }
}
 