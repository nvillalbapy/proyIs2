package fragments;

import helper.Connection;
import helper.URLs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.is2.mygestorapp.Homepage;
import com.is2.mygestorapp.Login;
import com.is2.mygestorapp.R;
import com.is2.mygestorapp.Registration;

import org.json.JSONException;
import org.json.JSONObject;

public class EditUserFragment extends Fragment {

    private FloatingActionButton acceptButton;
    private TextInputEditText profileName;
    private TextInputEditText profileUser;
    private TextInputEditText profileEmail;
    private TextInputEditText profilePhone;
    private TextInputEditText profilePassword;
    private Integer idUsuario;
    private String nombre, usuario, correo, telefono, contrasenha;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_edit_user, container, false);

        //Extrae los campos pasados como argumentos
        idUsuario = getArguments().getInt(Homepage.ID_KEY);
        nombre =  getArguments().getString(Homepage.NOMBRE_KEY);
        usuario =  getArguments().getString(Homepage.USUARIO_KEY);
        correo =  getArguments().getString(Homepage.CORREO_KEY);
        telefono =  getArguments().getString(Homepage.TELEFONO_KEY);
        contrasenha = getArguments().getString(Homepage.CONTRASENHA_KEY);

        // Presenta los campos extraidos
        profileName = (TextInputEditText) view.findViewById(R.id.edit_name);
        profileUser = (TextInputEditText) view.findViewById(R.id.edit_user);
        profileEmail = (TextInputEditText) view.findViewById(R.id.edit_email);
        profilePhone = (TextInputEditText) view.findViewById(R.id.edit_phone);
        profilePassword = (TextInputEditText) view.findViewById(R.id.edit_password);

        profileName.setText(nombre);
        profileUser.setText(usuario);
        profileEmail.setText(correo);
        profilePhone.setText(telefono);
        profilePassword.setText(contrasenha);

        acceptButton = (FloatingActionButton) view.findViewById(R.id.fab);

        //Si presiona el boton flotante, acpeta los cambios.
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptChanges();
            }
        });

        return view;
    }

    private void acceptChanges() {
        String nuevoNombre, nuevoUsuario, nuevoCorreo, nuevoTelefono, nuevaContrasenha;
        String msg = null;
        String url;
        JSONObject editParams = new JSONObject();
        Connection connection = new Connection();
        Context context = super.getContext();

        // Extraemos los nuevos valores
        nuevoNombre = profileName.getText().toString();
        nuevoUsuario = profileUser.getText().toString();
        nuevoCorreo = profileEmail.getText().toString();
        nuevoTelefono = profilePhone.getText().toString();
        nuevaContrasenha = profilePassword.getText().toString();

        // Si no hubieron cambios se utilizan los valores anteriores
        if (TextUtils.isEmpty(nuevoNombre)) {
            nuevoNombre = nombre;
        }

        if (TextUtils.isEmpty(nuevoUsuario)) {
            nuevoUsuario = usuario;
        }

        if (TextUtils.isEmpty(nuevoCorreo)) {
            nuevoCorreo = correo;
        }

        if (TextUtils.isEmpty(nuevoTelefono)) {
            nuevoTelefono = telefono;
        }

        if (TextUtils.isEmpty(nuevaContrasenha)) {
            nuevaContrasenha = contrasenha;
        }

        try {
            editParams.put("idUsuario", idUsuario);
            editParams.put("nombre", nuevoNombre);
            editParams.put("usuario", nuevoUsuario);
            editParams.put("password", nuevaContrasenha);
            editParams.put("mail", nuevoCorreo);
            editParams.put("telefono", nuevoTelefono);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Si es el mismo usuario, edita directamente
        if (usuario.equals(nuevoUsuario)){
            url = URLs.URL_USER + idUsuario;
            msg = connection.executePut(url, editParams.toString(), context);
            Toast.makeText(context, "Los cambios se han guardado correctamente.", 5).show();
            Homepage.idUsuario = idUsuario;
            getActivity().finish();
            Intent intent = new Intent(getContext(), Homepage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        // Si es un usuario diferente, verifica la disponibilidad
        else {
            url = URLs.URL_EDIT + idUsuario;
            msg = connection.executePut(url, editParams.toString(), context);
            if (msg != null){
                Toast.makeText(context, "Los cambios se han guardado correctamente.", 5).show();
                Homepage.idUsuario = idUsuario;
                getActivity().finish();
                Intent intent = new Intent(getContext(), Homepage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            else {
                Toast.makeText(context, "Ese usuario ya está en uso. Prueba con otro", 5).show();
                return;
            }
        }
    }

}