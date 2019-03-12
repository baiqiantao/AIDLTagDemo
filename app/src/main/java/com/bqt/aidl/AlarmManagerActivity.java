package com.bqt.aidl;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AlarmManagerActivity extends ListActivity {
	private AlarmManager manager;
	private int count;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String[] array = {
			"设置一次性定时后台服务",
			"设置一个周期性执行的定时服务",
			"取消AlarmManager的定时服务"};
		setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, array));
		
		manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		switch (position) {
			case 0:
				setOnceAlarm();
				break;
			case 1:
				setRepeatAlarm();
				break;
			case 2:
				Intent intent = new Intent(this, BackgroundService.class);
				manager.cancel(PendingIntent.getService(this, 0, intent, 0));
				break;
		}
	}

		/*AlarmManager中定义的type有五个可选值：
	 ELAPSED_REALTIME  闹钟在睡眠状态下不可用，如果在系统休眠时闹钟触发，它将不会被传递，直到下一次设备唤醒；使用相对系统启动开始的时间
	 ELAPSED_REALTIME_WAKEUP  闹钟在手机睡眠状态下会唤醒系统并执行提示功能，使用相对时间
	 RTC  闹钟在睡眠状态下不可用，该状态下闹钟使用绝对时间，即当前系统时间
	 RTC_WAKEUP  表示闹钟在睡眠状态下会唤醒系统并执行提示功能，使用绝对时间
	 POWER_OFF_WAKEUP  表示闹钟在手机【关机】状态下也能正常进行提示功能，用绝对时间，但某些版本并不支持！ */
	
	/*设置在triggerAtTime时间启动的定时服务。该方法用于设置一次性闹钟*/
	private void setOnceAlarm() {
		Intent intent = getIntent("onceAlarm");
		PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
		
		//表示闹钟(首次)执行时间。相对于系统启动时间，Returns milliseconds since boot, including time spent in sleep
		long triggerAtTime = SystemClock.elapsedRealtime() + 3 * 1000;
		
		//设置定时任务。CPU一旦休眠(比如关机状态)，Timer中的定时任务就无法运行，而Alarm具有唤醒CPU的功能
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
	}
	
	/*设置一个周期性执行的定时服务，参数表示首次执行时间和间隔时间*/
	private void setRepeatAlarm() {
		Intent intent = getIntent("repeatAlarm" + (count++));//这里传给Intent的值一旦设定后就不会再改变
		PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
		
		//相对于1970……的绝对时间，Returns milliseconds since boot, including time spent in sleep.
		long triggerAtTime = System.currentTimeMillis() + 3 * 1000;
		
		//时间间隔至少为60秒
		manager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtTime, 60 * 1000, pendingIntent);
		/*Frequent alarms are bad for battery life. As of API 22, the AlarmManager will override
		 near-future and high-frequency alarm requests, delaying the alarm at least 【5 seconds】 into the future
		 and ensuring that the repeat interval is at least 【60 seconds】.
		 If you really need to do work sooner than 5 seconds, post a delayed message or runnable to a Handler.*/
	}
	
	@NonNull
	private Intent getIntent(String name) {
		Intent intent = new Intent(this, BackgroundService.class);
		intent.putExtra("name", name);
		return intent;
	}
}