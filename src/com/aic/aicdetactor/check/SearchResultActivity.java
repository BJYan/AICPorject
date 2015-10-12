package com.aic.aicdetactor.check;

import com.aic.aicdetactor.CommonActivity;
import com.aic.aicdetactor.R;
import com.aic.aicdetactor.adapter.SearchResultStationExListAdapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.PopupWindow;

public class SearchResultActivity extends CommonActivity implements OnClickListener{
	ImageView SerachBtn;
	LayoutInflater inflater;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setActionBar("±æµÿ≤È—Ø",true);
		inflater = getLayoutInflater();
		setContentView(R.layout.search_result_layout);
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
			PopupWindow searchPopWin = new PopupWindow(searchPopWinView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			searchPopWin.showAsDropDown(arg0);
			break;

		default:
			break;
		}
	}

}
