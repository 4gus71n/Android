package ar.com.aleatoria.ue;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import ar.com.aleatoria.ue.dao.Subject;

public class SubjectViewAdapter extends ArrayAdapter<Subject> {
	private final Context context;
	private final Subject[] values;

	public boolean checkable = false;

	public SubjectViewAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		this.context = context;
		this.values = null;
	}

	public SubjectViewAdapter(Context context, int textViewResourceId,
			List<Subject> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.values = (Subject[]) objects.toArray(new Subject[objects.size()]);
	}

	// TODO: Reimplement using this recipe:
	// http://stackoverflow.com/a/1917861/147072
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.subject_view, parent, false);
		if (!checkable) {
			CheckBox cb = (CheckBox) v.findViewById(R.id.subject_checkbox);
			cb.setVisibility(View.INVISIBLE);
		}
		TextView description = (TextView) v
				.findViewById(R.id.subject_description);
		description.setText(values[position].getName());
		return v;
	}
}
