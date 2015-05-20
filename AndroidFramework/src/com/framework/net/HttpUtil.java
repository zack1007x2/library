package com.framework.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;

public class HttpUtil {
	
	private DefaultHttpClient client;
	private HttpPost post;
	private HttpGet get;
	private HttpResponse response;
	
	
	public HttpUtil(Context context){
		client = new DefaultHttpClient();
		client.setCookieStore(PersistentCookieStore.getInstance(context));
	}	
	/**
	 * @param url
	 * @param xml
	 */
	public InputStream sendXml(String url, String xml) {
		post = new HttpPost(url);
		try {
			StringEntity entity = new StringEntity(xml, "utf-8");
			post.setEntity(entity);

			response = client.execute(post);

			if (response.getStatusLine().getStatusCode() == 200) {
				// 返回的结果是输入�?-xml文件
				return response.getEntity().getContent();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;// 如果获取到null
	}

	/**
	 * 
	 * @param uri
	 * @param params
	 * @return
	 */
	public String sendPost(String uri, Map<String, String> params) {
		post = new HttpPost(uri);
		// post.setHeaders(headers);

		// 处理超时
		HttpParams httpParams = new BasicHttpParams();//
		httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 8000);
		HttpConnectionParams.setSoTimeout(httpParams, 8000);
		post.setParams(httpParams);

		try {
			// 设置参数
			if (params != null && params.size() > 0) {
				List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();

				for (Map.Entry<String, String> item : params.entrySet()) {
					BasicNameValuePair pair = new BasicNameValuePair(item.getKey(), item.getValue());
					parameters.add(pair);
				}
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, "utf-8");
				post.setEntity(entity);
			}

			response = client.execute(post);

			if (response.getStatusLine().getStatusCode() == 200) {
				// 返回的结果是输出-Json文件
				// return response.getEntity().getContent();
				return EntityUtils.toString(response.getEntity(), "utf-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public String sendGet(String uri) {
		get = new HttpGet(uri);

		HttpParams httpParams = new BasicHttpParams();//
		HttpConnectionParams.setConnectionTimeout(httpParams, 8000);
		HttpConnectionParams.setSoTimeout(httpParams, 8000);
		get.setParams(httpParams);

		try {
			response = client.execute(get);

			if (response.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(response.getEntity(), "utf-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	
	public InputStream loadImg(String uri) {
		if(TextUtils.isEmpty(uri)){
			return null;
		}
		get = new HttpGet(uri);

		HttpParams httpParams = new BasicHttpParams();//
		HttpConnectionParams.setConnectionTimeout(httpParams, 8000);
		HttpConnectionParams.setSoTimeout(httpParams, 8000);
		get.setParams(httpParams);

		try {
			response = client.execute(get);

			if (response.getStatusLine().getStatusCode() == 200) {
				return response.getEntity().getContent();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 用post的方式提交，返回字符
	 * 
	 * @param uri
	 *            请求地址
	 * @param params
	 *            参数集合(请求名字,请求内容)
	 * @throws Exception
	 *             异常处理
	 */
	public String doPost(String uri, Map<String, String> params)
			throws Exception {
		// post请求
		HttpPost post = new HttpPost(uri);
		// 头部
		post.addHeader("charset", HTTP.UTF_8);
		// NameValuePair(设置)
		ArrayList<NameValuePair> nvps = new ArrayList<NameValuePair>();
		Set<Entry<String, String>> set = params.entrySet();
		Iterator<Entry<String, String>> iter = set.iterator();
		HttpParams hparams = new BasicHttpParams();
		// 设置连接超时
		HttpConnectionParams.setConnectionTimeout(hparams, 3000);
		// 设置请求超时
		HttpConnectionParams.setSoTimeout(hparams, 5000);
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		/* 防止中文乱码 */
		post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		HttpResponse response = client.execute(post);

		if (response.getStatusLine().getStatusCode() == 200) {
			return EntityUtils.toString(response.getEntity(), "utf-8");
		} else {
			throw new IOException("Net Exception!");
		}
	}

	/**
	 * 用get的方式提交，返回byte[]
	 * 
	 * @param uri请求地址
	 * @return 响应的byte[]
	 * 
	 * @throws Exception
	 */
	public byte[] doGetByte(String uri) throws Exception {
		HttpGet get = new HttpGet(uri);
		HttpResponse response = client.execute(get);
		HttpParams hparams = new BasicHttpParams();
		// 设置连接超时
		HttpConnectionParams.setConnectionTimeout(hparams, 3000);
		// 设置请求超时
		HttpConnectionParams.setSoTimeout(hparams, 5000);
		if (response.getStatusLine().getStatusCode() == 200) {
			return EntityUtils.toByteArray(response.getEntity());
		} else {
			throw new IOException("Net Exception!");
		}
	}

	/**
	 * 用get的方式提交，返回字符类型
	 * 
	 * @return 响应的字符串
	 */
	public String doGet(String uri) throws Exception {
		HttpGet get = new HttpGet(uri);
		HttpResponse response = client.execute(get);
		HttpParams hparams = new BasicHttpParams();
		// 设置连接超时
		HttpConnectionParams.setConnectionTimeout(hparams, 3000);
		// 设置请求超时
		HttpConnectionParams.setSoTimeout(hparams, 5000);
		if (response.getStatusLine().getStatusCode() == 200) {
			return EntityUtils.toString(response.getEntity());
		} else {
			throw new IOException("Net Exception!");
		}
	}


	/**
	 * 保存日志
	 * 
	 * @param log
	 *            日志信息
	 */
	public static void setSaveLog(String log) {
		new AsyncTask<String, Void, String>() {

			@Override
			protected String doInBackground(String... params) {
				try {
					// 获取SD卡目�?
					String sdDir = getSDPath();

					if (sdDir != null && getSDPath().length() > 0) {
						/**
						 * 创建�?��可以�?��件中写入字符数据的字符流输出流对�?创建时必须明确文件的目的�?
						 * 如果文件不存在，这回自动创建。如果文件存在，则会覆盖�?当路径错误时会抛异常
						 * 
						 * 当在创建时加入true参数，回实现对文件的续写�?
						 */
						FileWriter fw = new FileWriter(sdDir
								+ "/smallposLog.txt", true);
						// 写入文本
						fw.write(params[0]);
						// 进行刷新，将字符写到目的地中�?
						fw.flush();
						// 关闭流，关闭资源。在关闭前会调用flush方法 刷新缓冲区�?关闭后在写的话，会抛IOException
						fw.close();
					}
				} catch (Exception e) {
				}
				return null;
			}
		}.execute(log);
	}

	/**
	 * 获取SD卡路�?
	 * 
	 * @return
	 */
	public static String getSDPath() {
		// 路径
		File sdDir = null;
		// 绝对路径
		String fileDir = "";
		try {
			// 判断SD卡是否存�?
			if (Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED)) {
				// 获取跟目�?
				sdDir = Environment.getExternalStorageDirectory();
				// 绝对路径
				fileDir = sdDir.getAbsolutePath();
			} else {
				fileDir = "";
			}
		} catch (Exception e) {
			fileDir = e.getMessage();
		}
		return fileDir;
	}

	/**
	 * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
	 * 
	 * @param url
	 *            Service net address
	 * @param params
	 *            text content
	 * @param files
	 *            pictures
	 * @return String result of Service response
	 * @throws IOException
	 */
	public static String uploadMsg(String url, Map<String, String> params,
			Map<String, File> files) throws IOException {
		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String PREFIX = "--", LINEND = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";

		URL uri = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
		conn.setReadTimeout(10 * 1000); // 缓存的最长时�?
		conn.setDoInput(true);// 允许输入
		conn.setDoOutput(true);// 允许输出
		conn.setUseCaches(false); // 不允许使用缓�?
		conn.setConnectTimeout(720000);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("Charsert", "UTF-8");
		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
				+ ";boundary=" + BOUNDARY);

		// 首先组拼文本类型的参�?
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);
			sb.append("Content-Disposition: form-data; name=\""
					+ entry.getKey() + "\"" + LINEND);
			sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
			sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
			sb.append(LINEND);
			sb.append(entry.getValue());
			sb.append(LINEND);
		}

		OutputStream outStream =conn.getOutputStream();
		outStream.write(sb.toString().getBytes());
		// 发�?文件数据
		if (files != null)
			for (Map.Entry<String, File> file : files.entrySet()) {
				StringBuilder sb1 = new StringBuilder();
				sb1.append(PREFIX);
				sb1.append(BOUNDARY);
				sb1.append(LINEND);

				sb1.append("Content-Disposition: form-data; name=\""
						+ file.getKey() + "\"; filename=\""
						+ file.getValue().getName() + "\"" + LINEND);
				sb1.append("Content-Type: application/octet-stream; charset="
						+ CHARSET + LINEND);
				sb1.append(LINEND);
				outStream.write(sb1.toString().getBytes());

				InputStream is = new FileInputStream(file.getValue());
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					outStream.write(buffer, 0, len);
				}

				is.close();
				outStream.write(LINEND.getBytes());
			}

		// 请求结束标志
		byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
		outStream.write(end_data);
		outStream.flush();
		// 得到响应�?
		int res = conn.getResponseCode();
		InputStream in = conn.getInputStream();
		StringBuilder sb2 = new StringBuilder();
		if (res == 200) {
			int ch = -8;
			while ((ch = in.read()) != -1) {
				sb2.append((char) ch);
			}
		}
		outStream.close();
		conn.disconnect();
		return sb2.toString();
	}

}
