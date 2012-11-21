package ar.com.aleatoria.ue.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import android.util.Log;


public class RestClient {
    RestTemplate restTemplate = new RestTemplate();
    private String username;
    private String password; 

    public RestClient() {
    		SecureSimpleClientHttpRequestFactory requestFactory = new SecureSimpleClientHttpRequestFactory();
    		requestFactory.setCredentialsProvider(new UECrendentialsProvider(new Credentials(username, password)));
        	restTemplate.setRequestFactory(requestFactory);
    }
    
    public void setGsonHttpMessageConverterAsMessageConverter() {
    	List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
    	converters.add(new GsonHttpMessageConverter());
    	restTemplate.setMessageConverters(converters);
    }
    
	public void setCredentials(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public <T> void postForEntity(String url, Object request, Class<T> responseType) {
		try {
			restTemplate.postForEntity(url, request, responseType);
		} catch (HttpClientErrorException e){
			Log.d("RestClient",e.getMessage());
		}
		
	}
	
	public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType) {
		try {
			return restTemplate.getForEntity(url, responseType);
		} catch (HttpClientErrorException e){
			Log.d("RestClient",e.getMessage());
		}
		return null;
	}
}
