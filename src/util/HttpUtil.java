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
//					// 设置HTTP的请求方式
//					connection.setRequestMethod("GET");
//					// 设置连接超时
//					connection.setConnectTimeout(8000);
//					// 设置读取数据超时
//					connection.setReadTimeout(8000);
//					// 获取服务器返回的输入流
//					InputStream in = connection.getInputStream(); // InputStream是字节流，字符流在读入的时候，先将二进制数据转化为字符
//					// 读取输入流中的数据
//					BufferedReader reader = new BufferedReader(
//							new InputStreamReader(in));// 用来读
//					StringBuilder response = new StringBuilder();// 字符串构造器，用来盛放从服务器读取的数据
//					String line;
//					while ((line = reader.readLine()) != null)// 一行一行的读取数据
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
