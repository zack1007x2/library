package com.framework.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.IntentService;
import android.content.Intent;
/**
 * 后台服务下载器
 * @author lee
 *
 */
public class BackgroundDownload extends IntentService {
	public BackgroundDownload() {
		super("smallpark");
	}

	public BackgroundDownload(String name) {
		super(name);
	}
	@Override
	protected void onHandleIntent(Intent intent) {
		InputStream stream = null;
		FileOutputStream fos = null;
		try {
			String url = intent.getStringExtra("url");
			String path = intent.getStringExtra("path");
			HttpUtil util = new HttpUtil(this);
			stream = util.loadImg(url);
			File file = new File(path);
			if(!file.exists())
				file.createNewFile();
			fos = new FileOutputStream(file);
			int len = 0;
			byte[] buffer = new byte[1024];
			while((len = stream.read(buffer)) != -1){
				fos.write(buffer, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(fos != null)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
				fos = null;
				System.gc();
			}
			try {
				if(stream != null)
					stream.close();
			} catch (IOException e) {
				e.printStackTrace();
				stream = null;
				System.gc();
			}
		}
	}

}
