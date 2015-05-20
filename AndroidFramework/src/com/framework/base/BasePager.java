package com.framework.base;

import java.lang.reflect.Field;

import com.framework.manager.PagerManager;
import com.framework.util.MyLogger;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
/**
 * 如果启动该类。数据会优化到极致，整个项目只会有一个Activity.Fragment  剩下的全是以该类作为基类的入口
 * 	(但是返回键就会失去功效，如需处理返回键和其他按键的事件则需要从activity传到fragment在从fragment传到pager页面中，方法需要自己思考)
 * @author lee
 */
public abstract class BasePager implements OnItemSelectedListener, OnItemClickListener, OnItemLongClickListener, OnClickListener, OnLongClickListener {
	private View view;
	public MyLogger Log;
	private BaseFragment frag;
	/**
	 * @param frag 当前Page所依附的Fragment
	 */
	public BasePager(BaseFragment frag){
		this.frag = frag;
		SharedPreferences sp = frag.getActivity().getSharedPreferences(SPContants.CONFIG, Context.MODE_PRIVATE);
		Log = MyLogger.kLog();
		view = initView(sp);
		findViews();
		setListener();
		initData();
	}
	public abstract View initView(SharedPreferences sp);
	public abstract void findViews();
	public abstract void initData();
	/**
	 * 获取当前页所依附的Fragment
	 * @return
	 */
	public BaseFragment getFragment(){
		return frag;
	}
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
	public <T extends View> T findViewById(int resId){
		return (T) view.findViewById(resId);
	}
	public View getView(){
		return view;
	}
	/**
	 * 普通控件点击事件
	 */
	@Override
	public void onClick(View v) {
	}
	/**
	 * 普通控件长按点击事件
	 */
	@Override
	public boolean onLongClick(View v) {
		return false;
	}
	/**
	 * ListView的item点击事件
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}
	/**
	 * ListView的item长按事件监听
	 */
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
	 * 处理返回键逻辑和其他系统按键逻辑
	 * @param keyCode
	 * @param event
	 */
}