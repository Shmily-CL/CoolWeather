package util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
	public static void sendHttpRequest(final String address,
			final HttpCallbackListener listener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpURLConnection connection = null;

				try {
					URL url = new URL(address);

					connection = (HttpURLConnection) url.openConnection();

					connection.setRequestMethod("GET");
					connection.setConnectTimeout(10000);
					connection.setReadTimeout(10000);

					InputStream in = connection.getInputStream();
					
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					
					StringBuilder response = new StringBuilder();
					
					String line;
					
					while((line = reader.readLine()) != null)
					{
						response.append(line);
					}
					
					if (listener != null) {
						listener.onFinish(response.toString());
					}
				} catch (Exception e) {
					// TODO: handle exception
					if(listener != null){
						
						listener.onError(e);
						
					}
				}
				finally{
					if(connection != null)
					{
						connection.disconnect();
					}
				}
			}
		}).start();
	}
}

//public class HttpUtil {
//
//	public static void sendHttpRequest(final String address,
//			final HttpCallbackListener listener) {
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				HttpURLConnection connection = null;
//				try {
//
//					URL url = new URL(address);
//					connection = (HttpURLConnection) url.openConnection();
//					// ����HTTP������ʽ
//					connection.setRequestMethod("GET");
//					// �������ӳ�ʱ
//					connection.setConnectTimeout(8000);
//					// ���ö�ȡ���ݳ�ʱ
//					connection.setReadTimeout(8000);
//					// ��ȡ���������ص�������
//					InputStream in = connection.getInputStream(); // InputStream���ֽ������ַ����ڶ����ʱ���Ƚ�����������ת��Ϊ�ַ�
//					// ��ȡ�������е�����
//					BufferedReader reader = new BufferedReader(
//							new InputStreamReader(in));// ������
//					StringBuilder response = new StringBuilder();// �ַ���������������ʢ�Ŵӷ�������ȡ������
//					String line;
//					while ((line = reader.readLine()) != null)// һ��һ�еĶ�ȡ����
//					{
//						response.append(line);
//					}
//					if (listener != null) {
//						listener.onFinish(response.toString());
//					}
//				} catch (Exception e) {
//					if (listener != null) {
//						listener.onError(e);
//					}
//				} finally {
//					if (connection != null) {
//						connection.disconnect();
//					}
//				}
//			}
//		}).start();
//	}
//}