package com.qdzq.control;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ControlImageTextView extends LinearLayout {

	public ControlImageTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.setOrientation(LinearLayout.VERTICAL);
		
		ImageView imageView = new ImageView(context, attrs);
	    imageView.setPadding(0, 0, 0, 0);
	    
	    TextView textView = new TextView(context, attrs);
	    textView.setPadding(0, 0, 0, 0);
	    textView.setGravity(Gravity.CENTER_HORIZONTAL);
	    
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
	
}
