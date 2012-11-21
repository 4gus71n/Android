
package ar.com.aleatoria.tracker.util;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.MultiAutoCompleteTextView;
import ar.com.aleatoria.akrasia.AkrasiaService;
import ar.com.aleatoria.akrasia.ServiceClient;
import ar.com.aleatoria.tracker.R;
import ar.com.aleatoria.tracker.TrackerFragment;
import ar.com.aleatoria.tracker.db.StudyTimeDAO;
import ar.com.aleatoria.tracker.db.TagDAO;
import ar.com.aleatoria.tracker.db.TimeTagDAO;
import ar.com.aleatoria.tracker.db.model.Tag;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class ApplicationContext {
    private Activity mMainActivity;

    private static final long MIN_PERSISTANCEABLE_TIME_VALUE = 1; // Minute

    private static String MIN_PERSISTANCEABLE_TIME_LABEL = "min-persistanceable-time";

    private static ApplicationContext sItself;

    private SimpleDateFormat mDateFormatter;

    // Add here all the daos that you want, you only must implement the
    // getSomthingDAO() method
    private StudyTimeDAO mStudyTimeDAO;

    private TagDAO mTagDAO;

    private TimeTagDAO mTimeTagDAO;

    private Bundle mTrackerBundle;

    private ServiceClient mClientServiceAPI;
    

    public void setMainActivity(Activity mainActivity) {
        this.mMainActivity = mainActivity;
    }

    public static ApplicationContext getInstance() {
        if (sItself == null)
            sItself = new ApplicationContext();
        return sItself;
    }

    public Activity getMainActivity() {
        return this.mMainActivity;
    }

    public TagDAO getTagDAO() {
        if (mTagDAO == null)
            mTagDAO = new TagDAO(this.mMainActivity);
        return mTagDAO;
    }

    public StudyTimeDAO getStudyTimeDAO() {
        if (mStudyTimeDAO == null)
            mStudyTimeDAO = new StudyTimeDAO(this.mMainActivity);
        return mStudyTimeDAO;
    }

    /**
     * This method returns a date formatter wich formate a date in spanish
     * 
     * @param format The output format
     */
    public SimpleDateFormat getDateFormatter(String format) {
        if (mDateFormatter == null) {
            mDateFormatter = new SimpleDateFormat(format);
            DateFormatSymbols formatSymbols = new DateFormatSymbols();
            formatSymbols.setMonths(new String[] {
                    "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto",
                    "Septiembre", "Octubre", "Noviembre", "Diciembre"
            });
            formatSymbols.setWeekdays(new String[] {
                    "Sabado", "Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes"
            });
            mDateFormatter.setDateFormatSymbols(formatSymbols);
        }
        return mDateFormatter;
    }

    public TimeTagDAO getTimeTagDAO() {
        if (mTimeTagDAO == null)
            mTimeTagDAO = new TimeTagDAO(this.mMainActivity);
        return mTimeTagDAO;
    }

    /**
     * This method makes a temporary setup of the environment, like the min time
     * that is consider persistable, etc.
     */
    public void setupPreferences() {
        SharedPreferences preferences = mMainActivity.getSharedPreferences(
                "ar.com.aleatoria.tracker", Context.MODE_PRIVATE);
        preferences.edit().putLong(MIN_PERSISTANCEABLE_TIME_LABEL, MIN_PERSISTANCEABLE_TIME_VALUE);

    }

    /**
     * @return The min-persistanceable time value, saved in the shared
     *         preferences.
     */
    public long getMinTimePreference() {
        SharedPreferences preferences = mMainActivity.getSharedPreferences(
                "ar.com.aleatoria.tracker", Context.MODE_PRIVATE);
        return preferences.getLong(MIN_PERSISTANCEABLE_TIME_LABEL, 0);
    }

    private Fragment getFragmentById(String id) {
        return ((SherlockFragmentActivity)mMainActivity).getSupportFragmentManager()
                .findFragmentByTag(id);
    }

    /**
     * This method reload the list of tags of the main activity, by this way we
     * are capable to see the new tags in the autocomplete without reset the
     * app.
     */
    public void refreshAutocompleteTagList() {
        TrackerFragment fragment = (TrackerFragment)getFragmentById("tracker");
        // Get all tags and turn it into an array to be displayed in the
        // autocomplete text view.
        List<String> tagList = new ArrayList<String>();
        for (Tag tag : ApplicationContext.getInstance().getTagDAO().getAll()) {
            tagList.add(tag.tag);
        }
        fragment.mTags = tagList.toArray(new String[tagList.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mMainActivity,
                android.R.layout.simple_gallery_item, fragment.mTags);
        MultiAutoCompleteTextView textView = (MultiAutoCompleteTextView)fragment.getView()
                .findViewById(R.id.tags);
        textView.setAdapter(adapter);
        textView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }


    public void appendTrackerBundle(Bundle trackerBundle) {
        this.mTrackerBundle = trackerBundle;
    }

    public Bundle getTrackerBundle() {
        return mTrackerBundle;
    }
}
