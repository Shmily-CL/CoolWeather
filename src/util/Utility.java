package util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.City;
import model.County;
import model.Province;
import android.R.integer;
import android.R.string;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import db.CoolWeatherDB;

//�����ʹ�����������ص����� -----��ʽ���£�����|���У�����|����,����|����

public class Utility {
	// �����ʹ�����������ص�ʡ������
	public synchronized static boolean handleProvincesResponse(
			CoolWeatherDB coolWeatherDB, String response) {
		if (!TextUtils.isEmpty(response)) {
			// �����пն�������~
			String[] allProvinces = response.split(",");
			if (allProvinces != null && allProvinces.length > 0) {
				for (String p : allProvinces) {
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);

					// �������������ݴ洢��Province����

					coolWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}

	// �����ʹ�����������ص��м�����
	public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,
			String response, int provinceId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allcities = response.split(",");
			if (allcities != null && allcities.length > 0) {
				for (String c : allcities) {
					String[] array = c.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);

					// ����������City���ݴ洢��City����

					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}

	// �����ʹ�����������ص��ؼ�����
	public static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB,
			String response, int cityId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allcounties = response.split(",");
			if (allcounties != null && allcounties.length > 0) {
				for (String c : allcounties) {
					String[] array = c.split("\\|");
					County county = new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);

					// ����������City���ݴ洢��County����
					coolWeatherDB.saveCounty(county);
				}
				return true;
			}
		}
		return false;
	}

	// �������������ص�JSON���ݣ����ҽ������������ݴ洢������
	// ��ʱ������{"weatherinfo":{"city":"��ɽ","cityid":"101190404","temp1":"4��","temp2":"16��","weather":"��ת����","img1":"n0.gif","img2":"d1.gif","ptime":"18:00"}}
	//���²������µ�����
	// {"HeWeather data service 3.0":
	//[{"aqi":{"city":{"aqi":"61","pm10":"62","pm25":"43","qlty":"��"}},
	//"basic":{"city":"��ɽ","cnty":"�й�","id":"CN101190404","lat":"31.320000","lon":"120.976000",
	//"update":{"loc":"2016-09-11 13:53","utc":"2016-09-11 05:53"}},
	//"daily_forecast":[{"astro":{"sr":"05:38","ss":"18:07"},
	//"cond":{"code_d":"300","code_n":"300","txt_d":"����","txt_n":"����"},
	//"date":"2016-09-11","hum":"83","pcpn":"2.1","pop":"95","pres":"1013",
	//"tmp":{"max":"27","min":"21"},"vis":"2","wind":{"deg":"177",
	//"dir":"���Ϸ�","sc":"3-4","spd":"13"}},{"astro":{"sr":"05:38","ss":"18:05"},
	//"cond":{"code_d":"101","code_n":"101","txt_d":"����","txt_n":"����"},
	//"date":"2016-09-12","hum":"58","pcpn":"0.1","pop":"0","pres":"1016",
	//"tmp":{"max":"29","min":"22"},"vis":"10","wind":{"deg":"51","dir":"����","sc":"3-4",
	//"spd":"14"}},{"astro":{"sr":"05:39","ss":"18:04"},"cond":{"code_d":"101","code_n":"302",
	//"txt_d":"����","txt_n":"������"},"date":"2016-09-13","hum":"66","pcpn":"0.1","pop":"34",
	//"pres":"1016","tmp":{"max":"28","min":"23"},"vis":"10","wind":{"deg":"73","dir":"������",
	//"sc":"3-4","spd":"16"}},{"astro":{"sr":"05:39","ss":"18:03"},"cond":{"code_d":"101","code_n":"302",
	//"txt_d":"����","txt_n":"������"},"date":"2016-09-14","hum":"68","pcpn":"0.4","pop":"72",
	//"pres":"1013","tmp":{"max":"30","min":"23"},"vis":"10","wind":{"deg":"76",
	//"dir":"������","sc":"΢��","spd":"5"}},{"astro":{"sr":"05:40","ss":"18:02"},
	//"cond":{"code_d":"306","code_n":"306","txt_d":"����","txt_n":"����"},"date":"2016-09-15","hum":"95",
	//"pcpn":"7.8","pop":"63","pres":"1009","tmp":{"max":"29","min":"24"},"vis":"7","wind":{"deg":"54",
	//"dir":"������","sc":"4-5","spd":"20"}},{"astro":{"sr":"05:40","ss":"18:00"},
	//"cond":{"code_d":"306","code_n":"306","txt_d":"����","txt_n":"����"},
	//"date":"2016-09-16","hum":"93","pcpn":"9.5","pop":"58","pres":"1003","tmp":{"max":"27","min":"22"},"vis":"7","wind":{"deg":"49","dir":"������","sc":"5-6","spd":"33"}},{"astro":{"sr":"05:41","ss":"17:59"},"cond":{"code_d":"101","code_n":"101","txt_d":"����","txt_n":"����"},"date":"2016-09-17","hum":"59","pcpn":"0.9","pop":"50","pres":"1008","tmp":{"max":"27","min":"19"},"vis":"9","wind":{"deg":"8","dir":"������","sc":"5-6","spd":"32"}}],"hourly_forecast":[{"date":"2016-09-11 16:00","hum":"88","pop":"95","pres":"1014","tmp":"26","wind":{"deg":"196","dir":"���Ϸ�","sc":"΢��","spd":"6"}},{"date":"2016-09-11 19:00","hum":"94","pop":"93","pres":"1014","tmp":"24","wind":{"deg":"192","dir":"���Ϸ�","sc":"΢��","spd":"5"}},{"date":"2016-09-11 22:00","hum":"96","pop":"37","pres":"1015","tmp":"22","wind":{"deg":"171","dir":"�Ϸ�","sc":"΢��","spd":"4"}}],"now":{"cond":{"code":"104","txt":"��"},"fl":"27","hum":"66","pcpn":"0.1","pres":"1014","tmp":"23","vis":"5","wind":{"deg":"190","dir":"���Ϸ�","sc":"3-4","spd":"16"}},"status":"ok","suggestion":{"comf":{"brf":"������","txt":"�����н��꣬����ʹ���Ǹо���Щ�ȣ������󲿷����Ի��бȽ����ʵĸо���"},"cw":{"brf":"����","txt":"����ϴ����δ��24Сʱ�����꣬����ڴ��ڼ�ϴ������ˮ��·�ϵ���ˮ���ܻ��ٴ�Ū�����İ�����"},"drsg":{"brf":"����","txt":"�����ų���T���������ӵ���ȷ�װ������������������֯�����������׺ͳ��㡣"},"flu":{"brf":"�ٷ�","txt":"���������������ˣ������Խ��¹��̣�������ð���ʽϵ͡�"},"sport":{"brf":"�ϲ���","txt":"�н�ˮ���ҷ�����ǿ����ѹ�ϵͣ��Ƽ��������ڽ��е�ǿ���˶�������ֻ����˶�����ע�������硣"},"trav":{"brf":"����","txt":"�н�ˮ���¶����ˣ���ϸ�����������һ��������ɲ�Ҫ��������ϣ����ǵó���ҪЯ����ߡ�"},"uv":{"brf":"��","txt":"������ǿ�Ƚ������������ǰͿ��SPF��12-15֮�䡢PA+�ķ�ɹ����Ʒ��"}}}]}
	public static void handleWeatherResponse(Context context, String response) {
		try {
			String temp = new String(response.getBytes(), "utf-8");// ��������utf-8�ı��뷽ʽ��ȡ����
			JSONObject dataOfJson = new JSONObject(temp);
			JSONArray heWeatherArray = dataOfJson
					.getJSONArray("HeWeather data service 3.0");
			JSONObject heWeatherArray0 = heWeatherArray.getJSONObject(0);
			JSONObject basic = heWeatherArray0.getJSONObject("basic");
			
			String cityName = basic.getString("city");
			String weatherCode0 = basic.getString("id");
			String weatherCode = weatherCode0.substring(2, 11);
			
			JSONObject updateJson = basic.getJSONObject("update");
			String publishTime0 = updateJson.getString("loc");
			String publishTime = publishTime0.substring(11, 16);
			
			JSONArray daily_forecast = heWeatherArray0
					.getJSONArray("daily_forecast");
			JSONObject daily_forecast0 = daily_forecast.getJSONObject(0);
			JSONObject cond = daily_forecast0.getJSONObject("cond");
			String txt_d = cond.getString("txt_d");
			String txt_n = cond.getString("txt_n");
			
			String weatherDiscribe;
			if (txt_d.equals(txt_n)) {
				weatherDiscribe = txt_d;
			} else {
				weatherDiscribe = txt_d + "ת" + txt_n;
			}
			
			JSONObject tmp = daily_forecast0.getJSONObject("tmp");
			String temp10 = tmp.getString("min");
			String temp1 = temp10 + "��";
			String temp20 = tmp.getString("max");
			String temp2 = temp20 + "��";
			Log.d("Utility", cityName + weatherCode + temp1 + temp2
					+ weatherDiscribe + publishTime);
			saveWeatherInfo(context, cityName, weatherCode, temp1, temp2,
					weatherDiscribe, publishTime);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// ��ʱ�����ݽ�����ʽ
		// try {
		// JSONObject jsonObject = new JSONObject(response);
		// JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
		// String cityname = weatherInfo.getString("city");
		// String weathercode = weatherInfo.getString("cityid");
		// String temp1 = weatherInfo.getString("temp1");
		// String temp2 = weatherInfo.getString("temp2");
		// String weatherDesp = weatherInfo.getString("weather");
		// String publishTime = weatherInfo.getString("ptime");
		//
		// saveWeatherInfo(context, cityname, weathercode, temp1, temp2,
		// weatherDesp, publishTime);
		//
		// } catch (JSONException e) {
		// // TODO: handle exception
		// e.printStackTrace();
		// }

	}

	private static void saveWeatherInfo(Context context, String cityname,
			String weathercode, String temp1, String temp2, String weatherDesp,
			String publishTime) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��M��d��", Locale.CHINA);

		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(context).edit();

		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityname);
		editor.putString("weather_code", weathercode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishTime);
		editor.putString("current_date", sdf.format(new Date()));

		editor.commit();
	}
}
