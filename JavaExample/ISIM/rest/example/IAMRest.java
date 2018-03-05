package rest.example;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.apache.wink.client.ClientResponse;

import com.ibm.json.java.JSON;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONArtifact;
import com.ibm.json.java.JSONObject;


public class IAMRest {
	
	private static String username = "";
	private static String userpassword = "";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Enter your ISIM Username: ");
		username = scanner.nextLine();
		
		System.out.println("Enter your ISIM Password: ");
		userpassword = scanner.nextLine();
		
		if((username != null ) && (userpassword != null)){
			System.out.println("RestTest: testing ISIM REST APIs...");
			
			AuthenticationUtil authUtil = new AuthenticationUtil();
			try {
				//test some action
				System.out.println("What would you like to do: ");
				System.out.println("1: Test Authentication");
				System.out.println("2: Search for a User");
				int actionOption = scanner.nextInt();
				scanner.nextLine();
				if(actionOption == 1){
					System.out.println("You selected to test authentication...");
					List<String> authTokens = authUtil.authenticate(username, userpassword);
					System.out.println("authTokens: " + authTokens.toString());
					System.out.println("CSRFTOKEN: " + authUtil.getCsrftoken());
				} else if (actionOption == 2){
					System.out.println("You selected to search for a User");
					System.out.println("Enter a filter in the format: 'attr=value': ");
					String filter = scanner.nextLine();
					System.out.println("Enter coma-delimited list of attributes to return.  Example: \"cn,sn\"");
					String attributes = scanner.nextLine();
					if(filter != null && attributes != null ){
						System.out.println("Using filter: " + filter);
						System.out.println("Returning attributes: " + attributes);
						ClientResponse response = IAMRestActions.searchPeople(filter, attributes, username, userpassword);
						int responseCode = response.getStatusCode();
						System.out.println("search response code: " + responseCode);
						if(responseCode != 404){
							String respString = response.getEntity(String.class);
							System.out.println(respString);
							JSONArray jObj = (JSONArray)JSON.parse(respString);
							System.out.println("JSON: " + jObj);
							JSONObject jObj1 = (JSONObject) jObj.get(0);
							System.out.println("TEST: " + jObj1);
							JSONObject testme = (JSONObject) ((JSONObject) jObj1.get("_links")).get("self");
							String testme2 = (String) testme.get("title");
							System.out.println("TEST title: " + testme2);
							
						}
					} else {
						System.out.println("you didn't enter a filter");
					}
				}

				
			} catch (RestClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			scanner.close();

			
		} else {
			System.out.println("need to enter creds...");
			scanner.close();
		}

	}

}
