package com.is2.mygestorapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Entidad "User"
 */

public class Uh{
    private int idUs;
    private String tituloUs;
	private String descripcionActividad;
    private String estado;
	private String fechaInicio;
    private String fechaFin;
    private User idUsuario;

	public Uh(JSONObject uh) throws JSONException {
        this.idUs = uh.getInt("idUs");
        this.tituloUs = uh.getString("tituloUs");
	    this.descripcionActividad = uh.getString("descripcionActividad");
        this.estado = uh.getString("estado");
        this.fechaInicio = uh.getString("fechaInicio");
        this.fechaFin = uh.getString("fechaFin");
        this.idUsuario = new User(uh.getJSONObject("idUsuario"));
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
    public String getFechaFin(){
        return fechaFin;

    }
    public String getDiasVto() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar fecActual = Calendar.getInstance();
        Date fechaActual, fechaVto;
        long dias = 0;

        fecActual.set(Calendar.HOUR_OF_DAY, 0);
        fecActual.set(Calendar.MINUTE, 0);
        fecActual.set(Calendar.SECOND, 0);

        fecActual.add(Calendar.DAY_OF_YEAR, -1);

        fechaActual = fecActual.getTime();

        try {
            fechaVto = dateFormat.parse(fechaFin);
            dias = ((fechaVto.getTime()-fechaActual.getTime())/86400000);
        } catch (ParseException e) {
            e.printStackTrace();
        }

	    if (dias >= 0){
            return "Quedan "+ (int) dias +" dias para finalizar la tarea." ;
        }
        else {
            return "Ya no quedan dias para finalizar la tarea." ;
        }
    }
    public User getIdUsuario(){
        return idUsuario;
    }
    public int getIdEstado(String estado) {
        switch (estado){
            case "Asignado":
                return 0;
            case "En proceso":
                return 1;
            case "Finalizado":
                return 2;
            default:
                return -1;
        }
    }
}

