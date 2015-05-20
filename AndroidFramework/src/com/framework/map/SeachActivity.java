package com.framework.map;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineSearch;
import com.baidu.mapapi.search.busline.BusLineSearchOption;
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import android.os.Bundle;
import android.widget.Toast;
/**
 * 百度地图的各种搜索功能
 * @author lee
 *
 */
public class SeachActivity extends HelloWorldActivity {
	private BaiduMap map;
	private PoiSearch seach;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		map = bmapView.getMap();
	}
	/**
	 * 搜索兴趣点
	 */
	public void seachInterest(){
		seach = PoiSearch.newInstance();
		seach.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
			@Override
			public void onGetPoiResult(PoiResult arg0) {
				//获取POI检索结果
			}
			@Override
			public void onGetPoiDetailResult(PoiDetailResult arg0) {
				//获取Place详情页检索结果  
			}
		});
		//在某个城市中搜索相关兴趣点
		seach.searchInCity(new PoiCitySearchOption()
		.city("北京")   //设置城市
		.keyword("餐厅")  //设置兴趣关键字
		.pageNum(10));  //设置每页显示的数量
	}
	/**
	 * 搜索公交路线
	 */
	public void seachBus(){
		seach = PoiSearch.newInstance();
		seach.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
			@Override
			public void onGetPoiResult(PoiResult result) {
				//获取POI检索结果
				 if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {  
				     return;  
				 }  
			    //遍历所有POI，找到类型为公交线路的POI  
			    for(PoiInfo poi : result.getAllPoi()){
			        if (poi.type == PoiInfo.POITYPE.BUS_LINE ||poi.type == PoiInfo.POITYPE.SUBWAY_LINE) {  
			            //说明该条POI为公交信息，获取该条POI的UID  
			            String busLineId = poi.uid;  
			            //定义监听者和设置监听器的方法与POI中的类似 
			            BusLineSearch busSeach = BusLineSearch.newInstance();
			            busSeach.setOnGetBusLineSearchResultListener(new OnGetBusLineSearchResultListener() {
							@Override
							public void onGetBusLineResult(BusLineResult arg0){
								
							}
						});
			            busSeach.searchBusLine(new BusLineSearchOption().city("北京").uid(busLineId));
			            break;  
			        }  
			    } 
			}
			@Override
			public void onGetPoiDetailResult(PoiDetailResult arg0) {
				//获取Place详情页检索结果  
			}
		});
		//搜索公交路线
		seach.searchInCity(new PoiCitySearchOption()
		.city("北京")
		.keyword("717")); //设置公交路线为717路
	}
	/**
	 * 公交线路规划
	 */
	public void Busplanning(){
		//第一步，创建公交线路规划检索实例；
		RoutePlanSearch seach = RoutePlanSearch.newInstance();
		//第二步，创建公交线路规划检索监听者；并设置
		seach.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {
			//获取公交换乘路径规划结果
			@Override
			public void onGetTransitRouteResult(TransitRouteResult result) {
				 if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {  
			            Toast.makeText(getApplicationContext(), "抱歉，未找到结果", Toast.LENGTH_SHORT).show();  
			        }  
			        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {  
			            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息  
			            //result.getSuggestAddrInfo()  
			            return;  
			        }  
			        if (result.error == SearchResult.ERRORNO.NO_ERROR) {  
			            TransitRouteOverlay overlay = new TransitRouteOverlay(map);  
			            map.setOnMarkerClickListener(overlay);  
			            overlay.setData(result.getRouteLines().get(0));  
			            overlay.addToMap();  
			            overlay.zoomToSpan();  
			        }  
			}
			//获取步行线路规划结果
			@Override
			public void onGetWalkingRouteResult(WalkingRouteResult arg0) {
				
			}	
			//获取驾车线路规划结果
			@Override
			public void onGetDrivingRouteResult(DrivingRouteResult arg0) {
				
			}
		});
		//第三步，准备检索起、终点信息；
		PlanNode stNode = PlanNode.withCityNameAndPlaceName("北京", "龙泽");  
		PlanNode enNode = PlanNode.withCityNameAndPlaceName("北京", "西单");
		//第四步，发起公交线路规划检索；
		seach.transitSearch(new TransitRoutePlanOption()
			    .from(stNode)  //起点 
			    .city("北京")  
			    .to(enNode));  //终点
		
		seach.destroy();
	}
	@Override
	protected void onDestroy() {
		seach.destroy();
		super.onDestroy();
	}
}
