package com.framework.map;
import com.baidu.mapapi.map.MapView;
import cn.com.framework.R;
import android.app.Activity;
import android.os.Bundle;
/**
 * 集成百度地图的开发环境
 * @author lee
 *
 */
public class HelloWorldActivity extends Activity {
	protected MapView bmapView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现    
        setContentView(R.layout.activity_baidu_helloworld);
        bmapView = (MapView) findViewById(R.id.bmapView);
	}
	@Override
	protected void onResume() {
		super.onResume();
		bmapView.onResume();
	}
	@Override
	protected void onPause() {
		super.onPause();
		bmapView.onPause();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		bmapView.onDestroy();
	}
}
