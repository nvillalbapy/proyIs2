package com.is2.mygestorapp;

import helper.Connection;
import helper.URLs;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Registration extends AppCompatActivity {
    Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        buttonRegister = (Button)findViewById(R.id.registration_button);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        Intent intent = new Intent(this, Homepage.class);
        Connection connection = new Connection();
        JSONObject registrationParams = new JSONObject();
        JSONObject json;
        User user;
        String message;

        //recupera los valores ingresados por el usuario
        EditText editTextName = (EditText)findViewById(R.id.registration_name);
        EditText editTextUsername = (EditText)findViewById(R.id.registration_user);
        EditText editTextPassword = (EditText)findViewById(R.id.registration_password);
        EditText editTextEmail = (EditText)findViewById(R.id.registration_email);
        EditText editTextPhone = (EditText)findViewById(R.id.registration_phone);
        CheckBox checkBoxTerms = (CheckBox)findViewById(R.id.registration_terms);;

        //Extraemos los valores introducidos.
        final String nombreUsuario = editTextName.getText().toString();
        final String usuario = editTextUsername.getText().toString();
        final String correoElectronico = editTextEmail.getText().toString();
        final String telefono = editTextPhone.getText().toString();
        final String contrasenha = editTextPassword.getText().toString();

        //Validamos las entradas.
        if (TextUtils.isEmpty(nombreUsuario)) {
            editTextName.setError("Debe ingresar su nombre.");
            editTextName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(usuario)) {
            editTextUsername.setError("Debe ingresar un usuario.");
            editTextUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(correoElectronico)) {
            editTextEmail.setError("Debe ingresar su correo electrónico.");
            editTextEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correoElectronico).matches()) {
            editTextEmail.setError("Ingrese un correo electrónico válido.");
            editTextEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(telefono)) {
            editTextPhone.setError("Debe ingresar un teléfono.");
            editTextPhone.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(contrasenha)) {
            editTextPassword.setError("Debe ingresar una contraseña.");
            editTextPassword.requestFocus();
            return;
        }

        if (contrasenha.length() < 6){
            editTextPassword.setError("La contraseña debe contener 6 dígitos como mínimo.");
            editTextPassword.requestFocus();
            return;
        }

        if(! checkBoxTerms.isChecked()){
            Toast.makeText(getApplicationContext(),"Debe aceptar los términos y condiciones para continuar con el registro.",Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            registrationParams.put("nombre", nombreUsuario);
            registrationParams.put("usuario", usuario);
            registrationParams.put("password", contrasenha);
            registrationParams.put("mail", correoElectronico);
            registrationParams.put("telefono", telefono);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            String url = URLs.URL_REGISTRATION;
            message = connection.executePost(url, registrationParams.toString(), this);

            if (message.equals("") || message == null){
                Toast.makeText(this,"Ese usuario ya está en uso. Prueba con otro", 5).show();
                return;
            }else {
                Toast.makeText(this,"Registro exitoso", 5).show();
                startActivity(intent);
                super.finishAffinity();
            }

            json = new JSONObject(message);
            user = new User(json);
            Homepage.idUsuario = user.getIdUsuario();
        }
        catch(NullPointerException e){
            Toast.makeText(this,"Conexión con el servidor fallida.", 5).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}