package fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.is2.mygestorapp.Homepage;
import com.is2.mygestorapp.Inicio;
import com.is2.mygestorapp.R;
import com.is2.mygestorapp.Uh;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import helper.Connection;
import helper.URLs;

public class AddUhFragment extends Fragment {

    private TextInputEditText uhTitulo, uhDescripcion, uhUsuario, uhFechaInicio, uhFechaFin;
    private FloatingActionButton acceptButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_add_uh, container, false);

        ((Homepage) getActivity()).getSupportActionBar().setTitle("Agregar tarea");

        // Presenta los campos extraidos
        uhTitulo = (TextInputEditText) view.findViewById(R.id.add_titulo);
        uhDescripcion = (TextInputEditText) view.findViewById(R.id.add_descripcion);
        uhUsuario = (TextInputEditText) view.findViewById(R.id.add_usuario_designado);
        uhFechaInicio = (TextInputEditText) view.findViewById(R.id.add_fecha_inicio);
        uhFechaFin = (TextInputEditText) view.findViewById(R.id.add_fecha_fin);

        view.findViewById(R.id.add_titulo).requestFocus();

        acceptButton = (FloatingActionButton) view.findViewById(R.id.fab_accept);

        //Si presiona el boton flotante, cancela los cambios.
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptChanges();
            }
        });

        return view;
    }

    private void acceptChanges() {
        JSONObject addParams = new JSONObject();
        JSONObject userParams = new JSONObject();
        Connection connection = new Connection();
        Context context = super.getContext();
        JSONObject usuario;
        String titulo, descripcion, estado, usuarioDesignado, fechaInicio, fechaFin;
        String msg = null;
        String url;

        // Extraemos los nuevos valores
        titulo = uhTitulo.getText().toString();
        descripcion = uhDescripcion.getText().toString();
        usuarioDesignado = uhUsuario.getText().toString();
        fechaInicio = uhFechaInicio.getText().toString();
        fechaFin = uhFechaFin.getText().toString();
        estado = "Asignado";

        url = URLs.URL_FIND;
        try {
            userParams.put("usuario", usuarioDesignado.toLowerCase());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        msg = connection.executePost(url, userParams.toString(), context);

        // Si no hubieron cambios se utilizan los valores anteriores
        if (TextUtils.isEmpty(titulo)) {
            uhTitulo.setError("La tarea debe tener un título.");
            uhTitulo.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(descripcion)) {
            uhDescripcion.setError("La tarea debe tener una descripción.");
            uhDescripcion.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(usuarioDesignado)) {
            uhUsuario.setError("La tarea debe tener un usuario asignado.");
            uhUsuario.requestFocus();
            return;
        }

        if (msg.equals("") || msg == null) {
            uhUsuario.setError("No existe el usuario.");
            uhUsuario.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(fechaInicio)) {
            uhFechaInicio.setError("La tarea debe tener una fecha de inicio.");
            uhFechaInicio.requestFocus();
            return;
        }

        if (!formatoCorreto(fechaInicio)) {
            uhFechaInicio.setError("La fecha debe tener el formato 'aaaa-mm-dd'.");
            uhFechaInicio.requestFocus();
            return;
        }

        if (comparaFechas(fechaInicio, "fechaActual") == -1) {
            uhFechaInicio.setError("La fecha de inicio no puede ser anterior a la actual.");
            uhFechaInicio.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(fechaFin)) {
            uhFechaFin.setError("La tarea debe tener una fecha de fin.");
            uhFechaFin.requestFocus();
            return;
        }

        if (!formatoCorreto(fechaFin)) {
            uhFechaFin.setError("La fecha debe tener el formato 'aaaa-mm-dd'.");
            uhFechaFin.requestFocus();
            return;
        }

        if (comparaFechas(fechaFin, fechaInicio) == -1) {
            uhFechaFin.setError("La fecha de fin no puede ser anterior a la fecha de inicio.");
            uhFechaFin.requestFocus();
            return;
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            usuario = new JSONObject(msg);
            addParams.put("tituloUs", titulo);
            addParams.put("descripcionActividad", descripcion);
            addParams.put("estado", estado);
            addParams.put("idUsuario", usuario);
            addParams.put("fechaInicio", fechaInicio + "T00:00:00-04:00");
            addParams.put("fechaFin", fechaFin + "T00:00:00-04:00");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        url = URLs.URL_CREAR_UH;
        msg = connection.executePost(url, addParams.toString(), context);
        Toast.makeText(context, "Se ha creado la tarea correctamente.", 5).show();

        getActivity().finish();
        Intent intent = new Intent(getContext(), Inicio.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private int comparaFechas(String fecha1, String fecha2){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar fec1 = Calendar.getInstance();
        Calendar fec2 = Calendar.getInstance();
        int numero;
        try {
            fec1.setTime(dateFormat.parse(fecha1));
            if (fecha2 == "fechaActual"){
                fec2.setTime(dateFormat.parse("2018-07-02"));
            }
            else {
                fec2.setTime(dateFormat.parse(fecha2));
            }
        }
        catch (ParseException ex)
        {
            System.out.println(ex);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        fec1.set(Calendar.HOUR_OF_DAY, 0);
        fec1.set(Calendar.MINUTE, 0);
        fec1.set(Calendar.SECOND, 0);

        fec2.set(Calendar.HOUR_OF_DAY, 0);
        fec2.set(Calendar.MINUTE, 0);
        fec2.set(Calendar.SECOND, 0);

        if (fec1.after(fec2)){
            numero = 1;
        }
        else if (fec1.before(fec2)){
            numero = -1;
        }
        else {
            numero = 0;
        }

        return numero;
    }

    private Boolean formatoCorreto(String fecha){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar fec = Calendar.getInstance();

        try {
            fec.setTime(dateFormat.parse(fecha));
        }
        catch (ParseException ex)
        {
            return false;
        } catch (java.text.ParseException e) {
            return false;
        }

        return true;
    }
}