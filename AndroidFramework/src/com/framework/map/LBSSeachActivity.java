package com.framework.map;

import com.baidu.mapapi.cloud.CloudListener;
import com.baidu.mapapi.cloud.CloudManager;
import com.baidu.mapapi.cloud.CloudSearchResult;
import com.baidu.mapapi.cloud.DetailSearchResult;
import com.baidu.mapapi.cloud.LocalSearchInfo;

import android.os.Bundle;

/**
 * LBS云检索功能
 * @author lee
 *
 */
public class LBSSeachActivity extends HelloWorldActivity implements CloudListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CloudManager manager = CloudManager.getInstance();
		manager.init(this);
		LocalSearchInfo info = new LocalSearchInfo();  
		info.ak = "B266f735e43ab207ec152deff44fec8b";  
		info.geoTableId = 31869;  
		info.tags = "";  
		info.q = "天安门";  
		info.region = "北京市";  
		manager.localSearch(info);
	}

	@Override
	public void onGetDetailSearchResult(DetailSearchResult result, int error) {
		
	}
	
	@Override
	public void onGetSearchResult(CloudSearchResult result, int error) {
		//在此处理相应的检索结果
	}
}
