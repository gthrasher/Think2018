package rest.example;

public class RestClientException extends Exception {

    private static final long serialVersionUID = -6643894596133338298L;
    
    public RestClientException(String message){
        super(message);
    }
    
    public RestClientException(String message, Exception e){
        super(message,e);
    }

}
