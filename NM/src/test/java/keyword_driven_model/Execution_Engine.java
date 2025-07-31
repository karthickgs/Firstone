package keyword_driven_model;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class Execution_Engine {

	public DataObject_Definitions getallmethods = new DataObject_Definitions();
	String celldata = "";

	public Method[] method;

	public void allmethod() {

		method = getallmethods.getClass().getMethods();

	}

	public void getmethods() throws IOException, IllegalAccessException, InvocationTargetException {

		Excel_utility.actionKeywordExcelReader(Constant_loader.constants("spath"),
				Constant_loader.constants("sheetname"));
		for (int row = 0; row < 32; row++) {
			celldata = Excel_utility.getcelldata(row, 4);
			execute_actions();

		}
	}
	
	public void execute_actions() throws IllegalAccessException, InvocationTargetException {
		for(int i=0;i<method.length;i++) {
			if(method[i].getName().equalsIgnoreCase(celldata)) {
				method[i].invoke(getallmethods);
			}
		}
	}

}
