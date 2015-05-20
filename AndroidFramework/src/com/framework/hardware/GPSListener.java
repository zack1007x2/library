package com.framework.hardware;
/**
 * GPS的信息闸口，可以取到一切有关GPS的信息
 * @author lee
 *
 */
public interface GPSListener {
	/**
	 * 当GPS信息发生变化时调用，该方法可以更新手机的GPS信息
	 * @param gpsParams GPS信息
	 */
	void updateGPS(GPSParams gpsParams);

}
