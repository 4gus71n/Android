
package ar.com.aleatoria.tracker;

import java.text.ParseException;
import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import ar.com.aleatoria.akrasia.AkrasiaService;
import ar.com.aleatoria.akrasia.AnnotatedHandler;
import ar.com.aleatoria.akrasia.ExecutesWhen;
import ar.com.aleatoria.akrasia.ServiceClient;
import ar.com.aleatoria.tracker.util.ApplicationContext;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;


public class MainActivity extends SherlockFragmentActivity {
    private ActionBar mActionBar;

    private Tab mStatTab;

    private Tab mTrackerTab;

	private ServiceClient mClientServiceAPI;
    
	/**
	 * Handler of incoming messages from service.
	 */
	public class  MyHandler  extends AnnotatedHandler {
		@ExecutesWhen(what = AkrasiaService.TRACKER_APP_BACKUP_RETRIVE)
		public void handleBackupRestore(Message msg) {
			Log.d("Client","Handling a message");
			Bundle bundle = msg.getData();
			if ((bundle != null) && (!bundle.isEmpty())) {
				Long timeStamp = bundle.getLong("last-time");
				Long chrometerTime = bundle.getLong("chrometer-time");
				Chronometer chrometer = (Chronometer) findViewById(R.id.chronometer);
				//We add the time between the timestamp and now to the chorometer base
				Long now = Calendar.getInstance().getTimeInMillis();
				chrometerTime = now - timeStamp + chrometerTime; 
				chrometer.setBase(chrometerTime);
				//This thing of make deprecated... i dont know...
				bundle.putBoolean("deprecated", true);
			}
		}
	};
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(com.actionbarsherlock.R.style.Sherlock___Theme);
        super.onCreate(savedInstanceState);
        // Notice that setContentView() is not used, because we use the root
        // android.R.id.content as the container for each fragment
        // try {
        // DatabaseFixture.populateDatabase();
        // } catch (NumberFormatException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // } catch (ParseException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        ApplicationContext.getInstance().setMainActivity(this);
        ApplicationContext.getInstance().setupPreferences();
        sherlockActionBarSetup();
    }

    private void sherlockActionBarSetup() {
        mActionBar = getSupportActionBar();
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayShowHomeEnabled(false);

        TabListener<TrackerFragment> trackerTabListener = new TabListener<TrackerFragment>(this,
                "tracker", TrackerFragment.class);
        mTrackerTab = mActionBar.newTab().setText("Track").setTabListener(trackerTabListener);
        mActionBar.addTab(mTrackerTab);

        TabListener<StatFragment> statTabListener = new TabListener<StatFragment>(this, "stats",
                StatFragment.class);
        mStatTab = mActionBar.newTab().setText("Stats").setTabListener(statTabListener);
        mActionBar.addTab(mStatTab);
    }

    public void sendBackupToTheService() {
    	Log.d("Client","We are going to make a backup of the chromnometer");
    	Chronometer chrometer = (Chronometer) findViewById(R.id.chronometer);
    	Bundle bundle = new Bundle();
		bundle.putLong("last-time", Calendar.getInstance().getTimeInMillis());
		bundle.putLong("chrometer-time", chrometer.getBase());
		bundle.putBoolean("deprecated", false);
		mClientServiceAPI.sendMessage(AkrasiaService.TRACKER_APP_BACKUP_REFRESH, bundle);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        sendBackupToTheService();
        mClientServiceAPI.doUnbindService(this);
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        mClientServiceAPI.doUnbindService(this);
    }
    
    public void restarBackupFromService(View view) {
    	mClientServiceAPI.sendMessage(AkrasiaService.TRACKER_APP_BACKUP_RETRIVE);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mClientServiceAPI.doUnbindService(this);
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	//Sadly this behavior can't be exported to ServiceClient.
        //This from below used to be in onResume method
        Log.d("Client","We are going to connect to the service");
        mClientServiceAPI = new ServiceClient();
      	mClientServiceAPI.setIncomingHandler(new MyHandler());
      	Intent intent = new Intent(AkrasiaService.class.getName());
      	
      	mClientServiceAPI.doBindService(intent,this);
    }
    
    /*
     * Apparently you can't just tie the callback to the fragment from:
     * http://stackoverflow.com/a/6271637/147072
     */
    public void triggerClick(View view) {
        TrackerFragment fragment = (TrackerFragment)getSupportFragmentManager().findFragmentByTag(
                "tracker");
        fragment.triggerClick(view);
    }

    public void saveTimeClick(View view) {
        TrackerFragment fragment = (TrackerFragment)getSupportFragmentManager().findFragmentByTag(
                "tracker");
        try {
            fragment.saveTimeClick(view);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // We reload the StatFragment this is to refresh the Graph of the
        // StatFragment
        mActionBar.removeTab(mStatTab);
        TabListener<StatFragment> statTabListener = new TabListener<StatFragment>(this, "stats",
                StatFragment.class);
        mStatTab = mActionBar.newTab().setText("Stats").setTabListener(statTabListener);
        mActionBar.addTab(mStatTab);

    }

    public void discardTimeClick(View view) {
        TrackerFragment fragment = (TrackerFragment)getSupportFragmentManager().findFragmentByTag(
                "tracker");
        fragment.discardTimeClick(view);
    }
}
