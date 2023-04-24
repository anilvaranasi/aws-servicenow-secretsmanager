# aws-servicenow-secretsmanager
This project is licensed under the terms of the MIT license.
ServiceNow jar file creation to access aws secrets manager
This JAR should be deployed on a midserver that has permission to communicate with AWS secrets manager.
Required environment variables should be setup.
JAR file takes input as file name where the secret that to be added.
Full notes :
This article covers setup needed to create a jar file that can read secret from aws secrets manager.
Pre-requisites  : 
1. Have an IAM user account in AWS that has access to aws secrets.
2. Create a secret in AWS

Credentials and config file setup
Environment variable setup

AWS_SHARED_CREDENTIALS_FILE -> C:\Users\XXXXXXXUSER\.aws\credentials
AWS_SECRET_ACCESS_KEY ->qVbOp61XwvjACjsiXXXXXYqxhpqyVYxekKL
AWS_DEFAULT_REGION -> us-east-2
AWS_CONFIG_FILE -> C:\Users\XXXXXXXUSER\.aws\config
AWS_ACCESS_KEY_ID -> AKIAT6XXXIOIYWV6
HOME -> C:\Users\XXXXXXXUSER
Add Maven bin to PATH
Install new software in eclipse to install aws components
    url : https://aws.amazon.com/eclipse
Contents of config file file name : config
[default]
region=us-east-2
output=json

Contents of credentials file file name : credential
[default]
aws_access_key_id=AKIXXXXX5ZIOIYWV6
aws_secret_access_key=qVbOp61XXXXXXGOLWpCxYqxhpqyVYxekKL


Create new project in Maven
    Create a simple project
    Group Id: doc-examples
    Artifact Id: lambda-java-example
    Version: 0.0.1-SNAPSHOT
    Packaging: jar
    Name: lambda-java-example

Add dependency 1
    aws-lambda-java-core
        Group Id: com.amazonaws
        Artifact Id: aws-lambda-java-core
        Version: 1.2.2

Add Plugin 1
    maven-shade-plugin
    Group Id: org.apache.maven.plugins
    Artifact Id: maven-shade-plugin
    Version: 3.2.2

Add Plugin 2
    org.apache.maven.plugins
    maven-jar-plugin
    3.3.0

Add configuration to plugin 2 (maven-jar-plugin)
            <configuration>
          <archive>
            <manifest>
              <addClasspath>true</addClasspath>
              <mainClass>example.Hello</mainClass>
            </manifest>
          </archive>
        </configuration>

Additional dependencies and properties to communicate with aws secrets
Add properties
    maven.compiler.source
    1.8
    maven.compiler.target
    1.8
    start-class
    example.Hello
Add dependency 3
    software.amazon.awssdk
    secretsmanager
    2.20.47
Add dependency 4
    software.amazon.awssdk
    regions
    2.20.47
Add dependency 5    
    org.slf4j
    slf4j-api
    2.0.7
Add dependency 6    
    org.slf4j
    slf4j-simple
    2.0.7

Add Java class to project
        Package: example
        Name: Hello

Add required code to Hello class this code is available in the aws secret record under java tab
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
Update project properties ->Java compiler -> uncheck -> use default compliance settings -> update generated class file compatibility -> 1.8, source 
compatibility -> 1.8, complier compliance level -> 1.8

Set encoding on project properties
    Resource -> text file encoding -> Other -> UTF-8
Build Project
    Run maven build
        Goals : package shade:shade

Check target to see the jar file
Execute target jar file
    D:\EclipseWorkspaces\Take2\lambda-java-example\target>
    java -jar lambda-java-example-0.0.1-SNAPSHOT.jar D:\\testout.txt


references
**********
https://www.sohamkamani.com/java/cli-app-with-maven/
https://stackoverflow.com/questions/29920434/maven-adding-mainclass-in-pom-xml-with-the-right-folder-path
https://maven.apache.org/shared/maven-archiver/examples/classpath.html
https://docs.aws.amazon.com/toolkit-for-eclipse/v1/user-guide/setup-install.html
https://docs.aws.amazon.com/lambda/latest/dg/java-package-eclipse.html
https://docs.aws.amazon.com/toolkit-for-eclipse/v1/user-guide/welcome.html
https://mvnrepository.com

ServiceNow Midserver setup
1. Install JDK from oracle version 8 or above
https://www.oracle.com/java/technologies/downloads/#jdk20-windows
setup JAVA_HOME
C:\Program Files\Java\jdk-20\bin
java -jar lambda-java-example-0.0.1-SNAPSHOT.jar D:\\testout.txt

18:29

There is a ServiceNow updateset that can be used to run this jar and fetch secret string as a subflow output.
link to Servicenow share = 
