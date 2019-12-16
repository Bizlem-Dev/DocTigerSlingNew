package com.service.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.activation.*;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.sling.api.SlingHttpServletResponse;

public class SendEmail {
	static {
		MailcapCommandMap mcap = new MailcapCommandMap(); mcap.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain"); mcap.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html"); mcap.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml"); mcap.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed; x-java-fallback-entry=true"); mcap.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
		CommandMap.setDefaultCommandMap(mcap);
	}
	
	public static void main(String[] args) {
		/*SendEmail se= new SendEmail();
		
		se.sendMail("anagha.bizlem@gmail.com", "Task rejected", "The Name: is rejected by ", "anagha.bizlem@gmail.com", "Anagha@123");*/
	 }

	 String fName="";
	 public String sendMail(String to, String subject, String body, String fromId, String fromPass, SlingHttpServletResponse rep){
		 Thread.currentThread().setContextClassLoader( getClass().getClassLoader() );
		 PrintWriter out;
		try {
			out = rep.getWriter();
		final String fromid1=fromId;
		final String frompass1=fromPass;
		 Properties props = new Properties();
		 props.setProperty("mail.transport.protocol", "smtp");  
		 props.setProperty("mail.host", "smtp.gmail.com");
		 props.put("mail.smtp.auth", "true");
		 props.put("mail.smtp.port", "465");  
		 props.put("mail.debug", "true");
		 props.put("mail.smtp.socketFactory.port", "465");
		 props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		 props.put("mail.smtp.socketFactory.fallback", "false");
		 
		 Authenticator authenticator = new Authenticator () {
			 public PasswordAuthentication getPasswordAuthentication(){
				 return new PasswordAuthentication(fromid1,frompass1);
				 //userid and password for "from" email address
				 }
			 };
			 // Get the Session object.
			 Session session = Session.getInstance(props, authenticator);
			/* out.println("Authentcator: "+authenticator);
			 out.println("Props: "+props);
			 out.println("Session: "+session);*/
  
		  try {
		     // Create a default MimeMessage object.
		     Message message = new MimeMessage(session);

		     // Set From: header field of the header.
		     message.setFrom(new InternetAddress(fromId));
		     //out.println("fromId: "+fromId);
		     
		     // Set To: header field of the header.
		     message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
		    // out.println("to: "+to);
		     // Set Subject: header field
		     message.setSubject(subject);
		    // out.println("subject: "+subject);
		     // Create the message part
		     // Now set the actual message
		     message.setText(body);
		    // out.println("body: "+body);
		     /*String data= new Replacedata_1().callFile(filePath);
		     out.println("data: "+data);
		     */
		   
		     
		     // Send message
		     try {
		     Transport.send(message);
		     //out.println("3");
		     }catch (Exception e) {
				// TODO: handle exception
		    	 e.toString();
		    	//out.println(e.toString());
			}
		     
		   // out.println("message: "+message);
		        
		     System.out.println("Sent message successfully....");
  
		  } catch (Exception e) {
		     //throw new RuntimeException(e);
		    // out.println("error: "+e.getMessage());
		     e.printStackTrace();
		  }} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		 return "Success";
		 }
	 
	 
	 }




