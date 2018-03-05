package rest.example;

import java.util.ArrayList;
import java.util.List;

import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.ClientRuntimeException;
import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;

/**
 * AuthenticationUtil : Used for authenticating client with the remote ISIM server.
 */
public class AuthenticationUtil {
    
    private String ltpaToken;
    private String jsessionid;
    private String csrftoken;
    
    public List<String> authenticate(String userName, String password) throws RestClientException{
    	String baseURL = Constants.BASE_URL;
        String serverURL = baseURL.substring(0,baseURL.lastIndexOf("itim"));
        String loginURL = serverURL + Constants.LOGIN_URL;
        
        try{
            RestClient rc = new RestClient(ResourceBuilder.getClientConfig());
            Resource r1 = rc.resource(loginURL).accept("*/*");
            ClientResponse resp1 = r1.get();
            setTokens(resp1);
        
            String authURL = serverURL + "itim/restlogin/j_security_check";
            RestClient rc2 = new RestClient(ResourceBuilder.getClientConfig());
            Resource r2 = rc2.resource(authURL);
            r2.cookie("j_username_tmp=" + userName + "; j_password_tmp=; JSESSIONID=" + jsessionid);
            r2.header("Content-Type", "application/x-www-form-urlencoded");
            ClientResponse resp2 = r2.post("j_username=" + userName + "&j_password=" + password);
            setTokens(resp2);
        
            String sysUserUrl = serverURL + "itim/rest/systemusers/me";
            RestClient rc3 = new RestClient(ResourceBuilder.getClientConfig());
            Resource r3 = rc3.resource(sysUserUrl);
            r3.cookie(ltpaToken);
            r3.cookie(jsessionid);
            r3.header("Content-Type", "application/x-www-form-urlencoded");
            ClientResponse resp3 = r3.get();
            setTokens(resp3);
        
            return getTokens();
        }
        catch(ClientRuntimeException e){
            throw new RestClientException(Constants.UNABLE_TO_CONNECT,e);
        }
    }

    public String getCsrftoken() {
        return csrftoken;
    }
    
    private List<String> getTokens(){
        List<String> tokens = new ArrayList<String>();
        if(ltpaToken != null){
            tokens.add(ltpaToken);
        }
        if(jsessionid != null){
            tokens.add(jsessionid);
        }
        return tokens;
    }
    
    private void setTokens(ClientResponse _clientResponse) {
        List<String> cookies = _clientResponse.getHeaders().get("Set-Cookie");
        if ( null != cookies ) {
            for ( String cookie : cookies ) {
                if ( cookie.startsWith("LtpaToken2") ) {
                    ltpaToken = cookie;
                } else if (cookie.startsWith("JSESSIONID")) {
                    jsessionid = cookie;
                }
            }
        }
        List<String> csrfTokens = _clientResponse.getHeaders().get("CSRFToken"); 
        if (null != csrfTokens && csrfTokens.size() > 0) {
            csrftoken = csrfTokens.get(0);
        }
    }

}
