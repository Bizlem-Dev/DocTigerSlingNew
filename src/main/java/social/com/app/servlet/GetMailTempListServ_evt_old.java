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
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/GetDTAMailTempList_old" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class GetMailTempListServ_evt_old extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	
	@Reference
	private ParseSlingData parseSlingData;
	//ParseSlingData parseSlingData= new ParseSlingDataImpl();


	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		PrintWriter out = response.getWriter();
			out.println("in servlet");
		//String email=request.getParameter("email").replace("@", "_");
		JSONObject json = new JSONObject();
			try {
				Session session = null;
				session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
				//Node content = session.getRootNode().getNode("content");
				//Node userNode = null;
				//Node emailnode= null;
				Node dtaNode= null;
				Node communication= null;
				Node mailtempLibraryNode= null;
				Node templatenode = null;
				
				String usrid = request.getParameter("email");
				String group = request.getParameter("group");

				FreeTrialandCart cart= new FreeTrialandCart();
				String freetrialstatus=cart.checkfreetrial(usrid);

				String js = "";
				dtaNode =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  usrid, group,session, response );
				out.println("dtaNode: "+dtaNode);
				if(dtaNode!=null) {


				if(dtaNode.hasNode("Communication") && dtaNode.getNode("Communication").hasNode("MailTemplate")){
					communication= dtaNode.getNode("Communication");
						mailtempLibraryNode= communication.getNode("MailTemplate");
					
				
			
				NodeIterator iterator = mailtempLibraryNode.getNodes();
				
				JSONArray array = new JSONArray();
				//out.println(iterator.getSize());
				//if(iterator.getSize()>0) {
					while (iterator.hasNext()) {

						templatenode = iterator.nextNode();
						
						String templatename = templatenode.getName().toString();
						
						array.put(templatename);
						
						json.put("status", "success");
						json.put("MailTemplates", array);
						
						js = json.toString();

					}
				/*}else {
					JSONArray tempArr= new JSONArray();
					json.put("status", "success");
					json.put("MailTemplates", tempArr);
					
					js = json.toString();
				}
				*/

					out.println(js);
				
				}else {
					JSONArray array= new JSONArray();
					json.put("status", "success");
					json.put("MailTemplates", array);
					out.println(json.toString());

				}
				
				
				}else {
					json.put("status", "error");
					json.put("message", "Invalid user");
					out.println(json.toString());

				}
			}catch (Exception e) {
				// TODO: handle exception
				json= new JSONObject();
				try {
					json.put("status", "error");
					json.put("message", e.getMessage());
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
	}

	
	}
