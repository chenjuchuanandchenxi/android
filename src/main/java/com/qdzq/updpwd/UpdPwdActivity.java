package com.qdzq.updpwd;

import java.util.Hashtable;

import com.qdzq.api.APIUser;
import com.qdzq.appzqwt.R;
import com.qdzq.comm.PublicOption;

import entity.comm.EntityError;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class UpdPwdActivity extends Activity {
	ImageView imgback;
	Button btnsave;
	AutoCompleteTextView txtoldpassword,txtnewpassword,txtreppassword;
	ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_updpwd);
		
		imgback =(ImageView)this.findViewById(R.id.imgback);
		imgback.setOnClickListener(new imgback_OnClickListener());
		
		btnsave =(Button)this.findViewById(R.id.btnsave);
		btnsave.setOnClickListener(new btnsave_OnClickListener());
		
		txtoldpassword =(AutoCompleteTextView)this.findViewById(R.id.txtoldpassword);
		txtnewpassword =(AutoCompleteTextView)this.findViewById(R.id.txtnewpassword);
		txtreppassword =(AutoCompleteTextView)this.findViewById(R.id.txtreppassword);
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
			onBackPressed();//必须使用这个调用返回键
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
	/*保存信息
	陈巨传
	2016.1.19*/
	protected class btnsave_OnClickListener implements OnClickListener{
		/* (non-Javadoc)
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(!txtnewpassword.getText().toString().equals(txtreppassword.getText().toString())){
				Toast toast = Toast.makeText(UpdPwdActivity.this, "输入的两次新密码不一致！", Toast.LENGTH_LONG);
	    		toast.setGravity(Gravity.CENTER, 0, 0);
	    		toast.show();
	    		return;
			}
			pd = new ProgressDialog(UpdPwdActivity.this);
	        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        pd.setTitle("密码修改");
	        pd.setMessage("数据正在处理中......");
	        pd.show();
	        HandlerSave handler = new HandlerSave();
	        RunnableSave runnable = new RunnableSave(handler);
			new Thread(runnable).start();
		}
	}
	/*校验用户句柄
	陈巨传
	2016.1.19*/
	protected class HandlerSave extends Handler{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			pd.dismiss();
			
			EntityError _entity = (EntityError)msg.obj;
			String errormsg = "密码修改成功！";
			if(!_entity.getErrcode().equals("")){
				errormsg = _entity.getErrmsg();
			}
			AlertDialog.Builder alertBuilder = new AlertDialog.Builder(UpdPwdActivity.this);
    		alertBuilder.setIcon(R.drawable.ico);
    		alertBuilder.setTitle("信息提示");
    		alertBuilder.setMessage(errormsg);
    		alertBuilder.setPositiveButton("确定", null);
    		alertBuilder.setCancelable(false);
    		alertBuilder.create().show();
		}
		
	}
	/*校验用户线程
	陈巨传
	2016.1.19*/
	protected class RunnableSave implements Runnable{
		HandlerSave h;
		public RunnableSave(HandlerSave h){
			this.h = h;
		}
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			// TODO Auto-generated method stub
			APIUser _api = new APIUser();
	        Hashtable<String, String> dic = new Hashtable<String, String>();
	        dic.put("userid", PublicOption.getUserid().toString());
            dic.put("oldpassword", txtoldpassword.getText().toString());
            dic.put("newpassword", txtnewpassword.getText().toString());
            dic.put("safecode", PublicOption.getSafecode());
	        EntityError _entity = _api.UpdPwd(dic);
	        Message msg = new Message();
	        msg.obj = _entity;
			h.sendMessage(msg);
		}
	}
}
