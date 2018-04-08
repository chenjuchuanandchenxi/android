/**
 * 
 */
package com.qdzq.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Administrator
 *
 */
public class SQliteHelper extends SQLiteOpenHelper  {
	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public SQliteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		//创建任务表
		StringBuilder sb1 = new StringBuilder();
		sb1.append("Create table inv(");
		sb1.append("billids varchar(50)");
		sb1.append(",billid VARCHAR(50)");
		sb1.append(",billcode varchar(50)");
		sb1.append(",posname VARCHAR(50)");
		sb1.append(",invstd varchar(50)");
		sb1.append(",qty VARCHAR(50)");
		sb1.append(",strength VARCHAR(50)");
		sb1.append(",limitname varchar(50)");
		sb1.append(",lentop VARCHAR(50)");
		sb1.append(",lenbom VARCHAR(50)");
		sb1.append(",image2 VARCHAR(50)");
		sb1.append(",memo VARCHAR(500)");
		sb1.append(")");
		db.execSQL(sb1.toString());
		//创建力矩表
		StringBuilder sb2 = new StringBuilder();
		sb2.append("Create table invfact(");
		sb2.append("billids varchar(50)");
		sb2.append(",factno VARCHAR(50)");
		sb2.append(",operstrength VARCHAR(50)");
		sb2.append(",operdate VARCHAR(50)");
		sb2.append(",factstate VARCHAR(50)");
		sb2.append(")");
		db.execSQL(sb2.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {//如果存在表，先删除，再使用Create创建
		// TODO Auto-generated method stub
		//任务表
		StringBuilder sb1 = new StringBuilder();
		sb1.append("drop table if exists inv");
		db.execSQL(sb1.toString());
		//力矩表
		StringBuilder sb2 = new StringBuilder();
		sb2.append("drop table if exists invfact");
		db.execSQL(sb2.toString());
		
		onCreate(db);
	}

}
