package com.framework.pay;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.framework.base.Toast;
import com.framework.util.AssetsUtil;
import com.unionpay.UPPayAssistEx;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Message;
/**
 * 银联支付工具类(测试代码已经被注释了，如需测试，请阅读本类，有说明)
 * @author lee
 *   使用方法：将下面的代码复制到调起支付的activity中，并调用pay()方法实现银联支付
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //步骤3：处理银联手机支付控件返回的支付结果
        if (data == null) {
            return;
        }

        String msg = "";
        //支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
        String str = data.getExtras().getString("pay_result");
        if (str.equalsIgnoreCase("success")) {
            msg = "支付成功！";
        } else if (str.equalsIgnoreCase("fail")) {
            msg = "支付失败！";
        } else if (str.equalsIgnoreCase("cancel")) {
            msg = "用户取消了支付";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("支付结果通知");
        builder.setMessage(msg);
        builder.setInverseBackgroundForced(true);
        // builder.setCustomTitle();
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
 *	
 *	<!-- 银联支付的相关权限信息 -->
	<uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
    <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
    <uses-permission android:name="com.tencent.mtt.extension.Player"/>
    <uses-permission android:name="android.webkit.permission.PLUGIN"/>
	
 */
public class UnionpayUtil {
	private Activity context;
	private ProgressDialog loadingDialog;
	//mMode参数解释： "00" - 启动银联正式环境 "01" - 连接银联测试环境
	private final String mMode = "01";
    public static final int PLUGIN_NOT_INSTALLED = -1;
    public static final int PLUGIN_NEED_UPGRADE = 2;
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			if (loadingDialog != null && loadingDialog.isShowing()) {
				loadingDialog.dismiss();
	        }
	        String tn = "";
	        if (msg.obj == null || ((String) msg.obj).length() == 0) {
	            AlertDialog.Builder builder = new AlertDialog.Builder(context);
	            builder.setTitle("错误提示");
	            builder.setMessage("网络连接失败,请重试!");
	            builder.setNegativeButton("确定",
	                    new DialogInterface.OnClickListener() {
	                        @Override
	                        public void onClick(DialogInterface dialog, int which) {
	                            dialog.dismiss();
	                        }
	                    });
	            builder.create().show();
	        } else {
	            tn = (String) msg.obj;
	            /*************************************************
	             * 步骤2：通过银联工具类启动支付插件
	             ************************************************/
	            doStartUnionPayPlugin(tn, mMode);
	        }

		}
	};
	
	public UnionpayUtil(Activity context) {
		this.context = context;
	}
	protected void doStartUnionPayPlugin(String tn,String mMode) {
		int ret = UPPayAssistEx.startPay(context, null, null, tn, mMode);
        if (ret == PLUGIN_NEED_UPGRADE || ret == PLUGIN_NOT_INSTALLED) {
            // 需要重新安装控件
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("提示");
            builder.setMessage("完成购买需要安装银联支付控件，是否安装？");
            builder.setNegativeButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        	AssetsUtil asset = new AssetsUtil(context);
                			asset.installapk("UPPayPluginEx.apk");
                            dialog.dismiss();
                        }
                    });
            builder.setPositiveButton("取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
        }

	}
	/**
	 * 支付方法
	 */
	public void pay(String tn){
		if(isPay()){
			loadingDialog = ProgressDialog.show(context, "", "正在努力的获取银联支付控件,请稍候...");
			doStartUnionPayPlugin(tn, mMode);
		}else{
			AssetsUtil asset = new AssetsUtil(context);
			asset.installapk("UPPayPluginEx.apk");
		}
	}
	/**
	 * 测试方法
	 */
	public void pay(){
		if(isPay()){
			loadingDialog = ProgressDialog.show(context, "", "正在努力的获取银联支付控件,请稍候...");
			new Thread(){
				public void run() {
					 String tn = null;
				     InputStream is;
				     try {
				            String url = "http://pay123.nat123.net/server/pay";
				            URL myURL = new URL(url);
				            URLConnection ucon = myURL.openConnection();
				            ucon.setConnectTimeout(120000);
				            is = ucon.getInputStream();
				            int i = -1;
				            ByteArrayOutputStream baos = new ByteArrayOutputStream();
				            while((i = is.read()) != -1){
				                baos.write(i);
				            }
				            tn = baos.toString();
				            is.close();
				            baos.close();
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
				        Message msg = mHandler.obtainMessage();
				        msg.obj = tn;
				        mHandler.sendMessage(msg);
				}
			}.start();
		}else{
			AssetsUtil asset = new AssetsUtil(context);
			asset.installapk("UPPayPluginEx.apk");
		}
	}

	public boolean isPay(){
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo("com.unionpay.uppay", 0);
			if(info != null){
				return true;
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
}
