package com.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;

import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

public class CreateJsonFile {
	String res1 = "";

	public String addTemplateToJson(String temaplateName, String inputFormat, String outputFormat, String templatePath,
			String docLanguage) throws JSONException {
		JSONObject templateobj = null;
		JSONObject rootObject = null;
		// String content="{}";
		// JSONObject rootObject =new JSONObject(content);

		String r = SearchFile();
//		 File file = new File("/home/vil/sling tomcat/apache-tomcat-6.0.35/webapps/ROOT/TemplateLibrary/UploadedTemplates.json");
//		 System.out.println("ssdd "+file.length());
		if (r.equals("UploadedTemplates.json")) {
		
			rootObject = readTemplateFile("/home/ubuntu/apache-tomcat-8.5.31/webapps/ROOT/TemplateLibraryAdvanced/UploadedTemplates.json");
					//"/home/vil/sling tomcat/apache-tomcat-6.0.35/webapps/ROOT/TemplateLibraryAdvance/UploadedTemplates.json");

		} else {
			String content = "{}";
			rootObject = new JSONObject(content);
			
		}
		templateobj = new JSONObject();
		templateobj.put("temaplateName", temaplateName);
		templateobj.put("inputFormat", inputFormat);
		templateobj.put("outputFormat", outputFormat);
		templateobj.put("templatePath", templatePath);
		templateobj.put("docLanguage", docLanguage);

		rootObject.put(temaplateName, templateobj);
		res1 = "1";

		try {
			String st = "/home/ubuntu/apache-tomcat-8.5.31/webapps/ROOT/TemplateLibraryAdvanced/UploadedTemplates.json";

			FileWriter file = new FileWriter(st);
			file.write(rootObject.toString());
			res1 = res1 + "created";
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return res1;
	}

	public JSONObject readTemplateFile(String st) {
		String res1 = "";
		String content = "";
		JSONObject rootObject = null;
		try {

			File file = new File(st);

			byte[] bytesArray = new byte[(int) file.length()];

			FileInputStream fis = new FileInputStream(file);
			fis.read(bytesArray); // read file into bytes[]

			content = new String(bytesArray);
			res1 = res1 + "read file4";

			rootObject = new JSONObject(content);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return rootObject;

	}

	public String SearchFile() {
		String resfil = "";
		try {
			File dir = new File("/home/ubuntu/apache-tomcat-8.5.31/webapps/ROOT/TemplateLibraryAdvanced");//  /home/vil/sling tomcat/apache-tomcat-6.0.35/webapps/ROOT/TemplateLibraryAdvance");

			resfil = "1";
			FilenameFilter filter = new FilenameFilter() {

				public boolean accept(File dir, String name) {

					return name.startsWith("UploadedTemplates");
				}

			};

			resfil = "2";
			String[] children = dir.list(filter);
			resfil = "3";
			if (children == null) {

				resfil = "4";
			} else {
				for (int i = 0; i < children.length; i++) {
					String filename = children[i];
					resfil = filename;

				}
			}
		} catch (Exception e) {
			resfil = "exception";
		}

		return resfil;
	}

}
