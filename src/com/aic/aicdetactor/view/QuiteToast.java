package com.aic.aicdetactor.view;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.aic.aicdetactor.R;



public class  QuiteToast {	
	 public static void showTips(final Context context,long	exitTime,Activity activity){
		
		if ((System.currentTimeMillis() - exitTime) > 2000) {			
			    Toast.makeText(context, context.getString(R.string.shureQuiteApp), Toast.LENGTH_SHORT).show();
			    exitTime = System.currentTimeMillis();
			  } else {
				  activity.finish();
			  }
	    	}
}
