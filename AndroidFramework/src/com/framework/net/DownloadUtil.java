package com.framework.net;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.ImageView;
/**
 * 图片下载，但不写入SD卡，内部接口
 * @author lee
 */
public class DownloadUtil extends AsyncTask<String, Void, Bitmap> {
	public OnResultListener listener;
	private Bitmap bitmap;
	private InputStream in;
	public DownloadUtil() {
	}
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}	
	@Override
	protected Bitmap doInBackground(String... params) {	
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(params[0]);
		HttpParams httpParams = new BasicHttpParams();//
		HttpConnectionParams.setConnectionTimeout(httpParams, 8000);
		HttpConnectionParams.setSoTimeout(httpParams, 8000);
		get.setParams(httpParams);
		try {
			System.out.println("正在下载...");
			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == 200) {
				in = response.getEntity().getContent();
				bitmap = BitmapFactory.decodeStream(in);
				System.out.println("bitmap="+bitmap);
				return bitmap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;	
	}
	@Override
	protected void onPostExecute(Bitmap result) {
		ByteArrayOutputStream out = null;
		try {
			if(listener != null){
				System.out.println("result = "+result);
				out = new ByteArrayOutputStream(result.getByteCount());
				result.compress(CompressFormat.JPEG, 100, out);
				byte[] buf = out.toByteArray();
				listener.onGetResult(buf, 200);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(out != null)
				try {
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
					out = null;
				}
			if(bitmap != null && bitmap.isRecycled() == false){
				bitmap.recycle();
				bitmap = null;
			}
			if(in != null)
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
					in = null;
				}
			System.gc();
		}
	}
}
