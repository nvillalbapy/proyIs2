package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.is2.mygestorapp.Homepage;
import com.is2.mygestorapp.R;


public class UsersFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_users, container, false);

        ((Homepage) getActivity()).getSupportActionBar().setTitle("Usuarios");

        return view;
    }

}