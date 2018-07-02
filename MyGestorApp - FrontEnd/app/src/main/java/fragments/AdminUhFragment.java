package fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ParseException;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;

import helper.Connection;
import helper.URLs;

public class AdminUhFragment extends Fragment {

    AdminEditUhFragment adminEditUhFragment = new AdminEditUhFragment();
    private FloatingActionButton editButton, deleteButton;
    final Bundle bundle = new Bundle();
    String titulo, descripcion, estado, usuarioDesignado, fechaInicio, fechaFin;
    int idUh, idEstado, idUsuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_admin_uh, container, false);

        ((Homepage) getActivity()).getSupportActionBar().setTitle("Detalle tarea");

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

        deleteButton = (FloatingActionButton) view.findViewById(R.id.fab_delete);
        editButton = (FloatingActionButton) view.findViewById(R.id.fab_edit);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUh();
            }
        });

        //Si presiona el boton flotante, empieza la actividad EditUser.
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editUh();
            }
        });

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

    private void editUh() {
        bundle.putInt(UserUhListFragment.ID_UH_KEY, idUh);
        bundle.putString(UserUhListFragment.TITULO_KEY, titulo);
        bundle.putString(UserUhListFragment.DESCRIPCION_KEY, descripcion);
        bundle.putString(UserUhListFragment.ESTADO_KEY, estado);
        bundle.putInt(UserUhListFragment.ID_ESTADO_KEY, idEstado);
        bundle.putInt(Homepage.ID_KEY, idUsuario);
        bundle.putString(Homepage.USUARIO_KEY, usuarioDesignado);
        bundle.putString(UserUhListFragment.FECHAINICIO_KEY, fechaInicio);
        bundle.putString(UserUhListFragment.FECHAFIN_KEY, fechaFin);

        adminEditUhFragment.setArguments(bundle);

        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, adminEditUhFragment).addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void deleteUh() {
        final Context context = super.getContext();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setMessage("¿Está seguro que desea eliminar esta tarea?");
        alertDialog.setTitle("Eliminar tarea");
        alertDialog.setIcon(R.drawable.baseline_warning_black_24dp);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Sí", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {

                Connection connection = new Connection();
                String url = URLs.URL_UH + idUh;

                String msg = connection.executeDelete(url,  context);
                Toast.makeText(context, "Se ha eliminado la tarea con éxito.", 5).show();

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

