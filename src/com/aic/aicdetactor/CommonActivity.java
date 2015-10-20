package com.aic.aicdetactor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.aic.aicdetactor.app.myApplication;
import com.aic.aicdetactor.dialog.FlippingLoadingDialog;
import com.aic.aicdetactor.dialog.CommonDialog;
import com.aic.aicdetactor.dialog.CommonAlterDialog;
import com.aic.aicdetactor.dialog.CommonAlterDialog.AltDialogCancelListener;
import com.aic.aicdetactor.dialog.CommonAlterDialog.AltDialogOKListener;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;

public class CommonActivity extends Activity{
	protected FlippingLoadingDialog mLoadingDialog;
	public myApplication app = null;
	CommonAlterDialog commonAlterDialog;
	CommonDialog chartDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mLoadingDialog = new FlippingLoadingDialog(this, "请求提交中");
	}
	
	protected void setActionBar(String titleName, boolean hasBackButton){
		ActionBar actionBar = getActionBar();		
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME  
		        | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);
		if(titleName!=null) actionBar.setTitle(titleName);
		actionBar.setDisplayHomeAsUpEnabled(hasBackButton);
		showOverflowMenu();
	}
	
	//force to show overflow menu in actionbar
	protected void showOverflowMenu() {
         try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public void showLoadingDialog(String text) {
		if (text != null) {
			mLoadingDialog.setText(text);
		}
		mLoadingDialog.show();
	}

	public void dismissLoadingDialog() {
		if (mLoadingDialog.isShowing()) {
			mLoadingDialog.dismiss();
		}
	}
	
	public void showChartDialog(Context context){
		chartDialog = new CommonDialog(context);
		chartDialog.setCloseBtnVisibility(View.VISIBLE);
		chartDialog.show();
	}
	
	public void dismissChartDialog(){
		if (chartDialog!=null&&chartDialog.isShowing()) {
			chartDialog.dismiss();
		}
	}
	
	public void showAlterDialog(Context context, String title, String content,
			AltDialogOKListener okListener,AltDialogCancelListener cancelListener) {
		commonAlterDialog = new CommonAlterDialog(context, title, content,
			okListener, cancelListener);
		commonAlterDialog.show();
	}
	
	public void dismissAlterDialog(){
		if (commonAlterDialog!=null&&commonAlterDialog.isShowing()) {
			commonAlterDialog.dismiss();
		}
	}
	 @Override  
	  public boolean onCreateOptionsMenu(Menu menu) {  
	    getMenuInflater().inflate(R.menu.main, menu);  
	    return true;  
	  }  
	 @Override  
	  public boolean onOptionsItemSelected(MenuItem item) {  
	    switch (item.getItemId()) { 
	    case android.R.id.home:
	    	finish();
	    	break;
	    case R.id.action_modify_pwd: {
	    	CustomDialog dialog=new CustomDialog(this, R.style.customDialog, R.layout.modify_password,app.getLoginWorkerName());
	        dialog.show();
	        }
	      break;  
	    case R.id.action_changeWorker:
	    //	initFragment();
	    	break;
	    case R.id.action_more:{
			}
	    	break;
	    case R.id.action_about:{
	    	final Dialog dialog = new AboutDialog(this);
			dialog.show();
			}
	    	break;
	    	
	    default:  
	      break;  
	    }  
	    return true;  
	  }
	 
	 public View getBlackLineChartView(String title, float[] y, String xyTitle){
		    
		 String[] titles = new String[]{title};
		 List<float[]> yValues = new ArrayList<float[]>();
		 yValues.add(y);
		 List<float[]> xValues = new ArrayList<float[]>(); 
		 float[] x= new float[y.length];
		 for(int i=0;i<y.length;i++){
			 x[i]=i;
		 }
		 xValues.add(x);
		 XYMultipleSeriesDataset dataset = buildDataset(titles, xValues, yValues);

		 XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		    renderer.setXLabels(12);
		    renderer.setYLabels(10);
		    renderer.setShowGrid(true);
		    renderer.setXLabelsAlign(Align.RIGHT);
		    renderer.setYLabelsAlign(Align.RIGHT);
		    renderer.setZoomButtonsVisible(true);
		    //renderer.setPanLimits(new double[] { -10, 20, -10, 40 });
		    //renderer.setZoomLimits(new double[] { -10, 20, -10, 40 });
		    renderer.setBackgroundColor(Color.WHITE);
		    renderer.setMarginsColor(Color.WHITE);
		    renderer.setApplyBackgroundColor(true);
		    renderer.setYAxisMin(0);
		    renderer.setYAxisMax(1700);
		    renderer.setXAxisMin(1);
		    renderer.setXAxisMax(200);
		    renderer.setLegendHeight(80);
		    //renderer.setXLabels(200);
		    renderer.setPanEnabled(true, true);
		    renderer.setPanLimits(new double[]{0, 4500, 0, 1700});
		    renderer.setMargins(new int[] {0, 25, 10, 0});
		    renderer.setPanEnabled(true,true);
		    if(xyTitle!=null){
			    renderer.setAxisTitleTextSize(20);
			    renderer.setXTitle(xyTitle);
		    }

		 XYSeriesRenderer XYrenderer = new XYSeriesRenderer();
		    XYrenderer.setColor(Color.RED);
		    XYrenderer.setPointStyle(PointStyle.CIRCLE);
		    renderer.addSeriesRenderer(XYrenderer);
		 return ChartFactory.getLineChartView(getApplicationContext(), dataset, renderer);
	 }
	 
	 /**
	  * 曲线图(数据集) : 创建曲线图图表数据集
	  * 
	  * @param 赋予的标题
	  * @param xValues x轴的数据
	  * @param yValues y轴的数据
	  * @return XY轴数据集
	  */
	 protected XYMultipleSeriesDataset buildDataset(String[] titles, List<float[]> xValues,
	     List<float[]> yValues) {
	   XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();/* 创建图表数据集 */
	   addXYSeries(dataset, titles, xValues, yValues, 0);              /* 添加单条曲线数据到图表数据集中 */
	   return dataset;
	 }
	  
	 /**
	  * 曲线图(被调用方法) : 添加 XY 轴坐标数据 到 XYMultipleSeriesDataset 数据集中
	  * 
	  * @param dataset 最后的 XY 数据集结果, 相当与返回值在参数中
	  * @param titles  要赋予的标题
	  * @param xValues x轴数据集合
	  * @param yValues y轴数据集合
	  * @param scale   缩放
	  * 
	  * titles 数组个数 与 xValues, yValues 个数相同
	  * tittle 与 一个图标可能有多条曲线, 每个曲线都有一个标题
	  * XYSeries 是曲线图中的 一条曲线, 其中封装了 曲线名称, X轴和Y轴数据
	  */
	 public void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles, List<float[]> xValues,
	     List<float[]> yValues, int scale) {
	   int length = titles.length;                         /* 获取标题个数 */
	   for (int i = 0; i < length; i++) {
	     XYSeries series = new XYSeries(titles[i], scale); /* 单条曲线数据 */
	     float[] xV = xValues.get(i);                     /* 获取该条曲线的x轴坐标数组 */
	     float[] yV = yValues.get(i);                     /* 获取该条曲线的y轴坐标数组 */
	     int seriesLength = xV.length;
	     for (int k = 0; k < seriesLength; k++) {
	       series.add(xV[k], yV[k]);                       /* 将该条曲线的 x,y 轴数组存放到 单条曲线数据中 */
	     }
	     dataset.addSeries(series);                        /* 将单条曲线数据存放到 图表数据集中 */
	   }
	 }
}
