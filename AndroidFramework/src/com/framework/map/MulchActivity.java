package com.framework.map;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import cn.com.framework.R;

import android.os.Bundle;
import android.widget.Button;
/**
 * 在百度地图上添加覆盖物
 * @author lee
 *
 */
public class MulchActivity extends HelloWorldActivity {
	private BaiduMap map;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		map = bmapView.getMap();
		LatLng point = new LatLng(39.963175, 116.400244);  
		//构建Marker图标  
		BitmapDescriptor bitmap = BitmapDescriptorFactory  
		    .fromResource(R.drawable.oer);  
		
		//构建MarkerOption，用于在地图上添加Marker  
		OverlayOptions option = new MarkerOptions()
			.position(point)   //设置覆盖物的位置
			.icon(bitmap)  //设置覆盖物上的图标    可以设置动画：通过marker的icons设置一组图片，再通过period设置多少帧刷新一次图片资源
			.zIndex(9)  //设置marker所在层级
			.draggable(true);  //设置可手势拖拽
		
		//在地图上添加Marker，并显示  
		Marker overlay = (Marker) map.addOverlay(option);
		//overlay.remove();  //调用Marker对象的remove方法实现指定marker的删除 
		
		//设置拖拽监听
		map.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
			
			@Override
			public void onMarkerDragStart(Marker arg0) {
				//开始拖拽时调用
			}
			
			@Override
			public void onMarkerDragEnd(Marker arg0) {
				 //拖拽结束时调用
			}
			
			@Override
			public void onMarkerDrag(Marker arg0) {
				//拖拽时调用
			}
		});
	}
	/**
	 * 添加几何图形覆盖物
	 * 
	 * 地图SDK提供多种结合图形覆盖物，利用这些图形，可帮助您构建更加丰富多彩的地图应用。目前提供的几何图形有：点（Dot）、折线（Polyline）、弧线（Arc）、圆（Circle）、多边形（Polygon）
	 */
	public void geometric(){
		//定义多边形的五个顶点  
		LatLng pt1 = new LatLng(39.93923, 116.357428);  
		LatLng pt2 = new LatLng(39.91923, 116.327428);  
		LatLng pt3 = new LatLng(39.89923, 116.347428);  
		LatLng pt4 = new LatLng(39.89923, 116.367428);  
		LatLng pt5 = new LatLng(39.91923, 116.387428);  
		List<LatLng> pts = new ArrayList<LatLng>();  
		pts.add(pt1);  
		pts.add(pt2);  
		pts.add(pt3);  
		pts.add(pt4);  
		pts.add(pt5);  
		//构建用户绘制多边形的Option对象  
		OverlayOptions polygonOption = new PolygonOptions()  
		    .points(pts)  
		    .stroke(new Stroke(5, 0xAA00FF00))  
		    .fillColor(0xAAFFFF00);  
		//在地图上添加多边形Option，用于显示  
		map.addOverlay(polygonOption);
	}
	/**
	 * 文字覆盖物
	 */
	public void chinese(){
		//定义文字所显示的坐标点  
		LatLng llText = new LatLng(39.86923, 116.397428);  
		//构建文字Option对象，用于在地图上添加文字  
		OverlayOptions textOption = new TextOptions()  
		    .bgColor(0xAAFFFF00)  
		    .fontSize(24)  
		    .fontColor(0xFFFF00FF)  
		    .text("百度地图SDK")  
		    .rotate(-30)  
		    .position(llText);  
		//在地图上添加该文字对象并显示  
		map.addOverlay(textOption);
	}
	/**
	 * 在地图上自定义布局并显示
	 */
	public void layout(){
		//创建InfoWindow展示的view  
		Button button = new Button(getApplicationContext());  
		button.setBackgroundResource(R.drawable.ic_launcher);  
		//定义用于显示该InfoWindow的坐标点  
		LatLng pt = new LatLng(39.86923, 116.397428);  
		//创建InfoWindow , 传入 view， 地理坐标， y轴偏移量 
		InfoWindow mInfoWindow = new InfoWindow(button, pt, -47);  
		//显示InfoWindow  
		map.showInfoWindow(mInfoWindow);
	}
	/**
	 * 地形图图层
	 * 地形图图层（GroundOverlay），又可叫做图片图层，即开发者可在地图的指定位置上添加图片。
	 * 该图片可随地图的平移、缩放、旋转等操作做相应的变换。该图层是一种特殊的Overlay， 它位于底图和底图标注层之间（即该图层不会遮挡地图标注信息）。 
	 */
	public void terrain(){
		//定义Ground的显示地理范围  
		LatLng southwest = new LatLng(39.92235, 116.380338);  
		LatLng northeast = new LatLng(39.947246, 116.414977);  
		LatLngBounds bounds = new LatLngBounds.Builder()  
		    .include(northeast)  
		    .include(southwest)  
		    .build();  
		//定义Ground显示的图片  
		BitmapDescriptor bdGround = BitmapDescriptorFactory  
		    .fromResource(R.drawable.ic_launcher);  
		//定义Ground覆盖物选项  
		OverlayOptions ooGround = new GroundOverlayOptions()  
		    .positionFromBounds(bounds)  
		    .image(bdGround)  
		    .transparency(0.8f);  
		//在地图中添加Ground覆盖物  
		map.addOverlay(ooGround);
	}
}
