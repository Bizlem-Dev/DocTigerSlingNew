package social.com.app.servlet;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;

import com.service.ParseSlingData;
import com.service.impl.FreeTrialandCart;
import com.service.impl.ParseSlingDataImpl;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
	@Property(name = "service.vendor", value = "VISL Company"),
	@Property(name = "sling.servlet.paths", value = { "/servlet/service/approvetask" }),
	@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
	@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
			"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
	"productEdit" }) })
@SuppressWarnings("serial")
public class ApproveTask extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;

	//@Reference
	//private ParseSlingData parseSlingData;
	ParseSlingData parseSlingData= new ParseSlingDataImpl();

	@Override 
	protected void doPost(SlingHttpServletRequest req, SlingHttpServletResponse rep) throws IOException {
		PrintWriter out= rep.getWriter();
		Session session=null;
		String creator="";
		String group="";
		String Type="";
		String TemplateName="";
		String approverSFemailId="";
		String approvSFusername="";
		Node TemplateNode=null;
		Node clauseidnode=null;
		Node DoctigerAdvanced =null;
		try{
			out.println("inapprove servlet");
			BufferedInputStream bis = new BufferedInputStream(req.getInputStream());
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			int result = bis.read();

			while (result != -1) {
				buf.write((byte) result);
				result = bis.read();
			}
			String res = buf.toString("UTF-8");
			JSONObject inputobj = new JSONObject(res);

			//	{"TemplateName":"temp2","Type":"Template","Description":"xcv","creator":"doctiger8@gmail.com", "approverSFemailId:"doctiger8@xyz.com", "approvSFrusername" :"doctiger@xyz.com"}
			TemplateName=inputobj.getString("TemplateName");
			Type= inputobj.getString("Type");
			creator= inputobj.getString("creator");
			group=inputobj.getString("group");
			//task_id=inputobj.getString("task-id");
			approverSFemailId= inputobj.getString("approverSFemailId");
			approvSFusername= inputobj.getString("approvSFusername"); 
			//approverPassword="12345";
			out.println("TemplateName "+TemplateName+"Type "+Type);
			out.println("creator "+creator+" approverSFemailId "+approverSFemailId+" approvSFusername "+approvSFusername);

			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));

			FreeTrialandCart cart= new FreeTrialandCart();
			String freetrialstatus=cart.checkfreetrial(creator);

			DoctigerAdvanced =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  creator,group, session, rep );
			if(DoctigerAdvanced!=null) {
				if(Type.equals("Template")) {
					out.println("in template");

					if(DoctigerAdvanced.hasNode("TemplateLibrary") && DoctigerAdvanced.getNode("TemplateLibrary").hasNode(TemplateName)  ) {
						out.println("1");
						TemplateNode= DoctigerAdvanced.getNode("TemplateLibrary").getNode(TemplateName);
						out.println("TemplateNode "+TemplateNode);

						if(TemplateNode.hasNode("Advanced") && TemplateNode.getNode("Advanced").hasNode("workflow")) {
							Node workflow= TemplateNode.getNode("Advanced").getNode("workflow");
							String currentapprover= workflow.getProperty("current_approver").getString();
							out.println("currentapprover "+currentapprover);

							if(workflow.hasNode(currentapprover)) {
								Node currentAppNode= workflow.getNode(currentapprover);
								out.println("currentAppNode "+currentAppNode);
								out.println("currentAppNode.getProperty(\"approvername\").getString()"+currentAppNode.getProperty("approvername").getString());
								out.print("approvSFusername.replace(\"@\", \"_\"))"+approvSFusername.replace("@", "_"));
								if(currentAppNode.getProperty("approvername").getString().equals(approvSFusername.replace("@", "_"))) {
									out.println("in if");
									//http://35.188.243.203:8080/kie-server/services/rest/server/containers/business-process_3.0/tasks/4/states/completed

									//String starturl = "http://35.188.243.203:8080/kie-server/services/rest/server/containers/com.biz:business-process:6.0/tasks/"+task_id+"/states/started";
									//String startresult = new SOAPCall().callPutWithAuth(starturl, approvSFusername, approverPassword);	
									//String urlstr = "http://35.188.243.203:8080/kie-server/services/rest/server/containers/com.biz:business-process:6.0/tasks/"+task_id+"/states/completed";
									//String approverresult = new SOAPCall().callPutWithAuth(urlstr, approvSFusername, approverPassword);	

									currentAppNode.setProperty("approvedstatus", "true");
									out.println(workflow.getProperty("current_approver").getString());
									out.print(workflow.getProperty("no_of_approvers").getString());
									if(workflow.getProperty("current_approver").getString().equals(workflow.getProperty("no_of_approvers").getString())) {
										workflow.setProperty("current_approver", currentapprover );
										currentAppNode.setProperty("approvedstatus", "true");
										TemplateNode.setProperty("Approved", "true");
										Double v = 1.1;
										TemplateNode.setProperty("version", v);
									}else {
										workflow.setProperty("current_approver", Integer.parseInt(currentapprover)+1 );
									}
								}
							}
						}
					}
					out.print("success");
				}else if(Type.equals("Clauses")){
					out.println("in clauses");
					//	public Node getClauseNodeByClause(String searchText, String email) 

					// Node usernode= session.getRootNode().getNode("content").getNode("user");
					//Node emailNode= usernode.getNode(creator.replace("@", "_"));

					if(DoctigerAdvanced.hasNode("Clauses")  ) {
						clauseidnode=this.getClauseNodeByClause( TemplateName,  approvSFusername.replace("@", "_"), DoctigerAdvanced,rep) ;
						clauseidnode=	DoctigerAdvanced.getNode("Clauses").getNode(clauseidnode.getName());
						out.println("clauseidnode "+clauseidnode);
						if(clauseidnode!=null) {
							if(clauseidnode.hasNode("Advanced") && clauseidnode.getNode("Advanced").hasNode("workflow")) {
								Node workflow= clauseidnode.getNode("Advanced").getNode("workflow");
								String currentapprover= workflow.getProperty("current_approver").getString();
								out.println("currentapprover "+currentapprover);

								if(workflow.hasNode(currentapprover)) {
									Node currentAppNode= workflow.getNode(currentapprover);
									out.println("currentapprover "+currentapprover);
									if(currentAppNode.getProperty("approvername").getString().equals(approvSFusername.replace("@", "_"))) {
										out.println("in if");

										//String starturl = "http://35.188.243.203:8080/kie-server/services/rest/server/containers/com.biz:business-process:6.0/tasks/"+task_id+"/states/started";
										//String startresult = new SOAPCall().callPutWithAuth(starturl, approvSFusername, approverPassword);	
										//String urlstr = "http://35.188.243.203:8080/kie-server/services/rest/server/containers/com.biz:business-process:6.0/tasks/"+task_id+"/states/completed";
										//String approverresult = new SOAPCall().callPutWithAuth(urlstr, approvSFusername, approverPassword);	
										currentAppNode.setProperty("approvedstatus", "true");

										out.println(workflow.getProperty("current_approver").getString());
										out.print(workflow.getProperty("no_of_approvers").getString());
										if(workflow.getProperty("current_approver").getString().equals(workflow.getProperty("no_of_approvers").getString())) {
											workflow.setProperty("current_approver", currentapprover );
											currentAppNode.setProperty("approvedstatus", "true");
											//Node id=emailNode.getNode("DocTigerAdvanced").getNode("Clauses").getNode(clauseidnode.getName()); 
											clauseidnode.setProperty("Approved", "true");
											Double v = 1.1;
											clauseidnode.setProperty("Version", v);
											out.println(clauseidnode.getProperty("Version").getString());
											out.println("Version");
											//  session.save();
											//out.print("success");
										}else {
											workflow.setProperty("current_approver", Integer.parseInt(currentapprover)+1 );
										}
									}
								}
							}
						}	
					}					
					out.print("success");
				}
				session.save();
			}else {
				out.print("invalid user");
			}		
		}catch(Exception e){
			out.println(e.toString());
			e.printStackTrace();
		}
	}
	public Node getClauseNodeByClause(String searchText, String email, Node DoctigerAdvanced, SlingHttpServletResponse rep ) {
		PrintWriter out= null;
		Node tempRulNode = null;
		Session session = null;
		if (!searchText.trim().equals("")) {
			try {
				out= rep.getWriter();

				out.println(searchText);
				out.println(email);
				session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
				String querryStr="select [ClauseName] from [nt:base] where ClauseName='" + searchText + "'  and ISDESCENDANTNODE('"+DoctigerAdvanced.getPath()+"/Clauses/')";
				out.println(querryStr);
				Workspace workspace = session.getWorkspace();
				Query query = workspace.getQueryManager().createQuery(querryStr, Query.JCR_SQL2);
				QueryResult result = query.execute();
				int cout=0;				
				NodeIterator iterator = result.getNodes();
				while (iterator.hasNext()) {
					out.print("cout"+cout);
					tempRulNode = iterator.nextNode();
					cout++;
				}
				//	session.save();
				return tempRulNode;
			} 
			catch (Exception e) {
				// TODO Auto-generated catch block
				// return e.getMessage();
			}
		}
		return tempRulNode;
	}
}
