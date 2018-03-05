package rest.example;

import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.Application;

import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.wink.client.ApacheHttpClientConfig;
import org.apache.wink.client.ClientConfig;
import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;

/**
 * ResourceBuilder : Creates org.apache.wink.client.Resource to make REST calls.
 *
 */
public class ResourceBuilder
{
    public static Resource buildResource(String RestURL, List<String> tokens){
        RestClient client = new RestClient(getClientConfig());
        
        Resource r = client.resource(RestURL).accept("application/json");
        
        if(tokens != null && tokens.size() >0) {
            for (String token : tokens){
                r.cookie(token);
            }
        }
        
        return r;
    }
        
    public static Resource buildResource(String RestURL, Map<String,String> queryParameters, List<String> tokens){
        RestClient client = new RestClient(getClientConfig());
        
        if(queryParameters != null) {
            StringBuffer queryParam = new StringBuffer();
            boolean first = true;
            for(String paramName : queryParameters.keySet()){
                if(!first){
                    queryParam.append("&");
                }
                queryParam.append(paramName);
                queryParam.append("=");
                queryParam.append(queryParameters.get(paramName));
                first = false;
            }
            RestURL = RestURL+"?"+queryParam.toString();
        }
        
        Resource r = client.resource(RestURL).accept("application/vnd.ibm.isim-v1+json");
        
        if(tokens != null && tokens.size() >0) {
            for (String token : tokens){
                r.cookie(token);
            }
        }
        
        return r;
    }
    
    static ClientConfig getClientConfig() {
        try{
        Scheme scheme = new Scheme("https", trustAllHttpsCertificates(),getPortNumber());
        DefaultHttpClient client = new DefaultHttpClient();
        client.getConnectionManager().getSchemeRegistry().register(scheme);
        ClientConfig config = new ApacheHttpClientConfig(client);
        config.followRedirects(false);
        config.acceptHeaderAutoSet(true);
        config.connectTimeout(300000); 
        config.readTimeout(300000); 
        config.setBypassHostnameVerification(true);
        config.applications(new Application() {
            @Override
            public java.util.Set<Class<?>> getClasses() {
                java.util.HashSet<Class<?>> classes = new java.util.HashSet<Class<?>>();
                classes.add(JacksonJaxbJsonProvider.class);//by default the wink client is not json enabled.
                return classes;
            }});

        return config;
    }catch(Exception e){
        e.printStackTrace();
    }
        return null;
    }
    
    private static org.apache.http.conn.ssl.SSLSocketFactory trustAllHttpsCertificates() throws Exception {
        // Create a trust manager that does not validate certificate chains:
        TrustManager[] trustAllCerts = new TrustManager[1];
        TrustManager tm = new TrustingManager();
        trustAllCerts[0] = tm;

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, null);
        org.apache.http.conn.ssl.SSLSocketFactory sslFactory = new org.apache.http.conn.ssl.SSLSocketFactory(sslContext); 
        
        sslFactory.setHostnameVerifier(new AllowAllHostnameVerifier());
        return sslFactory;
    }
    
    public static class TrustingManager implements TrustManager, X509TrustManager {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }

        public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }
    }
    
    private static int getPortNumber() throws RestClientException{
        //String baseURL = PropertiesManager.getPropertiesManager().getPropertyValue(Constants.BASE_URL);
        //String port = baseURL.substring(baseURL.lastIndexOf(":")+1, baseURL.indexOf("itim")-1);
    	String port = "9080";
        return Integer.parseInt(port);
    }

}
