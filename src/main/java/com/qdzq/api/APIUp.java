package com.qdzq.api;

import java.util.Hashtable;

import com.google.gson.Gson;
import com.qdzq.comm.DataHttp;
import entity.comm.EntityError;
import entity.orderinvfact.EntityOrderInvFact;

/*上传保存或直接保存
陈巨传
20160311*/
public class APIUp {
	public EntityError Save(Hashtable<String, String> dic) {
		String url = "/api/orderinvfact/s";
		EntityError _Entity = new EntityError();
        try {
            String retString = DataHttp.HttpPostSendData(url, dic);
            Gson gson = new Gson();
            _Entity = gson.fromJson(retString,EntityError.class);
        } catch (Exception exp) {
            _Entity.setErrcode("-1");
            _Entity.setErrmsg(exp.getMessage());
        }
        return _Entity;
    }
	/*列表
	陈巨传
	20160311*/
	public EntityOrderInvFact GetList(Hashtable<String, String> dic) {
		String url = "/api/orderinvfact/l";
		EntityOrderInvFact _Entity = new EntityOrderInvFact();
        try {
            String retString = DataHttp.HttpPostSendData(url, dic);
            Gson gson = new Gson();
            _Entity = gson.fromJson(retString,EntityOrderInvFact.class);
            
        } catch (Exception exp) {
            _Entity.setErrcode("-1");
            _Entity.setErrmsg(exp.getMessage());
        }
        return _Entity;
    }
}
