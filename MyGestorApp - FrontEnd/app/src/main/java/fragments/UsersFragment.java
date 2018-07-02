package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.is2.mygestorapp.Homepage;
import com.is2.mygestorapp.R;


public class UsersFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view;
        if (Homepage.idRol == 2){
            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.fragment_blanco, container, false);
            ((Homepage) getActivity()).getSupportActionBar().setTitle("Administrar usuarios");
            Toast.makeText(getContext(),"Opciones disponibles solo para administrador.", 5).show();
        }
        else {

            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.fragment_users, container, false);

            ((Homepage) getActivity()).getSupportActionBar().setTitle("Administrar usuarios");
            if (Homepage.idRol == 2){
                Toast.makeText(getContext(),"Opciones disponibles solo para administrador.", 5).show();
            }
            else {
                Toast.makeText(getContext(),"Disponible.", 5).show();
            }
        }
            return view;
    }
}