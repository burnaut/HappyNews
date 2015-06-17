package walla.dev.happynews;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver{
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
		context.startService(new Intent(context,BackgroundService.class));
		//hier dann intent starten, welches am server überprüft ob neue daten vorhanden sind
		}
	}
	
}