package com.hitasoft.apps.milymarket.util;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBController extends SQLiteOpenHelper {
	private static final String LOGCAT = null;

	public DBController(Context applicationcontext) {
		super(applicationcontext, "fantacySqliteDb.db", null, 1);
		Log.d(LOGCAT, "Created");
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		String query;
		query = "CREATE TABLE ItemDetails ( itemId INTEGER PRIMARY KEY,Data TEXT,Image BLOB)";
		database.execSQL(query);
		Log.d(LOGCAT, "bookmark Created");
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int version_old,
			int current_version) {
		String query;
		query = "DROP TABLE IF EXISTS ItemDetails";
		database.execSQL(query);
		onCreate(database);
	}

	public void insertItems(ImageData imagedata) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("itemId", imagedata.getId());
		values.put("Data", imagedata.getData());
		values.put("Image", imagedata.getImage());
		database.insert("ItemDetails", null, values);
		database.close();
	}

	public void deleteItem(String id) {
		Log.d(LOGCAT, "delete");
		SQLiteDatabase database = this.getWritableDatabase();
		String deleteQuery = "DELETE FROM  ItemDetails where itemId=" + id
				+ ";";
		Log.d("query", deleteQuery);
		database.execSQL(deleteQuery);
	}

	public void insertImage(int id, byte[] image) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("Image", image);
		database.insert("ItemDetails", null, values);
		String deleteQuery = "UPDATE ItemDetails SET Image=" + image
				+ " where itemId=" + id + ";";
		Log.d("query", deleteQuery);
		database.execSQL(deleteQuery);
		database.close();
	}

	public List<ImageData> getAllItems(String id, String id2) {
		List<ImageData> itemDetails = new ArrayList<ImageData>();
		String selectQuery = "SELECT  * FROM ItemDetails ;";
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				// HashMap<String, String> map = new HashMap<String, String>();
				// map.put("userId", cursor.getString(0));
				// map.put("BookId", cursor.getString(1));
				// map.put("PageId", cursor.getString(2));
				ImageData data = new ImageData();
				data.setId(cursor.getInt(0));
				data.setData(cursor.getString(1));
				data.setImage(cursor.getBlob(2));
				itemDetails.add(data);
			} while (cursor.moveToNext());
		}

		if (database != null) {
			database.close();
			cursor.close();
		}
		// return contact list
		return itemDetails;
	}

}
