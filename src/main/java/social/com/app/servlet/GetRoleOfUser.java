package social.com.app.servlet;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ResourceBundle;

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
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;

import com.service.ParseSlingData;
import com.service.SOAPCall;
import com.service.impl.FreeTrialandCart;
import com.service.impl.ParseSlingDataImpl;
import com.sun.jersey.core.util.Base64;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/getrole_id" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class GetRoleOfUser extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	ResourceBundle bundle = ResourceBundle.getBundle("config");
	static ResourceBundle bundleststic = ResourceBundle.getBundle("config");
	//@Reference
	//private ParseSlingData parseSlingData;
	ParseSlingData parseSlingData= new ParseSlingDataImpl();

	@SuppressWarnings("deprecation")
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		
		JSONObject js = new JSONObject();
		String email = "";
		String group = "";
		Node dtaNode = null;
		Node groupNode=null;
		Node useridnode=null;
		String role=null;
		String roleid=null;
		String lgtype=null;
		
		try {
			Session session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			
			email = request.getParameter("Email");
			group = request.getParameter("group");
			lgtype=request.getParameter("lgtype");
			FreeTrialandCart cart= new FreeTrialandCart();
			String freetrialstatus=cart.checkfreetrial(email);
			
			dtaNode =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  email,  group,  session,
					 response,  lgtype);
			if(dtaNode!=null) {
				
				groupNode=dtaNode.getParent();
				if(groupNode.hasNode("users") && groupNode.getNode("users").hasNode(email.replace("@", "_"))) {
					useridnode=groupNode.getNode("users").getNode(email.replace("@", "_"));
					if(useridnode.hasProperty("role")) {
						js.put("role", useridnode.getProperty("role").getString());
					}
				
					if(useridnode.hasProperty("roleid")) {
						js.put("roleid", useridnode.getProperty("roleid").getString());
					}
				}
			}
			out.print(js);
		} catch (Exception e) {
			// TODO: handle exception
			try {
				js.put("role", " ");
				js.put("roleid", " ");

			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

				out.println(js);
			
		}	
	}
}
