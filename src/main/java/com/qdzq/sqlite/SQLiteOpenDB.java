package com.qdzq.sqlite;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.qdzq.appzqwt.R;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

public class SQLiteOpenDB {
	private static final int BUFFER_SIZE = 400000;//默认文件大小
	private static final String DB_NAME = "zqwtdb.db"; //保存的数据库文件名
	private static final String PACKAGE_NAME = "com.qdzqwt.databases";//目录名称
	private static String mSdRootPath = Environment.getExternalStorageDirectory().getPath();//SD卡的根目录 
	private static final String DB_PATH = mSdRootPath + "/" + PACKAGE_NAME;  //在手机里存放数据库的位置
	private static final String dbfile = DB_PATH + "/" + DB_NAME;
    /*
     * 打开数据库:关闭为database。close
     */
    @SuppressWarnings("unused")
	public static SQLiteDatabase OpenDatabase(Context context) {
        try {
            if (!(new File(dbfile).exists())) {//判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
            	File f = new File(DB_PATH);
            	if (!f.exists()) {
                     f.mkdir();
                }
                InputStream is = context.getResources().openRawResource(R.raw.zqwtdb); //欲导入的数据库
                FileOutputStream fos = new FileOutputStream(dbfile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
            SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(dbfile,null);
            return database;
        } catch (FileNotFoundException e) {
            Log.e("Database", "File not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Database", "IO exception");
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("Database", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
