package activity;

//"http://www.weather.com.cn/data/list3/city"+ countyCode + ".xml";
//指定县级天气查询结果是----县级代号|天气代号

//

import util.HttpCallbackListener;
import util.HttpUtil;
import util.Utility;

import com.example.coolweather.R;
import com.example.coolweather.R.id;
import com.example.coolweather.R.layout;
import com.example.coolweather.R.menu;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity implements OnClickListener {
	private LinearLayout weatherInfoLayout;

	// 用于显示城市名字
	private TextView cityNameText;

	// 用于显示发布时间
	private TextView publishText;

	// 用于显示天气的描述信息
	private TextView weatherDespText;

	// 用于显示气温1
	private TextView temp1Text;

	// 用于显示气温2
	private TextView temp2Text;

	// 用于显示当前日期
	private TextView currentDataText;

	// 切换城市按钮
	private Button switchCity;

	// 跟新天气按钮
	private Button refreshWeather;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);

		// 初始话控件
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		cityNameText = (TextView) findViewById(R.id.city_name);
		publishText = (TextView) findViewById(R.id.publish_text);
		weatherDespText = (TextView) findViewById(R.id.weather_desp);
		temp1Text = (TextView) findViewById(R.id.temp1);
		temp2Text = (TextView) findViewById(R.id.temp2);

		currentDataText = (TextView) findViewById(R.id.current_date);

		switchCity = (Button) findViewById(R.id.switch_city);
		refreshWeather = (Button) findViewById(R.id.refresh_weather);
		
		switchCity.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);

		String countyCode = getIntent().getStringExtra("county_code");

		if (!TextUtils.isEmpty(countyCode)) {
			// 存在县级代号，就去查询天气
			publishText.setText("同步中......");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);

			queryWeatherCode(countyCode);
		} else {
			// 没有县级代号时候就显示本地的天气
			showWeather();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.switch_city:
			Intent intent = new Intent(this, ChooseAreaActivity.class);
			intent.putExtra("From_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh_weather:
			publishText.setText("同步中.......");
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(this);
			String weatherCode = prefs.getString("weather_code", "");
			if (!TextUtils.isEmpty(weatherCode)) {
				queryWeatherInfo(weatherCode);
			}
			break;
		default:
			break;
		}
	}

	// 查询县级代号对应的天气代号
	private void queryWeatherCode(String countyCode) {
		// TODO Auto-generated method stub
		String address = "http://www.weather.com.cn/data/list3/city"
				+ countyCode + ".xml";
		queryFromServer(address,"countyCode");
	}

	// 查询天气代号所对应的天气信息
	private void queryWeatherInfo(String weatherCode) {
		// TODO Auto-generated method stub
		String address = "http://api.heweather.com/x3/weather?cityid=CN"
				+ weatherCode + "&key=3e9f4017110547f2b74237fdc7152f38";
		//String address = "http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
		queryFromServer(address, "weatherCode");
	}

	private void queryFromServer(final String address, final String type) {
		// TODO Auto-generated method stub
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

			@Override
			public void onFinish(final String response) {
				// TODO Auto-generated method stub
				if ("countyCode".equals(type)) {
					if (!TextUtils.isEmpty(response)) {
						String[] array = response.split("\\|");
						if (array != null && array.length == 2) {
							String weatherCode = array[1];
							queryWeatherInfo(weatherCode);
						}
					}
				} else if ("weatherCode".equals(type)) {
					Utility.handleWeatherResponse(WeatherActivity.this,
							response);
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							showWeather();
						}
					});
				}
			}

			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						publishText.setText("同步失败");
					}
				});
			}
		});
	}

	private void showWeather() {
		// TODO Auto-generated method stub
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
		cityNameText.setText(sharedPreferences.getString("city_name", ""));
		temp1Text.setText(sharedPreferences.getString("temp1", ""));
		temp2Text.setText(sharedPreferences.getString("temp2", ""));
		
		weatherDespText.setText(sharedPreferences.getString("weather_desp", ""));
		weatherDespText.setVisibility(View.VISIBLE);
		
		publishText.setText("今天"+sharedPreferences.getString("publish_time","" )+"发布");
		
		
		currentDataText.setText(sharedPreferences.getString("current_date", ""));
		currentDataText.setVisibility(View.VISIBLE);
		
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
		//Intent intent =new Intent(this,AutoUpdateService.class);
		//startService(intent);
	}

}
