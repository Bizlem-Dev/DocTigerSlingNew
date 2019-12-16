package com.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.sling.api.SlingHttpServletResponse;
//import org.apache.poi.xwpf.usermodel.XWPFDocument;
//import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
//http://35.188.233.86:8080/DocTigerSFDCAcessToken/CallSFDCAPI?sfobject=Account&primerykey=Id&primerykeyvalue=0016F00002Pn55b
import org.jsoup.Jsoup;

public class Replacedata {

	public String callFile(String urlStr) {
		String data = "";
		urlStr = urlStr.replace(" ", "%20");
		URL url;
		InputStream ins = null;
		StringBuilder requestString = new StringBuilder(urlStr);

		try {
			url = new URL(requestString.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/text");
			ins = conn.getInputStream();
			 data = getStringFromInputStream(ins);


			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();

		}
		return data;
	}
	public JSONObject ReplacMailbody(JSONObject  objmail, String SFres, SlingHttpServletResponse res) {
		PrintWriter out=null;
String body= "";
	String to="";
	String from="";
	String subject="";
	String attachurl="";
	JSONObject resp= null;
 
	new JSONObject();
		try {
			out=res.getWriter();
			resp=new JSONObject();
			to=objmail.getString("to");
					from=objmail.getString("from");
					subject=objmail.getString("subject");
			body= objmail.getString("body");
			if(objmail.has("attachurl") && (!objmail.getString("attachurl").equals(""))) {
				attachurl=objmail.getString("attachurl");
				resp.put("attachurl", attachurl);

			}
			//$$name$$
	to = to.replace("$$", " $$ ").trim();		
 body= body.replace("$$", " $$ ");
 
// out.println("to.replace(\"$$\", \" $$ \")"+ to );
			HashMap<String, String> hmap = new HashMap<String, String>();

		//	String sfurl="http://35.188.233.86:8080/DocTigerSFDCAcessToken/CallSFDCAPI?sfobject=Account&primerykey=Id&primerykeyvalue=0016F00002Pn55b";
		//	String ressf =callSFAPI(sfurl);
			JSONObject jsf = new JSONObject(SFres);
			System.out.println("jsf  "+jsf);
			if (jsf.has("response")) {
				JSONArray jsar = jsf.getJSONArray("response");
				for (int i = 0; i < jsar.length(); i++) {
					JSONObject jObject = jsar.getJSONObject(i);
					
			
					//resp.put("to", to);
//					if(jObject.has("Mobile__c")) {
//						resp.put("MobileNo", jObject.get("Mobile__c"));
//					}else{
//						resp.put("MobileNo", "+917845645354");
//
//					}
					
					
					// jObject.put("phonetype", "phnWWW888");
					Iterator<?> keys = jObject.keys();

					while (keys.hasNext()) {
						String key = (String) keys.next();
						String value = jObject.getString(key);
						hmap.put("$$ "+key+" $$", value);

					}
					
			//	out.println("****************"+hmap.containsKey(to));
				if (hmap.containsKey(to)) {
				//	out.println("to "+to);					
					to = to.replace(to, hmap.get(to));
				}else{
					to="doctigerreceiver@gmail.com";
				}
				
				
				
				
					for (Map.Entry<String, String> entry : hmap.entrySet()) {

						String key =  entry.getKey().trim();
						String value = entry.getValue().toString();

						
						if (from.contains(key)) {
						//	out.println("from key :: "+key +" value :: "+value);
							from = from.replace(key, value);

						}
						if (subject.contains(key)) {
						//out.println("subject key :: "+key +" value :: "+value);
							subject = subject.replace(key, value);

						}
						if (body.contains(key)) {
						//	out.println("body key :: "+key +" value :: "+value);
							body = body.replace(key, value);

						}
					}
//                    body =   Jsoup.parse( body).text();

            		
                    
					resp.put("to", to);
					resp.put("from", from);
					resp.put("subject", subject);
					resp.put("body", body);




				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			out.println(e.getMessage());
		}
		return resp;
	}
	public JSONObject ReplacSMSbody(JSONObject  objmail, String SFres, SlingHttpServletResponse res) {
		PrintWriter out=null;
String body= "";
	String to="";
	String from="";
	String subject="";
	String attachurl="";
	JSONObject resp= null;
 
	new JSONObject();
		try {
			out=res.getWriter();
			resp=new JSONObject();
			to=objmail.getString("to");
					from=objmail.getString("from");
			body= objmail.getString("body");
			
			 to= to.replace("$$", " $$ ").trim();
			 body= body.replace("$$", " $$ ");
			// out.println("to##   "+to);
			HashMap<String, String> hmap = new HashMap<String, String>();

		//	String sfurl="http://35.188.233.86:8080/DocTigerSFDCAcessToken/CallSFDCAPI?sfobject=Account&primerykey=Id&primerykeyvalue=0016F00002Pn55b";
		//	String ressf =callSFAPI(sfurl);
			JSONObject jsf = new JSONObject(SFres);
			System.out.println("jsf  "+jsf);
			if (jsf.has("response")) {
				JSONArray jsar = jsf.getJSONArray("response");
				for (int i = 0; i < jsar.length(); i++) {
					JSONObject jObject = jsar.getJSONObject(i);
								
//					if(jObject.has("Mobile__c")) {
//						resp.put("MobileNo", jObject.get("Mobile__c"));
//					}else{
//						resp.put("MobileNo", "+917845645354");
//
//					}					
					// jObject.put("phonetype", "phnWWW888");
					Iterator<?> keys = jObject.keys();

					while (keys.hasNext()) {
						String key = (String) keys.next();
						String value = jObject.getString(key);
						hmap.put("$$ "+key+" $$", value);

					}
					
		//	out.println("****************"+hmap.containsKey(to));
					if (hmap.containsKey(to)) {
					//	out.println("to "+to);					
						to = to.replace(to, hmap.get(to));
					}else{
						to= "+917845645354";
						}
					
					
					for (Map.Entry<String, String> entry : hmap.entrySet()) {

						String key =  entry.getKey().trim();
						String value = entry.getValue().toString();
//						if (to.contains(key)) {
//							out.println("key :: "+key +" value :: "+value);
//							to = to.replace(key, value);
//
//						}else{
//							to= "+917845645354";
//							
//												}
//						
						if (from.contains(key)) {
						//	out.println("key :: "+key +" value :: "+value);
							from = from.replace(key, value);

						}
						if (body.contains(key)) {
						//	out.println("key :: "+key +" value :: "+value);
							body = body.replace(key, value);

						}
					}
                    body =   Jsoup.parse( body).text();

					resp.put("to", to);
					resp.put("from", from);
					resp.put("subject", subject);
					resp.put("body", body);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			out.println(e.getMessage());
		}
		return resp;
	}
	// convert InputStream to String

	public String Replacekey(String data, String SFresp) {

		String newdata = "";

		try {

			HashMap<String, String> hmap = new HashMap<String, String>();

			//String sfurl="http://35.188.233.86:8080/DocTigerSFDCAcessToken/CallSFDCAPI?sfobject=Account&primerykey=Id&primerykeyvalue=0016F00002Pn55b";
			//String ressf =callSFAPI(sfurl);
			JSONObject jsf = new JSONObject(SFresp);
			if (jsf.has("response")) {
				JSONArray jsar = jsf.getJSONArray("response");
				for (int i = 0; i < jsar.length(); i++) {
					JSONObject jObject = jsar.getJSONObject(i);
					// jObject.put("phonetype", "phnWWW888");
					Iterator<?> keys = jObject.keys();

					while (keys.hasNext()) {
						String key = (String) keys.next();

						String value = jObject.getString(key);
						hmap.put(key, value);

					}

					Map.Entry<String, String> entry1 = hmap.entrySet().iterator().next();
					String key1 = "<<" + entry1.getKey().trim() + ">>";

					String value1 = entry1.getValue();
					if (data.contains(key1)) {

						newdata = data.replaceAll(key1, value1);

					} else {
						newdata = data;
					}
					for (Map.Entry<String, String> entry : hmap.entrySet()) {

						String key = "<<" + entry.getKey().trim() + ">>";
						String value = entry.getValue().toString();
						// System.out.println("key : " + key + " Val=" + value);

						if (data.contains(key)) {

							newdata = newdata.replaceAll(key, value);

						}
					}
					// System.out.println("newdata ============== : " + newdata);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return newdata;
	}

	// convert InputStream to String
	private static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}

	public String callSFAPI(String urlStr) {
		String data = "";
		urlStr = urlStr.replace(" ", "%20");
		URL url;
		InputStream ins = null;
		StringBuilder requestString = new StringBuilder(urlStr);

		try {
			url = new URL(requestString.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/text");
			ins = conn.getInputStream();
			String data1 = getStringFromInputStream(ins);
			data = data1;

			conn.disconnect();
		} catch (Exception e) {
			return	e.getLocalizedMessage();

		}
		return data;
	}

	
	public String sendmail(String urlStr) {
		
		
		return urlStr;}
	
	
	
	public static void main(String[] args) {
		Replacedata a = new Replacedata();
		try {
	//a.ReplacMailbody("");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
