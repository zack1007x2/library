package com.framework.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.framework.net.DialogTools;
import com.framework.pay.UnionpayUtil;

public class UnionpayUtilActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DialogTools dialog = new DialogTools();
		dialog.createLoadDialog(this, "this is a test dialog");
//		UnionpayUtil util = new UnionpayUtil(this);
//		util.pay();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		 if (data == null) {
	            return;
	     }
        String msg = "";
        //支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
        String str = data.getExtras().getString("pay_result");
        if(str.equalsIgnoreCase("success")) {
            msg = "支付成功！";
        } else if (str.equalsIgnoreCase("fail")) {
            msg = "支付失败！";
        } else if (str.equalsIgnoreCase("cancel")) {
            msg = "用户取消了支付";
        }
        System.out.println("pay_result="+str);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("支付结果通知");
        builder.setMessage(msg);
        builder.setInverseBackgroundForced(true);
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
	}
}
