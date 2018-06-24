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
    public static final String EXTRA_MESSAGE = "com.is2.mygestorapp.MESSAGE";
    public static String message;

    Button buttonRegister;
    CheckBox checkBoxTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        buttonRegister = (Button)findViewById(R.id.buttonRegister);
        checkBoxTerms = (CheckBox)findViewById(R.id.checkBoxTerms);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBoxTerms.isChecked() == true){
                    registerUser();
                }else{
                    Toast.makeText(getApplicationContext(),"Debe aceptar los términos y condiciones para continuar con el registro.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerUser() {
        Intent intent = new Intent(this, Homepage.class);
        Connection connection = new Connection();
        JSONObject registrationParams = new JSONObject();

        //recupera los valores ingresados por el usuario
        EditText editTextName = (EditText)findViewById(R.id.editTextName);
        EditText editTextUsername = (EditText)findViewById(R.id.editTextUsername);
        EditText editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        EditText editTextEmail = (EditText)findViewById(R.id.editTextEmail);

        //Extraemos los valores introducidos.
        final String nombreUsuario = editTextName.getText().toString();
        final String usuario = editTextUsername.getText().toString();
        final String correoElectronico = editTextEmail.getText().toString();
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
            editTextEmail.setError("Ingrese un correo electroinico válido.");
            editTextEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(contrasenha)) {
            editTextPassword.setError("Debe ingresar una contraseña.");
            editTextPassword.requestFocus();
            return;
        }

        try {
            registrationParams.put("nombre", nombreUsuario);
            registrationParams.put("usuario", usuario);
            registrationParams.put("password", contrasenha);
            registrationParams.put("mail", correoElectronico);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            String url = URLs.URL_REGISTRATION;
            System.out.println("************************url "+ url + "******************** msg " + registrationParams);
            message = connection.executePost(url, registrationParams.toString(), this);

            if (message.equals("") || message == null){
                Toast.makeText(this,"Registro fallido.", 5).show();
                return;
            }else {
                Toast.makeText(this,"Registro exitoso", 5).show();
                startActivity(intent);
            }
        }
        catch(NullPointerException e){
            Toast.makeText(this,"Conexión con el servidor fallida.", 5).show();
        }
    }
}