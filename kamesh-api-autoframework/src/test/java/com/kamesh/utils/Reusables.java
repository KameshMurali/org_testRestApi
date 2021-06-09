package com.kamesh.utils;

import java.io.StringWriter;

import cucumber.api.Scenario;

public class Reusables {
	public static void printRequestsToExtenReport(StringWriter requestWriter2, Scenario step2) {
		String requestSpecifications = requestWriter2.toString().replaceAll("Set-Cookie(.*?);", "");
		String requestSpecificationArray[] = requestSpecifications.split("HTTP/1.1");
		for (String rp : requestSpecificationArray) {
			if (!rp.isEmpty()) {
				step2.write(Common.REQUEST);
				step2.write(rp);
			}
		}
	}

	public static void printResponseToExtenReport(StringWriter responseWriter2, Scenario step2) {
		String responseSpecifications = responseWriter2.toString().replaceAll("Set-Cookie(.*?);", "");
		String responseSpecificationArray[] = responseSpecifications.split("HTTP/1.1");
		for (String rp : responseSpecificationArray) {
			if (!rp.isEmpty()) {
				step2.write(Common.RESPONSE);
				step2.write(rp);
			}
		}
	}
}
