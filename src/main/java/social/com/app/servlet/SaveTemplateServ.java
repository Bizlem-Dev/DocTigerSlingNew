package social.com.app.servlet;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import com.sun.jersey.core.util.Base64;
import java.util.Date;
import java.util.ResourceBundle;

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

import com.service.ActivateWorkflow;
import com.service.ParseSlingData;
import com.service.ReadHeaderExcel;
import com.service.saveFileData;
import com.service.impl.FreeTrialandCart;
import com.service.impl.ParseSlingDataImpl;

import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/SaveTemplate" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
//http://35.200.169.114:8082/portal/servlet/service/SaveTemplate
public class SaveTemplateServ extends SlingAllMethodsServlet {
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
	   out.println("+bundleststic.getString(\"Sling_ip\")= "+bundleststic.getString("Sling_ip"));
	}
	
	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		PrintWriter out = response.getWriter();
		Session session = null;
		JSONObject js = new JSONObject();
		try {
			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			BufferedInputStream bis = new BufferedInputStream(request.getInputStream());
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			int result = bis.read();

			while (result != -1) {
				buf.write((byte) result);
				result = bis.read();
			}
			String res = buf.toString("UTF-8");
			JSONObject resultjsonobject = new JSONObject(res);
			String template = "";
			String email = "";
			String SFEmail = "";
			String group="";
			email = resultjsonobject.getString("email").trim();
			group = resultjsonobject.getString("group").trim();

			FreeTrialandCart cart= new FreeTrialandCart();
			String freetrialstatus=cart.checkfreetrial(email);
			
			Node DocTigerAdvance =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  email, group,session, response );
			if(DocTigerAdvance!=null) {
				template = resultjsonobject.getString("templatename").trim();
				if (request.getRequestPathInfo().getExtension().equals("Basic")) {
					SFEmail = resultjsonobject.getString("SFEmail").trim();
					if (template.equals(null) || template.length() == 0) {
						js.put("status", "error");
						js.put("message", "Please Enter Template Name");
						out.println(js);
					} else {
						if (email.equals(null) || email.length() == 0) {
							js.put("status", "error");
							js.put("message", "Please Provide email");
							out.println(js);
						} else {
						}
						Node tempn = null;
						if (DocTigerAdvance.hasNode("TemplateLibrary")) {
							tempn = DocTigerAdvance.getNode("TemplateLibrary");
						} else {
							tempn = DocTigerAdvance.addNode("TemplateLibrary");
						}
						
						String saveType = resultjsonobject.getString("saveType");
						Node Template = null;
						if (saveType.equals("new") || saveType.equals("edit")) {
						if (saveType.equals("new")) {
							if (tempn.hasNode(template)) {
								js.put("status", "error");
								js.put("message", "Same template exist. Please try with new Template Name");
								out.println(js);
							} else {
								Template = tempn.addNode(template);
								Template.setProperty("SFEmail", SFEmail);
								session.save();
							}
						}
						try {
							String metadata = "";
							String description = "";
							metadata = resultjsonobject.getString("metadata");
							resultjsonobject.getString("description");
							description = resultjsonobject.getString("description");
							
							String externalparam = resultjsonobject.getString("externalparam");

							if (saveType.equals("edit")) {
								if (tempn.hasNode(template)) {
									Template = tempn.getNode(template);
								} else {
									js.put("status", "error");
									js.put("message", "No template present to edit");
									out.println(js);
								}
							}
							String tempurl="";
							if(Template.hasProperty("TemplateUrl")) {
//								Template.setProperty("TemplateUrl", url);
								tempurl=Template.getProperty("TemplateUrl").getString();
							}
							
							long flag = 1;
							Date d1 = new Date();
							Template.setProperty("saveType", saveType);

							Template.setProperty("created_Date", d1.toString());
							Template.setProperty("flag", flag);
							Double v = 0.1;
							Template.setProperty("version", v);

							Template.setProperty("metadata", metadata);
							Template.setProperty("externalparam", externalparam);
							Template.setProperty("description", description);
							
							js.put("status", "success");
							js.put("saveType", saveType);
							js.put("email", email);
							js.put("templatename", template);
							js.put("templateurl", tempurl);
							session.save();
							out.println(js);
						} catch (Exception e) {
							// TODO: handle exception
						}
					} else {
						js.put("status", "error");
						js.put("message", "Please enter Template Type");
						out.println(js);
					}
				}
			}

			if (request.getRequestPathInfo().getExtension().equals("SFObj")) {
				Node tempn = null;
				if (DocTigerAdvance.hasNode("TemplateLibrary")) {
					tempn = DocTigerAdvance.getNode("TemplateLibrary");
				}
				JSONObject sfobj = resultjsonobject.getJSONObject("SFobject");

				Node Template = null;
				if (tempn.hasNode(template)) {
					Template = tempn.getNode(template);
				}
				Node sfob = null;
				if (!Template.hasNode("SFobject")) {
					sfob = Template.addNode("SFobject");
				} else {
					sfob = Template.getNode("SFobject");
				}

				String saveType = "";
				saveType = Template.getProperty("saveType").getString();

				JSONObject pkobj = resultjsonobject.getJSONObject("Primery key");
				String pkobject = pkobj.getString("object");
				String pkkey = pkobj.getString("key");
				sfob.setProperty("primeryKeyObject", pkobject);
				sfob.setProperty("primeryKey", pkkey);

				Node sfNode = null;

				JSONArray keys = sfobj.names();
				for (int i = 0; i < keys.length(); ++i) {
					String key = keys.getString(i); // Here's your key

					JSONArray keysArr = sfobj.getJSONArray(key);
					sfobj.getString(key);
					String[] keystrarr = new String[keysArr.length()];
					for (int j = 0; j < keystrarr.length; j++) {
						keystrarr[j] = keysArr.getString(j);
					}

					String[] allobjArr = new String[keys.length()];

					for (int k = 0; k < allobjArr.length; k++) {
						allobjArr[k] = keys.getString(k);
					}
					sfob.setProperty("All Objects", allobjArr);

					if (sfob.hasNode(key)) {
						sfNode = sfob.getNode(key);
					} else {
						sfNode = sfob.addNode(key);
					}

					sfNode.setProperty("Fields", keystrarr);
				}

				js.put("status", "success");
				js.put("templatename", template);
				js.put("email", email);
				js.put("saveType", saveType);
				js.put("SFobject", sfobj);
				js.put("Primery key", pkobj);
				session.save();
				out.println(js);
			}

			if (request.getRequestPathInfo().getExtension().equals("ExternalParameter")) {
				Node tempn = null;
				if (DocTigerAdvance.hasNode("TemplateLibrary")) {
					tempn = DocTigerAdvance.getNode("TemplateLibrary");
				}

				Node Template = null;
				if (tempn.hasNode(template)) {
					Template = tempn.getNode(template);
				}

				Node sfretNode = null;
				Node sfgetNode = null;

				String sfName = "";

				JSONObject sfret = new JSONObject();
				JSONObject prikeyobj = new JSONObject();

				if (Template.hasNode("SFobject")) {
					sfretNode = Template.getNode("SFobject");
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
					String sfpriKey = sfretNode.getProperty("primeryKey").getString();
					String obj_prikey = sfretNode.getProperty("primeryKeyObject").getString();

					prikeyobj.put("Object", obj_prikey);
					prikeyobj.put("key", sfpriKey);

					sfret.put("Primary Key", prikeyobj);
				}
				String saveType = "";
				if (Template.hasProperty("saveType")) {
					saveType = Template.getProperty("saveType").getString();
				}

				Node extparam = null;
				JSONArray exprar = new JSONArray();
				if (resultjsonobject.has("externalparamobject")) {
					String exparamProp = Template.getProperty("externalparam").getString();

					if (exparamProp.equalsIgnoreCase("true")) {

						if (resultjsonobject.has("externalparamobject")&& resultjsonobject.getJSONArray("externalparamobject") != null) {
							JSONArray externalparamobject = resultjsonobject.getJSONArray("externalparamobject");
							JSONObject extprm = null;
							String type = "";

							if (!Template.hasNode("externalparamobject")) {
								extparam = Template.addNode("externalparamobject");
								extparam.setProperty("last_count", 0);
							} else {
								extparam = Template.getNode("externalparamobject");
							}
							for (int i = 0; i < externalparamobject.length(); i++) {
								JSONObject exprmob = new JSONObject();

								extprm = externalparamobject.getJSONObject(i);
								Node extctnode = null;

								if (extprm.has("type")) {
									type = extprm.getString("type");
								}

								Node wsnode = null;
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
						} else {
						}
					} else {
					}

				} else {
					//

				}
//Download the documnet on UI
				try {

					Node filenode = null;
					if (extparam.hasNode("file")) {
						filenode = extparam.getNode("file");

						NodeIterator iteratorfile = filenode.getNodes();
						//JSONObject jsondata = new JSONObject();

						//JSONArray array = new JSONArray();

//					JSONObject jsonfile = new JSONObject("{\"type\":\"excel\",\"primerykey\":\"Accno\",\"output\":[\"amount\",\"invoiceno\"]}") ;

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

								exprar.put(excelretObj);
							}

						}
					} else {
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				js.put("status", "success");
				js.put("templatename", template);
				js.put("email", email);
				js.put("saveType", saveType);
				js.put("SFobject", sfret);
				
				if (exprar.length() != 0) {
					js.put("externalparamobject", exprar);
				}
				session.save();
				out.println(js);

			}

			if (request.getRequestPathInfo().getExtension().equals("Compose")) {

				Node tempn = null;
				if (DocTigerAdvance.hasNode("TemplateLibrary")) {
					tempn = DocTigerAdvance.getNode("TemplateLibrary");
				}

				//String username = resultjsonobject.getString("username");

				Node Template = null;
				if (tempn.hasNode(template)) {
					Template = tempn.getNode(template);
				}

				if (resultjsonobject.has("uploadtemplate") && !resultjsonobject.isNull("uploadtemplate")) {

					JSONObject uploadtempobj = resultjsonobject.getJSONObject("uploadtemplate");
					// if(( uploadtempobj.has("filename") || !uploadtempobj.isNull("filename") ||
					// !uploadtempobj.getString("filename").trim().isEmpty()) && (
					// uploadtempobj.has("filedata") || !uploadtempobj.isNull("filedata") ||
					// !uploadtempobj.getString("filedata").trim().isEmpty())) {
					String filedata = uploadtempobj.getString("filedata");
					String filename = uploadtempobj.getString("filename");
					int e = filename.indexOf(".");
					
					String ext = filename.substring(e + 1);
					// Download the document on UI
					try {
									
										byte[] bytes = Base64.decode(filedata);
										
										InputStream myInputStream = new ByteArrayInputStream(bytes);
										
										Template.setProperty("filename", filename);
										
										Node sf_object=null;
										if(Template.hasNode("TemplateFile")) {
											sf_object=	Template.getNode("TemplateFile");
										}else {
											sf_object=	Template.addNode("TemplateFile");
										}
										
										//http://35.200.169.114:8082/portal/content/services/freetrial/users/viki_gmail.com/DocTigerAdvanced/TemplateLibrary/Temptest20/TemplateFile/File/Document.docx
										//http://35.200.169.114:8082/portal/content/services/freetrial/users/viki_gmail.com/DocTigerAdvanced/TemplateLibrary/Temptest20/TemplateFile/Document.docx
//										String url = request.getScheme() + "://" + request.getServerName() + ":"
//												+ request.getServerPort() + request.getContextPath()
//												+ "/content/services/freetrial/users/" + email.replace("@", "_") + "/" + "DocTigerAdvanced/"+"TemplateLibrary/"+template+"/TemplateFile" + "/File/"  + filename;
										
										String url = request.getScheme() + "://" + request.getServerName() + ":"
												+ request.getServerPort() + request.getContextPath()
												+ DocTigerAdvance.getPath()+"/TemplateLibrary/"+template+"/TemplateFile" + "/File/"  + filename;
										
										Template.setProperty("TemplateUrl", url);
					//http://35.200.169.114:8082/portal/content/services/freetrial/users/viki_gmail.com/DocTigerAdvanced/TemplateLibrary/TemplateFile/TemplateTest.docx
										Node subfileNode = null;
										Node fileName=null;
										Node jcrNode1 = null;
										if (!sf_object.hasNode("File")) {
											fileName = sf_object.addNode("File");
											fileName.setProperty("file_url", url);

										} else {
											fileName = sf_object.getNode("File");
											fileName.setProperty("file_url", url);
											fileName.remove();
											fileName = sf_object.addNode("File");
											fileName.setProperty("file_url", url);
										}
										// if (!fileName.hasNode(name)) {
										subfileNode = fileName.addNode(filename, "nt:file");

										jcrNode1 = subfileNode.addNode("jcr:content", "nt:resource");

										jcrNode1.setProperty("jcr:data", myInputStream);

										jcrNode1.setProperty("jcr:mimeType", "attach");
					}catch (Exception ex) {
						// TODO: handle exception
					}
					
					saveFileData sfd = new saveFileData();
					String savepath= "/usr/local/tomcat8/apache-tomcat-8.5.35/webapps/ROOT/TemplateLibraryAdvanced/";
					Template.setProperty("fileServerPath", savepath + template);
					//Template.setProperty("filepathWorkflow", "http://35.221.183.246:8082/TemplateLibraryAdvanced/" + template + "." + ext);
					Template.setProperty("filepathWorkflow", "http://"+bundleststic.getString("Sling_ip")+":8082/TemplateLibraryAdvanced/" + template + "." + ext);
					
					//File dir = new File("/home/ubuntu/apache-tomcat-8.5.31/webapps/ROOT/TemplateLibraryAdvanced");
					File dir= new File("/usr/local/tomcat8/apache-tomcat-8.5.35/webapps/ROOT/TemplateLibraryAdvanced");
					if (dir.isDirectory()) {
					} else {
						//new File("/home/ubuntu/apache-tomcat-8.5.31/webapps/ROOT/TemplateLibraryAdvanced").mkdir();
						new File("/usr/local/tomcat8/apache-tomcat-8.5.35/webapps/ROOT/TemplateLibraryAdvanced").mkdir();
					}
					sfd.convertFile(filedata, template + "." + ext, savepath);
					//CreateJsonFile cj = new CreateJsonFile();

					//String jsp = cj.addTemplateToJson(template, ext, "outputFormat", savepath + template,	"docLanguage");

					// }else {}
				} else {
				}
				String advanced = resultjsonobject.getString("advanced");
				Template.setProperty("Advanced", advanced);

				String NumStyle= resultjsonobject.getString("NumStyle");
				Template.setProperty("Numbering Style", NumStyle);
				
				JSONArray clausearr = resultjsonobject.getJSONArray("selectedclause");

				JSONObject selclsobj = new JSONObject();
				for (int i = 0; i < clausearr.length(); i++) {
					selclsobj = clausearr.getJSONObject(i);

					String clsId = selclsobj.getString("ClauseId");
					String clsName = selclsobj.getString("ClauseName");

					Node selclsNode = null;
					Node clsNode = null;

					if (Template.hasNode("Selected Clauses")) {
						selclsNode = Template.getNode("Selected Clauses");
					} else {
						selclsNode = Template.addNode("Selected Clauses");
					}
					if (selclsNode.hasNode(clsId)) {
						clsNode = selclsNode.getNode(clsId);
					} else {
						clsNode = selclsNode.addNode(clsId);
					}

					clsNode.setProperty("ClauseName", clsName);
				}
				/*
				 * String selectedclausearr[] = new String[clausearr.length()]; for (int i = 0;
				 * i < selectedclausearr.length; i++) {
				 * 
				 * selectedclausearr[i] = clausearr.getString(i);
				 * 
				 * JSONObject selclsobj= new JSONObject();
				 * 
				 * } Template.setProperty("selectedclause", selectedclausearr);
				 */
				if (resultjsonobject.has("advancedobject")) {

					Node advancenode = null;
					if (!Template.hasNode("Advanced")) {
						advancenode = Template.addNode("Advanced");

					} else {
						advancenode = Template.getNode("Advanced");
					}

					JSONObject advanc = resultjsonobject.getJSONObject("advancedobject");
			if (advanc.getString("selectedworkflow").trim().length() != 0
							&& advanc.getString("selectedworkflow") != null) {
				if(DocTigerAdvance.hasNode("WorkflowApprovers") && DocTigerAdvance.getNode("WorkflowApprovers").hasNode("Templates") ) {

					
					String Workflow=null;

					Workflow= advanc.getString("selectedworkflow");	
					advancenode.setProperty("selectedWorkflow", Workflow);

					Template.setProperty("Workflow", "true");
					Template.setProperty("Workflow Name", Workflow);

					Double v=0.1;
					Template.setProperty("version", v);
					Template.setProperty("Approved", "false");
					
							
							Node WorkflowNode=null;
							
												
								if(advancenode.hasNode("workflow")) {	
									WorkflowNode =advancenode.getNode("workflow");
								}else {
									WorkflowNode =advancenode.addNode("workflow");
								}
								
									
								Node wktwmp = DocTigerAdvance.getNode("WorkflowApprovers").getNode("Templates") ;
								NodeIterator appitr =wktwmp.getNodes();
								int noofapp =0;
								JSONObject appobj;
								appobj = new JSONObject(
										"{\"requestType\":\"TEMPLATE\",\"approver1Group\":\"\",\"approver2Group\":\"\",\"approver3Group\":\"\",\"approver4Group\":\"\",\"approver5Group\":\"\"}");
						
								while(appitr.hasNext()) {
									noofapp++;
									Node appnose =appitr.nextNode();
									appobj.put("approver"+noofapp, appnose.getProperty("ApproverName").getString());
									
								}
								appobj.put("numberOfApprover", noofapp);
								
									String urlstr = "http://"+bundleststic.getString("Jbpm_ip")+":8080/kie-server/services/rest/server/containers/com.biz:business-process:6.0/processes/ApprovalWorkflow/instances";
									String wokusername = "kieserver";
									String wokpassword = "kieserver1!";
									 ActivateWorkflow ac=new ActivateWorkflow();
									String a =ac.callPostJSon(urlstr, appobj, wokusername, wokpassword);
									WorkflowNode.setProperty("appobj", appobj.toString());
									js.put("a", a);

									WorkflowNode.setProperty("workflow_Task_Id", a);
									WorkflowNode.setProperty("no_of_approvers", noofapp);
									WorkflowNode.setProperty("current_approver", "1");
									Node approvernd1 = null;
									//Node approvernd2 = null;
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
			                         if(WorkflowNode.hasNode("3")) {approvernd1 = WorkflowNode.getNode("3");}else {approvernd1 = WorkflowNode.addNode("2");}

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
									
									//advancenode.setProperty("selectedWorkflow", "false");
									Template.setProperty("Approved", "true");
									Double v = 1.1;
									Template.setProperty("version", v);
								}
						
					
						
					} else {
					//	out.println(" null "+advanc.getString("selectedworkflow"));
						advancenode.setProperty("selectedWorkflow", "false");
						Template.setProperty("Approved", "true");
						Double v = 1.1;
						Template.setProperty("version", v);

					} 
					// String selectedworkflow = advanc.getString("selectedworkflow");
					// advancenode.setProperty("selectedWorkflow", selectedworkflow);
						
					if (advanc.has("selectedrule") && (!advanc.getString("selectedrule").equals("--Select Rule--")) && (!advanc.getString("selectedrule").equals(""))){

						String selectedrule = advanc.getString("selectedrule");
						advancenode.setProperty("selectedRule", selectedrule);
					

					if (advanc.has("Ruledata") && (!advanc.getString("Ruledata").equals(""))) {
						
							
						String Ruledata = advanc.getString("Ruledata");
						advancenode.setProperty("Ruledata", Ruledata);
					

						JSONObject ruledataobj = new JSONObject(Ruledata);
						if(ruledataobj.has("URL")) {
						String URL = ruledataobj.getString("URL");

						try{
						String projectName =URL.substring(URL.indexOf("_")+1, URL.indexOf(selectedrule)-1) ;
						
						advancenode.setProperty("RuleProjectName", projectName);		
						//out.println("ParentClauseNode  "+advancenode);
					
						}catch(Exception e) {
							out.println(e.getMessage());
						}
						}

					}}

					if (advanc.has("selecteddocumentworkflow")) {
						String selectedworkflowForDoc = advanc.getString("selecteddocumentworkflow");
						advancenode.setProperty("selectedWorkflowForDoc", selectedworkflowForDoc);
					}
				}
				session.save();
				js.put("status", "success");
				js.put("message", "Template saved successfuly");

				out.println(js);

			}
			
		//end of if which check validity	
		}else{
			js.put("status", "error");
			js.put("message", "Invalid User");
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
	
	
	}
