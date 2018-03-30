package com.example.baseadapter.uitls;

import java.util.List;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public abstract class CommonAdapter<T> extends BaseAdapter {

	protected Context mContext ; 
	protected List<T> mDatas ;
	protected LayoutInflater mInflater ;
	private int layoutId ; 
	public CommonAdapter(Context context , List<T> datas , int layoutId) {
		// TODO Auto-generated constructor stub
		this.mContext = context ;
		mDatas = datas ;
		mInflater = LayoutInflater.from(context);
		this.layoutId = layoutId ;
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDatas.size();
	}

	@Override
	public T getItem(int position) {
		// TODO Auto-generated method stub
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void onDataChange(List<T> datas)
	{
		this.mDatas = datas ; 
		this.notifyDataSetChanged();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, layoutId, position); 
		convert(viewHolder , getItem(position));
		return viewHolder.getmConvertView();
	}
	
	public abstract void convert(ViewHolder holder , T t);

}
