package com.aic.aicdetactor.check;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.adapter.SearchResultConExListAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;

public class SearchResultConActivity extends Activity{
	ExpandableListView exList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_condition_result_layout);
		
		exList = (ExpandableListView) findViewById(R.id.search_con_result_list);
		SearchResultConExListAdapter searchResConExListAdapter = new SearchResultConExListAdapter(getApplicationContext());
		exList.setAdapter(searchResConExListAdapter);
	}
}
