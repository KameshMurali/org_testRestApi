package com.kamesh.schema;

import java.util.Map;

import org.junit.Assert;

import java.util.List;

import com.google.gson.Gson;
import com.kamesh.stepdefs.CommonSteps;

public class employee extends CommonSteps
  
{
	public static void validateProfileImageForEmployee() throws Exception {
//		Map<String, String> getEmployeeList = new Gson().fromJson(res.getBody().asString(),Map.class);
//		for(Map.Entry<String, String> pair:getEmployeeList.entrySet()) {
//			List
//		}
		List<Map<String,Object>> employeeList = res.getBody().jsonPath().getList("data");
		for(Map<String, Object> employeeData:employeeList)
			Assert.assertTrue("Employee:"+employeeData.get("id")+" profileImage isn't empty",employeeData.get("profile_image").toString().isEmpty());
	}
	
	public static void verifyEmployeeDetails() throws Exception{
		Map<String, String> getEmployeeList = new Gson().fromJson(res.getBody().asString(),Map.class);
		for(Map.Entry<String, String> pair:getEmployeeList.entrySet()) {
			if(pair.getValue()!=null) {
				if(pair.getKey().equalsIgnoreCase("status")) {
					Assert.assertTrue("Excpected status value doesn't match with the actual: "+pair.getValue(),pair.getValue().matches("\\w+"));
				}
				else if(pair.getKey().equalsIgnoreCase("message")) {
					//Validation of Message 
					Assert.assertTrue("Excpected message doesn't match with the actual: "+pair.getValue(),pair.getValue().equalsIgnoreCase("Successfully! Record has been fetched."));
				}
			}
		}
		Map<String,Object> employeedataMap = res.getBody().jsonPath().getMap("data");
		for(Map.Entry<String, Object> employeedata : employeedataMap.entrySet()) {
			if(employeedata.getValue()!=null) {
				if(employeedata.getKey().equalsIgnoreCase("id")) {
					Assert.assertTrue("id field doesnt have the expected value: "+employeedata.getValue(), employeedata.getValue().toString().matches("\\d+"));
				}
				else if(employeedata.getKey().equalsIgnoreCase("employee_name")) {
					Assert.assertTrue("employee_name field doesnt have the expected value: "+employeedata.getValue(), employeedata.getValue().toString().matches(".*"));
				}
				else if(employeedata.getKey().equalsIgnoreCase("employee_salary")) {
					Assert.assertTrue("employee_salary field doesnt have the expected value: "+employeedata.getValue(), employeedata.getValue().toString().matches("\\d+"));
				}
				else if(employeedata.getKey().equalsIgnoreCase("employee_age")) {
					Assert.assertTrue("employee_name field doesnt have the expected value: "+employeedata.getValue(), employeedata.getValue().toString().matches("\\d+"));
				}
				else if(employeedata.getKey().equalsIgnoreCase("profile_image")) {
					Assert.assertTrue("profileImage isn't empty",employeedata.getValue().toString().isEmpty());
				}
			}
		}
	}
	
}
