package com.aic.aicdetactor.acharEngine;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.XYChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.aic.aicdetactor.dialog.BigChartDialog;
import com.aic.aicdetactor.dialog.OneCtrlDialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

public class ChartBuilder {

	private static final String TAG = "ChartBuilder";
	Context context;
	
	public ChartBuilder(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	 public View getBlackLineChartView(final String title, final float[] y,final float Ymax,final float ffValue){
		    
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

		 final XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		    renderer.setXLabels(12);
		    renderer.setYLabels(10);
		    renderer.setShowGrid(true);
		    renderer.setXLabelsAlign(Align.RIGHT);
		    renderer.setYLabelsAlign(Align.RIGHT);
		    //renderer.setZoomButtonsVisible(true);
		    //renderer.setPanLimits(new double[] { -10, 20, -10, 40 });
		    //renderer.setZoomLimits(new double[] { -10, 20, -10, 40 });
		    renderer.setBackgroundColor(Color.DKGRAY);
		    renderer.setMarginsColor(Color.DKGRAY);
		    renderer.setApplyBackgroundColor(true);
		    renderer.setYAxisMin(-(ffValue-Ymax));
		    renderer.setYAxisMax(Ymax);
		    renderer.setXAxisMin(0);
		    renderer.setXAxisMax(y.length);
		    renderer.setLegendHeight(5);
		    //renderer.setXLabels(200);
		    renderer.setPanEnabled(false, false);
		    renderer.setPanLimits(new double[]{0, 4500, 0, 1700});
		    //renderer.setMargins(new int[] {0, 50, 10, 0});
		    renderer.setMargins(new int[] {0, 20, 10, 0});
		    renderer.setPanEnabled(true,true);
		    renderer.setAxisTitleTextSize(30);
		    //renderer.setYTitle("m/s*2");
		    
		 XYSeriesRenderer XYrenderer = new XYSeriesRenderer();
		    XYrenderer.setColor(Color.YELLOW);
		    XYrenderer.setPointStyle(PointStyle.X);
		    renderer.addSeriesRenderer(XYrenderer);
		 View chartView = ChartFactory.getLineChartView(context, dataset, renderer);
		 final XYChart lineChart = new LineChart(dataset, renderer);
/*		 chartView.setOnTouchListener(new OnTouchListener() {
			boolean moved = false;
			XYMultipleSeriesRenderer movedRenderer = lineChart.getRenderer();
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_MOVE:
					moved = true;
					break;
				case MotionEvent.ACTION_UP:
					if(moved) { 
						float[] MinMaxData = getRangeMaxMin(y,(int)movedRenderer.getXAxisMin(),(int)movedRenderer.getXAxisMax());
						Log.i(TAG, "XAxisMin = "+movedRenderer.getXAxisMin()+" XAxisMax = "+movedRenderer.getXAxisMax());
						String MinMax = "Min:"+MinMaxData[0]+"\nMax:"+MinMaxData[1];
						renderer.setXTitle(MinMax);
					}
					break;
				default:
					break;
				}
				return false;
			}
		});*/
		 chartView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					BigChartDialog bigCtrlDialog = new BigChartDialog(context);
					bigCtrlDialog.setChartView(getBigChartView(title,y,Ymax, ffValue));
					bigCtrlDialog.show();
					break;
				default:
					break;
				}
				return false;
			}
		});
		 return chartView;
	 }
	 
	 public View getBigChartView(String title, final float[] y,float Ymax,float ffValue){
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

		 final XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		    renderer.setXLabels(12);
		    renderer.setYLabels(10);
		    renderer.setShowGrid(true);
		    renderer.setXLabelsAlign(Align.RIGHT);
		    renderer.setYLabelsAlign(Align.RIGHT);
		    renderer.setZoomButtonsVisible(true);
		    //renderer.setPanLimits(new double[] { -10, 20, -10, 40 });
		    //renderer.setZoomLimits(new double[] { -10, 20, -10, 40 });
		    renderer.setBackgroundColor(Color.DKGRAY);
		    renderer.setMarginsColor(Color.DKGRAY);
		    renderer.setApplyBackgroundColor(true);
		    renderer.setYAxisMin(-(ffValue-Ymax));
		    renderer.setYAxisMax(Ymax);
		    renderer.setXAxisMin(0);
		    renderer.setXAxisMax(y.length);
		    renderer.setLegendHeight(130);
		    //renderer.setXLabels(200);
		    renderer.setPanEnabled(true, true);
		    renderer.setPanLimits(new double[]{0, 4500, 0, 1700});
		    renderer.setMargins(new int[] {0, 50, 10, 0});
		    renderer.setPanEnabled(true,true);
		    renderer.setAxisTitleTextSize(30);
		    renderer.setYTitle("m/s*2");
		    
		 XYSeriesRenderer XYrenderer = new XYSeriesRenderer();
		    XYrenderer.setColor(Color.YELLOW);
		    XYrenderer.setPointStyle(PointStyle.X);
		    renderer.addSeriesRenderer(XYrenderer);
		 View chartView = ChartFactory.getLineChartView(context, dataset, renderer);
		 final XYChart lineChart = new LineChart(dataset, renderer);
		 chartView.setOnTouchListener(new OnTouchListener() {
			boolean moved = false;
			XYMultipleSeriesRenderer movedRenderer = lineChart.getRenderer();
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_MOVE:
					moved = true;
					break;
				case MotionEvent.ACTION_UP:
					if(moved) { 
						float[] MinMaxData = getRangeMaxMin(y,(int)movedRenderer.getXAxisMin(),(int)movedRenderer.getXAxisMax());
						Log.i(TAG, "XAxisMin = "+movedRenderer.getXAxisMin()+" XAxisMax = "+movedRenderer.getXAxisMax());
						String MinMax = "Min:"+MinMaxData[0]+"\nMax:"+MinMaxData[1];
						renderer.setXTitle(MinMax);
					}
					break;
				default:
					break;
				}
				return false;
			}
		});
		 return chartView;
	 }
	 
	 private float[] getRangeMaxMin(float[] data,int start, int end){ 
		 if(start<0){start=0;}
		 if(end>=data.length){
			 end=data.length-1;
		 }
		 float[] MinMaxTemp = new float[]{data[start],data[start]};
		 for(int i=0;i<data.length;i++){
			 if(i>=start&&i<=end) {
				 Log.e(TAG, "data["+i+"] = "+data[i]);
				 if(data[i]<=MinMaxTemp[0]) MinMaxTemp[0] = data[i];
				 if(data[i]>MinMaxTemp[1]) MinMaxTemp[1] = data[i];
			 }
		 }
		 return MinMaxTemp;
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
