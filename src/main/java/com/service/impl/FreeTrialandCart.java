package com.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.sling.commons.json.JSONObject;

public class FreeTrialandCart {
	ResourceBundle bundle = ResourceBundle.getBundle("config");
	static ResourceBundle bundleststic = ResourceBundle.getBundle("config");

	public static void main(String[] args) {
		
		new FreeTrialandCart().checkfreetrial("viki@gmail.com");
	//"mohit.raj@bizlem.com"	
	}
	
	public String checkfreetrial(String userid) {
		int expireFlag=0;
		String addr = bundleststic.getString("Freetrialurl")+userid+"/DocTigerFreeTrial";
		//String addr = "http://development.bluealgo.com:8086/apirest/trialmgmt/trialuser/"+userid+"/DocTigerFreeTrial";
		String username = "username";
		String password = "password";
		try{
		URL url = new URL(addr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");

		conn.connect();
		InputStream in = conn.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String text = reader.readLine();
		System.out.println(text);
JSONObject obj = new JSONObject(text);

 expireFlag=obj.getInt("expireFlag");
	System.out.println(expireFlag);

		conn.disconnect();
		}catch(Exception ex)
		{
		ex.printStackTrace();
		System.out.println("made it here");
		}
		//return expireFlag+"";
		return "1";

		}
}
