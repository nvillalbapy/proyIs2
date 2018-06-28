package com.is2.mygestorapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Inicio extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        Intent intent;

        SharedPreferences sharedpreferences = getSharedPreferences(Homepage.MyPREFERENCES, Context.MODE_PRIVATE);
        if (sharedpreferences.contains(Homepage.ID_KEY)) {
            Homepage.idUsuario = sharedpreferences.getInt(Homepage.ID_KEY, 0);
            intent = new Intent(this, Homepage.class);
        }
        else {
            intent = new Intent(this, Login.class);
        }

        startActivity(intent);
        finish();
    }
}
