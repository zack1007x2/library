package com.framework.net;

import java.util.Calendar;
import cn.com.framework.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 对话框工具类
 * @author lee
 * @date 2014-4-1 下午5:59:10
 * @version V1.0
 */
public class DialogTools {
	/**
	 * 显示普通单按钮对话框
	 * @param ctx     上下文
	 * @param iconId  图标，如：R.drawable.icon 必填
	 * @param title   标题 必填
	 * @param message 显示内容 必填
	 * @param btnName 按钮名称 必填
	 * @param listener 监听器，
	 */
	public static void createDialog(Context ctx, int iconId, String title, String message, String btnName, android.content.DialogInterface.OnClickListener listener) {
		Dialog dialog = null;
		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ctx);
		builder.setIcon(iconId);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(btnName, listener);
		dialog = builder.create();
		dialog.show();
	}

	/**
	 * 创建普通双按钮对话框
	 * 
	 * @param ctx
	 * @param iconId
	 * @param title
	 * @param message
	 * @param btnPositiveName
	 * @param listener_Positive
	 * @param btnNegativeName
	 * @param listener_Negative
	 * @return 
	 */
	public static void createDialog(Context ctx, int iconId, String title, String message, String btnPositiveName, android.content.DialogInterface.OnClickListener listener_Positive,
			String btnNegativeName, android.content.DialogInterface.OnClickListener listener_Negative) {
		Dialog dialog = null;
		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ctx);
		builder.setIcon(iconId);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(btnPositiveName, listener_Positive);
		builder.setNegativeButton(btnNegativeName, listener_Negative);
		dialog = builder.create();
		dialog.show();
	}

	/**
	 * 创建列表对话框
	 * @param ctx
	 * @param iconId
	 * @param title
	 * @param itemsId
	 * @param listener
	 * @return
	 */
	public static void createListDialog(Context ctx, int iconId, String title, int itemsId, android.content.DialogInterface.OnClickListener listener) {
		Dialog dialog = null;
		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ctx);
		builder.setIcon(iconId);
		builder.setTitle(title);
		builder.setItems(itemsId, listener);
		dialog = builder.create();
		dialog.show();
	}

	/**
	 * 创建单选按钮对话框
	 * @param ctx
	 * @param iconId
	 * @param title
	 * @param itemsId
	 * @param listener
	 * @param btnName
	 * @param listener2
	 */
	public static void createRadioDialog(Context ctx, int iconId, String title, int itemsId, android.content.DialogInterface.OnClickListener listener, String btnName,
			android.content.DialogInterface.OnClickListener listener2) {
		Dialog dialog = null;
		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ctx);
		builder.setIcon(iconId);
		builder.setTitle(title);
		builder.setSingleChoiceItems(itemsId, 0, listener);
		builder.setPositiveButton(btnName, listener2);
		dialog = builder.create();
		dialog.show();
	}

	/**
	 * 创建复选按钮对话框
	 * @param ctx
	 * @param iconId
	 * @param title
	 * @param itemsId
	 * @param flags
	 * @param listener
	 * @param btnName
	 * @param listener2
	 */
	public static void createCheckBoxDialog(Context ctx, int iconId, String title, int itemsId, boolean[] flags, android.content.DialogInterface.OnMultiChoiceClickListener listener, String btnName,
			android.content.DialogInterface.OnClickListener listener2) {
		Dialog dialog = null;
		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ctx);
		builder.setIcon(iconId);
		builder.setTitle(title);
		builder.setMultiChoiceItems(itemsId, flags, listener);
		builder.setPositiveButton(btnName, listener2);
		dialog = builder.create();
		dialog.show();
	}

	/**
	 * 日期对话框
	 * @param context
	 * @param v
	 * @return 
	 */
	public static void createDateDialog(Context context, final View v) {
		Dialog dialog = null;
		Calendar calender = Calendar.getInstance();
		dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				if (v instanceof TextView) {
					((TextView) v).setText(year + "年"+ (monthOfYear + 1) + "月"+ dayOfMonth + "日");
				}
				if (v instanceof EditText) {
					((EditText) v).setText(year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
				}
			}
		}, calender.get(calender.YEAR), calender.get(calender.MONTH), calender.get(calender.DAY_OF_MONTH));

		dialog.show();
	}
	/**
	 * 加载对话框
	 * @param context
	 * @param msg
	 */
	public static void createLoadDialog(Context context, String msg) {
		android.app.ProgressDialog dialog = new ProgressDialog(context);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage(msg);
		dialog.setIndeterminate(false);
		dialog.setCancelable(false);
		dialog.show();
	}

	private static Dialog dialog;
	private static android.app.AlertDialog.Builder builder;

	public static void showProgressDialog(Context context) {
		showProgressDialog(context, "请等候，数据加载中...");
	}

	public static void showProgressDialog(Context context, String msg) {
		 LayoutInflater inflater = LayoutInflater.from(context);  
	        View v = inflater.inflate(R.layout.loading_dialog, null);  //得到加载view  
	        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view); //加载布局  
	        // main.xml中的ImageView  
	        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);  
	        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView); //提示文字  
	        //加载动画  
	        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(  
	                context, R.anim.dialog_anim);  
	        //使用ImageView显示动画  
	        spaceshipImage.startAnimation(hyperspaceJumpAnimation);  
	        tipTextView.setText(msg);//设置加载信息  
	  
	        dialog = new Dialog(context, R.style.loading_dialog);//创建自定义样式dialog  
	  
	        dialog.setCancelable(false);// 不可以用“返回键”取消
	        dialog.setContentView(layout, new LinearLayout.LayoutParams(  
	                LinearLayout.LayoutParams.FILL_PARENT,  
	                LinearLayout.LayoutParams.FILL_PARENT));// 设置布局  
	        dialog.show(); 

	}

	public static void closeProgressDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}
	/**
	 * 当判断当前手机没有网络时使用
	 * @param context
	 */
	public static void showNoNetWork(final Context context) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setIcon(R.drawable.ic_launcher)//
				.setTitle(R.string.app_name)//
				.setMessage("当前无网络").setPositiveButton("设置", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// 跳转到系统的网络设置界面
						Intent intent = new Intent();
						intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
						context.startActivity(intent);

					}
				}).setNegativeButton("知道了 ",null).show();
	}
	/**
	 * 创建自定义对话框
	 * @param context
	 */
	public static void createSelfDialog(Context context,int iconId,String title, View view) {
		builder = new android.app.AlertDialog.Builder(context);
		//设置进度条是否可以按返回键取消
		builder.setCancelable(true);
		builder.setIcon(iconId);
		builder.setTitle(title);
		builder.setView(view);
		builder.show();
	}
	public static void createSelfDialog(Context context,int iconId,String title, View view,DialogInterface.OnClickListener negativelistener,DialogInterface.OnClickListener positivelistener) {
		builder = new android.app.AlertDialog.Builder(context);
		//设置进度条是否可以按返回键取消
		builder.setCancelable(true);
		builder.setIcon(iconId);
		builder.setTitle(title);
		builder.setView(view);
		builder.setNegativeButton("确定", negativelistener);
		builder.setPositiveButton("取消", positivelistener);
		builder.show();
	}
}