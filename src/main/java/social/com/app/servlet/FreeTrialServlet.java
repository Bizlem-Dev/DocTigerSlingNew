package social.com.app.servlet;

import java.io.IOException;
import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.jcr.api.SlingRepository;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/FreeTrial" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class FreeTrialServlet extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	
	
	@Override 
	protected void doPost(SlingHttpServletRequest req, SlingHttpServletResponse rep) throws IOException {
		rep.getWriter();
		Session session =null;
		Node dtaNode= null;
		Node contentNode= null;
		Node userNode= null;
		Node emailNode= null;
		Node dtaftNode= null;
		Node serviceNode= null;
		Node ftNode= null;
		Node usrNode= null;
		Node servNode= null;
		
		try {
			session = repo.login(new SimpleCredentials("admin", "admin"
					.toCharArray()));
								
			if(session.getRootNode().hasNode("content")) {
				contentNode= session.getRootNode().getNode("content");
			
			if(contentNode.hasNode("user")) {
				userNode= contentNode.getNode("user");
			}else {
				userNode= contentNode.addNode("user");
			}
			if(userNode.hasNode("viki_gmail.com")) {
				emailNode= userNode.getNode("viki_gmail.com");
			}else {
				emailNode= userNode.addNode("viki_gmail.com");
			}	
			
			if(emailNode.hasNode("services")) {
				servNode= emailNode.getNode("services");
			}else {
				servNode= emailNode.addNode("services");
			}			
			
			if(servNode.hasNode("doctiger")) {
				dtaNode= servNode.getNode("doctiger");
			}else {
				dtaNode= servNode.addNode("doctiger");
			}
			
			if(dtaNode.hasNode("DocTigerFreeTrial")) {
				dtaftNode= dtaNode.getNode("DocTigerFreeTrial");
			}else {
				dtaftNode= dtaNode.addNode("DocTigerFreeTrial");
			}		
			
			dtaftNode.setProperty("docCount", "0");
			
			/*if(contentNode.hasNode("services")) {
				serviceNode= contentNode.getNode("services");
			}else {
				serviceNode= contentNode.addNode("services");
			}
			if(serviceNode.hasNode("freetrial")) {
				ftNode= serviceNode.getNode("freetrial");
			}else {
				ftNode= serviceNode.addNode("freetrial");
			}			
			if(ftNode.hasNode("users")) {
				usrNode= ftNode.getNode("users");
			}else {
				usrNode= ftNode.addNode("users");
			}			
			if(usrNode.hasNode("viki_gmail.com")) {
				usrNode.getNode("viki_gmail.com");
			}else {
				usrNode.addNode("viki_gmail.com");
			}*/
			}else {
				
			}		
					
		
		
			session.save();
			
			
		}catch (Exception e) {
			
				e.printStackTrace();
			
		}
		
	}
	}
	


