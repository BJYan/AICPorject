package com.aic.aicdetactor.check;

import com.aic.aicdetactor.CommonActivity;
import com.aic.aicdetactor.R;
import com.aic.aicdetactor.adapter.SearchResultConExListAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.PopupWindow;

public class SearchResultConActivity extends CommonActivity implements OnClickListener{
	ExpandableListView exList;
	ImageView SerachBtn;
	LayoutInflater inflater; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_condition_result_layout);
		
		setActionBar("Êý¾Ý¿â²éÑ¯",true);
		inflater = getLayoutInflater();
		exList = (ExpandableListView) findViewById(R.id.search_con_result_list);
		SearchResultConExListAdapter searchResConExListAdapter = new SearchResultConExListAdapter(getApplicationContext());
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
			PopupWindow searchPopWin = new PopupWindow(searchPopWinView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			searchPopWin.showAsDropDown(arg0);
			break;

		default:
			break;
		}
	}
	
}
