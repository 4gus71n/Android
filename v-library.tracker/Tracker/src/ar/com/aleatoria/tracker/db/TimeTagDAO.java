package ar.com.aleatoria.tracker.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ar.com.aleatoria.tracker.db.model.TimeTag;

public class TimeTagDAO extends DAO<TimeTag> {


	public TimeTagDAO(Context context) {
		super(context);
	}

	@Override
	protected TimeTag toObject(Cursor row) throws Exception {
		TimeTag tag = new TimeTag();
		tag.id = Long.parseLong(getColumnValue(row, "id"));
		tag.time = Long.parseLong(getColumnValue(row, "time"));
		tag.tag = Long.parseLong(getColumnValue(row, "tag"));
		return tag;
	}

	@Override
	public String getTableName() {
		return "time_tags";
	}

	@Override
	protected String sqlFields() {
		return "id integer primary key, "
				+ "time integer references studytime(id), "
				+ "tag integer references tags(id) ";
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists " + getTableName());
		onCreate(db);
	}
	

	@Override
	public ContentValues toValues(TimeTag object) {
		ContentValues values = new ContentValues();
		values.put("time", "" + object.time);
		values.put("tag", "" + object.tag);
		return values;
	}	
}
