package com.framework.manager;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import android.view.View;
import android.view.ViewGroup;

import com.framework.base.BasePager;
/**
 * Page页面的管理者对象,该类有待测试，目前已取消使用
 * @author lee
 */
public class PagerManager {
	private static PagerManager manager = new PagerManager();
	private Map<String, BasePager> pages = new LinkedHashMap<String, BasePager>();
	private PagerManager(){
		
	}
	public static PagerManager getInstance(){
		return manager;
	}
	/**
	 * 进栈
	 * @param tag
	 * @param page
	 */
	public void addToBackStace(String tag,BasePager page){
		pages.put(tag, page);
	}
	/**
	 * 在栈中寻找出Page页面
	 * @param tag
	 * @return
	 */
	public BasePager findPageByTag(String tag){
		return pages.get(tag);
	}
	/**
	 * 获取栈底的Page页面
	 * @return
	 */
	public BasePager getBottomPage(){
		return (BasePager) pages.values().toArray()[pages.values().size() - 1];
	}
	/**
	 * 将指定Page页面出栈
	 * @param tag
	 * @return
	 */
	public BasePager popToBackStace(String tag){
		 return pages.remove(tag);
	}
	/**
	 * 将指定Page页面出栈
	 * @param pager Page页面
	 * @return  如果Page栈中不包含此页面将返回空，否则返回该Page页面
	 */
	public BasePager popToBackStace(BasePager pager){
		if(pages.containsValue(pager)){
			Set<String> keySet = pages.keySet();
			Iterator<String> it = keySet.iterator();
			while(it.hasNext()){
				String key = it.next();
				BasePager pager2 = pages.get(key);
				if(pager2.equals(pager)){
					return pages.remove(key);
				}
			}
		}
		return null;
	}
	public void showPager(BasePager pager,String tag){
		View child  = pager.getView();
		if(child != null){
			child.setTag(pager);
			View view = pager.getFragment().getView();
			if(view instanceof ViewGroup){
				ViewGroup parent = (ViewGroup)view;
				parent.removeAllViews();
				parent.addView(child);
			}
		}
	}

}
