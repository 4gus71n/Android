package com.example.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

public class HttpFuck {
	Gson gson = new Gson();
	
	public <T> T post(String url, Class<T> clazz, List<NameValuePair> parameters) {
		// Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(url);
	    try {
	        // Add your data
	        httppost.setEntity(new UrlEncodedFormEntity(parameters));
	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        StringBuilder json = inputStreamToString(response.getEntity().getContent());
	        
			T gsonObject = gson.fromJson(json.toString(), clazz);
	        return gsonObject;
	        
	    } catch (ClientProtocolException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	    return null;
	}
	
	public <T> T get(String url, Class<T> clazz) {
		// Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpGet httppost = new HttpGet(url);
	    try {
	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        StringBuilder json = inputStreamToString(response.getEntity().getContent());
	        T gsonObject = gson.fromJson(json.toString(), clazz);
	        return gsonObject;
	        
	    } catch (ClientProtocolException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	    return null;
	}

	// Fast Implementation
	private StringBuilder inputStreamToString(InputStream is) throws IOException {
	    String line = "";
	    StringBuilder total = new StringBuilder();
	    
	    // Wrap a BufferedReader around the InputStream
	    BufferedReader rd = new BufferedReader(new InputStreamReader(is));

	    // Read response until the end
	    while ((line = rd.readLine()) != null) { 
	        total.append(line); 
	    }
	    
	    // Return full string
	    return total;
	}

	
}
