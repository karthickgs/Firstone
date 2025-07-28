package keyword_driven_model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.testng.IClass;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.annotations.Test;

public class TestRunner  extends TestResult{

	@Test
	public void run() throws IOException, InterruptedException  {

		DataObject_Definitions obj = new DataObject_Definitions();

		Runnable task = () -> {
			try {
				obj.launch_URL("baseURL");
				obj.enter_credentials("username", "password");
				obj.sel_division("division");
				obj.click_submit();
				obj.choose_product("screen_name");
				obj.add_newproposal();
				obj.sel_proposaltype("policy_type", "proposal_type");
				obj.sel_custcode("customer_code");
				obj.sel_inscode("first_name", "insured_code");
				obj.sel_locality("locality");
				obj.sel_social("socialorOthers");
				// obj.sel_doctype("document_type");
				obj.sel_exiscust();
				obj.sel_phypolicy();
				obj.enter_location("risk_location");
				obj.enter_businesstype("businesstype");
				obj.sel_sourcetype("source_type");
				obj.sel_executive("executive_name");
				obj.create_prop();
				obj.sel_upload_PANForm60("PanorForm60");
				obj.enter_remarks("testing_remarks");
				obj.sel_nature("work_nature");
				obj.sel_job("job");
				obj.sel_wage("wage_type");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		};
		task.run();
	}

	
	
}
