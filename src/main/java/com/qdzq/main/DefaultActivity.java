package com.qdzq.main;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import com.qdzq.api.APIDown;
import com.qdzq.api.APIUp;
import com.qdzq.appzqwt.R;
import com.qdzq.cls.ClsMsgThread;
import com.qdzq.comm.PublicOption;
import com.qdzq.control.ControlImageTextView;
import com.qdzq.control.ControlImageTextViewH;
import com.qdzq.entity.EntityOrderInvExtends;
import com.qdzq.entity.ModelOrderInvExtends;
import com.qdzq.port.PortEditActivity;
import com.qdzq.port.PortSetActivity;
import com.qdzq.sqlite.InvDao;
import com.qdzq.sqlite.InvFactDao;
import com.qdzq.sqlite.SQLiteOpenDB;
import com.qdzq.updpwd.UpdPwdActivity;

import entity.comm.EntityError;
import entity.orderinvfact.ModelOrderInvFact;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class DefaultActivity extends Activity implements OnClickListener {
	TextView txttitle;
	ControlImageTextView BtnDown, BtnUp,BtnOk,BtnBlue,BtnUpdPwd,BtnExit;
	ControlImageTextViewH tvdowncount, tvupcount,tvwaitcount;
	ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_default);

		txttitle = (TextView)this.findViewById(R.id.txttitle);
        BtnDown=(ControlImageTextView)this.findViewById(R.id.BtnDown);
        BtnUp=(ControlImageTextView)this.findViewById(R.id.BtnUp);
        BtnOk=(ControlImageTextView)this.findViewById(R.id.BtnOk);
        BtnBlue=(ControlImageTextView)this.findViewById(R.id.BtnBlue);
        BtnUpdPwd=(ControlImageTextView)this.findViewById(R.id.BtnUpdPwd);
        BtnExit=(ControlImageTextView)this.findViewById(R.id.BtnExit);
        tvdowncount=(ControlImageTextViewH)this.findViewById(R.id.tvdowncount);
        tvupcount=(ControlImageTextViewH)this.findViewById(R.id.tvupcount);
        tvwaitcount=(ControlImageTextViewH)this.findViewById(R.id.tvwaitcount);
        
        if(!PublicOption.getIsonline()){
        	txttitle.setText(R.string.default_titleoff);
        }
        
        BtnDown.setOnClickListener(this);
        BtnUp.setOnClickListener(this);
        BtnOk.setOnClickListener(this);
        BtnBlue.setOnClickListener(this);
        /*BtnInit.setOnClickListener(this);*/
        BtnUpdPwd.setOnClickListener(this);
        BtnExit.setOnClickListener(this);
        tvdowncount.setOnClickListener(this);
        tvupcount.setOnClickListener(this);
        tvwaitcount.setOnClickListener(this);
        
        Intent i = this.getIntent(); 
	    listpara = i.getIntegerArrayListExtra("list");
	    LoadMsg();
	}
	ArrayList<Integer> listpara;
	/*按钮事件
	陈巨传
	20160311*/
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.BtnDown){
			if(!PublicOption.getIsonline()){
				Toast toast = Toast.makeText(DefaultActivity.this, "只有联机模式下才可以下载任务！", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;
	        }
			pd = new ProgressDialog(DefaultActivity.this);
	        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        pd.setTitle("数据下载");
	        pd.setMessage("正在下载中......");
	        pd.show();
	        HandlerDown handler = new HandlerDown();
			RunnableDown runnable = new RunnableDown(handler);
			new Thread(runnable).start();
			return;
		} 
		if(v.getId() == R.id.BtnUp){
			if(!PublicOption.getIsonline()){
				Toast toast = Toast.makeText(DefaultActivity.this, "只有联机模式下才可以上传力矩！", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;
	        }
			pd = new ProgressDialog(DefaultActivity.this);
	        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        pd.setTitle("数据上传");
	        pd.setMessage("正在上传中......");
	        pd.show();
	        HandlerUp handler = new HandlerUp();
			RunnableUp runnable = new RunnableUp(handler);
			new Thread(runnable).start();
			return;
		} 
		if(v.getId() == R.id.BtnOk){
			Intent i = new Intent();
			i.setClass(DefaultActivity.this,PortEditActivity.class);
			DefaultActivity.this.startActivity(i);
			return;
		} 
		if(v.getId() == R.id.BtnBlue){
			BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
			if(!mBtAdapter.isEnabled()){
				Toast toast = Toast.makeText(DefaultActivity.this, "蓝牙未打开，请先打开蓝牙！", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;
			}
			Intent i = new Intent();
			i.setClass(DefaultActivity.this,PortSetActivity.class);
			DefaultActivity.this.startActivity(i);
			return;
		} 
		if(v.getId() == R.id.BtnUpdPwd){
			if(!PublicOption.getIsonline()){
				Toast toast = Toast.makeText(DefaultActivity.this, "只有联机模式下才可以修改密码！", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;
	        }
			Intent i = new Intent();
			i.setClass(DefaultActivity.this,UpdPwdActivity.class);
			DefaultActivity.this.startActivity(i);
			return;
		} 
		if(v.getId() == R.id.BtnExit){
			ShowExitDialog(DefaultActivity.this).show();
			return;
		} 
		if(v.getId() == R.id.tvdowncount || v.getId() == R.id.tvupcount || v.getId() == R.id.tvwaitcount){
			if(!PublicOption.getIsonline() && (v.getId() == R.id.tvdowncount || v.getId() == R.id.tvupcount)){
				Toast toast = Toast.makeText(DefaultActivity.this, "只有联机模式下才可以获取最新消息！", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;
	        } 
		
			pd = new ProgressDialog(DefaultActivity.this);
	        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        pd.setTitle("数据更新");
	        pd.setMessage("数据正在更新中......");
	        pd.show();
	        HandlerMsg handler = new HandlerMsg();
			ClsMsgThread runnablemsg = new ClsMsgThread(handler,DefaultActivity.this);
			new Thread(runnablemsg).start();
			return;
		} 
	}
	/*信息提示
	陈巨传
	20160311*/
	protected class HandlerMsg extends Handler{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			listpara = (ArrayList<Integer>)msg.obj;
			LoadMsg();
			pd.dismiss();
			Toast toast = Toast.makeText(DefaultActivity.this, "最新消息更新完毕！", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}
	private void LoadMsg(){
		if(PublicOption.getIsonline()){
			tvupcount.setColor(Color.RED);
			tvdowncount.setColor(Color.RED);
			tvupcount.setText("待上传任务数:"+String.valueOf(listpara.get(0)));
			tvdowncount.setText("待下载任务数:"+String.valueOf(listpara.get(1)));
			tvwaitcount.setVisibility(View.GONE);
		} else{
			tvwaitcount.setColor(Color.RED);	
			tvwaitcount.setText("未完成任务数:"+String.valueOf(listpara.get(0)));
		}
		
	}
	/*下载数据
	陈巨传
	20160311*/
	protected class HandlerDown extends Handler{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			EntityOrderInvExtends _entity = (EntityOrderInvExtends)msg.obj;
			if(!_entity.getErrcode().equals("")){
				pd.dismiss();
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(DefaultActivity.this);
	    		alertBuilder.setIcon(R.drawable.ico);
	    		alertBuilder.setTitle("信息提示");
	    		alertBuilder.setMessage("远程获取异常信息："+_entity.getErrmsg());
	    		alertBuilder.setPositiveButton("确定", null);
	    		alertBuilder.create().show();
	    		return;
			}
			List<ModelOrderInvExtends> list = _entity.getList();
			try{
				 //创建对象
		        InvDao invdao = new InvDao(SQLiteOpenDB.OpenDatabase(DefaultActivity.this));
		        invdao.Add(list);
		        
		        pd.dismiss();
		        
		        Toast toast = Toast.makeText(DefaultActivity.this, "数据下载完毕！", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			} catch(Exception exp){
				pd.dismiss();
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(DefaultActivity.this);
	    		alertBuilder.setIcon(R.drawable.ico);
	    		alertBuilder.setTitle("信息提示");
	    		alertBuilder.setMessage("Sqlite异常信息："+exp.getMessage());
	    		alertBuilder.setPositiveButton("确定", null);
	    		alertBuilder.create().show();
	    		return;
			}
		}
	}
	protected class RunnableDown implements Runnable{
		HandlerDown h;
		public RunnableDown(HandlerDown h){
			this.h = h;
		}
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			// TODO Auto-generated method stub
			APIDown _api = new APIDown();
	        Hashtable<String, String> dic = new Hashtable<String, String>();
	        dic.put("userid", PublicOption.getUserid());
            dic.put("safecode", PublicOption.getSafecode());
            EntityOrderInvExtends _entity = _api.GetList(dic);
	        Message msg = new Message();
	        msg.obj = _entity;
			h.sendMessage(msg);
		}
	}
	/*上传数据
	陈巨传
	20160311*/
	protected class HandlerUp extends Handler{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(msg.obj!=null){
				EntityError _entity = (EntityError)msg.obj;
				if(!_entity.getErrcode().equals("")){
					pd.dismiss();
	                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(DefaultActivity.this);
		    		alertBuilder.setIcon(R.drawable.ico);
		    		alertBuilder.setTitle("信息提示");
		    		alertBuilder.setMessage("远程获取异常信息："+_entity.getErrmsg());
		    		alertBuilder.setPositiveButton("确定", null);
		    		alertBuilder.create().show();
		    		return;
				}
			}
			pd.dismiss();
			
			Toast toast = Toast.makeText(DefaultActivity.this, "数据上传完毕！", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}
	protected class RunnableUp implements Runnable{
		HandlerUp h;
		public RunnableUp(HandlerUp h){
			this.h = h;
		}
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			// TODO Auto-generated method stub
			APIUp _api = new APIUp();
			InvDao _invdao = new InvDao(SQLiteOpenDB.OpenDatabase(DefaultActivity.this));
			List<ModelOrderInvExtends> listinv = _invdao.getWaitUpList();
			InvFactDao _invfactdao = new InvFactDao(SQLiteOpenDB.OpenDatabase(DefaultActivity.this));
			List<ModelOrderInvFact> listinvfact = _invfactdao.getWaitUpList();
			
			Message msg = new Message();
			for(ModelOrderInvExtends minv : listinv){
				Hashtable<String, String> dic = new Hashtable<String, String>();
				dic.put("billids", minv.getBillids());
		        dic.put("billid", minv.getBillid());
		        StringBuilder sb = new StringBuilder(); 
				for(ModelOrderInvFact minvfact : listinvfact){
					if(minvfact.getBillids().equals(minv.getBillids())){
				        if (sb.toString() != "") {
				        	sb.append("|!");
				        }
				        sb.append(minvfact.getFactno());
				        sb.append("|@" + minvfact.getOperstrength());
				        sb.append("|@" + minvfact.getOperdate());
					}
				}
				dic.put("detailstr", sb.toString());
				dic.put("safecode", PublicOption.getSafecode());
	            EntityError _entity = _api.Save(dic);
	            if(_entity.getErrcode().equals("")){
	            	_invfactdao.Del(minv.getBillids());
	            	_invdao.Del(minv.getBillids());
	            } else{
			        msg.obj = _entity;
					break;
	            }
			}  
			h.sendMessage(msg);
		}
	}
	/*退出提示
	陈巨传
	2016.01.22*/
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
				DefaultActivity.this.finish();
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
	/*再次进入
	陈巨传
	2016.01.22*/
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
	}
}
