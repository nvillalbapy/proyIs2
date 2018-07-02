package fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.is2.mygestorapp.Homepage;
import com.is2.mygestorapp.Inicio;
import com.is2.mygestorapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import helper.Connection;
import helper.URLs;

public class UserEditUhFragment extends Fragment {
    private FloatingActionButton acceptButton;
    private TextView uhTitulo, uhDescripcion, uhUsuario, uhFechaFin;
    private Spinner uhEstado;
    private String titulo, descripcion, estado, nuevoEstado, usuarioDesignado, fechaInicio, fechaFin;
    private int idUh, idEstado, idUsuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_user_edit_uh, container, false);

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
        uhTitulo = (TextView) view.findViewById(R.id.edit_titulo);
        uhDescripcion = (TextView) view.findViewById(R.id.edit_descripcion);
        uhEstado = (Spinner) view.findViewById(R.id.edit_estado);
        uhUsuario = (TextView) view.findViewById(R.id.edit_usuario_designado);
        uhFechaFin = (TextView) view.findViewById(R.id.edit_fecha_fin);

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
        String msg = null;
        String url;
        JSONObject editParams = new JSONObject();
        Connection connection = new Connection();
        Context context = super.getContext();
        JSONObject usuario;

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

        Toast.makeText(context, "Los cambios se han guardado correctamente.", 5).show();

        getActivity().finish();
        Intent intent = new Intent(getContext(), Inicio.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
