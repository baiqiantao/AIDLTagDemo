package com.bqt.aidl;

import android.app.IntentService;
import android.content.Intent;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

public class BackgroundService extends IntentService {
	public BackgroundService() {
		super("工作线程");
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i("bqt", "onCreate");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i("bqt", "onDestroy");
	}
	
	@Override
	protected void onHandleIntent(@Nullable Intent intent) {
		Log.i("bqt", "onHandleIntent，是否主线程：" + (Looper.myLooper() == Looper.getMainLooper()));
		String name = intent != null && intent.hasExtra("name") ? intent.getStringExtra("name") : "null";
		Log.i("bqt", "开始执行耗时任务，" + name);
		SystemClock.sleep(3 * 1000);
		Log.i("bqt", "耗时任务完成");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("bqt", "onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}
}