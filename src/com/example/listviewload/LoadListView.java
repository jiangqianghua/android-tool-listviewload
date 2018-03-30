package com.example.listviewload;

import java.sql.Date;
import java.text.SimpleDateFormat;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoadListView extends ListView  implements OnScrollListener{
	

	private View footer ;
	private View header ; 
	
	private int totalItemCount ; // ������
	private int lastVisbleItem ;// ���һ���ɼ���item
	private int firstVisableItem ; // ��ǰ��һ���ɼ���λ��
	private boolean isloading ;// ���ڼ���
	
	private int headerHeight ; // �����ļ��ĸ߶�
	
	private ILoadListener mLoadListener ;
	private boolean isRemark ; //��ǣ���ǰ����listiew��˰��µ�
	private int startY ; // ���¿�ʼ��yֵ
	
	private int state ; 
	final int NONE = 0 ; //����״̬
	final int PULL = 1 ; //��������ˢ�µ�״̬
	final int RELESE = 2; //��ʾ�ͷ�״̬
	final int REFLASHING = 3;  // ˢ��״̬
	
	private int scrollState ; // ��ǰ����״̬
	public LoadListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}
	
	public LoadListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public LoadListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		initView(context);
	}
	
	public LoadListView(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleRes);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	/**
	 * ��ӵײ�����footer
	 * @param context
	 */
	private void initView(Context context)
	{
		LayoutInflater inflater	 = LayoutInflater.from(context);
		footer = inflater.inflate(R.layout.footer, null);
		footer.findViewById(R.id.load_layout).setVisibility(View.GONE);
		this.addFooterView(footer);
		this.setOnScrollListener(this);
		
		header = inflater.inflate(R.layout.header, null);

		measureView(header);
		headerHeight = header.getMeasuredHeight();
		Log.e("err", "headerHeight "+headerHeight);
		topPadding(-headerHeight);
		this.addHeaderView(header);

	}
	
	private void topPadding(int topPadding)
	{
		header.setPadding(header.getPaddingLeft(), topPadding, header.getPaddingRight(), header.getPaddingBottom());
		header.invalidate(); 
	}

	/**
	 * ֪ͨ������ռ���ط�
	 * @param view
	 */
	private void measureView(View view){
		ViewGroup.LayoutParams p = view.getLayoutParams();
		if(p == null)
		{
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			
		}
		
		int width = ViewGroup.getChildMeasureSpec(0, 0, p.width);
		int height ; 
		int tempHeight = p.height ;
		if(tempHeight > 0 )
		{
			height = MeasureSpec.makeMeasureSpec(tempHeight, MeasureSpec.EXACTLY);
			
		}
		else
		{
			height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		view.measure(width, height);
	}
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

		if(totalItemCount == lastVisbleItem && scrollState== SCROLL_STATE_IDLE)
		{
			if(!isloading)
			{
				isloading = true ;
				// ���ظ�������
				footer.findViewById(R.id.load_layout).setVisibility(View.VISIBLE);
				mLoadListener.onload(); 
			}
		}
		this.scrollState = scrollState ;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		lastVisbleItem = firstVisibleItem + visibleItemCount;
		this.firstVisableItem = firstVisibleItem ;
		this.totalItemCount = totalItemCount ;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if(firstVisableItem == 0)
			{
				isRemark = true ;
				startY = (int) ev.getY() ;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			onMove(ev);		
			break;
		case MotionEvent.ACTION_UP:
			if(state == RELESE)
			{
				// ����ˢ������
				state = REFLASHING ;// ״̬ʱ����ˢ������
				// ������������
				mLoadListener.onReflash();
			}
			else if(state == PULL)
			{
				state = NONE ; 
				isRemark = false ;
			}
			break;
//		case MotionEvent.ACTION_DOWN:
//	
//	break;
		default:
			break;
		}
		reflashViewByState();
		return super.onTouchEvent(ev);
	}
	/*
	 * �ж��ƶ������еĲ���
	 */
	private void onMove(MotionEvent ev)
	{
		if(!isRemark)
			return ;
		int tempY = (int) ev.getY() ;
		int space = tempY - startY;
		int topPadding = space - headerHeight ;
		switch (state) {
		case NONE:
			if(space > 0 )
			{
				state = PULL;
			}
			break;
		case PULL:
			topPadding(topPadding);
			if(space > headerHeight + 30 &&
					scrollState == SCROLL_STATE_TOUCH_SCROLL)	 // ���ڹ���
			{
				state = RELESE ; // ��ʾ�ɿ�ˢ��ը״̬
			}
			break;
		case RELESE:
			topPadding(topPadding);
			if(space < headerHeight + 30 )	 // ���ڹ���
			{
				state = PULL ; // ��ʾ����״̬
			}
			else if(space <= 0 )
			{
				state = NONE ; 
				isRemark = false ;
			}
			break;
		case REFLASHING:
			
			break;
		default:
			break;
		}
//		
//		final int NONE = 0 ; //����״̬
//		final int PULL = 1 ; //��������ˢ�µ�״̬
//		final int RELESE = 2; //��ʾ�ͷ�״̬
//		final int REFLASHING = 3;  // ˢ��״̬
	}
	/**
	 * �������
	 */
	public void loadComplete(){
		isloading = false ; 
		footer.findViewById(R.id.load_layout).setVisibility(View.GONE);
		
	}
	/**
	 * ���ýӿ�
	 * @param loadListener
	 */
	public void setILoadListener(ILoadListener loadListener)
	{
		mLoadListener = loadListener ;
	}
	/**
	 * ���ظ������ݻص��ӿ�
	 * @author jiangqianghua
	 *
	 */
	public interface ILoadListener{
		
		public void onload();
		
		public void onReflash();
	}
	/**
	 * �������ݼ������
	 */
	public void reflashComplete()
	{
		state = NONE;
		isRemark = false ;
		reflashViewByState(); 
		TextView lastUpdate_time = (TextView) header.findViewById(R.id.lastUpdate_time);
		SimpleDateFormat format = new SimpleDateFormat("yyyy��MM��dd�� hh:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		String time = format.format(date);
		lastUpdate_time.setText(time);
	}
	/**
	 * ���ݵ�ǰ״̬ˢ�½���
	 */
	private void reflashViewByState()
	{
		TextView tip = (TextView) header.findViewById(R.id.tip);
		ImageView arrow = (ImageView) header.findViewById(R.id.arrow);
		ProgressBar progress = (ProgressBar) header.findViewById(R.id.progress);
		RotateAnimation anim = new RotateAnimation(0, 180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		anim.setDuration(500);
		anim.setFillAfter(true);
		RotateAnimation anim1 = new RotateAnimation(180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		anim1.setDuration(500);
		anim1.setFillAfter(true);
		switch (state) {
		case NONE:
			topPadding(-headerHeight);
			arrow.clearAnimation(); 
			break;
		case PULL:
			arrow.setVisibility(View.VISIBLE);
			progress.setVisibility(View.GONE);
			
			tip.setText("��������ˢ��");
			arrow.clearAnimation(); 
			arrow.setAnimation(anim1);
			break;
		case RELESE:
			arrow.setVisibility(View.VISIBLE);
			progress.setVisibility(View.GONE);
			
			tip.setText("�ɿ�����ˢ��");
			
			arrow.clearAnimation(); 
			arrow.setAnimation(anim);
			break;
		case REFLASHING:
			topPadding(headerHeight); // ����ˢ�����ö������ֵĸ߶�
			arrow.setVisibility(View.GONE);
			progress.setVisibility(View.VISIBLE);
			
			tip.setText("����ˢ��");
			arrow.clearAnimation(); 
			break;
		default:
			break;
		}
	}
}
