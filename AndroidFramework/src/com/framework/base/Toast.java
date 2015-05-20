package com.framework.base;
import android.content.Context;
/**
 * 去除了系统Toast的重叠问题
 * @author lee
 *
 */
public class Toast extends android.widget.Toast {
	private static android.widget.Toast toast;
	public Toast(Context context) {
		super(context);
	}
	/**
	 * 推荐使用，去除重叠了的
	 */
	public static android.widget.Toast makeText(CharSequence text,Context context, int duration) {
		if(toast == null){
			toast = makeText(context,text,duration);
		}else{
			toast.setText(text);
		}
		return toast;
	}
	/**
	 * 推荐使用，去除重叠了的
	 */
	public static android.widget.Toast makeText(int resId,Context context, int duration) {
		if(toast == null){
			toast = makeText(context,resId,duration);
		}else{
			toast.setText(resId);
		}
		return toast;
	}
}
