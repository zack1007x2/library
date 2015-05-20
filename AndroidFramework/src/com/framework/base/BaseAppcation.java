package com.framework.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.baidu.mapapi.SDKInitializer;

import android.app.Activity;
import android.app.Application;
import android.os.AsyncTask;

public class BaseAppcation extends Application{
	private List<String> UIS = new ArrayList<String>(); //需要退出提示的activity集合
	private List<AsyncTask<?,?,?>> asyncs = new ArrayList<AsyncTask<?,?,?>>();
	private boolean flag = true; //标识未检测过版本         false 标识已经检测过版本了
	private static BaseAppcation instance;
	public static <T extends BaseAppcation> T getInstance(){
		return (T) instance;
	}
	public boolean isFlag() {
		return flag;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		//初始化百度地图开发环境
//		SDKInitializer.initialize(getApplicationContext());
		if(instance == null){
			instance = this;
		}
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	private List<Activity> activitys = new ArrayList<Activity>(); //界面集合
	public void addActivity(Activity activity){
		activitys.remove(activity);
		activitys.add(activity);
	}
	public void exit(){
		for(Activity activity : activitys){
			activity.finish();
		}
	}
	public void delActivity(Activity activity){
		activitys.remove(activity);
		activity.finish();
	}
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		Toast.makeText("当前应用已经内存不足，请注意。。。", this, Toast.LENGTH_SHORT).show();
	}
	public List<String> getUIS() {
		return UIS;
	}
	public List<AsyncTask<?,?,?>> getAsyncs() {
		return asyncs;
	}
	public void put(AsyncTask<?,?,?> async){
		asyncs.add(async);
	}
    protected void clearAsyncTask() {
        Iterator<AsyncTask<?,?,?>> iterator = asyncs.iterator();
        while (iterator.hasNext()) {
            AsyncTask<?,?,?> asyncTask = iterator.next();
            if (asyncTask != null && !asyncTask.isCancelled()) {
                asyncTask.cancel(true);
            }
        }
        asyncs.clear();
    }
}
