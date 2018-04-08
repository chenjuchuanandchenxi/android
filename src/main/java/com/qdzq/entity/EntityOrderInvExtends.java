package com.qdzq.entity;

import java.util.List;

import entity.comm.EntityError;

public class EntityOrderInvExtends extends EntityError {
	private static final long serialVersionUID = 1L;
	private ModelOrderInvExtends info;
    private List<ModelOrderInvExtends> list;
	public ModelOrderInvExtends getInfo() {
		return info;
	}
	public void setInfo(ModelOrderInvExtends info) {
		this.info = info;
	}
	public List<ModelOrderInvExtends> getList() {
		return list;
	}
	public void setList(List<ModelOrderInvExtends> list) {
		this.list = list;
	}
}
