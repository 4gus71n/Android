package ar.com.aleatoria.tracker.stat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


import org.joda.time.Hours;
import org.joda.time.Instant;
import org.joda.time.Minutes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ar.com.aleatoria.tracker.R;
import ar.com.aleatoria.tracker.db.Tags;
import ar.com.aleatoria.tracker.db.model.StudyTime;
import ar.com.aleatoria.tracker.db.model.Tag;
import ar.com.aleatoria.tracker.util.ApplicationContext;

public class TimelistArrayAdapter extends ArrayAdapter<StudyTime> {
	private final Context context;
	private final StudyTime[] values;

	public TimelistArrayAdapter(Context context, int textViewResourceId,
			List<StudyTime> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.values = (StudyTime[]) objects.toArray(new StudyTime[objects.size()]);
	}

	public TimelistArrayAdapter(Context context, int textViewResourceId,
			StudyTime[] objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.values = objects;
	}

	public TimelistArrayAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		this.context = context;
		this.values = null;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.timelist_row, parent, false);
		Tags tags = new Tags();
		StudyTime time = this.values[position];
		TextView dateText = (TextView) v.findViewById(R.id.timelist_row_date);
		TextView timeText = (TextView) v.findViewById(R.id.timelist_row_time);
		TextView tagsText = (TextView) v.findViewById(R.id.timelist_tags);
		dateText.setText(ApplicationContext.getInstance().getDateFormatter("dd MMMM, EEEE").format(time
				.getStartedTime()));
		String duration = formatDuration(time);
		timeText.setText(duration);
		String taglist = tags.commaSeparate(tags.getTagsForStudyTime(time));
		tagsText.setText(taglist);
		return v;
	}

	private String formatDuration(StudyTime time) {
		Instant from = new Instant(time.getStartedTime().getTime());
		Instant to = new Instant(time.getFinishedTime().getTime());
		
		String duration = "";
		Hours hours = Hours.hoursBetween(from, to);
		if (hours.getHours() > 0) {
			from = from.plus(hours.toStandardDuration());
			duration = hours.getHours() + "h, ";
		}
		Minutes minutes = Minutes.minutesBetween(from, to);
		duration += minutes.getMinutes() + "m";
		return duration;
	}
}
