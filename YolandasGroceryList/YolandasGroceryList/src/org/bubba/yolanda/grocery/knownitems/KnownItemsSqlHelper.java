package org.bubba.yolanda.grocery.knownitems;

import org.bubba.yolanda.grocery.list.AbstractSqlHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class KnownItemsSqlHelper extends AbstractSqlHelper
{
	public static final String TABLE_NAME = "knownitems";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_ITEM = "item";

	public KnownItemsSqlHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
	    Log.w(KnownItemsSqlHelper.class.getName(),
	            "Upgrading database from version " + oldVersion + " to "
	                + newVersion + ", which will destroy all old data");
	        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	        onCreate(db);
	}
}