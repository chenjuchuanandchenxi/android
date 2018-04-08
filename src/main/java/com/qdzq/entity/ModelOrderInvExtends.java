package com.qdzq.entity;

import android.graphics.Bitmap;
import entity.orderinv.ModelOrderInv;

public class ModelOrderInvExtends extends ModelOrderInv {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Bitmap invbitmap;
	public Bitmap getInvbitmap() {
		return invbitmap;
	}
	public void setInvbitmap(Bitmap invbitmap) {
		this.invbitmap = invbitmap;
	}
}
