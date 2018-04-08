package com.qdzq.update;

public class ModelVersion {
	private int version;
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	private String url;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	//当前版本
	private int curversion;
	public int getCurversion() {
		return curversion;
	}
	public void setCurversion(int curversion) {
		this.curversion = curversion;
	}
}
