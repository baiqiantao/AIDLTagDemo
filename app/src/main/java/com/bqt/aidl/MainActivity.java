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

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends ListActivity {
	private BookManager mBookManager;
	private MyServiceConnection mServiceConnection;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String[] array = {"绑定服务",
				"解绑服务",
				"getBooks",
				"addBookIn",
				"addBookOut",
				"addBookInout",
				"",};
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>(Arrays.asList(array))));
		mServiceConnection = new MyServiceConnection();
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		switch (position) {
			case 0:
				Intent intent = new Intent();
				intent.setAction("com.bqt.service.aidl");
				intent.setPackage("com.bqt.aidl2");
				bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
				break;
			case 1:
				if (mServiceConnection != null) unbindService(mServiceConnection);
				else Toast.makeText(this, "还没绑定服务", Toast.LENGTH_SHORT).show();
				mBookManager = null;
				break;
			case 2:
				getBooks();
				break;
			case 3:
				addBookIn();
				break;
			case 4:
				addBookOut();
				break;
			case 5:
				addBookInout();
				break;
		}
	}
	
	private void getBooks() {
		if (mBookManager != null) {
			try {
				Log.i("bqt", "【客户端getBooks】" + mBookManager.getBooks().toString());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else Toast.makeText(this, "还没绑定服务", Toast.LENGTH_SHORT).show();
		
	}
	
	public void addBookIn() {
		if (mBookManager != null) {
			Book book = new Book();
			book.setName("包青天In");
			book.setPrice(10);
			try {
				//获得服务端执行方法的返回值，并打印输出
				Book returnBook = mBookManager.addBookIn(book);
				Log.i("bqt", "【客户端addBookIn】" + returnBook.toString());
				Log.i("bqt", "【客户端addBookIn：传进去的参数】" + book.toString());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else Toast.makeText(this, "还没绑定服务", Toast.LENGTH_SHORT).show();
	}
	
	public void addBookOut() {
		if (mBookManager != null) {
			Book book = new Book();
			book.setName("包青天Out");
			book.setPrice(20);
			try {
				Book returnBook = mBookManager.addBookOut(book);
				Log.i("bqt", "【客户端addBookOut】" + returnBook.toString());
				Log.i("bqt", "【客户端addBookOut：传进去的参数】" + book.toString());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else Toast.makeText(this, "还没绑定服务", Toast.LENGTH_SHORT).show();
	}
	
	public void addBookInout() {
		if (mBookManager != null) {
			Book book = new Book();
			book.setName("包青天Inout");
			book.setPrice(30);
			try {
				Book returnBook = mBookManager.addBookInout(book);
				Log.i("bqt", "【客户端addBookInout】" + returnBook.toString());
				Log.i("bqt", "【客户端addBookInout：传进去的参数】" + book.toString());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else Toast.makeText(this, "还没绑定服务", Toast.LENGTH_SHORT).show();
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