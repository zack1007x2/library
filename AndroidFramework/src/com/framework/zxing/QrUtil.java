package com.framework.zxing;

import java.util.Hashtable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
/**
 * 二维码工具类
 * @author asus1
 */
public class QrUtil {
	/**
	 * 根据字符传信息生成一张二维码图片
	 * @param string
	 * @return
	 */
	public static Bitmap qr_code(String string){
		MultiFormatWriter writer = new MultiFormatWriter();
		Hashtable<EncodeHintType, String> hst = new Hashtable<EncodeHintType, String>();
		hst.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		Bitmap bitmap = null;
		try {
			BitMatrix matrix = writer.encode(string,BarcodeFormat.QR_CODE, 400, 400, hst);
			int width = matrix.getWidth();
			int height = matrix.getHeight();
			int[] pixels = new int[width * height];
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (matrix.get(x, y)) {
						pixels[y * width + x] = 0xff000000;
					}
				}
			}
			bitmap = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	/**
	 * 启动扫描器  
	 * @param activity activity自身  this <br /><br />
	 * 		扫描后的结果数据封装：  <br />
	 * 	Intent resultIntent = new Intent();  <br />
		Bundle bundle = new Bundle();  <br />
		bundle.putString("result", resultString);  <br />
		bundle.putParcelable("bitmap", barcode);  <br />
		resultIntent.putExtras(bundle);  <br />
		setResult(RESULT_OK, resultIntent);  <br />
	 */
	public void startCapture(Activity activity){
		Intent openCameraIntent = new Intent(activity,MipcaActivityCapture.class);
		activity.startActivityForResult(openCameraIntent, 120);
	}
}
