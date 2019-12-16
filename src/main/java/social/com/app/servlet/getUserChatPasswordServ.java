package social.com.app.servlet;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Date;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Value;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
import javax.servlet.ServletException;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.jcr.api.SlingRepository;

import com.mongodb.connection.Connection;
import com.service.ActivateWorkflow;
import com.service.ParseSlingData;
import com.service.ParseSlingData_comment;
import com.service.ReadHeaderExcel;
import com.service.impl.FreeTrialandCart;
import com.service.impl.ParseSlingDataImpl;
import com.service.impl.ParseSlingDataImpl_comment;
import com.sun.jersey.core.util.Base64;

import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({
 @Property(name = "service.description", value = "Save product Servlet"),
 @Property(name = "service.vendor", value = "VISL Company"),
 @Property(name = "sling.servlet.paths", value = {"/servlet/service/getUserChatPassword12"}),
 @Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
 @Property(name = "sling.servlet.extensions", value = {"hotproducts",  "cat",  "latestproducts",  "brief",  "prodlist",  "catalog",  
		 "viewcart",  "productslist",  "addcart",  "createproduct",  "checkmodelno",  "productEdit" })})
@SuppressWarnings("serial")
public class getUserChatPasswordServ extends SlingAllMethodsServlet {

 @Reference
 private SlingRepository repo;
 ParseSlingData_comment parseSlingData = new ParseSlingDataImpl_comment();
 Session session = null;
//http://35.200.169.114:8082/portal/servlet/service/getUserChatPassword12

