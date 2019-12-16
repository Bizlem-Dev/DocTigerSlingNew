package com.service.impl;

import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.service.ParseSlingData;

@Component(configurationFactory = true)
// @Component(name="ParseSlingDataImpl",immediate = false, metatype = true)
@Service(value = ParseSlingData.class)
@Properties({ @Property(name = "ParseSlingData", value = "parse") })
// @Component @Service @Properties(@Property(name = "type",value="http"))
public class ParseSlingDataImpl implements ParseSlingData {
	@Reference
	private SlingRepository repo;
	ResourceBundle bundle = ResourceBundle.getBundle("config");
	static ResourceBundle bundleststic = ResourceBundle.getBundle("config");
	Session session = null;

	public Node getCarrotruleNode(String freetrialstatus, String email, Session session1,
			SlingHttpServletResponse response) {

		PrintWriter out = null;
		out.println("in CarrotRuleFreeTrial");
		
		Node contentNode = null;
		Node appserviceNode = null;
		Node appfreetrialNode = null;
		Node emailNode = null;
		Node CarrotruleMainNode = null;

		Node serviceid_userNode = null;
		Node adminserviceidNode = null;
		String adminserviceid = "";

		try {
			out = response.getWriter();

			out.println("freetrialstatus " + freetrialstatus);
			out.println("email " + email);

			// session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			if (session1.getRootNode().hasNode("content")) {
				contentNode = session1.getRootNode().getNode("content");
			} else {
				contentNode = session1.getRootNode().addNode("content");
			}
			out.println("contentNode " + contentNode);

			if (freetrialstatus.equalsIgnoreCase("0")) {

				if (contentNode.hasNode("services")) {
					appserviceNode = contentNode.getNode("services");

					out.println("appserviceNode " + appserviceNode);

					if (appserviceNode.hasNode("freetrial")) {
						appfreetrialNode = appserviceNode.getNode("freetrial");

						out.println("appfreetrialNode " + appfreetrialNode);

						if (appfreetrialNode.hasNode("users")
								&& appfreetrialNode.getNode("users").hasNode(email.replace("@", "_"))) {
							emailNode = appfreetrialNode.getNode("users").getNode(email.replace("@", "_"));
							out.println("emailNode " + emailNode);
							if (emailNode.hasNode("CarrotruleMainNode")) {
								CarrotruleMainNode = emailNode.getNode("CarrotruleMainNode");
							} else {
								CarrotruleMainNode = emailNode.addNode("CarrotruleMainNode");
							}
							out.println("CarrotruleMainNode " + CarrotruleMainNode);

						} else {
							// emailNode=appfreetrialNode.getNode("users").addNode(email.replace("@", "_"));
						}
					} else {
						// appfreetrialNode=appserviceNode.addNode("freetrial");
					}
				} else {
					// appserviceNode=contentNode.addNode("services");
				}
			} else {

				out.println("in else");

				if (contentNode.hasNode("user") && contentNode.getNode("user").hasNode(email.replace("@", "_"))) {
					emailNode = contentNode.getNode("user").getNode(email.replace("@", "_"));
					if (emailNode.hasNode("services") && emailNode.getNode("services").hasNode("carrotrule")
							&& emailNode.getNode("services").getNode("carrotrule").hasNodes()) {
						NodeIterator itr = emailNode.getNode("services").getNode("carrotrule").getNodes();
						while (itr.hasNext()) {
							adminserviceid = itr.nextNode().getName();
						}
					}
				}
				if ((adminserviceid != "") && (!adminserviceid.equals("CarrotRuleFreeTrial"))) {

					if (contentNode.hasNode("services")) {
						appserviceNode = contentNode.getNode("services");
					} else {
						appserviceNode = contentNode.addNode("services");
					}

					if (appserviceNode.hasNode(adminserviceid)) {
						appfreetrialNode = appserviceNode.getNode(adminserviceid);
					} else {
						appfreetrialNode = appserviceNode.addNode(adminserviceid);
					}
					if (appfreetrialNode.hasNode("users")) {
						serviceid_userNode = appfreetrialNode.getNode("users");
					} else {
						serviceid_userNode = appfreetrialNode.addNode("users");
					}
					if (serviceid_userNode.hasNode(email.replace("@", "_"))) {
						emailNode = serviceid_userNode.getNode(email.replace("@", "_"));
					} else {
						emailNode = serviceid_userNode.addNode(email.replace("@", "_"));
					}

					if (emailNode.hasNode("CarrotruleMainNode")) {
						CarrotruleMainNode = emailNode.getNode("CarrotruleMainNode");
					} else {
						CarrotruleMainNode = emailNode.addNode("CarrotruleMainNode");
					}
				}
			}

			// session.save();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			out.println(e.getMessage());
		}

		return CarrotruleMainNode;
	}

	public Node validationmethod_old(String freetrialstatus, String email, String group, Session session1,
			SlingHttpServletRequest request, SlingHttpServletResponse response) {

		// PrintWriter out=null;

		Node contentNode = null;

		Node service_doctigrNode = null;
		Node serviceidOrFreetrialNode = null;
		try {
			// out=response.getWriter();
			// out.println("in method");
			if (session1.getRootNode().hasNode("content") && session1.getRootNode().getNode("content").hasNode("user")
					&& session1.getRootNode().getNode("content").getNode("user").hasNode(email.replace("@", "_"))) {
				contentNode = session1.getRootNode().getNode("content");
				// out.println("contentNode: "+contentNode);
				Node usernode = contentNode.getNode("user");
				// out.println("userNode: "+usernode);
				Node emailNode = usernode.getNode(email.replace("@", "_"));
				// out.println("emailNode: "+emailNode);
				if (emailNode.hasNode("services") && emailNode.getNode("services").hasNode("doctiger")) {
					service_doctigrNode = emailNode.getNode("services").getNode("doctiger");
					// out.println("service_doctigrNode: "+service_doctigrNode);

				}
			}
			// out.println("contentNode "+contentNode);

			if (freetrialstatus.equalsIgnoreCase("0") && service_doctigrNode != null
					&& service_doctigrNode.hasNode("DocTigerFreeTrial")) {

				serviceidOrFreetrialNode = service_doctigrNode.getNode("DocTigerFreeTrial");
				if (serviceidOrFreetrialNode.hasProperty("doccount")) {
					// --->>
					serviceidOrFreetrialNode.getProperty("doccount").getString();
				}

			} else if (service_doctigrNode != null && service_doctigrNode.hasNodes()) {
				NodeIterator itr = service_doctigrNode.getNodes();
				int doccount = 0;
				String nodeid = null;
				while (itr.hasNext()) {
					Node node = itr.nextNode();
					nodeid = node.getName();
					if (nodeid.equals("DocTigerFreeTrial")) {
						// --->>
						serviceidOrFreetrialNode = node;
					}
				}

				HashMap<String, String> map = getCustomerDetails(nodeid, email, request, response);
				String productcode = map.get("productCode");
				String serviceEndDate = map.get("serviceEndDate");
				String quantity = map.get("quantity");
				if (productcode.equalsIgnoreCase("Doctigerperuserpermonth")) {
					String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
					Date currentdateobj = new SimpleDateFormat("yyyy-MM-dd").parse(currentDate);
					Date enddateobj = new SimpleDateFormat("yyyy-MM-dd").parse(serviceEndDate);
					if (enddateobj.before(currentdateobj)) {
						serviceidOrFreetrialNode = null;
					}
				} else if (productcode.equalsIgnoreCase("Doctigerperdocument")
						&& serviceidOrFreetrialNode.hasProperty("doccount")) {
					doccount = Integer.parseInt(serviceidOrFreetrialNode.getProperty("doccount").getString());
					if (Integer.parseInt(quantity) < doccount) {
						serviceidOrFreetrialNode = null;
					}
				}

			}
		} catch (Exception e) {
			e.getMessage();
			return null;
		}
		// out.println(serviceidOrFreetrialNode);
		return serviceidOrFreetrialNode;

	}
	/////////////////////////////////////////////////////////////////////////////////////////////////
	//validationmethod change according to group start 03-06-19===========================
	public Node validationmethod(String freetrialstatus, String email, String group, Session session1,
			SlingHttpServletResponse response) {

		// freetrialstatus="0";
		PrintWriter out = null;
		// out.println("in getDocTigerAdvNode");

		Node contentNode = null;
		Node appserviceNode = null;
		Node appfreetrialNode = null;
		Node emailNode = null;
		Node DoctigerAdvNode = null;

		Node adminserviceidNode = null;
		String adminserviceid = "";
		try {
			out = response.getWriter();

			// out.println("freetrialstatus "+freetrialstatus);
			// out.println("email "+email);

			// session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			if (session1.getRootNode().hasNode("content")) {
				contentNode = session1.getRootNode().getNode("content");
			} else {
				contentNode = session1.getRootNode().addNode("content");
			}
		// out.println("contentNode "+contentNode);

			if (freetrialstatus.equalsIgnoreCase("0")) {

				if (contentNode.hasNode("services")) {
					appserviceNode = contentNode.getNode("services");

					// out.println("appserviceNode "+appserviceNode);

					if (appserviceNode.hasNode("freetrial")) {
						appfreetrialNode = appserviceNode.getNode("freetrial");

						//out.println("appfreetrialNode "+appfreetrialNode);

						if (appfreetrialNode.hasNode("users")
								&& appfreetrialNode.getNode("users").hasNode(email.replace("@", "_"))) {
							emailNode = appfreetrialNode.getNode("users").getNode(email.replace("@", "_"));
							// out.println("emailNode "+emailNode);
							if (emailNode.hasNode("DocTigerAdvanced")) {
								DoctigerAdvNode = emailNode.getNode("DocTigerAdvanced");
							} else {
								DoctigerAdvNode = emailNode.addNode("DocTigerAdvanced");
							}
							 //out.println("DoctigerAdvNode "+DoctigerAdvNode);

						} else {
							// emailNode=appfreetrialNode.getNode("users").addNode(email.replace("@", "_"));
						}
					} else {
						// appfreetrialNode=appserviceNode.addNode("freetrial");
					}
				} else {
					// appserviceNode=contentNode.addNode("services");
				}

			} else {

			// out.println("in else");

				if (contentNode.hasNode("user") && contentNode.getNode("user").hasNode(email.replace("@", "_"))) {
					emailNode = contentNode.getNode("user").getNode(email.replace("@", "_"));
					if (emailNode.hasNode("services") && emailNode.getNode("services").hasNode("doctiger")
							&& emailNode.getNode("services").getNode("doctiger").hasNodes()) {
						NodeIterator itr = emailNode.getNode("services").getNode("doctiger").getNodes();
						while (itr.hasNext()) {
							adminserviceid = itr.nextNode().getName();
							if(!adminserviceid.equalsIgnoreCase("DocTigerFreeTrial")) {
								break;
							}
						}
					}
				}
				out.println("adminserviceid "+adminserviceid);
				if ((adminserviceid != "") && (!adminserviceid.equals("DocTigerFreeTrial"))) {

					if (contentNode.hasNode("services")) {
						appserviceNode = contentNode.getNode("services");
					} else {
						appserviceNode = contentNode.addNode("services");
					}
					out.println("appserviceNode "+appserviceNode);

					if (appserviceNode.hasNode(adminserviceid)) {
						appfreetrialNode = appserviceNode.getNode(adminserviceid);
					} else {
						appfreetrialNode = appserviceNode.addNode(adminserviceid);
					}
					out.println("appfreetrialNode "+appfreetrialNode);
					String quantity="";
					String Document_count_Id="0";
					String end_date="";
					boolean validity=false;
					
					if(appfreetrialNode.hasProperty("quantity")) {
						quantity=appfreetrialNode.getProperty("quantity").getString();
						out.println("quantity "+quantity);

						if(appfreetrialNode.hasProperty("Document_count_Id")) {
						Document_count_Id=appfreetrialNode.getProperty("Document_count_Id").getString();
						
						int qty=Integer.parseInt(quantity);
						int dcount=Integer.parseInt(Document_count_Id);
						out.println("qty "+ qty +"dcount "+dcount);
						if(qty>dcount) {
							validity=true;
							out.println(" validity "+validity);
						if(appfreetrialNode.hasProperty("end_date")) {
						 end_date=appfreetrialNode.getProperty("end_date").getString();
						String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
							Date currentdateobj = new SimpleDateFormat("yyyy-MM-dd").parse(currentDate);
							Date enddateobj = new SimpleDateFormat("yyyy-MM-dd").parse(end_date);
							out.println(" enddateobj  "+enddateobj +"currentdateobj  "+currentdateobj);

							if (enddateobj.before(currentdateobj)) {
								validity = false;
								out.println(" validity date "+validity);
							}
						}	else {
							validity=true;
						}
						}else {
							validity = false;
						}
						}
					}else if(appfreetrialNode.hasProperty("end_date")) {
						 end_date=appfreetrialNode.getProperty("end_date").getString();
						String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
							Date currentdateobj = new SimpleDateFormat("yyyy-MM-dd").parse(currentDate);
							Date enddateobj = new SimpleDateFormat("yyyy-MM-dd").parse(end_date);
							out.println(" enddateobj  "+enddateobj +"currentdateobj  "+currentdateobj);

							if (enddateobj.before(currentdateobj)) {
								validity = false;
								out.println(" validity date "+validity);

							}
						}	else {
							out.print("else vallidity is false");
							validity=false;
						}
					
					if(validity) {
					if (appfreetrialNode.hasNode(group)) {
						emailNode = appfreetrialNode.getNode(group);
					} else {
						emailNode = appfreetrialNode.addNode(group);
					}
	              //out.println("emailNode "+emailNode);
					if (emailNode.hasNode("DocTigerAdvanced")) {
						DoctigerAdvNode = emailNode.getNode("DocTigerAdvanced");
					} else {
						DoctigerAdvNode = emailNode.addNode("DocTigerAdvanced");
					}
					
					
				}else {DoctigerAdvNode=null;}
				}
			}

			// session.save();
		} catch (Exception e) {
			// TODO Auto-generated catch block
		//out.println(e.getMessage());
			DoctigerAdvNode=null;
		}

		return DoctigerAdvNode;
	}
	//validationmethod change according to group end===========================
		

	///////////////////////////////////////////////////////////////////////////////////////////////////
	//getDoctigerAdvancedNode change according to group start 28-05-19===========================

