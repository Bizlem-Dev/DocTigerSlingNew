package com.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;



public class SOAPCall {
	public static void main(String [] args) {
//		//String a="TemplateName=Template1&filename=Template1.docx&jsonstring={\"sdvsv\":\"vsdsv\"}";
//		JSONObject obj = new JSONObject();
		try {
			String sts="{\"to\":[\"tejal.jabade@bizlem.com\"],\"fromId\":\"doctigertest@gmail.com\",\"fromPass\":\"doctiger@123\",\"subject\":\"Testing125 Send Mail From MailTemlate\",\"body\":\r\n" + 
					"\"<p>Hello  Tejal ,<\\/p>\\n\\n<p>How are you?<\\/p>\\n\\n <p><strong>This is test mail sent from DocTiger.<\\/strong><\\/p>\\n\\n <p><u>hiiiiiiiiii<\\/u<\\/p>\\n\\n <p> 1 <\\/p>\\n\\n <p>Thanks<\\/p>\\n\\n<p>&nbsp;<\\/p>\\n\",\"cc\":[ \"anagha.rane@bizlem.com\"],\"bcc\":[\"tejal.jabade@bizlem.com\"],\"attachments\":[],\"attachmentPath\":\"\"}";
			JSONObject sendobj2 = new JSONObject(sts);
			JSONObject sendobj = new JSONObject();
			sendobj.put("to", "tejal.jabade@bizlem.com");
			sendobj.put("fromId", "doctigertest@gmail.com");
			sendobj.put("fromPass", "doctiger@123");
			sendobj.put("subject", "subject");
			String bd="<p>Dear $$Name$$</p>\r\n<p>This is test mail. \r\n </p><p>Thanks,</p><p> </p>";
			sendobj.put("body", bd);
		
		
			System.out.println("sendobj  " + sendobj2);
			int st=			new SOAPCall().callPostJSonModified("http://35.243.163.58:8080/NewMail/getFileAttachServlet", sendobj2);
			System.out.println("mail Status "+st);
		
//			obj.put("noOfProperties", "2");
//		
//		obj.put("project_name", "Doctiger");
//		//new SOAPCall().callPostJSon(" http://35.236.213.87:8080/drools/callrules/doctiger8@gmail.com_Doctiger_RuleEngine_Doctiger/fire", obj);
//	String a="{\"<<LastModifiedDate>>\":\"2018-07-19T12:39:10.000+0000\",\"<<Ownership>>\":\"Public\",\"<<Description>>\":\"Edge, founded in 1998, is a start-up based in Austin, TX. The company designs and manufactures a device to convert music from one digital format to another. Edge sells its product through retailers and its own website.\",\"<<BillingCity>>\":\"Austin\",\"<<Rating>>\":\"Hot\",\"<<Website>>\":\"http://edgecomm.com\",\"<<LastReferencedDate>>\":\"2018-07-28T12:57:30.000+0000\",\"<<NumberOfEmployees>>\":\"1000\",\"<<SLA__c>>\":\"Silver\",\"<<Name>>\":\"Edge Communications\",\"<<BillingAddress>>\":\"{\\\"country\\\":null,\\\"city\\\":\\\"Austin\\\",\\\"street\\\":\\\"312 Constitution Place\\\\\\\\nAustin, TX 78767\\\\\\\\nUSA\\\",\\\"latitude\\\":null,\\\"postalCode\\\":null,\\\"geocodeAccuracy\\\":null,\\\"state\\\":\\\"TX\\\",\\\"longitude\\\":null}\",\"<<Industry>>\":\"Electronics\",\"<<OwnerId>>\":\"0056F00000999WQQAY\",\"<<CreatedById>>\":\"0056F00000999WQQAY\",\"<<SLASerialNumber__c>>\":\"2657\",\"<<Phone>>\":\"(512) 757-6000\",\"<<TickerSymbol>>\":\"EDGE\",\"<<NumberofLocations__c>>\":\"2\",\"<<UpsellOpportunity__c>>\":\"Maybe\",\"<<PhotoUrl>>\":\"/services/images/photo/0016F00002Pn55bQAB\",\"<<IsDeleted>>\":\"false\",\"<<LastViewedDate>>\":\"2018-07-28T12:57:30.000+0000\",\"<<Sic>>\":\"6576\",\"<<ShippingStreet>>\":\"312 Constitution Place\\\\nAustin, TX 78767\\\\nUSA\",\"<<AccountNumber>>\":\"CD451796\",\"<<SystemModstamp>>\":\"2018-07-19T12:39:10.000+0000\",\"<<Type>>\":\"Customer - Direct\",\"<<CleanStatus>>\":\"Pending\",\"<<SLAExpirationDate__c>>\":\"2018-08-13\",\"<<BillingStreet>>\":\"312 Constitution Place\\\\nAustin, TX 78767\\\\nUSA\",\"<<ShippingAddress>>\":\"{\\\"country\\\":null,\\\"city\\\":null,\\\"street\\\":\\\"312 Constitution Place\\\\\\\\nAustin, TX 78767\\\\\\\\nUSA\\\",\\\"latitude\\\":null,\\\"postalCode\\\":null,\\\"geocodeAccuracy\\\":null,\\\"state\\\":null,\\\"longitude\\\":null}\",\"<<CreatedDate>>\":\"2018-07-19T12:39:10.000+0000\",\"<<CustomerPriority__c>>\":\"Medium\",\"<<attributes>>\":\"{\\\"type\\\":\\\"Account\\\",\\\"url\\\":\\\"/services/data/v43.0/sobjects/Account/0016F00002Pn55bQAB\\\"}\",\"<<Id>>\":\"0016F00002Pn55bQAB\",\"<<Fax>>\":\"(512) 757-9000\",\"<<LastModifiedById>>\":\"0056F00000999WQQAY\",\"<<BillingState>>\":\"TX\",\"<<AnnualRevenue>>\":\"139000000\",\"<<Active__c>>\":\"Yes\",\"[[CLASUE1]]\":\"In order to improve the predictability for importers, the shipping clause could be replaced by a contract clause CD451796. This would also exclude the risk of stockpiling http://edgecomm.com. A contract clause implies that if importers provide Electronics evidence that the order was placed before the duties were imposed (512) 757-6000, either in the form of a contract or payment Edge Communications, they will be exempt from the measures on this shipment Hot. Pending Another option would be to adjust the number of days on a case-by-case basis(512) 757-9000, by establishing the time span between the announcement of duties and their implementation,Customer - Direct  taking into account the actual shipping time to EU ports from the country being 0056F00000999WQQAY targeted in the investigation. x The shipping  clause should enter into 0056F00000999WQQAY Public {\\\"country\\\":null,\\\"city\\\":\\\"Austin\\\",\\\"street\\\":\\\"312 Constitution Place\\\\nAustin, TX 78767\\\\nUSA\\\",\\\"latitude\\\":null,\\\"postalCode\\\":null,\\\"geocodeAccuracy\\\":null,\\\"state\\\":\\\"TX\\\",\\\"longitude\\\":null} force after the publication of the measures in the EU Official Journal, in order to allow all importers to Edge Communications  benefit from the shipping clause and not only those that have made themselves known to the Commission at the initiation of the investigation. \",\"[[CLAUSE2]]\":\" As per above information  (512) 757-6000  you are eligible the rules given below(A) Risk must be discussed and documented. Edge Communications A risk analysis is warranted to demonstrate that the Government is not substantially increasing its risk. For example, when furnishing Government property, 0056F00000999WQQAY the Government is ordinarily responsible for suitability of use, timely delivery, and replacement of defective Government property According to the EU Regulation,CD451796 the EU may defence duties from anytime (512) 757-6000 between 60 days and 15 months from the official announcement date 0056F00000999WQQAY of a trade defence 0056F00000999WQQAY\",\"[[The Sale]]\":\" The Seller sells to the Purchaser, who hereby purchases, the Property on the terms and conditions of this Agreement. \",\"[[Parking]]\":\" The Seller grants to the Purchaser an exclusive license to the parking space(s) located in the parking structure of the Project as described at section 8 of the Particulars, if applicable.  \",\"[[Purchase Price & Payment]]\":\" The Purchase Price shall be paid by the Purchaser in accordance with instructions from the Seller, free of exchange, credit card collection or any other bank charges and without any deduction or set off in accordance with the Payment Plan. Notwithstanding the Payment Plan, the Purchaser agrees that the Seller has the right to claim payment of the Purchase Price up to a level commensurate with the percentage of completion of the Project, as confirmed by a technical report issued by RERA or its appointed consultant on the basis of a survey of the Project from time to time. Without prejudice to the Seller\\u2019s other rights pursuant to this Agreement including those contained at clause 12.2, in the event of the non payment on the due date of any part of the Purchase Price, Service Charges or Community Charges (if payable to the Seller), the Purchaser shall pay a penalty for the delay in making payment at the rate of ( 1% ) per month calculated on a simple interest basis on the 1st day of every month on all overdue payments of the Purchase Price, Service Charges or Community Charges (if payable to the Seller) from the day they become due until the date payment is made. Each payment made by the Purchaser shall be allocated first to the discharge of any amounts due by the Purchaser as a requirement of any Relevant Authority, including the Land Department, then to the Purchase Price, then to any penalties payable pursuant to clause 3.2, and thereafter to the Service Charges and the Community Charges. \",\"[[Possession and Risk]]\":\" The Anticipated Completion Date represents the date which at the date of this Agreement it is forecast that the Completion Date will occur. Without prejudice to the Seller\\u2019s rights pursuant to clause 16, the Seller reserves the right to extend the Anticipated Completion Date by up to ( 12 months ) months. In addition, the Purchaser acknowledges and agrees that any delay in paying any part of the Purchase Price may, upon notice from the Seller, result in a further delay to the Completion Date. The Seller shall serve the Purchaser the Handover Notice which shall determine the Completion Date, and the Completion Date shall be determined solely as stated in the Handover Notice. All risk and benefit (subject to clause 4.3) in respect of the Property shall pass to the Purchaser on the Completion Date.  Subject to clause 4.4, possession and occupation of the Unit will be given to the Purchaser on the later of (i) 30 days after the date on which the Handover Notice is served or (ii) payment of all of the Purchase Price, all Service Charges, all Community Charges, any amounts due to any Relevant Authority including the Land Department and any penalties or other charges due by the Purchaser or (iii) the Purchaser signs and returns the Handover Documents.  The Seller will be entitled to refuse to hand over possession and occupation of the Unit to the Purchaser if the Purchaser has not paid the full Purchase Price or has failed to comply with any other obligation hereunder. In the event that the Purchaser refuses to take possession of the Unit when requested by the Seller, the Purchaser shall pay a penalty for the delay in taking possession of the Unit at the rate of two percent ( 2% ) of the Purchase Price per month from the Completion Date until the Purchaser takes possession of the Unit. The Purchaser acknowledges that on the Completion Date and thereafter, the Residential Common Property, the Common Elements, the Common Use Facilities, Utilities, roads and pathways, other units in the Project or the Master Community or facilities in the adjacent areas may be unavailable or incomplete and that inconvenience may be suffered as a result. The Purchaser acknowledges and agrees that it shall have no claim against the Seller and/or the Master Developer for any such inconvenience and Purchaser agrees not to refuse taking possession of the Property for such reason.  The Purchaser shall from the Completion Date indemnify and hold the Seller and its officers, employees, agents and contractors harmless against all claims, proceedings, costs, damages, expenses and losses arising out of any damage to property or injury or fatality caused to any person (whether directly or indirectly) through the defective or damaged condition of any part of the Property or any other structure constructed thereon and any fixtures, fittings or electrical wiring therein; the spread of fire or smoke or the flow of water, wastewater or sewage from any part of the Property; or the act, default or neglect of the Purchaser or other occupiers of the Unit, his agents, representatives or contractors. \",\"[[Transfer of Title]]\":\" Provided that the Purchaser has fulfilled all of his payment and other obligations in terms of this Agreement, the Seller shall transfer clear and unencumbered freehold title to the Unit to the Purchaser at the Land Department as soon as is reasonably possible after the Completion Date, subject always to Applicable Laws. The Purchaser acknowledges that the Land Department may attribute an identification number for the Unit different from that stated at section 3 of the Particulars and agrees that the Seller shall have no liability in relation thereto. The Purchaser shall accept transfer of title to the Unit subject to such easements and restrictions benefiting or burdening the Property in terms of this Agreement, the Constitutional Documents or as imposed by the Master Developer or by any Relevant Authority and/or in accordance with the Applicable Laws. The Purchaser shall on demand sign the Handover Documents and all other requisite documentation and pay all costs for the registration of transfer of title to the Property into the name of the Purchaser in the Land Department. Without limitation, the Purchaser agrees to pay to the Seller or to the Land Department as directed by the Seller in respect of the registration of the Property registration fees levied by the Land Department on purchasers and sellers from time to time, which is currently a total of ( 4% ) of the Purchase Price; Emirates Real Estate Solutions fees or any increase or decrease in the percentage or amount so levied. The Purchaser further agrees and acknowledges that Purchaser is required by Applicable Laws to make payment of the amounts listed in this clause 5.4. If the Purchaser fails to make payment of such amounts, or any part thereof upon demand of the Seller, the Purchaser agrees that the Seller may allocate funds otherwise paid by the Purchaser here under to pay such registration charges, including any penalties levied by the Land Department, on behalf of the Purchaser whenever required. The Purchaser hereby confirms that it has fully read and understood this clause 5.4 and hereby authorises the Seller to allocate funds paid by the Purchaser as described herein.   The Purchaser acknowledges and agrees that the Land Department may impose a penalty of  ( 4% ) of the Purchase Price for the Purchaser\\u2019s failure to take all actions, provide all documents and pay all the Land Department registration fees within ( 60 days ) days of the date of this Agreement and that, in order to meet this deadline, the Seller requires to receive all such documents and all such registration fees from the Purchaser in full by no later than ( 35 days ) days after the date of this Agreement. \"}";	
//     JSONObject aa = new JSONObject(a);
//	//	System.out.println(a);
//	//String a="{\"<<LastModifiedDate>>\":\"2018-07-19T12:39:10.000+0000\",\"<<Ownership>>\":\"Public\",\"<<Description>>\":\"Edge, founded in 1998, is a start-up based in Austin, TX. The company designs and manufactures a device to convert music from one digital format to another. Edge sells its product through retailers and its own website.\",\"<<BillingCity>>\":\"Austin\",\"<<Rating>>\":\"Hot\",\"<<Website>>\":\"http://edgecomm.com\",\"<<LastReferencedDate>>\":\"2018-07-28T12:57:30.000+0000\",\"<<NumberOfEmployees>>\":\"1000\",\"<<SLA__c>>\":\"Silver\",\"<<Name>>\":\"Edge Communications\",\"<<BillingAddress>>\":\"{\\\"country\\\":null,\\\"city\\\":\\\"Austin\\\",\\\"street\\\":\\\"312 Constitution Place\\\\\\\\nAustin, TX 78767\\\\\\\\nUSA\\\",\\\"latitude\\\":null,\\\"postalCode\\\":null,\\\"geocodeAccuracy\\\":null,\\\"state\\\":\\\"TX\\\",\\\"longitude\\\":null}\",\"<<Industry>>\":\"Electronics\",\"<<OwnerId>>\":\"0056F00000999WQQAY\",\"<<CreatedById>>\":\"0056F00000999WQQAY\",\"<<SLASerialNumber__c>>\":\"2657\",\"<<Phone>>\":\"(512) 757-6000\",\"<<TickerSymbol>>\":\"EDGE\",\"<<NumberofLocations__c>>\":\"2\",\"<<UpsellOpportunity__c>>\":\"Maybe\",\"<<PhotoUrl>>\":\"/services/images/photo/0016F00002Pn55bQAB\",\"<<IsDeleted>>\":\"false\",\"<<LastViewedDate>>\":\"2018-07-28T12:57:30.000+0000\",\"<<Sic>>\":\"6576\",\"<<ShippingStreet>>\":\"312 Constitution Place\\\\nAustin, TX 78767\\\\nUSA\",\"<<AccountNumber>>\":\"CD451796\",\"<<SystemModstamp>>\":\"2018-07-19T12:39:10.000+0000\",\"<<Type>>\":\"Customer - Direct\",\"<<CleanStatus>>\":\"Pending\",\"<<SLAExpirationDate__c>>\":\"2018-08-13\",\"<<BillingStreet>>\":\"312 Constitution Place\\\\nAustin, TX 78767\\\\nUSA\",\"<<ShippingAddress>>\":\"{\\\"country\\\":null,\\\"city\\\":null,\\\"street\\\":\\\"312 Constitution Place\\\\\\\\nAustin, TX 78767\\\\\\\\nUSA\\\",\\\"latitude\\\":null,\\\"postalCode\\\":null,\\\"geocodeAccuracy\\\":null,\\\"state\\\":null,\\\"longitude\\\":null}\",\"<<CreatedDate>>\":\"2018-07-19T12:39:10.000+0000\",\"<<CustomerPriority__c>>\":\"Medium\",\"<<attributes>>\":\"{\\\"type\\\":\\\"Account\\\",\\\"url\\\":\\\"/services/data/v43.0/sobjects/Account/0016F00002Pn55bQAB\\\"}\",\"<<Id>>\":\"0016F00002Pn55bQAB\",\"<<Fax>>\":\"(512) 757-9000\",\"<<LastModifiedById>>\":\"0056F00000999WQQAY\",\"<<BillingState>>\":\"TX\",\"<<AnnualRevenue>>\":\"139000000\",\"<<Active__c>>\":\"Yes\",\"[[CLASUE1]]\":\"In order to improve the predictability for importers, the shipping clause could be replaced by a contract clause CD451796. This would also exclude the risk of stockpiling http://edgecomm.com. A contract clause implies that if importers provide Electronics evidence that the order was placed before the duties were imposed (512) 757-6000, either in the form of a contract or payment Edge Communications, they will be exempt from the measures on this shipment Hot. Pending Another option would be to adjust the number of days on a case-by-case basis(512) 757-9000, by establishing the time span between the announcement of duties and their implementation,Customer - Direct  taking into account the actual shipping time to EU ports from the country being 0056F00000999WQQAY targeted in the investigation. x The shipping  clause should enter into 0056F00000999WQQAY Public {\\\"country\\\":null,\\\"city\\\":\\\"Austin\\\",\\\"street\\\":\\\"312 Constitution Place\\\\nAustin, TX 78767\\\\nUSA\\\",\\\"latitude\\\":null,\\\"postalCode\\\":null,\\\"geocodeAccuracy\\\":null,\\\"state\\\":\\\"TX\\\",\\\"longitude\\\":null} force after the publication of the measures in the EU Official Journal, in order to allow all importers to Edge Communications  benefit from the shipping clause and not only those that have made themselves known to the Commission at the initiation of the investigation. \",\"[[CLAUSE2]]\":\" As per above information  (512) 757-6000  you are eligible the rules given below(A) Risk must be discussed and documented. Edge Communications A risk analysis is warranted to demonstrate that the Government is not substantially increasing its risk. For example, when furnishing Government property, 0056F00000999WQQAY the Government is ordinarily responsible for suitability of use, timely delivery, and replacement of defective Government property According to the EU Regulation,CD451796 the EU may defence duties from anytime (512) 757-6000 between 60 days and 15 months from the official announcement date 0056F00000999WQQAY of a trade defence 0056F00000999WQQAY\",\"[[The Sale]]\":\" The Seller sells to the Purchaser, who hereby purchases, the Property on the terms and conditions of this Agreement. \",\"[[Parking]]\":\" The Seller grants to the Purchaser an exclusive license to the parking space(s) located in the parking structure of the Project as described at section 8 of the Particulars, if applicable\"}";	
//       //	URLEncoder.encode( a, "UTF-8" );
//		
//		String wsinput="TemplateName=temp112&filename=temp112.docx&jsonstring="+a;
//         
//		//wsinput=URLEncoder.encode( wsinput, "UTF-8" );
//		  JsonGetterSetter objJsonGetterSetter = new JsonGetterSetter();
//		  objJsonGetterSetter.setFileName("temp112.docx");
//		  objJsonGetterSetter.setTemplateName("temp112");
//		  objJsonGetterSetter.setJsonString(a);
//		//String templatename_url = new SOAPCall().callPostJSon("http://35.188.233.86:8080/DocTigerSF/services/SFDCDocumentGeneration/DocGeneration", aa);
//		String templatename_url = new SOAPCall().callGetWithAuth("http://35.188.243.203:8080/kie-server/services/rest/server/queries/tasks/instances/pot-owners?status=Ready&status=Reserved&status=InProgress&groups=&page=0&pageSize=10&sortOrder=true", "hemant","hemant");
//        System.out.println("templatename_urlc  "+templatename_url);
System.out.println("start");
			
			String a= "{\r\n" + 
		"\"username\": \"viki1237675@gmail.com\",\r\n" + 
		"\"roleid\": 1,\r\n" + 
		"\"isactive\": 0\r\n" + 
		"}";
			JSONObject obj= new JSONObject(a);
//		int b= 	new SOAPCall().callPostJSonModified("http://35.221.160.146:8082/apirest/usrmgmt/user/", obj);
//			
//		System.out.println("b "+b);	
//			
			
			
			
	//	new SOAPCall().sendGet("") ;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

       public  String sendGet(String url) throws Exception {
             String userresult="";
	//String url = "http://35.221.160.146:8180/UserValidation/services/UserValidation/raveUserExistence?userId=doctiger100@gmail.com";
	try {
	URL obj = new URL(url);
	HttpURLConnection con = (HttpURLConnection) obj.openConnection();

	// optional default is GET
	con.setRequestMethod("GET");
	//con.setRequestProperty("Content-Type", "application/xml;charset=UTF-8");

	int responseCode = con.getResponseCode();
	System.out.println("\nSending 'GET' request to URL : " + url);
	System.out.println("Response Code : " + responseCode);

	 InputStream inputXml= con.getInputStream();

     DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
     DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
     Document doc = dBuilder.parse(inputXml);
     doc.getDocumentElement().normalize();
     NodeList nList1 = doc.getElementsByTagName("ns:raveUserExistenceResponse");
     org.w3c.dom.Node nNode = nList1.item(0);
     org.w3c.dom.Element eElement = (org.w3c.dom.Element) nNode;
      userresult = eElement.getElementsByTagName("ns:return").item(0).getTextContent();

     System.out.println("userresult  "+userresult);
return userresult;
	}catch(Exception e) {
		//e.printStackTrace();
		return e.getMessage();
	}
}

       public int callPostJSonModified(String urlstr, JSONObject Obj) {
    		
   		StringBuffer response =null;
   		int responseCode = 0;
   		String urlParameters = "";
   		try {

   		URL url = new URL(urlstr);
   		HttpURLConnection con = (HttpURLConnection) url.openConnection();
   		con.setRequestMethod("POST");

   		con.setRequestProperty("Content-Type", "application/json;");
   		//		+ "charset=UTF-8");
   	//	con.setRequestProperty("Accept-Charset", "UTF-8");

   		con.setDoOutput(true);
   		 DataOutputStream wr = new DataOutputStream(con.getOutputStream());
   		 wr.writeBytes(Obj.toString());
   		 wr.flush();
   		 wr.close();
         System.out.println("Obj "+Obj);
         //   		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
         //   		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr));
         //   		writer.write(Obj.toString());
         //   		writer.close();
         //   		wr.close();

   		responseCode = con.getResponseCode();
       System.out.println("responseCode "+responseCode);
   		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
   		String inputLine;
   		response = new StringBuffer();

   		while ((inputLine = in.readLine()) != null) {
   		response.append(inputLine);
   		}
   		in.close();

   		System.out.println(response.toString());
   		
   		
   		System.out.println("end");

   		}catch (Exception e) {
   		//return e.getMessage();
   		}
   		return responseCode;

   		}

       public String callAttachmentJSon(String urlstr, JSONObject Obj) {
   		
      		StringBuffer response =null;
      		int responseCode = 0;
      		String urlParameters = "";
      		String resp=null;
      		try {

      		URL url = new URL(urlstr);
      		HttpURLConnection con = (HttpURLConnection) url.openConnection();
      		con.setRequestMethod("POST");

      		con.setRequestProperty("Content-Type", "application/json;");
      		//		+ "charset=UTF-8");
      	//	con.setRequestProperty("Accept-Charset", "UTF-8");

      		con.setDoOutput(true);
      		 DataOutputStream wr = new DataOutputStream(con.getOutputStream());
      		 wr.writeBytes(Obj.toString());
      		 wr.flush();
      		 wr.close();
            System.out.println("Obj "+Obj);
            //   		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            //   		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr));
            //   		writer.write(Obj.toString());
            //   		writer.close();
            //   		wr.close();

      		responseCode = con.getResponseCode();
          System.out.println("responseCode "+responseCode);
          
      		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
      		String inputLine;
      		response = new StringBuffer();

      		while ((inputLine = in.readLine()) != null) {
      		response.append(inputLine);
      		}
      		in.close();

      		System.out.println(response.toString());
      		
      		resp=response.toString();
      		System.out.println("end");

      		}catch (Exception e) {
      		//return e.getMessage();
      			resp=e.getMessage();
      		}
      		return resp;

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
		   con.setRequestProperty("Accept-Charset", "UTF-8");
		  
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
		return   e.getMessage();
		  }
		  return response.toString();

		 }


}
