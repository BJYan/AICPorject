package com.aic.aicdetactor.check;

import com.aic.aicdetactor.CommonActivity;
import com.aic.aicdetactor.R;
import com.aic.aicdetactor.adapter.SearchResultConExListAdapter;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.PopupWindow;

public class SearchResultConActivity extends CommonActivity implements OnClickListener{
	ExpandableListView exList;
	ImageView SerachBtn;
	LayoutInflater inflater; 
	View content;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		inflater = getLayoutInflater();
		content = inflater.inflate(R.layout.search_condition_result_layout, null);
		setContentView(content);
		
		setActionBar("数据库查询",true);
		exList = (ExpandableListView) findViewById(R.id.search_con_result_list);
		SearchResultConExListAdapter searchResConExListAdapter = new SearchResultConExListAdapter(getApplicationContext(), this);
		exList.setAdapter(searchResConExListAdapter);
		
		SerachBtn = (ImageView) findViewById(R.id.search_con_button); 
		SerachBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.search_con_button: 
			View searchPopWinView = inflater.inflate(R.layout.search_database_item_searchmodel, null);
			final PopupWindow searchPopWin = new PopupWindow(searchPopWinView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,true);
			searchPopWin.setBackgroundDrawable(new BitmapDrawable());
			if(searchPopWin!=null&&!searchPopWin.isShowing()) searchPopWin.showAsDropDown(arg0);
			content.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					if(searchPopWin!=null&&searchPopWin.isShowing()) {
						searchPopWin.dismiss();
					}
					return false;
				}
			});
			break;
		case R.id.search_con_result_spectrum: 
			showChartDialog(this);
			break;
		case R.id.search_con_result_diagram:
			showChartDialog(this);
			break;
		case R.id.search_con_result_more: 
			showChartDialog(this);
			break;
		default:
			break;
		}
	}
	
}
