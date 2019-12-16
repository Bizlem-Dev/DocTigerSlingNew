package social.com.app.servlet;

import javax.servlet.RequestDispatcher;
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

import java.io.*;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/servlet/service/DoctigerNew_UIPage" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class DoctigerNewUIDisplay extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	final String VIDEOEXTENSION[] = { ".3g2", ".3gp", ".asf", ".asx", ".avi", ".flv", ".mov", ".mp4", ".mpg", ".rm",
			".swf", ".vob", ".wmv" };

	final int NUMBEROFRESULTSPERPAGE = 10;

	// http://35.201.178.201:8082/portal/servlet/service/GetRulenWorkflow
		
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		out.println("Test");
		try {
			//RequestDispatcher dis= request.getRequestDispatcher("/content/DoctigerCSSAndJS/static/.DocTiger");
		//RequestDispatcher dis= request.getRequestDispatcher("/content/common/.header");
			RequestDispatcher dis= request.getRequestDispatcher("/content/static/.DocTigerNewUI");

			dis.forward(request, response);
		}catch (Exception e) {
			// TODO: handle exception
			out.println(e.getMessage().toString());
			out.println(e);

		}
	}
	}