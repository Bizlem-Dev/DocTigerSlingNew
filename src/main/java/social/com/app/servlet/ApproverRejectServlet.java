package social.com.app.servlet;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
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
import com.service.impl.SendEmail;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
	@Property(name = "service.vendor", value = "VISL Company"),
	@Property(name = "sling.servlet.paths", value = { "/servlet/service/CheckApproval" }),
	@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
	@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
			"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
	"productEdit" }) })
@SuppressWarnings("serial")
public class ApproverRejectServlet extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;

	@Reference
	//private ParseSlingData parseSlingData;
	ParseSlingData parseSlingData= new ParseSlingDataImpl();

	@Override
	protected void doGet(SlingHttpServletRequest req, SlingHttpServletResponse rep)
			throws ServletException, IOException {
	}
	// http://35.221.183.246:8082/portal/servlet/service/SaveSMSTemplate.Basic

	//	http://35.201.178.201:8082/portal/servlet/service/SaveTemplate.Basic
	//	http://35.201.178.201:8082/portal/servlet/service/SaveTemplate.ExternalParameter

	@Override
	protected void doPost(SlingHttpServletRequest req, SlingHttpServletResponse rep) throws IOException {
		PrintWriter out = rep.getWriter();
		Session session = null;
		JSONObject js = new JSONObject();

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
			String data = "";
			String email = "";
			String group="";
			String type = "";
			//String creator = "";
			//String approverSFemailId= "";
			String SFEmail = "";
			String approvSFusername = "";
			String clauseId= "";

			Node DocTigerAdvance = null;
			Node clauseNode = null;
			Node clsNode = null;
			Node clsIdNode = null;
			Node templateNode = null;
			Node tempNode = null;

			email = obj.getString("email").replace("@", "_");
			group=obj.getString("group");
			data = obj.getString("TemplateName");
			type= obj.getString("Type");
			//creator= obj.getString("creator").replace("@", "_");

			//approverSFemailId= obj.getString("approverSFemailId");
			approvSFusername= obj.getString("approvSFusername");

			if (email.equals(null) || email.length() == 0) {
				js.put("status", "error");
				js.put("message", "Please Provide email");
				out.println(js);
			} else {

			}
			String usrid = obj.getString("email");

			FreeTrialandCart cart= new FreeTrialandCart();
			String freetrialstatus=cart.checkfreetrial(usrid);

			DocTigerAdvance =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  usrid,group, session, rep );
			if(DocTigerAdvance!=null) {
				if(type.equalsIgnoreCase("Clauses")) {
					//out.println("type  "+type);
					if(DocTigerAdvance.hasNode("Clauses")) {
						clauseNode= DocTigerAdvance.getNode("Clauses");
						//out.println("clauseNode  "+clauseNode);

						clsNode = getClauseByName(data, email, DocTigerAdvance);
						//out.println("clsNode  "+clsNode);
						clauseId= clsNode.getName().toString();

						if (clauseNode.hasNode(clauseId)) {
							clsIdNode= clauseNode.getNode(clauseId);
							//out.println( "clsIdNode "+clsIdNode);
							clsIdNode.setProperty("Rejected", "True");
							SFEmail= clsIdNode.getProperty("SFEmail").getString();
							//out.println("1: "+SFEmail);
						}else {}
					}else {}
				}else if(type.equalsIgnoreCase("Template")) {
					//out.println("type  "+type);
					if(DocTigerAdvance.hasNode("TemplateLibrary")) {
						templateNode= DocTigerAdvance.getNode("TemplateLibrary");
						//out.println("type  "+type);

						if (templateNode.hasNode(data)) {
							tempNode= templateNode.getNode(data);
							//out.println("tempNode  "+tempNode);

							tempNode.setProperty("Rejected", "True");
							SFEmail= tempNode.getProperty("SFEmail").getString();
							//out.println("2: "+SFEmail);
						}else {}
					}else {}
				}

				SendEmail se= new SendEmail();
				String status="";
				status= se.sendMail(SFEmail, "Task rejected", "The "+type+" Name: "+data+" is rejected by "+approvSFusername, "doctigertest@gmail.com", "doctiger@123", rep);
				out.println(status);
				session.save();
				js.put("status", status);
				out.println(js);
			}else {
				js.put("status", "error");
				js.put("message", "Invalid user");
				out.println(js);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
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

	public Node getClauseByName(String searchText, String email, Node DoctigerAdvNode) {
		Node clauseRetNode = null;
		Session session = null;
		if (!searchText.trim().equals("")) {
			try {
				session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
				//	String querryStr = "select [ClauseName] from [nt:base] where (contains('ClauseName','" + searchText + "'))  and ISDESCENDANTNODE('/content/user/" + email + "/DocTigerAdvanced/Clauses/')";
				String querryStr = "select [ClauseName] from [nt:base] where ClauseName=\"" +searchText +  "\" and ISDESCENDANTNODE('"+DoctigerAdvNode.getPath()+"/Clauses/')";

				Workspace workspace = session.getWorkspace();
				Query query = workspace.getQueryManager().createQuery(querryStr, Query.JCR_SQL2);
				QueryResult result = query.execute();
				NodeIterator iterator = result.getNodes();
				while (iterator.hasNext()) {
					clauseRetNode = iterator.nextNode();
					clauseRetNode.setProperty("queryresult", "found");
				}
				session.save();
				return clauseRetNode;
			} 
			catch (RepositoryException e) {
				// TODO Auto-generated catch block
				// return e.getMessage();
			} 
			catch (Exception e) {
				// TODO Auto-generated catch block
				// return e.getMessage();
			}
		}
		return clauseRetNode;
	}
}
