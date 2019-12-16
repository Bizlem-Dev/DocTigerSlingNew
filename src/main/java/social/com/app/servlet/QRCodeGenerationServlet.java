package social.com.app.servlet;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
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
import org.apache.sling.jcr.api.SlingRepository;

import com.service.ParseSlingData;
import com.service.impl.FreeTrialandCart;
import com.service.impl.ParseSlingDataImpl;

import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/QRCodeGeneration"}),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class QRCodeGenerationServlet extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	
	//@Reference
	//private ParseSlingData parseSlingData;
	ParseSlingData parseSlingData= new ParseSlingDataImpl();

	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out= response.getWriter();
		Session session =null;
		
		String tempName="";
		String size="";
		String position="";
		String tableNo="";
		int count=0; 
		
		JSONArray paramArray= new JSONArray();
		
		//Node userNode=null;
		//Node emailNode=null;
		Node dtaNode=null;
		Node tempNameNode= null;
		Node QRCodeNode= null;
		Node paramNode= null;
		Node paramcntNode=null; 
		
		BufferedInputStream bis = new BufferedInputStream(request.getInputStream());
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		int result = bis.read();
		while (result != -1) {
			buf.write((byte) result);
			result = bis.read();
		}
		String res = buf.toString("UTF-8"); 			  
		
		JSONObject basicObj= new JSONObject();
		try {
			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			JSONObject obj = new JSONObject(res);
			
			//String email= obj.getString("Email").replace("@", "_");
			
			String usrid = obj.getString("Email");
			String group = obj.getString("group");

			FreeTrialandCart cart= new FreeTrialandCart();
			String freetrialstatus=cart.checkfreetrial(usrid);
			
			dtaNode =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  usrid, group,session, response );
			if(dtaNode!=null) {

			
			if(dtaNode.hasNode("TemplateLibrary") ){
			
			Node tempn = null;
			tempn = dtaNode.getNode("TemplateLibrary");


			//String username = resultjsonobject.getString("username");
			tempName=obj.getString("templatename");
			
			if(tempn.hasNode(tempName)) {
				tempNameNode= tempn.getNode(tempName);
			}else {
			//	tempNameNode= tempn.addNode(tempName);
			}
			
			if(tempNameNode.hasNode("QR Code")) {
				QRCodeNode= tempNameNode.getNode("QR Code");
			}else {
				QRCodeNode= tempNameNode.addNode("QR Code");
			}
			
			
			size= obj.getString("size");
			position= obj.getString("position");
			tableNo= obj.getString("tableNo");
			paramArray= obj.getJSONArray("param");
			
			if(QRCodeNode.hasNode("Param")){
				paramNode= QRCodeNode.getNode("Param");
				count=(int) paramNode.getProperty("Count").getLong();
			}else {
				paramNode= QRCodeNode.addNode("Param");
				paramNode.setProperty("Count", count);
			}
			
						
			JSONObject sfobj= new JSONObject();
			
			//JSONArray paramArr= sfobj.getJSONArray(key);
			//sfobj.getString (key);
			//String [] paramarr= new String[paramArray.length()];
			for(int j=0; j<paramArray.length(); j++){
				sfobj= paramArray.getJSONObject(j);
				
				if(paramNode.hasNode(""+count)){
					paramcntNode=paramNode.getNode(""+count);
				}else{
					String lastcount=paramNode.getProperty("Count").getString();
					paramcntNode=paramNode.addNode(""+lastcount);
					count++;
					paramNode.setProperty("Count", count);
				}
				
				String SFObject= sfobj.getString("SFObject");
				String field= sfobj.getString("Field");
				
				paramcntNode.setProperty("SFObject", SFObject);
				paramcntNode.setProperty("Field", field);
			}
			
			/*String headerVal="";
			
			for(int j=0; j<paramArray.length(); j++){
				headerVal= paramArray.getString(j);
				
				if(paramNode.hasNode(""+count)){
					paramcntNode=paramNode.getNode(""+count);
				}else{
					String lastcount=paramNode.getProperty("Count").getString();
					paramcntNode=paramNode.addNode(""+lastcount);
					count++;
					paramNode.setProperty("Count", count);
				}
				
				paramcntNode.setProperty("Field", headerVal);
			}*/
			//QRCodeNode.setProperty("Parameters", paramarr);
			
			
			QRCodeNode.setProperty("TemplateName", tempName);
			QRCodeNode.setProperty("Size", size);
			QRCodeNode.setProperty("Position", position);
			QRCodeNode.setProperty("Table_No", tableNo);
			basicObj.put("status", "success");
			
			out.println(basicObj);
			}}else {
				JSONObject ret= new JSONObject();

				ret.put("status", "error");
				ret.put("message", "Invalid user");
				out.println(ret);
			}
			session.save();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					JSONObject ret= new JSONObject();
					try {
						ret.put("status", "error");
						ret.put("message", e.getMessage());
						out.println(ret);					
						} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
		

	}
}
