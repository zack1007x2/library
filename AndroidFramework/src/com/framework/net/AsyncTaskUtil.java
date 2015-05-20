package com.framework.net;
import java.util.Map;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
/**
 * 异步网络请求(推荐使用)
 * @author lee
 *	直接调用sendGet()/sendPost()方法，即可发送网络请求，不需要开启额外的线程
 *	自带离线缓存技术，如需调用离线缓存，对服务器返回的数据要求如下：
 *		例：{"flag":1,data:{[],[],[]}}  或：{"flag":1,"data":{"":"","":""}}
 *		你所需要的数据全在data里面
 *
 *	<uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
 */
public class AsyncTaskUtil extends AsyncTask<ParamUtil, Integer, String> {
	public Request req = Request.GET;  //默认get请求
	public OnResultListener listener;  //服务器数据回调监听
	public String msg;  //友好的消息提示
	public Context context;  //上下文环境，不能为空，否者无法使用
	public enum Request{
		GET,POST
	}
	/**
	 * 发送get请求， 不带提示框
	 * @param url  地址
	 * @param listener  回调监听器
	 * @param context  上下文环境
	 */
	public static void sendGet(String url,OnResultListener listener,Context context){
		ParamUtil params = new ParamUtil();
		params.url = url;
		AsyncTaskUtil async = new AsyncTaskUtil(context);
		async.req = Request.GET;
		params.listener = listener;
		async.execute(params);
	}
	/**
	 * 发送get请求到服务器，带有消息提示
	 * @param url 
	 * @param listener 
	 * @param context
	 * @param msg  消息提示的内容
	 */
	public static void sendGet(String url,OnResultListener listener,Context context,String msg){
		ParamUtil params = new ParamUtil();
		params.url = url;
		AsyncTaskUtil async = new AsyncTaskUtil(msg,context);
		async.req = Request.GET;
		params.listener = listener;
		async.execute(params);
	}
	/**
	 * 发送Post请求到服务器
	 * @param url 
	 * @param params 请求体，key=value
	 * @param listener 
	 * @param context
	 */
	public static void sendPost(String url,Map<String,String> params,OnResultListener listener,Context context){
		ParamUtil param = new ParamUtil();
		param.url = url;
		param.params = params;
		AsyncTaskUtil async = new AsyncTaskUtil(context);
		async.req = Request.POST;
		param.listener = listener;
		async.execute(param);
	}
	/**
	 * 发送Post请求到服务器，带有消息提示
	 * @param url
	 * @param params
	 * @param listener
	 * @param context
	 * @param msg
	 */
	public static void sendPost(String url,Map<String,String> params,OnResultListener listener,Context context,String msg){
		ParamUtil param = new ParamUtil();
		param.url = url;
		param.params = params;
		AsyncTaskUtil async = new AsyncTaskUtil(msg,context);
		async.req = Request.POST;
		param.listener = listener;
		async.execute(param);
	}
	/**
	 * 发送get请求， 不带提示框，带有离线缓存
	 * @param url  地址
	 * @param listener  回调监听器
	 * @param context  上下文环境
	 * @param clazz 结果的数据类型，例：User.class
	 */
	public static <T> void sendGet(String url,final OnResultListener listener,final Context context,final Class<T> clazz){
		ParamUtil params = new ParamUtil();
		params.url = url;
		AsyncTaskUtil async = new AsyncTaskUtil(context);
		async.req = Request.GET;
		params.listener = new OnResultListener() {
			@Override
			public void onGetResult(Object result, int iError) throws Exception {
//				T t = OfficeCash(context, clazz, result);
//				listener.onGetResult(t, iError);
			}
		};
		async.execute(params);
	}
	/**
	 * 发送get请求到服务器，带有消息提示,带有离线缓存
	 * @param url 
	 * @param listener 
	 * @param context
	 * @param msg  消息提示的内容
	 */
	public static <T> void sendGet(String url,final OnResultListener listener,final Context context,String msg,final Class<T> clazz){
		ParamUtil params = new ParamUtil();
		params.url = url;
		AsyncTaskUtil async = new AsyncTaskUtil(msg,context);
		async.req = Request.GET;
		params.listener = new OnResultListener() {
			@Override
			public void onGetResult(Object result, int iError) throws Exception {
//				T t = OfficeCash(context, clazz, result);
//				listener.onGetResult(t, iError);
			}
		};
		async.execute(params);
	}
	/**
	 * 发送Post请求到服务器,带有离线缓存，结果数据无需解析，已经解析过了
	 * @param url 
	 * @param params 请求体，key=value
	 * @param listener 
	 * @param context
	 */
	public static <T> void sendPost(String url,Map<String,String> params,final OnResultListener listener,final Context context,final Class<T> clazz){
		ParamUtil param = new ParamUtil();
		param.url = url;
		param.params = params;
		AsyncTaskUtil async = new AsyncTaskUtil(context);
		async.req = Request.POST;
		param.listener = new OnResultListener() {
			@Override
			public void onGetResult(Object result, int iError) throws Exception {
//				T t = OfficeCash(context, clazz, result);
//				listener.onGetResult(t, iError);  //t  无需解析，已经解析过了
			}
		};
		async.execute(param);
	}
	/**
	 * 发送Post请求到服务器，带有消息提示,带有离线缓存，结果无需解析，已经解析过了
	 * @param <T>
	 * @param url
	 * @param params
	 * @param listener
	 * @param context
	 * @param msg
	 */
	public static <T> void sendPost(String url,Map<String,String> params,final OnResultListener listener,final Context context,String msg,final Class<T> clazz){
		ParamUtil param = new ParamUtil();
		param.url = url;
		param.params = params;
		AsyncTaskUtil async = new AsyncTaskUtil(msg,context);
		async.req = Request.POST;
		param.listener = new OnResultListener() {
			@Override
			public void onGetResult(Object result, int iError) throws Exception {
//				T t = OfficeCash(context, clazz, result);
//				listener.onGetResult(t, iError);
			}
		};
		async.execute(param);
	}	
	public AsyncTaskUtil(String msg, Context context) {
		this.msg = msg;
		this.context = context;
	}
	
