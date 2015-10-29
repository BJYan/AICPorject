package com.aic.aicdetactor.adapter;

import java.util.ArrayList;
import java.util.List;

import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.data.PartItemJsonUp;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PartItemListAdapter extends BaseAdapter {
	List<String> statusList = new ArrayList<String>();
	private ArrayList<PartItemJsonUp> mPartItemList=null;
	private ArrayList<PartItemJsonUp> mOriPartItemList=null;//原始的数据
	//private ArrayList<String> mPartItemNameList=null;
	private int mStationIndex=0;
	private int mDeviceIndex=0;
	myApplication app ;
	Activity mActivity;
	public PartItemListAdapter(Activity av,int mStationIndex,int mDeviceIndex){
		this.mDeviceIndex=mDeviceIndex;
		this.mStationIndex=mStationIndex;
		this.mActivity = av;
		app = ((myApplication)av. getApplication());
		initListViewAndData();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mPartItemList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mPartItemList.get(arg0).Check_Content;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		
		TextView tmView;
		if(arg1 == null){
		TextView tm = new TextView(mActivity.getApplicationContext());
		tmView = tm;
		tmView.setText(mPartItemList.get(arg0).Check_Content);
		}else {
		tmView = (TextView)arg1;
		tmView.setText((mPartItemList.get(arg0).Check_Content) + "\t" + arg1.getTag());
		}

		return tmView;
	}
	public void initListViewAndData(){
		try {
			if(mPartItemList == null ||mOriPartItemList==null ){
			 mPartItemList = new ArrayList<PartItemJsonUp>();
			 mOriPartItemList= new ArrayList<PartItemJsonUp>();
			 }else{
				 mOriPartItemList.clear();
				 mPartItemList.clear();
			 }
			

			PartItemJsonUp item =null;
			for (int i = 0; i < app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(mDeviceIndex).PartItem.size(); i++) {
				
				item =  app.mLineJsonData.StationInfo.get(mStationIndex).DeviceItem.get(mDeviceIndex).PartItem.get(i);
				mPartItemList.add(item);
				mOriPartItemList.add(item);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		this.notifyDataSetChanged();
		
	}
	
	public void resetInitListData(){
		initListViewAndData();
	}
	public void revertListViewData(){
		
		ArrayList<PartItemJsonUp> ItemList = new ArrayList<PartItemJsonUp>();
		for(int i= mPartItemList.size()-1;i>=0;i--){
			ItemList.add(mPartItemList.get(i));
		}
		mPartItemList.clear();
		for(int m= 0;m<ItemList.size();m++){
			mPartItemList.add(ItemList.get(m));
		}
		this.notifyDataSetChanged();
		
	}
	
	public void getNewPartItemListDataByStatusArray(int index){
		mPartItemList.clear();
		for(int m= 0;m<mOriPartItemList.size();m++){
			if(mOriPartItemList.get(m).Start_Stop_Flag==index)	{
				mPartItemList.add(mOriPartItemList.get(m));
				}
			}
		this.notifyDataSetChanged();
	}
	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}
}
