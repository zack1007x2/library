package com.framework.webservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.framework.net.NetWorkUtils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.WindowManager;

/**
 * 这是一个针对android访问webService方法的简单框架,获取本类对象,调用call方法既可执行网络请求.
 * 设置setSoapNetWorkListener可获取服务器返回的数据
 * 
 * whereValues集合: 键:webService方法的参数名 值:参数对应需要的值
 * 
 * send传入参数selection,可只获取该selection指定的元素名数据
 * 
 * @author lee
 * 		创建对象,设置监听,调用send方法
 */

public class WebServiceUtils {
	private final int SERVER_CONNECTION_START = 0;
	private final int SERVER_CONNECTION_SUCCESS = 1;
	private final int SERVER_CONNCTION_FAILURE = 2;
	private final int SERVER_EXCEPTION = 3;
	private final int SERVER_CONNECTION_SUCCESS_RETURNLIST = 4;
	private String url;
	private String methodName;
	private Map<String, Object> whereValues = new HashMap<String, Object>();
	private Dialog dialog = null;
	private Context context;
	private SoapNetWorkListener soapNetWorkListener = null;
	private int connOutTime = WebConfig.CONN_TIME_OUT;
	private static int lastOperationTime = 0;

	public void setOnSoapNetWorkListener(SoapNetWorkListener soapNetWorkListener) {
		this.soapNetWorkListener = soapNetWorkListener;
	}

	/**
	 * 
	 * @param dialogTitle
	 *            进度条显示的消息
	 * @param context
	 *            上下文
	 * @param url
	 *            webService Url
	 * @param methodName
	 *            方法名
	 * @param whereValues
	 *            查询条件(键:参数名 值:查询值)
	 */
	public WebServiceUtils(String dialogTitle, Context context, String url,
			String methodName, Map<String, Object> whereValues) {
		this.context = context;
		this.url = url;
		this.methodName = methodName;
		this.whereValues = whereValues;
		if (context == null) {
			return;
		}
		if (!TextUtils.isEmpty(dialogTitle)) {
			dialog = MyProgressDialog.createLoadingDialog(context, dialogTitle);
			// 允许使用返回键,false为不允许
			dialog.setCancelable(false);

			// 设置透明度,1为不透明(dialog覆盖当前布局), 0 - 0.99 为透明,数值越底,当前布局亮度越高
			WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
			lp.dimAmount = 0.5f; // this sets the amount of darkening
			dialog.getWindow().setAttributes(lp);
			dialog.getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		}
	}

	/**
	 * @param context
	 *            上下文
	 * @param url
	 *            webService Url
	 * @param methodName
	 *            方法名
	 * @param whereValues
	 *            查询条件(键:参数名 值:查询值)
	 */
	public WebServiceUtils(Context context, String url, String methodName,
			Map<String, Object> whereValues) {
		this.context = context;
		this.url = url;
		this.methodName = methodName;
		this.whereValues = whereValues;
	}

	/**
	 * @param dialogTitle
	 *            进度条显示的消息
	 * @param connOutTime
	 *            最长请求超时时间
	 * @param context
	 *            上下文
	 * @param url
	 *            webService Url
	 * @param methodName
	 *            方法名
	 * @param whereValues
	 *            查询条件(键:参数名 值:查询值)
	 */
	public WebServiceUtils(String dialogTitle, Context context,
			int connOutTime, String url, String methodName,
			Map<String, Object> whereValues) {
		this.context = context;
		this.url = url;
		this.methodName = methodName;
		this.connOutTime = connOutTime;
		this.whereValues = whereValues;
		if (context == null) {
			return;
		}
		if (!TextUtils.isEmpty(dialogTitle)) {
			dialog = MyProgressDialog.createLoadingDialog(context, dialogTitle);
			// 允许使用返回键,false为不允许
			dialog.setCancelable(false);

			// 设置透明度,1为不透明(dialog覆盖当前布局), 0 - 0.99 为透明,数值越底,当前布局亮度越高
			WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
			lp.dimAmount = 0.5f; // this sets the amount of darkening
			dialog.getWindow().setAttributes(lp);
			dialog.getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		}
	}

	/**
	 * @param context
	 *            上下文
	 * @param connOutTime
	 *            最长请求超时时间
	 * @param url
	 *            webService Url
	 * @param methodName
	 *            方法名
	 * @param whereValues
	 *            查询条件(键:参数名 值:查询值)
	 */
	public WebServiceUtils(Context context, int connOutTime, String url,
			String methodName, Map<String, Object> whereValues) {
		this.context = context;
		this.url = url;
		this.methodName = methodName;
		this.connOutTime = connOutTime;
		this.whereValues = whereValues;
	}

