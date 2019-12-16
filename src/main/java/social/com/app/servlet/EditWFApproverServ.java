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
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/EditWFApprover" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class EditWFApproverServ  extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	//@Reference
	//private ParseSlingData parseSlingData;
	ParseSlingData parseSlingData= new ParseSlingDataImpl();

	@Override
	protected void doGet(SlingHttpServletRequest req, SlingHttpServletResponse rep) throws ServletException, IOException{
		Session session =null;
		
		PrintWriter out= rep.getWriter();
	
		Node dtaNode=null;
		Node clauseNode= null;
		Node wfApproverNode=null;
		Node no_approverNode= null;
		Node templateNode= null;
		
		String workflowType="";
		String approver="";
		JSONArray Approvers= new JSONArray();
		
		JSONObject retObj= new JSONObject();
		
		try {
			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			
			
			//email= req.getParameter("Email").replace("@", "_");
			
			String email1= req.getParameter("Email");
			String group= req.getParameter("group");

			FreeTrialandCart cart= new FreeTrialandCart();
			String freetrialstatus=cart.checkfreetrial(email1);
			
			dtaNode =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  email1,group, session, rep );
			if(dtaNode!=null) {

			
			if(dtaNode.hasNode("WorkflowApprovers")) {
				wfApproverNode= dtaNode.getNode("WorkflowApprovers");
			}
			
			workflowType= req.getParameter("workflowType");
			if(workflowType.equalsIgnoreCase("Clause")) {
				if(wfApproverNode.hasNode("Clauses")){
					clauseNode= wfApproverNode.getNode("Clauses");	
					
					NodeIterator iteratorWF = clauseNode.getNodes();
					
					while (iteratorWF.hasNext()) {
						no_approverNode = iteratorWF.nextNode();
						approver = no_approverNode.getProperty("ApproverName").getString();
						Approvers.put(approver);
					}
				retObj.put("status", "success");
				retObj.put("Approvers", Approvers);
				out.println(retObj);
				}
			
			}else if (workflowType.equalsIgnoreCase("Template")) {
				if(wfApproverNode.hasNode("Templates")){
					templateNode= wfApproverNode.getNode("Templates");
					
					NodeIterator iteratorWF = templateNode.getNodes();
					
					while (iteratorWF.hasNext()) {
						no_approverNode = iteratorWF.nextNode();
						approver = no_approverNode.getProperty("ApproverName").getString();
						Approvers.put(approver);
					}
				retObj.put("status", "success");
				retObj.put("Approvers", Approvers);
				out.println(retObj);
			}			
				
			}	
				
			session.save();
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
