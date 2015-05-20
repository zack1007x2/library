package com.framework.base;
import java.util.List;

import cn.com.framework.R;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
/**
 * 以HorizontalScrollView+Fragment组合的activity
 * @author lee
 */
public class BaseSorllActivity extends BaseActivity {
	private List<BaseFragment> frags;
	private FragmentManager manager;
	@Override
	public void onCreate(SharedPreferences sp, FragmentManager manager,Bundle savedInstanceState) {
		this.manager = manager;
		setContentView(R.layout.activity_soll_base);
		findViewById(R.id.btn_allow);
	}
	/**
	 * 设置标题栏视图
	 * @param view
	 */
	public void setTitleView(View view){
		LinearLayout linear_nav = findViewToId(R.id.linear_nav);
		linear_nav.removeAllViews();
		linear_nav.addView(view);
	}
	/**
	 * 设置内容视图
	 * @param frags
	 */
	public void setContentView(List<BaseFragment> frags){
		this.frags = frags;
		FragmentTransaction ft = manager.beginTransaction();
		ft.replace(R.id.frame_content, frags.get(0));
		ft.commit();
	}
	@Override
	public final void jumpFragment(BaseFragment frag, String tag) {
		Fragment fragment = manager.findFragmentByTag(tag);
		FragmentTransaction ft = manager.beginTransaction();
		if(fragment != null){
			ft.remove(fragment);
		}
		ft.replace(R.id.frame_content, frag, tag);
		if(tag != null){
			ft.addToBackStack(tag);
		}
		ft.commit();
	}
	/**
	 * 选中内容视图
	 * @param index 内容视图的索引
	 * @param tag 内容视图的标记
	 */
	public void switchView(int index,String tag){
		BaseFragment frag = frags.get(index);
		jumpFragment(frag, tag);
	}
}