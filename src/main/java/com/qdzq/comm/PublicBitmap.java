package com.qdzq.comm;

import android.graphics.Bitmap;

public class PublicBitmap {
	private static Bitmap bitmap;

	public static Bitmap getBitmap() {
		return bitmap;
	}

	public static void setBitmap(Bitmap bitmap) {
		PublicBitmap.bitmap = bitmap;
	}
	
}
