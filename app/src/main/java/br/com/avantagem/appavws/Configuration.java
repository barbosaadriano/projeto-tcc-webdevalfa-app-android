package br.com.avantagem.appavws;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Configuration extends AppCompatActivity {

    private EditText edRootServiceUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        Button btnSalvar = (Button) findViewById(R.id.btnSalvarConfig);
        edRootServiceUrl = (EditText) findViewById(R.id.edRootServiceUrl);
        SharedPreferences settings = getSharedPreferences(MainActivity.APP_CONFIG, 0);
        String url = settings.getString("serviceURL", "");
        edRootServiceUrl.setText(url);
        btnSalvar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences(MainActivity.APP_CONFIG, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("serviceURL", edRootServiceUrl.getText().toString());
                editor.commit();
                finish();
            }
        });
    }
}
