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
	// Database fields
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
	}

	public void close()
	{
		dbHelper.close();
	}
	
//	public void deleteItem(GroceryItem item)
//	{
//		List<GroceryItem> allItems = getAllItems();
//		
//		if(allItems == null || id >= allItems.size() || id < 0)
//		{
//			return;
//		}
//		
//		GroceryItem newGroceryItem = allItems.get(id);
//		deleteItem(item);
//	}
	
	public GroceryItem createItem(String item)
	{
		ContentValues values = new ContentValues();
		values.put(GroceryListSqlHelper.COLUMN_ITEM, item);
		values.put(GroceryListSqlHelper.COLUMN_QUANTITY, 1);
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
		System.out.println("Item deleted with id: " + id + "  item: " + item);
		database.delete(GroceryListSqlHelper.TABLE_NAME, GroceryListSqlHelper.COLUMN_ID + " = "
				+ id, null);
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
		// Make sure to close the cursor
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