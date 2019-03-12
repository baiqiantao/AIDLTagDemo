package com.bqt.aidl;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity {
	private BookManager mBookManager;
	private MyServiceConnection mServiceConnection;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String[] array = {
			"addBookIn",
			"addBookOut",
			"addBookInout",
			"后台服务 AlarmManager"};
		setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, array));
		
		mServiceConnection = new MyServiceConnection();
		Intent intent = new Intent();
		intent.setAction("com.bqt.service.aidl");
		intent.setPackage("com.bqt.aidl2");
		bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mServiceConnection != null) {
			unbindService(mServiceConnection);
		}
		mBookManager = null;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		if (mBookManager != null) {
			try {
				Book book = null, returnBook = null;
				switch (position) {
					case 0:
						book = new Book("客户端-In", 10);
						Log.i("bqt", "【客户端-传进去的Book-执行前】" + book);
						returnBook = mBookManager.addBookIn(book);
						break;
					case 1:
						book = new Book("客户端-Out", 20);
						Log.i("bqt", "【客户端-传进去的Book-执行前】" + book);
						returnBook = mBookManager.addBookOut(book);
						break;
					case 2:
						book = new Book("客户端-Inout", 30);
						Log.i("bqt", "【客户端-传进去的Book-执行前】" + book);
						returnBook = mBookManager.addBookInout(book);
						break;
					case 3:
						startActivity(new Intent(this, AlarmManagerActivity.class));
						return;
				}
				
				Log.i("bqt", "【客户端-returnBook】" + returnBook);
				Log.i("bqt", "【客户端-传进去的Book-执行后】" + book);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	private class MyServiceConnection implements ServiceConnection {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Toast.makeText(MainActivity.this, "服务已连接", Toast.LENGTH_SHORT).show();
			mBookManager = BookManager.Stub.asInterface(service);
		}
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			Toast.makeText(MainActivity.this, "服务已断开", Toast.LENGTH_SHORT).show();
			mBookManager = null;
		}
	}
	
}