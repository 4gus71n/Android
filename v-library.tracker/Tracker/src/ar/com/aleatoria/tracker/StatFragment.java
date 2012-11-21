package ar.com.aleatoria.tracker;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.res.Configuration;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import ar.com.aleatoria.tracker.db.model.StudyTime;
import ar.com.aleatoria.tracker.stat.TimelistArrayAdapter;
import ar.com.aleatoria.tracker.util.ISO8601;
import ar.com.aleatoria.tracker.util.ApplicationContext;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;

public class StatFragment extends Fragment {

	private static final String GRAPH_TITLE = "Estadisticas de estudio";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_stat, container, false);
		try {
			loadStatisticGraphic(v);
			populateTimelist(v);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return v;
	}
	
	public void populateTimelist(View v) throws ParseException {
		ListView timelist = (ListView) v.findViewById(R.id.timelist);
		List<StudyTime> studytimes = ApplicationContext.getInstance()
				.getStudyTimeDAO().getLastweek();
		TimelistArrayAdapter adapter = new TimelistArrayAdapter(getActivity(),
				android.R.id.text1, studytimes);
		timelist.setAdapter(adapter);
	}

	public void loadStatisticGraphic(View v) throws NumberFormatException,
			ParseException {
		// We are gonna to take the todays date, and calculate the date that was
		// seven days ago, to do this, we use the roll method, that if we go beyond
		// the max limit (i.e day 30, minute 60) it wraps the value of the field.
		Calendar aWeekAgoDate = Calendar.getInstance();
		List<Date> weekend = new ArrayList<Date>();
		for (int dateIndex = 0; dateIndex < 7; dateIndex++) {
			weekend.add(weekend.size(), aWeekAgoDate.getTime());
			aWeekAgoDate.roll(Calendar.DAY_OF_YEAR, false);
		}

		LinkedHashMap<String, Long> studyTimes = StudyTime
				.totalTimeStudiedInDays(weekend);

		List<GraphViewData> graphViews = generateViewSeries(studyTimes);
		GraphViewSeries exampleSeries = new GraphViewSeries(
				graphViews.toArray(new GraphViewData[graphViews.size()]));

		GraphView graphView = new BarGraphView(v.getContext(), GRAPH_TITLE) {
			@Override
			public void drawSeries(Canvas canvas, GraphViewData[] values,
					float graphwidth, float graphheight, float border,
					double minX, double minY, double diffX, double diffY,
					float horstart) {
				float colwidth = (graphwidth - (2 * border)) / values.length;
				colwidth += 6;
				// draw data
				for (int i = 0; i < values.length; i++) {
					float valY = (float) (values[i].valueY);
					float ratY = (float) (valY / (diffY + minY));
					float y = (graphheight - 10) * ratY;
					canvas.drawRect((i * colwidth) + horstart, (border - y)
							+ graphheight, ((i * colwidth) + horstart)
							+ (colwidth - 2), graphheight + border - 1, paint);
				}
			}
		};

		// Now we are gonna to take the days strings of the hashmap (the
		// keys) and use them as labels
		List<String> labelList = new ArrayList<String>(studyTimes.keySet());
		Collections.reverse(labelList);

		// We are gonna to turn the string-dates in the list labelList that
		// are like yyyy-mm-dd to a shortest form like mm/dd
		for (int index = 0; index < labelList.size(); index++) {
			String label = labelList.get(index);
			labelList.set(index, ISO8601.formatTinyTag(label));
		}

		graphView.setHorizontalLabels(labelList.toArray(new String[labelList
				.size()]));
		graphView.addSeries(exampleSeries); // data

		LinearLayout _graph = (LinearLayout) v.findViewById(R.id.graph1);
		_graph.addView(graphView);
	}

	private List<GraphViewData> generateViewSeries(
			LinkedHashMap<String, Long> studyTimes) {
		// Look the line 42
		List<GraphViewData> graphViews = new ArrayList<GraphViewData>();
		// We must sort this hash for date (key)
		Integer index = 1;
		for (Long value : studyTimes.values()) {
			graphViews.add(new GraphViewData(index++, value));
		}
		Collections.reverse(graphViews);
		// From the line 35 to this, we only took the values of the hashmap and
		// convert it into an array to made the GraphViewSeries
		return graphViews;
	}
	
}
