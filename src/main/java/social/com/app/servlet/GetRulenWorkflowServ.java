package social.com.app.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

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

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/GetRulenWorkflow" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class GetRulenWorkflowServ extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;	
	ResourceBundle bundle = ResourceBundle.getBundle("config");
	static ResourceBundle bundleststic = ResourceBundle.getBundle("config");
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		
		JSONObject wfObj= new JSONObject();
		JSONArray wfArr= new JSONArray();
		JSONObject ruleObj= new JSONObject();
		JSONArray ruleArr= new JSONArray();
				
		String email=request.getParameter("Email").replace("@", "_");
		//String SFEmail= request.getParameter("SFEmail"); 
		//String group=request.getParameter("group");

		
		String js="";
		
		if(request.getRequestPathInfo().getExtension().equals("workflows")){
			try {
				wfArr.put("DoctigerWorkflow");
				wfArr.put("DoctigerWorkflow_new");
//				wfArr.put("WorkFlow2");
//				wfArr.put("WorkFlow3");
//				wfArr.put("WorkFlow4");
				wfObj.put("status", "success");
				wfObj.put("WorkFlows", wfArr);
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
				
			}else if (request.getRequestPathInfo().getExtension().equals("rules")) { 
				StringBuffer response1 = null;
				try {//"+bundleststic.getString("CarrotRule_ip")+"
				URL obj = new URL("http://"+bundleststic.getString("CarrotRule_ip")+":8082/portal/servlet/service/carrotrule.doctiger?username="+email);
				
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();

				con.setRequestMethod("GET");
				int responseCode = con.getResponseCode();
				System.out.println("GET Response Code :: " + responseCode);
				if (responseCode == HttpURLConnection.HTTP_OK) { // success
				BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
				String inputLine;
				response1 = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
				response1.append(inputLine);
				}
				in.close();

				// print result
				System.out.println(response1.toString());
				} else {
				System.out.println("GET request not worked");
				}
				//JSO
				if(responseCode == 200 && response1.toString() != "") {
				
					JSONObject objNew = new JSONObject(response1.toString());
					JSONArray objArr = objNew.getJSONArray("Data");
					for(int i=0;i<objArr.length();i++) {
						JSONObject inside = objArr.getJSONObject(i);
						JSONObject subobj=new JSONObject();
						subobj.put("Rule_Engine", inside.getString("Rule Engine"));
						subobj.put("Rule_data", inside.getJSONObject("DocTiger").toString());				
						ruleArr.put(subobj);
				}
				}
				ruleObj.put("status", "success");
				ruleObj.put("Rules", ruleArr);
				
				} catch (JSONException e) {
				// TODO Auto-generated catch block
				try {
				ruleObj.put("status", "error");
				ruleObj.put("Rules", ruleArr);
				ruleObj.put("message", e.getMessage());

				} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				}

				}
				js= ruleObj.toString();			
			}
			else if(request.getRequestPathInfo().getExtension().equals("documentworkflow")){
				try {
					wfArr.put("docWorkFlow1");
					wfArr.put("docWorkFlow2");
					wfArr.put("docWorkFlow3");
					wfArr.put("docWorkFlow4");
					wfObj.put("status", "success");
					wfObj.put("DocumnetWorkFlows", wfArr);
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
				}else {}
		out.println(js);
		   
	}

	
	}
