package com.framework.base;

import java.lang.reflect.Field;

import com.framework.util.MyLogger;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

public class BaseDialog extends Dialog implements OnItemSelectedListener, OnItemClickListener, OnItemLongClickListener, android.view.View.OnClickListener, OnLongClickListener {
	public MyLogger Log;
	public BaseDialog(Context context) {
		super(context);
		Log = MyLogger.kLog();
		setListener();
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
	public <T extends View> T findViewToId(int resid){
		return (T) super.findViewById(resid);
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
	public void onNothingSelected(AdapterView<?> parent) {
	}
}
