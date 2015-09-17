package com.aic.aicdetactor.fragment;

import java.util.ArrayList;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.adapter.MessageAdapter;
import com.aic.aicdetactor.adapter.MessageListViewAdapter;
import com.aic.aicdetactor.adapter.SearchAdapter;
import com.aic.aicdetactor.adapter.SearchDatabaseExListAdapter;
import com.aic.aicdetactor.adapter.SearchLocalListAdapter;
import com.aic.aicdetactor.fragment.Message_Fragment.MyOnPageChangeListener;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

public class SearchFragment extends Fragment{
	TabHost tabHost;
	ViewPager viewPager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
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
        ExpandableListView searchDBList = (ExpandableListView) listViews.get(1).findViewById(R.id.search_database_list);
        SearchDatabaseExListAdapter SearchDatabaseExListAdapter = new SearchDatabaseExListAdapter(getActivity().getApplicationContext());
        searchDBList.setAdapter(SearchDatabaseExListAdapter);
        searchDBList.setGroupIndicator(null);
        
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
}