 @Override
 protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
 throws ServletException, IOException {
	  PrintWriter out = response.getWriter();
//	  String uid = request.getParameter("uid");
//	  Connection conn = null; 
//		 String password="";
//		 String entityId="";
//		 ResultSet rs=null;
//		 try {
//			 
//			 out.println("classpath :: "+System.getProperty("java.class.path"));
//			 Class.forName("com.mysql.jdbc.Driver").newInstance();
//		 conn =(Connection) DriverManager.getConnection("jdbc:mysql//localhost:3306/rave2?" +
//		 "user=root&password=password");
//		 String sql="select encrypt_password,entity_id from person where username='"+uid+"'";
//		 java.sql.Statement smt=((java.sql.Connection) conn).createStatement();
//		 rs=smt.executeQuery(sql);
//
//		 while(rs.next()){
//		 password=rs.getString("encrypt_password");
//		 entityId=rs.getString("entity_id");
//		 }
//		 } catch (Exception e) {
//		 // TODO Auto-generated catch block
//		 e.printStackTrace();
//		 //password="fail"+e.getMessage();
//		 }
		 
		 
			// Node emailnode=null;
			// Node Eventnode=null;
			// Node Eventidnode= null;
			// Node mailnode = null;
			// Node mailIdnode= null;
			// Node Doctigernode =null;
			// Node EventIdnode=null;
			// JSONArray arr = new JSONArray();

			Node dtaNode = null;
			Node communicationNode = null;
			Node eventNode = null;
			Node eventcntNode = null;
			Node CTNode = null;
			Node CTCntNode = null;
			Node CTType = null;

			JSONArray Data = new JSONArray();
//{"Email":"viki@gmail.com","EventId":"4","EventName":"Event03","Primery_key":"Id","SFObject":"","Primery_key_value":"1","SFData":{"response":[{"Id":"1","name":"NAME1","address":"ADDR1"}]}}
			try {
				String email= "viki@gmail.com";
				String usrid = email;
				String group= "G1";

				email = email.replace("@", "_");
				out = response.getWriter();
				 out.println("in getEventdata ");

				// out.print("session start :: "+session);
				session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
				// out.print("session :: "+session);
				// out.print(session.getRootNode().getNode("content"));
				// out.print(session.getRootNode().getNode("content").hasNode("user"));
				// out.println("email "+email);
				// out.println(session.getRootNode().getNode("content").getNode("user").hasNode(email));

				FreeTrialandCart cart = new FreeTrialandCart();
				String freetrialstatus = cart.checkfreetrial(usrid);
				 out.println("freetrialstatus: "+freetrialstatus);
				dtaNode = parseSlingData.getDocTigerAdvNode(freetrialstatus, usrid, group,session, response);
				 out.println("dtaNode: "+dtaNode);
				if (dtaNode != null) {
					if (dtaNode.hasNode("Communication")) {
						communicationNode = dtaNode.getNode("Communication");
						eventNode = communicationNode.getNode("Events");

						if (eventNode.hasNode("4")) {
							eventcntNode = eventNode.getNode("4");

							if (eventcntNode.hasNode("CT")) {
								CTNode = eventcntNode.getNode("CT");
								String CT = "";
								NodeIterator iterator = CTNode.getNodes();
								while (iterator.hasNext()) {
									JSONObject dataobj = new JSONObject();
									CTCntNode = iterator.nextNode();
									String AttachedTempName = "";
									String AttachedTempType = "";
									// out.print(CTCntNode.hasProperty("attachedTempName"));
									if (CTCntNode.hasProperty("attachedTempName")
											&& CTCntNode.hasProperty("attachedTempType")) {
										AttachedTempName = CTCntNode.getProperty("attachedTempName").getString();
										AttachedTempType = CTCntNode.getProperty("attachedTempType").getString();
									}
									NodeIterator ctIterator = CTCntNode.getNodes();
									String ct1 = "";
									dataobj.put("CTId", CTCntNode.getName().toString());
									while (ctIterator.hasNext()) {
										CTType = ctIterator.nextNode();

										if (ct1 == "") {
											ct1 = CTType.getName().toString();
										} else if (!ct1.contains(CTType.getName().toString())) {
											ct1 = ct1 + "," + CTType.getName().toString();
										}
										CT = CTType.getName().toString();

										if (CT.equalsIgnoreCase("mail")) {
											String MailTempName = CTType.getProperty("mailTempName").getString();
											// dataobj.put("CT", CT);
											dataobj.put("CT", ct1);
											dataobj.put("MailTempName", MailTempName);
											 out.println("dataobj ctmail: "+dataobj);
										}
										if (CT.equalsIgnoreCase("sms")) {
											String SMSTempName = CTType.getProperty("smsTempName").getString();
											// dataobj.accumulate("CT", CT);
											dataobj.put("CT", ct1);

											dataobj.put("smsTempName", SMSTempName);
											 out.println("dataobj sms: "+dataobj);
										}

									}
									dataobj.put("AttachedTempName", AttachedTempName);
									dataobj.put("AttachedTempType", AttachedTempType);
									Data.put(dataobj);
									 out.println("Data: "+Data);
								}
							}
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// out.print("error message getEventdata :: "+e.getMessage());
			}
			// out.println(arr);
		 out.println("new 123");
//		 out.println(password+"-@#@-"+entityId);
  
 }

 @SuppressWarnings("deprecation")
@Override
 protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
 throws ServletException, IOException {
  // TODO Auto-generated method stub

  
 }
//	public String getEventdata(String email, String eventId, String eventName, SlingHttpServletResponse response) {
//		PrintWriter out = null;
//		// Node emailnode=null;
//		// Node Eventnode=null;
//		// Node Eventidnode= null;
//		// Node mailnode = null;
//		// Node mailIdnode= null;
//		// Node Doctigernode =null;
//		// Node EventIdnode=null;
//		// JSONArray arr = new JSONArray();
//
//		Node dtaNode = null;
//		Node communicationNode = null;
//		Node eventNode = null;
//		Node eventcntNode = null;
//		Node CTNode = null;
//		Node CTCntNode = null;
//		Node CTType = null;
//
//		JSONArray Data = new JSONArray();
//
//		try {
//			String usrid = email;
//
//			email = email.replace("@", "_");
//			out = response.getWriter();
//			 out.println("in getEventdata ");
//
//			// out.print("session start :: "+session);
//			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
//			// out.print("session :: "+session);
//			// out.print(session.getRootNode().getNode("content"));
//			// out.print(session.getRootNode().getNode("content").hasNode("user"));
//			// out.println("email "+email);
//			// out.println(session.getRootNode().getNode("content").getNode("user").hasNode(email));
//
//			FreeTrialandCart cart = new FreeTrialandCart();
//			String freetrialstatus = cart.checkfreetrial(usrid);
//			 out.println("freetrialstatus: "+freetrialstatus);
//			dtaNode = getDocTigerAdvNode(freetrialstatus, usrid, session, response);
//			 out.println("dtaNode: "+dtaNode);
//			if (dtaNode != null) {
//				if (dtaNode.hasNode("Communication")) {
//					communicationNode = dtaNode.getNode("Communication");
//					eventNode = communicationNode.getNode("Events");
//
//					if (eventNode.hasNode(eventId)) {
//						eventcntNode = eventNode.getNode(eventId);
//
//						if (eventcntNode.hasNode("CT")) {
//							CTNode = eventcntNode.getNode("CT");
//							String CT = "";
//							NodeIterator iterator = CTNode.getNodes();
//							while (iterator.hasNext()) {
//								JSONObject dataobj = new JSONObject();
//								CTCntNode = iterator.nextNode();
//								String AttachedTempName = "";
//								String AttachedTempType = "";
//								// out.print(CTCntNode.hasProperty("attachedTempName"));
//								if (CTCntNode.hasProperty("attachedTempName")
//										&& CTCntNode.hasProperty("attachedTempType")) {
//									AttachedTempName = CTCntNode.getProperty("attachedTempName").getString();
//									AttachedTempType = CTCntNode.getProperty("attachedTempType").getString();
//								}
//								NodeIterator ctIterator = CTCntNode.getNodes();
//								String ct1 = "";
//								dataobj.put("CTId", CTCntNode.getName().toString());
//								while (ctIterator.hasNext()) {
//									CTType = ctIterator.nextNode();
//
//									if (ct1 == "") {
//										ct1 = CTType.getName().toString();
//									} else if (!ct1.contains(CTType.getName().toString())) {
//										ct1 = ct1 + "," + CTType.getName().toString();
//									}
//									CT = CTType.getName().toString();
//
//									if (CT.equalsIgnoreCase("mail")) {
//										String MailTempName = CTType.getProperty("mailTempName").getString();
//										// dataobj.put("CT", CT);
//										dataobj.put("CT", ct1);
//										dataobj.put("MailTempName", MailTempName);
//										 out.println("dataobj ctmail: "+dataobj);
//									}
//									if (CT.equalsIgnoreCase("sms")) {
//										String SMSTempName = CTType.getProperty("smsTempName").getString();
//										// dataobj.accumulate("CT", CT);
//										dataobj.put("CT", ct1);
//
//										dataobj.put("smsTempName", SMSTempName);
//										 out.println("dataobj sms: "+dataobj);
//									}
//
//								}
//								dataobj.put("AttachedTempName", AttachedTempName);
//								dataobj.put("AttachedTempType", AttachedTempType);
//								Data.put(dataobj);
//								 out.println("Data: "+Data);
//							}
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			// out.print("error message getEventdata :: "+e.getMessage());
//		}
//		// out.println(arr);
//
//		return Data.toString();
//
//	}

}