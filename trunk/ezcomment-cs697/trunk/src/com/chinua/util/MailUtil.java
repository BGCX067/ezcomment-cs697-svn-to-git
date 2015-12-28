package com.chinua.util;

import java.util.StringTokenizer;
import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.Multipart;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

public class MailUtil {
	public String sendMail(String email,String fname, String uname, String pass, int type){
		String text="",subject="";
		if(type==0)
			subject="Welcome to ezComment";
		if(type==1)
			subject="ezComment Password Reminder";
		if(type==2)
			subject="ezComment Password Changed";
		text="<html><body style='font-family:Arial;'><p><img src='http://ezcomment.appspot.com/resources/images/logo1.png' /></p>"
 +"<p>"+subject+"</p><p><br />Dear "+fname+",</p><p>"
+"Thank you for choosing to use ezComment. This application helps you create and manage commentBoxes on your website without writing a single line of code."
+"</p><p>Your username is: "+uname+" and your password is: "+pass+"</p>"
+"<p><a href='http://ezcomment.appspot.com'>Click here to login</a></p>"
+"</body></html>";
		if(type==0)
			pass=this.sendMsg2("iloabachie@gmail.com", "Someone Registered on ezComment", fname+" just registered as "+uname);
		return this.sendMsg(email, subject, text);
	}
	//Sends HTML mails
	public String sendMsg(String strTo,String strSubject,String strBody){
		String strCallResult="";
		try {
			//Extract out the To, Subject and Body of the Email to be sent
			//String strTo = req.getParameter("email_to");
			//String strSubject = req.getParameter("email_subject");
			//String strBody = req.getParameter("email_body");
			//Do validations here. Only basic ones i.e. cannot be null/empty
			//Currently only checking the To Email field
			if (strTo == null){
				strCallResult="To field cannot be empty.";
				return strCallResult;
			}
			//Trim the stuff
			strTo = strTo.trim();
			if (strTo.length() == 0){
				strCallResult="To field cannot be empty.";
				return strCallResult;
			}
			//Call the GAEJ Email Service
			Properties props = new Properties();
			Session session = Session.getDefaultInstance(props, null);
			Message msg = new MimeMessage(session);
			// Create a default MimeMessage object.
	         MimeMessage message = new MimeMessage(session);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress("ezComment<iloabachie@gmail.com>"));

	         // Set To: header field of the header.
	         message.addRecipient(Message.RecipientType.TO,
	                                  new InternetAddress(strTo));

	         // Set Subject: header field
	         message.setSubject(strSubject);

	         // Send the actual HTML message, as big as you like
	         message.setContent(strBody,"text/html" );

	         // Send message
	         Transport.send(message);
	         System.out.println("Sent message successfully....");
			System.out.println("=-=-=Sent mail "+strSubject+" to "+strTo);
			strCallResult = "success";
		} catch (AddressException e) {
			System.out.println("An error===AddressException");
			strCallResult="Invaild Email Address";
			e.printStackTrace();
		} catch (MessagingException e) {
			strCallResult="Error sending mail";
			System.out.println("An error===MessagingException");
			e.printStackTrace();
		} catch (Exception e) {
			strCallResult="Error sending mail";
			System.out.println("A serious error===General");
			e.printStackTrace();
		}
		return strCallResult;
	}
	public String sendMsg2(String strTo,String strSubject,String strBody){
		String strCallResult="";
		try {
			//Extract out the To, Subject and Body of the Email to be sent
			//String strTo = req.getParameter("email_to");
			//String strSubject = req.getParameter("email_subject");
			//String strBody = req.getParameter("email_body");
			//Do validations here. Only basic ones i.e. cannot be null/empty
			//Currently only checking the To Email field
			if (strTo == null){
				strCallResult="To field cannot be empty.";
				return strCallResult;
			}
			//Trim the stuff
			strTo = strTo.trim();
			if (strTo.length() == 0){
				strCallResult="To field cannot be empty.";
				return strCallResult;
			}
			//Call the GAEJ Email Service
			Properties props = new Properties();
			Session session = Session.getDefaultInstance(props, null);
			Message msg = new MimeMessage(session);
			
			msg.setHeader("X-Mailer", "Stedy Strategics WebMailer 1.0");
            msg.setHeader("X-Priority", "3");
            msg.setHeader("Content-Type", "text/html"); 
            
            MimeBodyPart msgBody = new MimeBodyPart();
            msgBody.setDataHandler(new DataHandler(msg, "text/html"));
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(msgBody);
            msg.setContent(mp,"text/html");
            //msg.setContentType("text/html");
            //InternetAddress[1] add=new InternetAddress();
			msg.setFrom(new InternetAddress("ezComment<iloabachie@gmail.com>"));
			//msg.setReplyTo(.);
			msg.addRecipient(Message.RecipientType.TO,
			new InternetAddress(strTo));
			msg.setSubject(strSubject);
			msg.setText(strBody);
			Transport.send(msg);
			System.out.println("=-=-=Sent mail "+strSubject+" to "+strTo);
			strCallResult = "success";
		} catch (AddressException e) {
			System.out.println("An error===AddressException");
			strCallResult="Invaild Email Address";
			e.printStackTrace();
		} catch (MessagingException e) {
			strCallResult="Error sending mail";
			System.out.println("An error===MessagingException");
			e.printStackTrace();
		} catch (Exception e) {
			strCallResult="Error sending mail";
			System.out.println("A serious error===General");
			e.printStackTrace();
		}
		return strCallResult;
	}
	
	public static boolean isValidEmailAddress(String emailAddress){
		return true;
	}
	//look at this code
	public static boolean isValidEmailAddress(String emailAddress, int i)
	  {
	    // a null string is invalid
	    if ( emailAddress == null )
	      return false;
	    //something is wrong with this code
	    //if ( emailAddress != null ) return true;
	    // a string without a "@" is an invalid email address
	    if ( emailAddress.indexOf("@") < 0 )
	      return false;

	    // a string without a "."  is an invalid email address
	    if ( emailAddress.indexOf(".") < 0 )
	      return false;

	    if ( lastEmailFieldTwoCharsOrMore(emailAddress) == false )
	     return false;

	    try
	    {
	      InternetAddress internetAddress = new InternetAddress(emailAddress);
	      System.out.println("=====valid email address::"+emailAddress);
	      return true;
	      
	    }
	    catch (AddressException ae)
	    {
		  // log exception
	      return false;
	    }
	  }
	/**
	   * Returns true if the last email field (i.e., the country code, or something
	   * like .com, .biz, .cc, etc.) is two chars or more in length, which it really
	   * must be to be legal.
	   */
	  private static boolean lastEmailFieldTwoCharsOrMore(String emailAddress)
	  {
		if (emailAddress == null) return false;
	    StringTokenizer st = new StringTokenizer(emailAddress,".");
	    String lastToken = null;
	    while ( st.hasMoreTokens() )
	    {
	      lastToken = st.nextToken();
	    }

	    if ( lastToken.length() >= 2 )
	    {
	      return true;
	    }
	    else
	    {
	      return false;
	    }
	  }




}
