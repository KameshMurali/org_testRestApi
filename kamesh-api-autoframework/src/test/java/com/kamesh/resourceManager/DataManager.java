package com.kamesh.resourceManager;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.management.RuntimeErrorException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.kamesh.stepdefs.CommonSteps;

public class DataManager {

	private static HashMap<String, LinkedHashMap<String, String>> testData = new HashMap<String, LinkedHashMap<String, String>>();
	private static String strExcelSheet = "";

	public static void initExcelData(String excelName, String sheetName) throws Exception {
		if (testData.size() > 1 && strExcelSheet.equals(sheetName))
			return;
		strExcelSheet = sheetName;
		File inputFile = new File(
				System.getProperty("user.dir") + File.separator + "src/test/resources/data/" + excelName + ".xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook(inputFile);
		XSSFSheet sheet = workbook.getSheet(sheetName);
		int iMaxRows = sheet.getPhysicalNumberOfRows();
		LinkedHashMap<String, String> lData = new LinkedHashMap<String, String>();
		int iMaxCols = sheet.getRow(0).getPhysicalNumberOfCells();
		XSSFRow rowHeader = sheet.getRow(0);
		for (int iRow = 1; iRow < iMaxRows; iRow++) {
			XSSFRow row = sheet.getRow(iRow);
			String strTestCaseName = "";
			for (int iCol = 0; iCol < iMaxCols; iCol++) {
				XSSFCell cell = row.getCell(iCol);
				String strVal = "";
				if (cell != null) {
					strVal = cell.getStringCellValue().trim();
					if (rowHeader.getCell(iCol).getStringCellValue().equalsIgnoreCase("TestCaseName"))
						strTestCaseName = strVal;
				}
				lData.put(rowHeader.getCell(iCol).getStringCellValue(), strVal);
			}
			testData.put(strTestCaseName, lData);
		}
		CommonSteps.log.info("Data Loaded successfully");
	}

	public static String refreshData(String strInfo) {
		if (strInfo.trim().isEmpty())
			return "";
		String strNew = getData(strInfo.trim());

		return strNew;

	}

	private static String getData(String fieldName) {
		String strVal = "";
		String testcasename = CommonSteps.getTestCaseName();
		System.out.println("TEST CASE NAME:"+testcasename);
		if (!testData.containsKey(testcasename))
			throw new RuntimeException(
					" Data Retrieval failure, Test Case name "+testcasename+" is invalid/ not found in the input sheet");
		if (!testData.get(testcasename).containsKey(fieldName))
			return "";
		strVal = testData.get(testcasename).get(fieldName);
		return strVal;
	}

}
