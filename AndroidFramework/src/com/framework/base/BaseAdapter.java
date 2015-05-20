package com.framework.base;

import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseAdapter<D> extends android.widget.BaseAdapter {
	public List<D> data = new ArrayList<D>();
	
	public BaseAdapter(List<D> data) {
		this.data.addAll(data);
	}
	@Override
	public int getCount() {
		return data.size();
	}
	@Override
	public D getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public abstract View getView(int position, View convertView, ViewGroup parent);
}
