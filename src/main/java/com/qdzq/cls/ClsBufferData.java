package com.qdzq.cls;

import java.util.Date;

public class ClsBufferData {
	//请求数据
	public static byte[] getBufferDataPut(){
		byte[] buffer = new byte[11];
		buffer[0] = 0x55;
		buffer[1] = 0x55;
		buffer[2] = 0x02;
		buffer[3] = 0x00;
		buffer[4] = 0x00;
		buffer[5] = 0x00;
		buffer[6] = 0x00;
		buffer[7] = 0x00;
		buffer[8] = 0x02;
		buffer[9] =  (byte)0xaa;
		buffer[10] =  (byte)0xaa;
    	return buffer;
	}
	//握手
	public static byte[] getBufferLinkPut(){
		byte[] buffer = new byte[11];
		buffer[0] = 0x55;
		buffer[1] = 0x55;
		buffer[2] = 0x01;
		buffer[3] = 0x00;
		buffer[4] = 0x00;
		buffer[5] = 0x00;
		buffer[6] = 0x00;
		buffer[7] = 0x00;
		buffer[8] = 0x01;
		buffer[9] =  (byte)0xaa;
		buffer[10] =  (byte)0xaa;
    	return buffer;
	}
	public static String getStringLinkGet(){
    	return "555511000000000011AAAA";
	}
	//清空
	public static byte[] getBufferClear(){
		byte[] buffer = new byte[11];
    	buffer[0] = 0x55;
    	buffer[1] = 0x55;
    	buffer[2] = 0x08;
    	buffer[3] = 0x00;
    	buffer[4] = 0x00;
    	buffer[5] = 0x00;
    	buffer[6] = 0x00;
    	buffer[7] = 0x00;
    	buffer[8] = 0x08;
    	buffer[9] =  (byte)0xaa;
    	buffer[10] =  (byte)0xaa;
    	return buffer;
	}
	//默认初始
	public static byte[] getBufferDefault(){
		byte[] buffer = new byte[11];
    	buffer[0] = 0x55;
    	buffer[1] = 0x55;
    	buffer[2] = (byte)0x0a;
    	buffer[3] = 0x00;
    	buffer[4] = 0x00;
    	buffer[5] = 0x00;
    	buffer[6] = 0x00;
    	buffer[7] = 0x00;
    	buffer[8] = (byte)0x0a;
    	buffer[9] =  (byte)0xaa;
    	buffer[10] =  (byte)0xaa;
    	return buffer;
	}
	//单位
	public static byte[] getBufferUnit(int selectedUnitIndex) {
		byte[] buffer = new byte[11];
    	buffer[0] = 0x55;
    	buffer[1] = 0x55;
    	buffer[2] = 0x09;
    	buffer[3] = (byte)selectedUnitIndex;
    	buffer[4] = 0x00;
    	buffer[5] = 0x00;
    	buffer[6] = 0x00;
    	buffer[7] = 0x00;
    	int unitIndex = selectedUnitIndex + 9;
    	buffer[8] = (byte)unitIndex;
    	buffer[9] =  (byte)0xaa;
    	buffer[10] =  (byte)0xaa;
		return buffer;
	}
	//精度
	public static byte[] getBufferPrec(int selectedPrecIndex) {
		byte[] buffer = new byte[11];
    	buffer[0] = 0x55;
    	buffer[1] = 0x55;
    	buffer[2] = 0x06;
    	buffer[3] = (byte)selectedPrecIndex;
    	buffer[4] = 0x00;
    	buffer[5] = 0x00;
    	buffer[6] = 0x00;
    	buffer[7] = 0x00;
    	int PrecIndex = selectedPrecIndex + 6;
    	buffer[8] = (byte)PrecIndex;
    	buffer[9] =  (byte)0xaa;
    	buffer[10] =  (byte)0xaa;
		return buffer;
	}
	//存储
	public static byte[] getBufferStore(int selectedStoreIndex) {
		byte[] buffer = new byte[11];
		buffer[0] = 0x55;
		buffer[1] = 0x55;
		buffer[2] = 0x05;
		buffer[3] = (byte)selectedStoreIndex;
		buffer[4] = 0x00;
		buffer[5] = 0x00;
		buffer[6] = 0x00;
		buffer[7] = 0x00;
		int StoreIndex = selectedStoreIndex + 5;
		buffer[8] = (byte)StoreIndex;
		buffer[9] =  (byte)0xaa;
		buffer[10] =  (byte)0xaa;
		return buffer;
	}
	//时间
	public static byte[] getBufferTime() {
		Date dt = new Date();
		byte[] buffer = new byte[11];
    	buffer[0] = 0x55;
    	buffer[1] = 0x55;
    	buffer[2] = 0x07;
    	String year = String.valueOf(dt.getYear());
    	buffer[3] = (byte)Integer.parseInt(year.substring(2, 2));
    	buffer[4] = (byte)dt.getMonth();
    	buffer[5] = (byte)dt.getDay();
    	buffer[6] = (byte)dt.getHours();
    	buffer[7] = (byte)dt.getMinutes();
    	buffer[8] = (byte)dt.getSeconds();    	
    	buffer[9] =  (byte)0xaa;
    	buffer[10] =  (byte)0xaa;
		return buffer;
	}
}
