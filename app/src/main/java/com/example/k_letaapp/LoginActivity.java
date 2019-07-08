package com.example.k_letaapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.Nullable;
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
import com.example.k_letaapp.Entity.RetornarMensajeOnResponseCallBacks;
import com.example.k_letaapp.Entity.Usuario;
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
    private ProgressBar pbLogin;
    public static final String KEY_USERNAME = "email";
    public static final String KEY_PASSWORD = "password";
    private SharedPreferences sharedPreferences;
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
        sharedPreferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        etUsername.setText("juanverdec@gmail.com");
        etPassword.setText("12345678");
    }

    private void bindUI(){
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etContrasenia);
        pbLogin = (ProgressBar)findViewById(R.id.pbLogin);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if (validarUI(username,password)) {
                    validarLogin(username,password);
                }
                break;
            case R.id.btnCancelar:
                cancelar();
                break;
        }
    }



    private boolean validarUI(String email , String password){
        boolean respuesta = false;
        if (!Constantes.General.Validacion.isValidEmail(email)){
            Toast.makeText(this,"Usuario (Email) no valido, por favor intente de nuevo",Toast.LENGTH_SHORT).show();
            respuesta = false;
        }else if (!Constantes.General.Validacion.isValidPassword(password)){
            Toast.makeText(this,"Contraseña no valida, 6 caracteres como minimo y maximo 12. Por favor intente de nuevo",Toast.LENGTH_SHORT).show();
            respuesta = false;
        } else{
             respuesta = true;
        }
        return respuesta;
    }


    private String validarLogin(final String email, final String password){
        final String[] mensajeRespuesta = {""};
        restAdapter = new Retrofit.Builder()
                .baseUrl(Property.URL.PRINCIPAL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = restAdapter.create(Services.class);

        Call<String> servicio = service.login(new Login(email,password));
        servicio.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                mensajeRespuesta[0] = response.message();
                System.out.println("responde" + response.message());
                if (response.isSuccessful()) {
                    String respuesta = response.body();
                    System.out.println("mensajeLogin " + mensajeRespuesta[0]);

                    int respuestaJson = respuesta.indexOf("true");
                    System.out.println(respuestaJson + " " + response + " " + email + " "  + password);

                    if (respuestaJson == -1){
                        Toast.makeText(LoginActivity.this,"Usuario no registrado, por favor registrese" ,Toast.LENGTH_SHORT).show();
                       // mensajeRespuesta[0] = "Usuario no registrado, por favor registrese";
                    }else {
                        //mensajeRespuesta[0] = "Bienvenido: " + email;
                        Toast.makeText(LoginActivity.this,"Bienvenido: " + email ,Toast.LENGTH_SHORT).show();
                        irMapsActivity();
                        obtenerDatosUsuario(email);
                    }
                } else {
                    String error =  response.message();
                        System.out.println(error);
                        Toast.makeText(LoginActivity.this, "Error al recibir peticion del servidor" + error, Toast.LENGTH_SHORT);
                        //mensajeRespuesta[0] = "Error al recibir peticion del servidor";
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("Error Retrofit " + t.getMessage() + " " + t.getCause());
                mensajeRespuesta[0] = "No se pudo conectar con el servidor, por favor intentelo mas tarde";
            }
        });
         return null;
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

    private void SaveOnPreferences(String email, String password){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email",email);
        editor.putString("password" , password);
    }

    private Usuario obtenerDatosUsuario(String email){
        Usuario usuario = new Usuario();
        restAdapter = new Retrofit.Builder()
                .baseUrl(Property.URL.HOST_OBTENER_USUARIO)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = restAdapter.create(Services.class);

        Call<Usuario> servicio = service.obtenerUsuario(email);
        servicio.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, retrofit2.Response<Usuario> response) {
                if (response.isSuccessful()){
                    System.out.println("mensaje " + response.body());
                    Usuario usuario = response.body();
                    System.out.println("Respuesta Obtener Usuario");
                    System.out.println("body " + usuario.getUsuarioNombre());
                    System.out.println("mensaje " + response.message());
                }else {
                    System.out.println("mensaje " + response.message());
                }


            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                System.out.println("Error Obtener Usuario " + t.getMessage());
            }
        });
        return usuario;
    }

   /* class LoginTask extends AsyncTask<String,Void,String> {
        String value = null;
        @Override
        protected String doInBackground(String... strings) {
            try {

             value =  validarLogin(strings[0],strings[1]);

            } catch (Exception e) {
                Log.e("Error Login", e.getMessage());
            }
           return null;
        }

        @Override
        protected void onPreExecute() {
            System.out.println("onPreExecute");
            pbLogin.setVisibility(View.VISIBLE);
            btnLogin.setEnabled(false);
            btnLogin.setTextColor(Color.GRAY);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println("onPostExecute");
            pbLogin.setVisibility(View.INVISIBLE);
            btnLogin.setEnabled(true);
            btnLogin.setTextColor(Color.WHITE);
            Toast.makeText(LoginActivity.this,s,Toast.LENGTH_SHORT).show();
        }


    }*/




}
