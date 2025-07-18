package keyworddriven_marine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Marine_excelloader {

	public ArrayList addexcel(int colno) throws IOException {
		String file = Marine_utilityfile.filepath;
		File f1 = new File(file);
		FileInputStream fis = new FileInputStream(f1);
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet sh = wb.getSheet("sheet1");
		Iterator row = sh.rowIterator();
		ArrayList<String> a = new ArrayList();
		while (row.hasNext() == true) {
			Row r = (Row) row.next();
			Cell cell = r.getCell(colno);
			String data = cell.getStringCellValue();
			a.add(data);

		}
		System.out.println("List: " + a);

		// Return the data to the Arraylist method.

		return a;

	}

}
