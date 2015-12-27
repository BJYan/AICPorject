package com.aic.aicdetactor.adapter;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.activity.SearchStationActivity;
import com.aic.aicdetactor.activity.StationActivity;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.comm.LineType;
import com.aic.aicdetactor.condition.ConditionalJudgement;
import com.aic.aicdetactor.database.LineDao;
import com.aic.aicdetactor.paramsdata.LineListInfoParams;
import com.aic.aicdetactor.setting.Setting;
import com.aic.aicdetactor.util.MLog;
import com.aic.aicdetactor.util.SystemUtil;
import com.aic.aicdetactor.view.GroupViewHolder;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchLineListAdapter extends LineListAdapter {
private String TAG = "SearchLineListAdapter";
	public SearchLineListAdapter(Context context, Activity av,
			LineType lineType) {
		super(context, av, lineType);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View getView( int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		GroupViewHolder holder =null;
		if(arg1==null){
			holder = new GroupViewHolder();		
			arg1 = mInflater.inflate(R.layout.route_list_item, null);
			ImageView image = (ImageView) arg1.findViewById(R.id.ka);
			image.setVisibility(View.GONE);
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
			intent.setClass(context,SearchStationActivity.class);
			intent.putExtra(CommonDef.route_info.NAME,mLineList.get(position).getName());
			app.setCurGsonPath(mLineList.get(position).getPath());
			app.setCurrentRouteIndex(position);
			app.setgRouteName(mLineList.get(position).getName());
			mActivity.startActivity(intent);
		}});
				
		return arg1;
	}
	
	void initListData() {
		MLog.Logd(TAG,"initListData()>>");
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
					MLog.Logd(TAG,"start "+ SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));
					
					if(app.isLogin()){
						LineDao dao = LineDao.getInstance(mActivity);
						ContentValues cv = new ContentValues();
						app.mJugmentListParms=dao.searchUnUploadLineInfoByWorker(app.getLoginWorkerName(), app.getLoginWorkerPwd(), cv,mLineType,false);
					}
//					
					int iRouteCount = app.mJugmentListParms !=null?app.mJugmentListParms.size():0;
					MLog.Logd(TAG,	"start 2 "+ SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));
					for (int routeIndex = 0; routeIndex < iRouteCount; routeIndex++) {
						
						MLog.Logd(TAG,"for start i="+ routeIndex+ ","+ SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));
						try {
							LineListInfoParams lineInfo = new LineListInfoParams();
						lineInfo.setPath(app.mJugmentListParms.get(routeIndex).T_Line.LinePath);
						lineInfo.setName(app.mJugmentListParms.get(routeIndex).T_Line.Name);
						lineInfo.setDeadLine("2010-8-8");
						lineInfo.setIndex(routeIndex+1);
						lineInfo.setProcess(
									app.mJugmentListParms.get(routeIndex).T_Line.LineCheckedCount + "/" + app.mJugmentListParms.get(routeIndex).T_Line.LineNormalTotalCount);
						
						mLineList.add(lineInfo);
						MLog.Logd(TAG,"2 end i="+ routeIndex+ ","+ SystemUtil.getSystemTime(SystemUtil.TIME_FORMAT_YYMMDDHHMM));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				
				mHander.sendMessage(mHander.obtainMessage(MSG_UPDATE_LISTVIEW));
				MLog.Logd(TAG,"initListData()<<");
			}
		}).start();

	}
	

}
