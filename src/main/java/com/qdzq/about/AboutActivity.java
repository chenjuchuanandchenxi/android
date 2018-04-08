package com.qdzq.about;

import java.io.File;

import com.qdzq.appzqwt.R;
import com.qdzq.comm.PublicOption;
import com.qdzq.update.UpdateManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AboutActivity extends Activity {
	Button btnversion,btnclear,btndelete;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_about);

		TextView textView = (TextView)findViewById(R.id.txtcontract);   
		textView.setMovementMethod(ScrollingMovementMethod.getInstance());
		
		btnversion = (Button)this.findViewById(R.id.btnversion);
		btnversion.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(!PublicOption.getIsonline()){
					Toast toast = Toast.makeText(AboutActivity.this, "只有联机模式下才可以升级最新版本！", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
		        } else{
					UpdateManager updatemanager = new UpdateManager(AboutActivity.this);
			        updatemanager.Update();
		        }
			}
		});
		btnclear = (Button)this.findViewById(R.id.btnclear);
		btnclear.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				ShowDialog(AboutActivity.this).show();
			}
		});
	}
	private Dialog ShowDialog(Context context){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("清空提示");
		builder.setMessage("确定要清空吗?");
		builder.setIcon(R.drawable.ico);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String alertmsg = "";
				String filepath = "";
				filepath = "/com.qdzqwt.databases";
		        alertmsg = "本地数据清空完毕！";
				File dirFile = new File(Environment.getExternalStorageDirectory().getPath() + filepath);  
				if(dirFile.exists()){   
			        if (dirFile.isDirectory()) {  
			            File[] children = dirFile.listFiles();  
			            for (int i = 0; i < children.length; i++) {  
			            	children[i].delete();
			            }
			        }
		        }
				Toast toast = Toast.makeText(AboutActivity.this, alertmsg, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		return builder.create();
	}

}
