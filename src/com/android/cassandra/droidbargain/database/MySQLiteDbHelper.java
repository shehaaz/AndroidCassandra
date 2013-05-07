package com.android.nitelights.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class MySQLiteDbHelper {
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "NITELIGHTS";
	
	private static final String VENUE_TABLE = "venueTable";
	public static final String KEY_VENUE_ID = "_id";
	public static final String KEY_VENUE_TITLE = "venue_title";
	public static final String KEY_COMMITS = "num_commits";

	private static final String PERSON_TABLE = "personTable";
	public static final String KEY_PERSON_ID = "_id";
	public static final String KEY_PERSON_NAME = "person_name";
	public static final String KEY_COMMITTED_VENUE = "committed_venue";
	
	
	
	private DatabaseSetup ourDBSetup;
	private final Context ourContext;
	private SQLiteDatabase SQLiteDatabase;


	//Constructor
	public MySQLiteDbHelper(Context c){
		ourContext = c;
	}

	/*Methods*/
	public MySQLiteDbHelper open() throws SQLException{
		ourDBSetup = new DatabaseSetup(ourContext);
		SQLiteDatabase = ourDBSetup.getWritableDatabase();
		return this;
	}

	public void close(){
		ourDBSetup.close();
	}

	public long createVenueEntry(String title, String NumCommits) {

		ContentValues cv = new ContentValues();
		cv.put(KEY_VENUE_TITLE, title);
		cv.put(KEY_COMMITS, NumCommits);

		return SQLiteDatabase.insert(VENUE_TABLE, null, cv); //inserts puts to the database
	}


	public String getData() {

		String[] columns = new String[]{KEY_VENUE_ID, KEY_VENUE_TITLE, KEY_COMMITS};
		Cursor c = SQLiteDatabase.query(VENUE_TABLE, columns, null, null, null, null, null);
		String result = "";

		int iRow = c.getColumnIndex(KEY_VENUE_ID);
		int iName = c.getColumnIndex(KEY_VENUE_TITLE);
		int iHotness = c.getColumnIndex(KEY_COMMITS);

		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result = result + c.getString(iRow) + " " + c.getString(iName) + " " + c.getString(iHotness) + "\n";
		}
		return result;
	}


	/*SQLite Database*/
	private static class DatabaseSetup extends SQLiteOpenHelper{

		public DatabaseSetup(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL( 
					"CREATE TABLE " + VENUE_TABLE + " (" +
							KEY_VENUE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
							KEY_VENUE_TITLE + " TEXT NOT NULL, " +
							KEY_COMMITS + " TEXT NOT NULL);"
					);
			
			db.execSQL( 
					"CREATE TABLE " + PERSON_TABLE + " (" +
							KEY_PERSON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
							KEY_PERSON_NAME + " TEXT NOT NULL, " +
							KEY_COMMITTED_VENUE + " TEXT NOT NULL);"
					);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + VENUE_TABLE);
			onCreate(db);
		}
	}
}

