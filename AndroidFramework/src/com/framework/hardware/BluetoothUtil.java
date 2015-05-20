package com.framework.hardware;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;
/**
 * 蓝牙工具类
 */
public class BluetoothUtil {
	private BluetoothAdapter adapter;
	private Context context;
	private BluetoothListener listener;
	private List<BluetoothDevice> devices;
	private BluetoothSocket socket;
	public BluetoothUtil(Context context){
		adapter = BluetoothAdapter.getDefaultAdapter();
		this.context = context;
		devices = new ArrayList<BluetoothDevice>();
	}
	
	public void setListener(BluetoothListener listener) {
		this.listener = listener;
	}
	/**
	 * 打开蓝牙
	 */
	public void openBluetooth(){
		if (adapter == null){    
			Toast.makeText(context, "该机不支持蓝牙！", Toast.LENGTH_SHORT).show();
		}
		if (!adapter.isEnabled()){  
		    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);  
		    intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);  
		    context.startActivity(intent);  
		    //adapter.enable();
		}
	}
	/**
	 * 获取已经配对的蓝牙设配
	 * @return
	 */
	public Set<BluetoothDevice> getPairedBluetooth(){
		Set<BluetoothDevice> devices = adapter.getBondedDevices();  
		return devices;
	}
	/**
	 * 搜索周围的蓝牙设备
	 */
	public void seachBluetooth(){
		IntentFilter intentFilter = new IntentFilter();  
		intentFilter.addAction(BluetoothDevice.ACTION_FOUND);  
		intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);  
		intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);  
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);  
		context.registerReceiver(receiver, intentFilter);  
		adapter.startDiscovery(); 
	}
	/**
	 * 连接到指定的蓝牙设配
	 * @param device
	 * @throws Exception
	 */
	public void connectBluetooth(BluetoothDevice device) throws Exception{
		 final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";  //可到 http://www.uuidgenerator.com 申请 
		 UUID uuid = UUID.fromString(SPP_UUID);  
		 socket = device.createRfcommSocketToServiceRecord(uuid);  
		 socket.connect();
	}
	private BroadcastReceiver receiver = new BroadcastReceiver(){
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();  
	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            devices.add(device);
	        }
	        if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){ //搜索完成
	        	listener.seach(devices);
	        }
		}
	};
	public interface BluetoothListener{
		public void seach(List<BluetoothDevice> devices);
	}
	/**
	 * 关闭本机的蓝牙
	 */
	public void closeBluetooth(){
		if(adapter != null && adapter.isEnabled())
			adapter.disable();
	}
	/**
	 * 发送数据到指定蓝牙设配
	 * @param data
	 * @throws IOException
	 */
	public void sendDataToBluetooth(byte[] data) throws IOException{
		if(socket != null){
			OutputStream out = socket.getOutputStream();
			out.write(data);
		}else{
			throw new NullPointerException("目标蓝牙未连接...");
		}
	}
}
