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
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/getMailTemplateList" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class getMailTemplateListServ extends SlingAllMethodsServlet {

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
			//Node content = session.getRootNode().getNode("content");
			Node tempnode = null;
			Node communicationNode = null;
			//Node usernode = null;
			Node doctiger = null;
			//Node userid = null;
			JSONObject json1 = new JSONObject();

			JSONArray array = new JSONArray();

			String usrid = request.getParameter("email");
			String group = request.getParameter("group");

			FreeTrialandCart cart= new FreeTrialandCart();
			String freetrialstatus=cart.checkfreetrial(usrid);
			
			doctiger =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  usrid, group,session, response );
			if(doctiger!=null) {
				
			
					if (doctiger.hasNode("Communication")) {
						communicationNode = doctiger.getNode("Communication");

						Node mailtemplateNode = null;
						if (communicationNode.hasNode("MailTemplate")) {
							mailtemplateNode = communicationNode.getNode("MailTemplate");

							NodeIterator iterator = mailtemplateNode.getNodes();
							Node temp1 = null;

							while (iterator.hasNext()) {
								JSONObject json = new JSONObject();

								tempnode = iterator.nextNode();

								String templatename = tempnode.getName().toString();

								if (mailtemplateNode.hasNode(templatename)) {

									temp1 = mailtemplateNode.getNode(templatename);
									String flag = temp1.getProperty("Flag").getString();
									String date = temp1.getProperty("Creation Date").getString();
									String desc = temp1.getProperty("Description").getString();
									json.put("templatetype", "mail");
									json.put("Template", templatename);
									json.put("Creation Date", date);
									json.put("Created_By", usrid);
									json.put("Approved_By", "admin");
									json.put("Flag", flag);
									json.put("description", desc);
									array.put(json);

								} else {
								}

							}

						} else {
						}
					} else {
						/*json1.put("status", "error");
						json1.put("message", "Mailtemplates Not found");*/					
					}

					Node templatenode = null;
					if (doctiger.hasNode("SMSTemplate")) {
						templatenode = doctiger.getNode("SMSTemplate");

						NodeIterator iterator = templatenode.getNodes();
						Node temp1 = null;

						while (iterator.hasNext()) {
							JSONObject json = new JSONObject();

							tempnode = iterator.nextNode();

							String templatename = tempnode.getName().toString();
							String date = "";
							String desc = "";
							if (templatenode.hasNode(templatename)) {

								temp1 = templatenode.getNode(templatename);
								if (temp1.hasProperty("created_Date")) {
									date = temp1.getProperty("created_Date").getString();
								}
								// String flag = temp1.getProperty("flag").getString();

								if (temp1.hasProperty("description")) {
									desc = temp1.getProperty("description").getString();
								}

								json.put("templatetype", "sms");
								json.put("SMSTemplate", templatename);
								json.put("Created_Date", date);
								json.put("Created_By", usrid);
								json.put("Approved_By", "admin");
								json.put("Flag", "1");
								json.put("version", "0.1");
								json.put("description", desc);

								array.put(json);

							} else {
							}

						}
					} else {
					}
				
			
			json1.put("status", "success");
			json1.put("TemplateList", array);

			out.println(json1);

			session.save();
			}else {
				JSONObject js1 = new JSONObject();
				js1.put("status", "error");
				js1.put("message", "Invalid user");
				out.println(js1);
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			JSONObject js1 = new JSONObject();
			try {
				js1.put("status", "error");
				js1.put("message", e.getMessage());
				out.println(js1);

			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}
}
