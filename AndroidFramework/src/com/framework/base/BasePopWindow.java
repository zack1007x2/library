package com.framework.base;

import java.lang.reflect.Field;

import com.framework.util.MyLogger;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.PopupWindow;
import android.widget.Spinner;
/**
 * 弹窗的页面基类
 * @author  lee
 */
public abstract class BasePopWindow extends PopupWindow implements OnItemSelectedListener, OnItemClickListener, OnItemLongClickListener, OnClickListener, OnLongClickListener {
	private View view;
	private BaseFragment frag;
	public MyLogger Log;
	
	public BasePopWindow(View view,int width,int height,boolean focusable){
		super(view, width, height, focusable);
		Log = MyLogger.kLog();
		this.view = view;
		findViews();
		initData();
		setListener();
	}
	public BasePopWindow(BaseFragment frag,View view,int width,int height,boolean focusable){
		super(view, width, height, focusable);
		Log = MyLogger.kLog();
		this.frag = frag;
		this.view = view;
		findViews();
		initData();
		setListener();
	}
	public abstract void findViews();
	public abstract void initData();
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
	public void setFrag(BaseFragment frag) {
		this.frag = frag;
	}
	/**
	 * 获取窗体索依附的Activity
	 * 如果fragment存在返回fragment所依附的Activity  如果不存在返回null<br />
	 * 可以调用setFrag()方法设置Fragment
	 * @return
	 */
	public <D extends BaseActivity> D getActivity(){
		if(frag != null)
			return (D) frag.getActivity();
		else
			return null;
	}
	/**
	 * 获取PopWindow上显示的View
	 * @return
	 */
	public View getPopView(){
		return view;
	}
	public <T extends View> T findViewById(int id) {
		return (T) view.findViewById(id);
	}
	@Override
	public boolean onLongClick(View v) {
		return false;
	}
	@Override
	public void onClick(View v) {
		
	}
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		return false;
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
	}
	@Override
	public void onNothingSelected(AdapterView<?> parent){
	}
}
