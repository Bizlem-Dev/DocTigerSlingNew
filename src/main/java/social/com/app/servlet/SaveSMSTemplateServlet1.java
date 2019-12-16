package social.com.app.servlet;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/SaveSMSTemplate" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class SaveSMSTemplateServlet1 extends SlingAllMethodsServlet {

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
			String group="";
			email = resultjsonobject.getString("email").trim();
			group = resultjsonobject.getString("group").trim();

			template = resultjsonobject.getString("smstemplatename").trim();
			if (email.equals(null) || email.length() == 0) {
				js.put("status", "error");
				js.put("message", "Please Provide email");
				out.println(js);
			} else {

			}
			
			FreeTrialandCart cart= new FreeTrialandCart();
			String freetrialstatus=cart.checkfreetrial(email);
			
			Node DocTigerAdvance =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  email, group,session, response );
			if(DocTigerAdvance!=null) {
		
			Node tempn = null;
			if (DocTigerAdvance.hasNode("SMSTemplate")) {
				tempn = DocTigerAdvance.getNode("SMSTemplate");

			} else {
				tempn = DocTigerAdvance.addNode("SMSTemplate");
			}
			if (request.getRequestPathInfo().getExtension().equals("Basic")) {

				if (template.equals(null) || template.length() == 0) {
					js.put("status", "error");
					js.put("message", "Please Enter SMS Template Name");
					out.println(js);

				} else {
					// try {
//					String username = resultjsonobject.getString("username");

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
								session.save();
							}

						}
						try {
							String metadata = "";
							String description = "";
							// if(resultjsonobject.has("Metadata") ||
							// !resultjsonobject.getString("Metadata").equals("") ||
							// !resultjsonobject.getString("Metadata").equals(null)){
							metadata = resultjsonobject.getString("Metadata");
							// }else {}
							// resultjsonobject.getString("description");
							// if(resultjsonobject.has("description") ||
							// !resultjsonobject.getString("description").equals("") ||
							// !resultjsonobject.getString("description").equals(null)){
							description = resultjsonobject.getString("Description");
							// }else {}
							String externalparam = resultjsonobject.getString("ExternalParam");

							if (saveType.equals("edit")) {
								if (tempn.hasNode(template)) {
									Template = tempn.getNode(template);
								} else {
									js.put("status", "error");
									js.put("message", "No SMStemplate present to edit");
									out.println(js);
								}

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
							js.put("SMStemplatename", template);

							
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


//				String username = resultjsonobject.getString("username");
				JSONObject sfobj = resultjsonobject.getJSONObject("SFObject");

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
				// if (Template.hasProperty("saveType")) {

				saveType = Template.getProperty("saveType").getString();

				JSONObject pkobj = resultjsonobject.getJSONObject("Primery key");
				String pkobject = pkobj.getString("Object");
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
				js.put("SMStemplatename", template);
				js.put("email", email);
				js.put("saveType", saveType);
				js.put("SFobject", sfobj);
				js.put("Primery key", pkobj);
			}

			if (request.getRequestPathInfo().getExtension().equals("ExternalParameter")) {

//				String username = resultjsonobject.getString("username");

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
				// JSONArray exprar =null;
				JSONArray exprar = new JSONArray();
//				JSONObject exprmob = new JSONObject();
				if (resultjsonobject.has("externalparamobject")) {
					String exparamProp = Template.getProperty("externalparam").getString();

					if (exparamProp.equalsIgnoreCase("true")) {

						if (resultjsonobject.has("externalparamobject")
								&& resultjsonobject.getJSONArray("externalparamobject") != null) {
							JSONArray externalparamobject = resultjsonobject.getJSONArray("externalparamobject");
							JSONObject extprm = null;
							String type = "";

							if (!Template.hasNode("externalparamobject")) {
								extparam = Template.addNode("externalparamobject");
								extparam.setProperty("last_count", 0);

							} else {
								extparam = Template.getNode("externalparamobject");

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

				try {

					Node filenode = null;
					if (extparam.hasNode("file")) {
						filenode = extparam.getNode("file");

						NodeIterator iteratorfile = filenode.getNodes();
						

//					JSONObject jsonfile = new JSONObject("{\"type\":\"excel\",\"primerykey\":\"Accno\",\"output\":[\"amount\",\"invoiceno\"]}") ;

						Node flnode = null;
						while (iteratorfile.hasNext()) {
							flnode = iteratorfile.nextNode();
							JSONArray hedarr = new JSONArray();
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
				js.put("SMStemplatename", template);
				js.put("email", email);
				js.put("saveType", saveType);
				js.put("SFobject", sfret);
				if (exprar.length() != 0) {
					js.put("externalparamobject", exprar);
				}

			}

			if (request.getRequestPathInfo().getExtension().equals("Compose")) {

				//String username = resultjsonobject.getString("username");

				Node Template = null;
				if (tempn.hasNode(template)) {
					Template = tempn.getNode(template);
				} else {
				}
				String from = "";
				String to = "";
				String body = "";

				from = resultjsonobject.getString("from");
				to = resultjsonobject.getString("to");
				body = resultjsonobject.getString("body");
				Template.setProperty("From", from);
				Template.setProperty("To", to);
				Template.setProperty("Body", body);

				js.put("status", "success");
				js.put("message", "SMSTemplate saved successfuly");

			}
			session.save();
			out.println(js);
			}else {
				js.put("status", "error");
				js.put("message", "Invaild user");
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
