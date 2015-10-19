package com.aic.aicdetactor;

import android.app.Fragment;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.CalendarView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.CalendarView.OnDateChangeListener;

public class CommonFragment extends Fragment {

	protected static final String TAG = null;

	public void CreatCalendarPopWin(final View contentView, LayoutInflater inflater, View root){
		final PopupWindow CalPop = new PopupWindow(contentView,
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true); 
		View PopupWindowview_task_end = inflater.inflate(R.layout.calendar_view_layout, null);
		CalendarView calendarView_task_end = (CalendarView) PopupWindowview_task_end.findViewById(R.id.calendar);
		CalPop.setBackgroundDrawable(new BitmapDrawable());
		
		calendarView_task_end.setOnDateChangeListener(new OnDateChangeListener() {  
        	  
            @Override  
            public void onSelectedDayChange(CalendarView arg0, int arg1,  
                    int arg2, int arg3) {  
                arg2 = arg2 + 1;  
                Log.i(TAG, "-------------" + arg1 + "-" + arg2 + "-"  
                        + arg3); 
                ((TextView) contentView).setText(arg1 + "-" + arg2 + "-"+ arg3);
                CalPop.dismiss();
            }  
        });
		root.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if(CalPop!=null&&CalPop.isShowing()) {
					CalPop.dismiss();
				}
				return false;
			}
		});
		CalPop.setContentView(PopupWindowview_task_end);
		if(CalPop!=null&&!CalPop.isShowing()) CalPop.showAsDropDown(contentView);
	}
}
