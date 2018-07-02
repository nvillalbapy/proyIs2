package fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.Spinner;

import com.is2.mygestorapp.Homepage;
import com.is2.mygestorapp.Inicio;
import com.is2.mygestorapp.R;
import com.is2.mygestorapp.User;

import org.json.JSONException;
import org.json.JSONObject;

import helper.Connection;
import helper.URLs;

public class RegistrationFragment extends Fragment {

    private TextInputEditText name, users, mail, phone, password;
    private FloatingActionButton acceptButton;
    private Spinner rol;
    private int idRol;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_add_user, container, false);

        ((Homepage) getActivity()).getSupportActionBar().setTitle("Agregar usuario");

        // Presenta los campos extraidos
        name = (TextInputEditText) view.findViewById(R.id.add_name);
        users = (TextInputEditText) view.findViewById(R.id.add_user);
        mail = (TextInputEditText) view.findViewById(R.id.add_email);
        phone = (TextInputEditText) view.findViewById(R.id.add_phone);
        password = (TextInputEditText) view.findViewById(R.id.add_password);
        rol = (Spinner) view.findViewById(R.id.type_user);

        view.findViewById(R.id.add_name).requestFocus();

        rol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
              @Override
              public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                  idRol = i+1;
                          //(Integer) adapterView.getItemAtPosition(i);
              }

              @Override
              public void onNothingSelected(AdapterView<?> adapterView) {

              }
        });


        acceptButton = (FloatingActionButton) view.findViewById(R.id.add_accept);

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
        JSONObject usuarioJson;
        JSONObject rolJson;
        User user;
        String nombre, usuario, correo, telefono, contrasenha;
        Integer tipousuario;
        String msg = null;
        String url;

        // Extraemos los nuevos valores
        nombre = name.getText().toString();
        usuario = users.getText().toString();
        correo = mail.getText().toString();
        telefono = phone.getText().toString();
        contrasenha = password.getText().toString();

        url = URLs.URL_ROL+idRol;
        try {
            userParams.put("idRol", idRol);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        msg = connection.executeGet(url, context);

        //Validamos las entradas.
        if (TextUtils.isEmpty(nombre)) {
            name.setError("Debe ingresar su nombre.");
            name.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(usuario)) {
            users.setError("Debe ingresar un usuario.");
            users.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(correo)) {
            mail.setError("Debe ingresar su correo electrónico.");
            mail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            mail.setError("Ingrese un correo electrónico válido.");
            mail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(telefono)) {
            phone.setError("Debe ingresar un teléfono.");
            phone.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(contrasenha)) {
            password.setError("Debe ingresar una contraseña.");
            password.requestFocus();
            return;
        }

        if (contrasenha.length() < 6){
            password.setError("La contraseña debe contener 6 dígitos como mínimo.");
            password.requestFocus();
            return;
        }


        try {
            rolJson = new JSONObject(msg);
            addParams.put("nombre", nombre);
            addParams.put("usuario", usuario);
            addParams.put("password", contrasenha);
            addParams.put("mail", correo);
            addParams.put("telefono", telefono);
            addParams.put("idRol", rolJson);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            url = URLs.URL_REGISTRATION;
            msg = connection.executePost(url, addParams.toString(), context);

            if (msg.equals("") || msg == null){
                Toast.makeText(context,"Ese usuario ya está en uso. Prueba con otro", 5).show();
                return;
            }else {
                Toast.makeText(context,"Registro exitoso", 5).show();

                getActivity().finish();
                Intent intent = new Intent(getContext(), Inicio.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            usuarioJson = new JSONObject(msg);
            user = new User(usuarioJson);
            Homepage.idUsuario = user.getIdUsuario();
        }
        catch(NullPointerException e){
            Toast.makeText(context, "Conexión con el servidor fallida.", 5).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}