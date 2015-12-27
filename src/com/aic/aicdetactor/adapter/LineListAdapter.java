package com.aic.aicdetactor.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.activity.StationActivity;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.comm.LineType;
import com.aic.aicdetactor.condition.ConditionalJudgement;
import com.aic.aicdetactor.database.LineDao;
import com.aic.aicdetactor.paramsdata.LineListInfoParams;
import com.aic.aicdetactor.setting.Setting;
import com.aic.aicdetactor.util.MLog;
import com.aic.aicdetactor.util.SystemUtil;
import com.aic.aicdetactor.view.GroupViewHolder;

public class LineListAdapter extends BaseAdapter{
	protected Context context;
	protected LayoutInflater mInflater;
	private final  String TAG="luotest.RouteNormalListAdapter";
	protected myApplication app = null;
	protected Activity mActivity=null;
	protected List<LineListInfoParams> mLineList=null;
	protected LineType mLineType;
	protected BaseAdapter adapter;
	public LineListAdapter(Context context,Activity av,LineType lineType) {
		// TODO Auto-generated constructor stub
		this.context = context;
		mInflater = LayoutInflater.from(context);
		this.mLineList = new ArrayList<LineListInfoParams>();
		app =(myApplication) av.getApplication();
		mActivity =av;
		mLineType = lineType;
		adapter = LineListAdapter.this;
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
			Intent intent = new Intent();
			if(mLineType==LineType.NormalRoute){
				ConditionalJudgement jugment = new ConditionalJudgement();
				ContentValues nInfo=new ContentValues();
				Log.d(TAG," onClick() app.mJugmentListParms.size() = "+app.mJugmentListParms.size()+",position ="+position);
				if(jugment.GetUploadJsonFile(app.mJugmentListParms.get(position).T_Line,
						app.mJugmentListParms.get(position).m_PeriodInfo, 
						app.mJugmentListParms.get(position).T_Turn, 
						app.mJugmentListParms.get(position).m_WorkerInfoJson, 
						app.mJugmentListParms.get(position).m_RoutePeroid, nInfo,mActivity.getApplicationContext())){
					
					
					String filePath = nInfo.getAsString("FileName");
					if("".endsWith(filePath)){
						app.gIsDataChecked=false;
						String p=app.mJugmentListParms.get(position).T_Line.LinePath;
						app.setCurGsonPath(p);
					}else{
						app.gIsDataChecked=true;
						app.setCurGsonPath(Setting.getUpLoadJsonPath()+filePath);
					}
					
				}else{
					Toast.makeText(mActivity.getApplicationContext(), nInfo.get("err").toString(), Toast.LENGTH_LONG).show();
					return;
				}
			}else if(mLineType == LineType.SpecialRoute){
				app.setCurGsonPath(mLineList.get(position).getPath());
			}
			intent.setClass(context,StationActivity.class);
			intent.putExtra(CommonDef.route_info.NAME,mLineList.get(position).getName());
			app.setCurrentRouteIndex(position);
			app.setgRouteName(mLineList.get(position).getName());
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
					MLog.Logd(TAG,
							"in init() 1 start "
									+ SystemUtil
											.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));
					LineType lineType=LineType.NormalRoute;
					if(mLineType==LineType.NormalRoute){
						lineType = LineType.NormalRoute;
					}else if(mLineType==LineType.SpecialRoute){
						lineType = LineType.SpecialRoute;
					}
					
					if(app.isLogin()){
						LineDao dao = LineDao.getInstance(mActivity);
						ContentValues cv = new ContentValues();
						app.mJugmentListParms=dao.queryLineInfoByWorkerEx(app.getLoginWorkerName(), app.getLoginWorkerPwd(), cv,lineType);
					}
					
					int iRouteCount = app.mJugmentListParms !=null?app.mJugmentListParms.size():0;
					MLog.Logd(TAG,
							"in init() 2 start "
									+ SystemUtil
											.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));
					for (int routeIndex = 0; routeIndex < iRouteCount; routeIndex++) {
						
							MLog.Logd(TAG,
									"in init() for start i="
											+ routeIndex
											+ ","
											+ SystemUtil
													.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));
							try {
								LineListInfoParams lineInfo = new LineListInfoParams();
							if(mLineType==LineType.NormalRoute){	
							
							lineInfo.setName(app.mJugmentListParms.get(routeIndex).T_Line.Name);
							lineInfo.setDeadLine("2010-8-8");
							lineInfo.setIndex(routeIndex+1);
							lineInfo.setProcess(
										app.mJugmentListParms.get(routeIndex).T_Line.LineCheckedCount + "/" + app.mJugmentListParms.get(routeIndex).T_Line.LineNormalTotalCount);
							
							}else if(mLineType==LineType.SpecialRoute){
								if(app.mJugmentListParms.get(routeIndex).T_Line.HasSpecialLine){
									
									lineInfo.setName(app.mJugmentListParms.get(routeIndex).T_Line.Name);
									lineInfo.setDeadLine("2000-10-10");
									lineInfo.setProcess(
										app.mJugmentListParms.get(routeIndex).T_Line.LineCheckedCount + "/" + app.mJugmentListParms.get(routeIndex).T_Line.LineSpecialTotalCount);
									lineInfo.setIndex( routeIndex+1);
									lineInfo.setPath(app.mJugmentListParms.get(routeIndex).T_Line.LinePath);
									
							}
								}
							mLineList.add(lineInfo);
							MLog.Logd(TAG,
									"in init() for end i="
											+ routeIndex
											+ ","
											+ SystemUtil
													.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				
				mHander.sendMessage(mHander.obtainMessage(MSG_UPDATE_LISTVIEW));
				MLog.Logd(TAG,"initListData()<<");
			}
		}).start();

	}
	
	protected final int MSG_UPDATE_LISTVIEW =0;
	protected Handler mHander= new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case MSG_UPDATE_LISTVIEW:
				adapter.notifyDataSetChanged();
			break;
			}
			super.handleMessage(msg);
		}
		
	};

}
