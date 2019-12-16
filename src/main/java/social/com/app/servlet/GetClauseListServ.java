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
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/GetParentClauseList" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class GetClauseListServ extends SlingAllMethodsServlet {

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
			try {
				//Node content = session.getRootNode().getNode("content");
				Node clnode = null;
				Node clausenode = null;
				//Node childnode = null;
				// Node chnode =null;

				//Node usernode = null;
				Node doctiger = null;

				//Node userid = null;
				String usrid = request.getParameter("email");
				String group = request.getParameter("group");

				FreeTrialandCart cart= new FreeTrialandCart();
				String freetrialstatus=cart.checkfreetrial(usrid);
				
				doctiger =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  usrid,group, session, response );
				if(doctiger!=null) {

				if (doctiger.hasNode("Clauses")) {
					clausenode = doctiger.getNode("Clauses");

				} else {
				}

				JSONObject mainobj = new JSONObject();
				//String js = "";
				NodeIterator iterator = clausenode.getNodes();
				JSONArray array = new JSONArray();
				// JSONArray arr1 = new JSONArray();
				while (iterator.hasNext()) {
					JSONObject json = new JSONObject();

					clnode = iterator.nextNode();
					String cls = clnode.getName().toString();

					Node cnt = null;
					if (clausenode.hasNode(cls)) {
						cnt = clausenode.getNode(cls);
						String clsname = cnt.getProperty("ClauseName").getString();
						
						long v=cnt.getProperty("Version").getLong();
						//out.println("versin = "+v);
						if(v > 0.1) {
					
						json.put("ClauseId", cnt.getName());
						json.put("ClauseName", clsname);
						array.put(json);
						}else {}

					}

				}
				mainobj.put("status", "success");
				mainobj.put("ParentClause", array);

				out.println(mainobj);
				}else {
					JSONObject js1 = new JSONObject();

					js1.put("status", "error");
					js1.put("message", "Invalid user");
					out.println(js1.toString());
					
				}
			} catch (Exception e) {
				// TODO: handle exception

				JSONObject js1 = new JSONObject();
				try {
					js1.put("status", "error");
					js1.put("message", e);
					String resp = js1.toString();
					out.println(resp);

				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

		} catch (Exception e) {
			// out1.print(e.getMessage());

			e.printStackTrace();
		}
	}
}
