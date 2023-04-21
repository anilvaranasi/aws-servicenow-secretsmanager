package example;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import java.io.FileOutputStream;

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

		// Your code goes here.
		System.out.println("Hello, World secret!" + secret);
		try {
			String fileName = args[0];
			//FileOutputStream fout = new FileOutputStream("D:\\testout.txt");
			FileOutputStream fout = new FileOutputStream(fileName);
			String s = "Welcome to Varan Java. " + secret;
			byte b[] = s.getBytes();// converting string into byte array
			fout.write(b);
			fout.close();
			System.out.println("success...");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
