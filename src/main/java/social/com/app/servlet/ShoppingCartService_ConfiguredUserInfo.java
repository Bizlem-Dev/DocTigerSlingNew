package social.com.app.servlet;

//http://bluealgo.com:8082/portal/process/shoppingcart/service_updated12
import com.service.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
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

import com.service.impl.FreeTrialandCart;
import com.service.impl.ParseSlingDataImpl;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/process/shoppingcart/service_ConfiguredUserInfo" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class ShoppingCartService_ConfiguredUserInfo extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;

	// private ParseSlingData parseSlingData;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = null;
		Session session = null;
		String email = "";
		String serviceId = "";
		Node content = null;
		Node servicenode2 = null;
		Node serviceidnode2 = null;
		Node groupNode = null;
		Node useridNode = null;
		JSONArray arr = null;
		JSONObject obj = null;
		String lgtype="";
		try {
			arr = new JSONArray();
			out = response.getWriter();
			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));

			email = request.getParameter("email");
			serviceId = request.getParameter("serviceId");
			
if(request.getParameter("lgtype")!=null) {
	lgtype = request.getParameter("lgtype");

}else {
	lgtype="";
}
			content = session.getRootNode().getNode(CrRuleConstValue.StringConstant.CONTENT.value());
			if(lgtype.equalsIgnoreCase("salesforce")) {
				if (!content.hasNode(CrRuleConstValue.StringConstant.SFALLSERVICES.value())) {
					servicenode2 = content.addNode(CrRuleConstValue.StringConstant.SFALLSERVICES.value());
				} else {
					servicenode2 = content.getNode(CrRuleConstValue.StringConstant.SFALLSERVICES.value());
				}
			}else {
			
			if (!content.hasNode(CrRuleConstValue.StringConstant.ALLSERVICES.value())) {
				servicenode2 = content.addNode(CrRuleConstValue.StringConstant.ALLSERVICES.value());
			} else {
				servicenode2 = content.getNode(CrRuleConstValue.StringConstant.ALLSERVICES.value());
			}
			}
			//out.println("servicenode2 " + servicenode2);

			
			if (servicenode2!=null) {
				if (servicenode2.hasNode(serviceId)) {
					serviceidnode2 = servicenode2.getNode(serviceId);
				}
				if (serviceidnode2.hasNodes()) {
					NodeIterator itr = serviceidnode2.getNodes();

					while (itr.hasNext()) {
						groupNode = itr.nextNode();
						if (groupNode.hasNode("users") && groupNode.getNode("users").hasNodes()) {

							NodeIterator itr2 = groupNode.getNode("users").getNodes();
							while (itr2.hasNext()) {
								useridNode = itr2.nextNode();
								obj = new JSONObject();
								String user = useridNode.getName();
								if (user.contains("_")) {
									user = user.replace("_", "@");
								}
								String role="";
								if(useridNode.hasProperty("role")) {
									role=useridNode.getProperty("role").getString();
								}
								obj.put("user", user);
								obj.put("role", role);
								obj.put("group", groupNode.getName());
								
								arr.put(obj);
							}
						}

					}

				}

			}
       out.print(arr);
		} catch (Exception e) {
			out.println(arr);

		}
	}

}