	private final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SERVER_CONNECTION_START:
				if (dialog != null) {
					dialog.show();
				}
				if (soapNetWorkListener != null) {
					soapNetWorkListener.OnSoapNetWorkListener(
							SoapNetWorkListener.INTELET_CONN_START, msg.obj);
				}
				break;
			case SERVER_CONNECTION_SUCCESS:
				if (dialog != null) {
					dialog.cancel();
				}
				if (soapNetWorkListener != null) {
					soapNetWorkListener.OnSoapNetWorkListener(
							SoapNetWorkListener.INTELET_CONN_SUCCESS, msg.obj);
				}
				break;
			case SERVER_CONNECTION_SUCCESS_RETURNLIST:
				if (dialog != null) {
					dialog.cancel();
				}
				if (soapNetWorkListener != null) {
					soapNetWorkListener
							.OnSoapNetWorkListener(
									SoapNetWorkListener.INTELET_CONNECTION_SUCCESS_RETURNLIST,
									msg.obj);
				}
				break;
			case SERVER_CONNCTION_FAILURE:
				if (dialog != null) {
					dialog.cancel();
				}
				if (soapNetWorkListener != null) {
					soapNetWorkListener.OnSoapNetWorkListener(
							SoapNetWorkListener.INTELET_SERVER_EXCEPTION,
							msg.obj == null ? "服务器繁忙" : msg.obj);
				}
				break;
			case SERVER_EXCEPTION:
				if (dialog != null) {
					dialog.cancel();
				}
				if (soapNetWorkListener != null) {
					soapNetWorkListener.OnSoapNetWorkListener(
							SoapNetWorkListener.INTELET_SERVER_EXCEPTION,
							msg.obj == null ? "连接服务器失败" : msg.obj);
				}
				break;
			}
		};
	};

	/**
	 * 发送网络请求
	 */
	public void send() {
		if (context != null) {
			// 如果网络不可用
			if (!NetWorkUtils.getNetConnecState(context)) {
				NetWorkUtils.showToast(context, "当前网络不可用");
				soapNetWorkListener.OnSoapNetWorkListener(SoapNetWorkListener.INTELET_SERVER_EXCEPTION,"当前网络不可用");
				return;
			}
		}

		if (isOvertime()) {
			NetWorkUtils.showToast(context, "登陆已超时，请重新登陆");
			lastOperationTime = 0;
			new Thread(new Runnable() {
				@Override
				public void run() {
					// 延迟1秒后
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
					// 重新启动应用
					restartApplication();
				}
			}).start();
			return;
		}
		request2Server();
	}

	/**
	 * 发送网络请求
	 * 
	 * @param selection
	 *            指定获取的对应元素名的返回值
	 */
	public void send(List<String> selection) {
		if (context != null) {
			// 如果网络不可用
			if (!NetWorkUtils.isNetConnected(context)) {
				NetWorkUtils.showToast(context, "当前网络不可用");
				soapNetWorkListener.OnSoapNetWorkListener(SoapNetWorkListener.INTELET_SERVER_EXCEPTION,"当前网络不可用");
				return;
			}
		}

		if (isOvertime()) {
			NetWorkUtils.showToast(context, "登陆已超时，请重新登陆");
			lastOperationTime = 0;
			new Thread(new Runnable() {
				@Override
				public void run() {
					// 延迟1秒后
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
					// 重新启动应用
					restartApplication();
				}
			}).start();
			return;
		}
		request2Server(selection);
	}

	private boolean isOvertime() {
		// 如果最后登陆时间为0毫秒，说明是第一次登陆，则未超出超时时间
		if (lastOperationTime == 0) {
			return false;
		}
		long overTime = lastOperationTime + WebConfig.LAST_OPERATION_TIME;
		return System.currentTimeMillis() > overTime;
	}

	private void restartApplication() {
		Intent intent = context.getPackageManager().getLaunchIntentForPackage(
				context.getPackageName());
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
	}

	private void request2Server() {
		new Thread() {
			public void run() {
				// 1.实例化SoapObject对象 参数1:webservice名称空间 参数2：方法名
				SoapObject request = new SoapObject(WebConfig.WEB_NAMESPACE,
						methodName);

				// 2.添加方法参数，需要与webservice里被调用的方法的参数名保持一致 键：参数名 值：传递的值
				if (whereValues != null && whereValues.size() > 0) {
					Set<String> set = whereValues.keySet();
					for (Iterator<String> it = set.iterator(); it.hasNext();) {
						String key = it.next();
						request.addProperty(key, whereValues.get(key));
					}
				}
				// 3.设置Soap的请求信息,参数部分为Soap协议的版本号
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				String SOAP_ACTION = WebConfig.WEB_NAMESPACE + methodName;
				envelope.bodyOut = request;
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);

				// 4.构建传输对象 参数1：请求服务器的Url地址 参数2:网络请求超时时间
				HttpTransportSE ht = new HttpTransportSE(url, connOutTime);
				ht.debug = true;
				try {
					Message startMsg = Message.obtain();
					startMsg.what = SERVER_CONNECTION_START;
					startMsg.obj = "获取数据中,请稍候";
					handler.sendMessage(startMsg);
					// 发送请求
					ht.call(SOAP_ACTION, envelope);
					String content = envelope.getResponse() == null ? null : envelope.getResponse().toString();
					if (content != null) {
						Message msg = Message.obtain();
						msg.what = SERVER_CONNECTION_SUCCESS;
						msg.obj = content;
						handler.sendMessage(msg);
					} else {
						Message msg = Message.obtain();
						msg.what = SERVER_CONNCTION_FAILURE;
						msg.obj = "未查询到相关数据";
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					try {
						ht = new HttpTransportSE(url, connOutTime);
						ht.call(null, envelope);
						String content = envelope.getResponse() == null ? null
								: envelope.getResponse().toString();
						if (content != null) {
							Message msg = Message.obtain();
							msg.what = SERVER_CONNECTION_SUCCESS;
							msg.obj = content;
							handler.sendMessage(msg);
						} else {
							Message msg = Message.obtain();
							msg.what = SERVER_CONNCTION_FAILURE;
							msg.obj = "未查询到相关数据";
							handler.sendMessage(msg);
						}
					} catch (Exception e2) {
						Message msg = Message.obtain();
						msg.what = SERVER_EXCEPTION;
						msg.obj = "服务器繁忙";
						handler.sendMessage(msg);
						e.printStackTrace();
					}
					e.printStackTrace();
				}
			}
		}.start();
	}

	private void request2Server(final List<String> selection) {
		new Thread() {
			public void run() {
				// 1.实例化SoapObject对象 参数1:webservice名称空间 参数2：方法名
				SoapObject request = new SoapObject(WebConfig.WEB_NAMESPACE,
						methodName);

				// 2.添加方法参数，需要与webservice里被调用的方法的参数名保持一致 键：参数名 值：传递的值
				// 2.添加方法参数，需要与webservice里被调用的方法的参数名保持一致 键：参数名 值：传递的值
				if (whereValues != null && whereValues.size() > 0) {
					Set<String> set = whereValues.keySet();
					for (Iterator<String> it = set.iterator(); it.hasNext();) {
						String key = it.next();
						request.addProperty(key, whereValues.get(key));
					}
				}
				// 3.设置Soap的请求信息,参数部分为Soap协议的版本号
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);

				String SOAP_ACTION = WebConfig.WEB_NAMESPACE + methodName;
				envelope.bodyOut = request;
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);

				// 4.构建传输对象 参数1：请求服务器的Url地址 参数2:网络请求超时时间
				HttpTransportSE ht = new HttpTransportSE(url, connOutTime);
				ht.debug = true;
				try {
					Message startMsg = Message.obtain();
					startMsg.what = SERVER_CONNECTION_START;
					startMsg.obj = "获取数据中,请稍候";
					handler.sendMessage(startMsg);
					// 发送请求
					ht.call(SOAP_ACTION, envelope);
					String content = envelope.getResponse() == null ? null
							: envelope.getResponse().toString();
					if (content != null) {
						SoapObject detail = (SoapObject) envelope.getResponse();
						SoapObject childs = (SoapObject) detail.getProperty(0);
						List<Map<String, String>> allList = new ArrayList<Map<String, String>>();
						Map<String, String> itemMap = null;
						for (int i = 0; i < childs.getPropertyCount(); i++) {
							itemMap = new HashMap<String, String>();
							for (String string : selection) {
								String item = ((SoapObject) childs
										.getProperty(i)).getProperty(string)
										.toString();
								itemMap.put(string, item);
							}
							allList.add(itemMap);
						}
						Message msg = Message.obtain();
						msg.what = SERVER_CONNECTION_SUCCESS_RETURNLIST;
						msg.obj = allList;
						handler.sendMessage(msg);
					} else {
						Message msg = Message.obtain();
						msg.what = SERVER_CONNCTION_FAILURE;
						msg.obj = "未查询到相关数据";
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					try {
						ht = new HttpTransportSE(url, connOutTime);
						ht.call(null, envelope);
						String content = envelope.getResponse() == null ? null
								: envelope.getResponse().toString();
						if (content != null) {
							Message msg = Message.obtain();
							msg.what = SERVER_CONNECTION_SUCCESS;
							msg.obj = content;
							handler.sendMessage(msg);
						} else {
							Message msg = Message.obtain();
							msg.what = SERVER_CONNCTION_FAILURE;
							msg.obj = "未查询到相关数据";
							handler.sendMessage(msg);
						}
					} catch (Exception e2) {
						Message msg = Message.obtain();
						msg.what = SERVER_EXCEPTION;
						msg.obj = "服务器繁忙";
						handler.sendMessage(msg);
						e.printStackTrace();
					}
					e.printStackTrace();
				}
			}
		}.start();
	}

	public static void setUserInfo(Map<String, Object> whereValues) {
		whereValues.put("AppID", "111111111");
		whereValues.put("LoginID", "admin");
	}
}
