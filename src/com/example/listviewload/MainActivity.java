package com.example.listviewload;

import java.util.LinkedList;
import java.util.List;

import com.bean.ItemBean;
import com.example.baseadapter.uitls.CommonAdapter;
import com.example.baseadapter.uitls.ViewHolder;
import com.example.listviewload.LoadListView.ILoadListener;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
/**
 * listViewˢ�������ʵ��
 * @author jiangqianghua
 *
 */
public class MainActivity extends Activity implements ILoadListener{

	private LoadListView mListView ;
	
	private List<ItemBean> mDatas = new LinkedList<ItemBean>() ;
	private CommonAdapter<ItemBean> mAdapter ; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mListView = (LoadListView) findViewById(R.id.lv_list);
		mListView.setILoadListener(this);
		initData();
		
		showListView();
	}
	
	private void initData()
	{
		for(int i = 0 ; i < 100 ; i++)
		{
			ItemBean ib = new ItemBean() ; 
			ib.setName("item "+i);
			mDatas.add(ib);
		}
	}
	
	private void showListView()
	{
		if(mAdapter == null )
		{
			mAdapter = new CommonAdapter<ItemBean>(MainActivity.this , mDatas,R.layout.item) {


				@Override
				public void convert(ViewHolder holder, ItemBean t) {
					// TODO Auto-generated method stub
					holder.setText(R.id.tv_texyView, t.getName());
				}
			};
			mListView.setAdapter(mAdapter);
		}
		else
		{
			mAdapter.onDataChange(mDatas);
		}

		
	}
	
	
	
	/**
	 * ���ظ���
	 */
	private void getLoadData(){
		int index = mDatas.size()  ; 
		for(int i = index ; i < index + 20 ; i++)
		{
			ItemBean ib = new ItemBean() ; 
			ib.setName("item "+i);
			mDatas.add(ib);
		}
	}
	@Override
	public void onload() {
		// TODO Auto-generated method stub
		// ����ʱ����
		Handler hander = new Handler();
		hander.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				// ��ȡ��������
				getLoadData();
				// ֪ͨlistView���½���
				showListView();
				
				mListView.loadComplete();
			}
		}, 2000);
		
	}

	/**
	 * ˢ������
	 */
	private void setRelashData(){
		mDatas.clear();
		for(int i = 0 ; i < 20 ; i++)
		{
			ItemBean ib = new ItemBean() ; 
			ib.setName("item "+i);
			mDatas.add(ib);
		}
	}
	@Override
	public void onReflash() {
		// TODO Auto-generated method stub
		
		Handler hander = new Handler();
		hander.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				// ��ȡ�����µ�����
				setRelashData();
				//֪ͨ������ʾ����
				// ֪ͨlistView���½���
				showListView();
				// ֪ͨ��ʾ���
				mListView.reflashComplete();
			}
		}, 2000);

	}
}
