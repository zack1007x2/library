package com.framework.hardware;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class GPSUtil {
	private LocationManager manager;
	
	public GPSUtil(Context context,final GPSListener listener){
		manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 8, new LocationListener() {
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				
			}
			//当GPS Location Provider可用时，更新位置
			@Override
			public void onProviderEnabled(String provider) {
				GPSParams gpsParams = get(manager.getLastKnownLocation(provider));
				listener.updateGPS(gpsParams);
			}
			@Override
			public void onProviderDisabled(String provider) {
				
			}
			//  当GPS定位信息发生改变时，更新位置
			@Override
			public void onLocationChanged(Location location) {
				GPSParams gpsParams = get(location);
				listener.updateGPS(gpsParams);
			}
		});
	}
	public GPSParams get(Location newLocation){
		GPSParams params = null;
		if(newLocation != null){
			params = new GPSParams();
			params.longitude = newLocation.getLongitude();
			params.latitude = newLocation.getLatitude();
			params.altitude = newLocation.getAltitude();
			params.bearing = newLocation.getBearing();
			params.speed = newLocation.getSpeed();
		}
		return params;
	}

}
