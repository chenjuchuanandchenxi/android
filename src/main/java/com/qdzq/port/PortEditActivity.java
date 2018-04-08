package com.qdzq.port;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.List;
import com.qdzq.api.APIDown;
import com.qdzq.appzqwt.R;
import com.qdzq.comm.DataCache;
import com.qdzq.comm.PublicBitmap;
import com.qdzq.comm.PublicOption;
import com.qdzq.entity.EntityOrderInvExtends;
import com.qdzq.entity.ModelOrderInvExtends;
import com.qdzq.main.DefaultActivity;
import com.qdzq.sqlite.InvDao;
import com.qdzq.sqlite.SQLiteOpenDB;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class PortEditActivity extends Activity {
	private PortEditAdapter myPortEditAdapter;
	private ListView lvlist;
	private ImageView imgback;
	private ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_portedit);
		
		lvlist = (ListView)this.findViewById(R.id.lvlist);
		lvlist.setOnItemClickListener(new lvlist_OnItemClickListener());
		
		imgback =(ImageView)this.findViewById(R.id.imgback);
		imgback.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
			
		});

		pd = new ProgressDialog(PortEditActivity.this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setTitle("任务加载");
        pd.setMessage("正在加载中......");
        pd.show();
        HandlerList handler = new HandlerList();
		RunnableList runnable = new RunnableList(handler);
		new Thread(runnable).start();
	}
	/*加载列表
	陈巨传
	2016.1.19*/
	protected class HandlerList extends Handler{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			EntityOrderInvExtends _entity = (EntityOrderInvExtends)msg.obj;
			List<ModelOrderInvExtends> _list = _entity.getList();
			myPortEditAdapter = new PortEditAdapter(_list,PortEditActivity.this);
			lvlist.setAdapter(myPortEditAdapter);
			pd.dismiss();
		}
	}
	protected class RunnableList implements Runnable{
		HandlerList h;
		public RunnableList(HandlerList h){
			this.h = h;
		}
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(PublicOption.getIsonline()){
				APIDown _api = new APIDown();
		        Hashtable<String, String> dic = new Hashtable<String, String>();
		        dic.put("userid", PublicOption.getUserid());
	            dic.put("safecode", PublicOption.getSafecode());
	            EntityOrderInvExtends _entity = _api.GetList(dic);
		        Message msg = new Message();
		        msg.obj = _entity;
		        h.sendMessage(msg);
			} else{
				InvDao _invdao = new InvDao(SQLiteOpenDB.OpenDatabase(PortEditActivity.this));
				EntityOrderInvExtends _entity = new EntityOrderInvExtends();
				List<ModelOrderInvExtends> _list = _invdao.getWaitDoList();
				_entity.setList(_list);
		        Message msg = new Message();
		        msg.obj = _entity;
		        h.sendMessage(msg);
			}
		}
	}
	/*选择
	陈巨传
	20160303*/
	private class lvlist_OnItemClickListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			myPortEditAdapter.setSelectItem(arg2);
			myPortEditAdapter.notifyDataSetInvalidated();
			
			model = (ModelOrderInvExtends)myPortEditAdapter.getItem(arg2);
			
			AlertDialog.Builder alertBuilder2 = new AlertDialog.Builder(PortEditActivity.this);
			alertBuilder2.setTitle("备注和图片");
			LinearLayout layout = new LinearLayout(PortEditActivity.this);
			layout.setOrientation(LinearLayout.VERTICAL);
			TextView tv = new TextView(PortEditActivity.this);
			tv.setText(model.getMemo());
			Bitmap bm=null;
			if(PublicOption.getIsonline()){
				bm = model.getInvbitmap();
			} else{
				String fileName = model.getImage2();
				if(fileName!=null){
					if(!fileName.equals("")){
						String[] s = fileName.split("/");
						try {
							bm = DataCache.getLoacalBitmap(s[2]);
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			ImageView img = new ImageView(PortEditActivity.this);
			img.setImageBitmap(bm);
			layout.addView(tv);
			layout.addView(img);
			alertBuilder2.setView(layout);
			alertBuilder2.setPositiveButton("执行",new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
					if(!mBtAdapter.isEnabled()){
						Toast toast = Toast.makeText(PortEditActivity.this, "蓝牙未打开，请先打开蓝牙！", Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
						return;
					}
					Intent i = new Intent();
					i.setClass(PortEditActivity.this, PortFactActivity.class);
					Bundle bundle = new Bundle();
					model.setInvbitmap(null);//传递中不能包含Bitmap值
					bundle.putSerializable("model", model);
					i.putExtras(bundle);
					PortEditActivity.this.startActivityForResult(i,GREQUEST_CODE);
				}
			});
			alertBuilder2.setNeutralButton("放大", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					String fileName = model.getImage2();
					if(fileName==null || fileName.equals("")){
						Toast toast = Toast.makeText(PortEditActivity.this, "没有图片可以放大！", Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();
						return;
					}
					Intent i = new Intent();
					i.setClass(PortEditActivity.this, PortEditBitmapActivity.class);
					Bundle bundle = new Bundle();
					PublicBitmap.setBitmap(model.getInvbitmap());
					model.setInvbitmap(null);//传递中不能包含Bitmap值
					bundle.putSerializable("model", model);
					i.putExtras(bundle);
					PortEditActivity.this.startActivity(i);
				}
			});
			alertBuilder2.setNegativeButton("关闭", null);
			alertBuilder2.setCancelable(false);
			alertBuilder2.create().show();
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==GREQUEST_CODE){
			if(resultCode == 1){
				Bundle bundle = data.getExtras();
				String maxnum = bundle.getString("result");
				if(model.getQty().equals(maxnum)){
					myPortEditAdapter.Remove(myPortEditAdapter.getSelectItem());
					lvlist.invalidate();
				}
				return;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	private final static int GREQUEST_CODE = 1;
	private ModelOrderInvExtends model=null;
}
