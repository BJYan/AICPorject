package com.aic.aicdetactor.activity;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.adapter.StationListAdapter;
import com.aic.aicdetactor.comm.LineType;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;

public class SearchStationActivity extends StationActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TAG = "SearchStationActivity";
		ImageView image=(ImageView) findViewById(R.id.station_nfc);
		image.setVisibility(View.GONE);;
    	
		setActionBar("本地查询",true);
		//mListViewAdapter = new SearchStationListAdapter(SearchStationActivity.this,this.getApplicationContext(),handler);
		mListViewAdapter = new StationListAdapter(SearchStationActivity.this,this.getApplicationContext(),handler,LineType.AllRoute);
		mListView.setAdapter(mListViewAdapter);
	}
	
	
	void initPopupWindowFliter(View parent) {
		LayoutInflater inflater = (LayoutInflater) this
				.getApplicationContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		final View rootview = inflater.inflate(
				R.layout.searchstation_menu, null, false);

		final PopupWindow pw_Left = new PopupWindow(rootview, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, true);
		pw_Left.setBackgroundDrawable(null);
		
		pw_Left.setTouchInterceptor(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if (arg1.getAction() == MotionEvent.ACTION_OUTSIDE) {
					pw_Left.dismiss();
					return true;
				}
				return false;
			}

		});
		ColorDrawable dw = new ColorDrawable(Color.GRAY);
		pw_Left.setBackgroundDrawable(dw);
		pw_Left.setOutsideTouchable(true);
		pw_Left.showAsDropDown(parent, Gravity.CENTER, 0);
		pw_Left.update();

	}

}
