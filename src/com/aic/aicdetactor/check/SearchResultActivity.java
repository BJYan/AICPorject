package com.aic.aicdetactor.check;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.adapter.SearchResultStationExListAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;

public class SearchResultActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.search_result_layout);
		ExpandableListView localSearchExListView = (ExpandableListView) findViewById(R.id.local_search_listview);
		SearchResultStationExListAdapter localSearchExListAdapter = new SearchResultStationExListAdapter(getApplicationContext());
		localSearchExListView.setAdapter(localSearchExListAdapter);
	}

}
