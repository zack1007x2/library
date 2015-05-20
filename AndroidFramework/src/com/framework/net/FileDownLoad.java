package com.framework.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.os.AsyncTask;
/**
 * 文件下载
 * @author lee
 */
public class FileDownLoad extends AsyncTask<String, Void, Void> {
	private String path;  //文件的保存路径
	private Context context;  //上下文环境
	private OnResultListener listener;	
	/**
	 * 文件下载
	 * @param path 下载后文件的保存路径
	 * @param url  文件的下载地址
	 * @param context 上下文环境
	 * @param listener 下载完成后会回调该接口
	 */
	public static void down(String path,String url,Context context,OnResultListener listener){
		FileDownLoad down = new FileDownLoad(path, context);
		down.listener = listener;
		down.execute(url);
	}
	
	public FileDownLoad(String path,Context context) {
		this.path = path;
		this.context = context;
	}
	@Override
	protected Void doInBackground(String... params) {
		InputStream in = null;
		FileOutputStream fos = null;
		if(NetWorkUtils.isNetConnected(context)){
			try {
				HttpUtil util = new HttpUtil(context);
				in = util.loadImg(params[0]);
				File file = new File(path);
				if(!file.exists())
					file.createNewFile();
				fos = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len = 0;
				while((len = in.read(buffer)) != -1){
					fos.write(buffer, 0, len);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					if(in != null)
						in.close();
				} catch (IOException e) {
					e.printStackTrace();
					in = null;
				}finally{
					try {
						if(fos != null)
							fos.close();
					} catch (IOException e) {
						e.printStackTrace();
						fos = null;
					}
				}
				System.gc();
			}
		}
		return null;
	}
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		try {
			if(listener != null){
				listener.onGetResult(null, 200);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
