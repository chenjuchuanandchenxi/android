/**
 * 
 */
package com.qdzq.urlset;


import com.qdzq.appzqwt.R;
import com.qdzq.comm.PublicOption;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * @author Administrator
 *
 */
public class UrlSetActivity extends Activity {
	ImageView imgback;
	Button btnok;
	AutoCompleteTextView txturl;
	SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_urlset);
		
		imgback =(ImageView)this.findViewById(R.id.imgback);
		imgback.setOnClickListener(new imgback_OnClickListener());
		
		btnok =(Button)this.findViewById(R.id.btnok);
		btnok.setOnClickListener(new btnok_OnClickListener());
		
		txturl =(AutoCompleteTextView)this.findViewById(R.id.txturl);
		txturl.setText(PublicOption.getUrl());
		
		sp = UrlSetActivity.this.getSharedPreferences("userinfo",Context.MODE_PRIVATE);//私有，Context常量
	}
	/*返回
	陈巨传
	2016.01.19*/
	protected class imgback_OnClickListener implements OnClickListener{
		/* (non-Javadoc)
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			onBackPressed();
		}
	}
	/*确认
	陈巨传
	2016.01.19*/
	protected class btnok_OnClickListener implements OnClickListener{
		/* (non-Javadoc)
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(txturl.getText().toString().equals("")){
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(UrlSetActivity.this);
	    		alertBuilder.setIcon(R.drawable.ico);
	    		alertBuilder.setTitle("信息提示");
	    		alertBuilder.setMessage("请填写服务器地址！");
	    		alertBuilder.setPositiveButton("确定", null);
	    		alertBuilder.create().show();
	    		return;
			}
			Editor editor = sp.edit();
			editor.putString("url", txturl.getText().toString());
			editor.commit();
			PublicOption.setUrl(txturl.getText().toString());
			Toast toast = Toast.makeText(UrlSetActivity.this, "参数设置成功！", Toast.LENGTH_LONG);
    		toast.setGravity(Gravity.CENTER, 0, 0);
    		toast.show();
		}
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
