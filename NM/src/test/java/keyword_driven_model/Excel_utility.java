package keyword_driven_model;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import io.opentelemetry.exporter.logging.SystemOutLogRecordExporter;

public class Excel_utility {

	static XSSFWorkbook book;
	static XSSFSheet sheet;
	static XSSFCell cell;
	String getHeader;
	String spath = "C:\\Users\\k1027\\Documents\\Automation\\Framework_selenium\\WC_product_files\\Test_data.xlsx";
	String sheetname = "Sheet1";
	String ckey;
	
	public Excel_utility (String ckey) {
		this.ckey = ckey;
		
	}
	
	
	public static void actionKeywordExcelReader(String spath, String sheetname) throws IOException {

		FileInputStream file = new FileInputStream(spath);
		book = new XSSFWorkbook(file);
		sheet = book.getSheet(sheetname);

	}

	public static String getcelldata(int row, int col) {

		cell = sheet.getRow(row).getCell(col);
		String celldata = cell.getStringCellValue();
		return celldata;

	}

	public String keyword() throws IOException {
		FileInputStream file = new FileInputStream(spath);
		book = new XSSFWorkbook(file);
		sheet = book.getSheet(sheetname);
		int rowvalue = sheet.getPhysicalNumberOfRows();

		String result = "";
		Map<String, String> allrec1 = new HashMap<String, String>();

		for (int i = 0; i < rowvalue; i++) {

			XSSFRow row = sheet.getRow(i);
			int col1 = sheet.getRow(i).getLastCellNum();
			System.out.println(col1);

			if (row != null) {

				for (int j = 0; j < col1; j++) {

					if (row.getCell(j) != null) {
						allrec1.put(sheet.getRow(0).getCell(j).toString(), sheet.getRow(1).getCell(j).toString());

					}
				}
			}

			for (Map.Entry<String, String> entryfreq : allrec1.entrySet()) {

				if (ckey.equalsIgnoreCase(entryfreq.getKey())) {

					result = entryfreq.getValue();
				}
			}
		}
		return result;

	}

//	public static void main(String[] args) throws IOException {
//
//		
//		String print = Excel_utility.keyword(path, sheetname, getHeader);
//		System.out.println(print);
//
//	}

}
