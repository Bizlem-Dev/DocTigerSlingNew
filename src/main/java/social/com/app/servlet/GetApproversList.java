package social.com.app.servlet;

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

import java.io.*;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/GetApproversList" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class GetApproversList extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	final String VIDEOEXTENSION[] = { ".3g2", ".3gp", ".asf", ".asx", ".avi", ".flv", ".mov", ".mp4", ".mpg", ".rm",
			".swf", ".vob", ".wmv" };

	final int NUMBEROFRESULTSPERPAGE = 10;

	// http://35.201.178.201:8082/portal/servlet/service/GetRulenWorkflow
		
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		PrintWriter out = response.getWriter();
		
		//String email=request.getParameter("Email").replace("@", "_");
		//String SFEmail= request.getParameter("SFEmail"); 
		//JSONObject ruleObj= new JSONObject();
		//JSONArray ruleArr= new JSONArray();
		JSONObject wfObj= new JSONObject();
		JSONArray wfArr= new JSONArray();
		
		String js="";
				try {
					wfArr.put("approver1");
					wfArr.put("approver2");
					wfArr.put("approver3");
					wfArr.put("approver4");
					wfObj.put("status", "success");
					wfObj.put("Approvers", wfArr);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						try {
							wfObj.put("status", "error");
							wfObj.put("message", e.getMessage());
							
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
					}
					js= wfObj.toString();
				
		out.println(js);
		}
	}