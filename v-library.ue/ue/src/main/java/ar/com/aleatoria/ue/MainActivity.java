package ar.com.aleatoria.ue;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Hours;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Toast;
import ar.com.aleatoria.ue.dao.DaoMaster;
import ar.com.aleatoria.ue.dao.InstantDao;
import ar.com.aleatoria.ue.dao.Instant;
import ar.com.aleatoria.ue.dao.Subject;
import ar.com.aleatoria.ue.dao.SubjectDao;
import ar.com.aleatoria.ue.dao.DaoMaster.DevOpenHelper;
import ar.com.aleatoria.ue.dao.DaoSession;
import ar.com.aleatoria.ue.stuff.ViewId;
import ar.com.aleatoria.ue.widget.InstantScrollView;
import ar.com.aleatoria.ue.widget.InstantWidget;
import ar.com.aleatoria.ue.widget.InstantWidget.State;

public class MainActivity extends Activity {
	private SQLiteDatabase db;

	private DaoMaster daoMaster;
	private DaoSession daoSession;
	private SubjectDao subjectDao;
	private InstantDao instantDao;

	private List<Instant> allInstants;
	private List<Subject> allSubjects;
	private SubjectViewAdapter adapter;
	private InstantWidget selectedWidget;
	
	private static String TAG = "ue";

	/**
	 * Called when the activity is first created.
	 * 
	 * @param savedInstanceState
	 *            If the activity is being re-initialized after previously being
	 *            shut down then this Bundle contains the data it most recently
	 *            supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it
	 *            is null.</b>
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		setContentView(R.layout.main);
		
		changeSaveButtonVisibility(View.GONE);
		initDao();
		
		loadSubjects();
		adapter = new SubjectViewAdapter(this.getApplicationContext(),
				R.layout.subject_view, allSubjects);
		ListView lv = (ListView) findViewById(R.id.subject_list);
		lv.setAdapter(adapter);
		
		loadInstants();
		generateItems();
		InstantScrollView sv = (InstantScrollView) findViewById(R.id.instant_scroll);
		sv.fullScrollOnLayout(InstantScrollView.FOCUS_RIGHT);
	}

	private void changeSaveButtonVisibility(int visibility) {
		Button saveButton = (Button) findViewById(R.id.ok_button);
		saveButton.setVisibility(visibility);
	}

	private void loadSubjects() {
		if (subjectDao.count() > 0) {
			allSubjects = subjectDao.loadAll();
			return;
		}
		String[] subjects = { 
			"Matemática", "Teología", "Educación física", "Biología molecular",
			"Paleontología", "Literatura irlandesa del Siglo XIX"
		};
		allSubjects = new ArrayList<Subject>();
		for (int i = 0; i < 6; i++) {
			Subject s = new Subject(null, subjects[i]);
			allSubjects.add(s);
		}
		subjectDao.insertInTx(allSubjects);
	}

	public void loadInstants() {
		// El service sabría a qué día corresponde su lista de 24 instantes
		DateMidnight today = new DateMidnight(DateMidnight.now());
		List<Instant> instants = instantDao
				.queryBuilder()
				.where(InstantDao.Properties.Date_created.ge(today.getMillis()))
				.list();
		if (instants.size() == 0) {
			for (int i = 0; i < 24; i++) {
				DateTime instantTime = new DateTime(today.toDateTime().plus(
						Hours.hours(i)));
				Instant instant = new Instant(null, instantTime.toDate(), null,
						false, null);
				instants.add(instant);
			}
			// Persist
			instantDao.insertInTx(instants);
		}
		this.allInstants = instants;
	}

	public void generateItems() {
		LinearLayout container = (LinearLayout) findViewById(R.id.instant_container);
		int instantsToShow = DateTime.now().getHourOfDay() + 1;
		for (int i = 0; i < instantsToShow; i++) {
			InstantWidget instant = new InstantWidget(this);
			Drawable box = getResources().getDrawable(R.drawable.instant);
			instant.setId(ViewId.getInstance().getUniqueId());
			instant.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			instant.setText(i + "hs");
			instant.setButtonDrawable(box);
			instant.setBoundInstant(allInstants.get(i));
			setOnClickListener(instant);

			container.addView(instant);
		}
	}

	public void setOnClickListener(InstantWidget instant) {
		instant.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				selectedWidget = (InstantWidget) v;
				setCheckable(true);
			}
		});
	}

	public void saveButtonClick(View v) {
		registerInstant(selectedWidget);
	}
	
	// TODO: Show selected instantWidget, so the user can change the selection
	protected void setCheckable(boolean checkable) {
		adapter.checkable = checkable;
		adapter.notifyDataSetChanged();
		if (checkable)
			changeSaveButtonVisibility(View.VISIBLE);
		else
			changeSaveButtonVisibility(View.GONE);
	}

	protected void registerInstant(InstantWidget widget) {
		if ((widget.getState() == State.checked)
				|| (widget.getState() == State.failed))
			return;

		setCheckable(false);
		// Here goes handling of subjects
		
		long id = widget.getBoundInstant().getId();
		Instant instant = instantDao.load(id);

		DateTime time = DateTime.now();
		instant.setDate_registered(time.toDate());
		instantDao.updateInTx(instant);

		// Why aren't you updating yourself
		widget.setBoundInstant(instant);
		widget.postInvalidate();

		Toast.makeText(getApplicationContext(), "Instante registrado",
				Toast.LENGTH_SHORT).show();
	}

	private void initDao() {
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "ue.db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();

		subjectDao = daoSession.getSubjectDao();
		instantDao = daoSession.getInstantDao();
	}

}
