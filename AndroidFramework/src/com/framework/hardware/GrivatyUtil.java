package com.framework.hardware;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

/**
 * 重力传感器工具类（推荐使用，绑定生命周期方法）
 * @author lee
 *
 */
public class GrivatyUtil implements ISensor {
	private Context context;
	private SensorManager manager;
	private Sensor sensor;
	private float x,y,z;
	public GrivatyUtil(Context context) {
		this.context = context;
	}

	public void onCreate(){
		manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		sensor = manager.getDefaultSensor(Sensor.TYPE_GRAVITY);
	}
	public void onRaumse(int rate){
		manager.registerListener(this, sensor, rate);
	}
	public void onPause(){
		manager.unregisterListener(this);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float[] values = event.values;
		x = values[0];
		y = values[1];
		z = values[2];
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}
	/**
	 * 获取X轴上的重力
	 * @return
	 */
	public float getX() {
		return x;
	}
	/**
	 * 获取Y轴上的重力
	 * @return
	 */
	public float getY() {
		return y;
	}
	/**
	 * 获取Z轴上的重力
	 * @return
	 */
	public float getZ() {
		return z;
	}
}
