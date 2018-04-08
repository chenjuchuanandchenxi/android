/**
 * 
 */
package com.qdzq.login;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.qdzq.api.APIUser;
import com.qdzq.appzqwt.R;
import com.qdzq.cls.ClsMsgThread;
import com.qdzq.comm.DataDesUtil;
import com.qdzq.comm.PublicOption;
import com.qdzq.entity.ModelPortItem;
import com.qdzq.main.MainActivity;
import com.qdzq.urlset.UrlSetActivity;

import entity.user.EntityUser;
import entity.user.ModelUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import android.widget.TextView;
/**
 * @author Administrator
 *
 */
public class LoginActivity extends Activity {
	TextView txtusercode,txtuserpwd;
	Button btnlogin,btnoffline;
	TextView btnurlset;
	CheckBox chkremeber;
	SharedPreferences sp;
	ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_login);
		
		txtusercode = (TextView)this.findViewById(R.id.txtusercode);
		txtuserpwd = (TextView)this.findViewById(R.id.txtuserpwd);
		
		btnlogin = (Button)this.findViewById(R.id.btnlogin);
		btnoffline =(Button)this.findViewById(R.id.btnoffline);
		btnurlset = (TextView)this.findViewById(R.id.btnurlset);
		chkremeber = (CheckBox)this.findViewById(R.id.chkremeber);
		
		sp = LoginActivity.this.getSharedPreferences("userinfo",Context.MODE_PRIVATE);//私有，Context常量
		LoadRemeber();
		
		btnlogin.setOnClickListener(new btnlogin_OnClickListener());
		btnoffline.setOnClickListener(new btnoffline_OnClickListener());
		btnurlset.setOnClickListener(new btnurlset_OnClickListener());
	}
	/*加载记住信息
	陈巨传
	2016.01.19*/
	private void LoadRemeber(){
		//脱机登录
		PublicOption.setUserid(sp.getString("userid", ""));
		//登录信息
		PublicOption.setUsercode(sp.getString("usercode", ""));
		PublicOption.setUsername(sp.getString("username", ""));
		String userpwd = sp.getString("userpwd", "");
		if(!PublicOption.getUsercode().equals("")){
			txtusercode.setText(PublicOption.getUsercode());
			txtuserpwd.setText(userpwd);
			chkremeber.setChecked(true);
		}
		//服务地址
		String url = sp.getString("url", PublicOption.getUrl());
		PublicOption.setUrl(url);
		//我的蓝牙
		String strlist = sp.getString("listdevices", "");
		if(!strlist.equals("")){
			String[] items = strlist.split(";");
			List<ModelPortItem> list = new ArrayList<ModelPortItem>();
			for(int i=0;i<items.length;i++){
				ModelPortItem model = new ModelPortItem();
				String[] item = items[i].split("\\|");
				model.setPortname(item[0]);
				model.setPortaddress(item[1]);
				model.setPortaddname(item[2]);
				list.add(model);
			}
			PublicOption.setListdevices(list);
		}
	}
	/*联机登录
	陈巨传
	2016.1.19*/
	protected class btnlogin_OnClickListener implements OnClickListener{
		/* (non-Javadoc)
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(txtusercode.getText().toString().equals("")){
				Toast toast = Toast.makeText(LoginActivity.this, "账户必须输入！", Toast.LENGTH_SHORT);
	    		toast.setGravity(Gravity.CENTER, 0, 0);
	    		toast.show();
	    		return;
			}
			pd = new ProgressDialog(LoginActivity.this);
	        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        pd.setTitle("用户校验");
	        pd.setMessage("正在校验中......");
	        pd.show();
	        HandlerCheck handler = new HandlerCheck();
			RunnableCheck runnable = new RunnableCheck(handler);
			new Thread(runnable).start();
		}
	}
	/*脱机登录
	陈巨传
	20160311*/
	private class btnoffline_OnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(PublicOption.getUserid().equals("")){
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(LoginActivity.this);
	    		alertBuilder.setIcon(R.drawable.ico);
	    		alertBuilder.setTitle("信息提示");
	    		alertBuilder.setMessage("未联机登录过，请先联机登录一次！");
	    		alertBuilder.setPositiveButton("确定", null);
	    		alertBuilder.setCancelable(false);
	    		alertBuilder.create().show();
	    		return;
			}
			PublicOption.setIsonline(false);
			
			pd = new ProgressDialog(LoginActivity.this);
	        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        pd.setTitle("用户校验");
	        pd.setMessage("正在校验中......");
	        pd.show();
	        HandlerMsg handler = new HandlerMsg();
			ClsMsgThread runnablemsg = new ClsMsgThread(handler,LoginActivity.this);
			new Thread(runnablemsg).start();
		}
		
	}
	/*参数设置
	陈巨传
	2016.1.19*/
	protected class btnurlset_OnClickListener implements OnClickListener{

		/* (non-Javadoc)
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent i = new Intent();
			i.setClass(LoginActivity.this, UrlSetActivity.class);
			LoginActivity.this.startActivity(i);
		}
		
	}
	/*校验用户句柄
	陈巨传
	2016.1.19*/
	protected class HandlerCheck extends Handler{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			EntityUser _entity = (EntityUser)msg.obj;
			if(!_entity.getErrcode().equals("")){
				pd.dismiss();
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(LoginActivity.this);
	    		alertBuilder.setIcon(R.drawable.ico);
	    		alertBuilder.setTitle("信息提示");
	    		alertBuilder.setMessage("异常信息："+_entity.getErrmsg());
	    		alertBuilder.setPositiveButton("确定", null);
	    		alertBuilder.setCancelable(false);
	    		alertBuilder.create().show();
	    		return;
			}
			
			ModelUser _model = _entity.getInfo();
			boolean _ispass = _model.isPass();
			if(!_ispass){
				pd.dismiss();
				AlertDialog.Builder alertBuilder = new AlertDialog.Builder(LoginActivity.this);
	    		alertBuilder.setIcon(R.drawable.ico);
	    		alertBuilder.setTitle("信息提示");
	    		alertBuilder.setMessage("账户或密码错误，请核对后重新登录！");
	    		alertBuilder.setPositiveButton("确定", null);
	    		alertBuilder.setCancelable(false);
	    		alertBuilder.create().show();
	    		return;
			}

			PublicOption.setUserid(_model.getUserid());
			PublicOption.setUsercode(_model.getUsercode());
			PublicOption.setUsername(_model.getUsername());
			
			Editor editor = sp.edit();
			editor.putString("userid", PublicOption.getUserid());
			if(chkremeber.isChecked()){
				editor.putString("usercode", txtusercode.getText().toString());
				editor.putString("userpwd", txtuserpwd.getText().toString());
			} else{
				editor.putString("usercode", "");
				editor.putString("userpwd", "");
			}
			editor.commit();
			
			HandlerMsg handler = new HandlerMsg();
			ClsMsgThread runnablemsg = new ClsMsgThread(handler,LoginActivity.this);
			new Thread(runnablemsg).start();
		}
	}
	/*获取消息
	陈巨传
	20160311*/
	protected class HandlerMsg extends Handler{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			pd.dismiss();
			
			ArrayList<Integer> list = (ArrayList<Integer>)msg.obj;

    		Intent i = new Intent();
    		i.setClass(LoginActivity.this, MainActivity.class);
    		i.putIntegerArrayListExtra("list", list);
    		LoginActivity.this.startActivity(i);
			LoginActivity.this.finish();
		}
	}
	/*校验用户线程
	陈巨传
	2016.1.19*/
	protected class RunnableCheck implements Runnable{
		HandlerCheck h;
		public RunnableCheck(HandlerCheck h){
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
	        String _UserCode=txtusercode.getText().toString();
	       	String _Password =txtuserpwd.getText().toString();
	       	try {
				_Password = DataDesUtil.encode(_Password);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
            dic.put("usercode", _UserCode);
            dic.put("userpwd", _Password);
            dic.put("safecode", PublicOption.getSafecode());
	        EntityUser _entity = _api.Check(dic);
	        Message msg = new Message();
	        msg.obj = _entity;
			h.sendMessage(msg);
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
