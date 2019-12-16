package social.com.app.servlet;

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
import javax.servlet.http.HttpServletResponse;

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
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/GetClsByName" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class GetClauseByNameServ extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	
	//@Reference
	//private ParseSlingData parseSlingData;
	ParseSlingData parseSlingData= new ParseSlingDataImpl();


	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
			
		String email=request.getParameter("email").replace("@", "_");
		String group=request.getParameter("group");

		JSONObject retobj= new JSONObject();
		
			try {
				Session session = null;
				session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
				//Node content = session.getRootNode().getNode("content");
				//Node userNode = null;
				//Node emailnode= null;
				Node dtaNode= null;
				Node clausesNode= null;
				Node clsNode = null;
				
				FreeTrialandCart cart= new FreeTrialandCart();
				String freetrialstatus=cart.checkfreetrial(email);
				
				dtaNode =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  email, group,session, response );
				if(dtaNode!=null) {
				
				if(dtaNode.hasNode("Clauses")){
					clausesNode= dtaNode.getNode("Clauses");
					
				}
				String clauseName= request.getParameter("clauseName");
				clsNode = getClauseNodeByName(clauseName, email,dtaNode ,response);
				String clauseId= clsNode.getName();
				if(clausesNode.hasNode(clauseId)) {
					retobj.put("status", "success");
					retobj.put("Email", email);
					retobj.put("clauseId", clsNode.getPath());
					out.println(retobj);
				}else {
					retobj.put("status", "error");
					retobj.put("message", "clauseId not found");
					out.println(retobj);
				}				
				}else {
					retobj.put("status", "error");
					retobj.put("message", "Invalid user");
					out.println(retobj);

				}
			}catch (Exception e) {
				// TODO: handle exception
				retobj= new JSONObject();
				try {
					retobj.put("status", "error");
					retobj.put("message", e.getMessage());
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				out.println(retobj);
			}

	}

	public Node getClauseNodeByName(String searchText, String email, Node DoctigerAdvNode ,HttpServletResponse rep) {
		Node tempRulNode = null;
		Node clsRetNode=null;
		Session session = null;
		if (!searchText.trim().equals("")) {
			//String newSearch= searchText.replace("%27", "'");
			try {
				rep.getWriter();
				session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
				//String querryStr = "select [ClauseName] from [nt:base] where ClauseName=\"" +searchText +  "\" and ISDESCENDANTNODE('/content/user/"+email+"/DocTigerAdvanced/Clauses/')";
			//new---	String querryStr = "select [ClauseName] from [nt:base] where (contains('ClauseName',\\\"" + searchText + " \"\\))  and ISDESCENDANTNODE('/content/user/" + email + "/DocTigerAdvanced/Clauses/')";
				String querryStr = "select [ClauseName] from [nt:base] where (contains('ClauseName',\\\"" + searchText + " \"\\))  and ISDESCENDANTNODE('"+DoctigerAdvNode.getPath()+"/Clauses/')";
	
				
				//out.println("querystr: "+querryStr);
				Workspace workspace = session.getWorkspace();
				Query query = workspace.getQueryManager().createQuery(querryStr, Query.JCR_SQL2);
				QueryResult result = query.execute();
				NodeIterator iterator = result.getNodes();
				while (iterator.hasNext()) {
					tempRulNode = iterator.nextNode();
					String clsName= tempRulNode.getProperty("ClauseName").getString();
				     if(clsName.equals(searchText)) {	
				    	 clsRetNode= tempRulNode;
					tempRulNode.setProperty("queryresult", "found");
				     }
				}
				session.save();
				return clsRetNode;
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

		return clsRetNode;

	}
	}
