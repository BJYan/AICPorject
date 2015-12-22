package com.aic.aicdetactor.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.activity.SearchResultActivity;
import com.aic.aicdetactor.database.DBHelper;
import com.aic.aicdetactor.database.LineDao;
import com.aic.aicdetactor.paramsdata.LocalSeachInfoParams;
import com.aic.aicdetactor.setting.Setting;
import com.aic.aicdetactor.view.GroupViewHolder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchLocalListAdapter extends BaseAdapter{
	Context context;
	Activity mActivity;
	private LayoutInflater mInflater;
	ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,     Object>>();
	public SearchLocalListAdapter(Context context,Activity activity) {
		// TODO Auto-generated constructor stub
		this.context = context;
		mActivity = activity;
		mInflater = LayoutInflater.from(context);
		initData();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mLocalLineList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mLocalLineList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView( final int arg01, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		GroupViewHolder holder =null;
		if(arg1==null){
			holder = new GroupViewHolder();
			arg1 = mInflater.inflate(R.layout.search_local_list_item, null);
			holder.indexText= (TextView)arg1.findViewById(R.id.search_local_item_num);
			holder.NameText= (TextView)arg1.findViewById(R.id.search_local_item_content);
			holder.DeadLineText= (TextView)arg1.findViewById(R.id.search_local_date);
			holder.ProcessText= (TextView)arg1.findViewById(R.id.progress);
			
		}else{
			holder=(GroupViewHolder) arg2.getTag();
		}
		if(mLocalLineList!=null && mLocalLineList.size()>0){
			holder.NameText.setText(mLocalLineList.get(arg01).getName());
			holder.DeadLineText.setText(mLocalLineList.get(arg01).getDate());
			holder.ProcessText.setText(mLocalLineList.get(arg01).getProcess());
			holder.indexText.setText(""+(arg01+1));
		}
//		final HashMap<String, Object> map =listItem.get(arg0);
//		holder.NameText.setText(map.get("Name").toString());
//		holder.DeadLineText.setText(map.get("Date").toString());
//		holder.ProcessText.setText(map.get("Process").toString());
//		holder.indexText.setText(""+arg0);
		
		arg1.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, SearchResultActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				if(mLocalLineList!=null && mLocalLineList.size()>0){
				intent.putExtra("Name",mLocalLineList.get(arg01).getName());
				intent.putExtra("Date", mLocalLineList.get(arg01).getDate());
				intent.putExtra("Process", mLocalLineList.get(arg01).getProcess());
				intent.putExtra("Path", mLocalLineList.get(arg01).getPath());
				}
				context.startActivity(intent);
			}
		});
		
		return arg1;
	}
	List<LocalSeachInfoParams>  mLocalLineList=null;
	void initData(){
		LineDao dao = LineDao.getInstance(context);
		mLocalLineList = dao.getLocalAllNormalAndSpecialLineInfo();
	}
	
	
}
