package walla.dev.happynews;

import java.util.concurrent.TimeUnit;
import android.database.sqlite.SQLiteDatabase;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

public class BackgroundService extends Service{
	Context globalcontext;
	public void onCreate() {
		globalcontext=getApplicationContext();
		
		
		new Thread(new Runnable() {
		    public void run() {
		      query();
		    }
		  }).start();

	}
	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
        
    }

	private void query(){
		
		AsyncTask<Integer, Void, Void> at=new NewsQuerier(globalcontext);
		at.execute(getid(globalcontext));
		try {
			TimeUnit.MINUTES.sleep(1);//Normalerweise Minutes nur Testweise Seconds
			query();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private int getid(Context context){
		SQLiteOpenHelper sqliteopenhelper = new SQliteList(this);
		SQLiteDatabase database = sqliteopenhelper.getWritableDatabase();
				System.out.println("Database open:" + database.isOpen());
		Cursor cursor = database.rawQuery("SELECT " + SQliteList.ID
				+ " FROM " + SQliteList.tablename, null);
		cursor.moveToLast();
		int id=cursor.getInt(cursor.getColumnIndex(SQliteList.ID));
		return id+1;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
