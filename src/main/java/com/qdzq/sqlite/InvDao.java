/**
 * 
 */
package com.qdzq.sqlite;

import java.util.ArrayList;
import java.util.List;

import com.qdzq.entity.ModelOrderInvExtends;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author Administrator
 *
 */
public class InvDao {
	private SQLiteDatabase db;
	public InvDao(SQLiteDatabase db){
		this.db = db;
	}
	/*下载数据
	陈巨传
	20160311*/
	public void Add(List<ModelOrderInvExtends> list){
		//本地任务列表
		List<ModelOrderInvExtends> _listlocal = getList();
		try{
			for(int i=0;i<list.size();i++){
				ModelOrderInvExtends modle = list.get(i);
				boolean isexit =false;
				for(ModelOrderInvExtends ml : _listlocal){
                	if(ml.getBillids().equals(modle.getBillids())){
                		isexit = true;
                		break;
                	}
                }
				if(!isexit){
					StringBuilder sb = new StringBuilder();
					sb.append("insert into inv(billids,billid,billcode,posname,invstd,qty,strength,limitname,lentop,lenbom,image2,memo) values(?,?,?,?,?,?,?,?,?,?,?,?)");
					Object[] obj = new Object[12];
					obj[0]=modle.getBillids();
					obj[1]=modle.getBillid();
					obj[2]=modle.getBillcode();
					obj[3]=modle.getPosname();
					obj[4]=modle.getInvstd();
					obj[5]=modle.getQty();
					obj[6]=modle.getStrength();
					obj[7]=modle.getLimitname();
					obj[8]=modle.getLentop();
					obj[9]=modle.getLenbom();
					obj[10]=modle.getImage2();
					obj[11]=modle.getMemo();
					db.execSQL(sb.toString(), obj);
				}
			}
		} catch(Exception exp){
			exp.printStackTrace();
		} finally{
			if(db!=null){
				db.close();
			}
		}
	}
	/*删除已上传列表
	陈巨传
	20160311*/
	public void Del(String billids){
		String[] where = new String[]{billids};
		try{
			StringBuilder sb = new StringBuilder();
			sb.append("delete from inv where billids=?");
			db.execSQL(sb.toString(), where);
		} catch(Exception exp){
			exp.printStackTrace();
		} finally{
			if(db!=null){
				db.close();
			}
		}
	}
	/*未完成列表
	陈巨传
	20160311*/
	public List<ModelOrderInvExtends> getWaitDoList(){
		List<ModelOrderInvExtends> list = new ArrayList<ModelOrderInvExtends>();
		Cursor cur = db.rawQuery("select a.* from inv a left join (select billids,max(cast(factno as int)) as factno from invfact group by billids) as b on a.billids=b.billids where cast(ifnull(a.qty,'0') as int)>cast(ifnull(b.factno,'0') as int)",null);
		try{
			while(cur.moveToNext()){
				ModelOrderInvExtends model = new ModelOrderInvExtends();
				model.setBillids(cur.getString(cur.getColumnIndex("billids")));
				model.setBillid(cur.getString(cur.getColumnIndex("billid")));
				model.setBillcode(cur.getString(cur.getColumnIndex("billcode")));
				model.setPosname(cur.getString(cur.getColumnIndex("posname")));
				model.setInvstd(cur.getString(cur.getColumnIndex("invstd")));
				model.setQty(cur.getString(cur.getColumnIndex("qty")));
				model.setStrength(cur.getString(cur.getColumnIndex("strength")));
				model.setLimitname(cur.getString(cur.getColumnIndex("limitname")));
				model.setLentop(cur.getString(cur.getColumnIndex("lentop")));
				model.setLenbom(cur.getString(cur.getColumnIndex("lenbom")));
				model.setImage2(cur.getString(cur.getColumnIndex("image2")));
				model.setMemo(cur.getString(cur.getColumnIndex("memo")));
				list.add(model);
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
	/*待上传列表
	陈巨传
	20160311*/
	public List<ModelOrderInvExtends> getWaitUpList(){
		List<ModelOrderInvExtends> list = new ArrayList<ModelOrderInvExtends>();
		Cursor cur = db.rawQuery("select a.* from inv a inner join (select billids,max(cast(factno as int)) as factno from invfact group by billids) as b on a.billids=b.billids where cast(ifnull(a.qty,'0') as int)=cast(ifnull(b.factno,'0') as int)",null);
		try{
			while(cur.moveToNext()){
				ModelOrderInvExtends model = new ModelOrderInvExtends();
				model.setBillids(cur.getString(cur.getColumnIndex("billids")));
				model.setBillid(cur.getString(cur.getColumnIndex("billid")));
				model.setBillcode(cur.getString(cur.getColumnIndex("billcode")));
				model.setPosname(cur.getString(cur.getColumnIndex("posname")));
				model.setInvstd(cur.getString(cur.getColumnIndex("invstd")));
				model.setQty(cur.getString(cur.getColumnIndex("qty")));
				model.setStrength(cur.getString(cur.getColumnIndex("strength")));
				model.setLimitname(cur.getString(cur.getColumnIndex("limitname")));
				model.setLentop(cur.getString(cur.getColumnIndex("lentop")));
				model.setLenbom(cur.getString(cur.getColumnIndex("lenbom")));
				model.setImage2(cur.getString(cur.getColumnIndex("image2")));
				model.setMemo(cur.getString(cur.getColumnIndex("memo")));
				//获取本地图片
				
				list.add(model);
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
	/*本地任务列表
	陈巨传
	20160311*/
	public List<ModelOrderInvExtends> getList(){
		List<ModelOrderInvExtends> list = new ArrayList<ModelOrderInvExtends>();
		Cursor cur = db.rawQuery("select a.* from inv a",null);
		try{
			while(cur.moveToNext()){
				ModelOrderInvExtends model = new ModelOrderInvExtends();
				model.setBillids(cur.getString(cur.getColumnIndex("billids")));
				model.setBillid(cur.getString(cur.getColumnIndex("billid")));
				model.setBillcode(cur.getString(cur.getColumnIndex("billcode")));
				model.setPosname(cur.getString(cur.getColumnIndex("posname")));
				model.setInvstd(cur.getString(cur.getColumnIndex("invstd")));
				model.setQty(cur.getString(cur.getColumnIndex("qty")));
				model.setStrength(cur.getString(cur.getColumnIndex("strength")));
				model.setLimitname(cur.getString(cur.getColumnIndex("limitname")));
				model.setLentop(cur.getString(cur.getColumnIndex("lentop")));
				model.setLenbom(cur.getString(cur.getColumnIndex("lenbom")));
				model.setImage2(cur.getString(cur.getColumnIndex("image2")));
				model.setMemo(cur.getString(cur.getColumnIndex("memo")));
				list.add(model);
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
