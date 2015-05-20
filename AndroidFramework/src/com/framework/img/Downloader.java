package com.framework.img;


public interface Downloader  {
	
	/**
	 * 请求网络的inputStream填充outputStream
	 * @param urlString
	 * @param outputStream
	 * @return
	 */
	public byte[] download(String urlString);
}
