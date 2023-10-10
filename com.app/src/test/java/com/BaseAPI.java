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
	
	private String authToken;
	protected static Logger myLog = LogManager.getLogger(BaseAPI.class);
	protected RequestSpecification specNoAuth;
	protected ResponseSpecification common200Response;
	protected ResponseSpecification common201Response;
	
	protected RequestSpecification bearerTokenSpec = new RequestSpecBuilder()
			.setBaseUri(Constants.baseUrl)
			.setAccept(ContentType.JSON)
			.setUrlEncodingEnabled(false)
			.setContentType(ContentType.JSON)
			.addHeader("Authorization", "Bearer "+authToken)
			.build();
	
	protected RequestSpecification SpecAuth = new RequestSpecBuilder()
			.setBaseUri(Constants.baseUrl)
			.setAccept(ContentType.JSON)
			.setContentType(ContentType.JSON)
			.build();
	
	@BeforeSuite
	public void setUpSpecs() {

		String loginPayloads = "{\"email\":\"%s\",\"password\":\"%s\"}";
		Response response = RestAssured.given().spec(SpecAuth)
				.body(String.format(loginPayloads,Constants.email, Constants.password))
				.when()
				.post(Constants.userLogIn);

		authToken = response.jsonPath().get(Constants.token_path).toString();

		specNoAuth = new RequestSpecBuilder()
				.setBaseUri(Constants.baseUrl)
				.setAccept(ContentType.JSON)
				.setContentType(ContentType.JSON)
				.addHeader("authtoken", response.jsonPath()
				.get(Constants.token_path).toString())
				.build();
		
		myLog.debug("=====setUpSpecs=======");
		myLog.info("StatusCode: "+response.getStatusCode());

		//System.out.println("@@@@@1: "+response.getHeaders());
		//System.out.println("@@@@@2: "+response.getCookies());
		//System.out.println("@@@@@3: "+response.body().asPrettyString());
		//System.out.println("@@@@@4: "+response.getTime());
		//System.out.println("@@@@@5: "+authToken);

		common200Response = new ResponseSpecBuilder()
				.expectContentType("application/json")
				.expectStatusCode(200)
				.expectResponseTime(Matchers.lessThan(5000L))
				.build();

		common201Response = new ResponseSpecBuilder()
				.expectContentType("application/json")
				.expectStatusCode(201)
				.expectResponseTime(Matchers.lessThan(5000L))
				.build();	
	}

	@AfterSuite
	public void cleanUpActivities() {
		authToken = null;
	}
}
