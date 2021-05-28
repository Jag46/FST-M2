package apitest;

import static io.restassured.RestAssured.given;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class GitHubAPI {
	
	RequestSpecification requestSpec;
	ResponseSpecification responseSpec;
	String sshKey = "ssh-rsa XXXX";
	int sshKeyId;
	@BeforeClass
	public void setUp() {
		requestSpec = new RequestSpecBuilder()
				.setContentType(ContentType.JSON)
				.setBaseUri("https://api.github.com")
				.addHeader("Authorization", "token *****")
				.build();
	}
  
	@Test(priority=1)
	public void postTest() {
		String reqBody = "{\"title\": \"TestAPIKey\",\"key\": \"" +sshKey+ "\"}";
		
		Response response = given().spec(requestSpec)
			    .contentType(ContentType.JSON)
			    .body(reqBody)
			    .when().post("/user/keys");
		sshKeyId = response.then().extract().path("id");
		System.out.println("sshKeyId generated from gitHub: "+sshKeyId);
		
		response.then().statusCode(201);
		
	}
	
	@Test(priority = 2)
	public void getKeys() {
		Response response = given().spec(requestSpec)
				.when().get("/user/keys");

		String resBody = response.getBody().asPrettyString();
		System.out.println(resBody);
		response.then().statusCode(200);
	}

	@Test(priority = 3)
	public void deleteKeys() {
		Response response = given().spec(requestSpec)
				.pathParam("keyId", sshKeyId).when().delete("/user/keys/{keyId}");

		String resBody = response.getBody().asPrettyString();
		System.out.println(resBody);
		response.then().statusCode(204);
	}
}


