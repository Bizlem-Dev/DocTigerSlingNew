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
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/DTANewGetClauseList" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class GetClauseListTemporary extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;

	//@Reference
		//private ParseSlingData parseSlingData;
	ParseSlingData parseSlingData= new ParseSlingDataImpl();

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)throws ServletException, IOException {
		// TODO Auto-generated method stub
				PrintWriter out = response.getWriter();
				JSONObject mainobj = new JSONObject();
				 
				try {
					Session session = null;
					session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
					//Node content = session.getRootNode().getNode("content");
					Node clnode = null;
					Node clausenode = null;
					//Node usernode=null;
					Node doctiger=null;
					
					//Node userid=null;
					String usrid=request.getParameter("email");
					String group=request.getParameter("group");

					//out.println("userid: "+usrid);
					FreeTrialandCart cart= new FreeTrialandCart();
					String freetrialstatus=cart.checkfreetrial(usrid);
					//out.println("freetrialstatus: "+freetrialstatus);
					doctiger =	parseSlingData.getDocTigerAdvNode(freetrialstatus, usrid, group,session, response);
					//out.println("doctiger: "+doctiger);
					if(doctiger!=null) {
						//out.println("in funcion");
					if (doctiger.hasNode("Clauses")) {
						clausenode = doctiger.getNode("Clauses");	
						//out.println("clausenode: "+clausenode);
						JSONArray clsArray = new JSONArray();
						
						String IsDeleted= "";
						NodeIterator iterator = clausenode.getNodes();
						while (iterator.hasNext()) {
							JSONObject clsObj= new JSONObject();	
							clnode = iterator.nextNode();
							String cls = clnode.getName().toString();
							//out.println("cls: "+cls);
							Node cnt=null;
							if(clausenode.hasNode(cls)) {
								cnt=clausenode.getNode(cls);
								String clsname=cnt.getProperty("ClauseName").getString();
								String Description=cnt.getProperty("Description").getString();
								if(cnt.hasProperty("IsDeleted")) {
									IsDeleted= cnt.getProperty("IsDeleted").getString();
									if(IsDeleted.equalsIgnoreCase("False")) {
													
								//out.println("versin = "+v);
								//if(v > 0.1) {
								//	out.println("versin = if  "+v);
										clsObj.put("ClauseId",cnt.getPath());
										clsObj.put("ClauseName", clsname);
										clsObj.put("Version", cnt.getProperty("Version").getLong());
										
										clsObj.put("CreatedBy", usrid);
										clsObj.put("ApprovedBy", "Admin");
										clsObj.put("CreationDate", cnt.getProperty("Creation Date").getString());
										clsObj.put("Description", Description);
								
										JSONArray childClsArray=new JSONArray();
								
										if(clnode.hasNode("ChildClauses")) {
											childClsArray= getChildData(clnode, usrid, response);
										}
										clsObj.put("ChildClauses", childClsArray);	
										clsArray.put(clsObj);
								//Node childcntNode= null;
								
								/*if(clnode.hasNode("ChildClauses")) {
									childnode= clnode.getNode("ChildClauses");
								
									NodeIterator childIterator= childnode.getNodes();
									JSONArray childClsArray = new JSONArray();
									while (childIterator.hasNext()) {
										JSONObject json = new JSONObject();
										childcntNode= childIterator.nextNode();
										String ChildClauseId= childcntNode.getName();
										
										Node childcnt= null;
										if(childnode.hasNode(ChildClauseId)) {
											childcnt= childnode.getNode(ChildClauseId);
										}
										json.put("ChildClauseId", ChildClauseId);
										json.put("ChildClauseName", childcnt.getProperty("ChildClauseName").getString());
										json.put("Clause Description", childcnt.getProperty("Description").getString());
//										json.put("", value)
										json.put("Version", "1.1");//0.1>
										json.put("CreatedBy", usrid);
										json.put("ApprovedBy", "Admin");
										json.put("CreationDate", cnt.getProperty("Creation Date").getString());
										childClsArray.put(json);
										clsObj.put("ChildClauses", childClsArray);
										
										}
									 
									}
								clsArray.put(clsObj);*/
							/*}else {
								
								//out.print("  ******* ");
							}*/
								}
								}else {
									clsObj.put("ClauseId",cnt.getPath());
									clsObj.put("ClauseName", clsname);
									clsObj.put("Version", cnt.getProperty("Version").getLong());
									
									clsObj.put("CreatedBy", usrid);
									clsObj.put("ApprovedBy", "Admin");
									clsObj.put("CreationDate", cnt.getProperty("Creation Date").getString());
									clsObj.put("Description", Description);
							
									JSONArray childClsArray=new JSONArray();
							
									if(clnode.hasNode("ChildClauses")) {
										childClsArray= getChildData(clnode, usrid, response);
									}
									clsObj.put("ChildClauses", childClsArray);	
									clsArray.put(clsObj);
							
								}
								}
							}

						mainobj.put("status", "success");
						mainobj.put("Clauses", clsArray);
						
						out.println(mainobj);
					} else {
						mainobj.put("status", "success");
						mainobj.put("Clauses", "");
						out.println(mainobj);
					}
					
					}else {
						
						mainobj.put("status", "error");
						mainobj.put("message", "Invalid user");
						out.println(mainobj);
					}
					} catch (Exception e) {
					try {
						mainobj.put("status","error");
						mainobj.put("message", e.getMessage());
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					out.println(mainobj);
				}
	}

	public JSONArray getChildData(Node cnt, String usrid, SlingHttpServletResponse response) {
		Node childnode= null;
		JSONArray childClsArray = null;
		
		Node childcntNode= null;
		
		try {
			PrintWriter out= response.getWriter();
			childClsArray= new JSONArray();
			if(cnt.hasNode("ChildClauses")) {
				childnode= cnt.getNode("ChildClauses");
				
				NodeIterator childIterator= childnode.getNodes();
				while (childIterator.hasNext()) { 
					JSONObject json = new JSONObject();
					childcntNode= childIterator.nextNode();
					String ChildClauseId= childcntNode.getPath();
					
					if(childcntNode.hasProperty("IsDeleted")) {
						String IsDeleted= childcntNode.getProperty("IsDeleted").getString();
						
						if(IsDeleted.equalsIgnoreCase("False")) {
					json.put("ChildClauseId", ChildClauseId);
					json.put("ChildClauseName", childcntNode.getProperty("ChildClauseName").getString());
					json.put("Clause Description", childcntNode.getProperty("Description").getString());
					//json.put("", value)
					json.put("Version", "1.1");//0.1>
					json.put("CreatedBy", usrid);
					json.put("ApprovedBy", "Admin");
					json.put("CreationDate", cnt.getProperty("Creation Date").getString());
					JSONArray subchildClsArray=new JSONArray();
					if(childcntNode.hasNode("ChildClauses")) {	
						
						subchildClsArray= getChildData(childcntNode, usrid, response);
						out.println("Childcntnode: "+childcntNode);
					}
					json.put("ChildClauses", subchildClsArray);	

					
					
					childClsArray.put(json);
						}}else {
							json.put("ChildClauseId", ChildClauseId);
							json.put("ChildClauseName", childcntNode.getProperty("ChildClauseName").getString());
							json.put("Clause Description", childcntNode.getProperty("Description").getString());
							//json.put("", value)
							json.put("Version", "1.1");//0.1>
							json.put("CreatedBy", usrid);
							json.put("ApprovedBy", "Admin");
							json.put("CreationDate", cnt.getProperty("Creation Date").getString());
							JSONArray subchildClsArray=new JSONArray();
							if(childcntNode.hasNode("ChildClauses")) {	
								
								subchildClsArray= getChildData(childcntNode, usrid, response);
								//out.println("Childcntnode: "+childcntNode);
							}
							json.put("ChildClauses", subchildClsArray);	

							
							
							childClsArray.put(json);

						}
					
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} 
		
		return childClsArray;
	}
}
