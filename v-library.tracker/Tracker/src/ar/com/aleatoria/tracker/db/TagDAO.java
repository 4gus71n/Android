package ar.com.aleatoria.tracker.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ar.com.aleatoria.tracker.db.exception.DuplicatedTagEntryException;
import ar.com.aleatoria.tracker.db.exception.NotFoundException;
import ar.com.aleatoria.tracker.db.model.Tag;

public class TagDAO extends DAO<Tag> {

	public TagDAO(Context context) {
		super(context);
	}

	@Override
	protected Tag toObject(Cursor row) throws Exception {
		Tag tag = new Tag();
		tag.id = Long.parseLong(getColumnValue(row, "id"));
		tag.tag = getColumnValue(row, "tag");
		return tag;
	}

	@Override
	public ContentValues toValues(Tag object) {
		ContentValues values = new ContentValues();
		values.put("tag", object.tag);
		return values;
	}

	@Override
	public String getTableName() {
		return "tags";
	}

	@Override
	protected String sqlFields() {
		return "id integer primary key, " +
				"tag text ";
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists " + getTableName());
		onCreate(db);
	}

	/* Takes a list of Tag objects and persists those that don't exist yet.
	 * Leaves the same list populated with id-ed tags */
	public void persist(ArrayList<Tag> tags) {
		for (Tag tag: tags) {
			try {
				tag = get(" tag = \"" + tag.tag + "\"");
			} catch (NotFoundException e) {
				tag.id = add(tag);
			}
		}
	}

	public long getIdFrom(String tag) {
		ArrayList<Tag> results = this.getAll("tag = '"+tag+"'");
		if (results.size() > 1) {
			throw new DuplicatedTagEntryException();
		} else if (results.size() < 1 ) {
			//If the result is 0, its because this is a new type of tag
			return (long) this.rowCount();
		} else {
			return results.get(0).id;
		}
	}
}
