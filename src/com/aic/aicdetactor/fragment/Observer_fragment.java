package com.aic.aicdetactor.fragment;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aic.aicdetactor.R;
import com.aic.aicdetactor.check.PartItemActivity.OnButtonListener;
import com.aic.aicdetactor.comm.CommonDef;
import com.aic.aicdetactor.data.KEY;


public class Observer_fragment extends Fragment implements OnButtonListener{

	//listview
	private ListView mListview = null;
	//示意图片显示
	private ImageView mImageView = null;
	
	//存储相机拍照的图片路径
	private String mPicturePath = null;
	
	//存储录音文件的路径
	private String mSoundPath = null;
	
	//存储文字记录信息文件路径
	private String mTextRecordPath = null;
	
	//之间的通信接口
	private OnMediakListener mCallback = null;
	String parStr = null;
	EditText mExternalInfoEditText = null;
	private TextView mDeviceNameTextView = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		parStr =getArguments().getString(KEY.KEY_PARTITEMDATA);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.observer, container, false);
		mListview = (ListView)view.findViewById(R.id.listView1);
		mImageView = (ImageView)view.findViewById(R.id.imageView1);	
		mDeviceNameTextView = (TextView)view.findViewById(R.id.check_name);
		mDeviceNameTextView.setText(parStr);
		mExternalInfoEditText = (EditText)view.findViewById(R.id.editText1);	
		parseExternalInfo();
		return view;
	}

	
    public interface OnMediakListener{
    	
    	void OnClick(String IndexButton);
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // 这是为了保证Activity容器实现了用以回调的接口。如果没有，它会抛出一个异常。
        try {
            mCallback = (OnMediakListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMediakListener");
        }
    }

    void parseExternalInfo(){
    	String[] array = parStr.split(KEY.PARTITEMDATA_SPLIT_KEYWORD);
		String newValue = array[CommonDef.partItemData_Index.PARTITEM_ADDITIONAL_INFO];
		mExternalInfoEditText.setText(newValue);
    }
	@Override
	public void OnButtonDown(int buttonId, Bundle bundle) {
		// TODO Auto-generated method stub
		if(buttonId == 0){
		Uri	imageFilePath = Uri.parse(bundle.getString("pictureUri"));
			 try {  
		           // Bundle extra = data.getExtras(); 
		          //  Bitmap bmp = (Bitmap)data.getExtras().get("data");
		            Log.d("test", "onActivityResult()  1"); 
		            /** 
		             * 然而为了节约内存的消耗，这里返回的图片是一个121*162的缩略图。 
		             * 那么如何返回我们需要的大图呢？看上面 
		             * 然而存储了图片。有了图片的存储位置，能不能直接将图片显示出来呢》 
		             * 这个问题就设计到对于图片的处理和显示，是非常消耗内存的，对于PC来说可能不算什么，但是对于手机来说 
		             * 很可能使你的应用因为内存耗尽而死亡。不过还好，Android为我们考虑到了这一点 
		             * Android中可以使用BitmapFactory类和他的一个内部类BitmapFactory.Options来实现图片的处理和显示 
		             * BitmapFactory是一个工具类，里面包含了很多种获取Bitmap的方法。BitmapFactory.Options类中有一个inSampleSize，比如设定他的值为8，则加载到内存中的图片的大小将 
		             * 是原图片的1/8大小。这样就远远降低了内存的消耗。 
		             * BitmapFactory.Options op = new BitmapFactory.Options(); 
		             * op.inSampleSize = 8; 
		             * Bitmap pic = BitmapFactory.decodeFile(imageFilePath, op); 
		             * 这是一种快捷的方式来加载一张大图，因为他不用考虑整个显示屏幕的大小和图片的原始大小 
		             * 然而有时候，我需要根据我们的屏幕来做相应的缩放，如何操作呢？ 
		             *  
		             */  
		            //首先取得屏幕对象  
		            Display display = this.getActivity().getWindowManager().getDefaultDisplay();  
		            //获取屏幕的宽和高  
		            int dw = display.getWidth();  
		            int dh = display.getHeight();  
		            /** 
		             * 为了计算缩放的比例，我们需要获取整个图片的尺寸，而不是图片 
		             * BitmapFactory.Options类中有一个布尔型变量inJustDecodeBounds，将其设置为true 
		             * 这样，我们获取到的就是图片的尺寸，而不用加载图片了。 
		             * 当我们设置这个值的时候，我们接着就可以从BitmapFactory.Options的outWidth和outHeight中获取到值 
		             */  
		            BitmapFactory.Options op = new BitmapFactory.Options(); 
		            Log.d("test", "onActivityResult()  2"); 
		            //op.inSampleSize = 8;  
		            op.inJustDecodeBounds = true;  
		            //Bitmap pic = BitmapFactory.decodeFile(imageFilePath, op);//调用这个方法以后，op中的outWidth和outHeight就有值了  
		            //由于使用了MediaStore存储，这里根据URI获取输入流的形式  
		            Bitmap pic = BitmapFactory.decodeStream(this.getActivity()
		                    .getContentResolver().openInputStream(imageFilePath),  
		                    null, op);  
		            int wRatio = (int) Math.ceil(op.outWidth / (float) dw); //计算宽度比例  
		            int hRatio = (int) Math.ceil(op.outHeight / (float) dh); //计算高度比例  
		            Log.d("test", wRatio + "wRatio");  
		            Log.d("test", hRatio + "hRatio");  
		            /** 
		             * 接下来，我们就需要判断是否需要缩放以及到底对宽还是高进行缩放。 
		             * 如果高和宽不是全都超出了屏幕，那么无需缩放。 
		             * 如果高和宽都超出了屏幕大小，则如何选择缩放呢》 
		             * 这需要判断wRatio和hRatio的大小 
		             * 大的一个将被缩放，因为缩放大的时，小的应该自动进行同比率缩放。 
		             * 缩放使用的还是inSampleSize变量 
		             */  
		            Log.d("test", "onActivityResult()  3"); 
		            if (wRatio > 1 && hRatio > 1) {  
		                if (wRatio > hRatio) {  
		                    op.inSampleSize = wRatio;  
		                } else {  
		                    op.inSampleSize = hRatio/2;  
		                }  
		            }  
		            op.inJustDecodeBounds = false; //注意这里，一定要设置为false，因为上面我们将其设置为true来获取图片尺寸了  
		            pic = BitmapFactory.decodeStream(this.getActivity().getContentResolver()  
		                    .openInputStream(imageFilePath), null, op);  
		          
		            mImageView.setImageBitmap(pic);  
		        } catch (Exception e) {  
		        	Log.d("test","main_media Exception "+e.toString());
		            e.printStackTrace();  
		        }  
			 
			 mCallback.OnClick(imageFilePath.toString() + "*");
		}else{
			mCallback.OnClick(mExternalInfoEditText.getText().toString());
		}
	}

}
