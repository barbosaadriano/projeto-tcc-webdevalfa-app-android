package br.com.avantagem.appavws;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.app.Activity;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class WebViewer extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_viewer);
        WebView wv = (WebView) findViewById(R.id.webViewChart);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);
        wv.addJavascriptInterface(this,"Android");
        wv.loadUrl("file:///android_asset/chart.html");
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @JavascriptInterface
    public String getDados(){
        return "[[1,12.5],[2,10.5]]";
    }
}
