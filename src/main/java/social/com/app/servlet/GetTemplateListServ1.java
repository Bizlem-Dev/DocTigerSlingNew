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
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/TemplateList" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class GetTemplateListServ1 extends SlingAllMethodsServlet {

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
			Node tempnode = null;
			Node templatenode = null;
			Node doctiger = null;
			
			String usrid = request.getParameter("email");
			String group = request.getParameter("group");


			FreeTrialandCart cart= new FreeTrialandCart();
			String freetrialstatus=cart.checkfreetrial(usrid);
			
		 doctiger =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  usrid, group,session, response );
			if(doctiger!=null) {


			if (doctiger.hasNode("TemplateLibrary")) {
				templatenode = doctiger.getNode("TemplateLibrary");

				NodeIterator iterator = templatenode.getNodes();
				JSONObject json1 = new JSONObject();

				JSONArray array = new JSONArray();
				Node temp1 = null;
				String selectedworkflow = "";
				String selectedrule = "";
				String date = "";
				String desc ="";
				String Ruledata="";
				
				while (iterator.hasNext()) {
					JSONObject json = new JSONObject();

					tempnode = iterator.nextNode();

					String templatename = tempnode.getName().toString();
					
					if (templatenode.hasNode(templatename)) {

						temp1 = templatenode.getNode(templatename);
						if (temp1.hasProperty("created_Date")) {
							 date = temp1.getProperty("created_Date").getString();
						}
						// String flag = temp1.getProperty("flag").getString();
						
						if (temp1.hasProperty("description")) {
							 desc = temp1.getProperty("description").getString();			
							 }
						
//						if(temp1.hasProperty("version")) {
	// version=temp1.getProperty("version").getString();
//						}
						json.put("Template", templatename);
						json.put("Created_Date", date);
						json.put("Created_By", usrid);
						json.put("Approved_By", "admin");
						json.put("Flag", "1");
						json.put("version", "0.1");

						json.put("description", desc);
						Node adv = null;
						if (temp1.hasNode("Advanced")) {
							adv = temp1.getNode("Advanced");
							if (adv.hasProperty("selectedWorkflow")) {
								selectedworkflow = adv.getProperty("selectedWorkflow").getString();
								json.put("selectedWorkflow", selectedworkflow);

							}
							if (adv.hasProperty("selectedRule")) {
								selectedrule = adv.getProperty("selectedRule").getString();
								json.put("selectedRule", selectedrule);

							}
							if(adv.hasProperty("Ruledata")) {
								Ruledata= adv.getProperty("Ruledata").getString();
								json.put("Ruledata", Ruledata);
							}

						}

						array.put(json);

					}
					json1.put("status", "success");

					json1.put("TemplateList", array);

				}
				// js = json1.toString();
//				String rsj="{\"status\":\"success\",\"TemplateList\":[{\"Template\":\"template1\",\"Created_Date\":\"Thu Jul 12 18:15:53 IST 2018\",\"Created_By\":\"user@gmail.com\",\"Approved_By\":\"admin\",\"Flag\":\"1\",\"description\":\"descr\",\"selectedWorkflow\":\"workflow1\",\"selectedRule\":\"rule1\"},{\"Template\":\"template2\",\"Created_Date\":\"Thu Jul 12 18:16:14 IST 2018\",\"Created_By\":\"user@gmail.com\",\"Approved_By\":\"admin\",\"Flag\":\"1\",\"description\":\"descr\",\"selectedWorkflow\":\"workflow1\",\"selectedRule\":\"rule1\"}]}";
				// out.println(rsj);
//				JSONObject jss=new JSONObject(rsj);
				out.println(json1);

				session.save();

			}else {
				JSONObject err = new JSONObject();
				err.put("status", "errot");
				err.put("TemplateList", "");
				out.println(err);
			}
			}else {
				JSONObject js1 = new JSONObject();
				js1.put("status", "error");
				js1.put("message", "Invalid User");
				out.println(js1);
				
			}
					} catch (Exception e) {
			// TODO: handle exception
			JSONObject js1 = new JSONObject();
			try {
				js1.put("status", "error");
				js1.put("message", e);
				js1.toString();
				out.println(js1);

			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

	}
}
