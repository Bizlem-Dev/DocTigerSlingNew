package social.com.app.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Value;
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
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/EditParentClses" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class EditParentClauseServ extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;	

	
	// @Reference
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
				/*Node content = session.getRootNode().getNode("content");
				Node userNode = null;
				Node emailnode= null;
				*/
				Node dtaNode= null;
				Node clausesNode= null;
				Node clsNode = null;
				Node sfNode= null;
				Node sfobjNode= null;
				Node exparamNode= null;
				Node wsNode= null;
				Node wscntNode= null;
				Node ipNode= null;
				Node ipcntNode= null;
				Node opNode= null;
				Node opcntNode= null;
				Node clsdesNode= null;
				Node paraNode= null;
				Node paracntNode= null;
				Node fileNode= null;
				Node file= null;
				
				String metadata="";
				String description="";
				String exparam="";
				String priKey="";
				String priKeyObj="";
				String url="";
				String token="";
				String un="";
				String pass="";
				String ipfieldName="";
				String ipfieldType="";
				String ipfieldLength="";
				String opfieldName="";
				String opfieldType="";
				String opfieldLength="";
				String priKeyex="";
				String filename="";
				String RuleBased="";
				String Ruledata="";
				String Controller="";
				String Workflow="";	
				
				
				String email1 = request.getParameter("email");		
				FreeTrialandCart cart= new FreeTrialandCart();
				String freetrialstatus=cart.checkfreetrial(email1);
				
			 dtaNode =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  email1, group,session, response );
				if(dtaNode!=null) {

				
				if(dtaNode.hasNode("Clauses")){
					clausesNode= dtaNode.getNode("Clauses");								
				}

				JSONObject sfret= new JSONObject();
								
				String clauseName= request.getParameter("clauseName");
				clsNode = getClauseNodeByName(clauseName, email, dtaNode,response);
				String clauseId= clsNode.getName();
				if(clausesNode.hasNode(clauseId)) {
					metadata= clsNode.getProperty("Metadata").getString();
					description= clsNode.getProperty("Description").getString();
					exparam= clsNode.getProperty("ExternalParameter").getString();
					
					if(clsNode.hasProperty("Workflow")) {
						Workflow= clsNode.getProperty("Workflow").getString();
						}
						if(clsNode.hasProperty("RuleBased")) {
						RuleBased= clsNode.getProperty("RuleBased").getString();
						}
						if(clsNode.hasProperty("Ruledata")) {
							Ruledata= clsNode.getProperty("Ruledata").getString();
						}
						if(clsNode.hasProperty("Controller Name")) {
						Controller= clsNode.getProperty("Controller Name").getString();
						}
						
					retobj.put("status", "success");
					retobj.put("Email", email);
					retobj.put("clauseId", clsNode.getPath());
					retobj.put("clauseName", clauseName);
					retobj.put("Metadata", metadata);
					retobj.put("Description", description);
					retobj.put("External Parameter", exparam);
					retobj.put("Workflow", Workflow);
					retobj.put("RuleBased", RuleBased);
					retobj.put("Ruledata", Ruledata);
					retobj.put("Controller", Controller);
					
					String sfName= "";
					
					if(clsNode.hasNode("SF Object")) {
						sfNode= clsNode.getNode("SF Object");
						
						priKey= sfNode.getProperty("Primary Key").getString();
						priKeyObj= sfNode.getProperty("Primary key Object").getString();
						NodeIterator iterator = sfNode.getNodes();
						
						Value[] data = null;
						while (iterator.hasNext()) {
							sfobjNode= iterator.nextNode();
							sfName= sfobjNode.getName().toString();
							data = sfobjNode.getProperty("Fields").getValues();
							JSONArray fieldArr= new JSONArray();

							for (int i1 = 0; i1 < data.length; i1++) {
								String values = data[i1].getString();
								fieldArr.put(values);
								sfret.put(sfName, fieldArr);
						     }
							}
						JSONObject prikeyobject= new JSONObject();
						
						prikeyobject.put("Object", priKeyObj);
						prikeyobject.put("key", priKey);
						
						sfret.put("Primary Key", prikeyobject);
						
						retobj.put("SFObject", sfret);
						
					}else {
					//	out.println("SF Object not found");
					}
					
					if(clsNode.hasNode("External Parameter")) {
						exparamNode= clsNode.getNode("External Parameter");
						
						if(exparamNode.hasNode("ws")) {
							wsNode= exparamNode.getNode("ws");
							
							NodeIterator exiterator = wsNode.getNodes();
							JSONArray exret= new JSONArray();
								
						while (exiterator.hasNext()) {
							JSONObject wsObj= new JSONObject();
							wscntNode= exiterator.nextNode();
							
							url= wscntNode.getProperty("URL").getString();
							token= wscntNode.getProperty("Token").getString();
							un= wscntNode.getProperty("Username").getString();
							pass= wscntNode.getProperty("Password").getString();
							priKeyex= wscntNode.getProperty("Primary Key").getString();
							
							wsObj.put("URL", url);
							wsObj.put("token", token);
							wsObj.put("username", un);
							wsObj.put("password", pass);
							wsObj.put("primerykey", priKeyex);
							
							if(wscntNode.hasNode("Input")) {
									ipNode= wscntNode.getNode("Input");
									NodeIterator ipIterator= ipNode.getNodes();
									JSONArray ipArr= new JSONArray();
									while (ipIterator.hasNext()) {
										JSONObject ipobj= new JSONObject();
										ipcntNode= ipIterator.nextNode();	
										String ip= ipcntNode.getName();
										
										Node ipcnt= null;
										if(ipNode.hasNode(ip)) {
											ipcnt= ipNode.getNode(ip);
										}
										
										ipfieldName= ipcnt.getProperty("Field Name").getString();
										ipfieldType= ipcnt.getProperty("Field Type").getString();
										ipfieldLength= ipcnt.getProperty("Field Length").getString();
										
										ipobj.put("Field Name", ipfieldName);
										ipobj.put("Field Type", ipfieldType);
										ipobj.put("Field Length", ipfieldLength);
										
										ipArr.put(ipobj);
										
									}
									wsObj.put("input", ipArr);
									
								}
								
								if(wscntNode.hasNode("Output")) {
									opNode= wscntNode.getNode("Output");
									NodeIterator opIterator= opNode.getNodes();
									JSONArray opArr= new JSONArray();
									while (opIterator.hasNext()) {
										JSONObject opobj= new JSONObject();
										opcntNode= opIterator.nextNode();	
										String op= opcntNode.getName();
										
										Node opcnt= null;
										if(opNode.hasNode(op)) {
											opcnt= opNode.getNode(op);
										}
										
										opfieldName= opcnt.getProperty("Field Name").getString();
										opfieldType= opcnt.getProperty("Field Type").getString();
										opfieldLength= opcnt.getProperty("Field Length").getString();
										
										opobj.put("Field Name", opfieldName);
										opobj.put("Field Type", opfieldType);
										opobj.put("Field Length", opfieldLength);
										
										opArr.put(opobj);
										
									}
									wsObj.put("output", opArr);
									
								}
								
								exret.put(wsObj);
						}
						retobj.put("externalparamobject", exret);
						
						}else {
						//	out.println("wsNode not found");
						}
						
						
					}else {
					}
					

//					====
					

					Node multilang=null;
				    Node langnode=null;
					Node lang= null;
						
						JSONObject langobj= new JSONObject();
					if(clsNode.hasNode("Multilingual Data")) {
							 multilang= clsNode.getNode("Multilingual Data");
							 NodeIterator mulilangIterator= multilang.getNodes();
								JSONArray langArr= new JSONArray();
								while (mulilangIterator.hasNext()) {
								 langobj= new JSONObject();
									langnode= mulilangIterator.nextNode();	
									String langname= langnode.getName();
								//	out.print("ll-- "+langname);
									if(multilang.hasNode(langname)) {
										lang= multilang.getNode(langname);
										langobj.put("language", langname);
										
										if(lang.hasNode("Clause Description")) {
											clsdesNode= lang.getNode("Clause Description");
											
											//out.println("clsdesNode: "+ clsdesNode);
											if(clsdesNode.hasNode("Paragraph")) {
												paraNode= clsdesNode.getNode("Paragraph");
												langobj.put("type", "online");
												
												NodeIterator paraiterator = paraNode.getNodes();
												JSONArray paraArr= new JSONArray();
												while (paraiterator.hasNext()) {
													paracntNode= paraiterator.nextNode();
													
													String parades= paracntNode.getProperty("Description").getString();
													paraArr.put(parades);	
													
												}
//												retobj.put("Clause Description", paraArr);
												langobj.put("para", paraArr);
												langArr.put(langobj);
												
											}else if (clsdesNode.hasNode("File")) {
												fileNode= clsdesNode.getNode("File");
												langobj.put("type", "file");
												
												NodeIterator fileIterator= fileNode.getNodes();
												while (fileIterator.hasNext()) {
													file= fileIterator.nextNode();
													
													filename= file.getName();
												}
											//	retobj.put("Clause Description", filename);
												langobj.put("filename", filename);
												langobj.put("filedata", "");
												langArr.put(langobj);
												
												
											}						
											
										}else {
										}
										
									}else {}
								}
								retobj.put("Multilingualdata", langArr);
							 
							 
							 
						 }
//					===========
					


					out.println(retobj);
				}else {
					retobj.put("status", "error");
					retobj.put("message", "clauseId not found");
					out.println(retobj);
				}				
				}else {
					retobj.put("status", "error");
					retobj.put("message", "Invalid user");
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

	public Node getClauseNodeByName(String searchText, String email, Node DocigerAdvNode , HttpServletResponse rep) {
		Node tempRulNode = null;
		Session session = null;
		if (!searchText.trim().equals("")) {
			//String newSearch= searchText.replace("%27", "'");
			try {
				session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
				//String querryStr = "select [ClauseName] from [nt:base] where ClauseName=\"" +searchText +  "\" and ISDESCENDANTNODE('/content/user/"+email+"/DocTigerAdvanced/Clauses/')";
				//new ---  String querryStr = "select [ClauseName] from [nt:base] where (contains('ClauseName',\\\"" + searchText + " \"\\))  and ISDESCENDANTNODE('/content/user/" + email + "/DocTigerAdvanced/Clauses/')";
				String querryStr = "select [ClauseName] from [nt:base] where (contains('ClauseName',\\\"" + searchText + " \"\\))  and ISDESCENDANTNODE('"+DocigerAdvNode.getPath()+"/Clauses/')";
				
				
				
				//out.println("querystr: "+querryStr);
				Workspace workspace = session.getWorkspace();
				Query query = workspace.getQueryManager().createQuery(querryStr, Query.JCR_SQL2);
				QueryResult result = query.execute();
				NodeIterator iterator = result.getNodes();
				while (iterator.hasNext()) {
					tempRulNode = iterator.nextNode();
					String clsName= tempRulNode.getProperty("ClauseName").getString();
				     if(clsName.equals(searchText)) {				     
					tempRulNode.setProperty("queryresult", "found");
				     }
				    }
				session.save();
				return tempRulNode;
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

		return tempRulNode;

	}
	}
