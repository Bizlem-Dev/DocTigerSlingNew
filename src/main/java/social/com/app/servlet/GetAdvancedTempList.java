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
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/GetAdvancedTemplates" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class GetAdvancedTempList  extends SlingAllMethodsServlet {

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
		JSONObject json1 = new JSONObject();

		try {
			Session session = null;
			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			Node tempnode = null;
			Node templatenode = null;
			Node doctiger = null;
			
			String usrid = request.getParameter("email");
			String group = request.getParameter("group");

			FreeTrialandCart cart= new FreeTrialandCart();
			String freetrialstatus=cart.checkfreetrial(usrid);
			
			doctiger =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  usrid, group,session, response );
			if(doctiger!=null) {
				if (doctiger.hasNode("AdvancedTemplate")) {
					templatenode = doctiger.getNode("AdvancedTemplate");
					NodeIterator iterator = templatenode.getNodes();

					JSONArray array = new JSONArray();
				
					while (iterator.hasNext()) {
						tempnode = iterator.nextNode();
						JSONObject json = new JSONObject();

						String templatename = tempnode.getName().toString();

						Node temp1=null;
						if (templatenode.hasNode(templatename)) {
							temp1 = templatenode.getNode(templatename);
							String date = temp1.getProperty("created_Date").getString();
						
							json.put("Template", templatename);
							json.put("Created_Date", date);
							json.put("Created_By", usrid);
							json.put("Approved_By", "admin");
							String selectedworkflow = temp1.getProperty("selectedWorkflow").getString();
							String selectedrule = temp1.getProperty("selectedRule").getString();
							String Ruledata= temp1.getProperty("Ruledata").getString();
							json.put("selectedWorkflow", selectedworkflow);
							json.put("selectedRule", selectedrule);
							json.put("Ruledata", Ruledata);
							array.put(json);
					
							json1.put("status", "success");
							json1.put("AdvancedTemplates", array);
						}
					}
				}else {
					JSONArray array = new JSONArray();
					json1.put("status", "success");
					json1.put("AdvancedTemplates", array);
				}
				out.println(json1);

				session.save();
			}else {
				json1.put("status", "error");
				json1.put("message", "Invalid user");
				out.println(json1);
			}
		} catch (Exception e) {
			// TODO: handle exception
			try {
				json1.put("status", "error");
				json1.put("message", e.getMessage());
				out.println(json1);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

	}
}
