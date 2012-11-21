package ar.com.aleatoria.ue.rest;

import java.net.HttpURLConnection;

import org.springframework.util.Base64Utils;

public class Base64SecureClientHttpRequestFactory extends
		SecureSimpleClientHttpRequestFactory {
	
	public Base64SecureClientHttpRequestFactory() {
    }

    public void prepareSecureConnection(HttpURLConnection connection) {
            if (credentialsProvider==null) {
                    return;
            }
            Credentials credentials = credentialsProvider.getCredentials(null);
        String token = credentials.getUserName() + ":" + credentials.getUserName();
        String encodedAuthorization = Base64Utils.encode(token.getBytes()).toString();
        connection.setRequestProperty("Authorization", "Basic " + encodedAuthorization);            
    } 
}
