package br.com.avantagem.appavws;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Graph extends AppCompatActivity {
    GraphView graph;
    String serviceURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        graph = (GraphView) findViewById(R.id.graph);
        Intent intent = getIntent();
        Bundle params = intent.getExtras();
        if(params!=null)
        {
            serviceURL = params.getString("url");
        }
        mostrarGrafico();
    }

    public void mostrarGrafico(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, serviceURL+"interface-controller/get-lasts-24-kwh/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final List<EventosListaModelo> lst = new ArrayList<EventosListaModelo>();
                            lst.clear();
                            JSONObject json = new JSONObject(response);
                            JSONArray evts = json.getJSONArray("dados");
                            DataPoint[] datap = new DataPoint[evts.length()];
                            for (int i =0; i<evts.length();i++) {
                                datap[i]= new DataPoint(
                                        //Double.parseDouble(evts.getJSONObject(i).getString("hora"))
                                        i
                                        ,Double.parseDouble(evts.getJSONObject(i).getString("consumo"))
                                );
                            }
                            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(datap);
                            series.setTitle("Kw/H últimas 24H");
                            series.setDrawDataPoints(true);
                            series.setDataPointsRadius(1);
                            graph.addSeries(series);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        SingletonRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
