package br.com.avantagem.appavws;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

public class LastEvents extends AppCompatActivity implements Runnable, AdapterView.OnItemClickListener {
    String serviceURL;
    ListView lista;
    HashMap<Double,Double> ultimosConsumos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_events);
        lista = (ListView) findViewById(R.id.lista);
        Intent intent = getIntent();
        Bundle params = intent.getExtras();
        ultimosConsumos = new HashMap<Double, Double>();
        if(params!=null)
        {
            serviceURL = params.getString("url");
        }
        new Handler().postDelayed(this,5000);
    }

    @Override
    public void run() {
        listar();
    }

    private void listar(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, serviceURL+"interface-controller/get-lasts-event-registred/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final List<EventosListaModelo> lst = new ArrayList<EventosListaModelo>();
                            lst.clear();
                            JSONObject json = new JSONObject(response);
                            JSONArray evts = json.getJSONArray("dados");
                            for (int i =0; i<evts.length();i++) {
                                EventosListaModelo evt = new EventosListaModelo(
                                        evts.getJSONObject(i).getString("descricao"),
                                        evts.getJSONObject(i).getString("momento"),
                                        evts.getJSONObject(i).getString("dados"),
                                        evts.getJSONObject(i).getString("dispositivo"),
                                        evts.getJSONObject(i).getString("local")
                                );
                                lst.add(evt);
                            }
                            desenhaLista(lst);
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
    private void desenhaLista(List<EventosListaModelo> l){
        AdrianoAdapter adapter =  new AdrianoAdapter(l,this);
        lista.setOnItemClickListener(this);
        lista.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mostraGrafico();
    }
    public void mostraGrafico(){
        Intent intent = new Intent(this,Graph.class);
        Bundle params = new Bundle();
        params.putString("url",serviceURL);
        intent.putExtras(params);
        startActivity(intent);
    }
}
