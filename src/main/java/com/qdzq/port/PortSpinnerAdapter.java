package com.qdzq.port;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qdzq.appzqwt.R;
import com.qdzq.entity.ModelPortItem;

public class PortSpinnerAdapter extends BaseAdapter {
	List<ModelPortItem> list;
	Context context;
	public PortSpinnerAdapter(List<ModelPortItem> list,Context context){
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
			convertView = inflater.inflate(R.layout.activity_portspinner_item, null);
		}
		ModelPortItem modle = list.get(position);
		TextView tvportname = (TextView)convertView.findViewById(R.id.tvblueaddname);
		tvportname.setText(modle.getPortaddname());
		TextView tvportaddress = (TextView)convertView.findViewById(R.id.tvblueaddress);
		tvportaddress.setText(modle.getPortaddress());
		return convertView;
	}
}
