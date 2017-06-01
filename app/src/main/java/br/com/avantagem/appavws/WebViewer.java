package br.com.avantagem.appavws;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.app.Activity;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WebViewer extends Activity implements Response.Listener<String> {
    String serviceURL;
    String dadosJS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_viewer);
        Intent intent = getIntent();
        Bundle params = intent.getExtras();
        if(params!=null)
        {
            serviceURL = params.getString("url");
        }
        dadosJS = "[]";
        mostrarGrafico();

    }
    private  void renderizar(){
        WebView wv = (WebView) findViewById(R.id.webViewChart);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);
        wv.addJavascriptInterface(this,"Android");
        wv.loadUrl("file:///android_asset/chart.html");
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    private void mostrarGrafico() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, serviceURL+"interface-controller/get-lasts-24-kwh/",
                this,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        SingletonRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    @JavascriptInterface
    public String getDados(){
        return dadosJS;
    }

    @Override
    public void onResponse(String response) {
        try {
            JSONObject json = new JSONObject(response);
            JSONArray evts = json.getJSONArray("dados");
            dadosJS = "[";
            for (int i =0; i<evts.length();i++) {
                dadosJS+="["+(i+1)+","+evts.getJSONObject(i).getString("consumo")+"]";
                if (i<(evts.length()-1)) {
                    dadosJS+=",";
                }
            }
            dadosJS+="]";
            renderizar();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
