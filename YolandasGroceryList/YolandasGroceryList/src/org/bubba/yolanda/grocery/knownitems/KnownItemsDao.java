package org.bubba.yolanda.grocery.knownitems;

import java.util.ArrayList;
import java.util.List;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class KnownItemsDao
{
	// Database fields
	private SQLiteDatabase database;
	private KnownItemsSqlHelper dbHelper;
	private String[] allColumns = { KnownItemsSqlHelper.COLUMN_ID, 
			KnownItemsSqlHelper.COLUMN_ITEM };

	public KnownItemsDao(Context context)
	{
		dbHelper = new KnownItemsSqlHelper(context);
	}

	public void open() throws SQLException
	{
		database = dbHelper.getWritableDatabase();
	}

	public void close()
	{
		dbHelper.close();
	}

	public KnownItem createKnownItem(String item)
	{
		ContentValues values = new ContentValues();
		values.put(KnownItemsSqlHelper.COLUMN_ITEM, item);
		long insertId = database.insert(KnownItemsSqlHelper.TABLE_NAME, null, values);

		Cursor cursor = database.query(KnownItemsSqlHelper.TABLE_NAME, allColumns,
				KnownItemsSqlHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();

		KnownItem newKnownItem = cursorToItem(cursor);
		cursor.close();
		return newKnownItem;
	}

	public void deleteItem(KnownItem item)
	{
		long id = item.getId();
		database.delete(KnownItemsSqlHelper.TABLE_NAME, KnownItemsSqlHelper.COLUMN_ID + " = "
				+ id, null);
	}

	public List<KnownItem> getAllItems()
	{
		List<KnownItem> items = new ArrayList<KnownItem>();

		Cursor cursor = database.query(KnownItemsSqlHelper.TABLE_NAME, allColumns,
				null, null, null, null, KnownItemsSqlHelper.COLUMN_ITEM);
		cursor.moveToFirst();

		while (!cursor.isAfterLast())
		{
			KnownItem item = cursorToItem(cursor);
			items.add(item);
			cursor.moveToNext();
		}

		cursor.close();
		return items;
	}

	private KnownItem cursorToItem(Cursor cursor)
	{
		KnownItem item = new KnownItem();
		item.setId(cursor.getLong(0));
		item.setItem(cursor.getString(1));
		return item;
	}

	public SQLiteDatabase getDatabase()
	{
		return database;
	}

	public void setDatabase(SQLiteDatabase database)
	{
		this.database = database;
	}
}