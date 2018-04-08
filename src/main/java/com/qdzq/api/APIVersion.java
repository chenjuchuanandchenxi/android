package com.qdzq.api;

import java.util.Hashtable;
import com.google.gson.Gson;
import com.qdzq.comm.DataHttp;
import entity.comm.EntityError;

public class APIVersion {
	/*	获取版本
	陈巨传
	20160311*/
	public EntityError GetVersion(Hashtable<String, String> dic) {
		String url = "/api/updateversion/l";
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
