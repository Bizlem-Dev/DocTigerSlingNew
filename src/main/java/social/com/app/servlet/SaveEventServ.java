package social.com.app.servlet;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
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
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/DTASaveEvent" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class SaveEventServ extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	
	//@Reference
	//private ParseSlingData parseSlingData;
	ParseSlingData parseSlingData= new ParseSlingDataImpl();

	@Override 
	protected void doPost(SlingHttpServletRequest req, SlingHttpServletResponse rep) throws IOException {
		PrintWriter out= rep.getWriter();
		Session session =null;
		String mailTempName="";
		String attachedTempName="";
		String attachedTempType="";
		String sendtocms="";
		String smsTempName="";
		
		Node dtaNode= null;
		Node communicationNode=null;
		Node eventsnode=null; 
		Node mailnode= null;
		Node eventIdNode= null;
		Node smsNode= null;
		Node CTNode= null;
		Node CTIdNode= null;
		
		JSONArray Data= new JSONArray();
		JSONObject commDetails= new JSONObject();
		
		String commTypeCSV= "";
		//String commType="";
		String email="";
		String group="";
		String[] cType;
		
		int i;
		int eventCount=0;
		int ctcount= 0;
		//int mailcount=0;
		
		JSONObject eventReturnobj= new JSONObject();
		
		try {
			session = repo.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));
			//out.println("in try block");
			BufferedInputStream bis = new BufferedInputStream(req.getInputStream());
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			int result = bis.read();
			while (result != -1) {
				buf.write((byte) result);
				result = bis.read();
			}
		   String res = buf.toString("UTF-8");
		   JSONObject obj = new JSONObject(res);
		  
			
			
			email= obj.getString("Email").replace("@", "_");
			String usrid = obj.getString("Email");
			 group = obj.getString("group");

			FreeTrialandCart cart= new FreeTrialandCart();
			String freetrialstatus=cart.checkfreetrial(usrid);
			
			dtaNode =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  usrid, group,session, rep );
			if(dtaNode!=null) {

			
			if(dtaNode.hasNode("Communication")){
				communicationNode= dtaNode.getNode("Communication");
			}else {
				communicationNode= dtaNode.addNode("Communication");
			}
			
			if(communicationNode.hasNode("Events")){
				eventsnode=communicationNode.getNode("Events");
				eventCount=(int) eventsnode.getProperty("eventCount").getLong();
			}else{
				eventsnode=communicationNode.addNode("Events");
				eventsnode.setProperty("eventCount", eventCount);
			}
			
			String saveType= obj.getString("savetype");
			String eventName= obj.getString("eventname");
			
			if(saveType.equalsIgnoreCase("New")) {
				
				eventIdNode = getEventNodeByName(eventName, email,dtaNode);
				
				if(eventIdNode==null) {
					sendtocms= obj.getString("SendToCMS");
					Data= obj.getJSONArray("Data");
					
					if(eventsnode.hasNode(""+eventCount)){
						eventIdNode=eventsnode.getNode(""+eventCount);
					}else{
						String lasteventcount=eventsnode.getProperty("eventCount").getString();
						eventIdNode=eventsnode.addNode(""+lasteventcount);
						eventCount++;
						eventsnode.setProperty("eventCount", eventCount);
						
						for(i=0; i<Data.length(); i++){
							commDetails= Data.getJSONObject(i);
							
							if(eventIdNode.hasNode("CT")){
								CTNode=eventIdNode.getNode("CT");
								ctcount=(int) CTNode.getProperty("count").getLong();
							}else{
								CTNode=eventIdNode.addNode("CT");
								CTNode.setProperty("count", ctcount);
							}
							if(CTNode.hasNode(""+ctcount)){
								CTIdNode=CTNode.getNode(""+ctcount);
							}else{
								String lastcount=CTNode.getProperty("count").getString();
								CTIdNode=CTNode.addNode(""+lastcount);
								ctcount++;
								CTNode.setProperty("count", ctcount);
							}
							commTypeCSV= commDetails.getString("CT");
							cType= commTypeCSV.split(",");
							//out.println("commTypeCSV: "+commTypeCSV);
							
							for (String s: cType){
								if(s.equalsIgnoreCase("Mail")){
									attachedTempName=commDetails.getString("AttachedTempName");
									attachedTempType=commDetails.getString("AttachedTempType");
									
									CTIdNode.setProperty("attachedTempName", attachedTempName);
									CTIdNode.setProperty("attachedTempType", attachedTempType);
									mailTempName=commDetails.getString("MailTempName");
									if(CTIdNode.hasNode("Mail")){
										mailnode=CTIdNode.getNode("Mail");
									}else{
										mailnode=CTIdNode.addNode("Mail");
									}
									mailnode.setProperty("mailTempName", mailTempName);
								}else if (s.equalsIgnoreCase("SMS")) {
									attachedTempName=commDetails.getString("AttachedTempName");
									attachedTempType=commDetails.getString("AttachedTempType");
									
									CTIdNode.setProperty("attachedTempName", attachedTempName);
									CTIdNode.setProperty("attachedTempType", attachedTempType);
									smsTempName=commDetails.getString("SMSTempName");
									if(CTIdNode.hasNode("SMS")){
										smsNode=CTIdNode.getNode("SMS");
									}else{
										smsNode=CTIdNode.addNode("SMS");
									}
									smsNode.setProperty("smsTempName", smsTempName);
								}else if (s.equalsIgnoreCase("Download")) {
									attachedTempName=commDetails.getString("AttachedTempName");
									attachedTempType=commDetails.getString("AttachedTempType");
									
									CTIdNode.setProperty("attachedTempName", attachedTempName);
									CTIdNode.setProperty("attachedTempType", attachedTempType);
									
									/*if(CTIdNode.hasNode("Download")){
										downloadNode=CTIdNode.getNode("DownLoad");
									}else{
										downloadNode=CTIdNode.addNode("Download");
									}*/
								}	

							}
																	
							eventIdNode.setProperty("Event_Name", eventName);
							eventIdNode.setProperty("Send to CMS", sendtocms);
							}
						eventReturnobj.put("status", "success");
						eventReturnobj.put("EventId", eventIdNode.getName());
						eventReturnobj.put("EventName", eventName);
						
						session.save();
						out.println(eventReturnobj);
						}
			
				}else {
					eventReturnobj.put("status", "error");
					eventReturnobj.put("message", "EventName already exists");
					out.println(eventReturnobj);
				}
				
			}else {
				String CTId="";
				String eventId= obj.getString("eventId");
				
				sendtocms= obj.getString("SendToCMS");
				Data= obj.getJSONArray("Data");
				if(eventsnode.hasNode(eventId)){
					eventIdNode=eventsnode.getNode(eventId);
					
					for(i=0; i<Data.length(); i++){
						commDetails= Data.getJSONObject(i);
						
						CTId= commDetails.getString("CTId");
						out.print("CTId** "+CTId);
						if(eventIdNode.hasNode("CT")){
							CTNode=eventIdNode.getNode("CT");
							ctcount=(int) CTNode.getProperty("count").getLong();
						}else{
							CTNode=eventIdNode.addNode("CT");
							CTNode.setProperty("count", ctcount);
						}
						if(CTId.equalsIgnoreCase("")) {
							
							String lastcount=CTNode.getProperty("count").getString();
							CTIdNode=CTNode.addNode(""+lastcount);
							ctcount++;
							CTNode.setProperty("count", ctcount);
							out.println("CTIdNodeif: "+CTIdNode);
						
							
							
							
						}else if(CTId.equalsIgnoreCase(null)) {
							String lastcount=CTNode.getProperty("count").getString();
							CTIdNode=CTNode.addNode(""+lastcount);
							ctcount++;
							CTNode.setProperty("count", ctcount);
							out.println("CTIdNodeElseif: "+CTIdNode);
						
						}else{
							
							out.println("CTId: "+CTId);
							if(CTNode.hasNode(CTId)){
								CTIdNode=CTNode.getNode(CTId);
								out.println("CTIdNodeelse: "+CTIdNode);
							}
//							String lastcount=CTNode.getProperty("count").getString();
//							CTIdNode=CTNode.addNode(""+lastcount);
//							ctcount++;
//							CTNode.setProperty("count", ctcount);
//							out.println("CTIdNodeElse: "+CTIdNode);
						}
							commTypeCSV= commDetails.getString("CT");
							cType= commTypeCSV.split(",");
							//out.println("commTypeCSV: "+commTypeCSV);
							
							for (String s: cType){
								if(s.equalsIgnoreCase("Mail")){
									attachedTempName=commDetails.getString("AttachedTempName");
									attachedTempType=commDetails.getString("AttachedTempType");
									
									CTIdNode.setProperty("attachedTempName", attachedTempName);
									CTIdNode.setProperty("attachedTempType", attachedTempType);
									mailTempName=commDetails.getString("MailTempName");
									if(CTIdNode.hasNode("Mail")){
										mailnode=CTIdNode.getNode("Mail");
									}else{
										mailnode=CTIdNode.addNode("Mail");
									}
									
									mailnode.setProperty("mailTempName", mailTempName);
									
								}else if (s.equalsIgnoreCase("SMS")) {
									attachedTempName=commDetails.getString("AttachedTempName");
									attachedTempType=commDetails.getString("AttachedTempType");
									
									CTIdNode.setProperty("attachedTempName", attachedTempName);
									CTIdNode.setProperty("attachedTempType", attachedTempType);
									smsTempName=commDetails.getString("SMSTempName");
									if(CTIdNode.hasNode("SMS")){
										smsNode=CTIdNode.getNode("SMS");
									}else{
										smsNode=CTIdNode.addNode("SMS");
									}
									smsNode.setProperty("smsTempName", smsTempName);
								}else if (s.equalsIgnoreCase("Download")) {
									attachedTempName=commDetails.getString("AttachedTempName");
									attachedTempType=commDetails.getString("AttachedTempType");
									
									CTIdNode.setProperty("attachedTempName", attachedTempName);
									CTIdNode.setProperty("attachedTempType", attachedTempType);
									
									/*if(CTIdNode.hasNode("Download")){
										downloadNode=CTIdNode.getNode("Download");
										
									}else{
										downloadNode=CTIdNode.addNode("Download");
									}*/
								}	
							}
						
						
								
							}
					session.save();
					eventReturnobj.put("status", "success");
					eventReturnobj.put("EventId", eventId);
					eventReturnobj.put("EventName", eventName);
					out.println(eventReturnobj);
					
				
				}else {
					eventReturnobj.put("status", "error");
					eventReturnobj.put("message", "Event Not Found");
					out.println(eventReturnobj);
				}
			}
			}else {
				eventReturnobj= new JSONObject();

				eventReturnobj.put("status", "error");
				eventReturnobj.put("message", "Invalid user");
				out.println(eventReturnobj);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			eventReturnobj= new JSONObject();
			try {
				eventReturnobj.put("status", "error");
				eventReturnobj.put("message", e.getMessage());
				out.println(eventReturnobj);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}

	public Node getEventNodeByName(String searchText, String email, Node DoctigerAdvNode) {
		Node eventRetNode = null;
		Session session = null;
		if (!searchText.trim().equals("")) {
			try {
				session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
				String querryStr = "select [Event_Name] from [nt:base] where (contains('Event_Name','" + searchText + "*'))  and ISDESCENDANTNODE('"+DoctigerAdvNode.getPath()+"/Communication/Events')";
				Workspace workspace = session.getWorkspace();
				Query query = workspace.getQueryManager().createQuery(querryStr, Query.JCR_SQL2);
				QueryResult result = query.execute();
				NodeIterator iterator = result.getNodes();
				while (iterator.hasNext()) {
					eventRetNode = iterator.nextNode();
					eventRetNode.setProperty("queryresult", "found");
				}
				session.save();
				return eventRetNode;
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				// return e.getMessage();
			}
		}

		return eventRetNode;

	}
	
	
	}
