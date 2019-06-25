package com.example.k_letaapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnIrRegistro,btnIrLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnIrRegistro = (Button)findViewById(R.id.btnIrRegistro);
        btnIrLogin = (Button)findViewById(R.id.btnIrLogin);

        btnIrRegistro.setOnClickListener(this);
        btnIrLogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnIrRegistro:
                irActivityRegistroUsuario();
                break;
            case R.id.btnIrLogin:
                irActivityLogin();
                break;
        }
    }

    private void irActivityLogin(){
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
    }

    private void irActivityRegistroUsuario(){
        Intent intent = new Intent(getApplicationContext(),RegistroUsuarioActivity.class);
        startActivity(intent);
    }

}
