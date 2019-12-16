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
	@Property(name = "sling.servlet.paths", value = { "/servlet/service/EditChildClses" }),
	@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
	@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
			"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
	"productEdit" }) })
@SuppressWarnings("serial")
public class EditChildClauseServ extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;	

	//@Reference
	//private ParseSlingData parseSlingData;
	ParseSlingData parseSlingData= new ParseSlingDataImpl();

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		String email=request.getParameter("email").replace("@", "_");
		String group=request.getParameter("group");
		JSONObject retobj= new JSONObject();

		try {
			Session session = null;
			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			Node dtaNode= null;
			Node clausesNode= null;
			Node clsNode = null;
			Node childclsNode= null;
			Node composeNode= null;
			Node clsdesNode= null;
			Node paraNode= null;
			Node paracntNode= null;
			Node fileNode= null;
			Node file= null;

			String metadata="";
			String description="";
			String displayName="";
			String Workflow="";
			String filename="";
			String email1 = request.getParameter("email");		
	
			FreeTrialandCart cart= new FreeTrialandCart();
			String freetrialstatus=cart.checkfreetrial(email1);

			dtaNode =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  email1, group,session, response );
			if(dtaNode!=null) {
				if(dtaNode.hasNode("Clauses")){
					clausesNode= dtaNode.getNode("Clauses");
				}

				String clauseId="";
				String childClauseId= request.getParameter("ParentClauseId");

				clauseId=childClauseId.substring(childClauseId.indexOf("Clauses/")+8, childClauseId.indexOf("/ChildClauses"));
				//out.println("clauseId: "+clauseId);
				if(clausesNode.hasNode(clauseId)) {
					clsNode= clausesNode.getNode(clauseId);
					if(clsNode.hasNode("ChildClauses")) {
						//childClauseNode= clsNode.getNode("ChildClauses");
						childclsNode= session.getNode(childClauseId);

						String childclauseName= childclsNode.getProperty("ChildClauseName").getString();
						metadata= childclsNode.getProperty("Metadata").getString();
						description= childclsNode.getProperty("Description").getString();
						if(clsNode.hasProperty("Workflow")) {
							Workflow= clsNode.getProperty("Workflow").getString();
						}

						if(childclsNode.hasProperty("DisplayName")){
							displayName= childclsNode.getProperty("DisplayName").getString();	
						}

						retobj.put("status", "success");
						retobj.put("Email", email);
						retobj.put("ParentClauseId", childClauseId);
						retobj.put("ParentClauseName", childclauseName);
						retobj.put("Metadata", metadata);
						retobj.put("Description", description);
						retobj.put("DisplayName", displayName);
						retobj.put("Workflow", Workflow);

						Node multilang=null;
						Node langnode=null;
						Node lang= null;
						JSONObject langobj= new JSONObject();
						if(childclsNode.hasNode("Multilingual Data")) {
							multilang= childclsNode.getNode("Multilingual Data");
							NodeIterator mulilangIterator= multilang.getNodes();
							JSONArray langArr= new JSONArray();
							while (mulilangIterator.hasNext()) {
								langobj= new JSONObject();
								langnode= mulilangIterator.nextNode();	
								String langname= langnode.getName();
								if(multilang.hasNode(langname)) {
									lang= multilang.getNode(langname);
									langobj.put("language", langname);
									if(lang.hasNode("Clause Description")) {
										clsdesNode= lang.getNode("Clause Description");
										if(clsdesNode.hasNode("Composed Online")) {
											composeNode= clsdesNode.getNode("Composed Online");
											if(composeNode.hasNode("Paragraph")) {
												paraNode= composeNode.getNode("Paragraph");
												langobj.put("type", "online");
												NodeIterator paraiterator = paraNode.getNodes();
												JSONArray paraArr= new JSONArray();
												while (paraiterator.hasNext()) {
													paracntNode= paraiterator.nextNode();
													String parades= paracntNode.getProperty("Description").getString();
													paraArr.put(parades);
												}
												langobj.put("para", paraArr);
												langArr.put(langobj);
											}else{}}
										else if (clsdesNode.hasNode("File")) {
											fileNode= clsdesNode.getNode("File");
											langobj.put("type", "file");
											NodeIterator fileIterator= fileNode.getNodes();
											while (fileIterator.hasNext()) {
												file= fileIterator.nextNode();
												filename= file.getName();
											}
											langobj.put("filename", filename);
											langobj.put("filedata", "");
											langArr.put(langobj);
										}
									}else {}
								}else {}
							}
							retobj.put("Multilingualdata", langArr);
						}else {}
						out.println(retobj);
					}else {}	
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
}
