package com.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;

//import java.util.Base64;
import com.sun.jersey.core.util.Base64;

public class ActivateWorkflow {

	public static void main(String[] args) {
		/*String urlstr = "http://35.188.243.203:8080/kie-server/services/rest/server/containers/business-process_5.0/processes/ApprovalWorkflow/instances";
		JSONObject obj;
		try {
			obj = new JSONObject(
					"{\"requestType\":\"TEMPLATE\", \"approver1\":\"doctiger@xyz.com\",\"approver2\":\"\",\"approver3\":\"\",\"approver4\":\"\",\"approver5\":\"\",\"numberOfApprover\":1,\"approver1Group\":\"\",\"approver2Group\":\"\",\"approver3Group\":\"\",\"approver4Group\":\"\",\"approver5Group\":\"\"}");

			String username = "kieserver";
			String password = "kieserver1!";

			String a = new ActivateWorkflow().callPostJSon(urlstr, obj, username, password);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

	}

	public String callPostJSon(String urlstr, JSONObject Obj, String username, String password) {
		StringBuffer response = null;
		int responseCode = 0;
		String urlParameters = "";
		try {

			URL url = new URL(urlstr);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");

			// BASE64Encoder enc = new sun.misc.BASE64Encoder();
			String userpassword = username + ":" + password;
			// String encodedAuthorization = enc.encode( userpassword.getBytes() );
			byte[] encodedAuthorization = Base64.encode(userpassword);
			// byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
			String authStringEnc = new String(encodedAuthorization);

			con.setRequestProperty("Authorization", "Basic " + authStringEnc);
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
		} catch (Exception e) {
			return e.getMessage();
		}
		return response.toString();

	}

	public String callPostJSonMap(String urlstr, JSONArray Obj, String username, String password) {
		StringBuffer response = null;
		int responseCode = 0;
		String urlParameters = "";
		try {

			URL url = new URL(urlstr);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");

			// BASE64Encoder enc = new sun.misc.BASE64Encoder();
			String userpassword = username + ":" + password;
			// String encodedAuthorization = enc.encode( userpassword.getBytes() );
			byte[] encodedAuthorization = Base64.encode(userpassword);
			// byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
			String authStringEnc = new String(encodedAuthorization);

			con.setRequestProperty("Authorization", "Basic " + authStringEnc);
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
		} catch (Exception e) {
			return e.getMessage();
		}
		return response.toString();

	}

}
