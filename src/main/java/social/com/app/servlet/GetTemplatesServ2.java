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
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;

import com.service.ParseSlingData;
import com.service.impl.FreeTrialandCart;
import com.service.impl.ParseSlingDataImpl;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/GetTemplates" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class GetTemplatesServ2 extends SlingAllMethodsServlet {

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
			JSONObject json = new JSONObject();
			
			try {
			String usrid = request.getParameter("email");
			String group = request.getParameter("group");

			FreeTrialandCart cart= new FreeTrialandCart();
			String freetrialstatus=cart.checkfreetrial(usrid);
			
			doctiger =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  usrid, group,session, response );
			if(doctiger!=null) {
				if (doctiger.hasNode("TemplateLibrary")) {
					templatenode = doctiger.getNode("TemplateLibrary");
					NodeIterator iterator = templatenode.getNodes();

					JSONArray array = new JSONArray();

					while (iterator.hasNext()) {
						tempnode = iterator.nextNode();
						String templatename = tempnode.getName().toString();
						array.put(templatename);
					}
					json.put("status", "success");
					json.put("Templates", array);
				} else {
					JSONArray array = new JSONArray();
					
					json.put("status", "success");
					json.put("Templates", array);
				}
				out.println(json);

				session.save();
			}else {
				json.put("status", "error");
				json.put("message", "Invalid user");
				out.println(json);
			}
		}catch (Exception e) {
			// TODO: handle exception
			json.put("status", "error");
			json.put("message", e.getMessage());
			out.println(json);
		}
	} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}
}

	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

	}
}
