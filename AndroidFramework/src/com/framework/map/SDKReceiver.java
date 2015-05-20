package com.framework.map;

import com.baidu.mapapi.SDKInitializer;
import com.framework.base.Toast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * 百度地图key验证结果广播接收者
 * @author lee
 *	IntentFilter iFilter = new IntentFilter();  
	iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);  
	iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);  
	mReceiver = new SDKReceiver();  
	registerReceiver(mReceiver, iFilter);
 */
public class SDKReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction() == SDKInitializer.SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_CODE){ //key验证错误
			Toast.makeText("key验证失败", context, Toast.LENGTH_SHORT).show();
		}
	}
}
