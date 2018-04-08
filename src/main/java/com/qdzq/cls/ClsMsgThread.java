package com.qdzq.cls;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.qdzq.api.APIDown;
import com.qdzq.comm.PublicOption;
import com.qdzq.entity.EntityOrderInvExtends;
import com.qdzq.entity.ModelOrderInvExtends;
import com.qdzq.sqlite.InvDao;
import com.qdzq.sqlite.SQLiteOpenDB;

public class ClsMsgThread implements Runnable {
	Handler h;
	Context context;
	public ClsMsgThread(Handler h,Context context){
		this.h = h;
		this.context = context;
	}
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		InvDao _invdao = new InvDao(SQLiteOpenDB.OpenDatabase(context));

		if(PublicOption.getIsonline()){
			//本地待上传
			List<ModelOrderInvExtends> _listup = _invdao.getWaitUpList();
			int upcount = _listup.size();
			list.add(upcount);
			//本地任务列表
			List<ModelOrderInvExtends> _listlocal = _invdao.getList();
			//服务器待下载列表
			APIDown _api = new APIDown();
	        Hashtable<String, String> dic = new Hashtable<String, String>();
	        dic.put("userid", PublicOption.getUserid());
            dic.put("safecode", PublicOption.getSafecode());
            EntityOrderInvExtends _entity = _api.GetList(dic);
            List<ModelOrderInvExtends> _list = _entity.getList();
            int downcount = _list.size();
            //服务器待下载数-本地任务列表=实际待下载列表
            for(ModelOrderInvExtends m : _list){
            	for(ModelOrderInvExtends ml : _listlocal){
                	if(ml.getBillids().equals(m.getBillids())){
                		downcount--;
                		break;
                	}
                }
            }
            list.add(downcount);//待下载数
		} else{
			List<ModelOrderInvExtends> _listdo = _invdao.getWaitDoList();
			list.add(_listdo.size());//待操作数
		}
		Message msg = new Message();
	    msg.obj = list;
        h.sendMessage(msg);
	}
}
