package social.com.app.servlet;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.ResourceBundle;

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
import com.service.ReadHeaderExcel;
import com.service.impl.FreeTrialandCart;
import com.service.impl.ParseSlingDataImpl;
import com.sun.jersey.core.util.Base64;

import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({
 @Property(name = "service.description", value = "Save product Servlet"),
 @Property(name = "service.vendor", value = "VISL Company"),
 @Property(name = "sling.servlet.paths", value = {"/servlet/service/DTANewSaveParent"}),
 @Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
 @Property(name = "sling.servlet.extensions", value = {"hotproducts",  "cat",  "latestproducts",  "brief",  "prodlist",  "catalog",  
		 "viewcart",  "productslist",  "addcart",  "createproduct",  "checkmodelno",  "productEdit" })})
@SuppressWarnings("serial")
public class SaveParentClause extends SlingAllMethodsServlet {

 @Reference
 private SlingRepository repo;
 ResourceBundle bundle = ResourceBundle.getBundle("config");
 static ResourceBundle bundleststic = ResourceBundle.getBundle("config");
// @Reference
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
  PrintWriter out = response.getWriter();

  Session session = null;

  String ClauseName = "";
  String Metadata = "";
  String ExternalParam = "";
  String Description = "";
  int count = 0;
  String ClauseId = "";
  String fileName = "";
  String fileData = "";
  String Workflow = "";
  String Rulebased = "";
  String Ruledata = "";
  String ControllerName = "";

  Date creationDate = new Date();

  Node dtaNode = null;
  Node clauseNode = null;
  Node pclauseCountNode = null;
  Node SFobjNode = null;
  Node sfNode = null;
  Node externalparamNode = null;
  Node inputNode = null;
  Node outputNode = null;
  Node ipcountNode = null;
  Node opcountNode = null;
  Node MultiDataNode = null;
  Node LanguageNode = null;
  Node clauseDesNode = null;
  Node paragraphNode = null;
  Node ParaCountNode = null;
  Node fileNode = null;

  int wsCount = 0;
  int ipcount = 0;
  int opcount = 0;
  //int fileCount = 0;

  BufferedInputStream bis = new BufferedInputStream(request.getInputStream());
  ByteArrayOutputStream buf = new ByteArrayOutputStream();
  int result = bis.read();
  while (result != -1) {
  	buf.write((byte) result);
  	result = bis.read();
  }
  String res = buf.toString("UTF-8");

  JSONObject basicObj = new JSONObject();
  JSONObject sfObjRet = new JSONObject();

