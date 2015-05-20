package com.framework.hardware;

import java.util.Arrays;

import android.nfc.NdefRecord;
/**
 * 内部接口（用于获取NFC设备里的文本数据）
 * @author lee
 *
 */
public class TextRecord {
	private final String mText;
	private TextRecord(String text) {
		mText = text;
	}
	/**
	 * 获取文本数据
	 * @return
	 */
	public String getText() {
		return mText;
	}
	/**
	 * 解析NDEF数据
	 * @param ndefRecord
	 * @return
	 */
	public static TextRecord parse(NdefRecord ndefRecord) {
		if (ndefRecord.getTnf() != NdefRecord.TNF_WELL_KNOWN) {
			return null;
		}
		if (!Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
			return null;
		}
		try {
			byte[] payload = ndefRecord.getPayload();
			String textEncoding = ((payload[0] & 0x80) == 0) ? "UTF-8"
					: "UTF-16";
			int languageCodeLength = payload[0] & 0x3f;
			String text = new String(payload, languageCodeLength + 1,
					payload.length - languageCodeLength - 1, textEncoding);
			return new TextRecord(text);
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
	}

}
