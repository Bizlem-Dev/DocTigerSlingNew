package social.com.app.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.jcr.Node;
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
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
	@Property(name = "service.vendor", value = "VISL Company"),
	@Property(name = "sling.servlet.paths", value = { "/servlet/service/DelelteClause" }),
	@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
	@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
			"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
	"productEdit" }) })
@SuppressWarnings("serial")
public class DeleClausesServ2 extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		Session session = null;

		new Date();

		try {
			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			JSONObject js=new JSONObject();
			try {
				String prclauseid = request.getParameter("parentclauseid");
				//			if (session.getRootNode().getNode("content").hasNode("user")) {
				//				userNode = session.getRootNode().getNode("content").getNode("user");
				//			}
				//			email = request.getParameter("email").replace("@", "_");
				//
				//			if (userNode.hasNode(email)) {
				//				emailNode = userNode.getNode(email);
				//			}
				//
				//			if (emailNode.hasNode("DocTigerAdvanced")) {
				//				dtaNode = emailNode.getNode("DocTigerAdvanced");
				//			}
				//
				//			if (dtaNode.hasNode("Clauses")) {
				//				clauseNode = dtaNode.getNode("Clauses");
				//
				//			}
				Node clsnode = null;
				//if (session.hasNode(prclauseid)) {
				//out.println("1");
				clsnode = session.getNode(prclauseid);
				clsnode.setProperty("IsDeleted", "True");
				//clsnode.removeShare();
				js.put("status", "success");	
				out.println(js.toString());
				//	}
				//}
				/*if (req.getRequestPathInfo().getExtension().equals("parent")) {

				Node clsnode = null;
				if (clauseNode.hasNode(prclauseid)) {
					clsnode = clauseNode.getNode(prclauseid);
					clsnode.removeShare();
					js.put("status", "success");
					out.println(js.toString());
				}
			}*/

				/*if (req.getRequestPathInfo().getExtension().equals("child")) {
				Node prclsnode = null;
				String chclauseid = req.getParameter("childclauseid");
				if (clauseNode.hasNode(prclauseid)) {
					prclsnode = clauseNode.getNode(prclauseid);
					Node chld = null;
					if (prclsnode.hasNode("ChildClauses")) {
						chld = prclsnode.getNode("ChildClauses");
						Node childnode = null;
						if (chld.hasNode(chclauseid)) {
							childnode = chld.getNode(chclauseid);

							childnode.removeShare();
							js.put("status", "success");
							out.println(js.toString());
						}

					}

				}
			}*/
				session.save();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				js.put("status", "error");
				js.put("message", e.getMessage());
				String resp = js.toString();
				out.println(resp);
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
