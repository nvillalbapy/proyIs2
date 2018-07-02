package com.is2.mygestorapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Rol {

    private Integer idRol;
    private String nombreRol;

    public static JSONObject Rol;

    public Rol(JSONObject user) throws JSONException {
        this.idRol = user.getInt("idRol");
        this.nombreRol = user.getString("nombreRol");
    }

    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }
}
