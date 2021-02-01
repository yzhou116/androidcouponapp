package com.cvte.taobaounion.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;

import com.cvte.taobaounion.base.BaseApplication;
import com.cvte.taobaounion.model.domain.CacheWithDuration;
import com.google.gson.Gson;

import java.util.GregorianCalendar;

import static java.lang.System.currentTimeMillis;

public class JsonCacheUtil {
    public static final String Json_SP_NAME = "json_sp_name";
    public SharedPreferences sharedPreferences;
    public Gson gson;

    private JsonCacheUtil() {
        sharedPreferences = BaseApplication.getAppContext().getSharedPreferences(Json_SP_NAME, Context.MODE_PRIVATE);
        gson = new Gson();


    }

    public void saveCache(String key, Object value) {
        this.saveCache(key, value, -1L);
    }


    public void saveCache(String key, Object value, long duration) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String jsonstr = gson.toJson(value);
        if (duration != -1L) {
            duration += currentTimeMillis();
        }
        CacheWithDuration cacheWithDuration = new CacheWithDuration(duration, jsonstr);
        String cacheWithTime = gson.toJson(cacheWithDuration);
        editor.putString(key, cacheWithTime);
        editor.apply();


    }

    public void delCache(String key) {
        sharedPreferences.edit().remove(key).apply();

    }

    public <T> T getValue(String key, Class<T> clazz) {
        String s = sharedPreferences.getString(key, null);
        if (s == null) {
            return null;
        }
        CacheWithDuration cacheWithDuration = gson.fromJson(s, CacheWithDuration.class);
        long currentTime = cacheWithDuration.getDuration();
        if (currentTime != -1 && currentTime - System.currentTimeMillis() <= 0) {
            return null;
        } else {
            String cache = cacheWithDuration.getCache();
            T result = gson.fromJson(cache, clazz);
            return result;
        }

    }


    private static JsonCacheUtil sInstance = null;

    public static JsonCacheUtil getInstance() {
        if (sInstance == null) {
            sInstance = new JsonCacheUtil();
        }
        return sInstance;
    }
}