public Node getDocTigerAdvNode(String freetrialstatus, String email, String group, Session session1,
		SlingHttpServletResponse response) {

	// freetrialstatus="0";
	PrintWriter out = null;
	// out.println("in getDocTigerAdvNode");

	Node contentNode = null;
	Node appserviceNode = null;
	Node appfreetrialNode = null;
	Node emailNode = null;
	Node DoctigerAdvNode = null;

	Node adminserviceidNode = null;
	String adminserviceid = "";
	try {
		out = response.getWriter();

		// out.println("freetrialstatus "+freetrialstatus);
		// out.println("email "+email);

		// session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
		if (session1.getRootNode().hasNode("content")) {
			contentNode = session1.getRootNode().getNode("content");
		} else {
			contentNode = session1.getRootNode().addNode("content");
		}
	// out.println("contentNode "+contentNode);

		if (freetrialstatus.equalsIgnoreCase("0")) {

			if (contentNode.hasNode("services")) {
				appserviceNode = contentNode.getNode("services");

				// out.println("appserviceNode "+appserviceNode);

				if (appserviceNode.hasNode("freetrial")) {
					appfreetrialNode = appserviceNode.getNode("freetrial");

					//out.println("appfreetrialNode "+appfreetrialNode);

					if (appfreetrialNode.hasNode("users")
							&& appfreetrialNode.getNode("users").hasNode(email.replace("@", "_"))) {
						emailNode = appfreetrialNode.getNode("users").getNode(email.replace("@", "_"));
						// out.println("emailNode "+emailNode);
						if (emailNode.hasNode("DocTigerAdvanced")) {
							DoctigerAdvNode = emailNode.getNode("DocTigerAdvanced");
						} else {
							DoctigerAdvNode = emailNode.addNode("DocTigerAdvanced");
						}
						 //out.println("DoctigerAdvNode "+DoctigerAdvNode);

					} else {
						// emailNode=appfreetrialNode.getNode("users").addNode(email.replace("@", "_"));
					}
				} else {
					// appfreetrialNode=appserviceNode.addNode("freetrial");
				}
			} else {
				// appserviceNode=contentNode.addNode("services");
			}

		} else {

		// out.println("in else");

			if (contentNode.hasNode("user") && contentNode.getNode("user").hasNode(email.replace("@", "_"))) {
				emailNode = contentNode.getNode("user").getNode(email.replace("@", "_"));
				if (emailNode.hasNode("services") && emailNode.getNode("services").hasNode("doctiger")
						&& emailNode.getNode("services").getNode("doctiger").hasNodes()) {
					NodeIterator itr = emailNode.getNode("services").getNode("doctiger").getNodes();
					while (itr.hasNext()) {
						adminserviceid = itr.nextNode().getName();
						if(!adminserviceid.equalsIgnoreCase("DocTigerFreeTrial")) {
							break;
						}
					}
				}
			}
			//out.println("adminserviceid "+adminserviceid);
			if ((adminserviceid != "") && (!adminserviceid.equals("DocTigerFreeTrial"))) {

				if (contentNode.hasNode("services")) {
					appserviceNode = contentNode.getNode("services");
				} else {
					appserviceNode = contentNode.addNode("services");
				}
				//out.println("appserviceNode "+appserviceNode);

				if (appserviceNode.hasNode(adminserviceid)) {
					appfreetrialNode = appserviceNode.getNode(adminserviceid);
				} else {
					appfreetrialNode = appserviceNode.addNode(adminserviceid);
				}
				if (appfreetrialNode.hasNode(group)) {
					emailNode = appfreetrialNode.getNode(group);
				} else {
					emailNode = appfreetrialNode.addNode(group);
				}
              //out.println("emailNode "+emailNode);
				if (emailNode.hasNode("DocTigerAdvanced")) {
					DoctigerAdvNode = emailNode.getNode("DocTigerAdvanced");
				} else {
					DoctigerAdvNode = emailNode.addNode("DocTigerAdvanced");
				}
			}
		}

		// session.save();
	} catch (Exception e) {
		// TODO Auto-generated catch block
	//out.println(e.getMessage());
		DoctigerAdvNode=null;
	}

	return DoctigerAdvNode;
}
//getDoctigerAdvancedNode change according to group end===========================
	
	
	

	public Node getDocTigerAdvNode(String freetrialstatus, String email, Session session1,
			SlingHttpServletResponse response) {

		// freetrialstatus="0";
		PrintWriter out = null;
		// out.println("in getDocTigerAdvNode");

		Node contentNode = null;
		Node appserviceNode = null;
		Node appfreetrialNode = null;
		Node emailNode = null;
		Node DoctigerAdvNode = null;

		Node adminserviceidNode = null;
		String adminserviceid = "";
		try {
			out = response.getWriter();

			// out.println("freetrialstatus "+freetrialstatus);
			// out.println("email "+email);

			// session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			if (session1.getRootNode().hasNode("content")) {
				contentNode = session1.getRootNode().getNode("content");
			} else {
				contentNode = session1.getRootNode().addNode("content");
			}
			// out.println("contentNode "+contentNode);

			if (freetrialstatus.equalsIgnoreCase("0")) {

				if (contentNode.hasNode("services")) {
					appserviceNode = contentNode.getNode("services");

					// out.println("appserviceNode "+appserviceNode);

					if (appserviceNode.hasNode("freetrial")) {
						appfreetrialNode = appserviceNode.getNode("freetrial");

						// out.println("appfreetrialNode "+appfreetrialNode);

						if (appfreetrialNode.hasNode("users")
								&& appfreetrialNode.getNode("users").hasNode(email.replace("@", "_"))) {
							emailNode = appfreetrialNode.getNode("users").getNode(email.replace("@", "_"));
							// out.println("emailNode "+emailNode);
							if (emailNode.hasNode("DocTigerAdvanced")) {
								DoctigerAdvNode = emailNode.getNode("DocTigerAdvanced");
							} else {
								DoctigerAdvNode = emailNode.addNode("DocTigerAdvanced");
							}
							// out.println("DoctigerAdvNode "+DoctigerAdvNode);

						} else {
							// emailNode=appfreetrialNode.getNode("users").addNode(email.replace("@", "_"));
						}
					} else {
						// appfreetrialNode=appserviceNode.addNode("freetrial");
					}
				} else {
					// appserviceNode=contentNode.addNode("services");
				}

			} else {

				// out.println("in else");

				if (contentNode.hasNode("user") && contentNode.getNode("user").hasNode(email.replace("@", "_"))) {
					emailNode = contentNode.getNode("user").getNode(email.replace("@", "_"));
					if (emailNode.hasNode("services") && emailNode.getNode("services").hasNode("doctiger")
							&& emailNode.getNode("services").getNode("doctiger").hasNodes()) {
						NodeIterator itr = emailNode.getNode("services").getNode("doctiger").getNodes();
						while (itr.hasNext()) {
							adminserviceid = itr.nextNode().getName();
						}
					}
				}
				if ((adminserviceid != "") && (!adminserviceid.equals("DocTigerFreeTrial"))) {

					if (contentNode.hasNode("services")) {
						appserviceNode = contentNode.getNode("services");
					} else {
						appserviceNode = contentNode.addNode("services");
					}

					if (appserviceNode.hasNode(adminserviceid)) {
						appfreetrialNode = appserviceNode.getNode(adminserviceid);
					} else {
						appfreetrialNode = appserviceNode.addNode(adminserviceid);
					}
					if (appfreetrialNode.hasNode(email.replace("@", "_"))) {
						emailNode = appfreetrialNode.getNode(email.replace("@", "_"));
					} else {
						emailNode = appfreetrialNode.addNode(email.replace("@", "_"));
					}

					if (emailNode.hasNode("DocTigerAdvanced")) {
						DoctigerAdvNode = emailNode.getNode("DocTigerAdvanced");
					} else {
						DoctigerAdvNode = emailNode.addNode("DocTigerAdvanced");
					}
				}
			}

			// session.save();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			DoctigerAdvNode = null;

		}

		return DoctigerAdvNode;
	}
	/**
	 * THIS METHOD check the freetrial flag is active or not, logintype(lgtype) id salesforce or not 
	 * check the doctiger services purchased by user and returns the doctigerAdvancedNode
	 * @param freetrialstatus
	 * @param email
	 * @param group
	 * @param session1
	 * @param response
	 * @param lgtype
	 * @return
	 */
	public Node getDocTigerAdvNode(String freetrialstatus, String email, String group, Session session1,
			SlingHttpServletResponse response, String lgtype) {

		// freetrialstatus="0";
		PrintWriter out = null;
		// out.println("in getDocTigerAdvNode");
		if(lgtype==null){
			lgtype="normal";
		}
		Node contentNode = null;
		Node appserviceNode = null;
		Node appfreetrialNode = null;
		Node emailNode = null;
		Node DoctigerAdvNode = null;

		Node adminserviceidNode = null;
		String adminserviceid = "";
		try {
			out = response.getWriter();

			// out.println("in method ");
			// out.println("email "+email);

			// session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			if (session1.getRootNode().hasNode("content")) {
				contentNode = session1.getRootNode().getNode("content");
			} else {
				contentNode = session1.getRootNode().addNode("content");
			}
			// out.println("contentNode "+contentNode);

			if (freetrialstatus.equalsIgnoreCase("0")) {

				if(lgtype.equalsIgnoreCase("salesforce") ) {
				   if (contentNode.hasNode("SFservices")) {
					appserviceNode = contentNode.getNode("SFservices");
				   }
				}else {
					if (contentNode.hasNode("services")) {
						appserviceNode = contentNode.getNode("services");
					}
				}
					// out.println("appserviceNode "+appserviceNode);
                      if(appserviceNode!=null) {
					if (appserviceNode.hasNode("freetrial")) {
						appfreetrialNode = appserviceNode.getNode("freetrial");

						// out.println("appfreetrialNode "+appfreetrialNode);

						if (appfreetrialNode.hasNode("users")
								&& appfreetrialNode.getNode("users").hasNode(email.replace("@", "_"))) {
							emailNode = appfreetrialNode.getNode("users").getNode(email.replace("@", "_"));
							// out.println("emailNode "+emailNode);
							if (emailNode.hasNode("DocTigerAdvanced")) {
								DoctigerAdvNode = emailNode.getNode("DocTigerAdvanced");
							} else {
								DoctigerAdvNode = emailNode.addNode("DocTigerAdvanced");
							}
							// out.println("DoctigerAdvNode "+DoctigerAdvNode);

						} else {
							// emailNode=appfreetrialNode.getNode("users").addNode(email.replace("@", "_"));
						}
					} else {
						// appfreetrialNode=appserviceNode.addNode("freetrial");
					}
				} else {
					// appserviceNode=contentNode.addNode("services");
				}

			} else {

				// out.println("in else");

				if (contentNode.hasNode("user") && contentNode.getNode("user").hasNode(email.replace("@", "_"))) {
					emailNode = contentNode.getNode("user").getNode(email.replace("@", "_"));
					if(lgtype.equalsIgnoreCase("salesforce")) {
					if (emailNode.hasNode("SFservices") && emailNode.getNode("SFservices").hasNode("salesautoconvert")
							&& emailNode.getNode("SFservices").getNode("salesautoconvert").hasNodes()) {
						NodeIterator itr = emailNode.getNode("SFservices").getNode("salesautoconvert").getNodes();
						while (itr.hasNext()) {
							adminserviceid = itr.nextNode().getName();
							if (!adminserviceid.equalsIgnoreCase("LeadAutoConvFrTrial")) {
								if ((adminserviceid != "") && (!adminserviceid.equals("LeadAutoConvFrTrial"))) {

									if (contentNode.hasNode("SFservices")) {
										appserviceNode = contentNode.getNode("SFservices");
									} else {
										appserviceNode = contentNode.addNode("SFservices");
									}
									// out.println("appserviceNode "+appserviceNode);

									if (appserviceNode.hasNode(adminserviceid)) {
										appfreetrialNode = appserviceNode.getNode(adminserviceid);

										if (appfreetrialNode.hasNode(group)) {
											emailNode = appfreetrialNode.getNode(group);
										} else {
											emailNode = appfreetrialNode.addNode(group);
										}
										// out.println("emailNode "+emailNode);
										if (emailNode.hasNode("DocTigerAdvanced")) {
											DoctigerAdvNode = emailNode.getNode("DocTigerAdvanced");
										} else {
											DoctigerAdvNode = emailNode.addNode("DocTigerAdvanced");
										}
									}
									break;
								}

							}
						}
						
					}else
					
					if (emailNode.hasNode("SFservices") && emailNode.getNode("SFservices").hasNode("doctiger")
							&& emailNode.getNode("SFservices").getNode("doctiger").hasNodes()) {
						NodeIterator itr = emailNode.getNode("SFservices").getNode("doctiger").getNodes();
						while (itr.hasNext()) {
							adminserviceid = itr.nextNode().getName();
							if (!adminserviceid.equalsIgnoreCase("DocTigerFreeTrial")) {
								if ((adminserviceid != "") && (!adminserviceid.equals("DocTigerFreeTrial"))) {

									if (contentNode.hasNode("SFservices")) {
										appserviceNode = contentNode.getNode("SFservices");
									} else {
										appserviceNode = contentNode.addNode("SFservices");
									}
									// out.println("appserviceNode "+appserviceNode);

									if (appserviceNode.hasNode(adminserviceid)) {
										appfreetrialNode = appserviceNode.getNode(adminserviceid);

										if (appfreetrialNode.hasNode(group)) {
											emailNode = appfreetrialNode.getNode(group);
										} else {
											emailNode = appfreetrialNode.addNode(group);
										}
										// out.println("emailNode "+emailNode);
										if (emailNode.hasNode("DocTigerAdvanced")) {
											DoctigerAdvNode = emailNode.getNode("DocTigerAdvanced");
										} else {
											DoctigerAdvNode = emailNode.addNode("DocTigerAdvanced");
										}
									}
									break;
								}

							}
						}
					}
					
					
					
					
				}else {
					if (emailNode.hasNode("services") && emailNode.getNode("services").hasNode("salesautoconvert")
							&& emailNode.getNode("services").getNode("salesautoconvert").hasNodes()) {
						NodeIterator itr = emailNode.getNode("services").getNode("salesautoconvert").getNodes();
						while (itr.hasNext()) {
							adminserviceid = itr.nextNode().getName();
							if (!adminserviceid.equalsIgnoreCase("LeadAutoConvFrTrial")) {
								if ((adminserviceid != "") && (!adminserviceid.equals("LeadAutoConvFrTrial"))) {

									if (contentNode.hasNode("services")) {
										appserviceNode = contentNode.getNode("services");
									} else {
										appserviceNode = contentNode.addNode("services");
									}
									// out.println("appserviceNode "+appserviceNode);

									if (appserviceNode.hasNode(adminserviceid)) {
										appfreetrialNode = appserviceNode.getNode(adminserviceid);

										if (appfreetrialNode.hasNode(group)) {
											emailNode = appfreetrialNode.getNode(group);
										} else {
											emailNode = appfreetrialNode.addNode(group);
										}
										// out.println("emailNode "+emailNode);
										if (emailNode.hasNode("DocTigerAdvanced")) {
											DoctigerAdvNode = emailNode.getNode("DocTigerAdvanced");
										} else {
											DoctigerAdvNode = emailNode.addNode("DocTigerAdvanced");
										}
									}
									break;
								}

							}
						}
						
					}else
					
					if (emailNode.hasNode("services") && emailNode.getNode("services").hasNode("doctiger")
							&& emailNode.getNode("services").getNode("doctiger").hasNodes()) {
						NodeIterator itr = emailNode.getNode("services").getNode("doctiger").getNodes();
						while (itr.hasNext()) {
							adminserviceid = itr.nextNode().getName();
							if (!adminserviceid.equalsIgnoreCase("DocTigerFreeTrial")) {
								if ((adminserviceid != "") && (!adminserviceid.equals("DocTigerFreeTrial"))) {

									if (contentNode.hasNode("services")) {
										appserviceNode = contentNode.getNode("services");
									} else {
										appserviceNode = contentNode.addNode("services");
									}
									// out.println("appserviceNode "+appserviceNode);

									if (appserviceNode.hasNode(adminserviceid)) {
										appfreetrialNode = appserviceNode.getNode(adminserviceid);

										if (appfreetrialNode.hasNode(group)) {
											emailNode = appfreetrialNode.getNode(group);
										} else {
											emailNode = appfreetrialNode.addNode(group);
										}
										// out.println("emailNode "+emailNode);
										if (emailNode.hasNode("DocTigerAdvanced")) {
											DoctigerAdvNode = emailNode.getNode("DocTigerAdvanced");
										} else {
											DoctigerAdvNode = emailNode.addNode("DocTigerAdvanced");
										}
									}
									break;
								}

							}
						}
					}
									}
					
					
				}
				// out.println("adminserviceid "+adminserviceid);

			}

			// session.save();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			out.println(e.getMessage());
			DoctigerAdvNode = null;
		}

		return DoctigerAdvNode;
	}
	public String getEventdata(String email,String group, String eventId, String eventName,SlingHttpServletResponse response, Session session) {
		PrintWriter out = null;
		// Node emailnode=null;
		// Node Eventnode=null;
		// Node Eventidnode= null;
		// Node mailnode = null;
		// Node mailIdnode= null;
		// Node Doctigernode =null;
		// Node EventIdnode=null;
		// JSONArray arr = new JSONArray();

		Node dtaNode = null;
		Node communicationNode = null;
		Node eventNode = null;
		Node eventcntNode = null;
		Node CTNode = null;
		Node CTCntNode = null;
		Node CTType = null;

		JSONArray Data = new JSONArray();

		try {
			String usrid = email;

			email = email.replace("@", "_");
			out = response.getWriter();
			// out.println("in getEventdata ");

			// out.print("session start :: "+session);
			//session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			// out.print("session :: "+session);
			// out.print(session.getRootNode().getNode("content"));
			// out.print(session.getRootNode().getNode("content").hasNode("user"));
			// out.println("email "+email);
			// out.println(session.getRootNode().getNode("content").getNode("user").hasNode(email));

			FreeTrialandCart cart = new FreeTrialandCart();
			String freetrialstatus = cart.checkfreetrial(usrid);
			// out.println("freetrialstatus: "+freetrialstatus);
			dtaNode = getDocTigerAdvNode(freetrialstatus, usrid, group,session, response);
			// out.println("dtaNode: "+dtaNode);
			if (dtaNode != null) {
				if (dtaNode.hasNode("Communication")) {
					communicationNode = dtaNode.getNode("Communication");
					eventNode = communicationNode.getNode("Events");

					if (eventNode.hasNode(eventId)) {
						eventcntNode = eventNode.getNode(eventId);

						if (eventcntNode.hasNode("CT")) {
							CTNode = eventcntNode.getNode("CT");
							String CT = "";
							NodeIterator iterator = CTNode.getNodes();
							while (iterator.hasNext()) {
								JSONObject dataobj = new JSONObject();
								CTCntNode = iterator.nextNode();
								String AttachedTempName = "";
								String AttachedTempType = "";
								// out.print(CTCntNode.hasProperty("attachedTempName"));
								if (CTCntNode.hasProperty("attachedTempName")
										&& CTCntNode.hasProperty("attachedTempType")) {
									AttachedTempName = CTCntNode.getProperty("attachedTempName").getString();
									AttachedTempType = CTCntNode.getProperty("attachedTempType").getString();
								}
								NodeIterator ctIterator = CTCntNode.getNodes();
								String ct1 = "";
								dataobj.put("CTId", CTCntNode.getName().toString());
								while (ctIterator.hasNext()) {
									CTType = ctIterator.nextNode();

									if (ct1 == "") {
										ct1 = CTType.getName().toString();
									} else if (!ct1.contains(CTType.getName().toString())) {
										ct1 = ct1 + "," + CTType.getName().toString();
									}
									CT = CTType.getName().toString();

									if (CT.equalsIgnoreCase("mail")) {
										String MailTempName = CTType.getProperty("mailTempName").getString();
										// dataobj.put("CT", CT);
										dataobj.put("CT", ct1);
										dataobj.put("MailTempName", MailTempName);
									}
									if (CT.equalsIgnoreCase("sms")) {
										String SMSTempName = CTType.getProperty("smsTempName").getString();
										// dataobj.accumulate("CT", CT);
										dataobj.put("CT", ct1);

										dataobj.put("smsTempName", SMSTempName);
									}

								}
								dataobj.put("AttachedTempName", AttachedTempName);
								dataobj.put("AttachedTempType", AttachedTempType);
								Data.put(dataobj);
								// out.println("Data: "+Data);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// out.print("error message getEventdata :: "+e.getMessage());
		}
		// out.println(arr);

		return Data.toString();

	}

	public JSONObject getTempSFObj(String email, String group, String tempname, SlingHttpServletResponse response) {

		PrintWriter out = null;
		Node tempnode = null;
		Node SFNode = null;
		Node dtaNode = null;
		String newemail = email.replace("@", "_");
		JSONObject sfobj = new JSONObject();

		try {
			out = response.getWriter();
			// out.print("session start :: "+session);
			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));

			FreeTrialandCart cart = new FreeTrialandCart();
			String freetrialstatus = cart.checkfreetrial(email);

			dtaNode = getDocTigerAdvNode(freetrialstatus, email, group,session, response);
			if (dtaNode != null) {

				if (dtaNode.hasNode("TemplateLibrary") && dtaNode.getNode("TemplateLibrary").hasNode(tempname)) {
					tempnode = dtaNode.getNode("TemplateLibrary").getNode(tempname);
					// out.print("tempnode "+tempnode);
					if (tempnode.hasNode("SFobject")) {
						SFNode = tempnode.getNode("SFobject");
						sfobj.put("Sfobject", SFNode.getProperty("primeryKeyObject").getString());
						sfobj.put("PK", SFNode.getProperty("primeryKey").getString());
						sfobj.put("tempnode", tempnode);
						// out.println(sfobj);
					} else {
						sfobj.put("Sfobject", "");
						sfobj.put("PK", "");
						sfobj.put("tempnode", tempnode);

					}

				}
			}
		} catch (Exception e) {
			out.print("error in getTempSFObj " + e.getMessage());
		}
		return sfobj;

	}

	public String getADVandTemplatedata(String email, String group,String templatename, String AttachtempalteType, String SFObject,
			String Primery_key, String Primery_key_value, JSONObject SFData, SlingHttpServletResponse response, Session session) {
		PrintWriter out = null;
		String templatename_url = null;
		Node templatenode = null;
		Node selectedclsnode = null;
		Node selectclsid = null;
		Node Doctigernode = null;
		Node Avdancedtempnode = null;
		Node QRcodeNode = null;
		String temptoparse = "";
		
		String tempurl="";
		String newemail = email.replace("@", "_");
		
		try {

			out = response.getWriter();
			// out.print("session start :: "+session);
			//session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			JSONObject sfobj = new JSONObject();

			FreeTrialandCart cart = new FreeTrialandCart();
			String freetrialstatus = cart.checkfreetrial(email);

			Doctigernode = getDocTigerAdvNode(freetrialstatus, email, group,session, response);
			if (Doctigernode != null) {

				if (AttachtempalteType.equals("TemplateLibrary")) {
					temptoparse = templatename;
					//sfobj = getTempSFObj(email, group,templatename, response);
					if (Doctigernode.hasNode("TemplateLibrary") && Doctigernode.getNode("TemplateLibrary").hasNode(templatename)) {
						templatenode = Doctigernode.getNode("TemplateLibrary").getNode(templatename);
					out.println("templatenode  "+templatenode);
					}

					//templatenode = (Node) sfobj.get("tempnode");
//					 out.println("sfobj * "+sfobj);
//					 out.println("templatenode * "+templatenode);

				} else if (AttachtempalteType.equals("AdvancedTemplate")) {
					if (Doctigernode.hasNode("AdvancedTemplate")
							&& Doctigernode.getNode("AdvancedTemplate").hasNode(templatename)) {
						Avdancedtempnode = Doctigernode.getNode("AdvancedTemplate").getNode(templatename);
						if (Avdancedtempnode.getProperty("static").getString().equals("true")) {
							String Tempname = Avdancedtempnode.getProperty("selectedTemplate").getString();
							temptoparse = Tempname;
							if (Doctigernode.hasNode("TemplateLibrary") && Doctigernode.getNode("TemplateLibrary").hasNode(templatename)) {
								templatenode = Doctigernode.getNode("TemplateLibrary").getNode(templatename);
							out.println("templatenode  "+templatenode);
							}
							//sfobj = getTempSFObj(email, group,Tempname, response);
							//templatenode = (Node) sfobj.get("tempnode");
							// out.println("sfobj ** "+sfobj);

							// out.println("templatenode ** "+templatenode);

						} else if (Avdancedtempnode.getProperty("dynamic").getString().equals("true")) {

							if (Avdancedtempnode.hasProperty("selectedRule")
									&& Avdancedtempnode.getProperty("selectedRule").getString() != "") {

								JSONObject modifiedSFrespobj = new JSONObject();
								JSONArray responsejsonarray = SFData.getJSONArray("response");
								JSONObject responsejson = responsejsonarray.getJSONObject(0);
								Iterator<String> ke = responsejson.keys();
								while (ke.hasNext()) {
									String jsonkeyname = (String) ke.next();
									String jsonvalues = responsejson.getString(jsonkeyname);
									modifiedSFrespobj.put(jsonkeyname.toLowerCase(), jsonvalues);
									// out.println(jsonkeyname+" "+ jsonvalues);
								}
								// out.println("modifiedSFrespobj "+modifiedSFrespobj);

								String ruledata = Avdancedtempnode.getProperty("Ruledata").getString();
								// out.println("ruledata "+ruledata);
								JSONObject ruledataobj = new JSONObject(ruledata.replace("\\", ""));
								// out.println("ruledataobj "+ruledataobj);
								String ruleurl = ruledataobj.getString("URL");
								JSONArray InputArr = ruledataobj.getJSONArray("Input");
								// out.println("ruleurl "+ruleurl);
								// out.println("InputArr "+InputArr);
								JSONObject inobj = null;
								JSONObject ruleinput = new JSONObject();
								for (int i = 0; i < InputArr.length(); i++) {
									inobj = InputArr.getJSONObject(i);
									String inputfield = inobj.getString("field");
									String inputtype = inobj.getString("type");
									if (modifiedSFrespobj.has(inputfield) && inputtype.equalsIgnoreCase("Integer")) {

										if (modifiedSFrespobj.has(inputfield)) {

											ruleinput.put(inputfield,
													Integer.parseInt(modifiedSFrespobj.getString(inputfield)));
										} else {
											ruleinput.put(inputfield, "");
										}
									} else if (modifiedSFrespobj.has(inputfield)
											&& inputtype.equalsIgnoreCase("String")) {
										if (modifiedSFrespobj.has(inputfield)) {

											ruleinput.put(inputfield, modifiedSFrespobj.getString(inputfield));
										} else {
											ruleinput.put(inputfield, "");
										}
									} else if (modifiedSFrespobj.has(inputfield)
											&& inputtype.equalsIgnoreCase("TEXTAREA")) {

										if (modifiedSFrespobj.has(inputfield)) {

											ruleinput.put(inputfield, modifiedSFrespobj.getString(inputfield));
										} else {
											ruleinput.put(inputfield, "");
										}
									} else {
										if (modifiedSFrespobj.has(inputfield)) {

											ruleinput.put(inputfield, modifiedSFrespobj.getString(inputfield));
										} else {
											ruleinput.put(inputfield, "");
										}
									}
								}

								// out.println("ruleinput "+ruleinput);
								// ruleinput.put("Property Type", "Type-1 V2");
								// String ruleres = new
								// SOAPCall().callPostJSon("http://35.236.213.87:8080/drools/callrules/doctiger8@gmail.com_DoctigerClauseNew_DoctigerClauseRuleEngineNew/fire",
								// ruleinput);
								String ruleres = new SOAPCall().callPostJSon(ruleurl, ruleinput);
								// out.println("ruleres "+ruleres);

								JSONObject ruleoutput = new JSONObject(ruleres);

								// -------------------------------------------------
								// JSONObject obj = new JSONObject();
								// obj.put("noOfProperties", "2");
								// obj.put("project_name", "Doctiger");
								// String result= new
								// SOAPCall().callPostJSon("http://35.236.213.87:8080/drools/callrules/doctiger8@gmail.com_Doctiger_RuleEngine_Doctiger/fire",
								// obj);
								// // out.println("result "+result);
								// ruleoutput = new JSONObject(result);
								// ---------------------------------------------------

								temptoparse = ruleoutput.getString("template_name");
								// out.println("temptoparse *** @@ "+temptoparse);
								if (Doctigernode.hasNode("TemplateLibrary") && Doctigernode.getNode("TemplateLibrary").hasNode(templatename)) {
									templatenode = Doctigernode.getNode("TemplateLibrary").getNode(templatename);
								out.println("templatenode  "+templatenode);
								}
								//sfobj = getTempSFObj(email, group,temptoparse, response);
								//templatenode = (Node) sfobj.get("tempnode");
								// out.println("sfobj **@@ "+sfobj);

//								 out.println("templatenode **@@ "+templatenode);
							}
						}
					}
				}
					if(templatenode.hasProperty("TemplateUrl")) {
					tempurl=templatenode.getProperty("TemplateUrl").getString();
					out.println("tempurl  "+tempurl);
					}
					
					
				// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
				// String
				// sfurl="http://35.236.220.85:8080/DocTigerSFDCAcessToken/CallSFDCAPI?sfobject="+SFObject+"&primerykey="+Primery_key+"&primerykeyvalue="+Primery_key_value;
				// Replacedata a = new Replacedata();
				// //String
				// sfurl="http://35.236.220.85:8080/DocTigerSFDCAcessToken/CallSFDCAPI?sfobject=Account&primerykey=Id&primerykeyvalue=0016F00002Pn55b";
				// String ressf = a.callSFAPI(sfurl);
				//
				//
				// //out.println("ressf "+ressf);
				// //out.println("");
				// JSONObject ressfobj = new JSONObject(ressf);
				// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

				JSONArray arr = SFData.getJSONArray("response");
				JSONObject reqrecord = arr.getJSONObject(0);
				// out.println(reqrecord);
				JSONObject newjson = new JSONObject();
				// out.println("ressfobj :: "+ressfobj);
				newjson.put("TemplateUrl", tempurl);
				Iterator<?> keys = reqrecord.keys();

				while (keys.hasNext()) {
					String key = (String) keys.next();
					String value = reqrecord.getString(key);
					newjson.put("<<" + key.trim() + ">>", value);
				}

				// note Addeed as we are adding clause while generating document
				if (templatenode.hasNode("Selected Clauses")) {
					String[] numberingstyle = null;
					int level = 0;
					if (templatenode.hasProperty("Numbring Style")) {
						String nostyle = templatenode.getProperty("Numbring Style").getString();
						numberingstyle = nostyle.split(".");
					}
					selectedclsnode = templatenode.getNode("Selected Clauses");
					NodeIterator clsnode = selectedclsnode.getNodes();
					int count = 1;
					while (clsnode.hasNext()) {
						selectclsid = clsnode.nextNode();
						String clauseid = selectclsid.getName();
						String clausename = selectclsid.getProperty("ClauseName").getString();
						// out.println("clauseid "+clauseid +"clausename "+clausename);
						// out.print("count "+count);
						// String rsl= getFileReplaceData(email, clauseid, clausename, response,
						// SFData.toString());
						String rsl = getFileReplaceData_new(email,group, clauseid, clausename, response, SFData.toString(),
								count);

						JSONObject langobj = new JSONObject(rsl);
						// out.println("langobj "+langobj);
						Iterator<?> langclskeys = langobj.keys();
						// out.println(1);
						while (langclskeys.hasNext()) {
							// out.println(2);
							String lkey = (String) langclskeys.next();
							// .println("lkey "+lkey);

							newjson.put(lkey, langobj.get(lkey).toString());

							// JSONObject lvalue = langobj.getJSONObject(lkey);
							// newjson.put("[["+clausename+"_"+lkey.trim()+"]]", lvalue.toString());

							// if(Doctigernode.hasNode("Clauses") &&
							// Doctigernode.getNode("Clauses").hasNode(clauseid)) {
							// Node clsid=Doctigernode.getNode("Clauses").getNode(clauseid);
							//
							// newjson.put(clausename+"_clausename_English", clausename);
							// newjson.put(clausename+"_clausename_Arabic",
							// clsid.getProperty("Metadata").getString());
							// }
						}
						count++;
					}
				}

				JSONObject QRobj = new JSONObject();
				if (templatenode.hasNode("QR Code")) {
					// out.println("templatenode "+templatenode);
					QRcodeNode = templatenode.getNode("QR Code");
					QRobj.put("Size", QRcodeNode.getProperty("Size").getString());
					QRobj.put("Position", QRcodeNode.getProperty("Position").getString());
					QRobj.put("TableNo", QRcodeNode.getProperty("Table_No").getString());

//					 out.println("QRobj "+QRobj);

					Node param = QRcodeNode.getNode("Param");
					NodeIterator QRitr = param.getNodes();
					JSONArray allQRparam = new JSONArray();
					while (QRitr.hasNext()) {
						Node oneQRparamNode = QRitr.nextNode();
						// out.println("allparam "+oneQRparamNode);

						JSONObject oneparam = new JSONObject();
						String field = oneQRparamNode.getProperty("Field").getString();
						String lowercasefield = field.toLowerCase();
						oneparam.put("Field", lowercasefield);
						oneparam.put("SFObject", oneQRparamNode.getProperty("SFObject").getString());
						if (reqrecord.has(lowercasefield) && reqrecord.getString(lowercasefield) != ""
								&& reqrecord.getString(lowercasefield) != null) {
							oneparam.put("Field_value", reqrecord.getString(lowercasefield));
						} else if (reqrecord.has(field) && reqrecord.getString(field) != ""
								&& reqrecord.getString(field) != null) {
							oneparam.put("Field_value", reqrecord.getString(field));

						} else {
							oneparam.put("Field_value", "NA");
						}
						allQRparam.put(oneparam);
						// out.println("oneparam "+oneparam);

					}
					// out.println("allQRparam "+allQRparam);
					QRobj.put("Param", allQRparam);
					newjson.put("QRobj", QRobj);

				}

				// out.println("newjson2 " +newjson);
				// String
				// wsinput="TemplateName="+temptoparse+"&filename="+temptoparse+".docx&jsonstring="+newjson.toString();
				// // templatename_url=new
				// SOAPCall().excutePost("http://35.188.233.86:8080/DocTigerSF/services/SFDCDocumentGeneration/DocGeneration",
				// wsinput);
				// out.println("templatename_urlc "+templatename_url);

				// JsonGetterSetter objJsonGetterSetter = new JsonGetterSetter();
				// objJsonGetterSetter.setFileName("temp112.docx");
				// objJsonGetterSetter.setTemplateName("temp112");
				// objJsonGetterSetter.setJsonString(newjson.toString());
				newjson.put("TemplateName", temptoparse);
				newjson.put("filename", temptoparse + ".docx");

				JSONObject sendsfobj = new JSONObject();
				sendsfobj.put("SFObject", SFObject);
				sendsfobj.put("Primery_key", Primery_key);
				sendsfobj.put("Primery_key_value", Primery_key_value);

				newjson.put("sfobj", sendsfobj);
				// newjson.put("logo", "http://35.221.183.246:8082/logo.png");
				newjson.put("Coverimageurl", "http://"+bundleststic.getString("Doctigerslingdvlp")+":8082/centerimage.png");
				newjson.put("Coverimage_tableNo", "1");
				// newjson.put("logo2", "");
				JSONArray floorplanarr = new JSONArray();
				if (reqrecord.has("u_plan__c")) {
					floorplanarr.put(reqrecord.get("u_plan__c"));
				} else if (reqrecord.has("f_plan__c")) {
					floorplanarr.put(reqrecord.get("f_plan__c"));
				} else if (reqrecord.has("p_plan__c")) {
					floorplanarr.put(reqrecord.get("p_plan__c"));
				}
				newjson.put("floorplanarr", floorplanarr.toString());

				out.println("newjson2 " +newjson);

				templatename_url = new SOAPCall().callPostJSonModified(
						bundleststic.getString("DocGenServletUrl"), newjson);
//				 out.println("templatename_urlc "+templatename_url);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			out.print("error message getADVandTemplatedata :: "+e.getMessage());
		}
		return templatename_url;

	}

	public JSONObject getMailTemplatedata(String email, String group, String mailtemplatename, String SFObject, String Primery_key,
			String Primery_key_value, JSONObject SFData, SlingHttpServletResponse response,Session session) {
		PrintWriter out = null;
		JSONObject mailbody = null;
		Node dtaNode = null;
		Node mailtemplatenode = null;
		Node SFNode = null;
		Node mailnode = null;
		Node mailIdnode = null;
		Node Doctigernode = null;
		Node EventIdnode = null;
		String newemail = email.replace("@", "_");
		
		try {

			out = response.getWriter();
			// out.print("session start :: "+session);
			//session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			JSONObject sfobj = new JSONObject();

			FreeTrialandCart cart = new FreeTrialandCart();
			String freetrialstatus = cart.checkfreetrial(email);

			dtaNode = getDocTigerAdvNode(freetrialstatus, email, group,session, response);
			if (dtaNode != null) {

				if (dtaNode.hasNode("Communication") && dtaNode.getNode("Communication").hasNode("MailTemplate")
						&& dtaNode.getNode("Communication").getNode("MailTemplate").hasNode(mailtemplatename)) {

					mailtemplatenode = dtaNode.getNode("Communication").getNode("MailTemplate")
							.getNode(mailtemplatename);
					// out.println(mailtemplatenode);
					if (mailtemplatenode.hasNode("SF Object")) {
						SFNode = mailtemplatenode.getNode("SF Object");
						sfobj.put("Sfobject", SFNode.getProperty("Primary key Object").getString());
						sfobj.put("PK", SFNode.getProperty("Primary Key").getString());
					}
					// out.println(sfobj);
					/// httpurl connection method to get SFdata
					JSONObject objmail = new JSONObject();
					String from = mailtemplatenode.getProperty("From").getString();
					objmail.put("from", from);
					// out.println("from "+from);
					String to = mailtemplatenode.getProperty("To").getString();
					objmail.put("to", to);
					// out.println("to* "+to);
					String subject = mailtemplatenode.getProperty("Subject").getString();
					objmail.put("subject", subject);
					// out.println("subject "+subject);
					String body = mailtemplatenode.getProperty("Body").getString();
					objmail.put("body", body);
					// out.println("body "+body);

					// out.println("mailtemplatenode.hasNode(\"Attachment\")
					// "+mailtemplatenode.hasNode("Attachment"));

					if (mailtemplatenode.hasNode("Attachment")
							&& mailtemplatenode.getNode("Attachment").hasProperty("filepath")) {
						String attachurl = mailtemplatenode.getNode("Attachment").getProperty("filepath").getString();
						objmail.put("attachurl", attachurl);
						// out.println("attachurl "+attachurl);

					}

					Replacedata a = new Replacedata();
					// ----------------------------------------------------------------------------------------------------------------------
					// //String
					// sfurl="http://35.236.220.85:8080/DocTigerSFDCAcessToken/CallSFDCAPI?sfobject=Account&primerykey=Id&primerykeyvalue=0016F00002Pn55b";
					// String
					// sfurl="http://35.236.220.85:8080/DocTigerSFDCAcessToken/CallSFDCAPI?sfobject="+SFObject+"&primerykey="+Primery_key+"&primerykeyvalue="+Primery_key_value;
					//
					// // String
					// sfurl="http://35.188.233.86:8080/DocTigerSFDCAcessToken/CallSFDCAPI?sfobject=Account&primerykey=Id&primerykeyvalue=0016F00002Pn55b";
					// String SFres = a.callSFAPI(sfurl);
					// -----------------------------------------------------------------------------------------------------------------------------
					// out.println("SFres "+SFres);

					// out.println("objmail"+objmail);
					// mailbody= new Replacedata().ReplacMailbody(objmail, SFres ,response);
					mailbody = new Replacedata().ReplacMailbody(objmail, SFData.toString(), response);

					// out.println("mailbody @ "+mailbody);
					// call method to send mail
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			out.print("error message getMailTemplatedata :: " + e.getMessage());
		}
		return mailbody;
	}

	public JSONObject getSMSTemplatedata(String email,String group, String SMStemplatename, String SFObject, String Primery_key,
			String Primery_key_value, JSONObject SFData, SlingHttpServletResponse response) {
		PrintWriter out = null;
		JSONObject SMSbody = null;
		Node dtaNode = null;
		Node SMStemplatenode = null;
		Node SFNode = null;
		Node mailnode = null;
		Node mailIdnode = null;
		Node Doctigernode = null;
		Node EventIdnode = null;
		String newemail = email.replace("@", "_");
		;
		try {

			out = response.getWriter();
			// out.print("session start :: "+session);
			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			JSONObject sfobj = new JSONObject();

			FreeTrialandCart cart = new FreeTrialandCart();
			String freetrialstatus = cart.checkfreetrial(email);

			dtaNode = getDocTigerAdvNode(freetrialstatus, email,group, session, response);
			if (dtaNode != null) {

				if (dtaNode.hasNode("SMSTemplate") && dtaNode.getNode("SMSTemplate").hasNode(SMStemplatename)) {

					SMStemplatenode = dtaNode.getNode("SMSTemplate").getNode(SMStemplatename);
					// out.println(mailtemplatenode);
					SFNode = SMStemplatenode.getNode("SFobject");
					sfobj.put("Sfobject", SFNode.getProperty("primeryKeyObject").getString());
					sfobj.put("PK", SFNode.getProperty("primeryKey").getString());
					// out.println(sfobj);
					/// httpurl connection method to get SFdata
					JSONObject objmail = new JSONObject();
					String from = SMStemplatenode.getProperty("From").getString();
					objmail.put("from", from);
					// out.println("from "+from);
					String to = SMStemplatenode.getProperty("To").getString();
					objmail.put("to", to);
					// out.println("to "+to);

					String body = SMStemplatenode.getProperty("Body").getString();
					objmail.put("body", body);
					// out.println("body "+body);

					Replacedata a = new Replacedata();
					// -------------------------------------------------------------------------------------------------------------------------
					// //String
					// sfurl="http://35.236.220.85:8080/DocTigerSFDCAcessToken/CallSFDCAPI?sfobject=Account&primerykey=Id&primerykeyvalue=0016F00002Pn55b";
					// String
					// sfurl="http://35.236.220.85:8080/DocTigerSFDCAcessToken/CallSFDCAPI?sfobject="+SFObject+"&primerykey="+Primery_key+"&primerykeyvalue="+Primery_key_value;
					//
					// // String
					// sfurl="http://35.188.233.86:8080/DocTigerSFDCAcessToken/CallSFDCAPI?sfobject=Account&primerykey=Id&primerykeyvalue=0016F00002Pn55b";
					// String SFres = a.callSFAPI(sfurl);
					// out.println("SFres "+SFres);
					// -----------------------------------------------------------------------------------------------------------------------------

					// out.println("objmail"+objmail);
					SMSbody = new Replacedata().ReplacSMSbody(objmail, SFData.toString(), response);
					// out.println("mailbody @ "+mailbody);
					// call method to send mail
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			out.print("error message getSMSTemplatedata :: " + e.getMessage());
		}
		return SMSbody;

	}

	public String getFileReplaceData(String email,String group, String clauseid, String clausename,
			SlingHttpServletResponse response, String SFresp) {
		String resp = "";
		PrintWriter out = null;
		Node DocTigerAdvance = null;

		JSONArray arr = null;
		JSONObject SFrespobj = null;
		JSONObject modifiedSFrespobj = null;
		JSONObject langobj = null;

		try {

			out = response.getWriter();

			modifiedSFrespobj = new JSONObject();

			SFrespobj = new JSONObject(SFresp);

			JSONArray responsejsonarray = SFrespobj.getJSONArray("response");
			// for (int lres = 0; lres < responsejsonarray.length(); lres++) {
			JSONObject responsejson = responsejsonarray.getJSONObject(0);
			Iterator<String> ke = responsejson.keys();
			while (ke.hasNext()) {

				String jsonkeyname = (String) ke.next();
				String jsonvalues = responsejson.getString(jsonkeyname);
				modifiedSFrespobj.put(jsonkeyname.toLowerCase(), jsonvalues);
				// out.println(jsonkeyname+" "+ jsonvalues);
			}
			// }

			// out.println("modifiedSFrespobj "+modifiedSFrespobj);
			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));

			FreeTrialandCart cart = new FreeTrialandCart();
			String freetrialstatus = cart.checkfreetrial(email);

			DocTigerAdvance = getDocTigerAdvNode(freetrialstatus, email, group,session, response);
			if (DocTigerAdvance != null) {

				// out.println("emailnode "+emailnode);
				if (DocTigerAdvance.hasNode("Clauses")) {
					/// content/user/user1_gmail.com/DocTigerAdvanced/Clauses/10/Clause Description
					Node cls = null;
					cls = DocTigerAdvance.getNode("Clauses");
					Node clid = null;
					Node clsdesc = null;

					if (cls.hasNode(clauseid)) {
						clid = cls.getNode(clauseid);
						JSONObject MailJSON = new JSONObject();

						JSONObject ruleinput = new JSONObject();

						if (clid.hasProperty("RuleBased") && clid.getProperty("RuleBased").getString() != "") {
							String ruleEngineName = clid.getProperty("RuleBased").getString();
							String ruledata = clid.getProperty("Ruledata").getString();
							// out.println("ruledata "+ruledata);
							JSONObject ruledataobj = new JSONObject(ruledata.replace("\\", ""));
							// out.println("ruledataobj "+ruledataobj);

							String ruleurl = ruledataobj.getString("URL");
							JSONArray InputArr = ruledataobj.getJSONArray("Input");
							// out.println("ruleurl "+ruleurl);
							// out.println("InputArr "+InputArr);
							JSONObject inobj = null;
							for (int i = 0; i < InputArr.length(); i++) {
								inobj = InputArr.getJSONObject(i);
								String inputfield = inobj.getString("field");
								String inputtype = inobj.getString("type");
								// out.println("inputfield "+inputfield +"inputtype "+inputtype);
								// out.println(modifiedSFrespobj.has(inputfield));
								// out.println(inputtype.equals("Integer"));
								// out.println(inputtype.equals("String"));

								if (modifiedSFrespobj.has(inputfield) && inputtype.equalsIgnoreCase("Integer")) {
									// out.println("has field integer");
									ruleinput.put(inputfield,
											Integer.parseInt(modifiedSFrespobj.getString(inputfield)));
								} else if (modifiedSFrespobj.has(inputfield) && inputtype.equalsIgnoreCase("String")) {
									// out.println("has field String");

									ruleinput.put(inputfield, modifiedSFrespobj.getString(inputfield));
								} else {
									// out.println(" field else");

									ruleinput.put(inputfield, "");
								}
							}

							// out.println("ruleinput "+ruleinput);
							// ruleinput.put("Property Type", "Type-1 V2");
							// String ruleres = new
							// SOAPCall().callPostJSon("http://35.236.213.87:8080/drools/callrules/doctiger8@gmail.com_DoctigerClauseNew_DoctigerClauseRuleEngineNew/fire",
							// ruleinput);
							String ruleres = new SOAPCall().callPostJSon(ruleurl, ruleinput);

							JSONObject ruleoutput = new JSONObject(ruleres);

							// out.println("ruleoutput "+ruleoutput);

							if (ruleoutput.length() != 0) {
								// out.println("ruleoutput "+ruleoutput);
								// out.println("clausename "+clausename);
								if (ruleoutput.has(clausename)) {
									// out.println("ruleoutput.has(clausename) "+ruleoutput.has(clausename));
									langobj = new JSONObject();
									String[] childclauses = ruleoutput.getString(clausename).split(",");

									for (int i = 0; i < childclauses.length; i++) {
										// out.println("childclauses[i] "+childclauses[i]);
										if (clid.hasNode("ChildClauses") && clid.getNode("ChildClauses").hasNodes()) {
											NodeIterator itr = clid.getNode("ChildClauses").getNodes();
											while (itr.hasNext()) {
												Node Child = itr.nextNode();
												if (Child.getProperty("ChildClauseName").getString()
														.equals(childclauses[i])) {
													// out.println(Child.getProperty("ChildClauseName").getString());

													if (Child.hasNode("Multilingual Data")) {
														Node languageNode = null;

														Node multilingualNode = Child.getNode("Multilingual Data");
														NodeIterator langitr = multilingualNode.getNodes();

														while (langitr.hasNext()) {
															arr = new JSONArray();

															languageNode = langitr.nextNode();
															Node childdesc = languageNode.getNode("Clause Description");
															if (childdesc.hasNode("Composed Online")) {
																Node compol = childdesc.getNode("Composed Online");
																// out.println("compol "+compol);
																resp = addmergefiled(compol, SFresp, response);
															} else if (childdesc.hasNode("File")) {
																// out.println("childdesc "+childdesc);
																resp = addmergefiled(childdesc, SFresp, response);

															}
															// out.println("resp1 "+resp);
															Iterator<?> keys = ruleoutput.keys();

															while (keys.hasNext()) {
																String key = (String) keys.next();
																// out.println("key "+ key);
																if (resp.contains("<<" + key + ">>")) {
																	// out.println( "resp.contains(\"<<\"+key+\">>\") "
																	// + resp.contains("<<"+key+">>"));
																	resp = resp.replace("<<" + key + ">>",
																			ruleoutput.getString(key));
																	// out.println("resp2 "+resp);
																}
															}

															if (langobj
																	.has(clausename + "_" + languageNode.getName())) {
																JSONArray arr1 = langobj.getJSONArray(
																		clausename + "_" + languageNode.getName());
																arr1.put(resp);
																langobj.put(clausename + "_" + languageNode.getName(),
																		arr1);

															} else {
																arr.put(resp);
																langobj.put(clausename + "_" + languageNode.getName(),
																		arr);
															}

														}

													}

													//// multilungalNode end }

												}
											}
										}
									}

								} else {
									langobj = new JSONObject();
									arr = new JSONArray();
									arr.put("");
									langobj.put(clausename, arr);

								}
							} else {

								langobj = new JSONObject();
								arr = new JSONArray();
								arr.put("");
								langobj.put(clausename, arr);

							}
						} else {
							// out.print("else rulebase ");
							// if(cls.getNode(clauseid).hasNode("Clause Description")) {
							// // callmethod to call rule
							// clsdesc=clid.getNode("Clause Description");
							// arr = new JSONArray();
							// resp= addmergefiled( clsdesc, SFresp, response) ;
							// arr.put(resp);
							// }
							langobj = new JSONObject();

							if (cls.getNode(clauseid).hasNode("Multilingual Data")) {
								Node languageNode = null;

								Node multilingualNode = cls.getNode(clauseid).getNode("Multilingual Data");
								// out.println("multilingualNode "+multilingualNode);
								NodeIterator langitr = multilingualNode.getNodes();

								while (langitr.hasNext()) {
									languageNode = langitr.nextNode();
									arr = new JSONArray();
									if (languageNode.hasNode("Clause Description")) {

										clsdesc = languageNode.getNode("Clause Description");
										resp = addmergefiled(clsdesc, SFresp, response);
										// out.println(resp);

										if (langobj.has(clausename + "_" + languageNode.getName())) {
											// out.println("in has");
											JSONArray arr1 = langobj
													.getJSONArray(clausename + "_" + languageNode.getName());
											arr1.put(resp);
											langobj.put(clausename + "_" + languageNode.getName(), arr1);

										} else {
											// out.println("else has");
											arr.put(resp);
											langobj.put(clausename + "_" + languageNode.getName(), arr);
										}

									}
								}

							}
							//// multilungalNode end }

						}

						session.save();
					} else {
						arr = new JSONArray();
						arr.put("");
					}
				}
			}
			// out.println(langobj.toString());
		} catch (Exception e) {
			// TODO: handle exception
			arr = new JSONArray();
			arr.put("");
			// out.print("error mssage getFileReplaceData "+e.getMessage());
		}
		return langobj.toString();

	}

	public String addmergefiled(Node clsdesc, String SFresp, SlingHttpServletResponse response) {
		PrintWriter out = null;
		String resp = "";
		try {
			out = response.getWriter();
			if (clsdesc.hasNode("File")) {
				// out.println("file true");
				Node file = null;
				file = clsdesc.getNode("File");
				// out.println(file);
				String filepath = file.getProperty("filepath").getString();
				// out.println("filepath "+ filepath);
				Replacedata rk = new Replacedata();

				String data = rk.callFile(filepath);
				// out.println("filedata "+data);
				resp = rk.Replacekey(data, SFresp);
				// out.println("resp in file "+resp);
			} else if (clsdesc.hasNode("Paragraph")) {
				// out.println("in paraghaph");
				String allpara = "";
				Node paranode = null;
				paranode = clsdesc.getNode("Paragraph");
				// resp=paranode.toString();
				NodeIterator iterator = paranode.getNodes();
				int count = 1;
				Node prnode = null;
				while (iterator.hasNext()) {
					prnode = iterator.nextNode();

					String paragraph = prnode.getProperty("Description").getString();
					// resp=resp+"** "+paragraph;
					allpara = allpara + "\r\n " + paragraph;
					// resp=resp+"** "+allpara;

				}
				// resp=resp+"** final "+allpara;
				Replacedata rk = new Replacedata();

				resp = rk.Replacekey(allpara, SFresp);

			} else {
				resp = " ";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
		// out.println("resp "+resp);
		return resp;
	}

	public String getClauseByName(String searchText, String email, String group,Node DoctigerAdvnode,
			SlingHttpServletResponse response) {
		PrintWriter out = null;
		String clauseid = "";
		Node clauseRetNode = null;
		Session session = null;
		if (!searchText.trim().equals("")) {
			try {
				// out= response.getWriter();
				// out.print("in getClauseByName");
				// out.println("email "+email);
				session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
				String querryStr = "select [ClauseName] from [nt:base] where (contains('ClauseName','" + searchText
						+ "*'))  and ISDESCENDANTNODE('/" + DoctigerAdvnode + "/Clauses/')";
				Workspace workspace = session.getWorkspace();
				Query query = workspace.getQueryManager().createQuery(querryStr, Query.JCR_SQL2);
				QueryResult result = query.execute();
				NodeIterator iterator = result.getNodes();
				while (iterator.hasNext()) {
					clauseRetNode = iterator.nextNode();
					// out.println("clauseRetNode "+clauseRetNode);
					clauseid = clauseRetNode.getName().toString();
					// out.println("clauseRetNode "+clauseRetNode+" clauseid "+clauseid);
				}
				return clauseid;

			} catch (Exception e) {
				return "error message getClauseByName" + e.getMessage();
			}
		}

		return clauseid;

	}

	public String getFileReplaceData_new(String email,String group, String clauseid, String clausename,
			SlingHttpServletResponse response, String SFresp, int count) {
		JSONObject twoclausejson = new JSONObject();
		JSONObject rtnobj = new JSONObject();

		PrintWriter out = null;
		try {
			out = response.getWriter();
			Node content = null;
			Node usernode = null;
			Node DocTigerAdvance = null;
			Node clauses = null;
			Node clausesidNode = null;
			Node MultilangNode = null;
			Node languagenode = null;
			JSONObject languagebasedjson = null;
			JSONArray desc = null;
			String language = null;

			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			content = session.getRootNode().getNode("content");

			FreeTrialandCart cart = new FreeTrialandCart();
			String freetrialstatus = cart.checkfreetrial(email);

			DocTigerAdvance = getDocTigerAdvNode(freetrialstatus, email,group, session, response);
			if (DocTigerAdvance != null) {

				if (DocTigerAdvance.hasNode("Clauses")) {
					clauses = DocTigerAdvance.getNode("Clauses");
					// out.print("Clauses "+clauses.getPath().toString());
					if (clauses.hasNode(clauseid)) {
						clausesidNode = clauses.getNode(clauseid);
						// --------
						rtnobj.put("<<" + clausesidNode.getProperty("Description").getString() + "No>>", count);

						if (clausesidNode.hasNode("Multilingual Data")) {
							MultilangNode = clausesidNode.getNode("Multilingual Data");
							// out.print("MultilangNode "+MultilangNode.getPath().toString());

							NodeIterator langitr = MultilangNode.getNodes();
							// out.println("langitr.getSize() "+langitr.getSize());
							while (langitr.hasNext()) {
								languagenode = langitr.nextNode();
								language = languagenode.getName();

								languagebasedjson = new JSONObject();
								languagebasedjson.put("name", clausesidNode.getProperty("ClauseName").getString());
								languagebasedjson.put("display", "2");
								languagebasedjson.put("numbering style", "1");
								desc = new JSONArray();

								if (languagenode.hasNode("Clause Description")) {
									Node clsdesc = languagenode.getNode("Clause Description");
									String resp = addmergefiled(clsdesc, SFresp, response);
									// out.println("resp 1"+resp);
									desc.put(resp);
								} else if (languagenode.hasNode("file")) {
								}
								languagebasedjson.put("desc", desc);

								if (clausesidNode.hasProperty("Display")
										&& clausesidNode.getProperty("Display").getString().equals("0")) {
									if (language.equalsIgnoreCase("arabic")) {
										rtnobj.put("<<" + clausename + "_" + language + ">>",
												clausesidNode.getProperty("Metadata").getString());
									} else {
										rtnobj.put("<<" + clausename + "_" + language + ">>", clausename);

									}
									rtnobj.put("[[" + clausename + "_" + language + "_d]]", "");
								} else if (clausesidNode.hasProperty("Display")
										&& clausesidNode.getProperty("Display").getString().equals("1")) {
									rtnobj.put("<<" + clausename + "_" + language + ">>", "");
									rtnobj.put("[[" + clausename + "_" + language + "_d]]", desc.toString());

								} else if (clausesidNode.hasProperty("Display")
										&& clausesidNode.getProperty("Display").getString().equals("2")) {
									if (language.equalsIgnoreCase("arabic")) {
										rtnobj.put("<<" + clausename + "_" + language + ">>",
												clausesidNode.getProperty("Metadata").getString());
									} else {
										rtnobj.put("<<" + clausename + "_" + language + ">>", clausename);

									}

									rtnobj.put("[[" + clausename + "_" + language + "_d]]", desc.toString());

								}
								// out.println("rtn in main "+rtnobj);
								if (clausesidNode.hasNode("ChildClauses")) {
									// call childparser method
									String clsname = clausesidNode.getProperty("ClauseName").getString();
									JSONArray child = parseChild(clausesidNode.getPath(), clsname, SFresp, response,
											language, rtnobj, Integer.toString(count));
									if (child != null) {
										languagebasedjson.put("Child", child);
									}
								}
								twoclausejson.put(language, languagebasedjson);
								// out.println("twoclausejson"+twoclausejson);
								// out.println("end language "+language);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return twoclausejson.toString();
		return rtnobj.toString();

	}

	public JSONArray parseChild(String pnodepath, String clausename, String SFresp, SlingHttpServletResponse response,
			String language, JSONObject rtnobj, String count) {
		JSONArray childArray = new JSONArray();

		PrintWriter out;
		try {
			out = response.getWriter();

			// out.println("rtnobj in child"+rtnobj);
			// out.println("pnodepath "+pnodepath);
			// out.println("clausename "+clausename);
			// out.println("language"+language);
			// out.println("count "+count);

			Node content = null;
			JSONObject modifiedSFrespobj = null;
			JSONObject SFrespobj = null;
			JSONObject child1obj = null;
			JSONArray arr = null;

			try {
				SFrespobj = new JSONObject(SFresp);
				modifiedSFrespobj = new JSONObject();
				JSONArray responsejsonarray = SFrespobj.getJSONArray("response");
				JSONObject responsejson = responsejsonarray.getJSONObject(0);
				Iterator<String> ke = responsejson.keys();
				while (ke.hasNext()) {
					String jsonkeyname = (String) ke.next();
					String jsonvalues = responsejson.getString(jsonkeyname);
					modifiedSFrespobj.put(jsonkeyname.toLowerCase(), jsonvalues);
					// out.println(jsonkeyname+" "+ jsonvalues);
				}
				// out.println("modifiedSFrespobj "+modifiedSFrespobj);

				session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
				Node pNode = session.getNode(pnodepath);
				if (pNode.hasProperty("RuleBased") && pNode.getProperty("RuleBased").getString() != "") {
					// out.println("rulebased");
					JSONObject ruleinput = new JSONObject();

					String ruleEngineName = pNode.getProperty("RuleBased").getString();
					String ruledata = pNode.getProperty("Ruledata").getString();
					// out.println("ruledata "+ruledata);
					JSONObject ruledataobj = new JSONObject(ruledata.replace("\\", ""));
					// out.println("ruledataobj "+ruledataobj);
					String ruleurl = ruledataobj.getString("URL");
					JSONArray InputArr = ruledataobj.getJSONArray("Input");
					// out.println("ruleurl "+ruleurl);
					// out.println("InputArr "+InputArr);
					JSONObject inobj = null;
					for (int i = 0; i < InputArr.length(); i++) {
						inobj = InputArr.getJSONObject(i);
						String inputfield = inobj.getString("field");
						String inputtype = inobj.getString("type");
						// out.println("inputfield "+inputfield +"inputtype "+inputtype);
						// out.println(modifiedSFrespobj.has(inputfield));
						// out.println(inputtype.equals("Integer"));
						// out.println(inputtype.equals("String"));

						if (modifiedSFrespobj.has(inputfield) && inputtype.equalsIgnoreCase("Integer")) {
							// out.println("has field integer");
							if (modifiedSFrespobj.has(inputfield)) {
								ruleinput.put(inputfield, Integer.parseInt(modifiedSFrespobj.getString(inputfield)));
							} else {
								ruleinput.put(inputfield, "");
							}

						} else if (modifiedSFrespobj.has(inputfield) && inputtype.equalsIgnoreCase("String")) {
							// out.println("has field String");
							if (modifiedSFrespobj.has(inputfield)) {

								ruleinput.put(inputfield, modifiedSFrespobj.getString(inputfield));
							} else {
								ruleinput.put(inputfield, "");
							}
						} else if (modifiedSFrespobj.has(inputfield) && inputtype.equalsIgnoreCase("textarea")) {
							// out.println("has field String");
							if (modifiedSFrespobj.has(inputfield)) {

								ruleinput.put(inputfield, modifiedSFrespobj.getString(inputfield));
							} else {
								ruleinput.put(inputfield, "");
							}
						} else {
							if (modifiedSFrespobj.has(inputfield)) {

								ruleinput.put(inputfield, modifiedSFrespobj.getString(inputfield));
							} else {
								ruleinput.put(inputfield, "");
							}
						}
					}

					// out.println("ruleinput "+ruleinput);
					// ruleinput.put("Property Type", "Type-1 V2");
					// String ruleres = new
					// SOAPCall().callPostJSon("http://35.236.213.87:8080/drools/callrules/doctiger8@gmail.com_DoctigerClauseNew_DoctigerClauseRuleEngineNew/fire",
					// ruleinput);
					String ruleres = new SOAPCall().callPostJSon(ruleurl, ruleinput);

					JSONObject ruleoutput = new JSONObject(ruleres);

					// out.println("ruleoutput "+ruleoutput);

					if (ruleoutput.length() != 0) {
						// out.println("ruleoutput "+ruleoutput);
						// out.println("clausename "+clausename);
						if (ruleoutput.has(clausename)) {
							// out.println("ruleoutput.has(clausename) "+ruleoutput.has(clausename));
							String[] childclauses = ruleoutput.getString(clausename).split(",");

							int mainchildcount = 0;

							if (pNode.hasNode("ChildClauses") && pNode.getNode("ChildClauses").hasNodes()) {
								NodeIterator itr = pNode.getNode("ChildClauses").getNodes();
								while (itr.hasNext()) {
									Node Child = itr.nextNode();
									int childarrcount = 0;

									for (int i = 0; i < childclauses.length; i++) {
										String forwordcount = null;
										childarrcount++;
										// out.println("childclauses[i] "+childclauses[i]);

										if (Child.getProperty("ChildClauseName").getString().equals(childclauses[i])) {
											if (Child.getProperty("Numbering Style").getString()
													.equalsIgnoreCase("1.1")) {
												// out.println("numbring style 1.1");
												int childcount = 1 + mainchildcount;
												rtnobj.put("<<" + Child.getProperty("Description").getString() + "No>>",
														count + "." + childcount);
												// out.println("<<"+Child.getProperty("Description").getString()+"No>>
												// "+ count+"."+childcount);
												forwordcount = count + "." + childcount;
											} else if (Child.getProperty("Numbering Style").getString()
													.equalsIgnoreCase("a")) {
												// out.println("numbring style a");
												char childcount = 'a';
												childcount = (char) (childcount + mainchildcount);
												// out.println("count ********* "+count+"."+childcount);
												rtnobj.put("<<" + Child.getProperty("Description").getString() + "No>>",
														count + "." + Character.toString(childcount));
												forwordcount = count + "." + Character.toString(childcount);
											} else if (Child.getProperty("Numbering Style").getString()
													.equalsIgnoreCase("i")) {
												// out.println("numbring style i");
												int childcount = 1 + mainchildcount;
												rtnobj.put("<<" + Child.getProperty("Description").getString() + "No>>",
														count + "." + childcount);
												forwordcount = count + "." + childcount;

											}

											child1obj = new JSONObject();
											child1obj.put("name", Child.getProperty("ChildClauseName").getString());
											child1obj.put("display", Child.getProperty("Display").getString());
											child1obj.put("numbering style",
													Child.getProperty("Numbering Style").getString());

											// out.println(Child.getProperty("ChildClauseName").getString());
											String resp = "";
											if (Child.hasNode("Multilingual Data")) {
												Node languageNode = null;

												Node multilingualNode = Child.getNode("Multilingual Data");
												if (multilingualNode.hasNode(language)) {
													languageNode = multilingualNode.getNode(language);
													arr = new JSONArray();

													Node childdesc = languageNode.getNode("Clause Description");
													if (childdesc.hasNode("Composed Online")) {
														Node compol = childdesc.getNode("Composed Online");
														// out.println("compol "+compol.getPath().toString());
														resp = addmergefiled(compol, SFresp, response);
													} else if (childdesc.hasNode("File")) {
														// out.println("childdesc "+childdesc);
														resp = addmergefiled(childdesc, SFresp, response);

													}
													// out.println("resp1 "+resp);
													Iterator<?> keys = ruleoutput.keys();

													while (keys.hasNext()) {
														String key = (String) keys.next();
														// out.println("key "+ key);
														if (resp.contains("<<" + key + ">>")) {
															// out.println( "resp.contains(\"<<\"+key+\">>\") " +
															// resp.contains("<<"+key+">>"));
															resp = resp.replace("<<" + key + ">>",
																	ruleoutput.getString(key));
															// out.println("resp2 "+resp);
														}
													}
													arr.put(resp);
												}
											}
											child1obj.put("desc", arr);

											if (Child.getProperty("Display").getString().equals("0")) {
												// onlu heading
												if (language.equalsIgnoreCase("arabic")) {
													rtnobj.put(
															"<<" + Child.getProperty("ChildClauseName").getString()
																	+ "_" + language + ">>",
															Child.getProperty("Metadata").getString());
												} else {
													rtnobj.put(
															"<<" + Child.getProperty("ChildClauseName").getString()
																	+ "_" + language + ">>",
															Child.getProperty("ChildClauseName").getString());
												}
												rtnobj.put("[[" + Child.getProperty("ChildClauseName").getString() + "_"
														+ language + "_d]]", "");

											} else if (Child.getProperty("Display").getString().equals("1")) {
												// out.println("in Display 1");
												// onlu desc
												rtnobj.put("<<" + Child.getProperty("ChildClauseName").getString() + "_"
														+ language + ">>", "");
												rtnobj.put("[[" + Child.getProperty("ChildClauseName").getString() + "_"
														+ language + "_d]]", arr.toString());

											} else if (Child.getProperty("Display").getString().equals("2")) {
												// heading and desc
												if (language.equalsIgnoreCase("arabic")) {
													rtnobj.put(
															"<<" + Child.getProperty("ChildClauseName").getString()
																	+ "_" + language + ">>",
															Child.getProperty("Metadata").getString());
												} else {
													rtnobj.put(
															"<<" + Child.getProperty("ChildClauseName").getString()
																	+ "_" + language + ">>",
															Child.getProperty("ChildClauseName").getString());
												}

												rtnobj.put("[[" + Child.getProperty("ChildClauseName").getString() + "_"
														+ language + "_d]]", arr.toString());

											}

											// ---if child node has sub child node method return array oc child just put
											// that array in main child json as child:[]
											if (Child.hasNode("ChildClauses")) {
												String chname = Child.getProperty("ChildClauseName").getString();
												JSONArray charr = parseChild(Child.getPath(), chname, SFresp, response,
														language, rtnobj, forwordcount);
												if (charr != null) {
													child1obj.put("Child", charr);
												}
											}

											mainchildcount++;
											break;
										} else {
											if (childarrcount == childclauses.length) {
												rtnobj.put("<<" + Child.getProperty("Description").getString() + "No>>",
														"");
												rtnobj.put("<<" + Child.getProperty("ChildClauseName").getString() + "_"
														+ language + ">>", "");
												rtnobj.put("[[" + Child.getProperty("ChildClauseName").getString() + "_"
														+ language + "_d]]", "");

											}

										}
									}
								}
							}

							childArray.put(child1obj);
						} else {
							child1obj = new JSONObject();
							childArray.put(child1obj);

						}
					} else {

						child1obj = new JSONObject();
						childArray.put(child1obj);
					}

				} else if ((!pNode.hasProperty("RuleBased")) && pNode.hasNode("ChildClauses")) {

					if (pNode.hasNode("ChildClauses") && pNode.getNode("ChildClauses").hasNodes()) {
						NodeIterator itr = pNode.getNode("ChildClauses").getNodes();
						int mainchildcount = 0;
						while (itr.hasNext()) {
							Node Child = itr.nextNode();

							String forwordcount = null;
							if (Child.getProperty("Numbering Style").getString().equalsIgnoreCase("1.1")) {
								// out.println("numbring style 1.1");

								int childcount = 1 + mainchildcount;
								rtnobj.put("<<" + Child.getProperty("Description").getString() + "No>>",
										count + "." + childcount);
								forwordcount = count + "." + childcount;
							} else if (Child.getProperty("Numbering Style").getString().equalsIgnoreCase("a")) {
								// out.println("numbring style a");

								char childcount = 'a';
								childcount = (char) (childcount + mainchildcount);
								rtnobj.put("<<" + Child.getProperty("Description").getString() + "No>>",
										count + "." + Character.toString(childcount));
								forwordcount = count + "." + Character.toString(childcount);
							} else if (Child.getProperty("Numbering Style").getString().equalsIgnoreCase("i")) {
								// out.println("numbring style i");
								int childcount = 1 + mainchildcount;
								rtnobj.put("<<" + Child.getProperty("Description").getString() + "No>>",
										count + "." + childcount);
								forwordcount = count + "." + childcount;

							}

							child1obj = new JSONObject();
							child1obj.put("name", Child.getProperty("ChildClauseName").getString());
							child1obj.put("display", Child.getProperty("Display").getString());
							child1obj.put("numbering style", Child.getProperty("Numbering Style").getString());

							// out.println(Child.getProperty("ChildClauseName").getString());
							String resp = "";
							if (Child.hasNode("Multilingual Data")) {
								Node languageNode = null;

								Node multilingualNode = Child.getNode("Multilingual Data");
								if (multilingualNode.hasNode(language)) {

									languageNode = multilingualNode.getNode(language);
									arr = new JSONArray();

									Node childdesc = languageNode.getNode("Clause Description");
									if (childdesc.hasNode("Composed Online")) {
										Node compol = childdesc.getNode("Composed Online");
										// out.println("compol "+compol.getPath().toString());
										resp = addmergefiled(compol, SFresp, response);
									} else if (childdesc.hasNode("File")) {
										// out.println("childdesc "+childdesc);
										resp = addmergefiled(childdesc, SFresp, response);
									}
									// out.println("resp1 "+resp);

									arr.put(resp);
								}
							}

							if (Child.getProperty("Display").getString().equals("0")) {
								// onlu heading
								if (language.equalsIgnoreCase("arabic")) {
									rtnobj.put("<<" + Child.getProperty("ChildClauseName").getString() + "_" + language
											+ ">>", Child.getProperty("Metadata").getString());
								} else {
									rtnobj.put("<<" + Child.getProperty("ChildClauseName").getString() + "_" + language
											+ ">>", Child.getProperty("ChildClauseName").getString());
								}
								rtnobj.put("[[" + Child.getProperty("ChildClauseName").getString() + "_" + language
										+ "_d]]", "");

							} else if (Child.getProperty("Display").getString().equals("1")) {
								// onlu desc
								rtnobj.put(
										"<<" + Child.getProperty("ChildClauseName").getString() + "_" + language + ">>",
										"");
								rtnobj.put("[[" + Child.getProperty("ChildClauseName").getString() + "_" + language
										+ "_d]]", arr.toString());

							} else if (Child.getProperty("Display").getString().equals("2")) {
								// heading and desc
								if (language.equalsIgnoreCase("arabic")) {
									rtnobj.put("<<" + Child.getProperty("ChildClauseName").getString() + "_" + language
											+ ">>", Child.getProperty("Metadata").getString());
								} else {
									rtnobj.put("<<" + Child.getProperty("ChildClauseName").getString() + "_" + language
											+ ">>", Child.getProperty("ChildClauseName").getString());
								}

								rtnobj.put("[[" + Child.getProperty("ChildClauseName").getString() + "_" + language
										+ "_d]]", arr.toString());

							}

							child1obj.put("desc", arr);

							// ---if child node has sub child node method return array oc child just put
							// that array in main child json as child:[]
							if (Child.hasNode("ChildClauses")) {
								String chname = Child.getProperty("ChildClauseName").getString();
								JSONArray charr = parseChild(Child.getPath(), chname, SFresp, response, language,
										rtnobj, forwordcount);
								if (charr != null) {
									child1obj.put("Child", charr);
								}
							}

							childArray.put(child1obj);
							mainchildcount++;
						}

					} else {
						child1obj = new JSONObject();
						childArray.put(child1obj);
					}

				} else {

					child1obj = new JSONObject();
					childArray.put(child1obj);
				}

				// out.println("rtnobj in child end "+rtnobj);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return childArray;

	}

	public JSONObject getDocGenValidity(String email, SlingHttpServletRequest request,
			SlingHttpServletResponse response) {

		PrintWriter out = null;
		Node content = null;
		Node usernode = null;
		Node emailnode = null;
		Node DocTigerAdvance = null;
		Node DocGenCounter = null;
		JSONObject returnobj = null;
		String admin = "";
		String activeServiceId = "";
		try {
			out = response.getWriter();

			returnobj = new JSONObject();
			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			content = session.getRootNode().getNode("content");

			if (content.hasNode("user") && content.getNode("user").hasNode(email.replaceAll("@", "_"))) {
				usernode = content.getNode("user");
				emailnode = usernode.getNode(email.replaceAll("@", "_"));

				if (emailnode.hasNode("services")) {
					NodeIterator itr = emailnode.getNode("services").getNodes();
					while (itr.hasNext()) {
						Node serviceIdNode = itr.nextNode();
						if (serviceIdNode.hasProperty("activeStatus")
								&& serviceIdNode.getProperty("activeStatus").getString().equalsIgnoreCase("true")) {
							admin = serviceIdNode.getProperty("admin").getString();
							activeServiceId = serviceIdNode.getName();
							break;
						}
					}

				}
			}
		} catch (Exception e) {
			e.getMessage();
			try {
				returnobj = new JSONObject();
				returnobj.put("valid", "false");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return returnobj;
		}
		return null;
	}

	
	public String updateDocGenCounter(String useremail,String freetrialstatus, Node doctigerAdvNode, Session session, SlingHttpServletResponse response) {
		Node serviceIdNode = null;
		Node  GroupNode=null;
		Node useremailNode=null;
		try {
			if(freetrialstatus.equalsIgnoreCase("1")) {
			//increment count on groupnode 
			GroupNode=doctigerAdvNode.getParent();
			if(GroupNode.hasProperty("Document_count_group")){
				int count=Integer.parseInt(GroupNode.getProperty("Document_count_group").getString())+1;
				GroupNode.setProperty("Document_count_group", count+"");
			}else {
				int count =0;
				GroupNode.setProperty("Document_count_group", count+"");
			}
			
			//increment count on useremail
			if(GroupNode.hasNode("users") && GroupNode.getNode("users").hasNode(useremail.replace("@", "_"))) {
				useremailNode=GroupNode.getNode("users").getNode(useremail.replace("@", "_"));
				
				if(useremailNode.hasProperty("Document_count_user")) {
					int count=Integer.parseInt(useremailNode.getProperty("Document_count_user").getString())+1;
					useremailNode.setProperty("Document_count_user", count+"");
				}else {
					int count =0;
					useremailNode.setProperty("Document_count_user", count+"");
				}
			}
			
			//increment count on serviceid 
			serviceIdNode=GroupNode.getParent();
			if(serviceIdNode.hasProperty("Document_count_Id")) {
				int count=Integer.parseInt(serviceIdNode.getProperty("Document_count_Id").getString())+1;
				serviceIdNode.setProperty("Document_count_Id", count+"");
			}else{
				int count =0;
				serviceIdNode.setProperty("Document_count_Id", count+"");
			}
			
			
			
			}
			session.save();
			return "true";

		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return "false";
		}
	}

	public HashMap<String, String> getCustomerDetails(String serviceId, String customerId,
			SlingHttpServletRequest request, SlingHttpServletResponse response) {
		PrintWriter out = null;
		InputStream inputXml = null;
		String content = "";
		String serviceUrl = "";
		String url = "";
		HashMap<String, String> map = null;
		String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		Date endDate = new Date();
		endDate.setDate(endDate.getDate() - 1);
		Date startDate = new Date();
		String url1 = "";

		try {
			out = response.getWriter();
			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			// if (session.getNode("/content/ip").hasProperty("auctionService")) {
			// serviceUrl = session.getNode("/content/ip")
			// .getProperty("auctionService").getString();
			// }
			// http://35.221.160.146:8082/AuctionServices/services/Auctions_WSDL/getCustomerServiceStatus?customerId=viki@gmail.com
			url = "http://"+bundleststic.getString("Sling_ip")+":8082/AuctionServices/services/Auctions_WSDL/getCustomerServiceStatus?customerId="
					+ customerId;

			// url="http://35.221.160.146:8082/AuctionServices/services/Auctions_WSDL/getCustomerServiceStatus?customerId=viki@gmail.com";
			// map --- {quantity=5.0, orderId=, consumptionQuantity=1.0, customerName=Vivek
			// undefined, productCode=VChat, serviceStartDate=2018-11-13, configId=,
			// customerId=viki@gmail.com, lastConsumptionDate=2018-11-20, rfq=Y,
			// serviceId=799, productDescription=, serviceEndDate=2019-03-13, status=active}

			inputXml = new URL(url).openConnection().getInputStream();
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputXml);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("ns:return");

			for (int temp = 0; temp < nList.getLength(); temp++) {
				url1 = "";
				org.w3c.dom.Node nNode = nList.item(temp);
				if (nNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
					map = new HashMap<String, String>();
					Element eElement = (Element) nNode;
					if (eElement.getElementsByTagName("ax21:serviceId").item(0).getTextContent()
							.equalsIgnoreCase(serviceId)) {
						map.put("serviceId", eElement.getElementsByTagName("ax21:serviceId").item(0).getTextContent());

						map.put("orderId", eElement.getElementsByTagName("ax21:orderId").item(0).getTextContent());
						map.put("productCode",
								eElement.getElementsByTagName("ax21:productCode").item(0).getTextContent());
						map.put("configId", eElement.getElementsByTagName("ax21:configId").item(0).getTextContent());
						map.put("consumptionQuantity",
								eElement.getElementsByTagName("ax21:consumptionQuantity").item(0).getTextContent());
						map.put("customerId",
								eElement.getElementsByTagName("ax21:customerId").item(0).getTextContent());
						map.put("customerName",
								eElement.getElementsByTagName("ax21:customerName").item(0).getTextContent());
						map.put("lastConsumptionDate",
								eElement.getElementsByTagName("ax21:lastConsumptionDate").item(0).getTextContent());
						map.put("productDescription",
								eElement.getElementsByTagName("ax21:productDescription").item(0).getTextContent());
						map.put("quantity", eElement.getElementsByTagName("ax21:quantity").item(0).getTextContent());
						map.put("rfq", eElement.getElementsByTagName("ax21:rfq").item(0).getTextContent());
						map.put("serviceEndDate",
								eElement.getElementsByTagName("ax21:serviceEndDate").item(0).getTextContent());
						map.put("serviceStartDate",
								eElement.getElementsByTagName("ax21:serviceStartDate").item(0).getTextContent());
						map.put("status", eElement.getElementsByTagName("ax21:status").item(0).getTextContent());
						map.put("serviceId", eElement.getElementsByTagName("ax21:serviceId").item(0).getTextContent());
						break;
					}
					out.println("map --- " + map);
				}
			}

		} catch (Exception e) {
			out.println(e);

		} finally {
			// session.logout();

		}
		return map;

	}


	



}
