package social.com.app.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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

//import com.service.ParseSlingData;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/GetFileName" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class GetFileNameServlet  extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	
	//@Reference
	//private ParseSlingData parseSlingData;

	
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	PrintWriter out = response.getWriter();
			
	String email=request.getParameter("email").replace("@", "_");
	String group=request.getParameter("group");
	JSONObject retEvntobj= new JSONObject();
		
	try {
		Session session = null;
		session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
		Node content= null;
		Node userNode = null;
		Node emailnode= null;
		Node dtaNode= null;
		/*Node excelNode= null;	
		Node excelCntNode= null;
		*/
		Node tempNode= null;
		Node serviceNode= null;
		Node freetrialNode= null;
		
		JSONObject Data= new JSONObject();
		
		/*if (session.getRootNode().getNode("content").hasNode("user")) {
			userNode = session.getRootNode().getNode("content").getNode("user");
		}*/
		content = session.getRootNode().getNode("content");
		
		if(content.hasNode("services")) {
			serviceNode= content.getNode("services");
		}
		
		email = request.getParameter("email").replace("@", "_");

		if(serviceNode.hasNode("freetrial")) {
			freetrialNode= serviceNode.getNode("freetrial");
		}
		
		if(freetrialNode.hasNode("users")) {
			userNode= freetrialNode.getNode("users");
		}
		if (userNode.hasNode(email)) {
			emailnode = userNode.getNode(email);
		}

		if (emailnode.hasNode("DocTigerAdvanced")) {
			dtaNode = emailnode.getNode("DocTigerAdvanced");
		}

		if (dtaNode.hasNode("TemplateLibrary")) {
			tempNode = dtaNode.getNode("TemplateLibrary");

			Node Template = null;
			if (tempNode.hasNode("Temp01")) {
				Template = tempNode.getNode("Temp01");
				
				String filePath= Template.getProperty("filepathWorkflow").getString();
				
				Data.put("Status","success");
				Data.put("FilePath", filePath);
			}
			/*if(excelNode.hasNode("0")){
				excelCntNode= excelNode.getNode("0");
				
				
			}*/
		}
		out.println(Data.toString());			
		
	}catch (Exception e) {
		// TODO: handle exception
		retEvntobj= new JSONObject();
		try {
			retEvntobj.put("status", "error");
			retEvntobj.put("message", e.getMessage());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		out.println(retEvntobj);
	}
}

}

