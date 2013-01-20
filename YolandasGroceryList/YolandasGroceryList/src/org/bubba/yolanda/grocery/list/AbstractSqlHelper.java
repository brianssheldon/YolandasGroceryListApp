package org.bubba.yolanda.grocery.list;

import org.bubba.yolanda.grocery.knownitems.KnownItemsSqlHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class AbstractSqlHelper extends SQLiteOpenHelper
{

	protected static final String DATABASE_NAME = "yolandagrocerylist.db";
	protected static final int DATABASE_VERSION = 1;

	private static final String TABLE_CREATE_KNOWN_ITEMS = "create table if not exists "
			+ KnownItemsSqlHelper.TABLE_NAME + "(" 
				+ KnownItemsSqlHelper.COLUMN_ID + " integer primary key autoincrement, " 
				+ KnownItemsSqlHelper.COLUMN_ITEM + " text not null);";
	
	private static final String TABLE_CREATE_GROCERY_LIST = "create table if not exists "
			+ GroceryListSqlHelper.TABLE_NAME + "(" 
				+ GroceryListSqlHelper.COLUMN_ID + " integer primary key autoincrement, "
				+ GroceryListSqlHelper.COLUMN_ITEM + " text not null, "
				+ GroceryListSqlHelper.COLUMN_QUANTITY + " text not null);";
	

	public AbstractSqlHelper(Context context, String name, CursorFactory factory, int version)
	{
		super(context, name, factory, version);
	}
	
	public void onCreate(SQLiteDatabase db)
	{
//      db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		db.execSQL(TABLE_CREATE_GROCERY_LIST);
		db.execSQL(TABLE_CREATE_KNOWN_ITEMS);
	}
}