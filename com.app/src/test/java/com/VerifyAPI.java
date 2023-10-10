package com;

import static org.hamcrest.Matchers.equalTo;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import utils.Constants;

public class VerifyAPI extends BaseAPI{
	
	@Test
	public void post_a_new_user() {

		String user_id = null;
		String loginPayloads = "{\"name\":\"%s\",\"job\":\"%s\"}";
		
		user_id = RestAssured.given()
		.spec(bearerTokenSpec)
		.body(String.format(loginPayloads,"Sam2", "IT2"))
		.when()
		.post(Constants.createUser)
		.then()
		.spec(common201Response)
		.extract()
		.response().jsonPath().get("id").toString();	
		
		System.out.println("created_user_id: "+user_id);
	
	}
	
	@Test
	public void get_all_users() {
		
		RestAssured.given()
				.spec(SpecAuth)
				.when()
				.get(Constants.usersById)
				.then()
				.spec(common200Response);
		
	}

	@Test (dependsOnMethods = {"get_all_users"})
	public void get_a_user_by_id() {
		String user_id = "4";
		RestAssured.given()
				.spec(SpecAuth)
				.param("id", user_id)
				.when()
				.get(Constants.usersById)
				.then()
				.assertThat().body("data.id", equalTo(Integer.parseInt(user_id)))
				.assertThat().body("data.first_name", equalTo("Eve"))
				.spec(common200Response);
	}

}
