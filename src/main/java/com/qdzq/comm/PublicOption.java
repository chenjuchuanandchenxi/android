package com.qdzq.comm;

import java.util.ArrayList;
import java.util.List;

import com.qdzq.entity.ModelPortItem;

import android.app.Application;

public class PublicOption extends Application {//application是全局信息
	private static String userid;
	private static String usercode;
	private static String username;
	private static String url = "http://10.20.50.13:8081";
	private static String safecode=DataMD5.getMD5("1111-1111-1111-1111");
	private static boolean isonline = true;
	private static List<ModelPortItem> listdevices = new ArrayList<ModelPortItem>();//我的工作设备
	private static String uuid="00001101-0000-1000-8000-00805F9B34FB";
	
    public static String getUuid() {
		return uuid;
	}
	public static String getUserid() {
		return userid;
	}
	public static void setUserid(String useridpara) {
		userid = useridpara;
	}
	public static void setUsercode(String usercodepara) {
        usercode = usercodepara;
    }
    public static String getUsercode() {
        return usercode;
    }
	public static String getUsername() {
		return username;
	}
	public static void setUsername(String username) {
		PublicOption.username = username;
	}
	public static String getUrl() {
		return url;
	}
	public static void setUrl(String urlpara) {
		url = urlpara;
	}
	public static String getSafecode() {
		return safecode;
	}
	public static boolean getIsonline() {
		return isonline;
	}
	public static void setIsonline(boolean isonlinepara) {
		isonline = isonlinepara;
	}
	public static List<ModelPortItem> getListdevices() {
		return listdevices;
	}
	public static void setListdevices(List<ModelPortItem> listdevices) {
		PublicOption.listdevices = listdevices;
	}
	
}
