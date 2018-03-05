package rest.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.Resource;

public class IAMRestActions {
	
	
	public static ClientResponse searchPeople(String filter, String attributes, String username, String userpassword){
		
		ClientResponse response = null;
		
		String restURL = Constants.BASE_URL + "/rest/people";
		
		String[] filterParts = filter.split("=");
		System.out.println("filterPart[0]: " + filterParts[0]);
		System.out.println("filterPart[1]: " + filterParts[1]);
		//String attributes = "cn,sn";
		Map<String,String> queryParameters = new HashMap<String, String>();
		queryParameters.put(filterParts[0],filterParts[1]);
		queryParameters.put("attributes", attributes);
		
		
		AuthenticationUtil authUtil = new AuthenticationUtil();
		List<String> authTokens;
		try {
			authTokens = authUtil.authenticate(username, userpassword);
			Resource r = ResourceBuilder.buildResource(restURL, queryParameters, authTokens);
			response = r.get();
			
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response;
	}

}
