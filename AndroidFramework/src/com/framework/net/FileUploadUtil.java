package com.framework.net;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;
/**
 * 文件上传  支持多文件上传
 * @author lee
 */
public class FileUploadUtil extends AsyncTask<File[], Void, String> {
	public Context context; //上下文环境
	private String url;
	private final String TYPE = "image";  //文件类型
	private OnResultListener listener;
	public FileUploadUtil(Context context,String url) {
		this.context = context;
		this.url = url;
	}
	@Override
	protected String doInBackground(File[]... imageFile) {
		try {
			String requestUrl = url;
			Map<String, String> params = new HashMap<String, String>();
			FormFile[] images = new FormFile[imageFile[0].length];
			for(int i = 0 ; i < imageFile[0].length ; i++){
				params.put("fileName"+i, imageFile[0][i].getName());
				images[i] = new FormFile(imageFile[0][i].getName(), imageFile[0][i], TYPE, "application/octet-stream");
			}
			return SocketHttpRequester.post(requestUrl, params, images);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	protected void onPostExecute(String result) {
		try {
			if(!TextUtils.isEmpty(result)){
				listener.onGetResult(result, 200);
			}else{
				listener.onGetResult(result, 400);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void uploadImg(String url,File[] imageFile,Context context,OnResultListener listener){
		FileUploadUtil async = new FileUploadUtil(context,url);
		async.listener = listener;
		async.execute(imageFile);
	}
}
