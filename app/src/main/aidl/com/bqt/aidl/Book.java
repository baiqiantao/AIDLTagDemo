package com.bqt.aidl;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
	
	private String name;
	private int price;
	
	public void readFromParcel(Parcel in) { //一定要注意，需要手动添加 readFromParcel() 方法
		this.name = in.readString();
		this.price = in.readInt();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.name);
		dest.writeInt(this.price);
	}
	
	public Book() { //必须有无参构造方法
	}
	
	protected Book(Parcel in) {
		this.name = in.readString();
		this.price = in.readInt();
	}
	
	public static final Creator<Book> CREATOR = new Creator<Book>() {
		@Override
		public Book createFromParcel(Parcel source) {
			return new Book(source);
		}
		
		@Override
		public Book[] newArray(int size) {
			return new Book[size];
		}
	};
	
	//********************************************  可选的  **********************************************
	
	public Book(String name, int price) {
		this.name = name;
		this.price = price;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getPrice() {
		return price;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}
	
	@Override
	public String toString() {
		return "{name=" + name + "，price=" + price + "}";
	}
}