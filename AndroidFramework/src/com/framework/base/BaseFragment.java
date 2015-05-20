package com.framework.base;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.framework.manager.PagerManager;
import com.framework.util.MyLogger;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
/**
 * Fragment的基类
 * @author lee
 *		无需为view设置监听操作，只需要声明一个int[]类型的ids变量，把所有要监听的控件id放到该数组中即可，同时需要继承该类
 */
public abstract class BaseFragment extends Fragment implements OnClickListener, OnLongClickListener, OnItemClickListener, OnItemLongClickListener, OnItemSelectedListener {
	private SharedPreferences sp;
	public FragmentManager manager;
	public View view;
	public MyLogger Log;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Log = MyLogger.kLog();
		manager = getFragmentManager();
		sp = getActivity().getSharedPreferences(SPContants.CONFIG, Context.MODE_PRIVATE);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		view = onCreateView(inflater, sp,savedInstanceState);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		findViews();
	}
	@Override
	public void onStart() {
		super.onStart();
		setListener();
	}
	@Override
	public void onResume() {
		super.onResume();
		initData();
	}
	@Override
	public void onDestroy() {
		BaseAppcation.getInstance().clearAsyncTask();
		super.onDestroy();
	}
	/**
	 * 监听事件重写了，只需要你手动声明一个以ids为变量名的int[] 该方法会自动为在这个数组里的控件设置上事件监听
	 */
	private void setListener() {
		try {
			Field field = getClass().getField("ids");
			int[] ids =  (int[]) field.get(this);
			if(ids != null && ids.length > 0)
				for(int id : ids){
					View view = findViewById(id);
					if(view instanceof AdapterView){
						if(view instanceof Spinner){
							((Spinner)view).setOnItemSelectedListener(this);
							continue;
						}
						((AdapterView<?>)view).setOnItemClickListener(this);
						((AdapterView<?>)view).setOnItemLongClickListener(this);
						continue;
					}
					view.setOnClickListener(this);
					view.setOnLongClickListener(this);
				}
		} catch (Exception e) {
		}
	}
	public abstract View onCreateView(LayoutInflater inflater,SharedPreferences sp,Bundle savedInstanceState);
	public abstract void findViews();
	public abstract void initData();
	public <T extends View> T findViewById(int resId){
		return (T) view.findViewById(resId);
	}
	@Override
	public void onClick(View v) {
	}
	@Override
	public boolean onLongClick(View v) {
		return false;
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		return false;
	}
	/**
	 * Spinner控件的事件监听
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}
	/**
	 * 显示Pager视图,一旦启动这种加载模式，会降低视图层对内存的使用率，可以更好的优化内存
	 * @param pager
	 */
	public void showPager(BasePager pager,String tag){
		View child  = pager.getView();
		if(child != null){
			//page页面视图View的setTag()方法被占用了。以便于寻找当前显示页
			child.setTag(pager);
			if(view instanceof ViewGroup){
				ViewGroup parent = (ViewGroup)view;
				parent.removeAllViews();
				parent.addView(child);
			}
		}
	}
	/**
	 * 获取当前正在显示Page页面，以便处理返回键的事件传递，如果获取不到将返回null
	 * @return
	 */
	public BasePager getPager(){
		if(view != null && view instanceof ViewGroup){
			ViewGroup group = (ViewGroup) view;
			Object obj = group.getChildAt(0).getTag();
			if(obj != null && obj instanceof BasePager){
				return (BasePager) obj;
			}
		}
		return null;
	}
}
