package ar.com.aleatoria.tracker.db;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ar.com.aleatoria.tracker.db.model.StudyTime;
import ar.com.aleatoria.tracker.util.ISO8601;

public class StudyTimeDAO extends DAO<StudyTime> {

	// StudyTime table name
	private static final String TABLE_STUDYTIME = "studytime";

	// StudyTime Table Columns names
	private static final String KEY_ID = "id";
	private static final String FIELD_START = "start";
	private static final String FIELD_FINISH = "finish";

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDYTIME);
		onCreate(db);
	}

	public StudyTimeDAO(Context context) {
		super(context);
	}

	public int studyTimesCountRows() throws NumberFormatException,
			ParseException {
		return getAll().size();
	}

	/**
	 * @param weekendDay
	 *            a single day
	 * @return Returns all the StudyTimes maded in a day
	 * @throws NumberFormatException
	 * @throws ParseException
	 */
	public List<StudyTime> getStudyTimesOfTheDate(Date weekendDay)
			throws NumberFormatException, ParseException {
		Calendar weekendDayCalendar = Calendar.getInstance();
		weekendDayCalendar.setTime(weekendDay);
		String weekendDayString = ISO8601.fromCalendar(weekendDayCalendar);
		List<StudyTime> studyTimeList = new ArrayList<StudyTime>();
		// Select only the raw with a starter time equals to the starter time of
		// weekendDayString (deprecating the time)
		String selectQuery = "SELECT  * FROM " + TABLE_STUDYTIME
				+ " WHERE ( date(" + FIELD_START + ") = date('"
				+ weekendDayString + "') )";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				StudyTime studyTime = toObject(cursor);
				studyTimeList.add(studyTime);
			} while (cursor.moveToNext());
		}
		// return studyTime list
		cursor.close();
		db.close();
		return studyTimeList;
	}

	@Override
	protected StudyTime toObject(Cursor row) throws ParseException {
		StudyTime studyTime = new StudyTime(ISO8601
				.toCalendar(row.getString(1)).getTime(), ISO8601.toCalendar(
				row.getString(2)).getTime());
		studyTime.id = Integer.parseInt(getColumnValue(row, "id"));
		return studyTime;
	}

	@Override
	public ContentValues toValues(StudyTime object) {
		ContentValues values = new ContentValues();

		Calendar start = Calendar.getInstance();
		start.setTime(object.getStartedTime());

		Calendar finish = Calendar.getInstance();
		finish.setTime(object.getFinishedTime());

		values.put(FIELD_START, ISO8601.fromCalendar(start));
		values.put(FIELD_FINISH, ISO8601.fromCalendar(finish));
		return values;
	}

	@Override
	public String getTableName() {
		return TABLE_STUDYTIME;
	}

	@Override
	protected String sqlFields() {
		return KEY_ID + " INTEGER PRIMARY KEY," + FIELD_START + " TEXT,"
				+ FIELD_FINISH + " TEXT ";
	}

	public List<StudyTime> getLastweek() throws ParseException {
		Calendar todayCalendar = Calendar.getInstance();
		List<StudyTime> result = new ArrayList<StudyTime>();
		for (int rollIndex = 0 ;  rollIndex < 7; rollIndex++) {
			Date today = todayCalendar.getTime();
			result.addAll(getStudyTimesOfTheDate(today));
			todayCalendar.roll(Calendar.DAY_OF_YEAR, false);
		}
		return result;
	}
}
