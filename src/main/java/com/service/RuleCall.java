package com.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

public class RuleCall {
	
	public static void main(String args[]) throws JSONException {
		RuleCall r=new RuleCall();
		String u="http://35.201.178.201:8082/portal/servlet/service/dDependencyDemo";
		String jr="{\"Email\":\"doctiger8@gmail.com\",\"EventId\":\"4\",\"EventName\":\"Event112\",\"SFObject\":\"Account\",\"Primery_key\":\"Id\",\"Primery_key_value\":\"0016F00002Pn55b\"}";
		JSONObject js=new JSONObject(jr);
		String s=r.callPostJSon(u, js);
		System.out.println("ssss == "+s);
		//{"Email":"doctiger8_gmail.com","EventId":"4","EventName":"Event112","SFObject":"Account","Primery_key":"Id","Primery_key_value":"0016F00002Pn55b"}
		
	}
	
	
	public String callPostJSon(String urlstr, JSONObject Obj) {
		StringBuffer response =null;
		int responseCode = 0;
		String urlParameters = "";
		try {

		URL url = new URL(urlstr);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");

		con.setRequestProperty("Content-Type", "application/json");


		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(Obj.toString());
		wr.flush();
		wr.close();

		responseCode = con.getResponseCode();

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
		response.append(inputLine);
		}
		in.close();

		System.out.println(response.toString());
		}
		catch (Exception e) {
		return e.getMessage();
		}
		return response.toString();

		}	

}
