package social.com.app.servlet;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ResourceBundle;

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
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;

import com.service.ActivateWorkflow;
import com.service.ParseSlingData;
import com.service.impl.FreeTrialandCart;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
	@Property(name = "service.vendor", value = "VISL Company"),
	@Property(name = "sling.servlet.paths", value = { "/servlet/service/addWFApprover" }),
	@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
	@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
			"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
	"productEdit" }) })
@SuppressWarnings("serial")
public class AddWFApproverServ  extends SlingAllMethodsServlet {
	@Reference
	private SlingRepository repo;
	ResourceBundle bundle = ResourceBundle.getBundle("config");
	static ResourceBundle bundleststic = ResourceBundle.getBundle("config");
	String jbpmip=bundleststic.getString("Jbpm_ip");
	@Reference
	private ParseSlingData parseSlingData;

	@Override 
	protected void doPost(SlingHttpServletRequest req, SlingHttpServletResponse rep) throws IOException {
		Session session =null;

		PrintWriter out= rep.getWriter();

		Node userNode=null;
		Node emailNode=null;
		Node dtaNode=null;
		Node clauseNode= null;
		Node wfApproverNode=null;
		Node no_approverNode= null;
		Node templateNode= null;

		String email="";
		String SFEmail="";
		String workflowType="";
		String saveType="";
		String approver="";
		int count=0;
		JSONArray Approvres= new JSONArray();

		JSONObject retObj= new JSONObject();

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
			JSONObject obj = new JSONObject(res);

			email= obj.getString("Email").replace("@", "_");
			String email1= obj.getString("Email");
			String group=obj.getString("group");
			FreeTrialandCart cart= new FreeTrialandCart();
			String freetrialstatus=cart.checkfreetrial(email1);
			out.println("freetrialstatus "+freetrialstatus);
			dtaNode =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  email1,group, session, rep );
			out.println("DocTigerAdvance "+dtaNode);
			if(dtaNode!=null) {
				if(dtaNode.hasNode("WorkflowApprovers")){
					wfApproverNode= dtaNode.getNode("WorkflowApprovers");
				}else {
					wfApproverNode= dtaNode.addNode("WorkflowApprovers");
				}

				saveType= obj.getString("saveType");
				SFEmail= obj.getString("SFEmail");
				workflowType= obj.getString("workflowType");
				if(saveType.equalsIgnoreCase("new")){
					if(workflowType.equalsIgnoreCase("Clause")) {

						if(wfApproverNode.hasNode("Clauses")){
							clauseNode= wfApproverNode.getNode("Clauses");
							count=(int) clauseNode.getProperty("No. of Approvers").getLong();
						}else {
							clauseNode= wfApproverNode.addNode("Clauses");
							clauseNode.setProperty("No. of Approvers", count);
						}

						Approvres= obj.getJSONArray("Approvers");

						for(int i=0; i<Approvres.length(); i++){
							if(clauseNode.hasNode(""+count)){
								no_approverNode=clauseNode.getNode(""+count);
							}else{
								String lastcount=clauseNode.getProperty("No. of Approvers").getString();
								no_approverNode=clauseNode.addNode(""+lastcount);
								count++;
								clauseNode.setProperty("No. of Approvers", count);
							}	

							approver= Approvres.getString(i);
							no_approverNode.setProperty("ApproverName", approver);
							//	out.println("approver: "+approver);
							ActivateWorkflow ac=new ActivateWorkflow();
						
							String urlstr1 = "http://"+jbpmip+":8080/jbpm-users/user/add";
							String wokusername1 = "kieserver";
							String wokpassword1 = "kieserver1!";
							JSONObject appobj1;
							appobj1 = new JSONObject("{\"username\" : \""+approver+"\", \"password\" : \"12345\",\"email\": \""+approver+"\",\"firstName\" : \"\",\"lastName\" : \"\",\"mobile\": \"\",\"createdBy\" : \"Admin\"}");
							//	out.println("appobj1: "+appobj1);

							String add =ac.callPostJSon(urlstr1, appobj1, wokusername1, wokpassword1);
							//	out.println("add: "+add);
							String urlstr2 = "http://"+jbpmip+":8080/jbpm-users/user/group/map";
							String wokusername2 = "kieserver";
							String wokpassword2 = "kieserver1!";
							JSONArray appobj2;
							//appobj2 = new JSONObject("{\"username\" : \""+approver+"\", \"password\" : \"12345\",\"email\": \""+approver+"\",\"firstName\" : \"\",\"lastName\" : \"\",\"mobile\": \"\",\"createdBy\" : \"Admin\"}");
							appobj2 = new JSONArray("[{\"userName\":\""+approver+"\",\"groupName\":\"kie-server\"},{\"userName\":\""+approver+"\",\"groupName\":\"rest-all\"},{\"userName\":\""+approver+"\",\"groupName\":\"user\"}]");
							String map =ac.callPostJSonMap(urlstr2, appobj2, wokusername2, wokpassword2);
							//out.println("map: "+map);




						}
					}else if(workflowType.equalsIgnoreCase("Template")){

						if(wfApproverNode.hasNode("Templates")){
							templateNode= wfApproverNode.getNode("Templates");
							count=(int) templateNode.getProperty("No. of Approvers").getLong();
						}
						else {
							templateNode= wfApproverNode.addNode("Templates");
							templateNode.setProperty("No. of Approvers", count);
						}

						Approvres= obj.getJSONArray("Approvers");

						for(int i=0; i<Approvres.length(); i++){
							if(templateNode.hasNode(""+count)){
								no_approverNode=templateNode.getNode(""+count);
							}
							else{
								String lastcount=templateNode.getProperty("No. of Approvers").getString();
								no_approverNode=templateNode.addNode(""+lastcount);
								count++;
								templateNode.setProperty("No. of Approvers", count);
							}	


							approver= Approvres.getString(i);
							no_approverNode.setProperty("ApproverName", approver);
							//	out.println("approver: "+approver);
							ActivateWorkflow ac=new ActivateWorkflow();
							String urlstr1 = "http://"+jbpmip+":8080/jbpm-users/user/add";
							String wokusername1 = "kieserver";
							String wokpassword1 = "kieserver1!";
							JSONObject appobj1;
							appobj1 = new JSONObject("{\"username\" : \""+approver+"\", \"password\" : \"12345\",\"email\": \""+approver+"\",\"firstName\" : \"\",\"lastName\" : \"\",\"mobile\": \"\",\"createdBy\" : \"Admin\"}");
							//out.println("appobj1: "+appobj1);

							String add =ac.callPostJSon(urlstr1, appobj1, wokusername1, wokpassword1);
							//out.println("add: "+add);
							String urlstr2 = "http://"+jbpmip+":8080/jbpm-users/user/group/map";
							String wokusername2 = "kieserver";
							String wokpassword2 = "kieserver1!";
							JSONArray appobj2;
							//appobj2 = new JSONObject("{\"username\" : \""+approver+"\", \"password\" : \"12345\",\"email\": \""+approver+"\",\"firstName\" : \"\",\"lastName\" : \"\",\"mobile\": \"\",\"createdBy\" : \"Admin\"}");
							appobj2 = new JSONArray("[{\"userName\":\""+approver+"\",\"groupName\":\"kie-server\"},{\"userName\":\""+approver+"\",\"groupName\":\"rest-all\"},{\"userName\":\""+approver+"\",\"groupName\":\"user\"}]");
							String map =ac.callPostJSonMap(urlstr2, appobj2, wokusername2, wokpassword2);
							//out.println("map: "+map);







						}
					}

					retObj.put("status", "success");
					retObj.put("message", "Workflow approver added Successfully");
					out.println(retObj);

					session.save();
				}else if(saveType.equalsIgnoreCase("edit")) {

					if(workflowType.equalsIgnoreCase("Clause")) {
						if(wfApproverNode.hasNode("Clauses")){
							clauseNode= wfApproverNode.getNode("Clauses");	
							clauseNode.remove();
							clauseNode= wfApproverNode.addNode("Clauses");
							clauseNode.setProperty("No. of Approvers", count);
						}
						else{
							clauseNode= wfApproverNode.addNode("Clauses");	
							clauseNode.setProperty("No. of Approvers", count); 
						}

						Approvres= obj.getJSONArray("Approvers");

						for(int i=0; i<Approvres.length(); i++){
							if(wfApproverNode.hasNode(""+count)){
								no_approverNode=clauseNode.getNode(""+count);
							}
							else{
								String lastcount=clauseNode.getProperty("No. of Approvers").getString();
								no_approverNode=clauseNode.addNode(""+lastcount);
								count++;
								clauseNode.setProperty("No. of Approvers", count);
							}	

							approver= Approvres.getString(i);
							no_approverNode.setProperty("ApproverName", approver);
							ActivateWorkflow ac=new ActivateWorkflow();
							String urlstr1 = "http://"+jbpmip+":8080/jbpm-users/user/add";
							String wokusername1 = "kieserver";
							String wokpassword1 = "kieserver1!";
							JSONObject appobj1;
							appobj1 = new JSONObject("{\"username\" : \""+approver+"\", \"password\" : \"12345\",\"email\": \""+approver+"\",\"firstName\" : \"\",\"lastName\" : \"\",\"mobile\": \"\",\"createdBy\" : \"Admin\"}");

							String add =ac.callPostJSon(urlstr1, appobj1, wokusername1, wokpassword1);

							String urlstr2 = "http://"+jbpmip+":8080/jbpm-users/user/group/map";
							String wokusername2 = "kieserver";
							String wokpassword2 = "kieserver1!";
							JSONArray appobj2;
							//appobj2 = new JSONObject("{\"username\" : \""+approver+"\", \"password\" : \"12345\",\"email\": \""+approver+"\",\"firstName\" : \"\",\"lastName\" : \"\",\"mobile\": \"\",\"createdBy\" : \"Admin\"}");
							appobj2 = new JSONArray("[{\"userName\":\""+approver+"\",\"groupName\":\"kie-server\"},{\"userName\":\""+approver+"\",\"groupName\":\"rest-all\"},{\"userName\":\""+approver+"\",\"groupName\":\"user\"}]");
							String map =ac.callPostJSonMap(urlstr2, appobj2, wokusername2, wokpassword2);
							//out.println("map: "+map);
						}
					}else if(workflowType.equalsIgnoreCase("Template")){
						if(wfApproverNode.hasNode("Templates")) {
							templateNode= wfApproverNode.getNode("Templates");
							templateNode.remove();
							templateNode= wfApproverNode.addNode("Templates");
							templateNode.setProperty("No. of Approvers", count);
						}else {
							templateNode= wfApproverNode.addNode("Templates");
							templateNode.setProperty("No. of Approvers", count);
						}

						Approvres= obj.getJSONArray("Approvers");

						for(int i=0; i<Approvres.length(); i++){
							if(templateNode.hasNode(""+count)){
								no_approverNode=templateNode.getNode(""+count);
							}else{
								String lastcount=templateNode.getProperty("No. of Approvers").getString();
								no_approverNode=templateNode.addNode(""+lastcount);
								count++;
								templateNode.setProperty("No. of Approvers", count);
							}	

							approver= Approvres.getString(i);
							no_approverNode.setProperty("ApproverName", approver);
							ActivateWorkflow ac=new ActivateWorkflow();
							String urlstr1 = "http://"+jbpmip+":8080/jbpm-users/user/add";
							String wokusername1 = "kieserver";
							String wokpassword1 = "kieserver1!";
							JSONObject appobj1;
							appobj1 = new JSONObject("{\"username\" : \""+approver+"\", \"password\" : \"12345\",\"email\": \""+approver+"\",\"firstName\" : \"\",\"lastName\" : \"\",\"mobile\": \"\",\"createdBy\" : \"Admin\"}");

							String add =ac.callPostJSon(urlstr1, appobj1, wokusername1, wokpassword1);

							String urlstr2 = "http://"+jbpmip+":8080/jbpm-users/user/group/map";
							String wokusername2 = "kieserver";
							String wokpassword2 = "kieserver1!";
							JSONArray appobj2;
							//appobj2 = new JSONObject("[{\"userName\":\""+approver+"\",\"groupName\":\"kie-server\"},{\"userName\":\""+approver+"\",\"groupName\":\"rest-all\"},{\"userName\":\""+approver+"\",\"groupName\":\"user\"}]");
							appobj2 = new JSONArray("[{\"userName\":\""+approver+"\",\"groupName\":\"kie-server\"},{\"userName\":\""+approver+"\",\"groupName\":\"rest-all\"},{\"userName\":\""+approver+"\",\"groupName\":\"user\"}]");
							String map =ac.callPostJSonMap(urlstr2, appobj2, wokusername2, wokpassword2);
							//out.println("map: "+map);
						}
					}

					retObj.put("status", "success");
					retObj.put("message", "Workflow approver added Successfully");
					out.println(retObj);

					session.save();
				}

			}else {
				retObj.put("status", "error");
				retObj.put("message", "Invalid user");
				out.println(retObj);
			}
		}catch (Exception e) {
			try {
				retObj.put("status", "error");
				retObj.put("message", e.getMessage());
				out.println(retObj);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}


}
