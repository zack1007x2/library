package com.framework.hardware;
import android.hardware.SensorEventListener;
public interface ISensor extends SensorEventListener{
	public void onCreate();
	public void onRaumse(int rate);
	public void onPause();
}