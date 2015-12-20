package com.aic.aicdetactor.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.activity.SearchResultActivity;
import com.aic.aicdetactor.database.DBHelper;
import com.aic.aicdetactor.database.LineDao;
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
		return listItem.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return listItem.get(arg0);
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
			arg1 = mInflater.inflate(R.layout.search_local_list_item, null);
			holder.indexText= (TextView)arg1.findViewById(R.id.search_local_item_num);
			holder.NameText= (TextView)arg1.findViewById(R.id.search_local_item_content);
			holder.DeadLineText= (TextView)arg1.findViewById(R.id.search_local_date);
			holder.ProcessText= (TextView)arg1.findViewById(R.id.progress);
			
		}else{
			holder=(GroupViewHolder) arg2.getTag();
		}
		final HashMap<String, Object> map =listItem.get(arg0);
		holder.NameText.setText(map.get("Name").toString());
		holder.DeadLineText.setText(map.get("Date").toString());
		holder.ProcessText.setText(map.get("Process").toString());
		holder.indexText.setText(""+arg0);
		
		arg1.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(context, SearchResultActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("Name", map.get("Name").toString());
				intent.putExtra("Date", map.get("Date").toString());
				intent.putExtra("Process", map.get("Process").toString());
				intent.putExtra("Path", map.get("Path").toString());
				context.startActivity(intent);
			}
		});
		
		return arg1;
	}
	 
	void initData(){
		LineDao dao = LineDao.getInstance(context);
		String StrSql = "select * from "+DBHelper.TABLE_CHECKING;
		Cursor cur= null;
		try{
		cur = dao.execSQL(StrSql);
		while(cur!=null && cur.moveToNext()){
			HashMap<String, Object> map = new HashMap<String, Object>();  
            map.put("Name", cur.getString(cur.getColumnIndex(DBHelper.Checking_Table.T_Line_Name)));
            map.put("Path", Setting.getUpLoadJsonPath()+cur.getString(cur.getColumnIndex(DBHelper.Checking_Table.File_Guid)));
            map.put("Process",cur.getString(cur.getColumnIndex(DBHelper.Checking_Table.Checked_Count))+"/"+cur.getString(cur.getColumnIndex(DBHelper.Checking_Table.ItemCounts)));
            map.put("Date", cur.getString(cur.getColumnIndex(DBHelper.Checking_Table.Date)));
            listItem.add(map);  
		}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(cur!=null){
			cur.close();
			}
		}
		
	}
	
	
}
