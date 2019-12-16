package social.com.app.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
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
	@Property(name = "sling.servlet.paths", value = { "/servlet/service/DeleteMailTemplate" }),
	@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
	@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
			"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
	"productEdit" }) })
@SuppressWarnings("serial")
public class DeleteMailTempServ extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;

	//@Reference
	//private ParseSlingData parseSlingData;
	ParseSlingData parseSlingData= new ParseSlingDataImpl();

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		// http://35.201.178.201:8082/portal/servlet/service/DeleteMailTemplate?email=doctiger@xyz.com&mailtemplate=Test temp
		// out.print("response serv post");

		Session session = null;
		try {
			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			Node doctiger = null;
			JSONObject js1=new JSONObject();

			try {
				String usrid = request.getParameter("email");
				String group = request.getParameter("group");

				String template = request.getParameter("mailtemplate");

				FreeTrialandCart cart= new FreeTrialandCart();
				String freetrialstatus=cart.checkfreetrial(usrid);

				doctiger =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  usrid,group, session, response );
				if(doctiger!=null) {
					Node communicationNode = null;
					if (doctiger.hasNode("Communication")) {
						communicationNode = doctiger.getNode("Communication");
					}
					Node mailtemplateNode = null;
					if (communicationNode.hasNode("MailTemplate")) {
						mailtemplateNode = communicationNode.getNode("MailTemplate");
					} 
					Node tempnode = null;
					if (mailtemplateNode.hasNode(template)) {
						tempnode = mailtemplateNode.getNode(template);
						tempnode.removeShare();

						String s= "{\"status\":\"success\",\"message\":\"MailTemplate Deleted from Database\"}";
						JSONObject obj = new JSONObject(s);
						out.print(obj );
					}else {
						String s= "{\"status\":\"error\",\"message\":\"MailTemplate Not Found\"}";
						JSONObject obj = new JSONObject(s);
						out.print(obj );
					}
					session.save();
				}else {
					js1.put("status", "error");
					js1.put("message", "Invaild user");
					String resp = js1.toString();
					out.println(resp);
				}
			}catch (Exception e) {
				// TODO: handle exception
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
		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
