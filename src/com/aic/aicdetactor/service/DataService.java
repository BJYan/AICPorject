package com.aic.aicdetactor.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.check.StationActivity;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.data.CheckStatus;
import com.aic.aicdetactor.util.SystemUtil;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class DataService extends Service {

	List<Map<String, String>> mRouteList = null;
	private String mNewRouteFileStr = null;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		iniDataThread.start();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onRebind(Intent intent) {
		// TODO Auto-generated method stub
		super.onRebind(intent);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}
	private	Handler InitDataHandler = new Handler()
    {
		public final  int INIT_DATA = 0;
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
          //  super.handleMessage(msg);
            //InitDataHandler.post(update_thread);
        	switch(msg.what){
        	case INIT_DATA:
        		init();
        		break;
        	}
           
        }       
    };
    
    private Thread iniDataThread = new Thread(new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg = new Message();
			msg.what = 0;
			InitDataHandler.sendMessage(msg);
		}
    	
    });

    public List<Map<String, String>> getRouteList(){
    	synchronized(mRouteList){
    	return mRouteList;
    	}
    }
	void init() {
		String name = SystemUtil.createGUID();
		name = "down.txt";
		mNewRouteFileStr = "/sdcard/"+name;
		((myApplication) getApplication()).insertNewRouteInfo(name,
				mNewRouteFileStr, this);		
		
		if (mRouteList == null) {
			mRouteList = new ArrayList<Map<String, String>>();
			int iRouteCount = ((myApplication) getApplication()).InitData();
			CheckStatus status = null;
			for (int routeIndex = 0; routeIndex < iRouteCount; routeIndex++) {
				try {
					Map<String, String> map = new HashMap<String, String>();
					status = ((myApplication) getApplication())
							.getNodeCount(null,0,routeIndex);
					map.put(CommonDef.route_info.NAME,
							((myApplication) getApplication()).getRoutName(routeIndex));
					map.put(CommonDef.route_info.DEADLINE, status.mLastTime);
					map.put(CommonDef.route_info.STATUS, "已检查");
					map.put(CommonDef.route_info.PROGRESS,status.mCheckedCount+"/"+status.mSum);					
					String index = "" + (routeIndex + 1);
					map.put(CommonDef.route_info.INDEX, index);
					mRouteList.add(map);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}
}
