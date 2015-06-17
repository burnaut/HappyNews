package walla.dev.happynews;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQliteList extends SQLiteOpenHelper{
	SQLiteDatabase db;
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME="HappyNews";
    private String LIST_TABLE_CREATE =null;
    public static final String tablename="NewsTable";
    public static final String ID="ID";
    public static final String URL="URL";
    public static final String TITLE="TITLE";
    public static final String CATEGORY="CATEGORY";
    
	public SQliteList(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		LIST_TABLE_CREATE ="CREATE TABLE "+tablename+"("+ID+" INTEGER,"+URL+" TEXT,"+TITLE+" TEXT,"+CATEGORY+" TEXT"+");";
    	try{
        db=getWritableDatabase(); //activate those two lines to actually create the table
        onCreate(db);
    	}catch(SQLiteException e){
    		
    	}
        Log.d("SQLite Database", "Database List_Table created");
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(LIST_TABLE_CREATE);
        Log.d("SQLite Database", "Table actually created");
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				
	}

}
