package com.qdzq.port;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;

import com.qdzq.api.APIUp;
import com.qdzq.appzqwt.R;
import com.qdzq.comm.DataHelp;
import com.qdzq.comm.DataMsg;
import com.qdzq.comm.PublicOption;
import com.qdzq.entity.ModelOrderInvExtends;
import com.qdzq.entity.ModelPortItem;
import com.qdzq.sqlite.InvFactDao;
import com.qdzq.sqlite.SQLiteOpenDB;

import entity.comm.EntityError;
import entity.orderinvfact.EntityOrderInvFact;
import entity.orderinvfact.ModelOrderInvFact;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class PortFactActivity extends Activity implements OnClickListener {
	//界面组件
	ImageView imgback;
	TextView tvlog, txtbillcode,txtinvstd,txtposname,txtqty,txtlimitname,txtstrength;
	Button btnrun;
	ListView lvlist;
	ProgressBar bar;
	CheckBox cklog,ckreceive;
	//下拉蓝牙
	Spinner spblue;
	PortSpinnerAdapter portspinneradapter;
	//已操作的力矩列表
	PortFactAdapter myPortFactAdapter;
	List<ModelOrderInvFact> listfact = new ArrayList<ModelOrderInvFact>();
	//蓝牙对象
	BluetoothAdapter mBtAdapter;
	BluetoothDevice device;
	BluetoothSocket clientsocket;
	//蓝牙数据流
	InputStream streamin;
	OutputStream streamout;
	//螺栓信息
	ModelOrderInvExtends modelinv;
	//进度对话框
	ProgressDialog pd;
	//公用变量
	boolean _IsRun =true;
	
	SoundPool mysoundPool;
	int iopen,iclose,i0,i1,i2,i3,i4,i5,i6,i7,i8,i9,ibeep1,ibeep2,ibeep3,itop,ibom,isave,iselect,ierrdata;
	/*初始加载
	陈巨传
	20160316*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_portfact);
		mysoundPool=new SoundPool(20,AudioManager.STREAM_MUSIC,0);
		iopen = mysoundPool.load(PortFactActivity.this, R.raw.open, 1);
		iclose = mysoundPool.load(PortFactActivity.this, R.raw.close, 1);
		i0 = mysoundPool.load(PortFactActivity.this, R.raw.n0, 1);
		i1 = mysoundPool.load(PortFactActivity.this, R.raw.n1, 1);
		i2 = mysoundPool.load(PortFactActivity.this, R.raw.n2, 1);
		i3 = mysoundPool.load(PortFactActivity.this, R.raw.n3, 1);
		i4 = mysoundPool.load(PortFactActivity.this, R.raw.n4, 1);
		i5 = mysoundPool.load(PortFactActivity.this, R.raw.n5, 1);
		i6 = mysoundPool.load(PortFactActivity.this, R.raw.n6, 1);
		i7 = mysoundPool.load(PortFactActivity.this, R.raw.n7, 1);
		i8 = mysoundPool.load(PortFactActivity.this, R.raw.n8, 1);
		i9 = mysoundPool.load(PortFactActivity.this, R.raw.n9, 1);
		ibeep1 = mysoundPool.load(PortFactActivity.this, R.raw.beep1, 1);
		ibeep2 = mysoundPool.load(PortFactActivity.this, R.raw.beep2, 1);
		ibeep3 = mysoundPool.load(PortFactActivity.this, R.raw.beep3, 1);
		itop = mysoundPool.load(PortFactActivity.this, R.raw.top, 1);
		ibom = mysoundPool.load(PortFactActivity.this, R.raw.bom, 1);
		isave = mysoundPool.load(PortFactActivity.this, R.raw.save, 1);
		iselect = mysoundPool.load(PortFactActivity.this, R.raw.select, 1);
		ierrdata = mysoundPool.load(PortFactActivity.this, R.raw.errdata, 1);
		//获取传递过来的单个任务信息
		Intent i = this.getIntent(); 
		modelinv=(ModelOrderInvExtends)i.getSerializableExtra("model");
		//获取组件实体
		txtbillcode = (TextView)this.findViewById(R.id.txtbillcode);
		txtbillcode.setText("单号："+modelinv.getBillcode());
		txtinvstd = (TextView)this.findViewById(R.id.txtinvstd);
		txtinvstd.setText("螺栓："+modelinv.getInvstd());
		txtposname = (TextView)this.findViewById(R.id.txtposname);
		txtposname.setText("位置："+modelinv.getPosname());
		txtqty = (TextView)this.findViewById(R.id.txtqty);
		txtqty.setText("数量："+modelinv.getQty());
		txtlimitname = (TextView)this.findViewById(R.id.txtlimitname);
		txtlimitname.setText("误差："+modelinv.getLimitname());
		txtstrength = (TextView)this.findViewById(R.id.txtstrength);
		txtstrength.setText("标准："+modelinv.getStrength());
		imgback =(ImageView)this.findViewById(R.id.imgback);
		tvlog = (TextView)this.findViewById(R.id.tvlog);
		btnrun =(Button)this.findViewById(R.id.btnrun);
		spblue =(Spinner)this.findViewById(R.id.spblue);
		lvlist = (ListView)this.findViewById(R.id.lvlist);
		bar = (ProgressBar)this.findViewById(R.id.bar);
		cklog =(CheckBox)this.findViewById(R.id.cklog);
		ckreceive=(CheckBox)this.findViewById(R.id.ckreceive);
		//显示日志事件
		cklog.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				if(isChecked){
					tvlog.setVisibility(View.VISIBLE);
				} else{
					tvlog.setVisibility(View.GONE);
				}
			}
		});
		ckreceive.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				if(isChecked){
            		mysoundPool.play(iopen,1, 1, 1, 0, 1);
            	} else{
            		mysoundPool.play(iclose,1, 1, 1, 0, 1);
            	}
				try {
    				Thread.sleep(1000);
    				_IsRun=isChecked;
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
			}
		});
		tvlog.setMovementMethod(ScrollingMovementMethod.getInstance());
		//运行和返回按钮事件
		btnrun.setOnClickListener(this);
		imgback.setOnClickListener(this);
		//加载蓝牙设备列表
		portspinneradapter = new PortSpinnerAdapter(PublicOption.getListdevices(),PortFactActivity.this);
		spblue.setAdapter(portspinneradapter);
		//加载已有力矩信息
		pd = new ProgressDialog(PortFactActivity.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setTitle("力矩加载");
        pd.setMessage("正在加载中......");
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        HandlerList handler = new HandlerList();
		RunnableList runnable = new RunnableList(handler);
		new Thread(runnable).start();
	}
	/*已操作力矩列表：必须使用线程和代理
	陈巨传
	2016.1.19*/
	protected class HandlerList extends Handler{
		@Override
		public void handleMessage(Message msg) {
			EntityOrderInvFact _entity = (EntityOrderInvFact)msg.obj;
			listfact = _entity.getList();
			myPortFactAdapter = new PortFactAdapter(listfact,PortFactActivity.this);
			lvlist.setAdapter(myPortFactAdapter);			
			pd.dismiss();
		}
	}
	protected class RunnableList implements Runnable{
		HandlerList h;
		public RunnableList(HandlerList h){
			this.h = h;
		}
		@Override
		public void run() {
			if(PublicOption.getIsonline()){
				APIUp _api = new APIUp();
		        Hashtable<String, String> dic = new Hashtable<String, String>();
		        dic.put("billids", modelinv.getBillids());
	            dic.put("safecode", PublicOption.getSafecode());
	            EntityOrderInvFact _entity = _api.GetList(dic);
		        Message msg = new Message();
		        msg.obj = _entity;
		        h.sendMessage(msg);
			} else{
				InvFactDao _invdaofact = new InvFactDao(SQLiteOpenDB.OpenDatabase(PortFactActivity.this));
				EntityOrderInvFact _entity = new EntityOrderInvFact();
				List<ModelOrderInvFact> _list = _invdaofact.getList(modelinv.getBillids());
				_entity.setList(_list);
		        Message msg = new Message();
		        msg.obj = _entity;
		        h.sendMessage(msg);
			}
		}
	}
	/*不同按钮对应事件
	陈巨传
	20160315*/
	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btnrun){
			if(spblue.getSelectedItem()==null){
				Toast toast = Toast.makeText(PortFactActivity.this, "请先进行工作蓝牙设置！", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;
			}
			if(btnrun.getText().toString().equals("开始运行")){//启动蓝牙
				ModelPortItem model = (ModelPortItem)spblue.getSelectedItem();
				
				mBtAdapter = BluetoothAdapter.getDefaultAdapter();
				device = mBtAdapter.getRemoteDevice(model.getPortaddress());
				
				pd = new ProgressDialog(PortFactActivity.this);
		        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		        pd.setTitle("远程蓝牙");
		        pd.setMessage("正在启动中......");
		        pd.setCancelable(true);
		        pd.setCanceledOnTouchOutside(false);
		        pd.show();
		        
				ConnectBlueHandler hblue = new ConnectBlueHandler();
				ConnectBlueRunable rblue = new ConnectBlueRunable(hblue);
				Thread tblue = new Thread(rblue);
				tblue.start();

				DataHelp.WriteLog("启动远程蓝牙设备",tvlog);
			} else{//关闭蓝牙
				bar.setProgress(0);
				btnrun.setText("开始运行");
				if(clientsocket!=null){
					if(clientsocket.isConnected()){
						try {
							clientsocket.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					clientsocket=null;
				}
				DataHelp.WriteLog("已停止远程蓝牙设备",tvlog);
				Toast toast = Toast.makeText(PortFactActivity.this, "已停止远程蓝牙设备", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
			return;
		}
		if(v.getId()==R.id.imgback){//返回按钮
			FinishActivity();
			return;
		}
	}
	//启动蓝牙：必须使用线程和代理
	private class ConnectBlueHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			DataMsg datamsg = (DataMsg)msg.obj;
			DataHelp.WriteLog(datamsg.getMsgcontent().toString(),tvlog);
			pd.dismiss();
			if(datamsg.getMsgcode().equals("-1")){//出现异常
				Toast toast = Toast.makeText(PortFactActivity.this, "请仔细检查蓝牙设备是否正常！", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
			if(datamsg.getMsgcode().equals("")){
				btnrun.setText("停止运行");
				ReceiveHandler rh = new ReceiveHandler();
				ProgressHandler ph = new ProgressHandler();
				AdapterHandler ah = new AdapterHandler();
				//开始循环数据
				ReceiveRunable rr = new ReceiveRunable(rh,ph,ah);
				Thread rt = new Thread(rr);
				rt.start();
				DataHelp.WriteLog("远程蓝牙设备接收",tvlog);
				Toast toast = Toast.makeText(PortFactActivity.this, datamsg.getMsgcontent().toString(), Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		}
	}
	private class ConnectBlueRunable implements Runnable{
		private ConnectBlueHandler h;
		public ConnectBlueRunable(ConnectBlueHandler h){
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
		@SuppressLint("NewApi") 
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
					if(clientsocket!=null){
						if(clientsocket.isConnected()){
							clientsocket.close();
						}
					}
				} catch (IOException e) {
					datamsg.setMsgcontent(datamsg.getMsgcontent() + "/"+e.getMessage());
				}
				msg.obj = datamsg;
			}
			h.sendMessage(msg);
		}
	}
	/*接收数据
	陈巨传
	20160306*/
	private class ReceiveRunable implements Runnable{
		private ReceiveHandler rh;
		private ProgressHandler ph;
		private AdapterHandler ah;
		public ReceiveRunable(ReceiveHandler rh,ProgressHandler ph, AdapterHandler ah){
			this.rh = rh;
			this.ph = ph;
			this.ah = ah;
			try {
				streamin = clientsocket.getInputStream();
				streamout = clientsocket.getOutputStream();
			} catch (IOException e) {
				Message msg = new Message();
				DataMsg datamsg = new DataMsg();
				datamsg.setMsgcode("-1");
				datamsg.setMsgcontent(e.getMessage());
				msg.obj = datamsg;
				rh.sendMessage(msg);
			}
		}
		private double runmax=0;
		private int runcount = 0;
		@SuppressLint("NewApi") 
		@Override
		public void run() {
	        while (btnrun.getText().toString().equals("停止运行")) {
	        	DataMsg datamsg = new DataMsg();
	        	try {	
		        	if(!_IsRun){
		        		continue;
		        	}
		        	//显示进度条
		        	Message phm = new Message();
		        	ph.sendMessage(phm);
		        	//如果为null，停止
	            	if(clientsocket==null){
	            		_IsRun =false;
	            	}
	            	//如果关闭，停止
	            	if(!clientsocket.isConnected()){
	            		_IsRun =false;
	            	}
            		byte[] buffer = new byte[1024]; 
	            	int bytes = streamin.read(buffer);
	            	//如果为空，进入下一次
	            	if(bytes<=0){
	            		try {
	     					Thread.sleep(200);
	     				} catch (InterruptedException e) {
	     					e.printStackTrace();
	     				}
	            		continue;
	            	}
                    StringBuilder sball = new StringBuilder();
                    for (int i = 0; i < 12; i++) {
                        sball.append(DataHelp.Bytes2HexString(buffer[i]));
                    }
                    //确定是否包含AA，并且AA后包含10个字符
                    if(sball.toString().equals("AA0000000000000000000000")){
                    	try {
         					Thread.sleep(200);
         				} catch (InterruptedException e) {
         					e.printStackTrace();
         				}
                    	continue;
                    }
                    if(sball.indexOf("AA")==-1){
                    	try {
         					Thread.sleep(200);
         				} catch (InterruptedException e) {
         					e.printStackTrace();
         				}
                    	continue;
                    }
                    int start = sball.indexOf("AA")+2;
                    int end = start+10;
                    if(end>24){
                    	end=24;
                    }
                    String str = sball.toString().substring(start,end);
                    if(str.length()<10){
                    	try {
         					Thread.sleep(200);
         				} catch (InterruptedException e) {
         					e.printStackTrace();
         				}
                    	continue;
                    }
                    int index = start/2;
                    List<String> list = new ArrayList<String>();
                    StringBuilder sb = new StringBuilder();
                    for (int i = index; i < index+5; i++) {
                        list.add(DataHelp.Bytes2HexString(buffer[i]));
                        sb.append(DataHelp.Bytes2HexString(buffer[i]));
                    }
                   	//格式数据值
                   	StringBuilder sbvalue = new StringBuilder();
                   	for (int i = 0; i <= list.size() - 1; i++) {
                   		String v = DataHelp.Hex2String(list.get(i));
                   		if(!v.equals("")){
                   			sbvalue.append(DataHelp.Hex2String(list.get(i)));
                   		}
                    }
                   	int value =0;
               		if(!sbvalue.toString().equals("")){
               			value =  Integer.parseInt(sbvalue.toString());
               		}
                    //获取参考数值
               		double top = Double.parseDouble(modelinv.getLentop());
                    double bom = Double.parseDouble(modelinv.getLenbom());
                    double dle =Double.parseDouble(String.valueOf(value)) / Double.parseDouble("100");
                    //取值是比较2个0之间的最大值
                    if(runcount==0){
                    	runmax = 0;
                    }
               		if((dle==Double.parseDouble("0") && runcount==0) || (dle==Double.parseDouble("0") && runcount==1 && runmax>Double.parseDouble("0"))){
               			runcount++;
               			System.out.println("dle"+String.valueOf(dle));
               			System.out.println("runcount"+String.valueOf(runcount));
               			System.out.println("runmax"+String.valueOf(runmax));
               		}
               		if(dle>runmax){
               			runmax = dle;
                   	}
                   	if(dle==Double.parseDouble("0") && runcount!=2){
                   		try {
         					Thread.sleep(200);
         				} catch (InterruptedException e) {
         					e.printStackTrace();
         				}
                   		continue;
                   	}
                    //显示合格的接收数据:只显示大于0的
                   	Message msgrec = new Message();
    				datamsg.setMsgcode("");
    				datamsg.setMsgcontent("接收信息:"+sball.toString()+"("+String.valueOf(dle)+")");
    				msgrec.obj = datamsg;
					rh.sendMessage(msgrec);
					//根据数值逐渐报警
                    if(dle>=bom-1){
                    	int secontd = 200;
                    	int ibeep = ibeep1;
                    	if(dle<top && dle>=bom){
                    		ibeep = ibeep2;
                    		secontd = 220;
                    	} else if(dle>=top){
                    		ibeep = ibeep3;
                    		secontd = 400;
                    	}
                    	mysoundPool.play(ibeep,1, 1, 1, 0, 1);
                        try {
         					Thread.sleep(secontd);
         				} catch (InterruptedException e) {
         					e.printStackTrace();
         				}
                   		continue;
                    }
                   	//2次0出现标识确认
					_IsRun = false;
                   	if(runcount==2){
                   		runcount=0;
                   		int num=GetMaxNum()+1; 
                       	ModelOrderInvFact model = new ModelOrderInvFact();
                       	model.setFactno(String.valueOf(num));
                       	model.setOperstrength(String.valueOf(runmax));
                   		//数据偏高处理
                   		if(runmax>top){
                            for (int i = 0; i < String.valueOf(num).length(); i++) {
                                mysoundPool.play(GetSoundID(String.valueOf(num).substring(i, 1)),1, 1, 1, 0, 1);
                            }
                            try {
                				Thread.sleep(1000);
                			} catch (InterruptedException e) {
                				e.printStackTrace();
                			}
                            mysoundPool.play(itop,1, 1, 1, 0, 1);
                            model.setFactstate("1");
                            model.setFactno("0");
                            num = num - 1;
                            try {
            					Thread.sleep(5000);
            				} catch (InterruptedException e) {
            					e.printStackTrace();
            				}
                      	} else if (runmax < bom) {//力矩偏低处理
                      		for (int i = 0; i < String.valueOf(num).length(); i++) {
                                mysoundPool.play(GetSoundID(String.valueOf(num).substring(i, 1)),1, 1, 1, 0, 1);
                            }
                      		try {
                				Thread.sleep(1000);
                			} catch (InterruptedException e) {
                				e.printStackTrace();
                			}
                            mysoundPool.play(ibom,1, 1, 1, 0, 1);
                            model.setFactstate("2");
                            model.setFactno("0");
                            num = num - 1;
                            try {
             					Thread.sleep(5000);
             				} catch (InterruptedException e) {
             					e.printStackTrace();
             				}
                        } else{
                        	model.setFactstate("3");
                        }
                        //保存信息
                        EntityError entity = Save(model,ah);
	                    if (!entity.getErrcode().equals("")) {
	                    	mysoundPool.play(ierrdata,1, 1, 1, 0, 1);
                            try {
             					Thread.sleep(5000);
             				} catch (InterruptedException e) {
             					e.printStackTrace();
             				}
	                    } else{
	                    	if(model.getFactstate().equals("3")){
		                        Message msgsave = new Message();
		                        if (num >= Integer.parseInt(modelinv.getQty())) {
		                        	mysoundPool.play(iselect,1, 1, 1, 0, 1);
		                            try {
		             					Thread.sleep(5000);
		             				} catch (InterruptedException e) {
		             					e.printStackTrace();
		             				}
		                            datamsg.setMsgcode("-20");
		            				datamsg.setMsgcontent("结束本任务");
		            				msgsave.obj = datamsg;
		            				rh.sendMessage(msgsave);
		            				break;
		                        } else {
		                        	for (int i = 0; i < String.valueOf(num).length(); i++) {
		                                mysoundPool.play(GetSoundID(String.valueOf(num).substring(i, 1)),1, 1, 1, 0, 1);
		                            }
		                    		try {
		                				Thread.sleep(1000);
		                			} catch (InterruptedException e) {
		                				e.printStackTrace();
		                			}
		                        	mysoundPool.play(isave,1, 1, 1, 0, 1);
		                            try {
		             					Thread.sleep(5000);
		             				} catch (InterruptedException e) {
		             					e.printStackTrace();
		             				}
		                        }
	                    	}
                        }
                   	}
                   	_IsRun = true;
	            } catch (IOException e) {
	            	//显示文本异常
	            	Message msgtxt = new Message();
	            	datamsg.setMsgcode("-10");
					datamsg.setMsgcontent(e.getMessage());
					msgtxt.obj = datamsg;
					rh.sendMessage(msgtxt);
					//语音提示异常
					mysoundPool.play(ierrdata,1, 1, 1, 0, 1);
					try {
     					Thread.sleep(5000);
     				} catch (InterruptedException e1) {
     					e1.printStackTrace();
     				}
	            }
	        }
		}
	}
	/*接收过程问题代理
	陈巨传
	20160306*/
	private class ReceiveHandler extends Handler{
		@SuppressLint("NewApi") 
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			DataMsg datamsg = (DataMsg)msg.obj;
			DataHelp.WriteLog(datamsg.getMsgcontent().toString(),tvlog);
			
			if(datamsg.getMsgcode().equals("-1")){
				Toast toast = Toast.makeText(PortFactActivity.this, datamsg.getMsgcontent().toString(), Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			} else if(datamsg.getMsgcode().equals("-10")){//运行过程中，因异常关闭蓝牙
				bar.setProgress(0);
				btnrun.setText("开始运行");
				if(clientsocket!=null){
					if(clientsocket.isConnected()){
						try {
							clientsocket.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					clientsocket=null;
				}
				Toast toast = Toast.makeText(PortFactActivity.this, "已停止远程蓝牙设备", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			} else if(datamsg.getMsgcode().equals("-20")){//运行过程中，本条任务工作完毕，停止蓝牙，关闭窗体
				bar.setProgress(0);
				btnrun.setText("开始运行");
				if(clientsocket!=null){
					if(clientsocket.isConnected()){
						try {
							clientsocket.close();
						} catch (IOException e) {
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
				mysoundPool.release();
				mysoundPool=null;
        		Intent intent = new Intent();
    			intent.putExtra("result", String.valueOf(GetMaxNum()));//返回当前序号
    			PortFactActivity.this.setResult(1, intent);
    			PortFactActivity.this.finish();
        	} 
		}
	}
	/*连接进度显示代理
	陈巨传
	20160316*/
	private class ProgressHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			bar.setMax(100);
			bar.setProgress(0);
            for (int i = 0; i <= 100; i++) {
                bar.setProgress(i);
            }
		}
	}
	/*绑定列表代理
	陈巨传
	20160316*/
	private class AdapterHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			List<ModelOrderInvFact> list = (List<ModelOrderInvFact>)msg.obj;
			myPortFactAdapter = new PortFactAdapter(list,PortFactActivity.this);
			lvlist.setAdapter(myPortFactAdapter);
		}
	}
	/*退出提示
	陈巨传
	2016.01.22*/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {//退出提示
		if(keyCode==KeyEvent.KEYCODE_BACK){
			FinishActivity();
			return true;
		} else if (KeyEvent.KEYCODE_HEADSETHOOK == keyCode) {//按下了耳机键  
            if (event.getRepeatCount() == 0) {
            	if(ckreceive.isChecked()){
            		ckreceive.setChecked(false);
            	} else{
            		ckreceive.setChecked(true);
            	}
            } 
        }  
		return super.onKeyDown(keyCode, event);
	}
	/*保存数据
	陈巨传
	20160316*/
	private EntityError Save(ModelOrderInvFact model,AdapterHandler ah){
        //加载力矩
        model.setBillids(modelinv.getBillids());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        model.setOperdate(df.format(new Date()));
        listfact.add(0,model);
        Message m = new Message();
        m.obj = listfact;
        ah.sendMessage(m);
		//保存数据
		StringBuilder sbs = new StringBuilder(); 
        if (sbs.toString() != "") {
        	sbs.append("|!");
        }
        sbs.append(model.getFactno());
        sbs.append("|@" + model.getOperstrength());
        sbs.append("|@" + model.getOperdate());
       
        EntityError entity = new EntityError();
        if (!PublicOption.getIsonline()) {
        	try{
				InvFactDao _invfactdao = new InvFactDao(SQLiteOpenDB.OpenDatabase(PortFactActivity.this));
				_invfactdao.Add(model);
        	} catch(Exception exp){
        		entity.setErrcode("-1");
        		entity.setErrmsg(exp.getMessage());
        	}
        } else {
        	APIUp apiup = new APIUp();
        	Hashtable<String, String> dic = new Hashtable<String, String>();
            dic.put("billid", modelinv.getBillid());
            dic.put("billids", modelinv.getBillids());
            dic.put("detailstr", sbs.toString());
            dic.put("safecode", PublicOption.getSafecode());
        	entity = apiup.Save(dic);
        }
		return entity;
	}
	/*共用退出：点返回按钮和退出键调用
	陈巨传
	2016.01.22*/
	private void FinishActivity(){
		if(btnrun.getText().toString().equals("开始运行")){
			Intent intent = new Intent();
			intent.putExtra("result", String.valueOf(GetMaxNum()));
			PortFactActivity.this.setResult(1, intent);
			PortFactActivity.this.finish();
		} else{
			Toast toast = Toast.makeText(PortFactActivity.this, "请先停止运行！", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}
	/*获取当前序号
	陈巨传
	20160316*/
	private int GetMaxNum(){
		int num=0;
		if(listfact.size()>0){
			for(ModelOrderInvFact m: listfact){
				if(!m.getFactno().equals("0")){
					num = Integer.parseInt(m.getFactno());
					break;
				}
			}
		}
		return num;
	}
	/*根据资源名称获取资源ID
    	陈巨传
    20160316*/
    private int GetID(String str){
    	Resources res = getResources();
    	String packageName = getPackageName();
    	int ResId = res.getIdentifier(str, "raw", packageName);
    	return ResId;
    }
    private int GetSoundID(String i){
    	if(i.equals("1")){
    		return i1;
    	} else if(i.equals("2")){
    		return i2;
    	} else if(i.equals("3")){
    		return i3;
    	} else if(i.equals("4")){
    		return i4;
    	} else if(i.equals("5")){
    		return i5;
    	} else if(i.equals("6")){
    		return i6;
    	} else if(i.equals("7")){
    		return i7;
    	} else if(i.equals("8")){
    		return i8;
    	} else if(i.equals("9")){
    		return i9;
    	}
    	return i0;
    }
}
