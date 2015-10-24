package com.aic.aicdetactor.check;

import com.aic.aicdetactor.CommonActivity;
import com.aic.aicdetactor.R;
import com.aic.aicdetactor.adapter.SearchResultStationExListAdapter;

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

public class SearchResultActivity extends CommonActivity implements OnClickListener{
	ImageView SerachBtn;
	LayoutInflater inflater;
	View content;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setActionBar("本地查询",true);
		inflater = getLayoutInflater();
		content = inflater.inflate(R.layout.search_result_layout, null);
		setContentView(content);
		ExpandableListView localSearchExListView = (ExpandableListView) findViewById(R.id.local_search_listview);
		SearchResultStationExListAdapter localSearchExListAdapter = new SearchResultStationExListAdapter(getApplicationContext());
		localSearchExListView.setAdapter(localSearchExListAdapter);
		
		SerachBtn = (ImageView) findViewById(R.id.search_button); 
		SerachBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.search_button:
			View searchPopWinView = inflater.inflate(R.layout.search_database_item_routemodel, null);
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

		default:
			break;
		}
	}

}
