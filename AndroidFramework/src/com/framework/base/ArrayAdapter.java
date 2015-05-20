package com.framework.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
/**
 * 数组模型的适配器 
 * @author lee
 * @param <T> 数组的类型
 */
public abstract class ArrayAdapter<T> extends BaseAdapter{
	public T[] data;
	public ArrayAdapter(T[] data){
		this.data = data;
	}
	@Override
	public int getCount() {
		if(data != null)
			return data.length;
		return 0;
	}
	@Override
	public T getItem(int position) {
		if(data != null)
			return data[position];
		return null;
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public abstract View getView(int position, View convertView, ViewGroup parent);
}
