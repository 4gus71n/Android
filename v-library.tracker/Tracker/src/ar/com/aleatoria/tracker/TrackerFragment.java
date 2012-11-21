
package ar.com.aleatoria.tracker;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;
import ar.com.aleatoria.tracker.db.Tags;
import ar.com.aleatoria.tracker.db.model.StudyTime;
import ar.com.aleatoria.tracker.db.model.Tag;
import ar.com.aleatoria.tracker.util.ApplicationContext;

public class TrackerFragment extends Fragment {
    private LinearLayout mTagsView;

    public StudyTime studyTime = new StudyTime();

    public boolean running = false;

    private Drawable mGreenBadge, mOrangeBadge;

    public String[] mTags;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Get all tags and turn it into an array to be displayed in the
        // autocomplete text view.
        List<String> tagList = new ArrayList<String>();
        for (Tag tag : ApplicationContext.getInstance().getTagDAO().getAll()) {
            tagList.add(tag.tag);
        }
        mTags = tagList.toArray(new String[tagList.size()]);

        mGreenBadge = this.getResources().getDrawable(R.drawable.green_badge);
        mOrangeBadge = this.getResources().getDrawable(R.drawable.orange_badge);

        View v = inflater.inflate(R.layout.fragment_tracker, container, false);
        mTagsView = (LinearLayout)v.findViewById(R.id.tags_view);
        mTagsView.setVisibility(LinearLayout.GONE);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, mTags);
        MultiAutoCompleteTextView textView = (MultiAutoCompleteTextView)v.findViewById(R.id.tags);
        textView.setAdapter(adapter);
        textView.setTextColor(getResources().getColorStateList(R.color.mycolorautocomplete));
        textView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Chronometer chronometer = (Chronometer)getView().findViewById(R.id.chronometer);
        outState.putLong("currentTime", chronometer.getBase());
        outState.putBoolean("running", running);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ApplicationContext.getInstance().appendTrackerBundle(savedInstanceState);

        if (savedInstanceState != null) {
            Chronometer chronometer = (Chronometer)getView().findViewById(R.id.chronometer);
            chronometer.setBase(savedInstanceState.getLong("currentTime"));
            boolean wasRunning = savedInstanceState.getBoolean("running");
            if (wasRunning)
                start();
        }
    }

    /*
     * Beware: you need to set a base time for the chronometer before calling
     * this method.
     */
    public void start() {
        Chronometer chronometer = (Chronometer)getView().findViewById(R.id.chronometer);
        ImageButton trigger = (ImageButton)getView().findViewById(R.id.trigger);
        studyTime.setStartedTime(Calendar.getInstance().getTime());
        chronometer.start();
        trigger.setImageDrawable(mOrangeBadge);
        running = true;
        mTagsView.setVisibility(LinearLayout.GONE);
    }

    public void stop() throws NumberFormatException, ParseException {
        Chronometer chronometer = (Chronometer)getView().findViewById(R.id.chronometer);
        ImageButton trigger = (ImageButton)getView().findViewById(R.id.trigger);
        chronometer.stop();
        studyTime.setFinishedTime(Calendar.getInstance().getTime());
        running = false;

        trigger.setImageDrawable(mGreenBadge);
        mTagsView.setVisibility(LinearLayout.VISIBLE);
    }

    private void reset() {
        Chronometer chronometer = (Chronometer)getView().findViewById(R.id.chronometer);
        ImageButton trigger = (ImageButton)getView().findViewById(R.id.trigger);
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());
        running = false;
        trigger.setImageDrawable(mGreenBadge);
    }

    public void triggerClick(View view) {
        try {
            Chronometer chronometer = (Chronometer)getView().findViewById(R.id.chronometer);

            long now = SystemClock.elapsedRealtime();
            if (running) {
                stop();
            } else {
                chronometer.setBase(now);
                start();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void saveTimeClick(View view) throws ParseException {
        TextView tags_input = (TextView)getView().findViewById(R.id.tags);
        if (tags_input.getText().length() > 0) {
            if (studyTime.getTotalTime() > ApplicationContext.getInstance().getMinTimePreference()) {
                hideSoftKeyboard(view);
                mTagsView.setVisibility(LinearLayout.GONE);
                studyTime.id = ApplicationContext.getInstance().getStudyTimeDAO().add(studyTime);
                Tags tags = new Tags();
                // Here we save the tags
                tags.applyTags(studyTime, tags_input.getText());
                Toast toast = Toast
                        .makeText(getActivity(), R.string.time_saved, Toast.LENGTH_SHORT);
                toast.show();
                reset();
            }
        } else {
            Toast toast = Toast.makeText(getActivity(), R.string.error_saving, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /*
     * Discard the time and reset the timer. If the user's already entered text
     * in the tags textview, the app prompts for confirmation.
     */
    public void _discard() {
        hideSoftKeyboard(getView());
        mTagsView.setVisibility(LinearLayout.GONE);
        reset();
        Toast toast = Toast.makeText(getActivity(), R.string.time_discarded, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void discardTimeClick(View view) {
        TextView tags_input = (TextView)getView().findViewById(R.id.tags);
        if (tags_input.getText().length() > 0) {
            /*
             * From:
             * http://stackoverflow.com/questions/2478517/how-to-display-a-
             * yes-no-dialog-box-in-android
             */
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            _discard();
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            // Just gtfo
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.discard_prompt)
                    .setPositiveButton("Sí", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        } else
            _discard();
    }

    /*
     * From:
     * http://stackoverflow.com/questions/4005728/hide-default-keyboard-on-
     * click-in-android
     */
    private void hideSoftKeyboard(View view) {
        TextView tags_input = (TextView)getView().findViewById(R.id.tags);
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(tags_input.getWindowToken(), 0);
    }

}
