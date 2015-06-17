package walla.dev.happynews;

import java.util.concurrent.ExecutionException;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class NewsList extends ActionBarActivity {

	private SQLiteDatabase database;
	private SparseArray<String> sa = null;
	private OnClickListener onclicklistener;
	private int id;
	private ContextThemeWrapper newContext;
	private LinearLayout ll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_list);
		System.out.println("onCreate");
		newContext = new ContextThemeWrapper(getBaseContext(),
				R.style.BorderlessButton);
		ll = (LinearLayout) findViewById(R.id.innerLinearLayout);
		sa = new SparseArray<String>();
		onclicklistener = new View.OnClickListener() {
			public void onClick(View v) {

				String url = sa.get(v.getId());
				System.out.println("Button clicked:" + url);
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(url));
				startActivity(intent);
			}
		};
		AsyncTask<Integer, Void, Void> at = new NewsQuerier(this);
		try {
			at.execute(getid(this)).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.news_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Toast.makeText(this, "Function currently not available", Toast.LENGTH_LONG).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume() {
		super.onResume();

		id = 1;
		SQLiteOpenHelper sqliteopenhelper = new SQliteList(this);
		database = sqliteopenhelper.getWritableDatabase();

		System.out.println("onStart called");
		System.out.println("Database open:" + database.isOpen());
		Cursor cursor = database.rawQuery("SELECT " + SQliteList.TITLE + ","
				+ SQliteList.URL + ","+ SQliteList.CATEGORY + " FROM " + SQliteList.tablename, null);

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			System.out.println("id:" + id);
			Button b = new Button(newContext);
			String title = cursor.getString(cursor
					.getColumnIndex(SQliteList.TITLE));
			String url = cursor
					.getString(cursor.getColumnIndex(SQliteList.URL));
			b.setText(title);
			b.setBackgroundColor(Color.WHITE);
			b.setTextColor(Color.DKGRAY);

			// stroke in order to divide each button
			View v = new View(this);
			v.setBackgroundColor(Color.LTGRAY);
			v.setMinimumHeight(1);
			// stroke finished

			b.setId(id);
			b.setOnClickListener(onclicklistener);
			ll.addView(b);
			ll.addView(v);
			sa.append(id, url);
			id++;
		}

	}

	@Override
	public void onPause() {
		super.onPause();
		System.out.println("onPause called");
		ll.removeAllViews();
		database.close();

	}

	private int getid(Context context) {
		SQLiteOpenHelper sqliteopenhelper = new SQliteList(this);
		SQLiteDatabase database = sqliteopenhelper.getWritableDatabase();
		System.out.println("Database open:" + database.isOpen());
		try {
			Cursor cursor = database.rawQuery("SELECT " + SQliteList.ID
					+ " FROM " + SQliteList.tablename, null);
			cursor.moveToLast();
			int id = cursor.getInt(cursor.getColumnIndex(SQliteList.ID));
			return id+1;
		} catch (Exception e) {

		}
		return 0;
	}
}
