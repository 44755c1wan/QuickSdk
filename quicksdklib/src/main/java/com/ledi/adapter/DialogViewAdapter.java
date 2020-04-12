package com.ledi.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ledi.util.Util;

public class DialogViewAdapter extends BaseAdapter {
	private Context context;
	private String[] dialogValues;
	private int index;
	// private ViewHolder holder;
	public DialogViewAdapter(Context context, String[] dialogValues,int index) {
		this.context = context;
		this.dialogValues = dialogValues;
		this.index = index;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dialogValues.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dialogValues[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder;
		if (convertView == null) {
			 holder = new Holder();
			
			convertView = View.inflate(parent.getContext(),
					Util.getResID(context, "ledi_dialog_values_item", "layout"), null);
			holder.tv = (TextView)convertView.findViewById(Util.getResID(context, "dialog_item_tv", "id"));
			holder.imgView = (ImageView)convertView.findViewById(Util.getResID(context, "dialog_item_img", "id"));
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
			holder.tv.setText(dialogValues[position]);
			if(position == index){
				holder.imgView.setBackgroundResource(Util.getResID(context, "ledi_dialog_yuan_down", "drawable"));
			}else {
				holder.imgView.setBackgroundResource(Util.getResID(context, "ledi_dialog_yuan_up", "drawable"));
			}
		return convertView;
	}
	class Holder{
		TextView tv;
		ImageView imgView;
	}

}
