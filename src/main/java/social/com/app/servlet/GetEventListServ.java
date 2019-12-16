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
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/GetEventList" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class GetEventListServ  extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	
	//@Reference
	//private ParseSlingData parseSlingData;
	ParseSlingData parseSlingData= new ParseSlingDataImpl();

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		PrintWriter out = response.getWriter();
			
		try {
			Session session = null;
			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			Node dtaNode= null;
			Node communicationNode= null;
			Node eventNode = null;
			Node eventcntNode= null;
			JSONObject json =null;

			String usrid = request.getParameter("email");
			String group = request.getParameter("group");

			FreeTrialandCart cart= new FreeTrialandCart();
			String freetrialstatus=cart.checkfreetrial(usrid);
				
			dtaNode =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  usrid,group, session, response );
			//out.println("usrid: "+usrid+" freetrialstatus: "+freetrialstatus+" dtaNode: "+dtaNode);
			if(dtaNode!=null) {
				if(dtaNode.hasNode("Communication")){
					communicationNode= dtaNode.getNode("Communication");
					
					if(communicationNode.hasNode("Events")){
						eventNode= communicationNode.getNode("Events");
						JSONObject subjson = new JSONObject();
						
						NodeIterator iterator = eventNode.getNodes();
						
						JSONArray Eventarray = new JSONArray();
						while (iterator.hasNext()) {
							subjson = new JSONObject();
							eventcntNode = iterator.nextNode();
							
							String eventId = eventcntNode.getName().toString();
							String eventName= eventcntNode.getProperty("Event_Name").getString();
										
							subjson.put("EventId",eventId );
							subjson.put("EventName", eventName);
							Eventarray.put(subjson);
						}
						json = new JSONObject();
						json.put("status", "success");
						json.put("EventList", Eventarray);
						
						out.println(json);
					}else{
						JSONArray Eventarray = new JSONArray();
						json = new JSONObject();
						json.put("status", "success");
						json.put("EventList", Eventarray);

						out.println(json);
					}
				}else{
					JSONArray Eventarray = new JSONArray();
					
					json = new JSONObject();
					json.put("status", "success");
					json.put("EventList", Eventarray);
					
					out.println(json);
				}
			}else {
				json = new JSONObject();
				json.put("status", "success");
				json.put("message", "Invalid user");
				
				out.println(json);
			}
		}catch (Exception e) {
			// TODO: handle exception
			JSONObject json =new JSONObject();
			try {
				json.put("status", "error");
				json.put("message", "No any Event Found");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			out.println(json);
		}
	}
}
