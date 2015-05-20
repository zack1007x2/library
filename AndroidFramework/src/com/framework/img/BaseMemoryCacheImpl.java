package com.framework.img;

import android.graphics.Bitmap;

public class BaseMemoryCacheImpl implements IMemoryCache {

	private final LruMemoryCache<String, Bitmap> mMemoryCache;
	
	public BaseMemoryCacheImpl(int size) {
		mMemoryCache = new LruMemoryCache<String, Bitmap>(size) {
             @Override
             protected int sizeOf(String key, Bitmap bitmap) {
                 return bitmap.getRowBytes() * bitmap.getHeight(); 
             }
         }; 
	}
	
	@Override
	public void put(String key, Bitmap bitmap) {
		mMemoryCache.put(key, bitmap);
	}

	@Override
	public Bitmap get(String key) {
		return mMemoryCache.get(key);
	}

	@Override
	public void evictAll() {
		mMemoryCache.evictAll();
	}

	@Override
	public void remove(String key) {
		mMemoryCache.remove(key);
	}


}
