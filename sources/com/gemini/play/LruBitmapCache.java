package com.gemini.play;

import android.graphics.Bitmap;
import android.util.LruCache;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.gemini.application.GeminiApplication;

public class LruBitmapCache implements ImageCache {
    public static Bitmap cacheBitmap;
    private LruCache<String, Bitmap> mMemoryCache = new LruCache<String, Bitmap>(GeminiApplication.memoryCacheSize) {
        protected int sizeOf(String key, Bitmap bitmap) {
            return bitmap.getRowBytes() * bitmap.getHeight();
        }
    };

    public Bitmap getBitmap(String url) {
        if (url == null) {
            return null;
        }
        return (Bitmap) this.mMemoryCache.get(url);
    }

    public void putBitmap(String url, Bitmap bitmap) {
        if (url != null && bitmap != null) {
            this.mMemoryCache.put(url, bitmap);
        }
    }

    public void clearBitmap() {
        this.mMemoryCache.evictAll();
    }
}
