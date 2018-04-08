package com.qdzq.api;

import java.util.Hashtable;
import com.google.gson.Gson;
import com.qdzq.comm.DataHttp;
import entity.comm.EntityError;
import entity.user.EntityUser;

public class APIUser {
	/*	检测用户合法性
	陈巨传
	20160311*/
	public EntityUser Check(Hashtable<String, String> dic) {
		String url = "/api/user/l";
		EntityUser _Entity = new EntityUser();
        try {
            String retString = DataHttp.HttpPostSendData(url, dic);
            Gson gson = new Gson();
            _Entity = gson.fromJson(retString,EntityUser.class);
        } catch (Exception exp) {
            _Entity.setErrcode("-1");
            _Entity.setErrmsg(exp.getMessage());
        }
        return _Entity;
    }
	/*	修改密码
	陈巨传
	20160311*/
	public EntityError UpdPwd(Hashtable<String, String> dic) {
		String url = "/api/user/pwd";
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
}
