/**
 * 
 */
package com.qdzq.comm;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.text.TextUtils;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
public class DataHelp {	
	//是否手机号码
	public static boolean isMobileNO(String mobiles) {  
	    /* 
		    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188 
		    联通：130、131、132、152、155、156、185、186 
		    电信：133、153、180、189、（1349卫通） 
		    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9 
	    */  
	    String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。  
	    if (TextUtils.isEmpty(mobiles)) return false;  
	    else return mobiles.matches(telRegex);  
	} 
	//固定参数和函数
    private static byte[] hex = "0123456789ABCDEF".getBytes();  
    public static int parse(char c) {  
        if (c >= 'a')  
            return (c - 'a' + 10) & 0x0f;  
        if (c >= 'A')  
            return (c - 'A' + 10) & 0x0f;  
        return (c - '0') & 0x0f;  
    }  
    // 从字节数组到十六进制字符串转换  
    public static String Bytes2HexString(byte[] b) {  
        byte[] buff = new byte[2 * b.length];  
        for (int i = 0; i < b.length; i++) {  
            buff[2 * i] = hex[(b[i] >> 4) & 0x0f];  
            buff[2 * i + 1] = hex[b[i] & 0x0f];  
        }  
        return new String(buff);  
    }  
    public static String Bytes2HexString(byte temp) {  
    	byte[] b = new byte[1];
    	b[0] = temp;
        byte[] buff = new byte[2 * b.length];  
        for (int i = 0; i < b.length; i++) {  
            buff[2 * i] = hex[(b[i] >> 4) & 0x0f];  
            buff[2 * i + 1] = hex[b[i] & 0x0f];  
        }  
        return new String(buff);  
    }  
    // 从十六进制字符串到字节数组转换  
    public static byte[] HexString2Bytes(String hexstr) {  
        byte[] b = new byte[hexstr.length() / 2];  
        int j = 0;  
        for (int i = 0; i < b.length; i++) {  
            char c0 = hexstr.charAt(j++);  
            char c1 = hexstr.charAt(j++);  
            b[i] = (byte) ((parse(c0) << 4) | parse(c1));  
        }  
        return b;  
    } 
    //十六进制转数码管对应的值
    public static String Hex2String(String hexstr){
    	if(hexstr.equals("FD") || hexstr.equals("7D")){
    		return "0";
    	} else if(hexstr.equals("E0") || hexstr.equals("60")){
    		return "1";
    	} else if(hexstr.equals("BE") || hexstr.equals("3E")){
    		return "2";
    	} else if(hexstr.equals("FA") || hexstr.equals("7A")){
    		return "3";
    	} else if(hexstr.equals("E3") || hexstr.equals("63")){
    		return "4";
    	} else if(hexstr.equals("DB") || hexstr.equals("5B")){
    		return "5";
    	} else if(hexstr.equals("DF") || hexstr.equals("5F")){
    		return "6";
    	} else if(hexstr.equals("F0") || hexstr.equals("70")){
    		return "7";
    	} else if(hexstr.equals("FF") || hexstr.equals("7F")){
    		return "8";
    	} else if(hexstr.equals("FB") || hexstr.equals("7B")){
    		return "9";
    	}
    	return "";
    }    
    /*日志记录
	陈巨传
	20160315*/
    public static void WriteLog(byte[] buffer, String title, TextView tvlog){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String str = df.format(new Date()) + "	" + title + "	" + DataHelp.Bytes2HexString(buffer);
    	if(tvlog.getLineCount()>=10000){
    		tvlog.setText("");
    	}
    	tvlog.setText(str+"\r\n"+tvlog.getText());
	}
	/*日志记录重载
	陈巨传
	20130316*/
    public static void WriteLog(String str, TextView tvlog){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	str = df.format(new Date()) + "	" + str + "	";
    	if(tvlog.getLineCount()>=10000){
    		tvlog.setText("");
    	}
    	tvlog.setText(str+"\r\n"+tvlog.getText());
	}
}
