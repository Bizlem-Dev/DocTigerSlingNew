package social.com.app.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;

import com.service.ParseSlingData;
import com.service.ReadHeaderExcel;
import com.service.impl.FreeTrialandCart;
import com.service.impl.ParseSlingDataImpl;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
	@Property(name = "service.vendor", value = "VISL Company"),
	@Property(name = "sling.servlet.paths", value = { "/servlet/service/EditMailTemplate" }),
	@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
	@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
			"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
	"productEdit" }) })
@SuppressWarnings("serial")
public class EditMailTemplateServ extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;	

	//@Reference
	//private ParseSlingData parseSlingData;
	ParseSlingData parseSlingData= new ParseSlingDataImpl();

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		JSONObject js = new JSONObject();

		try {
			Session session = null;
			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));

			Node tempnode = null;
			Node doctiger = null;

			String usrid = request.getParameter("email");
			String group = request.getParameter("group");

			String template = request.getParameter("mailtemplate");

			FreeTrialandCart cart= new FreeTrialandCart();
			String freetrialstatus=cart.checkfreetrial(usrid);

			doctiger =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  usrid, group,session, response );
			if(doctiger!=null) {
				Node communicationNode = null;
				if (doctiger.hasNode("Communication")) {
					communicationNode = doctiger.getNode("Communication");
				}
				Node mailtemplateNode = null;
				if (communicationNode.hasNode("MailTemplate")) {
					mailtemplateNode = communicationNode.getNode("MailTemplate");
				} else {
					mailtemplateNode = communicationNode.addNode("MailTemplate");
				}
				if (mailtemplateNode.hasNode(template)) {
					tempnode = mailtemplateNode.getNode(template);
				} else {
					js.put("status", "error");
					js.put("message", "No template found to edit");
					out.println(js);
				}

				String created_Date = "";
				long flag = 0;
				String version = "";
				String metadata = "";
				String description = "";

				if (tempnode.hasProperty("Creation Date")) {
					created_Date = tempnode.getProperty("Creation Date").getString();
				}
				if (tempnode.hasProperty("Flag")) {
					flag = tempnode.getProperty("Flag").getLong();
				}
				if (tempnode.hasProperty("Version")) {
					version = tempnode.getProperty("Version").getString();
				}
				if (tempnode.hasProperty("Metadata")) {
					metadata = tempnode.getProperty("Metadata").getString();
				}
				if (tempnode.hasProperty("Description")) {
					description = tempnode.getProperty("Description").getString();
				}
				String Body = "";
				String From = "";
				String Subject = "";
				String To = "";
				if (tempnode.hasProperty("Body")) {
					Body = tempnode.getProperty("Body").getString();
				}
				if (tempnode.hasProperty("From")) {
					From = tempnode.getProperty("From").getString();
				}
				if (tempnode.hasProperty("Subject")) {
					Subject = tempnode.getProperty("Subject").getString();
				}
				if (tempnode.hasProperty("To")) {
					To = tempnode.getProperty("To").getString();
				}
				Node sfretNode = null;
				Node sfgetNode = null;

				String sfName = "";

				JSONObject sfret = new JSONObject();
				JSONObject prikeyobj = new JSONObject();

				if (tempnode.hasNode("SF Object")) {
					sfretNode = tempnode.getNode("SF Object");
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

					prikeyobj.put("Object", obj_prikey);
					prikeyobj.put("key", sfpriKey);

					sfret.put("Primary Key", prikeyobj);
				}
				JSONArray mainexpar=null;
				Node extparam = null;
				if (tempnode.hasNode("externalparamobject")) {
					extparam = tempnode.getNode("externalparamobject");

					JSONObject exprmob = new JSONObject();
					String type = "";
					if (extparam.hasNode("ws")) {
						type = "ws";
					}

					Node wsnode1 = null;

					mainexpar = new JSONArray();

					JSONObject subinpjs = new JSONObject();
					JSONObject subopjs = new JSONObject();

					if (extparam.hasNode("ws")) {
						wsnode1 = extparam.getNode("ws");

						NodeIterator iterator = wsnode1.getNodes();

						Node wnode = null;
						Node wsct = null;
						Node inpnode = null;
						while (iterator.hasNext()) {
							wnode = iterator.nextNode();
							exprmob = new JSONObject();
							String wsname = wnode.getName().toString();

							if (wsnode1.hasNode(wsname)) {
								wsct = wsnode1.getNode(wsname);

								exprmob.put("type", type);

								String primerykey = "";
								if (wsct.hasProperty("primerykey")) {
									primerykey = wsct.getProperty("primerykey").getString();
									exprmob.put("primerykey", primerykey);
								}
								String url = "";
								String token = "";
								String username = "";
								String password = "";
								if (wsct.hasProperty("url")) {
									url = wsct.getProperty("url").getString();
									exprmob.put("url", url);
								}
								if (wsct.hasProperty("token")) {
									token = wsct.getProperty("token").getString();
									exprmob.put("token", token);
								}
								if (wsct.hasProperty("username")) {
									username = wsct.getProperty("username").getString();
									exprmob.put("username", username);
								}
								if (wsct.hasProperty("password")) {
									password = wsct.getProperty("password").getString();
									exprmob.put("password", password);
								}
								if (wsct.hasNode("input")) {
									inpnode = wsct.getNode("input");

									NodeIterator iterator1 = inpnode.getNodes();
									JSONArray inpar = new JSONArray();

									Node inct = null;
									Node inpt = null;
									while (iterator1.hasNext()) {
										inct = iterator1.nextNode();
										subinpjs = new JSONObject();
										String inp = inct.getName().toString();

										if (inpnode.hasNode(inp)) {
											inpt = inpnode.getNode(inp);

											String fieldname = inpt.getProperty("fieldname").getString();
											String fieldtype = inpt.getProperty("fieldtype").getString();
											String fieldlength = inpt.getProperty("fieldlength").getString();
											subinpjs.put("fieldname", fieldname);
											subinpjs.put("fieldtype", fieldtype);
											subinpjs.put("fieldlength", fieldlength);
											inpar.put(subinpjs);
										}
									}
									exprmob.put("input", inpar);
								}
								Node opnode = null;
								if (wsct.hasNode("output")) {
									opnode = wsct.getNode("output");

									NodeIterator iterator2 = opnode.getNodes();
									JSONArray opar = new JSONArray();

									Node oct = null;
									Node opt = null;
									while (iterator2.hasNext()) {
										oct = iterator2.nextNode();
										subopjs = new JSONObject();
										String inp = oct.getName().toString();

										if (opnode.hasNode(inp)) {
											opt = opnode.getNode(inp);

											String fieldname = opt.getProperty("fieldname").getString();
											String fieldtype = opt.getProperty("fieldtype").getString();
											String fieldlength = opt.getProperty("fieldlength").getString();
											subopjs.put("fieldname", fieldname);
											subopjs.put("fieldtype", fieldtype);
											subopjs.put("fieldlength", fieldlength);
											opar.put(subopjs);
										}
									}
									exprmob.put("output", opar);
								}
								mainexpar.put(exprmob);
							}
						}
					}

					try {
						Node filenode = null;
						if (extparam.hasNode("file")) {
							filenode = extparam.getNode("file");

							NodeIterator iteratorfile = filenode.getNodes();

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
									JSONObject excelretObj= new JSONObject();

									excelretObj.put("type", "excel");
									excelretObj.put("filename", flname);
									excelretObj.put("primerykey", prikeyobj);
									excelretObj.put("output", hedarr);

									mainexpar.put(excelretObj);
								}
							}
						}else {}
					}catch (Exception e) {
						// TODO: handle exception
					}
				}
				Node Attachmentnode = null;
				String fl = "";
				if (tempnode.hasNode("Attachment")) {

					Attachmentnode = tempnode.getNode("Attachment");

					NodeIterator iterator4 = Attachmentnode.getNodes();

					Node flnode = null;
					while (iterator4.hasNext()) {
						flnode = iterator4.nextNode();
						fl = flnode.getName().toString();
					}
				}
				Node adv = null;
				String selectedRule = "";
				String Ruledata = "";
				String selectedWorkflow = "";
				String selectedController="";
				JSONObject advob = new JSONObject();
				if (tempnode.hasNode("Advanced")) {
					adv = tempnode.getNode("Advanced");
					if (adv.hasProperty("Selected Rule")) {
						selectedRule = adv.getProperty("Selected Rule").getString();
						advob.put("selected Rule", selectedRule);
					}
					if (adv.hasProperty("Ruledata")) {
						Ruledata = adv.getProperty("Ruledata").getString();
						advob.put("Ruledata", Ruledata);
					}
					if (adv.hasProperty("Selected Workflow")) {
						selectedWorkflow = adv.getProperty("Selected Workflow").getString();
						advob.put("Selected Workflow", selectedWorkflow);
					}
					if (adv.hasProperty("Selected Controller")) {
						selectedController = adv.getProperty("Selected Controller").getString();
						advob.put("Selected Controller", selectedController);
					}
				}

				session.save();
				js.put("status", "success");
				js.put("email", usrid);
				js.put("template", template);
				js.put("Creation Date", created_Date);
				js.put("flag", flag);
				js.put("version", version);
				js.put("metadata", metadata);
				js.put("description", description);
				js.put("Body", Body);
				js.put("From", From);
				js.put("Subject", Subject);
				js.put("To", To);
				js.put("SFobject", sfret);
				js.put("externalparamobject", mainexpar);
				js.put("filename", fl);
				js.put("advancedobject", advob);

				out.println(js);
			}else {
				js.put("status", "error");
				js.put("message", "Invalid user");
				out.println(js);}
		} catch (Exception e) {
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