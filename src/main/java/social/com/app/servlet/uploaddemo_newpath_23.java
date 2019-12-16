package social.com.app.servlet;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ResourceBundle;

import javax.jcr.Node;
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
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;

import com.service.ParseSlingData;
import com.service.SOAPCall;
import com.service.impl.FreeTrialandCart;
import com.service.impl.ParseSlingDataImpl;
import com.sun.jersey.core.util.Base64;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/uploadcheckfile_newpath_2" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class uploaddemo_newpath_23 extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	ResourceBundle bundle = ResourceBundle.getBundle("config");
	static ResourceBundle bundleststic = ResourceBundle.getBundle("config");
	//@Reference
	//private ParseSlingData parseSlingData;
	ParseSlingData parseSlingData= new ParseSlingDataImpl();

	@SuppressWarnings("deprecation")
	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		
		JSONObject js = new JSONObject();
		String moduletype = "";
		String email = "";
		String group = "";
		String filename=null;
		String data=null;
		//Node userNode = null;
		//Node emailNode = null;
		Node dtaNode = null;
		String filepathproperty="";
		String Id_name1="";
		String  ClauseId ="";
		String 	ClauseName=	"";
		String Path ="";
		try {
			Session session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			BufferedInputStream bis = new BufferedInputStream(request.getInputStream());
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			int result = bis.read();

			while (result != -1) {
				buf.write((byte) result);
				result = bis.read();
			}
			String res = buf.toString("UTF-8");
			JSONObject resultjsonobject = new JSONObject(res);
		       out.println("resultjsonobject "+resultjsonobject);

			email = resultjsonobject.getString("Email");
			group = resultjsonobject.getString("group");
			moduletype = resultjsonobject.getString("Moduletype");
		   filename = resultjsonobject.getString("filename");
			 data = resultjsonobject.getString("filedata");

			int e = filename.indexOf(".");
			String extn = filename.substring(e + 1);
			
       if(resultjsonobject.has("Id_name1")) {
	   Id_name1=resultjsonobject.getString("Id_name1");
          }
       
       out.println("email "+email);
       out.println("group "+group);
       out.println("moduletype "+moduletype);
       out.println("filename "+filename);
       out.println("Id_name1 "+Id_name1);
       

       
       
       
       
			FreeTrialandCart cart= new FreeTrialandCart();
			String freetrialstatus=cart.checkfreetrial(email);
			
			dtaNode =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  email,group, session, response );
			if(dtaNode!=null) {
				if(moduletype.equalsIgnoreCase("clause")) {
					  ClauseId =resultjsonobject.getString("ClauseId");
					 	ClauseName=	 resultjsonobject.getString("ClauseName");
					 Path = ClauseId;
					 out.println("path 1"+Path );
				}else if(moduletype.equalsIgnoreCase("template")) {
					 	ClauseName=	 resultjsonobject.getString("ClauseName");
					 Path = dtaNode.getPath()+"/TemplateLibrary/"+ClauseName;
					 out.println("path 2"+Path);
				}else if(moduletype.equalsIgnoreCase("mailtemplate") 	) {
				 	ClauseName=	 resultjsonobject.getString("ClauseName");
					if(Id_name1.equalsIgnoreCase("mailName")) {
						 Path = dtaNode.getPath()+"/Communication/MailTemplate/"+ClauseName;
						 out.println("path 3"+Path);
					}else if (Id_name1.equalsIgnoreCase("smsName")) {
						 Path = dtaNode.getPath()+"/SMSTemplate/"+ClauseName;
						 out.println("path 4"+Path);
					}
				}
				
				out.println("session.nodeExists(Path)   "+session.nodeExists(Path));

				if(session.nodeExists(Path)) {
					Node newNode =session.getNode(Path);
					out.println("newNode  "+newNode);
					if(extn.equals("xls")) {
						Node ExcelNode= null;
					if(newNode.hasNode("Excel")) {
						
						ExcelNode= newNode.getNode("Excel");
					}else {
						ExcelNode= newNode.addNode("Excel");
						ExcelNode.setProperty("last_count", 0);
					}
					byte[] bytes = Base64.decode(data);
					
					Node jcrNode = null;
					Node filefileNode = null;
					InputStream myInputStream = new ByteArrayInputStream(bytes);
		
					long lsct = ExcelNode.getProperty("last_count").getLong();
					Node filectnode = null;
					if (!ExcelNode.hasNode(Long.toString(lsct))) {
						filectnode = ExcelNode.addNode(Long.toString(lsct));
						ExcelNode.setProperty("last_count", lsct + 1);
					} else {
						filectnode = ExcelNode.getNode(Long.toString(lsct));
					}
					
					if (!filectnode.hasNode(filename)) {
						filefileNode = filectnode.addNode(filename);
					} else {
						filefileNode = filectnode.getNode(filename);
					}
					
					try {
						Node subfileNode = filefileNode.addNode(filename, "nt:file");
						filectnode.setProperty("filename", filename);
						filectnode.setProperty("filepath",request.getScheme()+"://"+request.getServerName()+":"+ request.getServerPort()+request.getContextPath()+ filectnode.getPath()+"/"+subfileNode.getName() + "/" + filename);
						jcrNode = subfileNode.addNode("jcr:content", "nt:resource");
						jcrNode.setProperty("jcr:data", myInputStream);
						// jcrNode.setProperty("jcr:data", stream);
						jcrNode.setProperty("jcr:mimeType", "attach");
						js.put("status", "success");
						out.println(js);
					
				}catch(Exception e1) {}
				
			}else {
				js.put("status", "error");
				js.put("message", "Please upload xls file");
				out.println(js);
			}
			
			
			}else {
				js.put("status", "error");
				js.put("message", "Invalid user");
				out.println(js);
			}
			}
			session.save();

		} catch (Exception e) {
			// TODO: handle exception
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
