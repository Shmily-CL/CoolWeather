package util;

import java.util.Iterator;

import model.City;
import model.County;
import model.Province;

import android.R.integer;
import android.R.string;
import android.text.TextUtils;
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
					
					//����������City���ݴ洢��City����
					
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
						
						//����������City���ݴ洢��County����
						coolWeatherDB.saveCounty(county);
					}
					return true;
				}
			}
			return false;
		}

}
