package com.aic.aicdetactor.check;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.adapter.LocalSearchStationExListAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.LinearLayout;

public class LocalSerachActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.search_localsearch_layout);
		LinearLayout localSerachContainer = (LinearLayout) findViewById(R.id.localSerach_container);
		
		ExpandableListView localSearchExListView = (ExpandableListView) findViewById(R.id.local_search_listview);
		LocalSearchStationExListAdapter localSearchExListAdapter = new LocalSearchStationExListAdapter(getApplicationContext());
		localSearchExListView.setAdapter(localSearchExListAdapter);
	}

}
