package com.example.day03_httpclientpost;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView show;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		show = (TextView) findViewById(R.id.show);
		
	}
	
	public void post(View v){
		new Thread(){
			public void run() {
				/**
				 * 1.DafaultHttpCLient实例化
				 * 2.post方法实例化
				 * 3.执行
				 */
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost post = new HttpPost("http://v.juhe.cn/toutiao/index");
				try {
					ArrayList<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
					//将参数放入集合
					BasicNameValuePair pair1 = new BasicNameValuePair("type", "junshi");
					BasicNameValuePair pair2 = new BasicNameValuePair("key", "2f41498b35e69877fc56dc96776e5d1f");
					list.add(pair1);
					list.add(pair2);
					UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list);
					//放入参数
					 post.setEntity(entity);
					HttpResponse response = httpClient.execute(post);
					//获取服务器返回的数据
					StatusLine line=response.getStatusLine();
					int code = line.getStatusCode();
					//判断
					if (code==200) {
						HttpEntity httpEntity = response.getEntity();
						InputStream inputStream = httpEntity.getContent();
						//
						ByteArrayOutputStream os = new ByteArrayOutputStream();
						int len = 0;
						byte[] buffer = new byte[1024];
						while((len=inputStream.read(buffer))!=-1){
							os.write(buffer, 0, len);
						}
						final String json = os.toString();
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								show.setText(json);
							}
						});
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}.start();
	}
}
