package com.framework.net;

import org.json.JSONException;

/**
 * 网络数据处理
 * @author lee
 * 
 */
public interface OnResultListener {
	/**
	 * 结果处理
	 * @param result
	 * @param iError
	 *            :结果的状态码
	 */
	void onGetResult (Object result, int iError) throws Exception;
}
