package com.example.k_letaapp.Util;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.Date;

public class Constantes {
    public static class General {

        public static java.sql.Date fechaActual(){
            Date fecha = new Date();
            java.sql.Date fechaSQL = new java.sql.Date(fecha.getTime());
            return fechaSQL;
        }

        public static class Mensaje {
            public static String Exito = "MSG001";
            public static String Error = "MSG002";
        }

        public static class Validacion {

            public static boolean isValidEmail(String email){
                return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
            }

            public static boolean isValidPassword(String password){
                return password.length() > 4;
            }
        }
    }
}
