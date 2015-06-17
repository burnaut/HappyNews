package walla.dev.happynews;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

public class NewsQuerier extends AsyncTask<Integer, Void, Void> {
	Context globalcontext;

	public NewsQuerier(Context c) {
		globalcontext = c;
	}

	@Override
	protected Void doInBackground(Integer... params) {
		neuersqlclient(params);
		return null;
	}

	private void neuersqlclient(Integer... params) {
		Connection con = null;
		Statement stmnt = null;
		ResultSet rs = null;
		int columnTitle = 3;
		int columnURL = 2;
		int columnID = 1;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://veteran1.ez.lv",
					"newsquerier", "NitsaYAiF1337");// pw als hash speichern
			stmnt = con.createStatement();
			String query = "SELECT * FROM news";
			stmnt.executeQuery("USE happynews");
			rs = stmnt.executeQuery(query);
			while (rs.next()) {
				
				int id = rs.getInt(columnID);
				
				if(id>=params[0]){
				String title = rs.getString(columnTitle);
				String url = rs.getString(columnURL);
				storeindatabase(id, title, url);
				createNotification(id, title, url);
				}
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}

	}

	private void storeindatabase(int id, String title, String url) {
		SQLiteOpenHelper sqliteopenhelper = new SQliteList(globalcontext);
		SQLiteDatabase database = sqliteopenhelper.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(SQliteList.TITLE, title);
		values.put(SQliteList.URL, url);
		values.put(SQliteList.ID, id);
		database.insert(SQliteList.tablename, null, values);
	}

	private void createNotification(int id, String title, String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));
		PendingIntent pintent = PendingIntent.getActivity(globalcontext, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(
				globalcontext);
		nBuilder.setSmallIcon(R.drawable.ic_launcher);
		nBuilder.setContentTitle("HappyNews");
		nBuilder.setContentText(title);
		nBuilder.setSound(RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
		nBuilder.setContentIntent(pintent);
		NotificationManager notificationManager = (NotificationManager) globalcontext
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(id, nBuilder.build());
	}

}
