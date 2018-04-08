package com.qdzq.comm;

/*用户异步Handler传递数据
陈巨传
20160310*/
public class DataMsg {
	private String msgcode ="";
	private String msgcontent="";
	public String getMsgcode() {
		return msgcode;
	}
	public void setMsgcode(String msgcode) {
		this.msgcode = msgcode;
	}
	public String getMsgcontent() {
		return msgcontent;
	}
	public void setMsgcontent(String msgcontent) {
		this.msgcontent = msgcontent;
	}
	
}
