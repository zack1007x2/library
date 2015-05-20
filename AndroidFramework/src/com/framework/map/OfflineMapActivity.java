package com.framework.map;

import com.baidu.mapapi.map.BaiduMap;
import android.os.Bundle;

/**
 * 离线地图
 * @author lee
 * 离线地图资源可通过手动导入和接口下载两种方式获取。 
手动导入方法如下： 
先将从官网下载的离线包解压，把vmp文件夹拷入SD卡根目录下的BaiduMapSDK文件夹内 
注意：Android4.4及以上系统的设备（且存在外置SD卡），需要将vmp文件夹拷贝到sdcard/Android/Data/应用程序包名/BaiduMapSDK。 
 */
public class OfflineMapActivity extends HelloWorldActivity {
	private BaiduMap map;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		map = bmapView.getMap();
		
	}
}
