/**
 *
 */
package com.qdzq.guide;

import com.qdzq.appzqwt.R;
import com.qdzq.login.LoginActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
public class StartActivity extends Activity {
	TextView txttimer;
	SharedPreferences sp;
	int value;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_start);

		sp = StartActivity.this.getSharedPreferences("countinfo",Context.MODE_PRIVATE);//私有，Context常量
		String logincount = sp.getString("logincount", "");
		value = 0;
		if(!logincount.equals("")){
			value = value + Integer.parseInt(logincount);
		}
		Editor editor = sp.edit();
		editor.putString("logincount", String.valueOf(value+1));
		editor.commit();

		txttimer = (TextView)this.findViewById(R.id.txttimer);
		TimeCount time = new TimeCount(3000,1000);
		time.start();
	}
	/*倒计时类
	陈巨传
	2016.01.20*/
	protected class TimeCount extends CountDownTimer{

		/**
		 * @param millisInFuture    :总时长
		 * @param countDownInterval :计时的时间间隔
		 */
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			// TODO Auto-generated constructor stub
		}

		/* (non-Javadoc)
		 * @see android.os.CountDownTimer#onFinish()
		 */
		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			Intent i = new Intent();
			if(value==0){
				i.setClass(StartActivity.this, GuideActivity.class);
			} else{
				i.setClass(StartActivity.this, LoginActivity.class);
			}
			StartActivity.this.startActivity(i);
			StartActivity.this.finish();
		}

		/* (non-Javadoc)
		 * @see android.os.CountDownTimer#onTick(long)
		 */
		@Override
		public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub
			long text = millisUntilFinished/1000;
			txttimer.setText("倒计时："+String.valueOf(text)+"秒");
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
	/*释放资源：可以去掉，不过最好都有
	    陈巨传
	2016.01.22*/
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
