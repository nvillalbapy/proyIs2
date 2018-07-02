package fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.is2.mygestorapp.Homepage;
import com.is2.mygestorapp.Inicio;
import com.is2.mygestorapp.R;
import com.is2.mygestorapp.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import helper.Connection;
import helper.URLs;

public class AdminEditUhFragment extends Fragment {
    private FloatingActionButton acceptButton;
    private TextInputEditText uhTitulo, uhDescripcion, uhUsuario, uhFechaInicio, uhFechaFin;
    private Spinner uhEstado;
    private String titulo, descripcion, estado, nuevoEstado, usuarioDesignado, fechaInicio, fechaFin;
    private int idUh, idEstado, idUsuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_admin_edit_uh, container, false);

        //Extrae los campos pasados como argumentos
        idUh =  getArguments().getInt(UserUhListFragment.ID_UH_KEY);
        titulo =  getArguments().getString(UserUhListFragment.TITULO_KEY);
        descripcion =  getArguments().getString(UserUhListFragment.DESCRIPCION_KEY);
        estado =  getArguments().getString(UserUhListFragment.ESTADO_KEY);
        idEstado =  getArguments().getInt(UserUhListFragment.ID_ESTADO_KEY);
        idUsuario =  getArguments().getInt(Homepage.ID_KEY);
        usuarioDesignado =  getArguments().getString(Homepage.USUARIO_KEY);
        fechaInicio = getArguments().getString(UserUhListFragment.FECHAINICIO_KEY);
        fechaFin = getArguments().getString(UserUhListFragment.FECHAFIN_KEY);

        // Presenta los campos extraidos
        uhTitulo = (TextInputEditText) view.findViewById(R.id.edit_titulo);
        uhDescripcion = (TextInputEditText) view.findViewById(R.id.edit_descripcion);
        uhEstado = (Spinner) view.findViewById(R.id.edit_estado);
        uhUsuario = (TextInputEditText) view.findViewById(R.id.edit_usuario_designado);
        uhFechaFin = (TextInputEditText) view.findViewById(R.id.edit_fecha_fin);

        view.findViewById(R.id.edit_titulo).requestFocus();

        uhTitulo.setText(titulo);
        uhDescripcion.setText(descripcion);
        uhEstado.setSelection(idEstado) ;
        uhUsuario.setText(usuarioDesignado);
        uhFechaFin.setText(fechaFin.substring(0,10));

        uhEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            nuevoEstado = (String) adapterView.getItemAtPosition(i);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    });

        acceptButton = (FloatingActionButton) view.findViewById(R.id.fab_accept);

        //Si presiona el boton aceptar, guarda los cambios.
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptChanges();
            }
        });

        return view;
    }

    private void acceptChanges() {
        String nuevoTitulo, nuevaDescripcion, nuevoUsuario, nuevaFechaInicio, nuevaFechaFin;
        String msg = null;
        String url;
        User user;
        JSONObject userParams = new JSONObject();
        JSONObject editParams = new JSONObject();
        Connection connection = new Connection();
        Context context = super.getContext();
        JSONObject usuario;

        // Extraemos los nuevos valores
        nuevoTitulo = uhTitulo.getText().toString();
        nuevaDescripcion = uhDescripcion.getText().toString();
        nuevoUsuario = uhUsuario.getText().toString();
        nuevaFechaFin = uhFechaFin.getText().toString();

        url = URLs.URL_FIND;
        try {
            userParams.put("usuario", nuevoUsuario.toLowerCase());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        msg = connection.executePost(url, userParams.toString(), context);

        // Si no hubieron cambios se utilizan los valores anteriores
        if (TextUtils.isEmpty(nuevoTitulo)) {
            nuevoTitulo = titulo;
        }

        if (TextUtils.isEmpty(nuevaDescripcion)) {
            nuevaDescripcion = descripcion;
        }

        if (TextUtils.isEmpty(nuevaFechaFin)) {
            nuevaFechaFin = fechaFin;
        }

        if (comparaFechas(nuevaFechaFin, fechaInicio) == -1) {
            uhFechaFin.setError("La fecha de fin no puede ser anterior a la fecha de inicio.");
            uhFechaFin.requestFocus();
            return;
        }

        if (msg.equals("") || msg == null) {
            uhUsuario.setError("No existe el usuario.");
            return;
        }
       try {
            usuario = new JSONObject(msg);
            editParams.put("idUs", idUh);
            editParams.put("tituloUs", nuevoTitulo);
            editParams.put("descripcionActividad", nuevaDescripcion);
            editParams.put("estado", nuevoEstado);
            editParams.put("idUsuario", usuario);
            editParams.put("fechaInicio", fechaInicio);
            editParams.put("fechaFin", nuevaFechaFin + "T00:00:00-04:00");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        url = URLs.URL_UH + idUh;
        msg = connection.executePut(url, editParams.toString(), context);

        Toast.makeText(context, "Los cambios se han guardado correctamente.", 5).show();

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
}
