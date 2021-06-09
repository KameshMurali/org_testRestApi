package com.kamesh.runner;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features", glue = "com.kamesh.stepdefs", dryRun = false, strict = false, monochrome = true, tags = {
		"@assignment" }, plugin = { "pretty", "html:target/cucumber",
				"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
				"html:target/cucumber-html-report", "json:target/cucumber.json" })
public class TestRunner

{

}
