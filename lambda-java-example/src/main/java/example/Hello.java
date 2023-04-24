package example;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import org.json.*;
//import org.json.JSONException;
import java.io.FileOutputStream;
//import org.json.simple.JSONObject;
//import org.json.simple.JSONArray;
//import java.io.FileWriter;
//import java.io.IOException;
import java.util.HashMap;

public class Hello {

	public static void main(String[] args) {
		String secretName = "varankey";
		Region region = Region.of("us-east-2");

		// Create a Secrets Manager client
		SecretsManagerClient client = SecretsManagerClient.builder().region(region).build();

		GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder().secretId(secretName).build();

		GetSecretValueResponse getSecretValueResponse;

		try {
			getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
		} catch (Exception e) {
			// For a list of exceptions thrown, see
			// https://docs.aws.amazon.com/secretsmanager/latest/apireference/API_GetSecretValue.html
			throw e;
		}

		String secret = getSecretValueResponse.secretString();
		JSONObject secretObj = new JSONObject(secret);
		String secretValue = secretObj.getString(secretName);

		// Your code goes here.
		String fileName = args[0];
		try {
			FileOutputStream fout = new FileOutputStream(fileName);
			HashMap<String, Object> additionalDetails = new HashMap<String, Object>();
			additionalDetails.put("region", region.toString());
			additionalDetails.put("secret", secretValue);
			byte b[] = secretValue.getBytes();// converting string into byte array
			fout.write(b);
			fout.close();
			// System.out.println("success...");
		} catch (Exception e) {
			System.out.println(e);
		}

	}

}
