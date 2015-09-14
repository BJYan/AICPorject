package com.aic.aicdetactor;


import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
// import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

public class AboutDialog extends Dialog {
  // Log
  // private static final String TAG = "AboutDialog";

  private Context mContext;
  private static AboutDialog mDialog;
  private static OkListener mOkListener;
  private final String errorHTML = "<html><body><h1>Failed to load web page</h1></body></html>";

  public AboutDialog(Context context) {
    super(context);
    mContext = context;
    mDialog = this;
    mOkListener = new OkListener();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.dialog_about);

    // From About.html web page
    WebView webView = (WebView) findViewById(R.id.web_content);
    webView.setWebViewClient(new WebViewClient(){
      
    	@Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
          view.loadUrl(url);
          return false;
      }
      
    	@Override
    	public void onPageFinished(WebView view, final String url) {
    		// Log.i(TAG,"Web page loaded: " + url);
    	}

    	@Override
    	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
    		// Do something
    		view.loadData(errorHTML, "text/html", "UTF-8");
    		// Log.e(TAG,"Failed to load web page");
    	}
    });

    // Header
    Resources res = mContext.getResources();
    String appName = res.getString(R.string.app_name);
    TextView title = (TextView) findViewById(R.id.title);
    title.setText("About " + appName);

    // Application info
    TextView head = (TextView) findViewById(R.id.header);
    String appVersion = "Revision: ";
    try {
      appVersion += mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
    } catch (NameNotFoundException e) {
      // Log.v(TAG, e.getMessage());
    }
    head.setText(appVersion);

    // Dismiss button
    Button okButton = (Button) findViewById(R.id.buttonOK);
    okButton.setOnClickListener(mOkListener);

    // Device information
    TextView foot = (TextView) findViewById(R.id.footer);
    String txt = Build.MANUFACTURER.toUpperCase() + " " + Build.MODEL + " Android " + Build.VERSION.RELEASE + " " + Build.DISPLAY;

    foot.setText(txt);
  }

  private class OkListener implements android.view.View.OnClickListener {
    @Override
    public void onClick(View v) {
      mDialog.dismiss();
    }
  }
}
