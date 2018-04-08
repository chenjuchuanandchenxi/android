package com.qdzq.comm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
public class DataCache {
	private static String mSdRootPath = Environment.getExternalStorageDirectory().getPath();//sd卡的根目录 
    private static String mDataRootPath = null;//手机的缓存根目录 
    private static String FOLDER_NAME = "/CardExtends";//文件目录  
    //获取手机的缓存目录
	public DataCache(Context context){  
        mDataRootPath = context.getCacheDir().getPath();  
    }  
	/** 
     * 获取储存Image的目录 
     * @return 
     */  
    private static String getStorageDirectory(){  
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?  mSdRootPath + FOLDER_NAME : mDataRootPath + FOLDER_NAME;  
    }  
    /** 
     * 判断文件是否存在 
     * @param fileName 
     * @return 
     */  
    public static boolean isFileExists(String fileName){  
        return new File(getStorageDirectory() + File.separator + fileName).exists();  
    }  
    /** 
     * 获取文件的大小 
     * @param fileName 
     * @return 
     */  
    public static long getFileSize(String fileName) {  
        return new File(getStorageDirectory() + File.separator + fileName).length();  
    }  
    /** 
     * 删除SD卡或者手机的缓存图片和目录 
     */  
    public void deleteFile() {  
        File dirFile = new File(getStorageDirectory());  
        if(! dirFile.exists()){  
            return;  
        }  
        if (dirFile.isDirectory()) {  
            String[] children = dirFile.list();  
            for (int i = 0; i < children.length; i++) {  
                new File(dirFile, children[i]).delete();  
            }  
        }  
          
        dirFile.delete();  
    }  
	/**
     * 获取网落图片资源：内存中的流
     * @param url
     * @return
     
	 * @throws IOException */
	public static Bitmap GetUrlBitmap(String url) throws IOException{
		URL myurl;
		Bitmap bitmap= null;
		
		myurl = new URL(url);
		//获得连接
		HttpURLConnection conn=(HttpURLConnection)myurl.openConnection();
		//设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
		conn.setConnectTimeout(6000);
		//连接设置获得数据流
		conn.setDoInput(true);
		//不使用缓存
		conn.setUseCaches(false);
		//得到数据流
		InputStream is = conn.getInputStream();
		//解析得到图片
		bitmap = BitmapFactory.decodeStream(is);
		//关闭数据流
		is.close();
		
		return bitmap;
	}
	/**
	     * 获取网落图片资源：存放在扩展卡
	     * @param url
	     * @return
	     
	 * @throws IOException */
	public static void Save(String url, String filename) throws IOException{
		Bitmap bitmap = GetUrlBitmap(url);
		if(bitmap == null){  
            return;  
        }  
		
		String path = getStorageDirectory();
        File dirFile = new File(path);  
        if (!dirFile.exists()) {  
            dirFile.mkdir();  
        }  
        
        File myfile = new File(path + File.separator +filename); 
        
        FileOutputStream out = new FileOutputStream(myfile);  
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);  
        
        out.flush();  
        out.close();  
	}
	public static void Save(Bitmap bitmap, String filename) throws IOException{
		if(bitmap == null){  
            return;  
        }  
		
		String path = getStorageDirectory();
        File dirFile = new File(path);  
        if (!dirFile.exists()) {  
            dirFile.mkdir();  
        }  
        
        File myfile = new File(path + File.separator +filename); 
        
        FileOutputStream out = new FileOutputStream(myfile);  
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);  
        
        out.flush();  
        out.close();  
	}
	/*读取本地图片
	Bitmap bitmap = getLoacalBitmap("aa.jpg"); //从本地取图片
	陈巨传
	20160314*/
	public static Bitmap getLoacalBitmap(String fileName) throws FileNotFoundException {
	      return BitmapFactory.decodeFile(getStorageDirectory() + File.separator + fileName);  
	}
}
