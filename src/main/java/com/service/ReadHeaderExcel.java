package com.service;

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

public class ReadHeaderExcel {

	public static void main(String[] args) {
		ReadHeaderExcel a = new ReadHeaderExcel();
		try {
			String i = a.callget("http://35.236.154.164:8082/portal/content/user/viki_gmail.com/DocTigerAdvanced/Excel/0/test_2.xls/test_2.xls");
		String g="http://bluealgo.com:8082/portal/content/services/freetrial/users/viki_gmail.com/DocTigerAdvanced/Excel/20/Doctiger sales receipt.xls/Doctiger sales receipt.xls";	
		String t="http://bluealgo.com:8082/portal/content/services/freetrial/users/viki_gmail.com/DocTigerAdvanced/Excel/9/testinvoice.xls/testinvoice.xls";
		a.getJsondatabypk( "doctiger@xyz.com", g,  "id",  "1");
//			a.getJsondatabypk( "doctiger@xyz.com",  "http://35.236.154.164:8082/portal/content/user/doctiger_xyz.com/DocTigerAdvanced/Excel/0/demo.xls/demo.xls",  "id",  "2");
System.out.println(g);
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
					try {
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
					}catch (Exception e) {
						// TODO: handle exception
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

	
	public String getJsondatabypk(String email, String Filepath, String primarykey, String primarykeyvalue) {
		JSONArray hedarr=null;
		
		String res = "";
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
			res = getjsonReadExcel(ins, primarykeyvalue);
			System.out.println("rest= "+res);
		
			

		}catch(Exception e) {
			e.getMessage();
		}
		
		
		
		return res;
		
	}
	public String getjsonReadExcel(InputStream ins, String value) {
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
					try {
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
					}catch (Exception e) {
						// TODO: handle exception
					}

				}
				System.out.println("header "+header);
				}else {
					String pk="";
					Cell cell = row.getCell(0);
					try {			
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						// System.out.println(cell.getStringCellValue());
						pk=cell.getStringCellValue();

						break;
					case Cell.CELL_TYPE_NUMERIC:
						// System.out.println(cell.getNumericCellValue());
						pk=Integer.toString((int) cell.getNumericCellValue());
						break;
					}
					}catch (Exception e) {
						// TODO: handle exception
					}
						System.out.println("pk"+ pk);
					if(pk.equalsIgnoreCase(value)) {
						for(int i=0; i<header.length(); i++) {
							// write a code to get json data
							
							Cell cell1 = row.getCell(i);
							try {
							switch (cell1.getCellType()) {
							case Cell.CELL_TYPE_STRING:
								// System.out.println(cell1.getStringCellValue());
								subobj.put(header.getString(i), cell1.getStringCellValue());
								break;
							case Cell.CELL_TYPE_NUMERIC:
								// System.out.println(cell1.getNumericCellValue());
								subobj.put(header.getString(i), Integer.toString((int) cell1.getNumericCellValue()));
								break;
							}
							}catch (Exception e) {
								// TODO: handle exception
							}
							
							
						}
						break;
						}else {}
					
					
					
				}

			}
			responsearray.put(subobj);
			sfdataobj.put("response", responsearray);
			js = sfdataobj.toString();
			System.out.print("js = " + js);

			ins.close();
		} catch (Exception e) {
			js=e.getMessage();
		}
		return js.toString();
	}
}
