package com;

import static org.hamcrest.Matchers.equalTo;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import utils.Constants;

public class verifyAPI extends BaseAPI{
	
	String r_id="4";

	@Test
	public void post_a_new_user() {

		String loginPayloads = "{\"name\":\"%s\",\"job\":\"%s\"}";
		
		r_id = RestAssured.given()
		.spec(bearerTokenSpec)
		.body(String.format(loginPayloads,"Sam2", "IT2"))
		.when()
		.post(Constants.createUser)
		.then()
		.spec(common201Response)
		.extract()
		.response().jsonPath().get("id").toString();	
		
		//System.out.println("1@@@@@ "+r_id);
	
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
		
		RestAssured.given()
				.spec(SpecAuth)
				.param("id", r_id)
				.when()
				.get(Constants.usersById)
				.then()
				.assertThat().body("data.id", equalTo(Integer.parseInt(r_id)))
				.assertThat().body("data.first_name", equalTo("Eve"))
				.spec(common200Response);
		
	}

}
