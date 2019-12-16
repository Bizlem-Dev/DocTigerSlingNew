package com.service;

import java.io.IOException;
import java.util.HashMap;
import javax.jcr.Node;
import javax.jcr.Session;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.commons.json.JSONObject;

public interface  ParseSlingData   {
	public String getEventdata(String email, String group, String eventid, String eventname,  SlingHttpServletResponse response, Session session); 
	public JSONObject getTempSFObj(String email, String group,String tempname, SlingHttpServletResponse response ) ;

	public String getADVandTemplatedata(String email, String group, String templatename, String AttachtempalteType,String SFObject, String Primery_key, String Primery_key_value, JSONObject SFData, SlingHttpServletResponse response,Session session);
	public JSONObject getMailTemplatedata(String email, String group, String mailtemplatename, String SFObject, String Primery_key, String Primery_key_value,JSONObject SFData, SlingHttpServletResponse response,Session session);
	public String getFileReplaceData(String email, String group,String clauseid, String clausename,SlingHttpServletResponse response, String SFresp );
	public String getClauseByName(String searchText, String email, String group,Node DoctigerAdvnode, SlingHttpServletResponse response ) ;
	public String addmergefiled(Node clsdesc, String SFresp, SlingHttpServletResponse response ) ;
	public JSONObject getSMSTemplatedata(String email, String group, String SMStemplatename, String SFObject, String Primery_key, String Primery_key_value, JSONObject SFData, SlingHttpServletResponse response ) ;
	public String getFileReplaceData_new(String email,String group, String clauseid, String clausename,  SlingHttpServletResponse response, String SFresp , int count) ;
	public JSONObject getDocGenValidity(String email,SlingHttpServletRequest request, SlingHttpServletResponse response) ;

	public  HashMap<String, String> getCustomerDetails(String serviceId, String customerId, SlingHttpServletRequest request, SlingHttpServletResponse response) ;
	
	public Node getDocTigerAdvNode(String freetrialstatus, String email,String group, Session session, SlingHttpServletResponse response ) throws IOException;

	public Node getDocTigerAdvNode(String freetrialstatus, String email, Session session, SlingHttpServletResponse response ) throws IOException;
	public Node validationmethod_old(String freetrialstatus, String email,String group, Session session, SlingHttpServletRequest request, SlingHttpServletResponse response);
	public Node validationmethod(String freetrialstatus, String email, String group, Session session1,SlingHttpServletResponse response) ;
	public String updateDocGenCounter(String useremail,String freetrialstatus, Node doctigerAdvNode, Session session, SlingHttpServletResponse response) ;
	public Node getDocTigerAdvNode(String freetrialstatus, String email, String group, Session session1,
			SlingHttpServletResponse response, String lgtype);

}
