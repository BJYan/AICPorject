package com.aic.aicdetactor.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aic.aicdetactor.R;
/** 
 *  
 *  
 * @Title:  
 * @Description: 实现TODO 
 * @Copyright:Copyright (c) 2011 
 * @Company:易程科技股份有限公司 
 * @Date:2012-8-17 
 * @author  longgangbai 
 * @version 1.0 
 */  
public class SwitchView extends LinearLayout {  
    private static final String TAG = "Switch";  
    private static final int TEXT_SIZE = 23;  
    private OnCheckedChangeListener mOnCheckedChangeListener;  
    private boolean mChecked;  
    private Paint mPaint;  
    private String mCheckedString, mUnCheckedString;  
    private TextView onTextView;
    private TextView offTextView;
    
    public interface OnCheckedChangeListener {  
        public void onCheckedChanged(SwitchView switchView, boolean isChecked);  
    }  
  
    public SwitchView(Context context) {  
        this(context, null);  
    }  
  
    public SwitchView(Context context, AttributeSet attrs) {  
        this(context, attrs, 0);  
    }  
  
    public SwitchView(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
        // init desity dpi，之前引用屏幕密度的时候出现问题，去掉密度，按实际的宽和高的比例来设置文字的位置  
//        DisplayMetrics metric = new DisplayMetrics();  
//        WindowManager windowManager = (WindowManager) context  
//                .getSystemService(Context.WINDOW_SERVICE);  
//        windowManager.getDefaultDisplay().getMetrics(metric);  
//        mDensity = metric.density;  
        mCheckedString = context.getResources().getString(R.string.plancheck_name);//业务功能开启时的图片显示文字字符串：即ON  
        mUnCheckedString = context.getResources().getString(R.string.tem_check);//OFF  
        setBackgroundResource(R.drawable.white_btn);//设置开关按钮的背景  
        setChecked(false);//初始化控件为关的状态  
        configPaint(context,attrs);//设置画布属性（去掉密度，直接以字体12显示）  
       // computePosition();  
    }  
  
  // private void computePosition() {  
//        mLeftTextX = LEFT_STATE_STRING_MARGIN_LEFT * mDensity;  
//        mRightTextX = RIGHT_STATE_STRING_MARGIN_LEFT * mDensity;  
//        mTextY = STRING_MARGIN_TOP * mDensity;  
        //modify by chengxi  
//      mLeftTextX = getWidth()/2/3 * mDensity;  
//      mRightTextX =getWidth()/2* mDensity;  
//      mTextY = STRING_MARGIN_TOP * mDensity;    
 //   }  
  
    private void configPaint(Context context,AttributeSet attrs) {  
        mPaint = new Paint();  
        mPaint.setAntiAlias(true);//去文字锯齿  
      //  mPaint.setTextSize(TEXT_SIZE * mDensity);  
        float textSize = TEXT_SIZE * getResources().getDisplayMetrics().density;
        mPaint.setTextSize(textSize);  
        mPaint.setColor(Color.WHITE);//画布背景即文字字体的颜色 
        
    }  
  
    public void setChecked(boolean checked) {  
        mChecked = checked;  
        if (mChecked) {  
        	setBackgroundResource(R.drawable.left_on);
         } else {  
        	setBackgroundResource(R.drawable.right_on);  
        }  
        postInvalidate();  
            //见http://blog.csdn.net/sdhjob/article/details/6512973  
            //View的invalidate和postInvalidate都是更新界面，不同的是  
            //invalidate与创建VIEW都是在同一个线程中执行的，即主线程，如果在非主线程中执行的话需要配合Handler使用  
            //而postInvalidate可以在非主线程中更新UI界面  
  
    }  
  
    public boolean isChecked() {  
        return mChecked;  
    }  
  
    @Override  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);  
        canvas.drawText(mCheckedString, (getWidth()*0.5f)+12, getHeight()*0.7f, mPaint);  
        canvas.drawText(mUnCheckedString, 1 , getHeight()*0.7f, mPaint);  
    //画VIEW的时候开始画的点(0,0)是在左上角，图片从左上往下画，画文字的时候开始画的点是在左下角，文字从左下往上画  
    }  
  
    @Override  
    public boolean onTouchEvent(MotionEvent event) {  
        int action = event.getAction();  
        switch (action) {  
            case MotionEvent.ACTION_UP:  
                setChecked(!mChecked);  
                if (mOnCheckedChangeListener != null) {  
                    mOnCheckedChangeListener.onCheckedChanged(this, mChecked);  
                }  
                break;  
            default:  
                // Do nothing  
                break;  
        }  
  
        return true;  
    }  
  
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {  
        mOnCheckedChangeListener = listener;  
    }  
  
}  
