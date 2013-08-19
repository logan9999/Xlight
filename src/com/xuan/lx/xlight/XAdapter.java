package com.xuan.lx.xlight;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class XAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private int[] color;
	private int[] text;

	public XAdapter(Context context, int[] _color, int[] _text) {
		mInflater = LayoutInflater.from(context);
		color = _color;
		text = _text;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return text.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return text[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			/* 使用自定义的change_color作为Layout */
			convertView = mInflater.inflate(R.layout.change_color, null);
			/* 初始化holder的text */
			holder = new ViewHolder();
			holder.mText = (TextView) convertView.findViewById(R.id.myText);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mText.setText(text[position]);
		holder.mText.setBackgroundResource(color[position]);

		return convertView;
	}

	/* class ViewHolder */
	private class ViewHolder {
		TextView mText;
	}

}
