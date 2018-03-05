package rest.example;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.Resource;

import com.ibm.json.java.JSON;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

public class ResourceIdentifierUtil
{
    private static final String peopleService = "people";
    private static final String accessService = "access";
    
    public static String getPersonIdentifier(List<String> authTokens, String personName) throws RestClientException{
        String restURL = PropertiesManager.getPropertiesManager().getPropertyValue(Constants.BASE_URL) + "/" + peopleService;
        Map<String,String> queryParameters = new HashMap<String, String>();
        queryParameters.put("cn", personName);
        
        Resource r = ResourceBuilder.buildResource(restURL, queryParameters, authTokens);
        ClientResponse response = r.get();
        return getEntityURIFromResponse(response.getEntity(String.class));
    }

    public static String getAccessdentifier(List<String> authTokens, String accessName) throws RestClientException{
        String restURL = PropertiesManager.getPropertiesManager().getPropertyValue(Constants.BASE_URL) + "/" + accessService;
        Map<String,String> queryParameters = new HashMap<String, String>();
        queryParameters.put("accessName", accessName);
        
        Resource r = ResourceBuilder.buildResource(restURL, queryParameters, authTokens);
        ClientResponse response = r.get();
        return getEntityURIFromResponse(response.getEntity(String.class));
    }
    
    private static String getEntityURIFromResponse(String response) throws RestClientException{
        String uri = "";
        
        JSONArray result;
        try {
            result = (JSONArray) JSON.parse(response);
            if(result.size() > 1){
                throw new RestClientException(Constants.TOO_MANY_RESOURCES);
            }
            if(result == null || result.isEmpty()){
                throw new RestClientException(Constants.RESOURCE_NOT_FOUND);
            }
            else{
                JSONObject entity = (JSONObject) result.get(0);
                JSONObject selfLink = (JSONObject)((JSONObject)entity.get("_links")).get("self");
                uri = (String)selfLink.get("href");
            }
        } catch (IOException e) {
            
        }
        return uri;
    }
}
