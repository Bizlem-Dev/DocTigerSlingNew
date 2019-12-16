package social.com.app.servlet;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;
import javax.servlet.ServletException;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.jcr.api.SlingRepository;

import com.service.ActivateWorkflow;
import com.service.ParseSlingData;
import com.service.impl.FreeTrialandCart;
import com.service.impl.ParseSlingDataImpl;
import com.sun.jersey.core.util.Base64;

import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;


@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/SaveChildClases" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class SaveChildClausesServ extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;

ResourceBundle bundle = ResourceBundle.getBundle("config");
static ResourceBundle bundleststic = ResourceBundle.getBundle("config");
	
	//@Reference
	//private ParseSlingData parseSlingData;
	ParseSlingData parseSlingData= new ParseSlingDataImpl();


	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.println("Test");
	   out.println(request.getRemoteUser());
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
Session session =null;
		
		PrintWriter out= response.getWriter();
		
		Date creationDate = new Date();
		
		//Node userNode=null;
		//Node emailNode=null;
		Node dtaNode=null;
		Node ParentClauseNode=null;
		Node ChildclauseNode= null;
		Node ChildCountNode= null;
		Node clsDesNode= null;
		Node composeNode= null;
		Node paragraphNode= null;
		Node ParaCountNode= null;
		Node fileNode= null;
		Node oldClsNode= null;
		Node oldClsCntNode= null;
		Node oldVerCntNode= null;
						
		String email="";
		String group="";
		String ParentClauseId="";
		String Metadata="";
		String Description="";
		String ClauseName= "";
		String type="";
		String clauseDes="";
		String displayName="";
		String Workflow="";
		String oldVerCnt="";
				
		int count=0; 
		
		JSONObject retObj= new JSONObject();
		
		try {
			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			Workspace ws= null;
			ws= session.getWorkspace();
			
			/*if (session.getRootNode().getNode("content").hasNode("user")) {
				userNode = session.getRootNode().getNode("content").getNode("user");
			} 
			else {
				userNode = session.getRootNode().getNode("content").addNode("user");
			}*/
			
			BufferedInputStream bis = new BufferedInputStream(request.getInputStream());
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			int result = bis.read();
			while (result != -1) {
				buf.write((byte) result);
				result = bis.read();
			}
			String res = buf.toString("UTF-8");
			JSONObject obj = new JSONObject(res);
		  
			email= obj.getString("Email").replace("@", "_");
			group= obj.getString("group");

			String email1 = obj.getString("Email").trim();
			FreeTrialandCart cart= new FreeTrialandCart();
			String freetrialstatus=cart.checkfreetrial(email1);
			
		 dtaNode =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  email1, group,session, response );
			if(dtaNode!=null) {
			
			
			if(dtaNode.hasNode("Clauses")){
				dtaNode.getNode("Clauses");			
			}
			else{
				dtaNode.addNode("Clauses");			
			}
			
			String savetype= "";
			savetype= obj.getString("savetype");
			ParentClauseId= obj.getString("ParentClauseId");
			
			if(session.nodeExists(ParentClauseId)) {
				ParentClauseNode= session.getNode(ParentClauseId);
			
				String IsDeleted="";
				if(ParentClauseNode.hasProperty("IsDeleted")) {
					IsDeleted= ParentClauseNode.getProperty("IsDeleted").getString();
					if(IsDeleted.equalsIgnoreCase("true")) {
						retObj.put("status", "error");
						retObj.put("message", "Clause is deleted");
						out.println(retObj);
					}
			}
			else {
				if(savetype.equalsIgnoreCase("new")){
					if(ParentClauseNode.hasNode("ChildClauses")){
						ChildclauseNode= ParentClauseNode.getNode("ChildClauses");
						count=(int) ChildclauseNode.getProperty("ChildCount").getLong();
					}
					else {
						ChildclauseNode= ParentClauseNode.addNode("ChildClauses");
						ChildclauseNode.setProperty("ChildCount", count);
					}
				
					JSONArray childArr;
					if(obj.has("ChildArray")) {
					childArr= obj.getJSONArray("ChildArray");	
					for(int i=0; i<childArr.length(); i++){
						if(ChildclauseNode.hasNode(""+count)){
							ChildCountNode=ChildclauseNode.getNode(""+count);
						}
						else{
							String lastcount=ChildclauseNode.getProperty("ChildCount").getString();
							ChildCountNode=ChildclauseNode.addNode(""+lastcount);
							count++;
							ChildclauseNode.setProperty("ChildCount", count);
						}	
					
						JSONObject childobj= new JSONObject(); 			
						childobj= childArr.getJSONObject(i);
					
						ClauseName=childobj.getString("ClauseName");
						Metadata=childobj.getString("Metadata");
						Description=childobj.getString("Description");
						
						ChildCountNode.setProperty("ChildClauseName", ClauseName);
						ChildCountNode.setProperty("Metadata", Metadata);
						ChildCountNode.setProperty("Description", Description);
						displayName= childobj.getString("DisplayName");
						ChildCountNode.setProperty("Display Name", displayName);
						if(displayName.equalsIgnoreCase("true")) {
							ChildCountNode.setProperty("Display", "2");
						}else {
							ChildCountNode.setProperty("Display", "1");
						}
						ChildCountNode.setProperty("Creation Date", creationDate.toString());
						
						Pattern pattern = Pattern.compile("ChildClauses");
						Matcher matcher = pattern.matcher(ParentClauseId);
						int cnt_childClses= 0;
						while (matcher.find()) {
						    cnt_childClses++;
						}						
						
						if(cnt_childClses==0) {
							ChildCountNode.setProperty("Numbering Style", "1.1");
						}else if (cnt_childClses==1) {
							ChildCountNode.setProperty("Numbering Style", "a");
						}else if (cnt_childClses==2) {
							ChildCountNode.setProperty("Numbering Style", "i");
						}else if (cnt_childClses>=3) {
							ChildCountNode.setProperty("Numbering Style", "1");
						}
//						==
						Node MultiDataNode = null;
						Node LanguageNode = null;
						if (ChildCountNode.hasNode("Multilingual Data")) {
							MultiDataNode = ChildCountNode.getNode("Multilingual Data");
						} else {
							MultiDataNode = ChildCountNode.addNode("Multilingual Data");
						}

						JSONArray MultiLinArr = new JSONArray();
						MultiLinArr = childobj.getJSONArray("Multilingualdata");

						for (int j = 0; j < MultiLinArr.length(); j++) {
							JSONObject lanObj = new JSONObject();
							lanObj = MultiLinArr.getJSONObject(j);
							String language = lanObj.getString("language");
							if (MultiDataNode.hasNode(language)) {
								LanguageNode = MultiDataNode.getNode(language);
							} else {
								LanguageNode = MultiDataNode.addNode(language);
							}

							if (LanguageNode.hasNode("Clause Description")) {
								clsDesNode = LanguageNode.getNode("Clause Description");
							} else {
								clsDesNode = LanguageNode.addNode("Clause Description");
							}

							type = lanObj.getString("type");
							if (type.equalsIgnoreCase("online")) {
								JSONArray paraArr;
								paraArr = lanObj.getJSONArray("para");

								int paraCount = paraArr.length();

								if (clsDesNode.hasNode("Composed Online")) {
									composeNode = clsDesNode.getNode("Composed Online");
								} else {
									composeNode = clsDesNode.addNode("Composed Online");
								}

								if (composeNode.hasNode("Paragraph")) {
									paragraphNode = composeNode.getNode("Paragraph");
								} else {
									paragraphNode = composeNode.addNode("Paragraph");
								}
								paragraphNode.setProperty("ParaCount", paraCount);

								for (int n = 0; n < paraCount; n++) {
									if (paragraphNode.hasNode("" + n)) {
										ParaCountNode = paragraphNode.getNode("" + n);
									} else {
										ParaCountNode = paragraphNode.addNode("" + n);
									}
									clauseDes = paraArr.getString(n);
									ParaCountNode.setProperty("Description", clauseDes);
								}
							} else {
								Node file = null;
								if (clsDesNode.hasNode("File")) {
									file = clsDesNode.getNode("File");
								} else {
									file = clsDesNode.addNode("File");
								}
								String fileName = lanObj.getString("filename");
								String fileData = lanObj.getString("filedata");
								byte[] decoded = Base64.decode(fileData);
								InputStream is = null;
								is = new ByteArrayInputStream(decoded);

								fileNode = file.addNode(fileName, "nt:file");
								Node jcrNode = null;
								jcrNode = fileNode.addNode("jcr:content", "nt:resource");
								jcrNode.setProperty("jcr:data", is);
							}
						}
						
					}//==========
					}

					session.save();
					retObj.put("status", "success");
					retObj.put("message", "ClauseLibrary Saved Successfully");
					out.println(retObj);
				}else if(savetype.equalsIgnoreCase("edit")) {
					String childClauseId= obj.getString("ClauseId");
					ClauseName= obj.getString("ClauseName");
					
					if(dtaNode.hasNode("OldClauses")) {
						oldClsNode= dtaNode.getNode("OldClauses");
					}else {
						oldClsNode= dtaNode.addNode("OldClauses");
					}
					
					String parentClsId="";
					parentClsId= ParentClauseId.substring(ParentClauseId.lastIndexOf("/")+1);
					
					if(oldClsNode.hasNode(parentClsId)) {
						oldClsCntNode= oldClsNode.getNode(parentClsId);
					}else {
						oldClsCntNode= oldClsNode.addNode(parentClsId);
					}
					
					session.save();
					if(oldClsCntNode.hasNodes()) {
						NodeIterator oldclsIterator= oldClsCntNode.getNodes();
						
						while (oldclsIterator.hasNext()) {
							oldVerCntNode= oldclsIterator.nextNode();
						}
						
						oldVerCnt= oldVerCntNode.getName().toString();
						int cnt=Integer.parseInt( oldVerCnt.substring(oldVerCnt.indexOf("v")+1));
						
						cnt++;
						oldVerCntNode= oldClsCntNode.addNode("v"+cnt);
					}else {
						oldVerCntNode= oldClsCntNode.addNode("v"+0);
					}
					
					ws.copy(ws.getName(), "/content/user/doctiger_xyz.com/DocTigerAdvanced/Clauses/"+parentClsId, "/content/user/doctiger_xyz.com/DocTigerAdvanced/OldClauses/"+parentClsId+"/"+oldVerCntNode.getName());
					
					Metadata= obj.getString("Metadata");
					Description= obj.getString("Description");
				
					ChildCountNode= session.getNode(childClauseId);
				
							ChildCountNode.setProperty("ChildClauseName", ClauseName);
							ChildCountNode.setProperty("Metadata", Metadata);
							ChildCountNode.setProperty("Description", Description);
							displayName= obj.getString("DisplayName");
							ChildCountNode.setProperty("Display Name", displayName);
							if(displayName.equalsIgnoreCase("true")) {
								ChildCountNode.setProperty("Display", "2");
							}else {
								ChildCountNode.setProperty("Display", "1");
							}
							
							ChildCountNode.setProperty("Creation Date", creationDate.toString());
							
							
							Node MultiDataNode=null;
							Node LanguageNode=null;
							if (ChildCountNode.hasNode("Multilingual Data")) {
								MultiDataNode = ChildCountNode.getNode("Multilingual Data");
							} else {
								MultiDataNode = ChildCountNode.addNode("Multilingual Data");
							}
							JSONArray MultiLinArr = new JSONArray();
							MultiLinArr = obj.getJSONArray("Multilingualdata");

							for (int i = 0; i < MultiLinArr.length(); i++) {
								JSONObject lanObj = new JSONObject();
								lanObj = MultiLinArr.getJSONObject(i);
								String language = lanObj.getString("language");
								if (MultiDataNode.hasNode(language)) {
									LanguageNode = MultiDataNode.getNode(language);
								} else {
									LanguageNode = MultiDataNode.addNode(language);
								}

								type = lanObj.getString("type");
								if (LanguageNode.hasNode("Clause Description")) {
									clsDesNode = LanguageNode.getNode("Clause Description");
									
									if (type.equalsIgnoreCase("online")) {
										JSONArray paraArr;
										paraArr = lanObj.getJSONArray("para");

										int paraCount = paraArr.length();
										if (clsDesNode.hasNode("File")) {
											fileNode = clsDesNode.getNode("File");
											fileNode.removeShare();
											composeNode = clsDesNode.addNode("Composed Online");
										} else if (clsDesNode.hasNode("Composed Online")) {
											composeNode = clsDesNode.getNode("Composed Online");
											composeNode.removeShare();
											composeNode = clsDesNode.addNode("Composed Online");
										} else {
											composeNode = clsDesNode.addNode("Composed Online");
										}

										if (composeNode.hasNode("Paragraph")) {
											paragraphNode = composeNode.getNode("Paragraph");
										} else {
											paragraphNode = composeNode.addNode("Paragraph");
										}
										paragraphNode.setProperty("ParaCount", paraCount);

										for (int n = 0; n < paraCount; n++) {
											if (paragraphNode.hasNode("" + n)) {
												ParaCountNode = paragraphNode.getNode("" + n);
											} else {
												ParaCountNode = paragraphNode.addNode("" + n);
											}
											clauseDes = paraArr.getString(n);
											ParaCountNode.setProperty("Description", clauseDes);
										}
									} else {
										if (clsDesNode.hasNode("File")) {
											fileNode = clsDesNode.getNode("File");
											fileNode.removeShare();
										} else if (clsDesNode.hasNode("Composed Online")) {
											composeNode = clsDesNode.getNode("Composed Online");
											composeNode.removeShare();
										}

										String fileName = lanObj.getString("filename");
										String fileData = lanObj.getString("filedata");

										byte[] decoded = Base64.decode(fileData);
										InputStream is = null;
										is = new ByteArrayInputStream(decoded);

										Node file = null;
										if (clsDesNode.hasNode("File")) {
											fileNode = clsDesNode.getNode("File");

											NodeIterator fileIterator = fileNode.getNodes();
											while (fileIterator.hasNext()) {
												 String fp = request.getScheme()+"://"+request.getServerName()+":"+
												 request.getServerPort()+request.getContextPath()
									+ "/bin/cpm/nodes/property.bin/content/user/"+email.replaceAll("@", "_")+"/DocTigerAdvanced/Clauses/"+ParentClauseId+"/ChildClauses/"+childClauseId+"/Clause Description/File"+fileName+"/_jcr_content?name=jcr%3Adata";
												fileNode.setProperty("filepath", fp);

												file = fileIterator.nextNode();

												file.removeShare();

												file = fileNode.addNode(fileName, "nt:file");
												Node jcrNode = null;
												jcrNode = file.addNode("jcr:content", "nt:resource");
												jcrNode.setProperty("jcr:data", is);
												jcrNode.setProperty("jcr:mimeType", "attach");
											}
										} else {
											String fp = request.getScheme() + "://" + request.getServerName() + ":"
													+ request.getServerPort() + request.getContextPath()
													+ "/bin/cpm/nodes/property.bin/content/user/"
													+ email.replaceAll("@", "_") + "/DocTigerAdvanced/Clauses/"
													+ ParentClauseId + "/ChildClauses/" + childClauseId
													+ "/Clause Description/File" + fileName
													+ "/_jcr_content?name=jcr%3Adata";
											fileNode = clsDesNode.addNode("File");
											fileNode.setProperty("filepath", fp);

											file = fileNode.addNode(fileName, "nt:file");

											Node jcrNode = null;
											jcrNode = file.addNode("jcr:content", "nt:resource");
											jcrNode.setProperty("jcr:data", is);
											jcrNode.setProperty("jcr:mimeType", "attach");
										}
									}
								}else {
									clsDesNode = LanguageNode.addNode("Clause Description");
								}
							}
						
							
					
					if(obj.has("WorkFlow") && (!obj.getString("WorkFlow").equals("--Select Workflow--")) && (!obj.getString("WorkFlow").equals("")) ) {
						if(dtaNode.hasNode("WorkflowApprovers") && dtaNode.getNode("WorkflowApprovers").hasNode("Clauses") ) {
						Workflow= obj.getString("WorkFlow");
						ParentClauseNode.setProperty("Workflow", Workflow);
						Double v=0.1;
						ParentClauseNode.setProperty("Version", v);
						ParentClauseNode.setProperty("Approved", "false");

						Node Advancednode=null;
						if(ParentClauseNode.hasNode("Advanced")) {	
							 Advancednode =ParentClauseNode.getNode("Advanced");
						}else {
							 Advancednode =ParentClauseNode.addNode("Advanced");
						}
						Node WorkflowNode=null;
						if(Advancednode.hasNode("workflow")) {	
							WorkflowNode =Advancednode.getNode("workflow");
						}else {
							WorkflowNode =Advancednode.addNode("workflow");
						}
											
							Node wktwmp = dtaNode.getNode("WorkflowApprovers").getNode("Clauses") ;
							NodeIterator appitr =wktwmp.getNodes();
							int noofapp =0;
							JSONObject appobj;
							appobj = new JSONObject(
									"{\"requestType\":\"TEMPLATE\",\"numberOfApprover\":1,\"approver1Group\":\"\",\"approver2Group\":\"\",\"approver3Group\":\"\",\"approver4Group\":\"\",\"approver5Group\":\"\"}");
					
							while(appitr.hasNext()) {
								noofapp++;
								Node appnose =appitr.nextNode();
								appobj.put("approver"+noofapp, appnose.getProperty("ApproverName").getString());
								
							}
							appobj.put("numberOfApprover", noofapp);
							//  bundleststic.getString("Jbpm_ip")104.196.49.81
								String urlstr = "http://"+bundleststic.getString("Jbpm_ip")+":8080/kie-server/services/rest/server/containers/com.biz:business-process:6.0/processes/ApprovalWorkflow/instances";
								String wokusername = "kieserver";
								String wokpassword = "kieserver1!";
								String a = new ActivateWorkflow().callPostJSon(urlstr, appobj, wokusername, wokpassword);

								WorkflowNode.setProperty("workflow_Task_Id", a);
								WorkflowNode.setProperty("no_of_approvers", noofapp);
								WorkflowNode.setProperty("current_approver", "1");
								Node approvernd1 = null;
								if(appobj.has("approver1")) {
								String approver1 = appobj.getString("approver1");
		                         if(WorkflowNode.hasNode("1")) {approvernd1 = WorkflowNode.getNode("1");}else {approvernd1 = WorkflowNode.addNode("1");}
								approvernd1.setProperty("approvername", approver1.replace("@", "_"));
								approvernd1.setProperty("approvedstatus", "false");
	              }
	              if(appobj.has("approver2")) {
			                    String approver1 = appobj.getString("approver2");
		                         if(WorkflowNode.hasNode("2")) {approvernd1 = WorkflowNode.getNode("2");}else {approvernd1 = WorkflowNode.addNode("2");}
			                    approvernd1.setProperty("approvername", approver1.replace("@", "_"));
			                    approvernd1.setProperty("approvedstatus", "false");
	             }
	             if(appobj.has("approver3")) {
			                     String approver1 = appobj.getString("approver3");
		                         if(WorkflowNode.hasNode("3")) {approvernd1 = WorkflowNode.getNode("3");}else {approvernd1 = WorkflowNode.addNode("3");}
			                     approvernd1.setProperty("approvername", approver1.replace("@", "_"));
			                     approvernd1.setProperty("approvedstatus", "false");
	             }
	             if(appobj.has("approver4")) {
			                     String approver1 = appobj.getString("approver4");
		                         if(WorkflowNode.hasNode("4")) {approvernd1 = WorkflowNode.getNode("4");}else {approvernd1 = WorkflowNode.addNode("4");}
			                     approvernd1.setProperty("approvername", approver1.replace("@", "_"));
			                     approvernd1.setProperty("approvedstatus", "false");
	             }
	             if(appobj.has("approver5")) {
		                         String approver1 = appobj.getString("approver5");
		                         if(WorkflowNode.hasNode("5")) {approvernd1 = WorkflowNode.getNode("5");}else {approvernd1 = WorkflowNode.addNode("5");}
		                         approvernd1.setProperty("approvername", approver1.replace("@", "_"));
		                         approvernd1.setProperty("approvedstatus", "false");
	             }
							}else {
								Double v=1.1;
								ParentClauseNode.setProperty("Version", v);
								ParentClauseNode.setProperty("Approved", "true");
							}
						}

					
					session.save();
					
					retObj.put("status", "success");
					retObj.put("message", "ClauseLibrary Saved Successfully");
					out.println(retObj);
				}
			}}else {
				retObj.put("status", "error");
				retObj.put("message", "ParentClause Not Found");
				out.println(retObj);
			}
			session.save();
		}else {
			retObj.put("status", "error");
		retObj.put("message", "Invalid user");
		out.println(retObj);}
		
		}catch (Exception e) {
			try {
				retObj.put("status", "error");
				retObj.put("message", e.getMessage());
				out.println(retObj);
			} 
			catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	
		}	
	}
