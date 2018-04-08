package com.qdzq.control;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ControlImageTextViewH extends LinearLayout {
	ImageView imageView;
	TextView textView;
	public ControlImageTextViewH(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.setOrientation(LinearLayout.HORIZONTAL);
		
		imageView = new ImageView(context, attrs);
	    imageView.setPadding(0, 0, 0, 0);
	    
	    textView = new TextView(context, attrs);
	    textView.setPadding(0, 0, 0, 0);
	    textView.setGravity(Gravity.CENTER_VERTICAL);
	    
	    /*TypedArray type = context.obtainStyledAttributes(attrs, R.styleable.ControlImageTextView);
	    Drawable draw1 = type.getDrawable(R.styleable.imagesrc);
        if(draw1!=null){
        	imageView.setImageDrawable(draw1);
        }
        Drawable draw2 = type.getDrawable(R.styleable.imagebg);
        if(draw2!=null){
        	this.setBackgroundDrawable(draw2);
        }*/
	    
	    this.addView(imageView);
	    this.addView(textView);
	}
	public void setText(String str){
		textView.setText(str);
	}
	public void setColor(int c){
		textView.setTextColor(c);
	}
}
