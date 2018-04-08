/**
 * 
 */
package com.qdzq.sqlite;

import java.util.ArrayList;
import java.util.List;

import entity.orderinvfact.ModelOrderInvFact;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author Administrator
 *
 */
public class InvFactDao {
	private SQLiteDatabase db;
	public InvFactDao(SQLiteDatabase db){
		this.db = db;
	}
	/*增加
	陈巨传
	20160311*/
	public void Add(ModelOrderInvFact modle){
		try{
			StringBuilder sb = new StringBuilder();
			sb.append("insert into invfact(billids,factno,operstrength,operdate,factstate) values(?,?,?,?,?)");
			Object[] obj = new Object[5];
			obj[0]=modle.getBillids();
			obj[1]=modle.getFactno();
			obj[2]=modle.getOperstrength();
			obj[3]=modle.getOperdate();
			obj[4]=modle.getFactstate();
			db.execSQL(sb.toString(), obj);
		} catch(Exception exp){
			exp.printStackTrace();
		} finally{
			if(db!=null){
				db.close();
			}
		}
	}
	/*列表
	陈巨传
	20160311*/
	public List<ModelOrderInvFact> getList(String billids){
		String[] where = new String[]{billids};
		List<ModelOrderInvFact> list = new ArrayList<ModelOrderInvFact>();
		Cursor cur = db.rawQuery("select * from invfact where billids=?",where);
		try{
			while(cur.moveToNext()){
				ModelOrderInvFact model = new ModelOrderInvFact();
				model.setBillids(cur.getString(cur.getColumnIndex("billids")));
				model.setFactno(cur.getString(cur.getColumnIndex("factno")));
				model.setOperstrength(cur.getString(cur.getColumnIndex("operstrength")));
				model.setOperdate(cur.getString(cur.getColumnIndex("operdate")));
				model.setFactstate(cur.getString(cur.getColumnIndex("factstate")));
				list.add(0,model);
			}
		} catch(Exception exp){
			exp.printStackTrace();
		} finally{
			if(cur!=null)
				cur.close();
			else{
				db.close();
			}
		}
		return list;
	}
	/*删除已上传列表
	陈巨传
	20160311*/
	public void Del(String billids){
		String[] where = new String[]{billids};
		try{
			StringBuilder sb = new StringBuilder();
			sb.append("delete from invfact where billids=?");
			db.execSQL(sb.toString(), where);
		} catch(Exception exp){
			exp.printStackTrace();
		} finally{
			if(db!=null){
				db.close();
			}
		}
	}
	/*等待上传列表
	陈巨传
	20160311*/
	public List<ModelOrderInvFact> getWaitUpList(){
		List<ModelOrderInvFact> list = new ArrayList<ModelOrderInvFact>();
		StringBuilder sb = new StringBuilder();
		sb.append(" select a.* from invfact a");
		sb.append(" inner join (");
		sb.append(" 	select a.billids from inv a inner join (select billids,max(cast(factno as int)) as factno from invfact group by billids) as b on a.billids=b.billids where cast(ifnull(a.qty,'0') as int)=cast(ifnull(b.factno,'0') as int)");
		sb.append(" ) as b on a.billids=b.billids");
		Cursor cur = db.rawQuery(sb.toString(),null);
		try{
			while(cur.moveToNext()){
				ModelOrderInvFact model = new ModelOrderInvFact();
				model.setBillids(cur.getString(cur.getColumnIndex("billids")));
				model.setFactno(cur.getString(cur.getColumnIndex("factno")));
				model.setOperstrength(cur.getString(cur.getColumnIndex("operstrength")));
				model.setOperdate(cur.getString(cur.getColumnIndex("operdate")));
				model.setFactstate(cur.getString(cur.getColumnIndex("factstate")));
				list.add(0,model);
			}
		} catch(Exception exp){
			exp.printStackTrace();
		} finally{
			if(cur!=null)
				cur.close();
			else{
				db.close();
			}
		}
		return list;
	}
}