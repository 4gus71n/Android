
package ar.com.kimboo.r2d2;

import android.os.Bundle;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import ar.com.kimboo.r2d2.db.DatabaseManager;
import ar.com.kimboo.r2d2.model.User;

/**
 * @author astinx
 */

/**
 * ----------------------------------------------------------------------------
 * "THE BEER-WARE LICENSE" (Revision 42): <phk@FreeBSD.ORG> wrote this file. As
 * long as you retain this notice you can do whatever you want with this stuff.
 * If we meet some day, and you think this stuff is worth it, you can buy me a
 * beer in return Poul-Henning Kamp
 * ----------------------------------------------------------------------------
 */
public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseManager.init(this);
        setContentView(R.layout.activity_main);
    }

    public void login(View view) {
        TextView loginResult = (TextView) findViewById(R.id.login_result);
        User currUser = new User(((EditText) findViewById(R.id.editText1)).getText().toString(),
                ((EditText) findViewById(R.id.editText2)).getText().toString());
        if (DatabaseManager.getInstance().existUser(currUser)) {
            loginResult.setText("The user has been logged!");
        } else {
            loginResult.setText("Username or password are incorrect!");
        }
    }

    public void signin(View view) {
        TextView loginResult = (TextView) findViewById(R.id.login_result);
        User currUser = new User(((EditText) findViewById(R.id.editText1)).getText().toString(),
                ((EditText) findViewById(R.id.editText2)).getText().toString());
        if (DatabaseManager.getInstance().existUserEmail(currUser.getEmail())) {
            loginResult.setText("That user already exist!");
        } else {
            DatabaseManager.getInstance().addUser(currUser);
            loginResult.setText("The new user has been created!");
            //We change the view
            Intent mainView = new Intent(this, ApplicationActivity.class);
            startActivity(mainView);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
