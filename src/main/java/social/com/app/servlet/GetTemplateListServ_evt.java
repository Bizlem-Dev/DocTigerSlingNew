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
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/GetDTATemplateList" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class GetTemplateListServ_evt extends SlingAllMethodsServlet {

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
		JSONObject json = new JSONObject();
		//String email = request.getParameter("email").replace("@", "_");
		try {
			Session session = null;
			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			Node dtaNode = null;
			Node tempLibraryNode = null;
			Node templatenode = null;

			JSONArray array = new JSONArray();

			String email1=request.getParameter("email");
			String group=request.getParameter("group");

			FreeTrialandCart cart= new FreeTrialandCart();
			String freetrialstatus=cart.checkfreetrial(email1);
			
			dtaNode =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  email1, group,session, response );
			if(dtaNode!=null) {

			
						if (dtaNode.hasNode("TemplateLibrary")) {

							tempLibraryNode = dtaNode.getNode("TemplateLibrary");

							NodeIterator iterator = tempLibraryNode.getNodes();

							while (iterator.hasNext()) {
								JSONObject json1 = new JSONObject();

								templatenode = iterator.nextNode();

								String templatename = templatenode.getName().toString();
								//Node tmp=tempLibraryNode.getNode(templatename);
							//	if(tmp.hasProperty("Approved") && tmp.getProperty("Approved").getString().equals("true")) {
								json1.put("templatename", templatename);
								json1.put("templatetype", "TemplateLibrary");
								
								array.put(json1);
								//}else {}
							}
						}

						if (dtaNode.hasNode("AdvancedTemplate")) {
							Node Advtempnode = dtaNode.getNode("AdvancedTemplate");
							NodeIterator iterator = Advtempnode.getNodes();

							while (iterator.hasNext()) {
								JSONObject json1 = new JSONObject();

								Node templatadvenode = iterator.nextNode();

								String templatename = templatadvenode.getName().toString();
								json1.put("templatename", templatename);
								json1.put("templatetype", "AdvancedTemplate");

								array.put(json1);
							}
						}

				//String js = "";
				json.put("status", "success");
				json.put("Templates", array);

			out.println(json);
			}else {
				
				json.put("status", "error");
				json.put("message", "Invalid user");
				out.println(json);
			}
		} catch (Exception e) {
			// TODO: handle exception
			try {
				json.put("status", "error");
				json.put("message", e.getMessage());
				out.println(json);

			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}

	
	}
