package social.com.app.servlet;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ResourceBundle;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.servlet.ServletException;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;

import com.service.ParseSlingData;
import com.service.SOAPCall;
import com.service.impl.FreeTrialandCart;
import com.service.impl.ParseSlingDataImpl;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/dDependency_core" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class DynamicDependency_core extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	ResourceBundle bundle = ResourceBundle.getBundle("config");
	static ResourceBundle bundleststic = ResourceBundle.getBundle("config");
//http://35.200.169.114:8082/portal/servlet/service/dDependency_core
	//@Reference
	// private ParseSlingData parseSlingData;
	ParseSlingData parseSlingData = new ParseSlingDataImpl();

	Session session = null;

	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		out.println("in DD");
	}

	@Override
	protected void doPost(SlingHttpServletRequest req, SlingHttpServletResponse rep)
			throws ServletException, IOException {
		PrintWriter out = rep.getWriter();

		// out.println("in DD");
		rep.setCharacterEncoding("UTF-8");
		rep.setHeader("Content-Type", "text/html,charset=UTF-8");
		try {
			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));

			BufferedInputStream bis = new BufferedInputStream(req.getInputStream());
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			int result1 = bis.read();
			while (result1 != -1) {
				buf.write((byte) result1);
				result1 = bis.read();
			}
			// StandardCharsets.UTF_8.name() > JDK 7
			String res = buf.toString("UTF-8");
			// out.println("res: " + res);
			JSONObject resultobj = new JSONObject(res);
			String email = resultobj.getString("Email");
			String group = resultobj.getString("group");
			String EventId = resultobj.getString("EventId");
			String EventName = resultobj.getString("EventName");
			String SFObject = resultobj.getString("SFObject");
			String Primery_key = resultobj.getString("Primery_key");
			String Primery_key_value = resultobj.getString("Primery_key_value");
			JSONObject SFData = resultobj.getJSONObject("SFData");
			JSONArray attjs = new JSONArray();
			// out.println("SFData "+SFData);
			// if(EventName.equals("SPA Event")) {
			// out.print("http://35.188.233.86:8080/Template1_21-Sep-2018_08-23-35-818.pdf,http://35.188.233.86:8080/LTL_Fixed_Return_annex_Finalized_14_Aug_2018_24-Oct-2018_15-16-46-128.pdf,"
			// +
			// "http://35.188.233.86:8080/3_Majestine_TIGC_Membership_24-Oct-2018_15-16-41-634.pdf,"
			// +
			// "http://35.188.233.86:8080/Artesia_Hotel_28_nights_free_stay_promo_2018_24-Oct-2018_15-04-28-225.pdf");
			// }else {
			// out.println("SFData "+SFData);
			// out.println("email" + email + "EventId" + EventId + "EventName" + EventName +
			// "SFObject" + SFObject + "Primery_key" + Primery_key + "Primery_key_value" +
			// Primery_key_value);

			// out.println("New_SFData "+New_SFData_main);

			// call method to check validity of document generation
			// ===============================================================
			
			  FreeTrialandCart cart= new FreeTrialandCart(); 
			  String freetrialstatus=cart.checkfreetrial(email);
			  out.println("freetrialstatus: "+freetrialstatus);
			 
			Node doctigerAdvNode =parseSlingData.validationmethod( freetrialstatus, email,group,
			session , rep) ;

			 if(doctigerAdvNode!=null) {

			// ==============================================================================================================================

			 out.println("before eventdata" +doctigerAdvNode);
			String result = parseSlingData.getEventdata(email, group,EventId, EventName, rep,session);
			 out.println("event result " + result);
			JSONArray arr = new JSONArray(result);
			JSONObject CTrecord = null;

			for (int ii = 0; ii < arr.length(); ii++) {
				CTrecord = arr.getJSONObject(ii);
				// out.println(CTrecord);
				String docurl = "";
				if (CTrecord.has("AttachedTempType")) {
					String Templatename = CTrecord.getString("AttachedTempName");
					// out.println(Templatename);
					// call method to get template info
					String AttachtempalteType = CTrecord.getString("AttachedTempType");

					docurl = parseSlingData.getADVandTemplatedata(email, group,Templatename, AttachtempalteType, SFObject,
							Primery_key, Primery_key_value, SFData, rep, session);
					// String docurl="";
					out.println(docurl);

					if (docurl != null && docurl != "") {
						int o = docurl.lastIndexOf("/");
						String generatedfile = docurl.substring(o + 1, docurl.length());
				        out.println("comma separated ooooo: " +generatedfile);


						try {
                        if(! generatedfile.equalsIgnoreCase("null") &&   generatedfile!=null  ) {
							attjs.put(generatedfile);
							//out.println("attjs 1 "+attjs);
                             }
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				}

//				out.println("mail process");
				if (CTrecord.has("CT") && CTrecord.getString("CT").contains("Mail")) {
					String MailTemplate = CTrecord.getString("MailTempName");
					// call method to get template info
//					 out.println("MailTemplate " + MailTemplate);

					JSONObject maildata = parseSlingData.getMailTemplatedata(email,group, MailTemplate, SFObject, Primery_key,
							Primery_key_value, SFData, rep,session); // ,session
//					out.println("SFData= " + SFData);
					JSONArray resar = SFData.getJSONArray("response");
//					 out.println("resar " + resar);


					String attachfilenames="";
					String attachmentPath=bundleststic.getString("DocGenServerPDFFilePath");
					String fromId="";
					String fromPass="";
					String to="";
					String cc="";
					String bcc="";
					JSONArray tojs=new JSONArray();
					JSONArray ccjs=new JSONArray();
					JSONArray bccjs=new JSONArray();
					String attachfileurl = "";
					JSONArray attachurlarr = new JSONArray();
					
					try {
					for(int k=0;k<resar.length();k++) {
						JSONObject jsobj=resar.getJSONObject(k);
						if(jsobj.has("Email")) {
//							toemail=jsobj.getString("Email");
							
							to=jsobj.getString("Email");
							
							 String[] array = to.split(",");
						     System.out.println("comma separated String: " + array);

					
						     for(int i=0;i<array.length;i++) {
						     	
						    	 tojs.put(array[i]);
						     }
//							tojs=separateComma(to);
//							out.println("toemail= " + to);
//							out.println("tojs= " + tojs);
						}
						if(jsobj.has("fromId")) {
							fromId=jsobj.getString("fromId");
//							out.println("fromId= " + fromId);
						}else {
							
							fromId="doctigertest@gmail.com";
						}
						if(jsobj.has("fromPass")) {
							fromPass=jsobj.getString("fromPass");
//							out.println("fromPass= " + fromPass);
						}else 
						{
							fromPass=	"doctiger@123";
						}
				
//						if(jsobj.has("To")) {
//						
////							out.println("fromPass= " + fromPass);
//						}
						if(jsobj.has("Cc")) {
							cc=jsobj.getString("Cc");
							
							 String[] array = cc.split(",");
						     System.out.println("comma separated String: " + array);

					
						     for(int i=0;i<array.length;i++) {
						     	
						    	 ccjs.put(array[i]);
						     }
//							ccjs=separateComma(cc);
//							out.println("ccjs= " + ccjs);
						}
						if(jsobj.has("Bcc")) {
							bcc=jsobj.getString("Bcc");
//							bccjs=separateComma(bcc);
							 String[] array = bcc.split(",");
						     System.out.println("comma separated String: " + array);

					
						     for(int i=0;i<array.length;i++) {
						     	
						    	 bccjs.put(array[i]);
						     }
							
//							out.println("bccjs= " + bccjs);
						}
						if (jsobj.has("attachmentScorpio")&& jsobj.getString("attachmentScorpio") !="") {
							attachfileurl = jsobj.getString("attachmentScorpio");
//					out.println("comma separated len: " + attachfilenames.length());
							if (attachfileurl.length() > 0) {
								String[] array = attachfileurl.split(",");
								System.out.println("comma separated String: " + array);

								for (int i = 0; i < array.length; i++) {
									try {
									attachurlarr.put(array[i]);
									String fileurl=array[i];
									int o = fileurl.lastIndexOf("/");
									String filename = fileurl.substring(o + 1, fileurl.length());
									attjs.put(filename);
									out.println("attjs - "+attjs);
									}catch (Exception e) {
										// TODO: handle exception
									}
								}
//					attjs=separateComma(attachfilenames);
					out.println("attachmentScorpio= " + attachurlarr);
							}
						}
						
						if(jsobj.has("attachments")) {
							attachfilenames=jsobj.getString("attachments");
//							out.println("comma separated len: " + attachfilenames.length());
							if(attachfilenames.length()>0) {
							 String[] array = attachfilenames.split(",");
						     System.out.println("comma separated String: " + array);

					
						     for(int i=0;i<array.length;i++) {
						     	
						    	 attjs.put(array[i]);
									out.println("attjs 2 "+attjs);

						     }
//							attjs=separateComma(attachfilenames);
//							out.println("attjs= " + attjs);
							}
						}
						if(jsobj.has("attachmentPath") && jsobj.get("attachmentPath") !="") {
							attachmentPath=jsobj.getString("attachmentPath");
							
//							out.println("fromPass= " + fromPass);
						}
						
					}
					}catch (Exception e) {
						// TODO: handle exception
					}
					
					
//	try {
//						/* {"attachmenturl":["https://dev.bluealgo.com:8082/scorpioexcel/TonnageData.xls","https://dev.bluealgo.com:8082/scorpioexcel/SpotData.xls","https://dev.bluealgo.com:8082/scorpioexcel/TimeCharterReportsData.xls","https://dev.bluealgo.com:8082/scorpioexcel/BrokerTcRate.xls"]} */
//						if(attachurlarr.length()>0) {
//							JSONObject sendurl = new JSONObject();
//							String saveattachmentserv = "http://" + bundleststic.getString("DocGenServerIP")
//							+ ":8080/NewMail/SaveAttchmentFileServlet";
//							sendurl.put("attachmenturl", attachurlarr);
//							out.println("saveattachmentserv= "+saveattachmentserv);
//							out.println("sendurl= "+sendurl);
//					String resmethod = new SOAPCall().callAttachmentJSon(saveattachmentserv, sendurl);
//					out.println("resmethod= "+resmethod);
//						}
//					}catch (Exception e) {
//						// TODO: handle exception
//					}
	
	
//					out.println("maildata " + maildata);
					// status= new Report().sendMail(maildata, docurl,"", rep);
					JSONObject sendobj = new JSONObject();
					/* {"to":["tejal.jabade@bizlem.com"],"fromId":"doctigertest@gmail.com","fromPass":"doctiger@123","subject":"Testing12 Send Mail From MailTemlate","body":
					"<p>Hello  Tejal ,<\/p>\n\n<p>How are you?<\/p>\n\n <p><strong>This is test mail sent from DocTiger.<\/strong><\/p>\n\n <p><u>hiiiiiiiiii<\/u<\/p>\n\n <p> 1 <\/p>\n\n <p>Thanks<\/p>\n\n<p>&nbsp;<\/p>\n","cc":[ "anagha.rane@bizlem.com"],"bcc":["tejal.jabade@bizlem.com"],"attachments":[],"attachmentPath":""} */
							
					if(attachurlarr.length()>0) {
						sendobj.put("attachmentScorpio",attachurlarr);
						out.println("saveattachmentserv= "+attachurlarr);
					}
					
//					sendobj.put("to", maildata.get("to"));
					sendobj.put("to",tojs);
//					sendobj.put("fromId", "doctigertest@gmail.com");
//					sendobj.put("fromPass", "doctiger@123");
					sendobj.put("fromId", fromId);
					sendobj.put("fromPass",fromPass);
					sendobj.put("subject", maildata.get("subject"));
					sendobj.put("cc",ccjs);
					sendobj.put("bcc",bccjs);
					
					sendobj.put("attachmentPath",attachmentPath);
					out.println("attjs 3 "+attjs);

					sendobj.put("attachments",attjs);
					
//					out.println("newbody old= "+maildata.get("body").toString());
//					String newbody=maildata.get("body").toString().replaceAll("<p>", "").replaceAll("</p>", "\r\n");
//					out.println("newbody = "+newbody);
					
					sendobj.put("body",maildata.get("body")); //+ "\r\n" + docurl
					if (maildata.has("attachurl")) {
						sendobj.put("attachFilePath", maildata.get("attachurl"));
					}
					out.println("sendobj :: " + sendobj);
//					 out.println("Mailsendobj "+sendobj);
					///home/ubuntu/apache-tomcat-8.5.31/webapps/ROOT
					
					
//					out.println("sendMailUrl  " + sendMailUrl);
					
				
//			SendMailServletUrl
	
//					String sendMailUrl = "http://" + bundleststic.getString("DocGenServerIP")
//							+ ":8085/NewMailDev/getFileAttachServlet";
					String sendMailUrl =  bundleststic.getString("SendMailServletUrl");
//				    					status = 
					int st = new SOAPCall().callPostJSonModified(sendMailUrl, sendobj);
			
					out.println("mail Status " + st);
				}

				// out.println("mail process");
				// if(CTrecord.has("CT") && CTrecord.getString("CT").contains("Mail")) {
				// String MailTemplate = CTrecord.getString("MailTempName");
				// // call method to get template info
				// //out.println("MailTemplate " + MailTemplate);
				//
				// JSONObject maildata = parseSlingData.getMailTemplatedata(email, MailTemplate,
				// SFObject, Primery_key,
				// Primery_key_value, SFData, rep);
				// //out.println("maildata "+maildata);
				// // status= new Report().sendMail(maildata, docurl,"", rep);
				// JSONObject sendobj = new JSONObject();
				// sendobj.put("to", maildata.get("to"));
				// sendobj.put("fromId", "doctigertest@gmail.com");
				// sendobj.put("fromPass", "doctiger@123");
				// sendobj.put("subject", maildata.get("subject"));
				// sendobj.put("body", maildata.get("body") + "\r\n" + docurl);
				// if (maildata.has("attachurl")) {
				// sendobj.put("attachFilePath", maildata.get("attachurl"));
				// }
				// // out.println("sendobj " + sendobj);
				// // out.println("Mailsendobj "+sendobj);
				// String sendMailUrl =
				// "http://35.188.233.86:8080/NewMail/getFileAttachServlet";
				//
				// status = new SOAPCall().callPostJSon(sendMailUrl, sendobj);
				// //.println("mail Status "+status);
				// }

				// if(CTrecord.has("CT") && CTrecord.getString("CT").contains("SMS")) {
				// String SMSTemplate = CTrecord.getString("smsTempName");
				//
				// JSONObject SMSdata = parseSlingData.getSMSTemplatedata(email, SMSTemplate,
				// SFObject, Primery_key,
				// Primery_key_value, SFData, rep);
				// // out.println("SMSdata "+SMSdata);
				// String sendSMSUrl = "http://35.188.233.86:8080/NewMail/sendSMSServlet";
				// JSONObject sendobjMob = new JSONObject();
				// sendobjMob.put("to", SMSdata.get("to"));
				// //sendobjMob.put("message", "Your document has been sent.");
				// sendobjMob.put("message", SMSdata.getString("body"));
				// // out.println("SMSsendobjMob "+sendobjMob);
				// status = new SOAPCall().callPostJSon(sendSMSUrl, sendobjMob);
				// // out.println("sms status"+status);
				// }

				if (CTrecord.has("CT") && CTrecord.getString("CT").contains("Download")) {
				}

				// }
out.println("at end");
				// code to set counter on document
				// generation===================================================================
				String docgencounter = parseSlingData.updateDocGenCounter( email,  freetrialstatus,doctigerAdvNode, session ,rep);

				// ====================================================================================================================
			out.print("updated");
			}
			 out.println("Service Expired...");
			 }
		} catch (Exception e) {
			out.println(e.getMessage());
		}
	}

	public JSONArray separateComma(String list) {
		JSONArray js = new JSONArray();
		String[] array = list.split(",");
		System.out.println("comma separated String: " + array);

		JSONArray jsa = new JSONArray();
		for (int i = 0; i < array.length; i++) {

			jsa.put(array[i]);
		}

		return js;
	}

}
