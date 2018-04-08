package com.qdzq.port;
import java.util.Hashtable;
import java.util.List;

import com.qdzq.appzqwt.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PortSetAdapter1 extends BaseAdapter {
	List<Hashtable<String, String>> list;
	Context context;
	public PortSetAdapter1(List<Hashtable<String, String>> list,Context context){
		this.list = list;
		this.context = context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView ==null){
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//获取本应用的资源
			convertView = inflater.inflate(R.layout.activity_portset_item1, null);
		}
		if (position == selectItem) {  
			convertView.setBackgroundColor(Color.rgb(156, 205, 239));
        }   
        else {  
            convertView.setBackgroundColor(Color.WHITE);  
        }     
		TextView tvportname = (TextView)convertView.findViewById(R.id.tvportname);
		tvportname.setText(list.get(position).get("portname"));
		TextView tvportaddress = (TextView)convertView.findViewById(R.id.tvportaddress);
		tvportaddress.setText(list.get(position).get("portaddress"));
		return convertView;
	}
	//当前选中的行
	public  void setSelectItem(int selectItem) {  
        this.selectItem = selectItem;  
    }  
    private int  selectItem=-1;
}
