package com.framework.map;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviPara;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.baidu.mapapi.utils.DistanceUtil;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

/**
 * 距离计算、坐标转换和调启导航等。帮助开发者实现丰富的LBS功能。
 * @author lee
 *
 */
public class NavActivity extends HelloWorldActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	/**
	 * 启动百度地图App导航功能
	 */
	public void startNavi() {  
		//定义起、终点
	    LatLng pt1 = new LatLng(41, 115);  
	    LatLng pt2 = new LatLng(117, 39);  
	    // 构建 导航参数  
	    NaviPara para = new NaviPara();  
	    para.startPoint = pt1;  
	    para.startName = "从这里开始";  
	    para.endPoint = pt2;  
	    para.endName = "到这里结束";  
	    try {  
	    	//启动百度地图并开始导航
	        BaiduMapNavigation.openBaiduMapNavi(para, this);  
	    } catch (BaiduMapAppNotSupportNaviException e) {  //如果未安装百度地图App或App版本过低就会抛出该异常
	        e.printStackTrace();  
	        AlertDialog.Builder builder = new AlertDialog.Builder(this);  
	        builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");  
	        builder.setTitle("提示");  
	        builder.setPositiveButton("确认", new OnClickListener() {  
	            @Override  
	            public void onClick(DialogInterface dialog, int which) {  
		            dialog.dismiss();  
		            BaiduMapNavigation.getLatestBaiduMapApp(NavActivity.this);  
	            }  
	        });  
	        builder.setNegativeButton("取消", new OnClickListener() {  
	            @Override  
	            public void onClick(DialogInterface dialog, int which) {  
	            	dialog.dismiss();  
	            }  
	        });  
	        builder.show();  
	    }  
	}
	/**
	 * 计算两个点的直线距离
	 */
	public void distance(){
		LatLng p1 = new LatLng(41, 115);  
	    LatLng p2 = new LatLng(117, 39);
		DistanceUtil.getDistance(p1, p2);		
	}
	/**
	 * 坐标转换
	 */
	public void Lalng(){
		// 将google地图、soso地图、aliyun地图、mapabc地图和amap地图// 所用坐标转换成百度坐标
		LatLng sourceLatLng = new LatLng(41, 115); //转换前的坐标点
		CoordinateConverter converter  = new CoordinateConverter();  
		converter.from(CoordType.COMMON);  
		// sourceLatLng待转换坐标  
		converter.coord(sourceLatLng);  
		LatLng desLatLng = converter.convert();  //转换后的坐标点
/*		 
		// 将GPS设备采集的原始GPS坐标转换成百度坐标  
		CoordinateConverter converter  = new CoordinateConverter();  
		converter.from(CoordType.GPS);  
		// sourceLatLng待转换坐标  
		converter.coord(sourceLatLng);  
		LatLng desLatLng = converter.convert();
*/
	}
}
