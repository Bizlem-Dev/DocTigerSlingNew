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

import com.service.ParseSlingData;
import com.service.impl.FreeTrialandCart;
import com.service.impl.ParseSlingDataImpl;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
	@Property(name = "service.vendor", value = "VISL Company"),
	@Property(name = "sling.servlet.paths", value = { "/servlet/service/EditAdvancedTemplate" }),
	@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
	@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
			"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
	"productEdit" }) })
@SuppressWarnings("serial")
public class EditAdvancedTemplates  extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;

	//@Reference
	//private ParseSlingData parseSlingData;
	ParseSlingData parseSlingData= new ParseSlingDataImpl();

	@Override
	protected void doGet(SlingHttpServletRequest req, SlingHttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		Session session = null;
		JSONObject js = new JSONObject();
		Node DocTigerAdvance=null;
		try {
			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));

			String email = req.getParameter("email");
			String group = req.getParameter("group");


			String template = req.getParameter("template");

			FreeTrialandCart cart= new FreeTrialandCart();
			String freetrialstatus=cart.checkfreetrial(email);

			DocTigerAdvance =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  email, group, session, resp);
			if(DocTigerAdvance!=null) {
				Node advtemp = null;
				if (DocTigerAdvance.hasNode("AdvancedTemplate")) {
					advtemp = DocTigerAdvance.getNode("AdvancedTemplate");
				}

				Node Template = null;
				if (advtemp.hasNode(template)) {
					Template = advtemp.getNode(template);
				}
				String outputFormat ="";
				String staticval ="";
				String dynamic ="";
				String selectedTemplate ="";
				String selectedRule ="";
				String Ruledata ="";
				String advanced ="";
				String selectedController ="";
				String selectedWorkflow ="";

				if(Template.hasProperty("outputFormat")) {
					outputFormat= Template.getProperty("outputFormat").getString();
				}
				if(Template.hasProperty("static")) {
					staticval = Template.getProperty("static").getString();
				}
				if(Template.hasProperty("dynamic")) {
					dynamic = Template.getProperty("dynamic").getString();				}
				if(Template.hasProperty("selectedTemplate")) {
					selectedTemplate = Template.getProperty("selectedTemplate").getString();
				}
				if(Template.hasProperty("selectedRule")) {
					selectedRule = Template.getProperty("selectedRule").getString();
				}
				if(Template.hasProperty("Ruledata")) {
					Ruledata = Template.getProperty("Ruledata").getString();
				}
				if(Template.hasProperty("advanced")) {
					advanced = Template.getProperty("advanced").getString();
				}
				if(Template.hasProperty("selectedController")) {
					selectedController = Template.getProperty("selectedController").getString();
				}
				if(Template.hasProperty("selectedWorkflow")) {
					selectedWorkflow = Template.getProperty("selectedWorkflow").getString();
				}
				String type="";
				if(Template.hasProperty("type")) {
					type = Template.getProperty("type").getString();
				}

				js.put("status", "success");
				js.put("outputFormat", outputFormat);
				js.put("static", staticval);
				js.put("dynamic", dynamic);
				js.put("selectedTemplate", selectedTemplate);
				js.put("selectedRule", selectedRule);
				js.put("Ruledata", Ruledata);
				js.put("advanced", advanced);
				js.put("selectedController", selectedController);
				js.put("selectedWorkflow", selectedWorkflow);
				js.put("type", type);

				out.println(js);
			}else {
				js.put("status", "error");
				js.put("message", "Invalid user");
				out.println(js);
			}
		}catch (Exception e) {
			// TODO: handle exception
			try {
				js.put("status", "error");
				js.put("message", e.getMessage());
				out.println(js);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
