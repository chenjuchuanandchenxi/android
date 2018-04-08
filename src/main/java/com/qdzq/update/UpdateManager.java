package com.qdzq.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;

import com.qdzq.api.APIVersion;
import com.qdzq.appzqwt.R;
import com.qdzq.comm.PublicOption;
import entity.comm.EntityError;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UpdateManager {
	Context context;
	ModelVersion model;
	//需要修改的白能量
	String finalpackage="com.qdzq.appzqwt";
	String finalpath="DownAppZQWT";//放入扩展槽的目录
	String finalfile="AppZQWT";//APP程序名称
	String finaldown="/down/"+finalfile+".apk";//下载App程序
	ProgressDialog pd;
	//进度对话框
	TextView textview;
	ProgressBar progressbar;
	int curprogress = 0;
	boolean iscancel = false;
	AlertDialog alertprogress;
	//文件保存路径
	String savepath;
	/*1.构造函数*/
	public UpdateManager(Context context){
		this.context = context;
	}
	/*2.是否有新版本，以便进行更新*/
	public void Update(){
		//获取当前版本
		try{
			int version = context.getPackageManager().getPackageInfo(finalpackage, 0).versionCode;
			model = new ModelVersion();
			model.setCurversion(version);
		} catch(Exception exp){
			Log.i("IsUpdate错误：", exp.getMessage());
			exp.printStackTrace();
		}
		//获取最新版本
		pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setTitle("版本检测");
        pd.setMessage("正在检测中......");
        pd.show();
        HandlerVersion handler = new HandlerVersion();
		RunnableVersion runnable = new RunnableVersion(handler);
		new Thread(runnable).start();
	}
	protected class HandlerVersion extends Handler{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			pd.dismiss();
			EntityError _entity = (EntityError)msg.obj;
			if(!_entity.getErrcode().equals("")){
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
	    		alertBuilder.setIcon(R.drawable.ico);
	    		alertBuilder.setTitle("信息提示");
	    		alertBuilder.setMessage("异常信息："+_entity.getErrmsg());
	    		alertBuilder.setPositiveButton("确定", null);
	    		alertBuilder.setCancelable(false);
	    		alertBuilder.create().show();
	    		return;
			}
			model.setVersion(Integer.parseInt(_entity.getErrmsg()));
			model.setUrl(PublicOption.getUrl()+finaldown);
			if(model.getVersion()>model.getCurversion()){
				DownAPK();
			}
		}
	}
	protected class RunnableVersion implements Runnable{
		HandlerVersion h;
		public RunnableVersion(HandlerVersion h){
			this.h = h;
		}
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			// TODO Auto-generated method stub
			APIVersion _api = new APIVersion();
	        Hashtable<String, String> dic = new Hashtable<String, String>();
            dic.put("safecode", PublicOption.getSafecode());
	        EntityError _entity = _api.GetVersion(dic);
	        Message msg = new Message();
	        msg.obj = _entity;
			h.sendMessage(msg);
		}
	}
	/*3.将APK下载到扩展卡目录，采用进度条*/
	public void DownAPK(){
		AlertDialog.Builder builderUpdate = new AlertDialog.Builder(context);
		builderUpdate.setTitle("版本更新提示");
		builderUpdate.setMessage("有新版本，是否更新?");
		builderUpdate.setPositiveButton("现在更新", new OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				ShowProgress();
				
				HandlerlImpl handlerimpl = new HandlerlImpl();
				ThreadDown thread = new ThreadDown(handlerimpl);
				thread.start();
			}
		});
		builderUpdate.setNegativeButton("稍后更新", new OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		AlertDialog alert = builderUpdate.create();
		alert.show();
	}
	/*显示进度条*/
	private void ShowProgress(){
		AlertDialog.Builder builderProgress = new AlertDialog.Builder(context);
		builderProgress.setTitle("下载进度");
		//在对话框中增加进度条视图，采用LayoutInflater加载布局
		LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.activity_progress, null);
        progressbar = (ProgressBar)v.findViewById(R.id.progressBar1);
        textview = (TextView)v.findViewById(R.id.textView1);
        
        builderProgress.setView(v);
        builderProgress.setNegativeButton("取消下载", new OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				iscancel = true;
			}
		});
        alertprogress = builderProgress.create();
        alertprogress.show();
	}
	/*下载handel句柄类和下载类*/
	private class HandlerlImpl extends Handler{
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
				case 1:
					progressbar.setProgress(curprogress);
					textview.setText("下载进度" + String.valueOf(curprogress+1) + "/100");
					break;
				case 2:
					SetUpAPK();
					break;
			}
		}
	}
	private class ThreadDown extends Thread{
		private HandlerlImpl h;
		public ThreadDown(HandlerlImpl h){
			this.h = h;
		}
		@Override
		public void run() {
			//判断SD卡是否存在，并且是否具有读写权限
			boolean issd = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
			if(issd){
				String path = Environment.getExternalStorageDirectory() + "/";
				savepath = path + finalpath;
				try {
					//创建连接
					URL url = new URL(model.getUrl().toString());
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.connect();
                    //获取文件大小
                    int length = conn.getContentLength();
                    //创建输入流
                    InputStream is = conn.getInputStream();
                  //判断文件目录是否存在
                    File file = new File(savepath);
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    //创建输出流
                    File apkFile = new File(savepath, finalfile);
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    //创建缓存
                    int count = 0;
                    byte buf[] = new byte[1024];
                    //写入到文件中
                    do{
                        int numread = is.read(buf);
                        count += numread;
                        //计算进度条位置
                        curprogress = (int) (((float) count / length) * 100);
                        //更新进度
                        h.sendEmptyMessage(1);
                        if (numread <= 0) {//下载完成
                        	h.sendEmptyMessage(2);
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (!iscancel);//点击取消就停止下载
                    fos.close();
                    is.close();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	/*安装文件APK*/
	private void SetUpAPK(){
		//获取下载的文件
		File apkfile = new File(savepath, finalfile);
        if (!apkfile.exists()){
            return;
        }
        //通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        context.startActivity(i);
	}
}
