package fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.is2.mygestorapp.Homepage;
import com.is2.mygestorapp.Inicio;
import com.is2.mygestorapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import helper.Connection;
import helper.URLs;

public class UserUhFragment extends Fragment {

    String titulo, descripcion, estado, usuarioDesignado, fechaInicio, fechaFin;
    int idUh, idEstado, idUsuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_user_uh, container, false);

        ((Homepage) getActivity()).getSupportActionBar().setTitle("Detalle tarea");

        LinearLayout llBoton;
        Button button = new Button(getContext());

        //Extrae los campos pasados como argumentos
        idUh =  getArguments().getInt(UserUhListFragment.ID_UH_KEY);
        titulo =  getArguments().getString(UserUhListFragment.TITULO_KEY);
        descripcion =  getArguments().getString(UserUhListFragment.DESCRIPCION_KEY);
        estado =  getArguments().getString(UserUhListFragment.ESTADO_KEY);
        idEstado =  getArguments().getInt(UserUhListFragment.ID_ESTADO_KEY);
        usuarioDesignado =  getArguments().getString(Homepage.USUARIO_KEY);
        idUsuario =  getArguments().getInt(Homepage.ID_KEY);
        fechaInicio =  getArguments().getString(UserUhListFragment.FECHAINICIO_KEY);
        fechaFin = getArguments().getString(UserUhListFragment.FECHAFIN_KEY);

        // Presenta los campos extraidos
        TextView uhTitulo = (TextView) view.findViewById(R.id.uhTitulo);
        TextView uhDescripcion = (TextView) view.findViewById(R.id.uhDescripcion);
        TextView uhEstado = (TextView) view.findViewById(R.id.uhEstado);
        TextView uhUsuario = (TextView) view.findViewById(R.id.uhUsuario);
        TextView uhFechaInicio = (TextView) view.findViewById(R.id.uhFechaInicio);
        TextView uhFechaFin = (TextView) view.findViewById(R.id.uhFechaFin);

        uhTitulo.setText(titulo);
        uhDescripcion.setText(descripcion);
        uhEstado.setText(estado);
        uhUsuario.setText(usuarioDesignado);
        uhFechaInicio.setText(mostrarFecha(fechaInicio));
        uhFechaFin.setText(mostrarFecha(fechaFin));

        llBoton = (LinearLayout) view.findViewById(R.id.ll_button);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT );

        if (estado.equals("Asignado")){
            //Asignamos propiedades de layout al boton
            button.setLayoutParams(lp);
            button.setBackgroundColor(getResources().getColor(R.color.md_indigo_700));
            button.setTextColor(getResources().getColor(R.color.md_white_1000));

            //Asignamos Texto al botón
            button.setText("Iniciar tarea");
            //Asignamose el Listener
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cambiarEstado();
                }
            });
            //Añadimos el botón al linear layout
            llBoton.addView(button);
        } else if (estado.equals("En proceso")){
            //Asignamos propiedades de layout al boton
            button.setLayoutParams(lp);
            button.setBackgroundColor(getResources().getColor(R.color.md_indigo_700));
            button.setTextColor(getResources().getColor(R.color.md_white_1000));
            //Asignamos Texto al botón
            button.setText("Finalizar tarea");
            //Asignamose el Listener
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cambiarEstado();
                }
            });
            //Añadimos el botón al linear layout
            llBoton.addView(button);
        }

        return view;

    }

    protected String mostrarFecha(String fecha) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar fechaCal = Calendar.getInstance();

        try {
            fechaCal.setTime(dateFormat.parse(fecha));
        }
        catch (ParseException ex)
        {
            System.out.println(ex);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return dateFormat.format(fechaCal.getTime());
    }

    private void cambiarEstado() {
        Context context = super.getContext();
        JSONObject editParams = new JSONObject();
        Connection connection = new Connection();
        String nuevoEstado = null;
        String msg = null;
        String url;

        JSONObject usuario;

        if (estado.equals("Asignado")){
            nuevoEstado = "En proceso";
        } else if (estado.equals("En proceso")){
            nuevoEstado = "Finalizado";
        }

        url = URLs.URL_USER + idUsuario;
        msg = connection.executeGet(url, context);

        try {
            usuario = new JSONObject(msg);
            editParams.put("idUs", idUh);
            editParams.put("tituloUs", titulo);
            editParams.put("descripcionActividad", descripcion);
            editParams.put("estado", nuevoEstado);
            editParams.put("idUsuario", usuario);
            editParams.put("fechaInicio", fechaInicio);
            editParams.put("fechaFin", fechaFin);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        url = URLs.URL_UH + idUh;
        msg = connection.executePut(url, editParams.toString(), context);

        Toast.makeText(context, "Hecho.", 5).show();

        getActivity().finish();
        Intent intent = new Intent(getContext(), Inicio.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
