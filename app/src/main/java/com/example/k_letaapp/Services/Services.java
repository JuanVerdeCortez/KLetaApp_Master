package com.example.k_letaapp.Services;

import com.example.k_letaapp.Entity.Login;
import com.example.k_letaapp.Entity.MensajeResponseType;
import com.example.k_letaapp.Entity.Usuario;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Services {

    @POST("login")
    Call<String> login(@Body Login login);

    @FormUrlEncoded
    @POST("nuevo")
    Call<MensajeResponseType> registrarUsuario(@FieldMap Map<String,String> map);
}
