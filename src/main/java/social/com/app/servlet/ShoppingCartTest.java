package social.com.app.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
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
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.jcr.api.SlingRepository;

import com.service.ParseSlingData;
import com.service.impl.FreeTrialandCart;
import com.service.impl.ParseSlingDataImpl;

@Component(immediate = true, metatype = false)
@Service(value = javax.servlet.Servlet.class)
@Properties({ @Property(name = "service.description", value = "Save product Servlet"),
		@Property(name = "service.vendor", value = "VISL Company"),
		@Property(name = "sling.servlet.paths", value = { "/process/shoppingcart/ShoppingCartUserCheckServ123" }),
		@Property(name = "sling.servlet.resourceTypes", value = "sling/servlet/default"),
		@Property(name = "sling.servlet.extensions", value = { "hotproducts", "cat", "latestproducts", "brief",
				"prodlist", "catalog", "viewcart", "productslist", "addcart", "createproduct", "checkmodelno",
				"productEdit" }) })
@SuppressWarnings("serial")
public class ShoppingCartTest  extends SlingAllMethodsServlet {

	@Reference
	private SlingRepository repo;
	//http://bluealgo.com:8082/portal/process/shoppingcart/ShoppingCartUserCheckServ123?userId=viki@gmail.com
	//@Reference
	//private ParseSlingData parseSlingData;
	ParseSlingData parseSlingData= new ParseSlingDataImpl();

@Override
protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
		throws ServletException, IOException {
	PrintWriter out = response.getWriter();
	Session session = null;
	Node userNode = null;
	String res;
	//out.println("prod");
	try {
		out = response.getWriter();
		String userId=request.getParameter("userId");
		String userName = userId.replace("@", "_");
		System.out.println("userId "+userId +" userName "+userName);
		session = repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
		userNode = session.getRootNode().getNode("content").getNode("user");
		if (userNode.hasNode(userName)) {
		//response.getOutputStream().print("true");
		res="true";
		} else {
		//response.getOutputStream().print("false");
		res="false";
		}
		//out.println("res "+res);
		if( res.equals("false")){
		String password = generateSessionKey(6);
		out.println("password "+password);
		final String[] paramKey = {"givenName", "username", "email", "password", "confirmPassword","mobileNumber"};
		final String[] paramValue = {"New User", userId,
		userId, password, password,"7567788766"};
		
		
		Runnable myrunnable = new Runnable() {
			public void run() {
				try {
					String result =callPostService("http://bluealgo.com/portal/app/newaccount", paramKey, paramValue);
					System.out.println("result "+result);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		};
		new Thread(myrunnable).start();

		
		
		out.print("true");
		}else {
		out.print("false");
		}
		
		session.save();
		} catch (Exception e) {
		e.printStackTrace();
		}
}


public  static String generateSessionKey(int length) {
    String alphabet
            = new String("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"); //9
    int n = alphabet.length(); //10

    String result = new String();
    Random r = new Random(); //11

    for (int i = 0; i < length; i++) //12
    {
        result = result + alphabet.charAt(r.nextInt(n)); //13
    }
    return result;
}
public static String callPostService(String urlStr, String[] paramName,
        String[] paramValue) {

    StringBuilder response = new StringBuilder();
    URL url;
    try {
        System.out.println("caalign callPostService");
        url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setAllowUserInteraction(false);
        conn.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
        OutputStream out = conn.getOutputStream();
        Writer writer = new OutputStreamWriter(out, "UTF-8");
        for (int i = 0; i < paramName.length; i++) {
            writer.write(paramName[i]);
            writer.write("=");
            writer.write(URLEncoder.encode(paramValue[i], "UTF-8"));
            writer.write("&");
        }
        writer.close();
        out.close();
        if (conn.getResponseCode() != 200) {
            System.out.println("indeside caalign responce" + conn.getResponseCode());
        }
        System.out.println("caalign responce");
        BufferedReader rd = new BufferedReader(new InputStreamReader(
                conn.getInputStream()));
        response = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            response.append(line);
        }
        rd.close();
        System.out.println("closinng conneciotn" + conn.getResponseCode());
        conn.disconnect();
    } catch (Exception e) {
        e.printStackTrace();

    }
    System.out.println("priting responce");
    System.out.println(response.toString() + "~~~~~~~~~~~~~~~~~~~~~~~");
    return response.toString();
}

	
}
