package social.com.app.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

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
import com.service.impl.SOAPCall;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/workflowinfo" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class workflowinfo extends SlingAllMethodsServlet {

	
	
	@Reference
	private SlingRepository repo;
	ResourceBundle bundle = ResourceBundle.getBundle("config");
	static ResourceBundle bundleststic = ResourceBundle.getBundle("config");

	//@Reference
	//private ParseSlingData parseSlingData;
	ParseSlingData parseSlingData= new ParseSlingDataImpl();

	

	@Override
	protected void doPost(SlingHttpServletRequest req, SlingHttpServletResponse rep) throws IOException {
		PrintWriter out = rep.getWriter();
		String group="";
		String approveremailId = "";
		String approvrusername = "";
		String Workflow_task_id = "";
		String wokusername = "";
		String wokpassword = "";
		String task_proc_inst_id = "";
		Session session = null;
		JSONObject approverresultobj = null;
		JSONObject returnobj = null;

		try {

			returnobj = new JSONObject();
			JSONArray returnarr = new JSONArray();
			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));

			//Node usernode = session.getRootNode().getNode("content").getNode("user");
			BufferedInputStream bis = new BufferedInputStream(req.getInputStream());
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			int result = bis.read();

			while (result != -1) {
				buf.write((byte) result);
				result = bis.read();
			}
			String res = buf.toString("UTF-8");
			JSONObject inputobj = new JSONObject(res);
			group = inputobj.getString("group");
			approveremailId = inputobj.getString("approverSFemailId");
			approvrusername = inputobj.getString("approvSFusername");
			wokusername = inputobj.getString("username");
			wokpassword = inputobj.getString("password");
			String password = "12345";

			String urlstr = "http://104.196.49.81:8080/kie-server/services/rest/server/queries/tasks/instances/pot-owners?status=Ready&status=Reserved&status=InProgress&groups=&page=0&pageSize=10&sortOrder=true";
			String approverresult = new SOAPCall().callGetWithAuth(urlstr, wokusername, password);
			approverresultobj = new JSONObject(approverresult);
