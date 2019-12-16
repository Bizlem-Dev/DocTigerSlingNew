package social.com.app.servlet;

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
import org.apache.sling.commons.json.JSONArray;
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
	@Property(name = "sling.servlet.paths", value = { "/servlet/service/EditEventList" }),
	@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
	@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
			"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
	"productEdit" }) })
@SuppressWarnings("serial")
public class EditEventServlet  extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;

	//@Reference
	//private ParseSlingData parseSlingData;
	ParseSlingData parseSlingData= new ParseSlingDataImpl();

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		String email=request.getParameter("email").replace("@", "_");
		String group=request.getParameter("group");

		JSONObject retEvntobj= new JSONObject();

		try {
			Session session = null;
			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));

			Node dtaNode= null;
			Node communicationNode= null;
			Node eventNode = null;
			Node eventcntNode= null;
			Node CTNode= null;
			Node CTCntNode= null;
			Node CTType= null;

			JSONArray Data= new JSONArray();
			String usrid=request.getParameter("email");
			FreeTrialandCart cart= new FreeTrialandCart();
			String freetrialstatus=cart.checkfreetrial(usrid);

			dtaNode =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  usrid, session, response );
			if(dtaNode!=null) {
				/*if(emailnode.hasNode("DocTigerAdvanced") && emailnode.getNode("DocTigerAdvanced").hasNode("Communication") && emailnode.getNode("DocTigerAdvanced").getNode("Communication").hasNode("Events")){
					dtaNode= emailnode.getNode("DocTigerAdvanced");
				 */				
				communicationNode= dtaNode.getNode("Communication");
				eventNode= communicationNode.getNode("Events");

				String eventId= request.getParameter("eventId");
				String eventName= request.getParameter("eventName");

				if(eventNode.hasNode(eventId)) {
					eventcntNode= eventNode.getNode(eventId);

					String SendToCMS= eventcntNode.getProperty("Send to CMS").getString();
					//out.println(SendToCMS);

					if(eventcntNode.hasNode("CT")) {
						CTNode= eventcntNode.getNode("CT");
						String CT="";
						NodeIterator iterator = CTNode.getNodes();
						while (iterator.hasNext()) {
							JSONObject dataobj= new JSONObject();
							CTCntNode= iterator.nextNode();

							String AttachedTempName= CTCntNode.getProperty("attachedTempName").getString();
							String AttachedTempType= CTCntNode.getProperty("attachedTempType").getString();

							NodeIterator ctIterator= CTCntNode.getNodes();
							String ct1="";
							dataobj.put("CTId", CTCntNode.getName().toString());
							while (ctIterator.hasNext()) {
								CTType= ctIterator.nextNode();

								if(ct1=="") {
									ct1=CTType.getName().toString();
								}else if(!ct1.contains(CTType.getName().toString())) {
									ct1=ct1+","+CTType.getName().toString();
								}
								CT= CTType.getName().toString();

								if(CT.equalsIgnoreCase("mail")) {
									String MailTempName= CTType.getProperty("mailTempName").getString();
									//dataobj.put("CT", CT);	
									dataobj.put("CT", ct1);	
									dataobj.put("MailTempName", MailTempName);
								}
								if (CT.equalsIgnoreCase("sms")) {
									String SMSTempName= CTType.getProperty("smsTempName").getString();
									//dataobj.accumulate("CT", CT);
									dataobj.put("CT", ct1);

									dataobj.put("smsTempName", SMSTempName);
								}
								if (CT.equalsIgnoreCase("download")) {
									//dataobj.accumulate("CT", CT);
									dataobj.put("CT", ct1);
								}
							}
							dataobj.put("AttachedTempName", AttachedTempName);
							dataobj.put("AttachedTempType", AttachedTempType);		
							Data.put(dataobj);
						}
					}
					retEvntobj.put("status", "success");
					retEvntobj.put("Email", email);
					retEvntobj.put("Eventname", eventName);
					retEvntobj.put("SendToCMS", SendToCMS);
					retEvntobj.put("savetype", "edit");
					retEvntobj.put("Data", Data);
				}				
			}else {
				retEvntobj= new JSONObject();
				retEvntobj.put("status", "error");
				retEvntobj.put("message", "Invalid user");
			}
			out.println(retEvntobj);
			/*} else {
			retEvntobj= new JSONObject();

			retEvntobj.put("status", "error");
			retEvntobj.put("message", "Invalid user");
		}*/
		}catch (Exception e) {
			// TODO: handle exception
			retEvntobj= new JSONObject();
			try {
				retEvntobj.put("status", "error");
				retEvntobj.put("message", e.getMessage());
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			out.println(retEvntobj);
		}
	}
}