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
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;

import com.service.ParseSlingData;
import com.service.ParseSlingData_comment;
import com.service.SOAPCall;
import com.service.impl.FreeTrialandCart;
import com.service.impl.ParseSlingDataImpl;
import com.service.impl.ParseSlingDataImpl_comment;

//@SlingServlet(paths = "/servlet/service/dDependency_core_comment")
@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/dDependency_core_comment123" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class DynamicDependency_core_comment extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	ResourceBundle bundle = ResourceBundle.getBundle("config");
	static ResourceBundle bundleststic = ResourceBundle.getBundle("config");
	//@Reference
	// private ParseSlingData parseSlingData;
	ParseSlingData_comment parseSlingData = new ParseSlingDataImpl_comment();

	Session session = null;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		out.println("in DD123");
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
			out.println("res: " + res);
			JSONObject resultobj = new JSONObject(res);
			String email = resultobj.getString("Email");
			String group = resultobj.getString("group");
			String EventId = resultobj.getString("EventId");
			String EventName = resultobj.getString("EventName");
			String SFObject = resultobj.getString("SFObject");
			String Primery_key = resultobj.getString("Primery_key");
			String Primery_key_value = resultobj.getString("Primery_key_value");
			JSONObject SFData = resultobj.getJSONObject("SFData");

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
			/*
			 * FreeTrialandCart cart= new FreeTrialandCart(); String
			 * freetrialstatus=cart.checkfreetrial(email);
			 * out.println("freetrialstatus: "+freetrialstatus);
			 */
			// Node update_node =parseSlingData.validationmethod( freetrialstatus, email,
			// session , req, rep) ;

			// if(update_node!=null) {

			// ==============================================================================================================================

			// out.println("before eventdata");
			String result = parseSlingData.getEventdata(email, EventId, EventName, rep,session);
			out.println("event result ::" + result);
			JSONArray arr = new JSONArray(result);
			JSONObject CTrecord = null;
			out.println("arr.length()=" + arr.length());
			for (int ii = 0; ii < arr.length(); ii++) {
				CTrecord = arr.getJSONObject(ii);
				out.println(CTrecord);
				String docurl = "";
				if (CTrecord.has("AttachedTempType")) {
					String Templatename = CTrecord.getString("AttachedTempName");
					out.println(Templatename);
					// call method to get template info
					String AttachtempalteType = CTrecord.getString("AttachedTempType");
					out.println(AttachtempalteType);

					docurl = parseSlingData.getADVandTemplatedata(email, Templatename, AttachtempalteType, SFObject,
							Primery_key, Primery_key_value, SFData, rep,session);
					// String docurl="";
					out.println(docurl);
				}
				out.println("mail process");
				if (CTrecord.has("CT") && CTrecord.getString("CT").contains("Mail")) {
					String MailTemplate = CTrecord.getString("MailTempName");
					// call method to get template info
					// out.println("MailTemplate " + MailTemplate);

					JSONObject maildata = parseSlingData.getMailTemplatedata(email, MailTemplate, SFObject, Primery_key,
							Primery_key_value, SFData, rep,session);
					out.println("SFData= " + SFData);
					out.println("maildata " + maildata);
					// status= new Report().sendMail(maildata, docurl,"", rep);
					JSONObject sendobj = new JSONObject();
					sendobj.put("to", maildata.get("to"));
					sendobj.put("fromId", "doctigertest@gmail.com");
					sendobj.put("fromPass", "doctiger@123");
					sendobj.put("subject", maildata.get("subject"));
					sendobj.put("body", maildata.get("body") + "\r\n" + docurl);
					if (maildata.has("attachurl")) {
						sendobj.put("attachFilePath", maildata.get("attachurl"));
					}
					out.println("sendobj  " + sendobj);
					// out.println("Mailsendobj "+sendobj);
					String sendMailUrl = "http://" + bundleststic.getString("DocGenServerIP")
							+ ":8080/NewMail/getFileAttachServlet";

//				    					status = 
					int st = new SOAPCall().callPostJSonModified(sendMailUrl, sendobj);
					out.println("mail Status " + st);
				}

//				    					if(CTrecord.has("CT") && CTrecord.getString("CT").contains("SMS")) {
//				    						String SMSTemplate = CTrecord.getString("smsTempName");
//				
//				    						JSONObject SMSdata = parseSlingData.getSMSTemplatedata(email, SMSTemplate, SFObject, Primery_key,
//				    								Primery_key_value,  SFData, rep);
//				    				//		out.println("SMSdata  "+SMSdata);
//				    					String sendSMSUrl = "http://35.188.233.86:8080/NewMail/sendSMSServlet";
//				    					JSONObject sendobjMob = new JSONObject();
//				    					sendobjMob.put("to", SMSdata.get("to"));
//				    					//sendobjMob.put("message", "Your document has been sent.");
//				    					sendobjMob.put("message", SMSdata.getString("body"));
//				    	           //     out.println("SMSsendobjMob "+sendobjMob);
////				    					status = new SOAPCall().callPostJSonModified(sendSMSUrl, sendobjMob);
//				    				//	out.println("sms status"+status);
//				    					}

				if (CTrecord.has("CT") && CTrecord.getString("CT").contains("Download")) {
				}

				// }

				// code to set counter on document
				// generation===================================================================
				// String docgencounter = parseSlingData.updateDocGenCounter(email, update_node,
				// rep);
				// ====================================================================================================================
			}
			// }
		} catch (Exception e) {
			out.println(e.getMessage());
		}
	}
}
