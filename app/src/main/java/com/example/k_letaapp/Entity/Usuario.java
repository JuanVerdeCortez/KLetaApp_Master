package com.example.k_letaapp.Entity;

public class Usuario {
    private String nombre;
    private String apellido;
    private String documento;
    private String genero;
    private String correo;
    private String password;
    private String imagen;

    public Usuario (){

    }

    public Usuario(String nombre, String apellido, String documento, String genero, String correo, String password, String imagen) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.documento = documento;
        this.genero = genero;
        this.correo = correo;
        this.password = password;
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
