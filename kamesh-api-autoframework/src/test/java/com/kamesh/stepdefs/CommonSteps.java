package com.kamesh.stepdefs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.output.WriterOutputStream;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Assert;

import com.jayway.jsonpath.JsonPath;
import com.kamesh.resourceManager.DataManager;
import com.kamesh.schema.employee;
import com.kamesh.utils.Common;
import com.kamesh.utils.Reusables;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class CommonSteps {

	public static RequestSpecification requestSpecs;
	public static Response res;
	public static Properties prop = new Properties();
	public static FileInputStream fis;
	public static Scenario step;
	public static Logger log = LogManager.getLogger(CommonSteps.class.getName());
	public static long currentTimestamp = System.currentTimeMillis();
	public static ThreadLocal<String> testCaseName = new ThreadLocal<String>();
	public static String resourceURL;
	URL temp = getClass().getClassLoader().getResource("config.properties");
	private StringWriter requestWriter;
	private PrintStream requestCapture;
	private StringWriter responseWriter;
	private PrintStream responseCapture;

	@Before
	public void beforeEveryScenario(Scenario s) throws Exception {
		CommonSteps.log.info(Common.SCENARIO_BEGINS);
		step = s;
		step.write(Common.SCENARIO_BEGINS);
		requestWriter = new StringWriter();
		requestCapture = new PrintStream(new WriterOutputStream(requestWriter), true);
		responseWriter = new StringWriter();
		responseCapture = new PrintStream(new WriterOutputStream(responseWriter), true);
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.urlEncodingEnabled = true;
		requestSpecs = RestAssured.given().filter(new RequestLoggingFilter(requestCapture))
				.filter(new ResponseLoggingFilter(responseCapture));
	}

	public static String getTestCaseName() {
		String s = step.getName();
		if (s == null)
			return "";
		if (s.contains("_"))
			return s.substring(s.indexOf("_") + 1);
		else
			return s.trim();

	}

	// Get config file loaded
	@Given("I want to initiate the config file")
	public void initiatePropertyFile() throws IOException {
		fis = new FileInputStream(temp.getFile());
		prop.load(fis);
	}

	@And("I perform GET operation {string}")
	public void invokeGETOperation(String resoourceName) {
		resourceURL = prop.getProperty(resoourceName);
//		RestAssured.baseURI= resourceURL;
		res = requestSpecs.when().get(resourceURL);
		log.info("Time taken to respond:" + res.getTimeIn(TimeUnit.MILLISECONDS));
		step.write("Time taken to respond:" + res.getTimeIn(TimeUnit.MILLISECONDS));

	}

	@And("I pass path parameters")
	public void passpathparameters(Map<String, String> pathparam) {
		Iterator<Entry<String, String>> it = pathparam.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> pair = it.next();
			requestSpecs = requestSpecs.pathParameter(pair.getKey(), DataManager.refreshData(pair.getValue()));
		}
		log.info("Request Specs on setting path parameters:"+requestSpecs.toString());
	}

	@Then("I should get {string} response")
	public void validateHttpStatusCode(String arg) {
		if (res != null) {
			CommonSteps.log.info(Common.RESPONSE_STATUSCODE + res.getStatusCode());
			step.write(Common.RESPONSE_STATUSCODE + res.getStatusCode());
			Assert.assertTrue("Response http statusCode is not 200", res.getStatusCode() == 200);
		}
	}

	@And("I should verify if profile_image is blank for all the employees")
	public void verifyProfileImageisEmptyforAll() throws Exception {
		employee.validateProfileImageForEmployee();
	}

	
	@And("I should validate employeeDetails response structure and values")
	public void validateEmployeeDetailsResponseStructure() throws Exception {
		employee.verifyEmployeeDetails();
	}
	
	@And("I should get the response with the following values")
	public static void verifyJsonResponseValues(Map<String,String> table) {
	String valueToBeCompared = "";
	String key = "";
	String bodyStringValue = res.getBody().asString();
	Iterator<Entry<String,String>> it = table.entrySet().iterator();
	while(it.hasNext()) {
		Map.Entry<String, String> pair = it.next();
		key = pair.getKey();
		Object value = JsonPath.parse(bodyStringValue).read("$."+key);
		valueToBeCompared = value.toString();
		Assert.assertTrue("Actual value :"+valueToBeCompared+" doesn't match with the expected", pair.getValue().equalsIgnoreCase(valueToBeCompared));
	}
	}
	
	
	
	
	
	@And("use data from excel {string} using sheet {string}")
	public void initExcelData(String strExcelName, String strSheetName) throws Exception {
		DataManager.initExcelData(strExcelName, strSheetName);
	}

	@After
	public void afterEveryScenario() {
		step.write(Common.SCENARIO_ENDS);
		Reusables.printRequestsToExtenReport(requestWriter, step);
		Reusables.printResponseToExtenReport(responseWriter, step);
	}

}
