package com.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;

public class ReadHeader_Excel {

	public static void main(String[] args) {
		ReadHeader_Excel a = new ReadHeader_Excel();
		try {
			//String i = a.callget("http://35.186.166.22:8082/portal/content/MasterExcelNode/master1.xls/master1.xls");
			
			a.getJsondatabypk( "doctiger@xyz.com",  "http://bluealgo.com:8082/portal/content/services/freetrial/users/viki_gmail.com/DocTigerAdvanced/Excel/23/Doctiger invoicenew.xls/Doctiger invoicenew.xls");
InputStream ins =new FileInputStream(new File("C:\\Users\\user\\Downloads\\sampletest (1).xls"));
		//	a.getjsonReadExcel(ins);
			} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String callget(String urlStr) {
		String allheardes = "";
		urlStr = urlStr.replace(" ", "%20");
		URL url;
		InputStream ins = null;
		StringBuilder requestString = new StringBuilder(urlStr);

		try {
			url = new URL(requestString.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/text");
			System.out.println("1");
			ins = conn.getInputStream();
			allheardes = ReadExcel(ins);

			conn.disconnect();
		} catch (Exception e) {
			allheardes=e.getMessage();

		}
		// System.out.println("ins = "+ins);
		System.out.println("done");
		return allheardes;
	}

	public String ReadExcel(InputStream ins) {
		JSONObject json = new JSONObject();

		JSONArray array = new JSONArray();
		String js = "";
		try {

			// Create Workbook instance holding reference to .xlsx file
			HSSFWorkbook workbook = new HSSFWorkbook(ins);

			// Get first/desired sheet from the workbook
			HSSFSheet sheet = workbook.getSheetAt(0);
			System.out.println(sheet.getSheetName());
			int rowcount;
			// Iterate through each rows one by one
			System.out.println("last " + sheet.getLastRowNum());

			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				// For each row, iterate through all the columns
				Iterator<Cell> cellIterator = row.cellIterator();

				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();

					switch (cell.getCellType()) {

					case Cell.CELL_TYPE_STRING:
						// System.out.println(cell.getStringCellValue());
						array.put(cell.getStringCellValue());
						json.put("Headers", array);

						break;
					case Cell.CELL_TYPE_NUMERIC:
						// System.out.println(cell.getNumericCellValue());
						array.put(Double.toString(cell.getNumericCellValue()));
						json.put("Headers", array);
						break;
					}

				}
				break;

			}
			js = json.toString();
			System.out.print("js = " + js);

			ins.close();
		} catch (Exception e) {
			js=e.getMessage();
		}
		return js;
	}

	
	public JSONArray getJsondatabypk(String email, String Filepath) {
		JSONArray hedarr=null;
		
		JSONArray res = null;
		String urlStr = Filepath.replace(" ", "%20");
		URL url;
		InputStream ins = null;
		StringBuilder requestString = new StringBuilder(urlStr);

		try {
			url = new URL(requestString.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/text");
			System.out.println("1");
			ins = conn.getInputStream();
			System.out.println("1");
			res = getjsonReadExcel(ins);
		}catch(Exception e) {
			e.getMessage();
		}
		return res;
		
	}
	public JSONArray getjsonReadExcel(InputStream ins) {
		JSONObject json = new JSONObject();
JSONObject sfdataobj = new JSONObject();
JSONArray responsearray = new JSONArray();
JSONObject subobj = new JSONObject();
		JSONArray header = new JSONArray();
		String js = "";
		try {

			// Create Workbook instance holding reference to .xlsx file
			HSSFWorkbook workbook = new HSSFWorkbook(ins);

			// Get first/desired sheet from the workbook
			HSSFSheet sheet = workbook.getSheetAt(0);
			System.out.println(sheet.getSheetName());
			int rowcount;
			// Iterate through each rows one by one
			System.out.println("last " + sheet.getLastRowNum());

			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				System.out.println("row count"+  row.getRowNum());
				if(row.getRowNum()==0) {
				// For each row, iterate through all the columns
				Iterator<Cell> cellIterator = row.cellIterator();

				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();

					switch (cell.getCellType()) {

					case Cell.CELL_TYPE_STRING:
						// System.out.println(cell.getStringCellValue());
						header.put(cell.getStringCellValue());
						json.put("Headers", header);

						break;
					case Cell.CELL_TYPE_NUMERIC:
						// System.out.println(cell.getNumericCellValue());
						header.put(Double.toString(cell.getNumericCellValue()));
						json.put("Headers", header);
						break;
					}

				}
				System.out.println("header "+header);
				}

			}
			responsearray.put(subobj);
			sfdataobj.put("response", responsearray);
			js = sfdataobj.toString();
			System.out.print("js = " + js);

			ins.close();
		} catch (Exception e) {
			e.printStackTrace();
			js=e.getMessage();
		}
		return header;
	}
}
