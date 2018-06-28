package com.is2.mygestorapp;

import helper.URLs;
import helper.Connection;

import android.text.TextUtils;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.is2.mygestorapp.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button buttonLogin = (Button)findViewById(R.id.login_button);
        TextView textViewRegister = (TextView)findViewById(R.id.login_register);

        //Si presiona login, se llama al método Login.
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarLogin();
            }
        });

        //Si presiona Registrarme, empieza la actividad Registration.
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Registration.class));
            }
        });
    }

    /*Metodo que sera llamado cuando el usuario haga click en el boton login*/
    private void validarLogin(){
        Intent intent = new Intent(this, Homepage.class);
        finish();

        Connection connection = new Connection();
        JSONObject loginParams = new JSONObject();
        JSONObject json = new JSONObject();
        User user;
        String message;

        //recupera los valores ingresados por el usuario
        EditText editTextUser = (EditText)findViewById(R.id.login_user);
        EditText editTextPassword = (EditText)findViewById(R.id.login_password);

        //Extraemos los valores introducidos.
        final String usuario = editTextUser.getText().toString();
        final String contrasenha= editTextPassword.getText().toString();

        //Validación de entradas.
        if (TextUtils.isEmpty(usuario)) {
            editTextUser.setError("Debe ingresar su usuario.");
            editTextUser.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(contrasenha)) {
            editTextPassword.setError("Debe ingresar su contraseña.");
            editTextPassword.requestFocus();
            return;
        }

        try {
            loginParams.put("password", contrasenha);
            loginParams.put("usuario", usuario);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            String url = URLs.URL_LOGIN;
            message = connection.executePost(url, loginParams.toString(), this);
            if (message.equals("") || message == null){
                Toast.makeText(this,"Datos incorrectos.", 5).show();
                return;
            }

            json = new JSONObject(message);
            user = new User(json);
            Homepage.idUsuario = user.getIdUsuario();

            intent.putExtra(EXTRA_MESSAGE, editTextUser.getText().toString());
            startActivity(intent);
        }
        catch(NullPointerException e){
            Toast.makeText(this,"Conexión con el servidor fallida.", 5).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
    }*/

}