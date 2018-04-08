package com.qdzq.port;
import java.io.FileNotFoundException;
import java.io.IOException;
import com.qdzq.appzqwt.R;
import com.qdzq.comm.DataCache;
import com.qdzq.comm.PublicBitmap;
import com.qdzq.comm.PublicOption;
import com.qdzq.control.ControlZoomImageView;
import com.qdzq.entity.ModelOrderInvExtends;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;


public class PortEditBitmapActivity extends Activity {
	private ControlZoomImageView zoomImg;
	private ModelOrderInvExtends model;
	private ProgressDialog pd;
	private Bitmap bm=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.activity_porteditbitmap);
		
		zoomImg = (ControlZoomImageView)findViewById(R.id.imgview);
        
		Intent i = this.getIntent(); 
		model=(ModelOrderInvExtends)i.getSerializableExtra("model");
		
		if(PublicOption.getIsonline()){
			zoomImg.setImage(PublicBitmap.getBitmap());
			/*pd = new ProgressDialog(PortEditBitmapActivity.this);
	        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        pd.setTitle("图片加载");
	        pd.setMessage("图片正在加载中......");
	        pd.show();
	        HandlerList handler = new HandlerList();
			RunnableList runnable = new RunnableList(handler);
			new Thread(runnable).start();*/
		} else{
			String fileName = model.getImage2();
			String[] s = fileName.split("/");
			try {
				bm = DataCache.getLoacalBitmap(s[2]);
				zoomImg.setImage(bm);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/*加载图片
	陈巨传
	2016.1.19*/
	protected class HandlerList extends Handler{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			zoomImg.setImage(bm);
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
			try {
				bm = DataCache.GetUrlBitmap(PublicOption.getUrl() + model.getImage2());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Message msg = new Message();
	        h.sendMessage(msg);//Bitmap无法赋值传递
		}
	}
}
