package com.aic.aicdetactor.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.aic.aicdetactor.R;
import com.aic.aicdetactor.adapter.SearchAdapter;
import com.aic.aicdetactor.adapter.SearchDatabaseExListAdapter;
import com.aic.aicdetactor.adapter.SearchLocalListAdapter;
import com.aic.aicdetactor.check.SearchResultActivity;
import com.aic.aicdetactor.check.SearchResultConActivity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

public class SearchFragment extends Fragment implements OnClickListener{
	TabHost tabHost;
	ViewPager viewPager;
	List<View> DBsearchItemList;
	Map<TextView, Boolean> options;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		options = new HashMap<TextView, Boolean>();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View searchView = inflater.inflate(R.layout.search_fragment_layout, container, false);
		tabHost = (TabHost)searchView.findViewById(R.id.search_tabhost);  
        // 如果没有继承TabActivity时，通过该种方法加载启动tabHost  
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("本地查询")  
                .setContent(  
                R.id.view1));
  
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("数据库查询")  
                .setContent(R.id.view2)); 
        
        viewPager = (ViewPager)searchView.findViewById(R.id.search_vPager); 
        
        ArrayList<View> listViews = new ArrayList<View>();
        listViews.add(inflater.inflate(R.layout.search_local_layout, null));
        listViews.add(inflater.inflate(R.layout.search_database_layout, null));
        
        ListView searchLocalList = (ListView) listViews.get(0).findViewById(R.id.search_local_list);
        SearchLocalListAdapter searchLocalListAdapter = new SearchLocalListAdapter(getActivity().getApplicationContext());
        searchLocalList.setAdapter(searchLocalListAdapter);
        
        DBsearchItemList = new ArrayList<View>();
        DBsearchItemList.add(inflater.inflate(R.layout.search_database_item_searchmodel, null));
        DBsearchItemList.add(inflater.inflate(R.layout.search_database_item_routemodel, null));
        TextView SearchBtn_1 = (TextView) DBsearchItemList.get(0).findViewById(R.id.search_button_1);
        SearchBtn_1.setOnClickListener(this);
        TextView SearchBtn_2 = (TextView) DBsearchItemList.get(1).findViewById(R.id.search_button_2);
        SearchBtn_2.setOnClickListener(this);
        
        ExpandableListView searchDBList = (ExpandableListView) listViews.get(1).findViewById(R.id.search_database_list);
        SearchDatabaseExListAdapter SearchDBExListAdapter = new SearchDatabaseExListAdapter(getActivity().getApplicationContext(),DBsearchItemList);
        searchDBList.setAdapter(SearchDBExListAdapter);
        searchDBList.setGroupIndicator(null);
        RelativeLayout optionsContainer = (RelativeLayout) DBsearchItemList.get(0).findViewById(R.id.options_item_container);
        for(int i=0;i<optionsContainer.getChildCount();i++){
        	if(optionsContainer.getChildAt(i).getTag()!=null&&
        			((String)optionsContainer.getChildAt(i).getTag()).equals("options_name")) continue;
        	else {
        		optionsContainer.getChildAt(i).setOnClickListener(this);
        		options.put((TextView) optionsContainer.getChildAt(i), false);
        	}
        }
        
        SearchAdapter searchAdapter = new SearchAdapter(listViews);
        viewPager.setAdapter(searchAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        
        tabHost.setOnTabChangedListener(new OnTabChangeListener(){  
            @Override  
            public void onTabChanged(String tabId){  
                Log.i("DownLoadFragment--tabId--=", tabId);  
                if(tabId.equals("tab1")) viewPager.setCurrentItem(0);
                if(tabId.equals("tab2")) viewPager.setCurrentItem(2);
            }  
        });
		return searchView;
	}
	
	class MyOnPageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			tabHost.setCurrentTab(arg0);
		}
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if(arg0 instanceof TextView && arg0.getTag()==null){
			if(options.get(arg0)) options.put((TextView) arg0, false);
			else options.put((TextView) arg0, true);
			
			Iterator<TextView> optionsIterator = options.keySet().iterator();
			while(optionsIterator.hasNext()){
				TextView tempTextView = optionsIterator.next();
				if(options.get(tempTextView)) tempTextView.setBackgroundResource(R.drawable.search_database_itemshape_checked);
				else tempTextView.setBackgroundResource(R.drawable.search_database_itemshape);
			}
		}
		if(arg0.getId()==R.id.search_button_1) {
			Intent intent = new Intent();
			intent.setClass(getActivity(), SearchResultConActivity.class);
			getActivity().startActivity(intent);
		}
		if(arg0.getId()==R.id.search_button_2) {
			Intent intent = new Intent();
			intent.setClass(getActivity(), SearchResultActivity.class);
			getActivity().startActivity(intent);
		}
	}
}