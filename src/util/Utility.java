package util;

import java.util.Iterator;

import model.City;
import model.County;
import model.Province;

import android.R.integer;
import android.R.string;
import android.text.TextUtils;
import db.CoolWeatherDB;

//解析和处理服务器返回的数据 -----格式如下：代号|城市，代号|城市,代号|城市

public class Utility {
	// 解析和处理服务器返回的省级数据
	public synchronized static boolean handleProvincesResponse(
			CoolWeatherDB coolWeatherDB, String response) {
		if (!TextUtils.isEmpty(response)) {
			// 两种判空都进行了~
			String[] allProvinces = response.split(",");
			if (allProvinces != null && allProvinces.length > 0) {
				for (String p : allProvinces) {
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);

					// 解析出来的数据存储到Province表中

					coolWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}

	// 解析和处理服务器返回的市级数据
	public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,
			String response, int provinceId) {
		if(!TextUtils.isEmpty(response))
		{
			String [] allcities = response.split(",");
			if (allcities != null && allcities.length > 0) {
				for(String c : allcities)
				{
					String[] array = c.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					
					//解析出来的City数据存储到City表中
					
					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}
	
	
	// 解析和处理服务器返回的县级数据
		public static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB,
				String response, int cityId) {
			if(!TextUtils.isEmpty(response))
			{
				String [] allcounties = response.split(",");
				if (allcounties != null && allcounties.length > 0) {
					for(String c : allcounties)
					{
						String[] array = c.split("\\|");
						County county = new County();
						county.setCountyCode(array[0]);
						county.setCountyName(array[1]);
						county.setCityId(cityId);
						
						//解析出来的City数据存储到County表中
						coolWeatherDB.saveCounty(county);
					}
					return true;
				}
			}
			return false;
		}

}
