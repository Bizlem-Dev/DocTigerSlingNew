package social.com.app.servlet;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
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
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/SaveRequestResponse" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class SaveReqResServlet  extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	
	//@Reference
	//private ParseSlingData parseSlingData;
	ParseSlingData parseSlingData= new ParseSlingDataImpl();

	@Override 
	protected void doPost(SlingHttpServletRequest req, SlingHttpServletResponse rep) throws IOException {
		Session session = null;
		PrintWriter out = rep.getWriter();
		
		String selectedEvent="";
		String priKey="";
		String opFiledName="";
		
		JSONObject retObj= new JSONObject();
		
		try {
			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			
			BufferedInputStream bis = new BufferedInputStream(req.getInputStream());
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			int result = bis.read();
			while (result != -1) {
				buf.write((byte) result);
				result = bis.read();
			}
		   String res = buf.toString("UTF-8");
		   JSONObject obj = new JSONObject(res);
		  
			Node dtaNode= null;
			Node eventNode= null;
			Node eventCntNode= null;
			Node reqresNode= null;
			Node Communication=null;
			
			
			String email1=obj.getString("Email");
			String group=obj.getString("group");

			FreeTrialandCart cart= new FreeTrialandCart();
			String freetrialstatus=cart.checkfreetrial(email1);
			
			dtaNode =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  email1, group,session, rep);
			if(dtaNode!=null) {

			if(dtaNode.hasNode("Communication")){ 
				Communication= dtaNode.getNode("Communication");
			}else {
				Communication= dtaNode.addNode("Communication");
			}
			
			if(Communication.hasNode("Events")){
				eventNode=Communication.getNode("Events");
			}else{
				eventNode=Communication.addNode("Events");
			}
			
			String eventId= obj.getString("eventId");
			
			if(eventNode.hasNode(eventId)){
				eventCntNode= eventNode.getNode(eventId);
				
				if(eventCntNode.hasNode("Request_Response")){
					reqresNode=eventCntNode.getNode("Request_Response");
				}else{
					reqresNode=eventCntNode.addNode("Request_Response");
				}
			
				selectedEvent= obj.getString("selectedEvent");
				priKey= obj.getString("primaryKey");
				opFiledName= obj.getString("outputfiledname");

				reqresNode.setProperty("Selected Event", selectedEvent);
				reqresNode.setProperty("Primary Key", priKey);
				reqresNode.setProperty("Output Field Name", opFiledName);
				
		session.save();
		
		retObj.put("status", "success");
		out.println(retObj);
			}else {
				retObj.put("status", "error");
				retObj.put("message", "EventId not found");
				out.println(retObj);	
				}
			}else {
				
				retObj.put("status", "error");
				retObj.put("message", "Invalid user");
				out.println(retObj);	
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			retObj= new JSONObject();
			
			try {
				retObj.put("status", "error");
				retObj.put("message", e.getMessage());
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			out.println(retObj);	
		}
		
	}
	
	}
