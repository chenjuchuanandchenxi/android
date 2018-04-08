/**
 * 
 */
package com.qdzq.guide;


import java.util.ArrayList;
import java.util.List;

import com.qdzq.appzqwt.R;
import com.qdzq.login.LoginActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

/**
 * @author Administrator
 *
 */
public class GuideActivity extends Activity {
	private View guide1,guide2,guide3;
	private ImageView imgnav1,imgnav2,imgnav3;
	private Button btnenter;
	private ViewPager viewpager;
	private List<View> list = new ArrayList<View>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_guide);
		
		LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);//获取本应用的资源
		guide1 = inflater.inflate(R.layout.activity_guide1, null);
		guide2 = inflater.inflate(R.layout.activity_guide2, null);
		guide3 = inflater.inflate(R.layout.activity_guide3, null);
		list.add(guide1);
		list.add(guide2);
		list.add(guide3);
		MyPagerAdapter mypageradapter = new MyPagerAdapter(list);
		
		imgnav1 = (ImageView)this.findViewById(R.id.imgnav1);
		imgnav2 = (ImageView)this.findViewById(R.id.imgnav2);
		imgnav3 = (ImageView)this.findViewById(R.id.imgnav3);
		
		btnenter = (Button)guide3.findViewById(R.id.btnenter);
		btnenter.setOnClickListener(new btnenter_OnClickListener());
		
		viewpager = (ViewPager)this.findViewById(R.id.viewpager);
		viewpager.setOnPageChangeListener(new select_OnPageChangeListener());
		viewpager.setAdapter(mypageradapter);
		viewpager.setCurrentItem(0);
	}
	/*页面改变
	陈巨传
	2016.01.22*/
	public class select_OnPageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			switch(arg0){
				case 0:{
					imgnav1.setImageResource(R.drawable.guide_focused);
					imgnav2.setImageResource(R.drawable.guide_normal);
					imgnav3.setImageResource(R.drawable.guide_normal);
					break;
				}
				case 1:{
					imgnav2.setImageResource(R.drawable.guide_focused);
					imgnav1.setImageResource(R.drawable.guide_normal);
					imgnav3.setImageResource(R.drawable.guide_normal);
					break;
				}
				case 2:{
					imgnav3.setImageResource(R.drawable.guide_focused);
					imgnav1.setImageResource(R.drawable.guide_normal);
					imgnav2.setImageResource(R.drawable.guide_normal);
					break;
				}
			}
			viewpager.setCurrentItem(arg0);
		}
	}
	/*进入系统
	陈巨传
	2016.01.22*/
	public class btnenter_OnClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent i = new Intent();
			i.setClass(guide3.getContext(),LoginActivity.class);
			guide3.getContext().startActivity(i);
			GuideActivity.this.finish();
		}
	}
	/*退回键盘
	陈巨传
	2016.01.22*/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			this.finish();//之后如果写有onDestroy，会调用onDestroy
			System.exit(0);//退出整个应用
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	/*释放资源
	    陈巨传
	2016.01.22*/
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
