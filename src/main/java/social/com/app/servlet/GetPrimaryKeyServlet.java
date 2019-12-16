package social.com.app.servlet;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
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
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;

import com.service.ParseSlingData;
import com.service.impl.FreeTrialandCart;
import com.service.impl.ParseSlingDataImpl;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/GetPrimaryKey" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class GetPrimaryKeyServlet  extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	
	//@Reference
	//private ParseSlingData parseSlingData;
	ParseSlingData parseSlingData= new ParseSlingDataImpl();


	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		PrintWriter out = response.getWriter();
			
	JSONObject retutnobj= new JSONObject();
		
		try {
				Session session = null;
				session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
				
				BufferedInputStream bis = new BufferedInputStream(request.getInputStream());
				ByteArrayOutputStream buf = new ByteArrayOutputStream();
				int result = bis.read();
				while (result != -1) {
					buf.write((byte) result);
					result = bis.read();
				}
				String res = buf.toString("UTF-8");
				JSONObject obj = new JSONObject(res);
			  
				String email= obj.getString("email").replace("@", "_");
				String email1= obj.getString("email");
				String group= obj.getString("group");

				
				Node dtaNode= null;
				Node communicationNode= null;
				Node eventNode = null;
				Node eventcntNode= null;
				Node mailNode= null;
				Node mailCountNode= null;
				Node tempLibNode= null;
				Node attachedtempNode= null;
				Node sfobjbNode= null;
				Node adtempNode= null;
				
				FreeTrialandCart cart= new FreeTrialandCart();
				String freetrialstatus=cart.checkfreetrial(email1);
				
				dtaNode =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  email1, group,session, response );
				if(dtaNode!=null) {

				if(dtaNode.hasNode("Communication")){
					communicationNode= dtaNode.getNode("Communication");
					if(communicationNode.hasNode("Events")){
						eventNode= communicationNode.getNode("Events");
					}
				}
				String eventId= obj.getString("eventId");
				String eventName= obj.getString("eventname");
				
				String priKey= "";
				String object= ""; 
				if(eventNode.hasNode(eventId)) {
					eventcntNode= eventNode.getNode(eventId);
					
					if(eventcntNode.hasNode("Mail")) {
						mailNode= eventcntNode.getNode("Mail");
						
						NodeIterator iterator = mailNode.getNodes();
						for(int i=0; i<1; i++) {
							mailCountNode= iterator.nextNode();
							
							String attachedTempName= mailCountNode.getProperty("attachedTempName").getString();
							String attachedTempType= mailCountNode.getProperty("attachedTempType").getString();
							
							if(attachedTempType.equalsIgnoreCase("TemplateLibrary")) {
								if(dtaNode.hasNode("TemplateLibrary")) {
									tempLibNode= dtaNode.getNode("TemplateLibrary");
									if(tempLibNode.hasNode(attachedTempName)) {
										attachedtempNode=tempLibNode.getNode(attachedTempName);
										if(attachedtempNode.hasNode("SFobject")) {
											sfobjbNode= attachedtempNode.getNode("SFobject");
											priKey= sfobjbNode.getProperty("primeryKey").getString();
											object= sfobjbNode.getProperty("primeryKeyObject").getString();
										
										retutnobj.put("status", "success");
										retutnobj.put("Email", email);
										retutnobj.put("EventId", eventId);
										retutnobj.put("EventName", eventName);
										retutnobj.put("Primary Key", priKey);
										retutnobj.put("Object", object);
										out.println(retutnobj);
										}}else {
										retutnobj.put("status", "error");
										retutnobj.put("message", "AttachedTempName Node not found");
										/*retutnobj.put("Email", email);
										retutnobj.put("EventId", eventId);
										retutnobj.put("EventName", eventName);
										retutnobj.put("Primary Key", "CaseNo");
										retutnobj.put("Object", "Case");
										*/out.println(retutnobj);
									}
								}
							}else if(attachedTempType.equalsIgnoreCase("AdvancedTemplate")) {
								if(dtaNode.hasNode("AdvancedTemplate")) {
									adtempNode= dtaNode.getNode("AdvancedTemplate");
									if(adtempNode.hasNode(attachedTempName)) {
										attachedtempNode=adtempNode.getNode(attachedTempName);
										String staticProp= attachedtempNode.getProperty("static").getString();
										String dynamicProp= attachedtempNode.getProperty("dynamic").getString();
										
										if(staticProp.equalsIgnoreCase("true")) {
											String selectedTemplate= attachedtempNode.getProperty("selectedTemplate").getString();
											
											if(dtaNode.hasNode("TemplateLibrary")) {
												tempLibNode= dtaNode.getNode("TemplateLibrary");
												if(tempLibNode.hasNode(selectedTemplate)) {
													attachedtempNode=tempLibNode.getNode(selectedTemplate);
													if(attachedtempNode.hasNode("SFobject")) {
														sfobjbNode= attachedtempNode.getNode("SFobject");
														priKey= sfobjbNode.getProperty("primeryKey").getString();
														object= sfobjbNode.getProperty("primeryKeyObject").getString();
													
													retutnobj.put("status", "success");
													retutnobj.put("Email", email);
													retutnobj.put("EventId", eventId);
													retutnobj.put("EventName", eventName);
													retutnobj.put("Primary Key", priKey);
													retutnobj.put("Object", object);
													out.println(retutnobj);
													}}else {
													retutnobj.put("status", "error");
													retutnobj.put("message", "AttachedTempName Node not found");
													/*retutnobj.put("Email", email);
													retutnobj.put("EventId", eventId);
													retutnobj.put("EventName", eventName);
													retutnobj.put("Primary Key", "CaseNo");
													retutnobj.put("Object", "Case");
													*/out.println(retutnobj);
												}
											}
										}else if (dynamicProp.equalsIgnoreCase("true")) {
											retutnobj.put("status", "success");
											retutnobj.put("Email", email);
											retutnobj.put("EventId", eventId);
											retutnobj.put("EventName", eventName);
											retutnobj.put("Primary Key", "AccId");
											retutnobj.put("Object", "Account");
											out.println(retutnobj);
										}
										}else {
										retutnobj.put("status", "error");
										retutnobj.put("message", "AttachedTempName Node not found");
										out.println(retutnobj);
									}
								}
							}
						}
					}
					
				}else {
					retutnobj.put("status", "error");
					retutnobj.put("message", "Event ID Node not found");
					out.println(retutnobj);
				}
				}else {
					
					retutnobj.put("status", "error");
					retutnobj.put("message", "Invalid user");
					out.println(retutnobj);
				}
			}catch (Exception e) {
				try {
					retutnobj.put("status", "error");
					retutnobj.put("message", e.getMessage());
					out.println(retutnobj);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
	}
	
	}
