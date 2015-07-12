package com.aic.aicdetactor.setting;

import android.os.Environment;

/**
 * 本文件主要是针对软件app的相关设置属性
 * 数据分为两类，
 * 第一类是从服务器上获取的计划巡检数据
 * 第二类数据是本地生成的数据，例如 拍照、录音、快速记录等用户生成的数据相关设置。
 * @author Administrator
 *
 */
public class Setting {

	/*
	 * 相关文件存储的默认根目录
	 */
	private String mDataDirector = null;	
	public Setting(){
		mDataDirector = Environment.getExternalStorageState()+"/AIC/";
	}
	public void setDataDirector(String director){
		if(director == null){
			return;
		}
		mDataDirector = director;
	}
	
	public String getDataDirector(){
		return mDataDirector;
	}
}
