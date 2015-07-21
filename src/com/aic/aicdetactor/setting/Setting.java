package com.aic.aicdetactor.setting;

import java.io.File;

import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.util.SystemUtil;

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
	private String mMediaDirector = null;

	public Setting() {
		mDataDirector = Environment.getExternalStorageDirectory() + "/AIC/";

		//mDataDirector = "/sdcard" + "/AIC/";
		File destDir = new File(mDataDirector);
		if (!destDir.exists()) {
			destDir.mkdirs();
			destDir = null;
		}

		mMediaDirector = mDataDirector + "media/";
		destDir = new File(mMediaDirector);
		if (!destDir.exists()) {
			destDir.mkdirs();
			destDir = null;
		}

		destDir = new File(mMediaDirector + "audio/");
		if (!destDir.exists()) {
			destDir.mkdirs();
			destDir = null;
		}

		destDir = new File(mMediaDirector + "picture/");
		if (!destDir.exists()) {
			destDir.mkdirs();
			destDir = null;
		}

		destDir = new File(mMediaDirector + "txt/");
		if (!destDir.exists()) {
			destDir.mkdirs();
			destDir = null;
		}
	}
//	public void setData_Root_Director(String director){
//		if(director == null){
//			return;
//		}
//		mDataDirector = director;
//	}
	
	public String getData_Root_Director(){
		return mDataDirector;
	}
	
	/**
	 * 根据文件类型，返回不同的文件路径
	 * @param fileType 0：拍照图片路径，1：录音文件路径，2：快速记录文件类型
	 * @return
	 */
	public String getData_Media_Director(int fileType){
		String path = null;
		switch(fileType){
		case CommonDef.FILE_TYPE_PICTRUE:
			path = mMediaDirector +"picture/";
			break;
		case CommonDef.FILE_TYPE_AUDIO:
			path = mMediaDirector +"audio/";
			break;
		case CommonDef.FILE_TYPE_TEXTRECORD:
			path = mMediaDirector +"txt/";
			break;
		default :
			path = mMediaDirector;
			break;
		}
		
		File destDir = new File(path);
		if (!destDir.exists()) {
			destDir.mkdirs();
			destDir = null;
		}
		
		return path;
	}
	
	public void clearMediaDirector(int fileType){
		String path = null;
		switch(fileType){
		case CommonDef.FILE_TYPE_PICTRUE:
			path = mMediaDirector +"picture/";
			break;
		case CommonDef.FILE_TYPE_AUDIO:
			path = mMediaDirector +"audio/";
			break;
		case CommonDef.FILE_TYPE_TEXTRECORD:
			path = mMediaDirector +"txt/";
			break;
		default :
			path = mMediaDirector;
			break;
		}
		SystemUtil.DeleteFolder(path);
	}
	
	
}
