package social.com.app.servlet;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;

import com.service.ParseSlingData;
import com.service.ReadHeader_Excel;
import com.service.impl.FreeTrialandCart;
import com.service.impl.ParseSlingDataImpl;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/getHeaders_Excel" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class getHeadersFromExcel extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;	
	
	//@Reference
	//private ParseSlingData parseSlingData;
	ParseSlingData parseSlingData= new ParseSlingDataImpl();


	@Override
	protected void doPost(SlingHttpServletRequest req, SlingHttpServletResponse rep) throws IOException {
		PrintWriter out = rep.getWriter();
		
		Session session = null;
		
		//Node emailnode=null;
		Node ExcelNode=null;
		
		String email=null;
		long lastcount;
		
		try {
			
			BufferedInputStream bis = new BufferedInputStream(req.getInputStream());
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			int result = bis.read();
			while (result != -1) {
				buf.write((byte) result);
				result = bis.read();
			}
			String res = buf.toString("UTF-8");
			JSONObject obj = new JSONObject(res);
		  
			email=obj.getString("email").replace("@", "_");
			String email1=obj.getString("email");
			String group=obj.getString("group");

			
			session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
			
			
				JSONObject resultjson= new JSONObject();
				
				FreeTrialandCart cart= new FreeTrialandCart();
				String freetrialstatus=cart.checkfreetrial(email1);
				
			Node	doctiger =	parseSlingData.getDocTigerAdvNode( freetrialstatus,  email1,group, session, rep );
		//	out.println("doctiger  "+doctiger);

			if(doctiger!=null) {

				if( doctiger.hasNode("Excel") ){
					ExcelNode=doctiger.getNode("Excel") ;
				//	out.println("ExcelNode  "+ExcelNode);
					lastcount=	ExcelNode.getProperty("last_count").getLong();
					//out.println("lastcount  "+lastcount);
					String count=Integer.toString((int) (lastcount-1));
					if(ExcelNode.hasNode(count)) {
						Node reqFileNode=ExcelNode.getNode(count);
					reqFileNode.getProperty("filename").getString();
						String filepath = reqFileNode.getProperty("filepath").getString();
					//out.println("filepath  "+reqFileNode);
						ReadHeader_Excel rm = new ReadHeader_Excel();
						
						JSONArray resultjsonstring = rm.getJsondatabypk( email,  filepath);
				//	out.print("resultjsonstring "+resultjsonstring);
					resultjson.put("status", "success");
					resultjson.put("Headers", resultjsonstring);
					out.println(resultjson.toString());
					}else {
						resultjson.put("status", "error");
						resultjson.put("message", "Excel Not found");
						out.println(resultjson.toString());
					}
				}else {					
					resultjson.put("status", "error");
					resultjson.put("message", "Excel Not found");
					out.println(resultjson.toString());
				}
			}else {
				resultjson.put("status", "error");
				resultjson.put("message", "Invalid user");
				out.print(resultjson.toString());
			}
		} catch (Exception e) {
			out.print("error "+e.getMessage());
		}
	}	
}
