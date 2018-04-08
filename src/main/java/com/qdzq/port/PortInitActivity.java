package com.qdzq.port;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import com.qdzq.appzqwt.R;
import com.qdzq.cls.ClsBufferData;
import com.qdzq.comm.DataHelp;
import com.qdzq.comm.DataMsg;
import com.qdzq.comm.PublicOption;
import com.qdzq.control.ControlImageTextView;
import com.qdzq.entity.ModelPortItem;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
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
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class PortInitActivity extends Activity implements OnClickListener {
	ImageView imgback;
	TextView tvlog;
	Spinner spblue;
	Button btnrun;
	CheckBox cklog;
	ControlImageTextView btndel,btninit,btnprec,btnstore,btnunit,btntime;
	ProgressDialog pd;
	PortSpinnerAdapter portspinneradapter;
	
	BluetoothAdapter mBtAdapter;
	BluetoothDevice device;
	BluetoothSocket clientsocket;
	InputStream streamin;
	OutputStream streamout;
	
	boolean _IsRun =false;//是否开启
	boolean _IsCheck=false;//是否检测
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_portinit);
		
		imgback =(ImageView)this.findViewById(R.id.imgback);
		imgback.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FinishActivity();
			}
			
		});
		spblue =(Spinner)this.findViewById(R.id.spblue);
		tvlog = (TextView)this.findViewById(R.id.tvlog);
		btnrun =(Button)this.findViewById(R.id.btnrun);
		btndel =(ControlImageTextView)this.findViewById(R.id.btndel);
		btninit =(ControlImageTextView)this.findViewById(R.id.btninit);
		btnprec =(ControlImageTextView)this.findViewById(R.id.btnprec);
		btnstore =(ControlImageTextView)this.findViewById(R.id.btnstore);
		btnunit =(ControlImageTextView)this.findViewById(R.id.btnunit);
		btntime =(ControlImageTextView)this.findViewById(R.id.btntime);
		cklog =(CheckBox)this.findViewById(R.id.cklog);
		cklog.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					tvlog.setVisibility(View.VISIBLE);
				} else{
					tvlog.setVisibility(View.GONE);
				}
			}
		});
		
		btnrun.setOnClickListener(this);
		btninit.setOnClickListener(this);
		btndel.setOnClickListener(this);
		btnprec.setOnClickListener(this);
		btnstore.setOnClickListener(this);
		btnunit.setOnClickListener(this);
		btntime.setOnClickListener(this);
				
		portspinneradapter = new PortSpinnerAdapter(PublicOption.getListdevices(),PortInitActivity.this);
		spblue.setAdapter(portspinneradapter);
	}
	/*按钮选择
	陈巨传
	20160315*/
	@SuppressLint("NewApi") 
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.btnrun){
			if(spblue.getSelectedItem()==null){
				Toast toast = Toast.makeText(PortInitActivity.this, "请先进行工作蓝牙设置！", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;
			}
			if(btnrun.getText().toString().equals("开始运行")){
				ModelPortItem model = (ModelPortItem)spblue.getSelectedItem();
				
				mBtAdapter = BluetoothAdapter.getDefaultAdapter();
				device = mBtAdapter.getRemoteDevice(model.getPortaddress());
				
				pd = new ProgressDialog(PortInitActivity.this);
		        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		        pd.setTitle("远程蓝牙");
		        pd.setMessage("正在启动中......");
		        pd.show();
				
				ConnectHandler h = new ConnectHandler();
				ConnectRunable connrunable = new ConnectRunable(h);
				Thread connthread = new Thread(connrunable);
				connthread.start();
				
				_IsRun = true;
				_IsCheck=false;
				btnrun.setText(R.string.portfact_btnstop);
				DataHelp.WriteLog("启动远程蓝牙设备",tvlog);
			}else{
				_IsRun = false;
				_IsCheck =false;
				OnCancel();
				btnrun.setText(R.string.portfact_btnrun);
				DataHelp.WriteLog("停止远程蓝牙设备",tvlog);
			}
			
			return;
		} else{
			if(clientsocket==null){
				Toast toast = Toast.makeText(PortInitActivity.this, "必须连接远程蓝牙后才可以操作！", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;
			}
			if(!clientsocket.isConnected()){
				Toast toast = Toast.makeText(PortInitActivity.this, "必须连接远程蓝牙后才可以操作！", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;
			}
			if(v.getId()==R.id.btninit){
				WriteBuffer("发送-恢复出厂值",ClsBufferData.getBufferDefault());
				return;
			}
			if(v.getId()==R.id.btndel){	
				WriteBuffer("发送-清空数据",ClsBufferData.getBufferClear());
				return;
			}
			if(v.getId()==R.id.btnprec){	
				Prec();
				return;
			}
			if(v.getId()==R.id.btnunit){	
				Unit();
				return;
			}
			if(v.getId()==R.id.btnstore){	
				Store();
				return;
			}
			if(v.getId()==R.id.btntime){	
				WriteBuffer("发送-同步时间",ClsBufferData.getBufferTime());
				return;
			}
		}
	}
	/*写入信息
	陈巨传
	20160315*/
	private void WriteBuffer(String msg, byte[] buffer){
		try {
			streamout.write(buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg = msg+":"+e.getMessage();
			
			btnrun.performClick();//模拟点击
			Toast toast = Toast.makeText(PortInitActivity.this, "请仔细检查蓝牙设备是否正常！", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
		DataHelp.WriteLog(buffer,msg,tvlog);
	}
	/*单位设置
	陈巨传
	20160315*/
	private void Unit(){
		AlertDialog.Builder alertBuilder2 = new AlertDialog.Builder(PortInitActivity.this);
		alertBuilder2.setTitle("单位");
		alertBuilder2.setSingleChoiceItems(arrayUnit,0,new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				selectedUnitIndex=which;
			}
		});
		alertBuilder2.setPositiveButton("确定",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				WriteBuffer("发送-单位设置-"+arrayUnit[selectedUnitIndex].toString(),ClsBufferData.getBufferUnit(selectedUnitIndex));
			}
		});
		alertBuilder2.setNegativeButton("关闭", null);
		alertBuilder2.setCancelable(false);
		alertBuilder2.create().show();
	}
	int selectedUnitIndex = 0; 
	String[] arrayUnit = new String[] {"N.m", "lbf.fn", "lbf.in"}; 
	/*精度设置
	陈巨传
	20160315*/
	private void Prec(){
		AlertDialog.Builder alertBuilder2 = new AlertDialog.Builder(PortInitActivity.this);
		alertBuilder2.setTitle("精度");
		alertBuilder2.setSingleChoiceItems(arrayPrec,0,new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				selectedPrecIndex=which;
			}
		});
		alertBuilder2.setPositiveButton("确定",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				WriteBuffer("发送-判断精度-"+arrayPrec[selectedPrecIndex].toString(),ClsBufferData.getBufferPrec(selectedPrecIndex));
			}
		});
		alertBuilder2.setNegativeButton("关闭", null);
		alertBuilder2.setCancelable(false);
		alertBuilder2.create().show();
	}
	int selectedPrecIndex = 0; 
	String[] arrayPrec = new String[] {"±1%", "±2%", "±3%", "±4%"}; 
	/*存储设置
	陈巨传
	20160315*/
	private void Store(){
		AlertDialog.Builder alertBuilder2 = new AlertDialog.Builder(PortInitActivity.this);
		alertBuilder2.setTitle("精度");
		alertBuilder2.setSingleChoiceItems(arrayStore,0,new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				selectedStoreIndex=which;
			}
		});
		alertBuilder2.setPositiveButton("确定",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				WriteBuffer("发送-存储模式-"+arrayStore[selectedStoreIndex].toString(),ClsBufferData.getBufferStore(selectedStoreIndex));
			}
		});
		alertBuilder2.setNegativeButton("关闭", null);
		alertBuilder2.setCancelable(false);
		alertBuilder2.create().show();
	}
	int selectedStoreIndex = 0; 
	String[] arrayStore = new String[] {"0S", "0.5S", "1S", "1.5S", "2.0S", "2.5S", "3.0S", "3.5S", "4.0S"}; 
	/*连接交换线程
	陈巨传
	20160306*/
	private class ConnectRunable implements Runnable{
		private ConnectHandler h;
		public ConnectRunable(ConnectHandler h){
			super();
			this.h = h;
			String uuid=PublicOption.getUuid();
			try {
				clientsocket = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid));
			} catch (IOException e) {
				Message msg = new Message();
				DataMsg datamsg = new DataMsg();
				datamsg.setMsgcode("-1");
				datamsg.setMsgcontent(e.getMessage());
				msg.obj = datamsg;
				h.sendMessage(msg);
			}
		}
		@Override
		public void run() {
			Message msg = new Message();
			try{
				clientsocket.connect();
				DataMsg datamsg = new DataMsg();
				datamsg.setMsgcontent("远程蓝牙连接成功");
				msg.obj = datamsg;
			} catch(Exception exp){
				DataMsg datamsg = new DataMsg();
				datamsg.setMsgcode("-1");
				datamsg.setMsgcontent(exp.getMessage());
				try {
					clientsocket.close();
				} catch (IOException e) {
					datamsg.setMsgcontent(datamsg.getMsgcontent() + "/"+e.getMessage());
				}
				msg.obj = datamsg;
			}
			h.sendMessage(msg);
		}
	}
	/*连接交换代理
	陈巨传
	20160306*/
	private class ConnectHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			DataMsg datamsg = (DataMsg)msg.obj;
			DataHelp.WriteLog(datamsg.getMsgcontent().toString(),tvlog);
			if(datamsg.getMsgcode().equals("-1")){//出现异常
				pd.dismiss();
				btnrun.performClick();//模拟点击
				Toast toast = Toast.makeText(PortInitActivity.this, "请仔细检查蓝牙设备是否正常！", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
			if(datamsg.getMsgcode().equals("")){//成功
				ConnectedHandler h = new ConnectedHandler();
				
				ConnectedRunable connedrunable = new ConnectedRunable(h);
				Thread connedthread = new Thread(connedrunable);
				connedthread.start();
				DataHelp.WriteLog("远程蓝牙设备握手",tvlog);
			}
		}
	}
	/*数据交换线程
	陈巨传
	20160306*/
	private class ConnectedRunable implements Runnable{
		private ConnectedHandler h;
		public ConnectedRunable(ConnectedHandler h){
			this.h = h;
			try {
				streamin = clientsocket.getInputStream();
				streamout = clientsocket.getOutputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Message msg = new Message();
				DataMsg datamsg = new DataMsg();
				datamsg.setMsgcode("-1");
				datamsg.setMsgcontent(e.getMessage());
				msg.obj = datamsg;
				h.sendMessage(msg);
			}
		}
		@SuppressLint("NewApi") 
		@Override
		public void run() {
			while (_IsRun) {
				try {
					if(clientsocket.isConnected()){
			        	//发送
						if (!_IsCheck) {
							streamout.write(ClsBufferData.getBufferLinkPut());
							Message msg = new Message();
							DataMsg datamsg = new DataMsg();
							String str = DataHelp.Bytes2HexString(ClsBufferData.getBufferLinkPut());
	        				datamsg.setMsgcontent("发送-握手信息:"+str);
	        				msg.obj = datamsg;
							h.sendMessage(msg);
						}
						//延时1秒
		        		try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		        		//接收
	        			byte[] buffer = new byte[1024]; 
	        			int bytes = streamin.read(buffer);
	        			if(bytes>0){
	        				String str=DataHelp.Bytes2HexString(buffer);
	        				if (str.substring(0, 22).trim().equals(ClsBufferData.getStringLinkGet())) {
	        					 _IsCheck = true;
	        					 _IsRun = false;
		                    }
	        				Message msg = new Message();
	        				DataMsg datamsg = new DataMsg();
	        				datamsg.setMsgcode("10");
	        				datamsg.setMsgcontent("接收-握手信息:"+str.substring(0, 22).toString());
	        				msg.obj = datamsg;
							h.sendMessage(msg);
	        			}
					}
				} catch (IOException e) {
	            	Message msg = new Message();
	            	DataMsg datamsg = new DataMsg();
	            	datamsg.setMsgcode("-10");
					datamsg.setMsgcontent(e.getMessage());
					msg.obj = datamsg;
					h.sendMessage(msg);
	            }
        		//延时1秒
		    	try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
		}
	}
	/*数据交换代理
	陈巨传
	20160306*/
	private class ConnectedHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			DataMsg datamsg = (DataMsg)msg.obj;
			DataHelp.WriteLog(datamsg.getMsgcontent().toString(),tvlog);
			if(datamsg.getMsgcode().equals("10")){//连接成功
				pd.dismiss();
				Toast toast = Toast.makeText(PortInitActivity.this, "与远程蓝牙通讯成功！", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
			if(datamsg.getMsgcode().equals("-10")){
				btnrun.performClick();//模拟点击
				Toast toast = Toast.makeText(PortInitActivity.this, "请仔细检查蓝牙设备是否正常！", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		}
	}
	/*键盘退出
	陈巨传
	2016.01.22*/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {//退出提示
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			FinishActivity();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	/*公用退出
	陈巨传
	2016.01.22*/
	private void FinishActivity(){
		if(btnrun.getText().toString().equals("开始运行")){
			this.finish();
		} else{
			Toast toast = Toast.makeText(PortInitActivity.this, "请先停止运行！", Toast.LENGTH_LONG);
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
	/*变量释放
	陈巨传
	20160303*/
	@SuppressLint("NewApi")
	private void OnCancel(){
		if(clientsocket!=null){
			if(clientsocket.isConnected()){
				try {
					clientsocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			clientsocket=null;
		}
		if(device!=null){
			device=null;
		}
		if (mBtAdapter != null){
			mBtAdapter.cancelDiscovery();
		}
		if(streamin!=null){
			streamin=null;
		}
		if(streamout!=null){
			streamout=null;
		}
	}
}
