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
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/newpath_excel" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class newpath_excel extends SlingAllMethodsServlet {

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

		//Node userNode = null;
		//Node emailNode = null;
		Node dtaNode = null;
		
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

			email = resultjsonobject.getString("Email");
			group = resultjsonobject.getString("group");

			FreeTrialandCart cart= new FreeTrialandCart();
			String freetrialstatus=cart.checkfreetrial(email);
			
			dtaNode =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  email,group, session, response );
			if(dtaNode!=null) {

			
			String filename = resultjsonobject.getString("filename");
			int e = filename.indexOf(".");
			String extn = filename.substring(e + 1);
			if(extn.equals("xls")) {
				moduletype = resultjsonobject.getString("Moduletype");
				Node ExcelNode= null;
				if(dtaNode.hasNode("Excel")){
					ExcelNode= dtaNode.getNode("Excel");
				}else {
					ExcelNode= dtaNode.addNode("Excel");
					ExcelNode.setProperty("last_count", 0);
				}
				
				String data = resultjsonobject.getString("filedata");
//				/* Integration of doctiger with leadautoconverter.  */
//				String leadautoconverterApi="http://"+bundleststic.getString("Sling_ip")+":8082/portal/servlet/service/uploadPersonalSubscibersExcel";
//				try {
//					new SOAPCall().callPostJSonModified(leadautoconverterApi,resultjsonobject);
//					
//				}catch (Exception ec) {
//					// TODO: handle exception
//				}
				
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
					if(moduletype.equalsIgnoreCase("template") ) {
						//filectnode.setProperty("filepath", request.getScheme()+"://"+request.getServerName()+":"+ request.getServerPort()+request.getContextPath()+ "/content/user/"+email.replaceAll("@", "_")+"/DocTigerAdvanced/TemplateLibrary/"+templateName+ "/externalparamobject/file/"+lsct+"/"+ subfileNode.getName() + "/" + filename);
						
						//filectnode.setProperty("filepath", request.getScheme()+"://"+request.getServerName()+":"+ request.getServerPort()+request.getContextPath()+ "/bin/cpm/nodes/property.bin/content/user/"+email.replaceAll("@", "_")+"/DocTigerAdvanced/TemplateLibrary/"+templateName+ "/externalparamobject/file/"+lsct+"/"+ subfileNode.getName() + "/" + filename+"/_jcr_content?name=jcr%3Adata");
						
						filectnode.setProperty("filepath", request.getScheme()+"://"+request.getServerName()+":"+ request.getServerPort()+request.getContextPath()+dtaNode.getPath()+"/Excel/"+lsct+"/"+ subfileNode.getName() + "/" + filename);
						
						//String fp=request.getScheme()+"://"+request.getServerName()+":"+ request.getServerPort()+request.getContextPath()+ "/bin/cpm/nodes/property.bin/content/user/"+email.replaceAll("@", "_")+"/DocTigerAdvanced/TemplateLibrary/"+templateName+ "/externalparamobject/file/"+lsct+"/"+ subfileNode.getName() + "/" + filename+"/_jcr_content?name=jcr%3Adata";
					} else if(moduletype.equalsIgnoreCase("mailtemplate")) {
						//filectnode.setProperty("filepath", request.getScheme()+"://"+request.getServerName()+":"+ request.getServerPort()+request.getContextPath()+ "/content/user/"+email.replaceAll("@", "_")+"/DocTigerAdvanced/Communication/MailTemplate/"+mailtemplatename+ "/externalparamobject/file/"+lsct+"/"+ subfileNode.getName() + "/" + filename);
						
						//filectnode.setProperty("filepath", request.getScheme()+"://"+request.getServerName()+":"+ request.getServerPort()+request.getContextPath()+ "/bin/cpm/nodes/property.bin/content/user/"+email.replaceAll("@", "_")+"/DocTigerAdvanced/Communication/MailTemplate/"+mailtemplatename+ "/externalparamobject/file/"+lsct+"/"+ subfileNode.getName() + "/" + filename+"/_jcr_content?name=jcr%3Adata");
						
						filectnode.setProperty("filepath", request.getScheme()+"://"+request.getServerName()+":"+ request.getServerPort()+request.getContextPath()+dtaNode.getPath()+"/Excel/"+lsct+"/"+ subfileNode.getName() + "/" + filename);
					} else if(moduletype.equalsIgnoreCase("clause")) {
						//filectnode.setProperty("filepath", request.getScheme()+"://"+request.getServerName()+":"+ request.getServerPort()+request.getContextPath()+ "/content/user/"+email.replaceAll("@", "_")+"/DocTigerAdvanced/Clauses/"+ClauseId+ "/External Parameter/file/"+lsct+"/"+ subfileNode.getName() + "/" + filename);
						
						//filectnode.setProperty("filepath", request.getScheme()+"://"+request.getServerName()+":"+ request.getServerPort()+request.getContextPath()+ "/bin/cpm/nodes/property.bin/content/user/"+email.replaceAll("@", "_")+"/DocTigerAdvanced/Clauses/"+ClauseId+ "/External Parameter/file/"+lsct+"/"+ subfileNode.getName() + "/" + filename+"/_jcr_content?name=jcr%3Adata");
						
						filectnode.setProperty("filepath", request.getScheme()+"://"+request.getServerName()+":"+ request.getServerPort()+request.getContextPath()+dtaNode.getPath()+"/Excel/"+lsct+"/"+ subfileNode.getName() + "/" + filename);
					}else {
						filectnode.setProperty("filepath", request.getScheme()+"://"+request.getServerName()+":"+ request.getServerPort()+request.getContextPath()+dtaNode.getPath()+"/Excel/"+lsct+"/"+ subfileNode.getName() + "/" + filename);
				
						//filectnode.setProperty("filepath", request.getScheme()+"://"+request.getServerName()+":"+ request.getServerPort()+request.getContextPath()+ "/bin/cpm/nodes/property.bin/content/user/"+email.replaceAll("@", "_")+"/DocTigerAdvanced/Excel/"+lsct+"/"+ subfileNode.getName() + "/" + filename+"/_jcr_content?name=jcr%3Adata");	
					}
					
					jcrNode = subfileNode.addNode("jcr:content", "nt:resource");
					jcrNode.setProperty("jcr:data", myInputStream);
					// jcrNode.setProperty("jcr:data", stream);
					jcrNode.setProperty("jcr:mimeType", "attach");
					js.put("status", "success");
					out.println(js);
				}catch (Exception ec) {}
			}else {
				js.put("status", "error");
				js.put("message", "Please upload xls file");
				out.println(js);
			}
			session.save();
			
			
			}else {
				js.put("status", "error");
				js.put("message", "Invalid user");
				out.println(js);
			}
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
