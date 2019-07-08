package com.example.k_letaapp;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.k_letaapp.Entity.MensajeResponseType;
import com.example.k_letaapp.Services.Services;
import com.example.k_letaapp.Util.Constantes;
import com.example.k_letaapp.Util.Property;
import com.google.android.gms.location.LocationListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistroUsuarioActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener{
    private EditText etNombres,etApellidos,etDni,etEmail,etContrasenia,etContraseniaConfirm;
    private Button btnRegistrar,btnCancelar;
    private Spinner spSexo;
    private Services service;
    private Retrofit restAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);
        setTitle("Registro de Usuarios");
        UIBind();
        btnRegistrar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
    }

    private void UIBind(){
        etNombres = (EditText)findViewById(R.id.etNombres);
        etApellidos= (EditText)findViewById(R.id.etApellidos);
        etDni = (EditText)findViewById(R.id.etDni);
        etEmail = (EditText)findViewById(R.id.etEmail);
        spSexo = (Spinner)findViewById(R.id.spSexo);
        etContrasenia = (EditText)findViewById(R.id.etContrasenia);
        etContraseniaConfirm = (EditText)findViewById(R.id.etContraseniaConfirm);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrar);
        btnCancelar = (Button) findViewById(R.id.btnCancelarRegistro);


        Map<String,String> mapaSexo = new HashMap<>();
        mapaSexo.put("M","Masculino");
        mapaSexo.put("F","Femenino");
        ArrayList<String> lista = new ArrayList<String>();
        lista.add("Masculino");
        lista.add("Femenino");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,lista);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSexo.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegistrar:
                registrarUsuario();
                break;
            case R.id.btnCancelarRegistro:
                cancelarRegistro();
                break;
        }
    }



    public void registrarUsuario(){

        if (TextUtils.isEmpty(etNombres.getText())) {
            Toast.makeText(this, "Ingrese nombres completos", Toast.LENGTH_SHORT).show();
            return;
        } else if (etNombres.getText().length() > 50){
                Toast.makeText(this,"El nombre solo puede tener 50 caracteres de longitud como maximo",Toast.LENGTH_SHORT).show();
                return;
        } else if (etApellidos.getText().length() > 50) {
            Toast.makeText(this, "El apellido solo puede tener 50 caracteres de longitud como maximo", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(etApellidos.getText())) {
            Toast.makeText(this, "Ingrese apellidos completos", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(etDni.getText())) {
            Toast.makeText(this, "Ingrese el documento de identidad", Toast.LENGTH_SHORT).show();
            return;
        } else if (etDni.getText().toString().length() != 8) {
            Toast.makeText(this, "El documento de identidad debe tener 8 caracteres numericos", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(etEmail.getText())){
            Toast.makeText(this,"Ingrese un email valido",Toast.LENGTH_SHORT).show();
            return;
        } else if (!Constantes.General.Validacion.isValidEmail(etEmail.getText().toString())){
            Toast.makeText(this,"Email no valido : ejemplo@dominio.com",Toast.LENGTH_SHORT).show();
            return;
        }  else if (!Constantes.General.Validacion.isValidPassword(etContrasenia.getText().toString())) {
            Toast.makeText(this, "Contraseña no valida, 6 caracteres como minimo y maximo 12. Por favor intente de nuevo", Toast.LENGTH_SHORT).show();
            return;
        } else if (!Constantes.General.Validacion.isValidPassword(etContraseniaConfirm.getText().toString())) {
            Toast.makeText(this, "Contraseña de confirmacion no valida, 6 caracteres como minimo y maximo 12. Por favor intente de nuevo", Toast.LENGTH_SHORT).show();
            return;
        }  else if (!etContrasenia.getText().toString().equals(etContraseniaConfirm.getText().toString())) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        } else {
            String value = "";
            String spinnerValue = spSexo.getSelectedItem().toString();
            if (spinnerValue.equals("Masculino")){
              value = "M";
            } else{
                value = "F";
            }
            Map<String, String> map = new HashMap<>();
            map.put("nombre", etNombres.getText().toString());
            map.put("apellido",etApellidos.getText().toString());
            map.put("documento",etDni.getText().toString());
            map.put("genero",value);
            map.put("correo",etEmail.getText().toString());
            map.put("password",etContrasenia.getText().toString());
            map.put("fecha_alta", Constantes.General.fechaActual().toString());
            map.put("fecha_modificacion",Constantes.General.fechaActual().toString());

            validarRegistroUsuario(map);
        }

    }

    public void validarRegistroUsuario(Map<String,String> usuarioMap){

        restAdapter = new Retrofit.Builder()
                .baseUrl(Property.URL.HOST_REGISTRO)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = restAdapter.create(Services.class);
        System.out.println("verificar");
        Call<MensajeResponseType> servicio = service.registrarUsuario(usuarioMap);
        servicio.enqueue(new Callback<MensajeResponseType>(){

            @Override
            public void onResponse(Call<MensajeResponseType> call, Response<MensajeResponseType> response) {

                if (response.isSuccessful()) {
                    MensajeResponseType mensajeRespuesta = response.body();

                    if (mensajeRespuesta.getCodigo().equals(Constantes.General.Mensaje.Exito)){
                        Toast.makeText(RegistroUsuarioActivity.this, "Registro satisfactorio, por favor ingrese al aplicativo" , Toast.LENGTH_LONG);
                        irLogin();
                    } else if (mensajeRespuesta.getCodigo().equals(Constantes.General.Mensaje.Error)){
                        Toast.makeText(RegistroUsuarioActivity.this, "No se pudo registrar el usuario, intentelo mas tarde" , Toast.LENGTH_SHORT);
                        return;
                    }

                } else {
                    String error = "";
                    if (response.errorBody()
                            .contentType()
                            .subtype()
                            .equals("application/json")) {

                        error = response.message();
                        System.out.println(error);
                        Toast.makeText(RegistroUsuarioActivity.this, "Error al recibir peticion del servidor" , Toast.LENGTH_SHORT);
                    }}
            }

            @Override
            public void onFailure(Call<MensajeResponseType> call, Throwable t) {
                Toast.makeText(RegistroUsuarioActivity.this,"No se pudo conectar con el servidor, por favor intentelo mas tarde" , Toast.LENGTH_SHORT).show();
                System.out.println("Error Retrofit " + t.getMessage() + " " + t.getCause());
            }
        });
    }

    public void irLogin(){
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void cancelarRegistro(){
        super.onBackPressed();
        finish();
    }

}

