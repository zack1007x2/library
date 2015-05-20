package com.framework.hardware;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;

import com.framework.base.Toast;
import com.framework.util.MyLogger;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Parcelable;
import android.util.SparseArray;
/**
 * 绑定生命周期方法(千万别忘了onNewIntent()方法，该方法也必须在activity的onNewIntent()方法中调用)
 * @author 
 *		<uses-permission android:name="android.permission.NFC"/>
 */
public class NFCUtil {
	private NfcAdapter adapter;
	private PendingIntent mPendingIntent;
	private Activity activity;
	private Tag tag;
	private MyLogger log;
	public NFCUtil(Activity activity){
		log = MyLogger.kLog();
		this.activity = activity;
		adapter = NfcAdapter.getDefaultAdapter(activity);
		mPendingIntent = PendingIntent.getActivity(activity, 0, new Intent(activity,
				activity.getClass()), 0);
	}
	public void onResume() {
		if (adapter != null)
			adapter.enableForegroundDispatch(activity, mPendingIntent, null,
					null);
	}

	public void onPause() {
		if (adapter != null)
			adapter.disableForegroundDispatch(activity);
	}
	/**
	 * 此方法必须在activity的onNewIntent()中调用，否者无法使用NFC
	 * @param intent  activity中该方法的参数
	 */
	public void onNewIntent(Intent intent) {
		tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
	}
	/**
	 * 创建自启动程序   写入NFC贴纸，当NFC贴纸靠近手机时会自动启动包名对应的程序
	 * 	要求手机系统4.0以上
	 * @param mPackageName  写入的包名
	 */
	public void createLaunchAppNFCTag(String mPackageName) {
		if (tag == null) {
			return;
		}
		NdefMessage ndefMessage = new NdefMessage(
				new NdefRecord[] { NdefRecord
						.createApplicationRecord(mPackageName)});
		int size = ndefMessage.toByteArray().length;
		try {
			Ndef ndef = Ndef.get(tag);
			if(ndef!=null){
				ndef.connect();
				if(!ndef.isWritable()){
					return;
				}
				if(ndef.getMaxSize() < size){
					return;
				}
				ndef.writeNdefMessage(ndefMessage);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 当NFC贴纸靠近时手机会自动打开某个网页
	 * @param url  网页地址
	 */
	public void createWebNFCTag(String url){
		if (tag == null) {
			return;
		}
		NdefMessage ndefMessage = new NdefMessage(
				new NdefRecord[] { NdefRecord.createUri(Uri
						.parse(url)) });
		int size = ndefMessage.toByteArray().length;
		try{
			Ndef ndef = Ndef.get(tag);
			if(ndef != null){
				ndef.connect();
				if(!ndef.isWritable()){
					return;
				}
				if(ndef.getMaxSize() < size){
					return;
				}
				ndef.writeNdefMessage(ndefMessage);
			}else {
				//格式化NFC
				NdefFormatable format = NdefFormatable.get(tag);
				if(format != null){
					format.connect();
					format.format(ndefMessage);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 根据text字符串创建一条NDEF数据
	 * @param text 构造NDEF数据的text字符串
	 * @return 代表NDEF数据的对象
	 */
	public NdefMessage createTextRecord(String text) {
		byte[] langBytes = Locale.CHINA.getLanguage().getBytes(
				Charset.forName("US-ASCII"));
		Charset utfEncoding = Charset.forName("UTF-8");
		byte[] textBytes = text.getBytes(utfEncoding);
		int utfBit = 0;
		char status = (char) (utfBit + langBytes.length);
		byte[] data = new byte[1 + langBytes.length + textBytes.length];
		data[0] = (byte) status;
		System.arraycopy(langBytes, 0, data, 1, langBytes.length);
		System.arraycopy(textBytes, 0, data, 1 + langBytes.length,
				textBytes.length);
		NdefRecord ndefRecord = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
				NdefRecord.RTD_TEXT, new byte[0], data);
		NdefMessage ndefMessage = new NdefMessage(
				new NdefRecord[] { ndefRecord });
		return ndefMessage;
	}
	/**
	 * 将NDEF数据写入NFC设备
	 * @param message
	 */
	public void writeNDEFMessage(NdefMessage message){
		Ndef ndef = Ndef.get(tag);
		try {
			ndef.connect();
			ndef.writeNdefMessage(message);
		} catch (IOException e) {
			log.i("NFC连接失败...");
			e.printStackTrace();
		} catch (FormatException e) {
			log.i("NDEF格式错误...");
			e.printStackTrace();
		}
	}
	/**
	 * 读取NFC设备中的数据(NDEF)
	 * @return
	 */
	public TextRecord readNFCTag() {
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(activity.getIntent().getAction())) {
			Parcelable[] rawMsgs = activity.getIntent().getParcelableArrayExtra(
					NfcAdapter.EXTRA_NDEF_MESSAGES);
			NdefMessage msgs[] = null;
			if (rawMsgs != null) {
				msgs = new NdefMessage[rawMsgs.length];
				for (int i = 0; i < rawMsgs.length; i++) {
					msgs[i] = (NdefMessage) rawMsgs[i];
				}
			}
			try {
				if (msgs != null) {
					NdefRecord record = msgs[0].getRecords()[0];
					TextRecord textRecord = TextRecord.parse(record);
					return textRecord;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * 写入MifareUltralight数据
	 * @param arr
	 * @throws Exception
	 */
	public void writeMifareUltralight(SparseArray arr) throws Exception{
		String[] techList =tag.getTechList();
		boolean haveMifareUltralight = false;
    	for(String tech: techList){
    		if(tech.indexOf("MifareUltralight") >= 0){
    			haveMifareUltralight = true;
    			break;
    		}
    	}
    	if(!haveMifareUltralight){
    		Toast.makeText("不支持MifareUltralight数据格式",activity, Toast.LENGTH_LONG).show();
    		return;
    	}
		MifareUltralight mif = MifareUltralight.get(tag);
		mif.connect();
		for(int i = 0 ; i < arr.size() ; i++){
			mif.writePage(i, arr.get(i).toString().getBytes());
		}
		Toast.makeText("成功写入MifareUltralight格式数据!",activity, Toast.LENGTH_LONG).show();
		mif.close();
	}
	/**
	 * 读取MifareUltralight数据
	 * @return
	 */
	public String readMifareUltralight(){
		MifareUltralight ultralight = MifareUltralight.get(tag);
		try{
			ultralight.connect();
			byte[] data = ultralight.readPages(4);
			return new String(data, Charset.forName("GB2312"));
		}catch (Exception e) {
			log.i("MifareUltralight对象读取异常");
			e.printStackTrace();
		}finally{
			try{
				ultralight.close();
			}catch (Exception e) {
				log.i("MifareUltralight对象关闭异常");
				e.printStackTrace();
			}
		}
		return null;
	}
}