  try {
	  session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
  
	  JSONObject obj = new JSONObject(res);
	  
	  String email = obj.getString("Email").replace("@", "_");
	  String email1 = obj.getString("Email").trim();
String group=obj.getString("group");
	  FreeTrialandCart cart= new FreeTrialandCart();
	  String freetrialstatus=cart.checkfreetrial(email1);
	  //out.println("freetrialstatus: "+freetrialstatus);
	  dtaNode =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  email1,group, session, response );
	  if(dtaNode!=null) {
		  ClauseName = obj.getString("ClauseName");
		  clauseNode = getClauseByName(ClauseName, email, dtaNode);
		  //out.println(clauseNode);
		  String saveType = obj.getString("savetype");
		  if (request.getRequestPathInfo().getExtension().equals("basic")) {
			  if (saveType.equalsIgnoreCase("new")) {
				  String SFEmail = obj.getString("SFEmail");
				  if (clauseNode == null) {
					  if (obj.has("Metadata") || !obj.getString("Metadata").equals("") || !obj.getString("Metadata").equals(null)) {
						  Metadata = obj.getString("Metadata");
					  } else {}
					  ExternalParam = obj.getString("ExternalParam");
					  if (obj.has("Description") || !obj.getString("Description").equals("") || !obj.getString("Description").equals(null)) {
						  Description = obj.getString("Description");
					  } else {}
					  if (ClauseName.equals(null) || ClauseName.equals("")) {
						  basicObj = new JSONObject();
						  basicObj.put("Status", "Error");
						  basicObj.put("Messege", "ClauseName should not be Empty");
						  //out.println(basicObj.toString());
					  } else {
						  if (dtaNode.hasNode("Clauses")) {
							  clauseNode = dtaNode.getNode("Clauses");
							  count = (int) clauseNode.getProperty("Count").getLong();
						  } else {
							  clauseNode = dtaNode.addNode("Clauses");
							  clauseNode.setProperty("Count", count);
						  }
						  if (clauseNode.hasNode("" + count)) {
							  pclauseCountNode = clauseNode.getNode("" + count);
						  } else {
							  String lastcount = clauseNode.getProperty("Count").getString();
							  pclauseCountNode = clauseNode.addNode("" + lastcount);
							  count++;
							  clauseNode.setProperty("Count", count);
						  }
						  ClauseId = pclauseCountNode.getName().toString();
						  pclauseCountNode.setProperty("ClauseName", ClauseName);
						  pclauseCountNode.setProperty("SFEmail", SFEmail);
						  pclauseCountNode.setProperty("Metadata", Metadata);
						  pclauseCountNode.setProperty("ExternalParameter", ExternalParam);
						  pclauseCountNode.setProperty("Flag", 1);
						  Double v = 1.1;
						  pclauseCountNode.setProperty("Version", v);
						  pclauseCountNode.setProperty("Creation Date", creationDate.toString());
						  pclauseCountNode.setProperty("Description", Description);
						  pclauseCountNode.setProperty("Save Type", saveType);
						  pclauseCountNode.setProperty("Display", "2");
						  
						  basicObj.put("status", "success");
						  //basicObj.put("ClauseId", ClauseId);
						  basicObj.put("ClauseId", pclauseCountNode.getPath());
						  basicObj.put("ClauseName", ClauseName);
						  basicObj.put("savetype", saveType);
						  //session.save();
						  out.println(basicObj);
					  }
				  } else {
					  basicObj.put("status", "error");
					  basicObj.put("message", "ClauseName already exists");
					  out.println(basicObj);
				  }
			  } else {
				  if (dtaNode.hasNode("Clauses")) {
					  clauseNode = dtaNode.getNode("Clauses");
					  //out.println(clauseNode);
					  ClauseId = obj.getString("ClauseId");
					  //out.println("ClauseId: "+ClauseId);
					  /* if (dtaNode.hasNode("OldClauses")) {
					   *       oldClsNode = dtaNode.getNode("OldClauses");
					   *             out.println(oldClsNode);
					   * } else {
					   *       oldClsNode = dtaNode.addNode("OldClauses");
					   *             out.println(oldClsNode);
						}*/
					  //String parentClsId = ClauseId.substring(ClauseId.lastIndexOf("/") + 1);
					  //out.println("parentClsId: "+parentClsId);

					  /*  if (oldClsNode.hasNode(parentClsId)) {
      						oldClsCntNode = oldClsNode.getNode(parentClsId);
      						      //out.println("1");
      						       *      } else {
      						       *            oldClsCntNode = oldClsNode.addNode(parentClsId);
      						       *                  //out.println("2");
      						       *                       }*/
					  // session.save();
					  /* if (oldClsCntNode.hasNodes()) {
					   *       NodeIterator oldclsIterator = oldClsCntNode.getNodes();
					   *             while (oldclsIterator.hasNext()) {
					   *                    oldVerCntNode = oldclsIterator.nextNode();
					   *                          }
					   *                                oldVerCnt = oldVerCntNode.getName().toString();
					   *                                      int cnt = Integer.parseInt(oldVerCnt.substring(oldVerCnt.indexOf("v") + 1));
					   *                                            //out.println(cnt);
					   *                                                  cnt++;
					   *                                                        oldVerCntNode = oldClsCntNode.addNode("v" + cnt);
					   *                                                             } else {
					   *                                                                   oldVerCntNode = oldClsCntNode.addNode("v" + 0);
					   *                                                                         //out.println("1"+oldVerCntNode.getName());
					   *                                                                              }*/
					       //out.println(oldVerCntNode.getName());
					  //   ws.copy(ws.getName(), "/content/user/doctiger_xyz.com/DocTigerAdvanced/Clauses/" + parentClsId, "/content/user/doctiger_xyz.com/DocTigerAdvanced/OldClauses/" + parentClsId + "/" + oldVerCntNode.getName());

					  if (session.nodeExists(ClauseId)) {
						  pclauseCountNode = session.getNode(ClauseId);
					  }
					  if (obj.has("Metadata") || !obj.getString("Metadata").equals("") || !obj.getString("Metadata").equals(null)) {
						  Metadata = obj.getString("Metadata");
						  //out.println("Metadata: "+Metadata);
					  } else {}
					  ExternalParam = obj.getString("ExternalParam");
					  //out.println("ExternalParam: "+ExternalParam);
					  if (obj.has("Description") || !obj.getString("Description").equals("") || !obj.getString("Description").equals(null)) {
						  Description = obj.getString("Description");
						  //out.println("Description: "+Description);
					  } else {}
					  pclauseCountNode.setProperty("ClauseName", ClauseName);
					  pclauseCountNode.setProperty("Metadata", Metadata);
					  pclauseCountNode.setProperty("ExternalParameter", ExternalParam);
					  pclauseCountNode.setProperty("Flag", 1);
					  if (pclauseCountNode.hasNode("Advanced")) {
						  pclauseCountNode.setProperty("Version", 0.1);
					  } else {
						  Double v = 1.1;
						  pclauseCountNode.setProperty("Version", v);
					  }
					  pclauseCountNode.setProperty("Creation Date", creationDate.toString());
					  pclauseCountNode.setProperty("Description", Description);
					  pclauseCountNode.setProperty("Save Type", saveType);
					  pclauseCountNode.setProperty("Display", "2");
					  
					  basicObj.put("status", "success");
					  basicObj.put("ClauseId", pclauseCountNode.getPath());
					  basicObj.put("ClauseName", ClauseName);
					  basicObj.put("savetype", saveType);
					  
					  out.println(basicObj);
					 }else {
						 
					 }
				  }
			  }else if (request.getRequestPathInfo().getExtension().equals("sfobj")) {
				  String ClauseId1 = obj.getString("ClauseId");
				  String ClauseName1 = obj.getString("ClauseName");
				  if (dtaNode.hasNode("Clauses")) {
					  clauseNode = dtaNode.getNode("Clauses");
					  if (session.nodeExists(ClauseId1)) {
						  pclauseCountNode = session.getNode(ClauseId1);
						  JSONObject sfobj = obj.getJSONObject("SFobject");
						  JSONObject priObj = obj.getJSONObject("Primary Key");
						  JSONArray keys = sfobj.names();
						  for (int i = 0; i < keys.length(); ++i) {
							  String key = keys.getString(i); // Here's your key
							  JSONArray keysArr = sfobj.getJSONArray(key);
							  sfobj.getString(key);
							  String[] keystrarr = new String[keysArr.length()];
							  for (int j = 0; j < keystrarr.length; j++) {
								  keystrarr[j] = keysArr.getString(j);
							  }
							  if (pclauseCountNode.hasNode("SF Object")) {
								  SFobjNode = pclauseCountNode.getNode("SF Object");
							  } else {
								  SFobjNode = pclauseCountNode.addNode("SF Object");
							  }
							  String[] allobjArr = new String[keys.length()];
							  for (int k = 0; k < allobjArr.length; k++) {
								  allobjArr[k] = keys.getString(k);
							  }
							  SFobjNode.setProperty("All Objects", allobjArr);
							  String obj_pri = priObj.getString("Object");
							  String primaryKey = priObj.getString("key");
							  SFobjNode.setProperty("Primary key Object", obj_pri);
							  SFobjNode.setProperty("Primary Key", primaryKey);
							  if (SFobjNode.hasNode(key)) {
								  sfNode = SFobjNode.getNode(key);
							  } else {
								  sfNode = SFobjNode.addNode(key);
							  }
							  sfNode.setProperty("Fields", keystrarr);
							  sfObjRet.put("status", "success");
							  sfObjRet.put("Email", obj.getString("Email"));
							  sfObjRet.put("ClauseId", ClauseId1);
							  sfObjRet.put("ClauseName", ClauseName1);
							  sfObjRet.put("SFObject", sfobj);
							  sfObjRet.put("Primary Key", priObj);
						  }
						  //session.save();
						  out.println(sfObjRet.toString());
					  } else {
						  sfObjRet = new JSONObject();
						  sfObjRet.put("error", "Clause not found");
						  out.println(sfObjRet.toString());
					  }
				  }
			  } else if (request.getRequestPathInfo().getExtension().equals("exparam")) {
				  String ClauseId2 = obj.getString("ClauseId");
				  //JSONObject exparamReturn= new JSONObject();
				  JSONObject exretObj = new JSONObject();
				  JSONArray exretArr = new JSONArray();
				  if (dtaNode.hasNode("Clauses")) {
					  clauseNode = dtaNode.getNode("Clauses");
					  JSONObject exreturn = new JSONObject();
					  if (session.nodeExists(ClauseId2)) {
						  pclauseCountNode = session.getNode(ClauseId2);
						  JSONArray exparamArray = obj.getJSONArray("externalparamobject");
						  String type = "";
						  String priKey = "";
						  JSONArray input;
						  JSONArray output;
						  String fieldinName = "";
						  String fieldinType = "";
						  int fieldinLength = 0;
						  String fieldoutName = "";
						  String fieldoutType = "";
						  int fieldoutLength = 0;
						  
						  JSONObject fieldinobj = new JSONObject();
						  JSONObject fieldoutobj = new JSONObject();
						  
						  Node sfretNode = null;
						  Node sfgetNode = null;
						  
						  String sfName = "";
						  JSONObject sfret = new JSONObject();
						  JSONObject prikeyobj = new JSONObject();
						  
						  if (pclauseCountNode.hasNode("SF Object")) {
							  sfretNode = pclauseCountNode.getNode("SF Object");
							  NodeIterator iteratorSF = sfretNode.getNodes();
							  Value[] data = null;
							  
							  while (iteratorSF.hasNext()) {
								  sfgetNode = iteratorSF.nextNode();
								  sfName = sfgetNode.getName().toString();
								  data = sfgetNode.getProperty("Fields").getValues();
								  JSONArray fieldArr = new JSONArray();
								  
								  for (int i1 = 0; i1 < data.length; i1++) {
									  String values = data[i1].getString();
									  fieldArr.put(values);
									  sfret.put(sfName, fieldArr);
								  }
							  }
							  String sfpriKey = sfretNode.getProperty("Primary Key").getString();
							  String obj_prikey = sfretNode.getProperty("Primary key Object").getString();
							  
							  //JSONObject prikeyobj = new JSONObject();
							  
							  prikeyobj.put("Object", obj_prikey);
							  prikeyobj.put("key", sfpriKey);
							  
							  sfret.put("Primary Key", prikeyobj);
							  out.println("sfret: "+sfret);
						  } else {
							  sfret.put("message", "SFObj not found");
						  }

				Node wsNode = null;
				Node wsCountNode = null;
				for (int i = 0; i < exparamArray.length(); i++) {
					
					JSONObject exparamobj = exparamArray.getJSONObject(i);
					
					type = exparamobj.getString("type");
					priKey = exparamobj.getString("primerykey");
									
					if (type.equalsIgnoreCase("ws")) {
						if (pclauseCountNode.hasNode("External Parameter")) {
							externalparamNode = pclauseCountNode.getNode("External Parameter");
						} else {
							externalparamNode = pclauseCountNode.addNode("External Parameter");
						}
						if (externalparamNode.hasNode("ws")) {
							wsNode = externalparamNode.getNode("ws");
							wsCount = (int) wsNode.getProperty("wsCount").getLong();
						} else {
							wsNode = externalparamNode.addNode("ws");
							wsNode.setProperty("wsCount", wsCount);
						}
						if (wsNode.hasNode("" + wsCount)) {
							wsCountNode = wsNode.getNode("" + wsCount);
						} else {
							String wslastcount = wsNode.getProperty("wsCount").getString();
							wsCountNode = wsNode.addNode("" + wslastcount);
							wsCount++;
							wsNode.setProperty("wsCount", wsCount);
						}
						String url = exparamobj.getString("url");
						String token = exparamobj.getString("token");
						String username = exparamobj.getString("username");
						String password = exparamobj.getString("password");
						input = exparamobj.getJSONArray("input");
						for (int j = 0; j < input.length(); j++) {
							fieldinobj = input.getJSONObject(j);
							
							if (wsCountNode.hasNode("Input")) {
								inputNode = wsCountNode.getNode("Input");
								ipcount = (int) inputNode.getProperty("IPCount").getLong();
							} else {
								inputNode = wsCountNode.addNode("Input");
								inputNode.setProperty("IPCount", ipcount);
							}
							
							if (inputNode.hasNode("" + ipcount)) {
								ipcountNode = inputNode.getNode("" + ipcount);
							} else {
								String iplastcount = inputNode.getProperty("IPCount").getString();
								ipcountNode = inputNode.addNode("" + iplastcount);
								ipcount++;
								inputNode.setProperty("IPCount", ipcount);
							}
							
							fieldinName = fieldinobj.getString("fieldname");
							fieldinType = fieldinobj.getString("fieldtype");
							fieldinLength = fieldinobj.getInt("fieldlength");
							ipcountNode.setProperty("Field Name", fieldinName);
							ipcountNode.setProperty("Field Type", fieldinType);
							ipcountNode.setProperty("Field Length", fieldinLength);
						}
						
						JSONArray opArr = new JSONArray();
						output = exparamobj.getJSONArray("output");
						for (int k = 0; k < output.length(); k++) {
							fieldoutobj = output.getJSONObject(k);
							if (wsCountNode.hasNode("Output")) {
								outputNode = wsCountNode.getNode("Output");
								opcount = (int) outputNode.getProperty("OPCount").getLong();
							} else {
								outputNode = wsCountNode.addNode("Output");
								outputNode.setProperty("OPCount", opcount);
							}
							if (outputNode.hasNode("" + opcount)) {
								opcountNode = outputNode.getNode("" + opcount);
							} else {
								String oplastcount = outputNode.getProperty("OPCount").getString();
								opcountNode = outputNode.addNode("" + oplastcount);
								opcount++;
								outputNode.setProperty("OPCount", opcount);
							}
							fieldoutName = fieldoutobj.getString("fieldname");
							fieldoutType = fieldoutobj.getString("fieldtype");
							fieldoutLength = fieldoutobj.getInt("fieldlength");
							opcountNode.setProperty("Field Name", fieldoutName);
							opcountNode.setProperty("Field Type", fieldoutType);
							opcountNode.setProperty("Field Length", fieldoutLength);
							opArr.put(fieldoutName);
							}
							wsCountNode.setProperty("URL", url);
							wsCountNode.setProperty("Token", token);
							wsCountNode.setProperty("Username", username);
							wsCountNode.setProperty("Password", password);
							wsCountNode.setProperty("Primary Key", priKey);
							wsCountNode.setProperty("Type", type);
							
							exretObj.put("type", type);
							exretObj.put("primerykey", priKey);
							exretObj.put("output", opArr);
							
							exretArr.put(exretObj);
							
							//out.println("exretArr: "+exretArr);
						}
					}
					Node filenode = null;
					if (externalparamNode.hasNode("file")) {
						filenode = externalparamNode.getNode("file");
						//fileCount = (int) filenode.getProperty("last_count").getLong();
						NodeIterator iteratorfile = filenode.getNodes();
						//JSONObject jsondata = new JSONObject();
						
						//JSONArray array = new JSONArray();
						
						//JSONObject jsonfile = new JSONObject("{\"type\":\"excel\",\"primerykey\":\"Accno\",\"output\":[\"amount\",\"invoiceno\"]}") ;
						
						Node flnode = null;
						while (iteratorfile.hasNext()) {
							flnode = iteratorfile.nextNode();
							
							JSONArray hedarr = new JSONArray();
							//JSONObject jsonfile = new JSONObject();
							String filect = flnode.getName().toString();
							Node fct = null;
							if (filenode.hasNode(filect)) {
								fct = filenode.getNode(filect);
								String flname = fct.getProperty("filename").getString();
								String filepath = fct.getProperty("filepath").getString();
								ReadHeaderExcel rm = new ReadHeaderExcel();
								String resp = rm.callget(filepath);
								JSONObject jshed = new JSONObject(resp);
								hedarr = jshed.getJSONArray("Headers");
								JSONObject excelretObj = new JSONObject();
								excelretObj.put("type", "excel");
								excelretObj.put("filename", flname);
								excelretObj.put("primerykey", prikeyobj);
								excelretObj.put("output", hedarr);
								exretArr.put(excelretObj);
							}
						}
					} else {
						sfret.put("", "");
					}
					exreturn.put("status", "success");
					exreturn.put("Email", obj.getString("Email"));
					exreturn.put("ClauseId", ClauseId2);
					exreturn.put("ClauseName", obj.getString("ClauseName"));
					exreturn.put("SFobject", sfret);
					exreturn.put("externalparamobject", exretArr);
					
					//out.println("ClauseId2: "+ClauseId2);
					//session.save();
					out.println(exreturn);
					
			} else {
				exreturn.put("status", "success");
				exreturn.put("message", "ClauseNode not found");
				out.println(exreturn);
			}
	    }else {
			//"Clauses" node not found.
		}
	} else if (request.getRequestPathInfo().getExtension().equals("compose")) {
		 String ClauseId3 = obj.getString("ClauseId");
		 //out.println("ClauseId3: " + ClauseId3);
		 JSONObject composeReturn = new JSONObject();
		 if (dtaNode.hasNode("Clauses")) {
		  clauseNode = dtaNode.getNode("Clauses");
		  if (session.nodeExists(ClauseId3)) {
		   pclauseCountNode = session.getNode(ClauseId3);

		   //out.println(pclauseCountNode);
		   if (pclauseCountNode.hasNode("Multilingual Data")) {
		    MultiDataNode = pclauseCountNode.getNode("Multilingual Data");
		   } else {
		    MultiDataNode = pclauseCountNode.addNode("Multilingual Data");
		   }
		   //session.save();

		   JSONArray multilanguageArr = new JSONArray();
		   multilanguageArr = obj.getJSONArray("Multilingualdata");
		   for (int i = 0; i < multilanguageArr.length(); i++) {
		    JSONObject lanObj = new JSONObject();
		    lanObj = multilanguageArr.getJSONObject(i);
		    String language = lanObj.getString("language");

		    if (MultiDataNode.hasNode(language)) {
		     LanguageNode = MultiDataNode.getNode(language);
		    } else {
		     LanguageNode = MultiDataNode.addNode(language);
		    }

		    if (LanguageNode.hasNode("Clause Description")) {
		     clauseDesNode = LanguageNode.getNode("Clause Description");
		    } else {
		     clauseDesNode = LanguageNode.addNode("Clause Description");
		    }

		    //JSONObject clausedesobj= obj.getJSONObject("clausedescription");
		    String clausedestype = "";
		    clausedestype = lanObj.getString("type");
		    if (clausedestype.equalsIgnoreCase("online")) {
		     String description = "";
		     JSONArray paraArray = lanObj.getJSONArray("para");
		     int paraCount = paraArray.length();

		     if (clauseDesNode.hasNode("Paragraph")) {
		      paragraphNode = clauseDesNode.getNode("Paragraph");
		     } else {
		      paragraphNode = clauseDesNode.addNode("Paragraph");
		     }

		     paragraphNode.setProperty("ParaCount", paraCount);

		     for (int n = 0; n < paraArray.length(); n++) {
		      if (paragraphNode.hasNode("" + n)) {
		       ParaCountNode = paragraphNode.getNode("" + n);
		      } else {
		       ParaCountNode = paragraphNode.addNode("" + n);
		      }
		      description = paraArray.getString(n);
		      ParaCountNode.setProperty("Description", description);
		     }
		    } else {
		     fileName = lanObj.getString("filename");
		     fileData = lanObj.getString("filedata");
		     byte[] decoded = Base64.decode(fileData);
		     InputStream is = null;
		     is = new ByteArrayInputStream(decoded);
		     Node file = null;

		     if (clauseDesNode.hasNode("File")) {
		      file = clauseDesNode.getNode("File");
		      NodeIterator fileIterator = file.getNodes();
		      while (fileIterator.hasNext()) {
		    	  //String fp=req.getScheme()+"://"+req.getServerName()+":"+ req.getServerPort()+req.getContextPath()+ "/content/user/"+email+"/DocTigerAdvanced/Clauses/"+ClauseId3+"/Clause Description/File/" + fileName;
		    	  //filectnode.setProperty("filepath", request.getScheme()+"://"+request.getServerName()+":"+ request.getServerPort()+request.getContextPath()+ "/content/user/"+email.replaceAll("@", "_")+"/DocTigerAdvanced/Excel/"+lsct+"/"+ subfileNode.getName() + "/" + filename);
		    	  /*String fp = request.getScheme() + "://" + request.getServerName() + ":" +request.getServerPort() + request.getContextPath() +"/bin/cpm/nodes/property.bin/content/user/" + email.replaceAll("@", "_") + "/DocTigerAdvanced/Clauses/" + ClauseId3 + "/Clause Description/File/" + fileName + "/_jcr_content?name=jcr%3Adata";
		    	   * */		    	  
		    	  String fp = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/content/user/"+email.replaceAll("@", "_")+"/DocTigerAdvanced/Clauses/"+ClauseId3+"/Clause Description/File/"+fileName;
		    	   
		    	  //fileNode.setProperty("filepath", fp);

		    	  file.setProperty("filepath", fp);

		       fileNode = fileIterator.nextNode();
		       fileNode.removeShare();
		       fileNode = file.addNode(fileName, "nt:file");
		       Node jcrNode = null;
		       jcrNode = fileNode.addNode("jcr:content", "nt:resource");
		       jcrNode.setProperty("jcr:data", is);
		       jcrNode.setProperty("jcr:mimeType", "attach");
		      }
		     } else {
		      //								String fp=req.getScheme()+"://"+req.getServerName()+":"+ req.getServerPort()+req.getContextPath()
		      //								+ "/content/user/"+email+"/DocTigerAdvanced/Clauses/"+ClauseId3+"/Clause Description/File/" + fileName;
		      String fp = request.getScheme() + "://" + request.getServerName() + ":" +
		       request.getServerPort() + request.getContextPath() +
		       "/bin/cpm/nodes/property.bin/content/user/" + email.replaceAll("@", "_") + "/DocTigerAdvanced/Clauses/" + ClauseId3 + "/Clause Description/File/" + fileName + "/_jcr_content?name=jcr%3Adata";


		      file = clauseDesNode.addNode("File");
		      file.setProperty("filepath", fp);

		      fileNode = file.addNode(fileName, "nt:file");
		      Node jcrNode = null;
		      jcrNode = fileNode.addNode("jcr:content", "nt:resource");
		      jcrNode.setProperty("jcr:data", is);
		      jcrNode.setProperty("jcr:mimeType", "attach");
		     }
		    }

		   }


		  }
		  Node sfretNode = null;
		  Node sfgetNode = null;

		  String sfName = "";

		  JSONObject sfret = new JSONObject();
		  JSONObject prikeyobj = new JSONObject();

		  if (pclauseCountNode.hasNode("SF Object")) {
		   sfretNode = pclauseCountNode.getNode("SF Object");
		   NodeIterator iteratorSF = sfretNode.getNodes();
		   Value[] data = null;

		   while (iteratorSF.hasNext()) {
		    sfgetNode = iteratorSF.nextNode();
		    sfName = sfgetNode.getName().toString();
		    data = sfgetNode.getProperty("Fields").getValues();
		    JSONArray fieldArr = new JSONArray();

		    for (int i = 0; i < data.length; i++) {
		     String values = data[i].getString();
		     fieldArr.put(values);
		     sfret.put(sfName, fieldArr);
		    }
		   }
		   String sfpriKey = sfretNode.getProperty("Primary Key").getString();
		   String obj_prikey = sfretNode.getProperty("Primary key Object").getString();


		   prikeyobj.put("Object", obj_prikey);
		   prikeyobj.put("key", sfpriKey);

		   sfret.put("Primary Key", prikeyobj);
		   composeReturn.put("status", "success");
		   composeReturn.put("Email", obj.getString("Email"));
		   composeReturn.put("ClauseId", ClauseId3);
		   composeReturn.put("ClauseName", obj.getString("ClauseName"));
		   composeReturn.put("SFobject", sfret);
		  } else {
		   composeReturn.put("status", "success");
		   composeReturn.put("Email", obj.getString("Email"));
		   composeReturn.put("ClauseId", ClauseId3);
		   composeReturn.put("ClauseName", obj.getString("ClauseName"));

		  }

		  /*composeReturn.put("status", "success");
		  composeReturn.put("Email", obj.getString("Email"));
		  composeReturn.put("ClauseId", ClauseId3);
		  composeReturn.put("ClauseName", obj.getString("ClauseName"));
		  composeReturn.put("SFobject", sfret);*/

		  String exparamProp = pclauseCountNode.getProperty("ExternalParameter").getString();

		  if (exparamProp.equalsIgnoreCase("true")) {
		   Node expretNode = null;
		   Node wsgetNode = null;
		   Node wsNode = null;
		   JSONArray expretArr = new JSONArray();

		   if (pclauseCountNode.hasNode("External Parameter")) {
		    expretNode = pclauseCountNode.getNode("External Parameter");
		    if (expretNode.hasNode("ws")) {
		     wsNode = expretNode.getNode("ws");

		     JSONObject wsretObj = new JSONObject();

		     NodeIterator iteratorexp = wsNode.getNodes();
		     while (iteratorexp.hasNext()) {
		      wsgetNode = iteratorexp.nextNode();
		      String typeProp = "";
		      String PrikeyProp = "";
		      typeProp = wsgetNode.getProperty("Type").getString();
		      PrikeyProp = wsgetNode.getProperty("Primary Key").getString();

		      wsretObj.put("type", typeProp);
		      wsretObj.put("primerykey", PrikeyProp);

		      JSONArray outArr = new JSONArray();

		      Node opNode = null;
		      Node opcnt = null;
		      if (wsgetNode.hasNode("Output")) {
		       opNode = wsgetNode.getNode("Output");
		       NodeIterator expopcnt = opNode.getNodes();

		       while (expopcnt.hasNext()) {
		        opcnt = expopcnt.nextNode();
		        String outfieldName = opcnt.getProperty("Field Name").getString();
		        outArr.put(outfieldName);
		       }

		      }
		      wsretObj.put("Output", outArr);
		      expretArr.put(wsretObj);
		     }
		    }

		    Node filenode = null;
		    if (expretNode.hasNode("file")) {
		     filenode = expretNode.getNode("file");
		     NodeIterator iteratorfile = filenode.getNodes();
		     //JSONObject jsondata = new JSONObject();

		     //JSONArray array = new JSONArray();

		     //JSONObject jsonfile = new JSONObject("{\"type\":\"excel\",\"primerykey\":\"Accno\",\"output\":[\"amount\",\"invoiceno\"]}") ;

		     Node flnode = null;
		     while (iteratorfile.hasNext()) {
		      flnode = iteratorfile.nextNode();
		      JSONArray hedarr = new JSONArray();
		   //   JSONObject jsonfile = new JSONObject();
		      String filect = flnode.getName().toString();
		      Node fct = null;
		      if (filenode.hasNode(filect)) {
		       fct = filenode.getNode(filect);

		       String flname = fct.getProperty("filename").getString();
		       String filepath = fct.getProperty("filepath").getString();

		       ReadHeaderExcel rm = new ReadHeaderExcel();

		       String resp = rm.callget(filepath);

		       JSONObject jshed = new JSONObject(resp);
		       hedarr = jshed.getJSONArray("Headers");
		       JSONObject excelretObj = new JSONObject();

		       excelretObj.put("type", "excel");
		       excelretObj.put("filename", flname);
		       excelretObj.put("primerykey", prikeyobj);
		       excelretObj.put("output", hedarr);

		       expretArr.put(excelretObj);
		      }

		     }
		    } else {
		     //out.println("FileNode ot found");
		    }
		    composeReturn.put("externalparamobject", expretArr);

		   }
		  }


		  if (obj.has("WorkFlow") && (!obj.getString("WorkFlow").equals("--Select Workflow--")) && (!obj.getString("WorkFlow").equals(""))) {
		   if (dtaNode.hasNode("WorkflowApprovers") && dtaNode.getNode("WorkflowApprovers").hasNode("Clauses")) {


		    Node Advancednode = null;
		    if (pclauseCountNode.hasNode("Advanced")) {
		     Advancednode = pclauseCountNode.getNode("Advanced");
		    } else {
		     Advancednode = pclauseCountNode.addNode("Advanced");
		    }
		    Node WorkflowNode = null;


		    if (Advancednode.hasNode("workflow")) {
		     WorkflowNode = Advancednode.getNode("workflow");
		    } else {
		     WorkflowNode = Advancednode.addNode("workflow");
		    }


		    Workflow = obj.getString("WorkFlow");
		    pclauseCountNode.setProperty("Workflow", Workflow);
		    Double v = 0.1;
		    pclauseCountNode.setProperty("Version", v);
		    pclauseCountNode.setProperty("Approved", "false");


		    Node wktwmp = dtaNode.getNode("WorkflowApprovers").getNode("Clauses");
		    NodeIterator appitr = wktwmp.getNodes();
		    int noofapp = 0;
		    JSONObject appobj;
		    appobj = new JSONObject(
		     "{\"requestType\":\"CLAUSE\",\"approver1Group\":\"\",\"approver2Group\":\"\",\"approver3Group\":\"\",\"approver4Group\":\"\",\"approver5Group\":\"\"}");

		    while (appitr.hasNext()) {
		     noofapp++;
		     Node appnose = appitr.nextNode();
		     appobj.put("approver" + noofapp, appnose.getProperty("ApproverName").getString());

		    }
		    appobj.put("numberOfApprover", noofapp);

		    String urlstr = "http://104.196.49.81:8080/kie-server/services/rest/server/containers/com.biz:business-process:6.0/processes/ApprovalWorkflow/instances";
		    String wokusername = "kieserver";
		    String wokpassword = "kieserver1!";
		    ActivateWorkflow ac = new ActivateWorkflow();
		    // out.println(obj);
		    String a = ac.callPostJSon(urlstr, appobj, wokusername, wokpassword);
		    WorkflowNode.setProperty("obj", appobj.toString());
		    WorkflowNode.setProperty("workflow_Task_Id", a);
		    WorkflowNode.setProperty("no_of_approvers", noofapp);
		    WorkflowNode.setProperty("current_approver", "1");
		    Node approvernd = null;
		    if (appobj.has("approver1")) {
		     String approver = appobj.getString("approver1");
		     if (WorkflowNode.hasNode("1")) {
		      approvernd = WorkflowNode.getNode("1");
		     } else {
		      approvernd = WorkflowNode.addNode("1");
		     }
		     approvernd.setProperty("approvername", approver.replace("@", "_"));
		     approvernd.setProperty("approvedstatus", "false");
		    }
		    if (appobj.has("approver2")) {
		     String approver = appobj.getString("approver2");
		     if (WorkflowNode.hasNode("2")) {
		      approvernd = WorkflowNode.getNode("2");
		     } else {
		      approvernd = WorkflowNode.addNode("2");
		     }
		     approvernd.setProperty("approvername", approver.replace("@", "_"));
		     approvernd.setProperty("approvedstatus", "false");
		    }
		    if (appobj.has("approver3")) {
		     String approver = appobj.getString("approver3");
		     if (WorkflowNode.hasNode("3")) {
		      approvernd = WorkflowNode.getNode("3");
		     } else {
		      approvernd = WorkflowNode.addNode("3");
		     }
		     approvernd.setProperty("approvername", approver.replace("@", "_"));
		     approvernd.setProperty("approvedstatus", "false");
		    }
		    if (appobj.has("approver4")) {
		     String approver = appobj.getString("approver4");
		     if (WorkflowNode.hasNode("4")) {
		      approvernd = WorkflowNode.getNode("4");
		     } else {
		      approvernd = WorkflowNode.addNode("4");
		     }
		     approvernd.setProperty("approvername", approver.replace("@", "_"));
		     approvernd.setProperty("approvedstatus", "false");
		    }
		    if (appobj.has("approver5")) {
		     String approver = appobj.getString("approver5");
		     if (WorkflowNode.hasNode("5")) {
		      approvernd = WorkflowNode.getNode("5");
		     } else {
		      approvernd = WorkflowNode.addNode("5");
		     }
		     approvernd.setProperty("approvername", approver.replace("@", "_"));
		     approvernd.setProperty("approvedstatus", "false");
		    }


		   }
		  } else {
		   Double v = 1.1;
		   pclauseCountNode.setProperty("Version", v);
		  }

		  if (obj.has("RuleBased") && (!obj.getString("RuleBased").equals("--Select Rule--"))) {
		   Rulebased = obj.getString("RuleBased");
		   pclauseCountNode.setProperty("RuleBased", Rulebased);


		   //  out.println(obj.has("Ruledata"));

		   if (obj.has("Ruledata")) {
		    Ruledata = obj.getString("Ruledata");
		    //	out.println("Ruledata: "+Ruledata);
		    //out.print("df"+ ParentClauseNode.setProperty("Ruledata", Ruledata).getString());
		    pclauseCountNode.setProperty("Ruledata", Ruledata);

		    JSONObject ruledataobj = new JSONObject(Ruledata);
		    if (ruledataobj.has("URL")) {
		     String URL = ruledataobj.getString("URL");

		     try {
		      String projectName = URL.substring(URL.indexOf("_") + 1, URL.indexOf(Rulebased) - 1);
		      pclauseCountNode.setProperty("RuleProjectName", projectName);
		      //out.println("ParentClauseNode  "+ParentClauseNode);
		     } catch (Exception e) {}
		    } else {

		    }

		   }
		  }
		  if (obj.has("Controller")) {
		   ControllerName = obj.getString("Controller");
		   pclauseCountNode.setProperty("Controller Name", ControllerName);
		  }

		 }
		 out.println(composeReturn);
		}else {
	   out.println("Use Correct Extension");
   }
	}else { JSONObject ret = new JSONObject();
	  
		    ret.put("status", "error");
		    ret.put("message", "Invalid User");
		    out.println(ret);
		   }
   session.save();
  } catch (Exception e) {
   // TODO Auto-generated catch block
   JSONObject ret = new JSONObject();
   try {
    ret.put("status", "error");
    ret.put("message", "*"+e.getMessage());
    out.println(ret);
   } catch (JSONException e1) {
    // TODO Auto-generated catch block
    e1.printStackTrace();
   }
  }

  //out.println("Test");
 }

 public Node getClauseByName(String searchText, String email, Node DoctigerAdvNode) {
  Node clauseRetNode = null;
  Node clsRetNode=null;
  Session session = null;
  if (!searchText.trim().equals("")) {
   try {
    session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
    
 String querryStr = "select [ClauseName] from [nt:base] where (contains('ClauseName','" + searchText + "'))  and ISDESCENDANTNODE('"+DoctigerAdvNode.getPath()+"/Clauses/')";

    //new----  String querryStr = "select [ClauseName] from [nt:base] where (contains('ClauseName','" + searchText + "'))  and ISDESCENDANTNODE('/content/user/" + email + "/DocTigerAdvanced/Clauses/')";
    //old---   String querryStr = "select [ClauseName] from [nt:base] where ClauseName=\"" + searchText + "\" and ISDESCENDANTNODE('/content/user/" + email + "/DocTigerAdvanced/Clauses/')";

    Workspace workspace = session.getWorkspace();
    Query query = workspace.getQueryManager().createQuery(querryStr, Query.JCR_SQL2);
    QueryResult result = query.execute();
    NodeIterator iterator = result.getNodes();
    while (iterator.hasNext()) {
     clauseRetNode = iterator.nextNode();
     String clsName= clauseRetNode.getProperty("ClauseName").getString();
     if(clsName.equals(searchText)) {
    	 clsRetNode= clauseRetNode;
    	 //clauseRetNode.setProperty("queryresult", "found");
    	 
     }
    }
    
    
    
    //session.save();
    //return clsRetNode;
   } catch (RepositoryException e) {
    // TODO Auto-generated catch block
    // return e.getMessage();

   } catch (Exception e) {
    // TODO Auto-generated catch block
    // return e.getMessage();

   }
  }

  return clsRetNode;

 }
}