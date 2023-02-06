package com;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matchers;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.Constants;

public class BaseAPI {
	
	protected static Logger myLog = LogManager.getLogger(BaseAPI.class);
	protected RequestSpecification SpecAuth = new RequestSpecBuilder()
			.setBaseUri(Constants.baseUrl)
			.setAccept(ContentType.JSON)
			.setContentType(ContentType.JSON)
			.build();
	protected RequestSpecification specNoAuth;

	protected ResponseSpecification commonResponse;
	
	private String authToken;
	
	@BeforeSuite
	public void setUpSpecs() {

		String loginPayloads = "{\"identifier\":\"%s\",\"password\":\"%s\"}";

		Response response = RestAssured.given().spec(SpecAuth)
				.body(String.format(loginPayloads,Constants.identifier, Constants.password))
				.when()
				.post(Constants.authUrl);
		
		authToken = response.jsonPath().get("jwt").toString();

		specNoAuth = new RequestSpecBuilder()
				.setBaseUri(Constants.baseUrl)
				.setAccept(ContentType.JSON)
				.setContentType(ContentType.JSON)
				.build();
		

		myLog.debug("============");
		myLog.info(response.getStatusCode());
		System.out.println("@@@@@1: "+response.getHeaders());
		System.out.println("@@@@@2: "+response.getCookies());
		System.out.println("@@@@@3: "+response.body().asPrettyString());
		System.out.println("@@@@@4: "+response.getTime());
		System.out.println("@@@@@5: "+authToken);

		commonResponse = new ResponseSpecBuilder()
				.expectStatusCode(200)
				.expectResponseTime(Matchers.lessThan(5000L))
				.build();

	}

	@AfterSuite
	public void cleanUpActivities() {
		authToken=null;
	}
	

}
