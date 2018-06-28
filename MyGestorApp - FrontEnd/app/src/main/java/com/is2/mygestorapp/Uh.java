package com.is2.mygestorapp;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Entidad "User"
 */

public class Uh{
    private int idUs;
    private String tituloUs;
	private String descripcionActividad;
    private String estado;
	private String fechaInicio;
    //private String fechaFin;
    //private int idUsuario;

	public Uh(JSONObject uh) throws JSONException {
        this.idUs = uh.getInt("idUs");
        this.tituloUs = uh.getString("tituloUs");
	    this.descripcionActividad = uh.getString("descripcionActividad");
        this.estado = uh.getString("estado");
        this.fechaInicio = uh.getString("fechaInicio");
       //this.fechaFin = uh.getString("fechaFin");
        //this.idUsuario = uh.getInt("idUsuario");
	}

	public int getIdUs(){
        return idUs;
    }
	public String getTituloUs(){
		return tituloUs;
	}
    public String getDescripcionActividad(){
        return descripcionActividad;
    }
    public String getEstado(){
        return estado;
    }
	public String getFechaInicio(){
		return fechaInicio;

	}
    /*public String getFechaFin(){
        return fechaFin;

    }
    public int getIdUsuario(){
        return idUsuario;
    }*/
}

