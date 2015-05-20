package com.framework.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.Gradient;
import com.baidu.mapapi.map.HeatMap;
import com.baidu.mapapi.model.LatLng;

import android.graphics.Color;
import android.os.Bundle;
/**
 * 修改地图的显示样式
 * @author lee
 *
 */
public class MapTypeActivity extends HelloWorldActivity {
	private BaiduMap map;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		map = bmapView.getMap();
		map.setMapType(BaiduMap.MAP_TYPE_NORMAL); //普通地图
		map.setMapType(BaiduMap.MAP_TYPE_SATELLITE); //卫星地图
		map.setTrafficEnabled(true); //实时路况的显示
		map.setBaiduHeatMapEnabled(true); //开启百度地图城市热力图
	}
	/**
	 * 自定义热力图
	 */
	public void custom(){
		//第一步，设置颜色变化： 
		//设置渐变颜色值
		int[] DEFAULT_GRADIENT_COLORS = {Color.rgb(102, 225,  0), Color.rgb(255, 0, 0) };
		//设置渐变颜色起始值
		float[] DEFAULT_GRADIENT_START_POINTS = { 0.2f, 1f };
		//构造颜色渐变对象
		Gradient gradient = new Gradient(DEFAULT_GRADIENT_COLORS, DEFAULT_GRADIENT_START_POINTS);
		
		//第二步，准备数据： 

		//以下数据为随机生成地理位置点，开发者根据自己的实际业务，传入自有位置数据即可
		List<LatLng> randomList = new ArrayList<LatLng>();
		Random r = new Random();
		for (int i = 0; i < 500; i++) {
		    // 116.220000,39.780000 116.570000,40.150000
		    int rlat = r.nextInt(370000);
		    int rlng = r.nextInt(370000);
		    int lat = 39780000 + rlat;
		    int lng = 116220000 + rlng;
		    LatLng ll = new LatLng(lat / 1E6, lng / 1E6);
		    randomList.add(ll);
		}
		
		//第三步，添加、显示热力图： 
		//在大量热力图数据情况下，build过程相对较慢，建议放在新建线程实现
		HeatMap heatmap = new HeatMap.Builder()
		    .data(randomList)
		    .gradient(gradient)
		    .build();
		//在地图上添加热力图
		map.addHeatMap(heatmap);
		
//		heatmap.removeHeatMap();   //删除热力图
	}
}
