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
	String sshKey = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQD8JCuZZ8mKdGwYSgAjU6RU2QopdGx7+45EyRdzAd1uHPi742FvNyRUUbf37xq+ZQ+UHSzZXWVpa28ASRSJUzI9qg+PeK5N7cktNGbYyh0iU1D6eOb/BA/RBpdw5tnw1mVRWXheaqK1BudE50I+6AiGp7u1ZiMoAom1wnTXOZQUJ6qBqva35Lacc+VDXD4NF31v+38G7hAB/wPShi4JrdwzJy9RT6TerDA6M9Vo7g5m/tjb4FRrmRzEFdyM08K+G2MnymbyicsC4+85lD+rcCce7G633wlh2BCVgMM4gPawxNWK7EotVQpmyUpcOKX8GcqXHn01YrsVt6yZDZGGiX2d";
	int sshKeyId;
	@BeforeClass
	public void setUp() {
		requestSpec = new RequestSpecBuilder()
				.setContentType(ContentType.JSON)
				.setBaseUri("https://api.github.com")
				.addHeader("Authorization", "token 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")
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


