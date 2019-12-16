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
	@Property(name = "sling.servlet.paths", value = { "/servlet/service/EditSMSTemplate" }),
	@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
	@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
			"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
	"productEdit" }) })
@SuppressWarnings("serial")
public class EditSMSTemplatesServ extends SlingAllMethodsServlet {

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
			Node templatenode = null;
			Node doctiger = null;

			String usrid = request.getParameter("email");
			String group = request.getParameter("group");

			String template = request.getParameter("smstemplate");

			FreeTrialandCart cart= new FreeTrialandCart();
			String freetrialstatus=cart.checkfreetrial(usrid);

			doctiger =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  usrid,group, session, response );
			if(doctiger!=null) {
				if (doctiger.hasNode("SMSTemplate")) {
					templatenode = doctiger.getNode("SMSTemplate");
				}else {}
				if (templatenode.hasNode(template)) {
					tempnode = templatenode.getNode(template);
				} else {
					js.put("status", "error");
					js.put("message", "No template found to edit");
					out.println(js);
				}
				String saveType = "";
				String created_Date = "";
				long flag = 0;
				String version = "";
				String metadata = "";
				String description = "";
				if (tempnode.hasProperty("saveType")) {
					saveType = tempnode.getProperty("saveType").getString();
				}
				if (tempnode.hasProperty("created_Date")) {
					created_Date = tempnode.getProperty("created_Date").getString();
				}
				if (tempnode.hasProperty("flag")) {
					flag = tempnode.getProperty("flag").getLong();
				}
				if (tempnode.hasProperty("version")) {
					version = tempnode.getProperty("version").getString();
				}
				if (tempnode.hasProperty("metadata")) {
					metadata = tempnode.getProperty("metadata").getString();
				}
				if (tempnode.hasProperty("description")) {
					description = tempnode.getProperty("description").getString();
				}
				Node sfretNode = null;
				Node sfgetNode = null;

				String sfName = "";

				JSONObject sfret = new JSONObject();
				JSONObject prikeyobj = new JSONObject();

				if (tempnode.hasNode("SFobject")) {
					sfretNode = tempnode.getNode("SFobject");
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
				Node extparam = null;
				//Node wsnode = null;
				JSONObject exprmob = new JSONObject();
				String type = "";
				try {
					if (tempnode.hasNode("externalparamobject")) {
						extparam = tempnode.getNode("externalparamobject");
					} else {}

					if (extparam.hasNode("ws")) {
						type = "ws";
					} else {}
				} catch (Exception e) {
				}
				Node wsnode1 = null;

				JSONArray mainexpar = new JSONArray();

				JSONObject subinpjs = new JSONObject();
				JSONObject subopjs = new JSONObject();
				try {
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

								String primerykey = wsct.getProperty("primerykey").getString();
								exprmob.put("primerykey", primerykey);
								String url = wsct.getProperty("url").getString();
								exprmob.put("url", url);
								String token = wsct.getProperty("token").getString();
								exprmob.put("token", token);
								String username = wsct.getProperty("username").getString();
								exprmob.put("username", username);
								String password = wsct.getProperty("password").getString();
								exprmob.put("password", password);

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

								mainexpar.put(excelretObj);
							}
						}
					} else {}
				} catch (Exception e) {}

				String Body = "";
				String From = "";
				String To = "";
				if (tempnode.hasProperty("Body")) {
					Body = tempnode.getProperty("Body").getString();
				}
				if (tempnode.hasProperty("From")) {
					From = tempnode.getProperty("From").getString();
				}
				if (tempnode.hasProperty("To")) {
					To = tempnode.getProperty("To").getString();
				}

				session.save();

				js.put("status", "success");
				js.put("email", usrid);
				js.put("template", template);
				js.put("saveType", saveType);
				js.put("created_Date", created_Date);
				js.put("flag", flag);
				js.put("version", version);
				js.put("metadata", metadata);
				js.put("description", description);
				js.put("SFobject", sfret);

				if (mainexpar.length() != 0) {
					js.put("externalparamobject", mainexpar);
				}
				js.put("Body", Body);
				js.put("From", From);
				js.put("To", To);

				out.println(js);
			}else {
				js.put("status", "error");
				js.put("message", "Invalid user");
				out.println(js);
			}
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