	public AsyncTaskUtil(Context context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if(msg != null && msg.length() > 0)
			DialogTools.showProgressDialog(context, msg);
	}
	@Override
	protected String doInBackground(ParamUtil... params) {
		HttpUtil client = new HttpUtil(context);
		String result = null;
		if(params[0].listener != null && listener == null){
			this.listener = params[0].listener;  //设置回调监听器
		}
		if(NetWorkUtils.isNetConnected(context)){  //检查网络状态
			if(req == Request.GET){
				result = client.sendGet(params[0].url);  //发送get请求
			}else if(req == Request.POST){
				result = client.sendPost(params[0].url, params[0].params); //发送Post请求
			}
		}
		return result;
	}
	@Override
	protected void onPostExecute(String result) {
		DialogTools.closeProgressDialog();  //关闭友好提示信息
		try {
			if(result != null && result.length() > 0){
				if(listener != null){
					listener.onGetResult(result, 200);
				}
			}else{
				if(listener != null){
					listener.onGetResult(result, 404);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 离线缓存
	 * @param context
	 * @param clazz
	 * @param result
	 * @return   T  无需解析的数据，已经解析成clazz对应的对象了
	 * @throws JSONException
	 */
//	private static <T> T OfficeCash(final Context context, final Class<T> clazz,
//			Object result) throws JSONException {
//		JSONObject json = new JSONObject(result.toString());
//		int flag = json.getInt("flag");
//		T t = null;
//		if(flag == 1){
//			JSONObject data = json.getJSONObject("data");
//			t = JsonUtil.parserJson(data.toString(), clazz);
//			FinalDb db = FinalDb.create(context);
//			if(t instanceof Collection){
//				Iterator<T> it = ((Collection<T>)t).iterator();
//				while(it.hasNext()){
//					db.save(it.next());  //保存服务器返回的数据
//				}
//			}else{
//				db.save(t);
//			}
//		}
//		return t;
//	}
}
