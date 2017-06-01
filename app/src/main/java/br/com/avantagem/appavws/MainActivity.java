package br.com.avantagem.appavws;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements Runnable, View.OnClickListener {

    public static final String APP_CONFIG = "AppAvWsConfig";
    private String serviceURL = "";
    private Handler handler;
    private Button bt1 ;
    private Button bt2 ;
    private Button bt3 ;
    private Button bt4 ;
    private Button btnTc;
    private  TextView tvConnectionStatus;
    Intent telaEvents;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        telaEvents = new Intent(this,LastEvents.class);
        checkServiceURL();
        Button btnConfig = (Button) findViewById(R.id.btnConfig);
        bt1 = (Button) findViewById(R.id.btnPorta1);
        bt1.setOnClickListener(this);
        bt2 = (Button) findViewById(R.id.btnPorta2);
        bt2.setOnClickListener(this);
        bt3 = (Button) findViewById(R.id.btnPorta3);
        bt3.setOnClickListener(this);
        bt4 = (Button) findViewById(R.id.btnPorta4);
        bt4.setOnClickListener(this);
        tvConnectionStatus = (TextView) findViewById(R.id.tvConnectionStatus);
        Button btnLista = (Button) findViewById(R.id.btnLista);
        btnLista.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                checkServiceURL();
                Bundle params = new Bundle();
                params.putString("url",serviceURL);
                telaEvents.putExtras(params);
                startActivity(telaEvents);
            }
        });
        btnConfig.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                callConfig();
            }
        });
        verificaStatusDasPortas();
        handler = new Handler();
        handler.postDelayed(this,10000);
    }
    private void checkServiceURL(){
        SharedPreferences settings = getSharedPreferences(APP_CONFIG, 0);
        serviceURL = settings.getString("serviceURL", "");
        if (serviceURL.isEmpty()){
           callConfig();
        }
    }
    /* Função para verificar existência de conexão com a internet
	 */
    public  boolean verificaConexao() {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            conectado = true;
            tvConnectionStatus.setText("CONECTADO");
            tvConnectionStatus.setBackgroundColor(Color.parseColor("#68e096"));
        } else {
            conectado = false;
            tvConnectionStatus.setText("DESCONECTADO");
            tvConnectionStatus.setBackgroundColor(Color.parseColor("#e06868"));
        }
        return conectado;
    }
    private void callConfig(){
        Intent config = new Intent(this,Configuration.class);
        startActivity(config);
    }

    @Override
    public void run() {
        if (verificaConexao()) {
            verificaStatusDasPortas();
        }
        handler.postDelayed(this,10000);
    }

    private void updatePortStatus(int port, String status) {
        TextView tv = null;
        Button btn = null;
        switch (port) {
            case 1:
                tv = (TextView) findViewById(R.id.tvStatusPorta1);
                btn = (Button) findViewById(R.id.btnPorta1);
                break;
            case 2:
                tv = (TextView) findViewById(R.id.tvStatusPorta2);
                btn = (Button) findViewById(R.id.btnPorta2);
                break;
            case 3:
                tv = (TextView) findViewById(R.id.tvStatusPorta3);
                btn = (Button) findViewById(R.id.btnPorta3);
                break;
            case 4:
                tv = (TextView) findViewById(R.id.tvStatusPorta4);
                btn = (Button) findViewById(R.id.btnPorta4);
                break;

        }
        if (!tv.equals(null)) {
            tv.setText(status);
            if (status.equals("desligada"))
                tv.setBackgroundColor(Color.RED);
            if(status.equals("ligada"))
                tv.setBackgroundColor(Color.GREEN);
        }
        if (!btn.equals(null)) {
            if (status.equals("desligada"))
                btn.setText("LIGAR");
            if(status.equals("ligada"))
                btn.setText("DESLIGAR");
        }
    }

    private void verificaStatusDasPortas(){
        checkServiceURL();
        if (!serviceURL.isEmpty()) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, serviceURL+"interface-controller/get-ports-status/",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json = new JSONObject(response);
                                JSONArray portas = json.getJSONArray("ports");
                                for (int i =0; i<portas.length();i++) {
                                    updatePortStatus(
                                            portas.getJSONObject(i).getInt("id"),
                                            portas.getJSONObject(i).getString("status")
                                            );
                                }
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

    @Override
    public void onClick(View v) {
        int port = 0;
        String action = "";
        if (v.equals(bt1)) {
            port = 1;
        }
        if (v.equals(bt2)) {
            port = 2;
        }
        if (v.equals(bt3)) {
            port = 3;
        }
        if (v.equals(bt4)) {
            port = 4;
        }
        Button b = (Button) v;
        if (b.getText().equals("LIGAR"))
            action = "ligar";
        if (b.getText().equals("DESLIGAR"))
            action = "desligar";
        enviarComando(port,action);
    }
    private void enviarComando(int port, String action) {
        checkServiceURL();
        if (!serviceURL.isEmpty()) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    serviceURL+"interface-controller/registre-port-action/?port="+port+"&action="+action,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json = new JSONObject(response);
                               if (json.getString("status").equals("OK")) {
                                   Toast.makeText(getApplicationContext(), "Ação registrada", Toast.LENGTH_SHORT).show();
                                   verificaStatusDasPortas();
                               } else {
                                   Toast.makeText(getApplicationContext(), "Falha ao registrar a ação", Toast.LENGTH_SHORT).show();
                               }
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
}