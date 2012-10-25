package org.bubba.yolanda.grocery.list;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class GroceryListSqlHelper extends AbstractSqlHelper
{
	public static final String TABLE_NAME = "groceryList";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_ITEM = "item";
	public static final String COLUMN_QUANTITY = "quantity";

	// Database creation sql statement
//	private static final String TABLE_CREATE = "create table "
//			+ TABLE_NAME + "(" 
//				+ COLUMN_ID + " integer primary key autoincrement, "
//				+ COLUMN_ITEM + " text not null, "
//				+ COLUMN_QUANTITY + " text not null);";

	public GroceryListSqlHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{	// moved to abstract class & created by knownITems table
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
//        db.execSQL(TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
	    Log.w(GroceryListSqlHelper.class.getName(),
	            "Upgrading database from version " + oldVersion + " to "
	                + newVersion + ", which will destroy all old data");
	        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	        onCreate(db);
	}
}