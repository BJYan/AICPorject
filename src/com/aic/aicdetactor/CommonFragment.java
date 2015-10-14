package com.aic.aicdetactor;

import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.CalendarView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.CalendarView.OnDateChangeListener;

public class CommonFragment extends Fragment {

	protected static final String TAG = null;

	public void CreatCalendarPopWin(final View contentView, LayoutInflater inflater){
		final PopupWindow CalPop = new PopupWindow(contentView,
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true); 
		View PopupWindowview_task_end = inflater.inflate(R.layout.calendar_view_layout, null);
		CalendarView calendarView_task_end = (CalendarView) PopupWindowview_task_end.findViewById(R.id.calendar);
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
		CalPop.setContentView(PopupWindowview_task_end);
		CalPop.showAsDropDown(contentView);
	}
}
