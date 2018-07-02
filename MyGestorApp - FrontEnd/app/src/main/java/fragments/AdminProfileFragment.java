package fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.is2.mygestorapp.Homepage;
import com.is2.mygestorapp.Inicio;
import com.is2.mygestorapp.R;
import com.is2.mygestorapp.User;

import org.json.JSONException;
import org.json.JSONObject;

import helper.Connection;
import helper.URLs;

import static com.is2.mygestorapp.Homepage.ID_KEY;

public class AdminProfileFragment extends Fragment {

    private FloatingActionButton delete_btn;
    private FloatingActionButton editButton;
    private Integer idUsuario;
    EditUserFragment editUserFragment = new EditUserFragment();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_admin_profile, container, false);
        final Bundle bundle = new Bundle();
        final Connection connection = new Connection();
        JSONObject usuarioJson = null;
        User user = null;

        final String nombre, usuario, correo, contrasenha, telefono, message, url;

        ((Homepage) getActivity()).getSupportActionBar().setTitle("Detalle usuario");

        //Extrae los campos pasados como argumentos
        idUsuario = getArguments().getInt(ID_KEY);
        nombre =  getArguments().getString(Homepage.NOMBRE_KEY);
        usuario =  getArguments().getString(Homepage.USUARIO_KEY);
        correo =  getArguments().getString(Homepage.CORREO_KEY);
        telefono =  getArguments().getString(Homepage.TELEFONO_KEY);
        contrasenha = getArguments().getString(Homepage.CONTRASENHA_KEY);

        url = URLs.URL_USER + idUsuario;
        message = connection.executeGet(url, getContext());
        try {
            usuarioJson = new JSONObject(message);
            user = new User(usuarioJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Presenta los campos extraidos
        TextView profileName = (TextView) view.findViewById(R.id.profile_name);
        TextView profileUser = (TextView) view.findViewById(R.id.profile_user);
        TextView profileEmail = (TextView) view.findViewById(R.id.profile_email);
        TextView profilePhone = (TextView) view.findViewById(R.id.profile_phone);
        TextView profileTipo = (TextView) view.findViewById(R.id.profile_tipo);

        profileName.setText(nombre);
        profileUser.setText(usuario);
        profileEmail.setText(correo);
        profilePhone.setText(telefono);
        profileTipo.setText(user.getIdRol().getNombreRol());

        editButton = (FloatingActionButton) view.findViewById(R.id.fab);
        delete_btn = (FloatingActionButton) view.findViewById(R.id.borrar);


        //Si presiona el boton flotante, empieza la actividad EditUser.
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putInt(Homepage.ID_KEY, idUsuario);
                bundle.putString(Homepage.NOMBRE_KEY, nombre);
                bundle.putString(Homepage.USUARIO_KEY, usuario);
                bundle.putString(Homepage.CORREO_KEY, correo);
                bundle.putString(Homepage.TELEFONO_KEY, telefono);
                bundle.putString(Homepage.CONTRASENHA_KEY, contrasenha);
                editUserFragment.setArguments(bundle);

                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, editUserFragment).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteChanges();
            }
        });
        return view;

    }
    private void deleteChanges() {

        final Context context = super.getContext();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(super.getContext());
        alertDialog.setMessage("¿Estas seguro de que desea eliminar este usuario?");
        alertDialog.setTitle("Eliminar usuario");
        alertDialog.setIcon(R.drawable.baseline_warning_black_24dp);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Sí", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {

                Connection connection = new Connection();

                String url = URLs.URL_USER + idUsuario;
                String msg = connection.executeDelete(url,  getContext());
                Toast.makeText(context, "Se ha eliminado al usuario con éxito.", 5).show();

                getActivity().finish();
                Intent intent = new Intent(getContext(), Inicio.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });
        alertDialog.show();




    }
}