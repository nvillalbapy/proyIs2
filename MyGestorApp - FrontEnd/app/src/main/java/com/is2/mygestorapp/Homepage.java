package com.is2.mygestorapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import fragments.AdminUhListFragment;
import fragments.ProfileFragment;
import fragments.UserUhListFragment;
import fragments.UsersListFragment;
import helper.Connection;

import static helper.URLs.URL_USER;

public class Homepage extends AppCompatActivity {
    public static final String ID_KEY = "ID_USUARIO";
    public static final String NOMBRE_KEY = "NOMBRE";
    public static final String USUARIO_KEY = "USUARIO";
    public static final String CORREO_KEY = "CORREO";
    public static final String TELEFONO_KEY = "TELEFONO";
    public static final String ID_ROL_KEY = "ID_ROL";
    public static final String CONTRASENHA_KEY = "CONTRASENHA";
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static Integer idUsuario, idRol;
    private static final int INTERVALO = 2000; //2 segundos
    private long tiempoPrimerClick;
    Fragment currentFragment;

    ProfileFragment profileFragment = new ProfileFragment();
    UserUhListFragment userUhListFragment = new UserUhListFragment();
    UsersListFragment usersListFragment = new UsersListFragment();
    AdminUhListFragment adminUhListFragment = new AdminUhListFragment();

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Bundle bundle = new Bundle();
        Connection connection = new Connection();
        SharedPreferences sharedPreferences;
        JSONObject json;
        User user;
        String nombre, usuario, correo, telefono, contrasenha, url, msg;
        Integer id;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            setupNavigationDrawerContent(navigationView);
        }

        setupNavigationDrawerContent(navigationView);

        //Se cargan los valores en un nuevo objeto de tipo User de acuerdo
        //el usuario haya ingresado haya ingresado por login o registration
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            url = URL_USER + idUsuario;
            msg = connection.executeGet(url, this);

            json = new JSONObject(msg);
            user = new User(json);
            idUsuario = null;

            //Obtenemos los valores de cada variable del objeto User
            id = user.getIdUsuario();
            nombre = user.getNombre();
            usuario = user.getUsuario();
            correo = user.getMail();
            telefono = user.getPhone();
            contrasenha = user.getPassword();
            idRol = user.getIdRol().getIdRol();

            // Guardamos los valores de la sesion iniciada
            sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(ID_KEY, id);
            editor.apply();

            //Pasamos dichos valores como argumento al profileFragment
            bundle.putInt(ID_KEY, id);
            bundle.putString(NOMBRE_KEY, nombre);
            bundle.putString(USUARIO_KEY, usuario);
            bundle.putString(CORREO_KEY, correo);
            bundle.putString(TELEFONO_KEY, telefono);
            bundle.putString(CONTRASENHA_KEY, contrasenha);
            bundle.putInt(ID_ROL_KEY, idRol);

            profileFragment.setArguments(bundle);
            userUhListFragment.setArguments(bundle);

            //Mostramos el nombre y correo en header
            View headerView = navigationView.getHeaderView(0);
            TextView navName = (TextView) headerView.findViewById(R.id.nav_dr_nombre);
            navName.setText(nombre);
            TextView navEmail = (TextView) headerView.findViewById(R.id.nav_dr_correo);
            navEmail.setText(correo);
        }
        catch(NullPointerException e){
            e.printStackTrace();
            Toast.makeText(this,"Conexión con el servidor fallida.", 5).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //First start (Uh Fragment)
        setFragment(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.item_navigation_drawer_uh:
                                menuItem.setChecked(true);
                                setFragment(0);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_navigation_drawer_usuarios:
                                menuItem.setChecked(true);
                                setFragment(1);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_navigation_drawer_perfil:
                                menuItem.setChecked(true);
                                setFragment(2);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_navigation_drawer_cerrar_sesion:
                                menuItem.setChecked(true);
                                setFragment(3);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_navigation_drawer_tareas:
                                menuItem.setChecked(true);
                                setFragment(4);
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;

                        }
                        return true;
                    }
                });
    }

    public void setFragment(int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        switch (position) {
            case 0:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, userUhListFragment);
                currentFragment = usersListFragment;
                fragmentTransaction.commit();
                break;
            case 1:
                if (Homepage.idRol == 2){
                    alertDialog.setMessage("Opciones disponibles solo para usuarios del tipo administrador.");
                    alertDialog.setTitle("Acceso restringido");
                    alertDialog.setIcon(R.drawable.baseline_warning_black_24dp);
                    alertDialog.setCancelable(true);
                    alertDialog.show();
                }
                else {
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment, usersListFragment);
                    currentFragment = null;
                    fragmentTransaction.commit();
                }
                break;
            case 2:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, profileFragment);
                currentFragment = null;
                fragmentTransaction.commit();
                break;
            case 3:
                alertDialog.setMessage("¿Estas seguro que desea cerrar sesión?");
                alertDialog.setTitle("Cerrar sesión");
                alertDialog.setIcon(R.drawable.baseline_warning_black_24dp);
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Sí", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        cerrarSesion();
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });
                alertDialog.show();
                break;
            case 4:
                if (Homepage.idRol == 2){
                    alertDialog.setMessage("Opciones disponibles solo para usuarios del tipo administrador.");
                    alertDialog.setTitle("Acceso restringido");
                    alertDialog.setIcon(R.drawable.baseline_warning_black_24dp);
                    alertDialog.setCancelable(true);
                    alertDialog.show();
                }
                else {
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment, adminUhListFragment);
                    currentFragment = null;
                    fragmentTransaction.commit();
                }
                break;
        }
    }

    @Override
    public void onBackPressed(){

        int count = getSupportFragmentManager().getBackStackEntryCount();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);

        //Si el menú esta abierto, lo cierra.
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            //Si se realizo una transacción ejecuta onBackPressed();
        } else {

            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                if (currentFragment == null) {
                    setFragment(0);
                } else  {
                    if (tiempoPrimerClick + INTERVALO > System.currentTimeMillis()){
                        super.onBackPressed();
                        moveTaskToBack(true);
                        //return;
                    }else {
                        Toast.makeText(this, "Vuelve a presionar para salir.", Toast.LENGTH_SHORT).show();
                    }
                    tiempoPrimerClick = System.currentTimeMillis();
                }
            }
            else if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    super.onBackPressed();
            }
        }
    }

    public void cerrarSesion(){
        SharedPreferences sharedpreferences = getSharedPreferences(Homepage.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.apply();
        super.finishAffinity();
        Intent i = new Intent(this, Inicio.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}