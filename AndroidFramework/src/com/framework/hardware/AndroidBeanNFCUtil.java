package com.framework.hardware;

import java.nio.charset.Charset;
import java.util.Locale;

import com.framework.base.Toast;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.os.Parcelable;
/**
 * 绑定生命周期方法，调用相应方法
 * 	使用AndroidBean与NFC设备通信
 */
public class AndroidBeanNFCUtil implements CreateNdefMessageCallback, OnNdefPushCompleteCallback{
	private NfcAdapter mNfcAdapter;
	private PendingIntent mPendingIntent;
	private String text;
	private Activity activity;
	/**
	 * 在onCreate()方法中调用
	 * @param activity
	 */
	public AndroidBeanNFCUtil(Activity activity){
		this.activity = activity;
		mNfcAdapter = NfcAdapter.getDefaultAdapter(activity);
		mPendingIntent = PendingIntent.getActivity(activity, 0, new Intent(activity,
				activity.getClass()), 0);
		mNfcAdapter.setNdefPushMessageCallback(this,activity);
		mNfcAdapter.setOnNdefPushCompleteCallback(this, activity);
	}
	/**
	 * 在onResume()方法中调用
	 */
	public void onResume() {
		if (mNfcAdapter != null)
			mNfcAdapter.enableForegroundDispatch(activity, mPendingIntent, null,
					null);
	}
	/**
	 * 在onPause()方法中调用
	 */
	public void onPause() {
		if (mNfcAdapter != null)
			mNfcAdapter.disableForegroundDispatch(activity);
	}
	/**
	 * 设置AndroidBean中的数据text
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
	}
	@Override
	public NdefMessage createNdefMessage(NfcEvent event) {
		NdefMessage message = createTextRecord(text);
		return message;
	}
	/**
	 * 在onNewIntent()方法中调用
	 * @param intent
	 */
	public void onNewIntent(Intent intent){
		Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		NdefMessage msg = (NdefMessage) rawMsgs[0];
		String text = TextRecord.parse(msg.getRecords()[0]).getText();
		Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
	}
	@Override
	public void onNdefPushComplete(NfcEvent event) {
		
	}
	private NdefMessage createTextRecord(String text) {
		byte[] langBytes = Locale.CHINA.getLanguage().getBytes(Charset.forName("US-ASCII"));
		Charset utfEncoding = Charset.forName("UTF-8");
		byte[] textBytes = text.getBytes(utfEncoding);
		int utfBit = 0;
		char status = (char) (utfBit + langBytes.length);
		byte[] data = new byte[1 + langBytes.length + textBytes.length];
		data[0] = (byte) status;
		System.arraycopy(langBytes, 0, data, 1, langBytes.length);
		System.arraycopy(textBytes, 0, data, 1 + langBytes.length,textBytes.length);
		NdefRecord ndefRecord = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,NdefRecord.RTD_TEXT, new byte[0], data);
		NdefMessage ndefMessage = new NdefMessage(new NdefRecord[] { ndefRecord });
		return ndefMessage;
	}
}
