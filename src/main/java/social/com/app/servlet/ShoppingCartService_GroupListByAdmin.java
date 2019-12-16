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
		@Property(name = "sling.servlet.paths", value = { "/process/shoppingcart/service_GroupListByAdmin" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class ShoppingCartService_GroupListByAdmin extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;

	// private ParseSlingData parseSlingData;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = null;
		Session session = null;
		String email = "";
		JSONArray arr = new JSONArray();

		try {
			out = response.getWriter();
			Node groupNodes = null;
			JSONObject obj = null;
			String propvalue = "";
			email = request.getParameter("email");
			if (email.contains("@")) {
				email = email.replace("@", "_");
			}
			
				session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));

				// String querryStr = "select [ClauseName] from [nt:base] where
				// (contains('ClauseName','" + searchText + "')) and
				// ISDESCENDANTNODE('"+DoctigerAdvNode.getPath()+"/Clauses/')";

				String querryStr = "select * from [nt:base] where (contains('groupOwnerEmail','" + email
						+ "')) and ISDESCENDANTNODE('/content/group/')";
				////out.println("querryStr " + querryStr);
				Workspace workspace = session.getWorkspace();
				Query query = workspace.getQueryManager().createQuery(querryStr, Query.JCR_SQL2);
				QueryResult result = query.execute();
				NodeIterator iterator = result.getNodes();
				while (iterator.hasNext()) {
					groupNodes = iterator.nextNode();
					obj=new JSONObject();
					////out.println("groupNodes.getName() " + groupNodes.getName());
					////out.println("groupNodes " + groupNodes);
					// obj.put("groupId", groupNodes.getName());
					if (groupNodes.hasProperty("groupAccess")) {
						propvalue = groupNodes.getProperty("groupAccess").getString();
						//out.println("propvalue " + propvalue);
						obj.put("groupAccess", propvalue);
						//out.println("2");
					}
					//out.println("1 ");
					if (groupNodes.hasProperty("groupDate")) {
						propvalue = groupNodes.getProperty("groupDate").getString();
						//out.println("propvalue " + propvalue);
						obj.put("groupDate", propvalue);
						//out.println("2 ");

					}
					//out.println("1 ");

					if (groupNodes.hasProperty("groupDescription")) {
						propvalue = groupNodes.getProperty("groupDescription").getString();
						//out.println("propvalue " + propvalue);
						obj.put("groupDescription", propvalue);
						//out.println("2 ");

					}
					//out.println("1 ");
					if (groupNodes.hasProperty("groupName")) {
						propvalue = groupNodes.getProperty("groupName").getString();
						//out.println("propvalue " + propvalue);
						//out.println("propvalue " + propvalue);
						obj.put("groupName", propvalue);
						//out.println("2 ");

					}
					//out.println("1 ");
					if (groupNodes.hasProperty("groupOwnerEmail")) {
						propvalue = groupNodes.getProperty("groupOwnerEmail").getString();
						//out.println("propvalue " + propvalue);
						obj.put("groupOwnerEmail", propvalue);
						//out.println("2 ");

					}
					//out.println("1 ");
					if (groupNodes.hasProperty("groupSummary")) {
						propvalue = groupNodes.getProperty("groupOwnerEmail").getString();
						//out.println("propvalue " + propvalue);
						obj.put("groupOwnerEmail", propvalue);
						//out.println("2 ");

					}
					//out.println("1 ");
					if (groupNodes.hasProperty("groupType")) {
						propvalue = groupNodes.getProperty("groupType").getString();
						//out.println("propvalue " + propvalue);
						obj.put("groupType", propvalue);
						//out.println("2 ");

					}
					//out.println("1 ");
					if (groupNodes.hasProperty("groupWebsite")) {
						propvalue = groupNodes.getProperty("groupWebsite").getString();
						//out.println("propvalue " + propvalue);
						obj.put("groupWebsite", propvalue);
						//out.println("2 ");

						
					}
					//out.println("1 ");
					if (groupNodes.hasProperty("groupid")) {
						Long groupid = groupNodes.getProperty("groupid").getLong();
						//out.println("groupid " + groupid);

						obj.put("groupid", groupid.toString());
						//out.println("2 ");

					}
					//out.println("1 ");

					arr.put(obj);
				}
				out.println(arr);
				// session.save();
				// return clsRetNode;

			

		} catch (Exception e) {
			out.println(arr);

		}
	}

}
