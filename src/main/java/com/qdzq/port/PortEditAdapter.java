package com.qdzq.port;
import java.util.List;

import com.qdzq.appzqwt.R;
import com.qdzq.entity.ModelOrderInvExtends;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PortEditAdapter extends BaseAdapter {
	List<ModelOrderInvExtends> list;
	Context context;
	public PortEditAdapter(List<ModelOrderInvExtends> list,Context context){
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
			convertView = inflater.inflate(R.layout.activity_portedit_item, null);
		}
		if (position == selectItem) {  
			convertView.setBackgroundColor(Color.rgb(156, 205, 239));
        }   
        else {  
            convertView.setBackgroundColor(Color.WHITE);  
        }     
		
		TextView txtbillcode = (TextView)convertView.findViewById(R.id.txtbillcode);
		txtbillcode.setText("单号："+list.get(position).getBillcode());
		TextView txtinvstd = (TextView)convertView.findViewById(R.id.txtinvstd);
		txtinvstd.setText("螺栓："+list.get(position).getInvstd());
		TextView txtposname = (TextView)convertView.findViewById(R.id.txtposname);
		txtposname.setText("位置："+list.get(position).getPosname());
		TextView txtqty = (TextView)convertView.findViewById(R.id.txtqty);
		txtqty.setText("数量："+list.get(position).getQty());
		TextView txtlimitname = (TextView)convertView.findViewById(R.id.txtlimitname);
		txtlimitname.setText("误差："+list.get(position).getLimitname());
		TextView txtstrength = (TextView)convertView.findViewById(R.id.txtstrength);
		txtstrength.setText("标准："+list.get(position).getStrength());

		return convertView;
	}
	//当前选中的行
	public  void setSelectItem(int selectItem) {  
        this.selectItem = selectItem;  
    }  
	public  int getSelectItem() {  
        return selectItem;
    } 
    private int  selectItem=-1;
    //当前选中的行
  	public  void Remove(int selectItem) {  
  		list.remove(selectItem);
  		this.notifyDataSetChanged();
    }  
}
