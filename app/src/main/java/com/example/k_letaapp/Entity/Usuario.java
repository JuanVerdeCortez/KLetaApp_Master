package com.example.k_letaapp.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Usuario {

    @SerializedName("usuario_id")
    @Expose
    private String usuarioId;
    @SerializedName("usuario_nombre")
    @Expose
    private String usuarioNombre;
    @SerializedName("usuario_apellido")
    @Expose
    private String usuarioApellido;
    @SerializedName("usuario_documento")
    @Expose
    private String usuarioDocumento;
    @SerializedName("usuario_genero")
    @Expose
    private String usuarioGenero;
    @SerializedName("usuario_correo")
    @Expose
    private String usuarioCorreo;
    @SerializedName("usuario_password")
    @Expose
    private String usuarioPassword;
    @SerializedName("usuario_fecha_alta")
    @Expose
    private String usuarioFechaAlta;
    @SerializedName("usuario_fecha_modificacion")
    @Expose
    private String usuarioFechaModificacion;
    @SerializedName("imagen_id")
    @Expose
    private Object imagenId;

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public String getUsuarioApellido() {
        return usuarioApellido;
    }

    public void setUsuarioApellido(String usuarioApellido) {
        this.usuarioApellido = usuarioApellido;
    }

    public String getUsuarioDocumento() {
        return usuarioDocumento;
    }

    public void setUsuarioDocumento(String usuarioDocumento) {
        this.usuarioDocumento = usuarioDocumento;
    }

    public String getUsuarioGenero() {
        return usuarioGenero;
    }

    public void setUsuarioGenero(String usuarioGenero) {
        this.usuarioGenero = usuarioGenero;
    }

    public String getUsuarioCorreo() {
        return usuarioCorreo;
    }

    public void setUsuarioCorreo(String usuarioCorreo) {
        this.usuarioCorreo = usuarioCorreo;
    }

    public String getUsuarioPassword() {
        return usuarioPassword;
    }

    public void setUsuarioPassword(String usuarioPassword) {
        this.usuarioPassword = usuarioPassword;
    }

    public String getUsuarioFechaAlta() {
        return usuarioFechaAlta;
    }

    public void setUsuarioFechaAlta(String usuarioFechaAlta) {
        this.usuarioFechaAlta = usuarioFechaAlta;
    }

    public String getUsuarioFechaModificacion() {
        return usuarioFechaModificacion;
    }

    public void setUsuarioFechaModificacion(String usuarioFechaModificacion) {
        this.usuarioFechaModificacion = usuarioFechaModificacion;
    }

    public Object getImagenId() {
        return imagenId;
    }

    public void setImagenId(Object imagenId) {
        this.imagenId = imagenId;
    }

}