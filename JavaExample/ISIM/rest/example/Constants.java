package rest.example;

public class Constants
{

    // Properties defined in isim.properties file
    //public static final String BASE_URL = "baseURL";
	public static final String BASE_URL = "http://isimserver:9080/itim"; //resplace isimserver with your ISIM URL
        
    // URL constants: 
    public static final String LOGIN_URL = "itim/restlogin/login.jsp";
        
    // Exception Messages
    public static final String MISSING_PROPERTY = "Property not defined in isim.properties file";
    public static final String TOO_MANY_RESOURCES = "Multiple resources are found with the given name";
    public static final String RESOURCE_NOT_FOUND = "No resource is found with the given name";
    public static final String UNABLE_TO_CONNECT = "Not able to connect with the remote server";
}
