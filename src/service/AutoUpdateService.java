package service;

import receiver.AutoUpdateReceiver;
import util.HttpCallbackListener;
import util.HttpUtil;
import util.Utility;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

public class AutoUpdateService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				updateWeather();
			}
		}).start();

		AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);

		int anHour = 6 * 60 * 60 * 1000; // 两小时的毫秒数

		long triggerTime = SystemClock.elapsedRealtime() + anHour;

		Intent intent2 = new Intent(this, AutoUpdateReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
				intent2, 0);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime,
				pendingIntent);

		return super.onStartCommand(intent, flags, startId);
	}

	private void updateWeather() {
		// TODO Auto-generated method stub
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		String weatherCode = prefs.getString("weather_code", "");
		String address = "http://api.heweather.com/x3/weather?cityid=CN"
				+ weatherCode + "&key=3e9f4017110547f2b74237fdc7152f38";
		
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				Utility.handleWeatherResponse(AutoUpdateService.this, response);
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				e.printStackTrace();
			}
		});
	}
}
