package com.qdzq.main;

import java.util.ArrayList;
import java.util.List;

import com.qdzq.about.AboutActivity;
import com.qdzq.appzqwt.R;
import android.os.Bundle;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

@SuppressWarnings("deprecation")
public class MainActivity extends ActivityGroup {
	private View viewdefault,viewabout;
	private ViewPager viewpager;
	private List<View> list = new ArrayList<View>();

	private Button btndefault,btnabout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		Intent i = this.getIntent();
		listpara = i.getIntegerArrayListExtra("list");

		viewdefault=getView(DefaultActivity.class,"0");
		viewabout=getView(AboutActivity.class,"1");
		list.add(viewdefault);
		list.add(viewabout);
		MyPagerAdapter mypageradapter = new MyPagerAdapter(list);

		viewpager = (ViewPager)this.findViewById(R.id.viewpager);
		viewpager.setAdapter( mypageradapter);
		viewpager.setOnPageChangeListener(new select_OnPageChangeListener());
		viewpager.setCurrentItem(0);

		btndefault = (Button)this.findViewById(R.id.btndefault);
		btnabout = (Button)this.findViewById(R.id.btnabout);
		btndefault.setOnClickListener(new btn_OnClickListener(0));
		btnabout.setOnClickListener(new btn_OnClickListener(1));
	}
	ArrayList<Integer> listpara;
	/*窗体变换
        陈巨传
    20160309*/
	public View getView(Class<?> cls, String index){
		Intent i = new Intent();
		i.setClass(MainActivity.this, cls);
		i.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		if(index.equals("0")){
			i.putIntegerArrayListExtra("list", listpara);
		}
		View ret = this.getLocalActivityManager().startActivity(index, i).getDecorView();
		return ret;
	}
	/*工具事件
	陈巨传
	2016.01.22*/
	public class btn_OnClickListener implements OnClickListener{
		private int arg0;
		public btn_OnClickListener(int arg0){
			this.arg0 = arg0;
		}
		@Override
		public void onClick(View v) {
			viewpager.setCurrentItem(arg0);
		}

	}
	/*滑动事件
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
			viewpager.setCurrentItem(arg0);
			switch(arg0){
				case 0:
					btndefault.performClick();
					break;
				case 1:
					btnabout.performClick();
					break;
			}
		}
	}
	/*退出提示
	陈巨传
	2016.01.22*/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {//退出提示
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0){
			ShowExitDialog(MainActivity.this).show();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	private Dialog ShowExitDialog(Context context){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("退出提示");
		builder.setMessage("确定要退出程序吗?");
		builder.setIcon(R.drawable.ico);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				MainActivity.this.finish();
				System.exit(0);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});
		return builder.create();
	}
	/*释放资源
	    陈巨传
	2016.01.22*/
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	/*再次进入
	陈巨传
	2016.01.22*/
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		if ((Intent.FLAG_ACTIVITY_CLEAR_TOP & intent.getFlags()) != 0) {

		}
	}
}
