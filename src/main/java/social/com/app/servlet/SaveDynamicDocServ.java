package social.com.app.servlet;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
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
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/SaveAdvancedTemplate" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class SaveDynamicDocServ  extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	
	
	//@Reference
	//private ParseSlingData parseSlingData;
	ParseSlingData parseSlingData= new ParseSlingDataImpl();

	@Override
	protected void doPost(SlingHttpServletRequest req, SlingHttpServletResponse resp)
			throws ServletException, IOException {
		// http://35.201.178.201:8082/portal/servlet/service/SaveAdvancedTemplate
		// http://35.201.178.201:8082/portal/servlet/service/SaveAdvancedTemplate
		PrintWriter out = resp.getWriter();
		JSONObject js = new JSONObject();
		Session session = null;
		try {

			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));

			BufferedInputStream bis = new BufferedInputStream(req.getInputStream());
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			int result = bis.read();

			while (result != -1) {
				buf.write((byte) result);
				result = bis.read();
			}
			String res = buf.toString("UTF-8");

			JSONObject resultjsonobject = new JSONObject(res);
			String email = "";
			String group="";
			String template = "";
			email = resultjsonobject.getString("email").trim();
			group = resultjsonobject.getString("group").trim();

			template = resultjsonobject.getString("dynamicTemplatename").trim();
			if (template.equals(null) || template.length() == 0) {				

				js.put("status", "error");
				js.put("message", "Please Enter Template Name");
				out.println(js);
			} else {
				
				if (email.equals(null) || email.length() == 0) {				
					js.put("status", "error");
					js.put("message", "Please Provide email");
					out.println(js);
				} else {
				}

				FreeTrialandCart cart= new FreeTrialandCart();
				String freetrialstatus=cart.checkfreetrial(email);
				
			Node 	DocTigerAdvance =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  email, group,session, resp );
				if(DocTigerAdvance!=null) {
				

			
				Node AdvancedTemplate = null;
				if (DocTigerAdvance.hasNode("AdvancedTemplate")) {
					AdvancedTemplate = DocTigerAdvance.getNode("AdvancedTemplate");
				} else {
					AdvancedTemplate = DocTigerAdvance.addNode("AdvancedTemplate");
				}

				//String username = resultjsonobject.getString("username");

				String outputFormat = resultjsonobject.getString("outputFormat");
				String staticval = resultjsonobject.getString("static");
				String dynamic = resultjsonobject.getString("dynamic");
				String selectedTemplate = "";
				String selectedRule = "";
				String Ruledata = "";
				String selectedController = "";
				String projectName="";

				if(staticval.equalsIgnoreCase("true")) {
				if (resultjsonobject.has("selectedTemplate")) {
					selectedTemplate = resultjsonobject.getString("selectedTemplate");
				}}
				if(dynamic.equalsIgnoreCase("true")) {
				if (resultjsonobject.has("selectedRule")) {
					selectedRule = resultjsonobject.getString("selectedRule");
				
				if (resultjsonobject.has("Ruledata")) {
					Ruledata = resultjsonobject.getString("Ruledata");
					

					JSONObject ruledataobj = new JSONObject(Ruledata);
					if(ruledataobj.has("URL")) {

					String URL = ruledataobj.getString("URL");
					try {
					 projectName =URL.substring(URL.indexOf("_")+1, URL.indexOf(selectedRule)-1) ;
					}catch(Exception e) {}
					}
					
				}}
				}

				String advanced = resultjsonobject.getString("advanced");
				String selectedWorkflow="";
				if(advanced.equalsIgnoreCase("true")) {

				if (resultjsonobject.has("selectedController")) {

					selectedController = resultjsonobject.getString("selectedController");
				}
				 selectedWorkflow = resultjsonobject.getString("selectedWorkflow");
				}
				String type = resultjsonobject.getString("type");
				Node Template = null;
				if (type.equals("new")) {

					if (AdvancedTemplate.hasNode(template)) {

						js.put("status", "error");
						js.put("message", "Same template exist. Please try with new Template Name");
						out.println(js);

					} else {
						Template = AdvancedTemplate.addNode(template);
						session.save();
					}

				}

				try {
					if (type.equals("edit")) {
						Template = AdvancedTemplate.getNode(template);

					}

					// if (!AdvancedTemplate.hasNode(template)) {
					// Template = AdvancedTemplate.addNode(template);
					//
					// } else {
					// Template = AdvancedTemplate.getNode(template);
					//
					// }
					
					Date d1 = new Date();
//					Template.setProperty("approved_By", approved_By);

					Template.setProperty("created_Date", d1.toString());
					

					Template.setProperty("outputFormat", outputFormat);
					Template.setProperty("static", staticval);
					Template.setProperty("type", type);
					Template.setProperty("dynamic", dynamic);
					Template.setProperty("selectedTemplate", selectedTemplate);
					Template.setProperty("selectedRule", selectedRule);
					Template.setProperty("Ruledata", Ruledata);
					Template.setProperty("RuleProjectName", projectName);		

					Template.setProperty("advanced", advanced);
					Template.setProperty("selectedController", selectedController);
					Template.setProperty("selectedWorkflow", selectedWorkflow);
					session.save();

					js.put("status", "success");
					js.put("message", "AdvancedTemplate saved successfuly");
					out.println(js);
				} catch (Exception e) {
					// e.printStackTrace();

				}
			}else {
				js.put("status", "error");
				js.put("message", "Invalid user");
				out.println(js);
				
			}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block

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
