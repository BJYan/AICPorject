package com.aic.aicdetactor.data;

import com.aic.aicdetactor.R;
import android.content.Context;

/**
 * 获取巡检项的巡检状态，保存巡检总数，已巡检数量及最后的巡检时间
 * @author Administrator
 *
 */
public class CheckStatus{
	public int mSum = 0;
	public int mCheckedCount =0;
	public String mLastTime = null;
	public Context mContext = null;
	//true mease has finish checked this node,
	public boolean hasChecked = false;
	//public static 
	public CheckStatus(){
		mSum = 0;
		mCheckedCount =0;
		mLastTime = null;		
	}
	
	public void setContext(Context mContext){
		this.mContext= mContext;
	}
	/**
	 * 
	 * @return -1 标示未开始检测，0,标示正在巡检中但未完成，1表示已巡检完成
	 */
	public String getStatus() {
		if (mContext != null) {
			if (mSum != 0 && mSum == mCheckedCount) {
				return mContext.getString(R.string.checked);
			}
			if (mCheckedCount != 0 && mSum > mCheckedCount) {
				return mContext.getString(R.string.checking);
			}
		}

		return null;
	}
}