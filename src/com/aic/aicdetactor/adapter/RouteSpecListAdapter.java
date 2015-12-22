package com.aic.aicdetactor.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.activity.StationActivity;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.comm.LineType;
import com.aic.aicdetactor.database.LineDao;
import com.aic.aicdetactor.paramsdata.LineListInfoParams;
import com.aic.aicdetactor.util.MLog;
import com.aic.aicdetactor.util.SystemUtil;
import com.aic.aicdetactor.view.GroupViewHolder;

public class RouteSpecListAdapter extends BaseAdapter{
	private Context context;
	private LayoutInflater mInflater;
	private final  String TAG="luotest.RouteSpecListAdapter";
	private myApplication app = null;
	private Activity mActivity=null;
	private List<LineListInfoParams> mLineList=null;
	public RouteSpecListAdapter(Context context,Activity av) {
		// TODO Auto-generated constructor stub
		this.context = context;
		mInflater = LayoutInflater.from(context);
		this.mLineList = new ArrayList<LineListInfoParams>();
		app =(myApplication) av.getApplication();
		mActivity =av;
		initListData();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mLineList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mLineList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView( int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		GroupViewHolder holder =null;
		if(arg1==null){
			holder = new GroupViewHolder();
		
			arg1 = mInflater.inflate(R.layout.route_list_item, null);
			holder.indexText  = (TextView) arg1.findViewById(R.id.index);
			holder.NameText  = (TextView) arg1.findViewById(R.id.pathname);
			holder.DeadLineText  = (TextView) arg1.findViewById(R.id.deadtime);
			holder.ProcessText  = (TextView) arg1.findViewById(R.id.progress);
			arg2.setTag(holder);
		}else{
			holder=(GroupViewHolder) arg2.getTag();
		}
		
		if(mLineList!=null && mLineList.size()>0){
			holder.indexText.setText(""+mLineList.get(arg0).getIndex());
			holder.NameText.setText(mLineList.get(arg0).getName());
			holder.DeadLineText.setText(mLineList.get(arg0).getDeadLine());
			holder.ProcessText.setText(mLineList.get(arg0).getProcess());
			}
		final int position=arg0;
	    arg1.setOnClickListener(new OnClickListener(){
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
		
			app.setCurGsonPath(mLineList.get(position).getPath());
			app.setgRouteName(mLineList.get(position).getName());
			Intent intent = new Intent();
			app.setCurrentRouteIndex(position);
			intent.putExtra(CommonDef.route_info.NAME,mLineList.get(position).getName());
			intent.setClass(context,StationActivity.class);
			mActivity.startActivity(intent);
		}});
				
		return arg1;
	}

	
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return super.getItemViewType(position);
	}
	void initListData() {
		//final int type =types;
		MLog.Logd(TAG,"initListData()>>");
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
					MLog.Logd(TAG,"start "+ SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));
					if(app.isLogin()){
						LineDao dao = LineDao.getInstance(mActivity);
						ContentValues cv = new ContentValues();
						app.mJugmentListParms=dao.queryLineInfoByWorkerEx(app.getLoginWorkerName(), app.getLoginWorkerPwd(), cv,LineType.SpecialRoute);
						}
					
					int iRouteCount = app.mJugmentListParms !=null?app.mJugmentListParms.size():0;
					MLog.Logd(TAG,"start 1 "+ SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));
					for (int routeIndex = 0; routeIndex < iRouteCount; routeIndex++) {
						try {
							MLog.Logd(TAG,"for start i="
											+ routeIndex
											+ ","
											+ SystemUtil
													.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));
							
							if(app.mJugmentListParms.get(routeIndex).T_Line.HasSpecialLine){
								LineListInfoParams lineInfo = new LineListInfoParams();
								lineInfo.setName(app.mJugmentListParms.get(routeIndex).T_Line.Name);
								lineInfo.setDeadLine("2000-10-10");
								lineInfo.setProcess(
									app.mJugmentListParms.get(routeIndex).T_Line.LineCheckedCount + "/" + app.mJugmentListParms.get(routeIndex).T_Line.LineSpecialTotalCount);
								lineInfo.setIndex( routeIndex+1);
								lineInfo.setPath(app.mJugmentListParms.get(routeIndex).T_Line.LinePath);
							MLog.Logd(TAG,
									" for end i="
											+ routeIndex
											+ ","
											+ SystemUtil
													.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));
							mLineList.add(lineInfo);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				mHander.sendMessage(mHander.obtainMessage(MSG_UPDATE_LISTVIEW));
				MLog.Logd(TAG,"initListData()<<");
			}
		}).start();

	}
	
	private final int MSG_UPDATE_LISTVIEW =0;
	Handler mHander= new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case MSG_UPDATE_LISTVIEW:
				RouteSpecListAdapter.this.notifyDataSetChanged();
		//	if (mListViewAdapter != null) {
		//		mListViewAdapter.notifyDataSetChanged();
		//	}
			break;
			}
			super.handleMessage(msg);
		}
		
	};

}
