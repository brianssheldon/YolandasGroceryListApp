package org.bubba.yolanda.grocery.list;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class GroceryListDao
{
	private SQLiteDatabase database;
	private GroceryListSqlHelper dbHelper;
	private String[] allColumns = { GroceryListSqlHelper.COLUMN_ID, 
			GroceryListSqlHelper.COLUMN_ITEM,
			GroceryListSqlHelper.COLUMN_QUANTITY};

	public GroceryListDao(Context context)
	{
		dbHelper = new GroceryListSqlHelper(context);
	}

	public void open() throws SQLException
	{
		database = dbHelper.getWritableDatabase();
		dbHelper.onCreate(database);
	}

	public void close()
	{
		dbHelper.close();
	}
	
	public GroceryItem createItem(String item, int quantity)
	{
		ContentValues values = new ContentValues();
		values.put(GroceryListSqlHelper.COLUMN_ITEM, item);
		values.put(GroceryListSqlHelper.COLUMN_QUANTITY, quantity);
		long insertId = database.insert(GroceryListSqlHelper.TABLE_NAME, null, values);
		
		Cursor cursor = database.query(GroceryListSqlHelper.TABLE_NAME, allColumns,
				GroceryListSqlHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();

		GroceryItem newGroceryItem = cursorToItem(cursor);
		cursor.close();
		return newGroceryItem;
	}

	public void deleteItem(GroceryItem item)
	{
		long id = item.getId();
//		int nbrOfRows = 
		database.delete(GroceryListSqlHelper.TABLE_NAME, GroceryListSqlHelper.COLUMN_ID + " = "
				+ id, null);
	}

	public void deleteAllItems()
	{
		database.delete(GroceryListSqlHelper.TABLE_NAME, GroceryListSqlHelper.COLUMN_ID + " > 0", null);
	}

	public void updateItem(GroceryItem item)
	{
		deleteItem(item);
		createItem(item.getItem(), item.getQuantity());
	}

	public List<GroceryItem> getAllItems()
	{
		List<GroceryItem> items = new ArrayList<GroceryItem>();
		
		Cursor cursor = database.query(GroceryListSqlHelper.TABLE_NAME, allColumns,
				null, null, null, null, GroceryListSqlHelper.COLUMN_ITEM);
		cursor.moveToFirst();

		while (!cursor.isAfterLast())
		{
			GroceryItem item = cursorToItem(cursor);
			items.add(item);
			cursor.moveToNext();
		}

		cursor.close();
		return items;
	}

	private GroceryItem cursorToItem(Cursor cursor)
	{
		GroceryItem item = new GroceryItem();
		item.setId(cursor.getLong(0));
		item.setItem(cursor.getString(1));
		item.setQuantity(cursor.getInt(2));
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