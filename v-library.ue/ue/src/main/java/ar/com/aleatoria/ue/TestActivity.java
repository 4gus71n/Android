package ar.com.aleatoria.ue;

import org.springframework.http.ResponseEntity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import ar.com.aleatoria.ue.dao.User;
import ar.com.aleatoria.ue.rest.RestClient;

public class TestActivity extends Activity {
	private RestClient restClient;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        
        restClient = new RestClient();
    	restClient.setGsonHttpMessageConverterAsMessageConverter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_test, menu);
        return true;
    }
    
    public void test(View view) {
    	ResponseEntity<User[]> subjects = restClient.getForEntity("http://10.0.2.2:8080/server/user/", User[].class);
    	for ( User sub : subjects.getBody()) {
    		Log.d("Users",""+sub.getUsername());
    	}
    }
    
    public void signup(View view) {
    	String username = ((EditText) findViewById(R.id.editText1)).getText().toString();
    	String password = ((EditText) findViewById(R.id.editText2)).getText().toString();
    	User usuario = new User(username,password);
    	
    	restClient.setCredentials(username,password);
    	restClient.postForEntity("http://10.0.2.2:8080/server/user/", usuario, String.class);
    }
    
    public void login(View view) {
    	
    }
}
