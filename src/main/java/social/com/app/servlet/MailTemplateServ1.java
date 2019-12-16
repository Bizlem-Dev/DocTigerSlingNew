package social.com.app.servlet;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Value;
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
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/DTAMailTemp" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class MailTemplateServ1 extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	
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

		Session session = null;
		PrintWriter out = response.getWriter();
		
		String templatename = "";
		String from ="";
		String to = "";
		String subject="";
		String body="";
		String email="";
		String Metadata="";
		String description="";
		String ExternalParam="";
		String SelectedRule="";
		String Ruledata="";
		String SelectedWorkflow="";
		String Selectedcontroller="";
		String advanced= "";
		try {
			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			Node dtaNode=null;
			Node mailtemplateNode= null;
			Node templatenode = null;
			Node communicationNode=null;
			JSONObject emailerror= new JSONObject();

			BufferedInputStream bis = new BufferedInputStream(request.getInputStream());
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			int result = bis.read();
			while (result != -1) {
				buf.write((byte) result);
				result = bis.read();
			}
			String res = buf.toString("UTF-8");
			JSONObject obj = new JSONObject(res);
		//	out.print(obj);
			
			String group  = obj.getString("group");


			if (obj.getString("email").length() == 0) {
				emailerror.put("status", "error");
				emailerror.put("message", "Please Provide email");
				out.println(emailerror);
			} else {
				email = obj.getString("email");
			
				//out.print(email);

			//email= obj.getString("Email").replace("@", "_");
				
				if(obj.getString("mailtemplatename").length() == 0) {
					emailerror=new JSONObject();
					emailerror.put("status", "error");
					emailerror.put("message", "Please Provide MailTemplate Name");
					out.println(emailerror);
				}else {
			
					FreeTrialandCart cart= new FreeTrialandCart();
					String freetrialstatus=cart.checkfreetrial(email);
					
					dtaNode =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  email,group, session, response );
					if(dtaNode!=null) {

			
			if(dtaNode.hasNode("Communication")){
				communicationNode= dtaNode.getNode("Communication");
			}else {
				communicationNode= dtaNode.addNode("Communication");
			}
			
			if (request.getRequestPathInfo().getExtension().equals("basic")) {
				JSONObject basicObj= new JSONObject();
				if(communicationNode.hasNode("MailTemplate")){
					mailtemplateNode= communicationNode.getNode("MailTemplate");
				}else {
					mailtemplateNode= communicationNode.addNode("MailTemplate");
				}
				templatename= obj.getString("mailtemplatename");
				
				String saveType = obj.getString("saveType");
				if (saveType.equals("new") || saveType.equals("edit")) {

					if (saveType.equals("new")) {
						basicObj= new JSONObject();
						if (mailtemplateNode.hasNode(templatename)) {

							basicObj.put("status", "error");
							basicObj.put("message", "Same Mailtemplate exist. Please try with new Template Name");
							out.println(basicObj);

						} else {
							templatenode = mailtemplateNode.addNode(templatename);
							
						

//						if(mailtemplateNode.hasNode(templatename)){
//						templatenode= mailtemplateNode.getNode(templatename);
//					}else {
//						templatenode= mailtemplateNode.addNode(templatename);
//					}
					Metadata= obj.getString("Metadata");
					description= obj.getString("Description");
					ExternalParam= obj.getString("ExternalParam");
					
					templatenode.setProperty("Metadata", Metadata);
					templatenode.setProperty("ExternalParameter", ExternalParam);
					templatenode.setProperty("Description", description);
					Date creationDate= new Date();
					templatenode.setProperty("Flag", 1);
					templatenode.setProperty("Version", 1.0);
					templatenode.setProperty("Creation Date", creationDate.toString());
					//session.save();
					basicObj.put("status", "success");
					basicObj.put("mailtemplatename", templatename);
					
					session.save();
					out.println(basicObj);
						}	
						
					}else if (saveType.equals("edit")) {
						if (mailtemplateNode.hasNode(templatename)) {
							templatenode = mailtemplateNode.getNode(templatename);
							
							Metadata= obj.getString("Metadata");
							description= obj.getString("Description");
							ExternalParam= obj.getString("ExternalParam");
							
							templatenode.setProperty("Metadata", Metadata);
							templatenode.setProperty("ExternalParameter", ExternalParam);
							templatenode.setProperty("Description", description);
							Date creationDate= new Date();
							templatenode.setProperty("Flag", 1);
							templatenode.setProperty("Version", 1.0);
							templatenode.setProperty("Creation Date", creationDate.toString());
							//session.save();
							basicObj.put("status", "success");
							basicObj.put("mailtemplatename", templatename);
							session.save();
							out.println(basicObj);
						} else {
							basicObj.put("status", "error");
							basicObj.put("message", "No template present to edit");
							out.println(basicObj);
						}

					}
				
					

				} else {
					basicObj.put("status", "error");
					basicObj.put("message", "Please enter save Type");
					out.println(basicObj);
				}
				
			}else if (request.getRequestPathInfo().getExtension().equals("sfobj")) {
				JSONObject sfObjRet= new JSONObject();
				if(obj.has("saveType")) {
				String saveType = obj.getString("saveType");
				sfObjRet.put("saveType", saveType);
				}
				sfObjRet.put("email", email);
				if(communicationNode.hasNode("MailTemplate")){
					mailtemplateNode= communicationNode.getNode("MailTemplate");
					templatename= obj.getString("mailtemplatename");
				
					if(mailtemplateNode.hasNode(templatename)){
						templatenode= mailtemplateNode.getNode(templatename);
						Node SFobjNode= null;
						Node sfNode= null;
						
						if(templatenode.hasNode("SF Object")){
					   		SFobjNode= templatenode.getNode("SF Object");						   		
						}else{
							SFobjNode= templatenode.addNode("SF Object");
							}
						
						JSONObject sfobj= obj.getJSONObject("SFObject");

						JSONObject pkobj = obj.getJSONObject("Primery key");
						String pkobject = pkobj.getString("Object");
						String pkkey = pkobj.getString("key");
						SFobjNode.setProperty("Primary key Object", pkobject);
						SFobjNode.setProperty("Primary Key", pkkey);
						
						
						JSONArray keys = sfobj.names();
						
						for (int i = 0; i < keys.length (); ++i) {
							 String key = keys.getString (i); // Here's your key
							  
							JSONArray keysArr= sfobj.getJSONArray(key);
							sfobj.getString (key);
							String [] keystrarr= new String[keysArr.length()];
							for(int j=0; j<keystrarr.length; j++){
								keystrarr[j]= keysArr.getString(j);
							}
						
							
						   	String [] allobjArr= new String[keys.length()];
							
							for(int k=0; k<allobjArr.length; k++){
								allobjArr[k]= keys.getString(k);
							}
							
							
						 	SFobjNode.setProperty("All Objects", allobjArr);
							
						 	
						   	if(SFobjNode.hasNode(key)){
						   		sfNode= SFobjNode.getNode(key);
						   	}else {
								sfNode= SFobjNode.addNode(key);
							}
							
						   	sfNode.setProperty("Fields", keystrarr);
						   	
						   	sfObjRet.put("status", "success");
						   	sfObjRet.put("mailtemplatename", templatename);
						   	sfObjRet.put("SFObject", sfobj);
						   	sfObjRet.put("Primery key", pkobj);
						}
						session.save();
						out.println(sfObjRet);

					}
				}else{
				   	sfObjRet=new JSONObject();
				   	sfObjRet.put("error", "MailTemplate not found");
					out.println(sfObjRet.toString());
				}
				
			}	if (request.getRequestPathInfo().getExtension().equals("exparam")) {

				JSONObject js = new JSONObject();
				
				if(communicationNode.hasNode("MailTemplate")){
					mailtemplateNode= communicationNode.getNode("MailTemplate");
				
					templatename= obj.getString("mailtemplatename");
					
					if(mailtemplateNode.hasNode(templatename)){
						templatenode= mailtemplateNode.getNode(templatename);
				
					
				String saveType= obj.getString("saveType");
				Node extparam = null;
				JSONArray exprar = new JSONArray();
				String exparamProp= templatenode.getProperty("ExternalParameter").getString();
				
				if(exparamProp.equalsIgnoreCase("true")){
				
				if (obj.has("externalparamobject")) {
					JSONArray externalparamobject = obj.getJSONArray("externalparamobject");
					new JSONArray();
					JSONObject extprm = null;
					String type = "";

					if (!templatenode.hasNode("externalparamobject")) {
						extparam = templatenode.addNode("externalparamobject");
						extparam.setProperty("last_count", 0);

					} else {
						extparam = templatenode.getNode("externalparamobject");

					}
					
				
//					 exprmob = new JSONObject();
					for (int i = 0; i < externalparamobject.length(); i++) {
						JSONObject exprmob = new JSONObject();

						extprm = externalparamobject.getJSONObject(i);
						// JSONArray exprar = new JSONArray();
						Node extctnode = null;

						if (extprm.has("type")) {
							type = extprm.getString("type");

						}

						Node wsnode = null;
						// extctnode.setProperty("type", type);
						if (type.equals("ws")) {

							if (!extparam.hasNode("ws")) {
								wsnode = extparam.addNode("ws");
								wsnode.setProperty("last_count", 0);

							} else {
								wsnode = extparam.getNode("ws");

							}
							long lsct = wsnode.getProperty("last_count").getLong();
							if (!wsnode.hasNode(Long.toString(lsct))) {
								extctnode = wsnode.addNode(Long.toString(lsct));

								wsnode.setProperty("last_count", lsct + 1);
								session.save();
							} else {
								extctnode = wsnode.getNode(Long.toString(lsct));

							}

							exprmob.put("type", type);

							if (extprm.has("primerykey")) {
								String primerykey = extprm.getString("primerykey");
								extctnode.setProperty("primerykey", primerykey);
								exprmob.put("primerykey", primerykey);
							}
							if (extprm.has("url")) {
								String url = extprm.getString("url");
								extctnode.setProperty("url", url);

							}
							if (extprm.has("token")) {
								String token = extprm.getString("token");
								extctnode.setProperty("token", token);
							}
							if (extprm.has("username")) {
								String username1 = extprm.getString("username");
								extctnode.setProperty("username", username1);
							}
							if (extprm.has("password")) {
								String password = extprm.getString("password");
								extctnode.setProperty("password", password);
							}

							if (extprm.has("input")) {
								JSONArray inp = extprm.getJSONArray("input");
								JSONObject inpob = null;

								Node inpnode = null;
								if (!extctnode.hasNode("input")) {
									inpnode = extctnode.addNode("input");
									inpnode.setProperty("last_count", 0);
								} else {
									inpnode = extctnode.getNode("input");

								}
								JSONArray inar = new JSONArray();
								for (int in = 0; in < inp.length(); in++) {
									long inpct = inpnode.getProperty("last_count").getLong();
									Node inpctnode = null;
									if (!inpnode.hasNode(Long.toString(inpct))) {
										inpctnode = inpnode.addNode(Long.toString(inpct));
										inpnode.setProperty("last_count", inpct + 1);
									} else {
										inpctnode = extparam.getNode(Long.toString(inpct));

									}

									inpob = inp.getJSONObject(in);

									String fieldname = inpob.getString("fieldname");
									inar.put(fieldname);
									String fieldtype = inpob.getString("fieldtype");
									String fieldlength = inpob.getString("fieldlength");

									inpctnode.setProperty("fieldname", fieldname);
									inpctnode.setProperty("fieldtype", fieldtype);
									inpctnode.setProperty("fieldlength", fieldlength);

								}
								exprmob.put("input", inar);
								// exprar.put(exprmob);

							}

							if (extprm.has("output")) {
								JSONArray outp = extprm.getJSONArray("output");
								JSONObject outpob = null;

								Node opnode = null;
								if (!extctnode.hasNode("output")) {
									opnode = extctnode.addNode("output");
									opnode.setProperty("last_count", 0);
								} else {
									opnode = extctnode.getNode("output");

								}
								JSONArray opar = new JSONArray();
								for (int op = 0; op < outp.length(); op++) {
									long opct = opnode.getProperty("last_count").getLong();

									Node opctnode = null;
									if (!opnode.hasNode(Long.toString(opct))) {
										opctnode = opnode.addNode(Long.toString(opct));
										opnode.setProperty("last_count", opct + 1);
									} else {
										opctnode = extparam.getNode(Long.toString(opct));

									}

									outpob = outp.getJSONObject(op);

									String fieldname = outpob.getString("fieldname");
									opar.put(fieldname);

									String fieldtype = outpob.getString("fieldtype");
									String fieldlength = outpob.getString("fieldlength");

									opctnode.setProperty("fieldname", fieldname);
									opctnode.setProperty("fieldtype", fieldtype);
									opctnode.setProperty("fieldlength", fieldlength);

								}
								exprmob.put("output", opar);
								// js.put("externalparamobject", exprar);

							}
							exprar.put(exprmob);

						}

					}
				}
				}

				Node sfretNode = null;
				Node sfgetNode = null;
				String sfpriKey="";
				String sfName = "";

				JSONObject sfret = new JSONObject();

				if (templatenode.hasNode("SF Object")) {
					sfretNode = templatenode.getNode("SF Object");
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
					 sfpriKey = sfretNode.getProperty("Primary Key").getString();
					String obj_prikey = sfretNode.getProperty("Primary key Object").getString();

					JSONObject prikeyobj = new JSONObject();

					prikeyobj.put("Object", obj_prikey);
					prikeyobj.put("key", sfpriKey);

					sfret.put("Primary Key", prikeyobj);

				}
//				else {
//					js.put("status", "error");
//					js.put("message", "SFobject Not found");
//					out.println(js);
//				}

				Node filenode = null;
		        if (extparam.hasNode("file")) {
		       filenode = extparam.getNode("file");

		       NodeIterator iteratorfile = filenode.getNodes();
		       new JSONObject();

		       new JSONArray();

		       //JSONObject jsonfile = new JSONObject("{\"type\":\"excel\",\"primerykey\":\"Accno\",\"output\":[\"amount\",\"invoiceno\"]}") ;

		       Node flnode = null;
		       while (iteratorfile.hasNext()) {
		        flnode = iteratorfile.nextNode();
		        JSONArray hedarr = new JSONArray();
		        new JSONObject();
		        String filect = flnode.getName().toString();
		        Node fct = null;
		        if (filenode.hasNode(filect)) {
		         fct = filenode.getNode(filect);

		         String flname = fct.getProperty("filename").getString();
		         String filepath = fct.getProperty("filepath").getString();
		         ReadHeaderExcel rm = new ReadHeaderExcel();
try {
		         String resp = rm.callget(filepath);
		         JSONObject jshed = new JSONObject(resp);
		         hedarr = jshed.getJSONArray("Headers");
		        
}catch(Exception e) {
	
JSONObject obj12=new JSONObject();
obj12.put("error", e.getMessage());
//out.print(obj12);
}
JSONObject excelretObj= new JSONObject();
 
		         excelretObj.put("type", "excel");
		         excelretObj.put("filename", flname);
		         excelretObj.put("primerykey", sfpriKey);
		         excelretObj.put("output", hedarr);
		         
		         exprar.put(excelretObj);
		         
		        }
		       }

		    
		      }


				// SFobject
				// jssf.put("Primery key", pkjs);
				js.put("status", "success");
				js.put("mailtemplatename", templatename);
				js.put("email", email);
				js.put("saveType", saveType);

				js.put("externalparamobject", exprar);
				js.put("SFobject", sfret);

			  
				
				//session.save();
				out.println(js);
					}else{
					   	js=new JSONObject();
					   	js.put("ststus", "error");
					   	js.put("message", "MailTemplate not found");
						out.println(js);
					}
					
				}else{
				   	js=new JSONObject();
				   	js.put("ststus", "error");
				   	js.put("message", "MailTemplate not found");
				   	out.println(js);
				}
				session.save();
             }else if(request.getRequestPathInfo().getExtension().equals("compose")){
            	 JSONObject composeobj = new JSONObject();
					if(obj.has("saveType")) {
						 obj.getString("saveType");
						}
					if(!(obj.getString("mailtemplatename").length()==0)) {
					templatename= obj.getString("mailtemplatename");
					
					from= obj.getString("from");
					to= obj.getString("to");
					subject= obj.getString("subject");
					body= obj.getString("body");
					
					Node advancedNode= null;
					if(communicationNode.hasNode("MailTemplate")){
						mailtemplateNode= communicationNode.getNode("MailTemplate");					
						if(mailtemplateNode.hasNode(templatename)){
							templatenode= mailtemplateNode.getNode(templatename);
							templatenode.setProperty("From", from);
							templatenode.setProperty("To", to);
							templatenode.setProperty("Subject", subject);
							templatenode.setProperty("Body", body);
								
							advanced= obj.getString("advanced");
								
							if(advanced.equalsIgnoreCase("true")){
								if(templatenode.hasNode("Advanced")){
									advancedNode= templatenode.getNode("Advanced");
								}else {
									advancedNode= templatenode.addNode("Advanced");
								}
							
								if(obj.has("selectedrule")) {
									SelectedRule= obj.getString("selectedrule");
									advancedNode.setProperty("Selected Rule", SelectedRule);
							
								
								if(obj.has("Ruledata")) {
									Ruledata= obj.getString("Ruledata");
									advancedNode.setProperty("Ruledata", Ruledata);
									

									JSONObject ruledataobj = new JSONObject(Ruledata);
									if(ruledataobj.has("URL")) {
									String URL = ruledataobj.getString("URL");

									try{
									String projectName =URL.substring(URL.indexOf("_")+1, URL.indexOf(SelectedRule)-1) ;
									
									advancedNode.setProperty("RuleProjectName", projectName);		
									//out.println("ParentClauseNode  "+advancenode);
									
									}catch(Exception e) {}
									
									
								}}}
								
								if(obj.has("SelectedWorkflow")) {
									SelectedWorkflow= obj.getString("selectedworkflow");
									advancedNode.setProperty("Selected Workflow", SelectedWorkflow);
								}
							
								if(obj.has("Selectedcontroller")) {
									Selectedcontroller= obj.getString("selectedcontroller");
									advancedNode.setProperty("Selected Controller", Selectedcontroller);
								}
							
							}
								session.save();
							Node attachmentNode= null;
							if(templatenode.hasNode("Attachment")){
								attachmentNode= templatenode.getNode("Attachment");
							}else {
								attachmentNode= templatenode.addNode("Attachment");
							}
							
							String fileName="";
							String fileData="";
							if(obj.has("filename") && !obj.getString("filename").equals("")) {
							fileName= obj.getString("filename");
							fileData= obj.getString("filedata");
							
							byte[] decoded = Base64.decode(fileData);
							InputStream is = null;
							is = new ByteArrayInputStream(decoded);
							Node fileNode=null;
								if(attachmentNode.hasNodes()) {
									NodeIterator fitr= attachmentNode.getNodes();
									while(fitr.hasNext()) {
										
									fileNode=fitr.nextNode();
									fileNode.removeShare();
									fileNode=attachmentNode.addNode(fileName,"nt:file");
									Node jcrNode= null;
									jcrNode = fileNode.addNode("jcr:content","nt:resource");
									jcrNode.setProperty("jcr:data",is);
									jcrNode.setProperty("jcr:mimeType","attach");
									attachmentNode.setProperty("filepath", request.getScheme()+"://"+request.getServerName()+":"+ request.getServerPort()+request.getContextPath()
									+ "/content/user/"+email.replaceAll("@", "_")+"/DocTigerAdvanced/Communication/MailTemplate/"+templatename+ "/Attachment/" + fileName);
									
								}
								}else {
									fileNode = attachmentNode.addNode(fileName,"nt:file");
									Node jcrNode= null;
									jcrNode = fileNode.addNode("jcr:content","nt:resource");
									jcrNode.setProperty("jcr:data",is);
									jcrNode.setProperty("jcr:mimeType","attach");
									attachmentNode.setProperty("filepath", request.getScheme()+"://"+request.getServerName()+":"+ request.getServerPort()+request.getContextPath()
									+ "/content/user/"+email.replaceAll("@", "_")+"/DocTigerAdvanced/Communication/MailTemplate/"+templatename+ "/Attachment/" + fileName);
									
					    		
							}}
						}}
					composeobj=new JSONObject();
					composeobj.put("status", "success");
					composeobj.put("message","MailTemplate Saved Succesfully");
					out.println(composeobj);
				}else{
					composeobj=new JSONObject();
					composeobj.put("status", "error");
					composeobj.put("message","MailTemplate not found");

					out.println(composeobj);
				}
			
			session.save();
				
		}
				}else {
					JSONObject err =new JSONObject();
					err.put("status", "error");
					err.put("message", "Invalid user");
					out.print(err);

				}
				
				}
			}
		} catch (Exception e) {
			JSONObject err =new JSONObject();
			try {
				err.put("status", "error");
				err.put("message", e.getMessage());
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			out.print(err);
			e.printStackTrace();
		}

				}
		}
