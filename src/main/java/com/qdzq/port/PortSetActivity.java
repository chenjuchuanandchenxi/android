package com.qdzq.port;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import com.qdzq.appzqwt.R;
import com.qdzq.comm.PublicOption;
import com.qdzq.entity.ModelPortItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

public class PortSetActivity extends Activity {
	private BluetoothAdapter mBtAdapter;
	
	private ImageView imgback;
	
	private PortSetAdapter1 PortSetAdapterPaired;
	private PortSetAdapter2 PortSetAdapterlist;

	private ListView lvportpaired, lvportlist;
	private List<Hashtable<String, String>> listPairedDevices = new ArrayList<Hashtable<String,String>>();//已配对比变量
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_portset);

		imgback =(ImageView)this.findViewById(R.id.imgback);
		imgback.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
			
		});
		
		lvportpaired = (ListView)this.findViewById(R.id.lvportpaired);
		lvportpaired.setOnCreateContextMenuListener(new lvportpaired_OnCreateContextMenuListener());
		lvportpaired.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				PortSetAdapterPaired.setSelectItem(arg2);  
				PortSetAdapterPaired.notifyDataSetInvalidated();
			}
		});
		
		lvportlist = (ListView)this.findViewById(R.id.lvportlist);
		lvportlist.setOnCreateContextMenuListener(new lvportlist_OnCreateContextMenuListener());
		lvportlist.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				PortSetAdapterlist.setSelectItem(arg2);  
				PortSetAdapterlist.notifyDataSetInvalidated();
			}
		});
		
		//手机本身的蓝牙
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		//已配对搜索
		Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();//已配对成功的远程蓝牙
		if (pairedDevices.size() > 0){
			for (BluetoothDevice device : pairedDevices){
				Hashtable<String,String> tablePairedDevices = new Hashtable<String,String>();
				tablePairedDevices.put("portname", device.getName());
				tablePairedDevices.put("portaddress", device.getAddress());
				listPairedDevices.add(tablePairedDevices);
			}
		}
		PortSetAdapterPaired = new PortSetAdapter1(listPairedDevices,PortSetActivity.this);
		lvportpaired.setAdapter(PortSetAdapterPaired);
		//我的工作蓝牙
		PortSetAdapterlist = new PortSetAdapter2(PublicOption.getListdevices(),PortSetActivity.this);
		lvportlist.setAdapter(PortSetAdapterlist);
	}
	/*长按弹出下拉按钮
	陈巨传
	20160303*/
	private class lvportpaired_OnCreateContextMenuListener implements OnCreateContextMenuListener{
		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			// TODO Auto-generated method stub
			menu.setHeaderTitle("配对-工作");//标题
			menu.add(0, 0, 0, "增加蓝牙");//下拉菜单
			menu.add(0, 1, 0, "取消返回");//下拉菜单
		}
	}
	/*删除我的蓝牙
	陈巨传
	20130613*/
	private class lvportlist_OnCreateContextMenuListener implements OnCreateContextMenuListener{
		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			// TODO Auto-generated method stub
			menu.setHeaderTitle("工作蓝牙");//标题
			menu.add(0, 2, 0, "删除蓝牙");//下拉菜单
			menu.add(0, 1, 0, "取消返回");//下拉菜单
		}
	}
	/*所有菜单事件
	陈巨传
	20130613*/
	@Override
	public boolean onContextItemSelected(MenuItem item){//属于Activity基本，能够相应本页面的菜单事件
		ContextMenuInfo info = item.getMenuInfo();
		AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterContextMenuInfo)info;
		int position = contextMenuInfo.position;
		
		if(item.getItemId()==0){
			PortSetAdapterPaired.setSelectItem(position);  
			PortSetAdapterPaired.notifyDataSetInvalidated();
			
			Hashtable<String, String> table = listPairedDevices.get(position);
			String portname = table.get("portname");
			String protaddress = table.get("portaddress");
			//校验是否已经存在
			for(int i=0;i<PublicOption.getListdevices().size();i++){
				ModelPortItem m = PublicOption.getListdevices().get(i);
				if(m.getPortaddress().equals(protaddress)){
					AlertDialog.Builder alertBuilder1 = new AlertDialog.Builder(PortSetActivity.this);
					alertBuilder1.setIcon(R.drawable.ico);
					alertBuilder1.setTitle("信息提示");
					alertBuilder1.setMessage("当前蓝牙已经保存，请勿重复保存！");
					alertBuilder1.setPositiveButton("确定",null);
					alertBuilder1.setCancelable(false);
					alertBuilder1.create().show();
					return super.onContextItemSelected(item); 
				}
			}
			//弹出对话录入助记码，进行保存参数
			editText = new EditText(PortSetActivity.this);
			AlertDialog.Builder alertBuilder2 = new AlertDialog.Builder(PortSetActivity.this);
			alertBuilder2.setIcon(R.drawable.ico);
			alertBuilder2.setTitle("信息提示");
			alertBuilder2.setMessage("请输入蓝牙的助记码");
			alertBuilder2.setView(editText);
			alertBuilder2.setPositiveButton("确定",new alertBuilder_OnClickListener(portname,protaddress));
			alertBuilder2.setNegativeButton("取消", null);
			alertBuilder2.setCancelable(false);
			alertBuilder2.create().show();
		} else if(item.getItemId()==2){
			PortSetAdapterlist.setSelectItem(position);  
			PortSetAdapterlist.notifyDataSetInvalidated();
			//删除工作蓝牙
			PublicOption.getListdevices().remove(position);
			SaveSharedPreferences();
		}
		return super.onContextItemSelected(item); 
	}
	EditText editText;
	protected class alertBuilder_OnClickListener implements DialogInterface.OnClickListener{
		private String portname;
		private String protaddress;
		public alertBuilder_OnClickListener(String portname,String protaddress){
			this.portname = portname;
			this.protaddress = protaddress;
		}
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			String portaddname = editText.getText().toString();
			if(portaddname.equals("")){
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(PortSetActivity.this);
	    		alertBuilder.setIcon(R.drawable.ico);
	    		alertBuilder.setTitle("信息提示");
	    		alertBuilder.setMessage("助记码必须输入！");
	    		alertBuilder.setPositiveButton("确定", null);
	    		alertBuilder.setCancelable(false);
	    		alertBuilder.create().show();
	    		return;
			}
			
			List<ModelPortItem> list = PublicOption.getListdevices();
			ModelPortItem model = new ModelPortItem();
			model.setPortname(portname);
			model.setPortaddress(protaddress);
			model.setPortaddname(portaddname);
			list.add(model);
			PublicOption.setListdevices(list);
			
			SaveSharedPreferences();
		}
	}
	private void SaveSharedPreferences(){
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<PublicOption.getListdevices().size();i++){
			ModelPortItem m = PublicOption.getListdevices().get(i);
			if(sb.toString().equals("")){
				sb.append(m.getPortname() + "|"+m.getPortaddress() + "|"+m.getPortaddname());
			} else{
				sb.append(";"+m.getPortname() + "|"+m.getPortaddress() + "|"+m.getPortaddname());
			}
		}
		
		SharedPreferences sp = PortSetActivity.this.getSharedPreferences("userinfo",Context.MODE_PRIVATE);//私有，Context常量
		Editor editor = sp.edit();
		editor.putString("listdevices", sb.toString());
		editor.commit();
		
		//重新加载数据
		PortSetAdapterPaired.notifyDataSetInvalidated();
	}
	/*类似WinForm中Closed，只要关闭窗口就会调用
	陈巨传
	20160303*/
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// 确保我们没有发现了
		if (mBtAdapter != null){
			mBtAdapter.cancelDiscovery();
		}
	}
}