//			 out.println(approverresultobj);

	//****		NodeIterator itr = usernode.getNodes();
			Node DoctigerAdvanced=null;
			Node templateNode = null;
			Node clausenode = null;

			//Node Emailnode = null;
			Node workflow = null;
			Node workflow1 = null;
			
			FreeTrialandCart cart= new FreeTrialandCart();
			String freetrialstatus=cart.checkfreetrial(approveremailId);
			
			DoctigerAdvanced =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  approveremailId, group,session, rep );
			if(DoctigerAdvanced!=null) {
				
			//to get emailnode====
				Node serviceidNode= DoctigerAdvanced.getParent();
				String serviceid=serviceidNode.getName();
			//=======================	
				

			
	//*****		while (itr.hasNext()) {
    //*****			Emailnode = itr.nextNode();
		        //		 out.println("Emailnode "+ Emailnode);
	//*****			if (Emailnode.hasNode("DocTigerAdvanced")
	//*****						&& Emailnode.getNode("DocTigerAdvanced").hasNode("TemplateLibrary")) {
					
				if(DoctigerAdvanced.hasNode("TemplateLibrary")) {	
					
					Node templatLibrary = DoctigerAdvanced.getNode("TemplateLibrary");
			//		 out.println("templatLibrary "+ templatLibrary);

					NodeIterator itrtemp = templatLibrary.getNodes();
					while (itrtemp.hasNext()) {
						templateNode = itrtemp.nextNode();

						if (templateNode.hasNode("Advanced") && templateNode.getNode("Advanced").hasNode("workflow")) {
							workflow = templateNode.getNode("Advanced").getNode("workflow");
				//		out.println("workflow "+workflow);
							String current_approver = workflow.getProperty("current_approver").getString();
							String workflow_Task_Id = workflow.getProperty("workflow_Task_Id").getString();
							String Approvername = workflow.getNode(current_approver).getProperty("approvername")
									.getString().replace("_", "@");
							String approved_status = workflow.getNode(current_approver).getProperty("approvedstatus")
									.getString();
			//				 out.println("current_approver "+current_approver +" workflow_Task_Id "+workflow_Task_Id +" Approvername "+Approvername);

							JSONArray arr = approverresultobj.getJSONArray("task-summary");
				//			 out.println("arr "+arr);
							for (int i = 0; i < arr.length(); i++) {

								JSONObject obj = arr.getJSONObject(i);
								task_proc_inst_id = obj.getString("task-proc-inst-id");
								// out.println("");
//								out.println("obj "+obj);
//								 out.println( "task_proc_inst_id "+task_proc_inst_id);
								 
//								 out.println(""+Approvername+".equals("+approvrusername+")"+Approvername.equals(approvrusername));
//								 out.println("workflow_Task_Id.equals(\"task_proc_inst_id\") "+workflow_Task_Id.equals(task_proc_inst_id) );
								
								
								//here we can checkig if this templste is approved by all or not && templateNode.getProperty("Approver").getString().equals("false")
								if (Approvername.equals(approvrusername) && workflow_Task_Id.equals(task_proc_inst_id)
										&& approved_status.equals("false")) {
									JSONObject subarr = new JSONObject();
									subarr.put("TemplateName", templateNode.getName());
									subarr.put("Type", "Template");
									if (templateNode.hasProperty("description")
											&& templateNode.getProperty("description").getString() != null) {
										subarr.put("Description", templateNode.getProperty("description").getString());
									}
									if (templateNode.hasProperty("filepathWorkflow")
											&& templateNode.getProperty("filepathWorkflow").getString() != null) {
										subarr.put("AttachmentLink",
												templateNode.getProperty("filepathWorkflow").getString());
									}
		//****approveremailId		subarr.put("creator", Emailnode.getName().replaceAll("_", "@"));
									subarr.put("creator", approveremailId.replaceAll("_", "@"));
									subarr.put("task-id", obj.getString("task-id"));
			//						out.println("subarr "+ subarr );
									returnarr.put(subarr);
								}

							}

						}
					}
				}
				
	//***			if(Emailnode.hasNode("DocTigerAdvanced")
	//***					&& Emailnode.getNode("DocTigerAdvanced").hasNode("Clauses")) {
				
				if(DoctigerAdvanced.hasNode("Clauses")) {	

					Node ClauseLibrary = DoctigerAdvanced.getNode("Clauses");
					// out.println("templatLibrary "+ templatLibrary);

					NodeIterator itrtemp = ClauseLibrary.getNodes();
					while (itrtemp.hasNext()) {
						clausenode = itrtemp.nextNode();

						if (clausenode.hasNode("Advanced") && clausenode.getNode("Advanced").hasNode("workflow")) {
							workflow1 = clausenode.getNode("Advanced").getNode("workflow");
		//					 out.println("workflow "+workflow);
							String current_approver = workflow1.getProperty("current_approver").getString();
							String workflow_Task_Id = workflow1.getProperty("workflow_Task_Id").getString();
							String Approvername = workflow1.getNode(current_approver).getProperty("approvername")
									.getString().replace("_", "@");
							String approved_status = workflow1.getNode(current_approver).getProperty("approvedstatus")
									.getString();
		//					 out.println("current_approver "+current_approver +" workflow_Task_Id "+workflow_Task_Id +" Approvername "+Approvername);

							JSONArray arr = approverresultobj.getJSONArray("task-summary");
		//					 out.println("arr "+arr);
							for (int i = 0; i < arr.length(); i++) {

								JSONObject obj = arr.getJSONObject(i);
								task_proc_inst_id = obj.getString("task-proc-inst-id");
		//						 out.println("");

		//						 out.println("obj "+obj +" task_proc_inst_id "+task_proc_inst_id);
		//						 out.println("Approvername.equals(approvrusername)"+Approvername.equals(approvrusername));
		//						 out.println("workflow_Task_Id.equals(\"task_proc_inst_id\") "+workflow_Task_Id.equals(task_proc_inst_id) );
								
								
								//here we can checkig if this templste is approved by all or not && templateNode.getProperty("Approver").getString().equals("false")
								if (Approvername.equals(approvrusername) && workflow_Task_Id.equals(task_proc_inst_id)
										&& approved_status.equals("false")) {
									JSONObject subarr = new JSONObject();
									subarr.put("TemplateName", clausenode.getProperty("ClauseName").getString());
									subarr.put("Type", "Clauses");
									if (clausenode.hasProperty("description")
											&& clausenode.getProperty("Description").getString() != null) {
										subarr.put("Description", clausenode.getProperty("Description").getString());
									}
//									if (clausenode.hasProperty("filepathWorkflow")
//											&& clausenode.getProperty("filepathWorkflow").getString() != null) {
//										subarr.put("AttachmentLink",
//												clausenode.getProperty("filepathWorkflow").getString());
//									}
									JSONObject attachobj = new JSONObject();
	//***								attachobj.put("Email", Emailnode.getName().replaceAll("_", "@"));
									attachobj.put("Email", approveremailId.replaceAll("_", "@"));

									attachobj.put("clauseName", clausenode.getProperty("ClauseName").getString());
									attachobj.put("ClauseId", clausenode.getName());
									attachobj.put("SFEmail", approveremailId);
									
									subarr.put("AttachmentLink",attachobj.toString());
								
									
	//***							subarr.put("creator", Emailnode.getName().replaceAll("_", "@"));
									subarr.put("creator", approveremailId.replaceAll("_", "@"));

									subarr.put("task-id", obj.getString("task-id"));

		//							out.println("subarr "+ subarr );
									returnarr.put(subarr);
								}

							}

						}
					}
					
				}
				
				
		//***	}

			out.println(returnarr);
			}else {
				JSONArray arr= new JSONArray();
				out.println(arr);
			}
		} catch (Exception e) {
			out.print(e.getMessage());
		}

	}
	
	}
