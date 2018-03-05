package rest.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

/**
 * PropertiesManager : Reads the properties defined in rest_example_config.properties
 *
 */
public class PropertiesManager
{
    private static PropertiesManager propertyManager;
    private HashMap<String,String> propertiesList = new HashMap<String,String>();
    private static final String PROPERTIES_FILE = "rest_example_config.properties";
    
    
    private PropertiesManager(){
        Properties properties = new Properties();
        try {
            FileInputStream in = new FileInputStream(PROPERTIES_FILE);
            properties.load(in);
            in.close();
            
            //load properties into HashMap
            for (String propertyName : properties.stringPropertyNames()){
                String propertyValue = properties.getProperty(propertyName);
                propertiesList.put(propertyName, propertyValue);
            }
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public static PropertiesManager getPropertiesManager(){
        if(propertyManager == null){
            propertyManager =  new PropertiesManager();
        }
        return propertyManager;
    }
    
    public String getPropertyValue(String propertyName) throws RestClientException{
        String propValue = propertiesList.get(propertyName);
        if (propValue == null || propValue.isEmpty()){
            throw new RestClientException(Constants.MISSING_PROPERTY + " " + propertyName);
        }
        return propertiesList.get(propertyName);
    }
}
