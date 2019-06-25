package com.example.k_letaapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.k_letaapp.Entity.Login;
import com.example.k_letaapp.Services.Services;
import com.example.k_letaapp.Util.Constantes;
import com.example.k_letaapp.Util.Property;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etUsername,etPassword;
    private Button btnLogin,btnCancelar;
    private RequestQueue requestQueue;
    private static final String TAG = "LoginActivity";
    private JSONArray jsonArray;
    private ProgressBar progressBar;
    public static final String KEY_USERNAME = "email";
    public static final String KEY_PASSWORD = "password";

    private Retrofit restAdapter;
    private Services service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setTitle(R.string.titulo_login);
        bindUI();
        btnLogin.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);


    }

    private void bindUI(){
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etContrasenia);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                login(etUsername.getText().toString(),etPassword.getText().toString());
                break;
            case R.id.btnCancelar:
                cancelar();
                break;
        }
    }



    private void login(String email , String password){
        if (!Constantes.General.Validacion.isValidEmail(email)){
            Toast.makeText(this,"Usuario (Email) no valido, por favor intente de nuevo",Toast.LENGTH_SHORT).show();
            return;
        }else if (!Constantes.General.Validacion.isValidPassword(password)){
            Toast.makeText(this,"Contraseña no valida, 4 caracteres como minimo, por favor intente de nuevo",Toast.LENGTH_SHORT).show();
            return;
        }else{
             validarLogin(email,password);
        }

    }

    private void validarLogin(final String email, final String password){
        boolean retorno = false;
        restAdapter = new Retrofit.Builder()
                .baseUrl(Property.URL.PRINCIPAL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = restAdapter.create(Services.class);

        Call<String> servicio = service.login(new Login(email,password));
        servicio.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {

                System.out.println("responde" + response.message());
                if (response.isSuccessful()) {
                    String mensajeRespuesta = response.body();
                    System.out.println("mensajeLogin " + mensajeRespuesta);

                    int respuestaJson = mensajeRespuesta.indexOf("true");
                    System.out.println(respuestaJson + " " + response + " " + email + " "  + password);

                    if (respuestaJson == -1){
                        Toast.makeText(LoginActivity.this,"Usuario no registrado, por favor registrese" ,Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        Toast.makeText(LoginActivity.this,"Bienvenido: " + email ,Toast.LENGTH_SHORT).show();
                        irMapsActivity();
                    }
                } else {
                    String error = "";
                    if (response.errorBody()
                            .contentType()
                            .subtype()
                            .equals("application/json")) {

                        error = response.message();
                        System.out.println(error);
                        Toast.makeText(LoginActivity.this, "Error al recibir peticion del servidor" , Toast.LENGTH_SHORT);
                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(LoginActivity.this,"No se pudo conectar con el servidor, por favor intentelo mas tarde" , Toast.LENGTH_SHORT).show();
                System.out.println("Error Retrofit " + t.getMessage() + " " + t.getCause());
            }
        });

    }

    private void validarLoginVolley(final String email, final String password){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Property.URL.PRINCIPAL + Property.POST.LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    System.out.println(response);
                    int respuestaJson = response.indexOf("true");
                    System.out.println(respuestaJson + " " + response + " " + email + " "  + password);

                     if (respuestaJson == -1){
                        Toast.makeText(LoginActivity.this,"Usuario no registrado, por favor registrese" ,Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(LoginActivity.this,"Bienvenido: " + email ,Toast.LENGTH_SHORT).show();
                        irMapsActivity();

                    }
                } catch (Exception ex) {
                    Log.e(TAG,ex.getMessage());
                    System.out.println("Error " + ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this,"No se pudo conectar con el servidor, por favor intentelo mas tarde" , Toast.LENGTH_SHORT).show();
                System.out.println("Error Volley " + error.getMessage() + " " + error.getCause());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put(KEY_USERNAME,email);
                params.put(KEY_PASSWORD,password);
                return params;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void cancelar(){
        super.onBackPressed();
        finish();
    }

    public void irMapsActivity(){
        Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
        startActivity(intent);
        this.finish();
    }

    class LoginTask extends AsyncTask<String,Void,Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            progressBar.setVisibility(View.VISIBLE);
            login(etUsername.getText().toString(),etPassword.getText().toString());
            return null;
        }
    }




}
