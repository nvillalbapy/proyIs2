package com.is2.mygestorapp;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Entidad "User"
 */

public class User {
    private int idUsuario;
    private String nombre;
    private String usuario;
    private String password;
    private String mail;
    public static JSONObject Usuario;

    public User(JSONObject user) throws JSONException {
        this.idUsuario = user.getInt("idUsuario");
        this.nombre = user.getString("nombre");
        this.usuario = user.getString("usuario");
        this.password = user.getString("password");
        this.mail = user.getString("mail");
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getPassword() {
        return password;
    }

    public String getMail() {
        return mail;
    }
}
