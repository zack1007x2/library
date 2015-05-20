package com.framework.hardware;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
/**
 * 加速度传感器
 */
public class AccessSensorUtil implements ISensor {
	private Context context;
	private SensorManager manager;
	private float x,y,z;
	private Sensor sensor;
	public AccessSensorUtil(Context context) {
		this.context = context;
	}
	@Override
	public void onCreate() {
		manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	}
	@Override
	public void onRaumse(int rate) {
		manager.registerListener(this, sensor, rate);
	}
	@Override
	public void onPause() {
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
	 * 获取X轴上的加速度值
	 */
	public float getX() {
		return x;
	}
	/**
	 * 获取Y轴上的加速度值
	 */
	public float getY() {
		return y;
	}
	/**
	 * 获取Z轴上的加速度值
	 */
	public float getZ() {
		return z;
	}
	
}
