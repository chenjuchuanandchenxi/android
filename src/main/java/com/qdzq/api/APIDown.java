package com.qdzq.api;

import java.util.Hashtable;

import android.graphics.Bitmap;
import com.google.gson.Gson;
import com.qdzq.comm.DataCache;
import com.qdzq.comm.DataHttp;
import com.qdzq.comm.PublicOption;
import com.qdzq.entity.EntityOrderInvExtends;

/*下载数据源
陈巨传
20160311*/
public class APIDown {
	/*	下载数据列表
	陈巨传
	20160311*/
	public EntityOrderInvExtends GetList(Hashtable<String, String> dic) {
		String url = "/api/orderinv/l";
		EntityOrderInvExtends _Entity = new EntityOrderInvExtends();
        try {
            String retString = DataHttp.HttpPostSendData(url, dic);
            Gson gson = new Gson();
            _Entity = gson.fromJson(retString,EntityOrderInvExtends.class);
            if(_Entity.getList()!=null){
            	for(int i=0;i<_Entity.getList().size();i++){
            		String _imgurl = _Entity.getList().get(i).getImage2();
            		if(_imgurl!=null){
	            		if(!_imgurl.equals("")){
	            			Bitmap bitmap = DataCache.GetUrlBitmap(PublicOption.getUrl() + _imgurl);
	            			_Entity.getList().get(i).setInvbitmap(bitmap);
	            			String[] s = _imgurl.split("/");
	            			DataCache.Save(bitmap,s[2]);
	            		}
            		}
            	}
            }
        } catch (Exception exp) {
            _Entity.setErrcode("-1");
            _Entity.setErrmsg(exp.getMessage());
        }
        return _Entity;
    }
}
