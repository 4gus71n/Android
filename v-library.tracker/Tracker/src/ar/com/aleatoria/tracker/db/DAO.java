package ar.com.aleatoria.tracker.db;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import ar.com.aleatoria.tracker.db.exception.IdNotFoundException;
import ar.com.aleatoria.tracker.db.exception.NotFoundException;
import ar.com.aleatoria.tracker.db.exception.ToObjectConversionException;
import ar.com.aleatoria.tracker.db.model.Model;

public abstract class DAO<T extends Model> extends SQLiteOpenHelper {
	// All Static variables
	// Database Version
	private static int DATABASE_VERSION = 1;

	// Database Name
	private static String DATABASE_NAME = "Akrasia.db";

	public DAO(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	abstract protected T toObject(Cursor row) throws Exception;

	abstract public ContentValues toValues(T object);

	abstract public String getTableName();

	abstract protected String sqlFields();

	@Override
	public void onCreate(SQLiteDatabase db) {
		/*
		 * TODO: This method must create the tables for all DAOs as it's only
		 * executed once.
		 */
		LinkedHashMap<String, String> tables = new LinkedHashMap<String, String>();
		tables.put("studytime",
				"id integer primary key, start text, finish text ");
		tables.put("tags", "id integer primary key, tag text ");
		tables.put("time_tags",
				"id integer primary key, time integer references studytime(id), "
						+ "tag integer references tags(id) ");

		for (String key : tables.keySet()) {
			String create = "create table " + key + " (" + tables.get(key) + ")";
			db.execSQL(create);
		}
	}

	public long add(T object) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = toValues(object);
		long id = db.insert(getTableName(), null, values);
		object.id = id;
		db.close();
		return id;
	}

	public T get(int id) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(getTableName(), null, "id =? ",
				new String[] { "" + id }, null, null, null);
		if (cursor == null)
			throw new IdNotFoundException();
		cursor.moveToFirst();
		try {
			return toObject(cursor);
		} catch (Exception e) {
			throw new ToObjectConversionException(e);
		} finally {
			cursor.close();
			db.close();
		}
	}

	public T get(String where) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(getTableName(), null, where, null, null, null,
				null);
		if (cursor.getCount() == 0)
			throw new NotFoundException();
		cursor.moveToFirst();
		try {
			return toObject(cursor);
		} catch (Exception e) {
			throw new ToObjectConversionException(e);
		} finally {
			cursor.close();
			db.close();
		}
		
	}

	public List<T> getAll() {
		return getAll("");
	}
	
	public Integer rowCount() {
		return this.getAll().size();
	}
	
	public ArrayList<T> getAll(String where) {
		ArrayList<T> result = new ArrayList<T>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(getTableName(), null, where, null, null, null,
				null);
		try {
			cursor.moveToFirst();

			while (!cursor.isAfterLast()) {
				result.add(toObject(cursor));
				cursor.moveToNext();
			}

		} catch (Exception e) {
			throw new ToObjectConversionException(e);
		} finally {
			cursor.close();
			db.close();
		}
		return result;
	}

	public void delete(int id) {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(getTableName(), "id =? ", new String[] { "" + id });
		db.close();
	}

	public void update(T object) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = toValues(object);
		db.update(getTableName(), values, "id =? ", new String[] { ""
				+ object.id });
		db.close();
	}

	public String getColumnValue(Cursor row, String column) {
		return row.getString(row.getColumnIndex(column));
	}
}
