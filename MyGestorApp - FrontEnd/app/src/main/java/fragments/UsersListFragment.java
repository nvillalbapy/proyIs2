package fragments;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.is2.mygestorapp.AdaptadorUser;
import com.is2.mygestorapp.User;
import com.is2.mygestorapp.Homepage;
import com.is2.mygestorapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Pattern;

import helper.Connection;
import helper.URLs;


public class UsersListFragment extends Fragment {

    private FloatingActionButton addButton;
    AdminProfileFragment adminProfileFragment = new AdminProfileFragment();
    private ListView lvItems;
    private AdaptadorUser adaptadoru;
    RegistrationFragment registrationFragment = new RegistrationFragment();
    User user;
    Bundle bundle = new Bundle();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_users, container, false);

        ((Homepage) getActivity()).getSupportActionBar().setTitle("Administrar usuarios");

        final Connection connection = new Connection();
        String url, message;
        addButton = (FloatingActionButton) view.findViewById(R.id.add);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            // Obtenemos la lista de tareas asignadas a dicho usuario
            url = URLs.URL_USER;
            message = connection.executeGet(url, getContext());
            if (message == null) {
                Toast.makeText(getContext(), "No existen usuarios.", 5).show();
            } else {
                lvItems = (ListView) view.findViewById(R.id.lvItems);
                adaptadoru = new AdaptadorUser(getContext(), GetArrayItems(message));
                lvItems.setAdapter(adaptadoru);
                lvItems.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                user = (User) lvItems.getItemAtPosition(position);
                                bundle.putInt(Homepage.ID_KEY, user.getIdUsuario());
                                bundle.putString(Homepage.NOMBRE_KEY, user.getNombre());
                                bundle.putString(Homepage.USUARIO_KEY, user.getUsuario());
                                bundle.putString(Homepage.CORREO_KEY, user.getMail());
                                bundle.putString(Homepage.TELEFONO_KEY, user.getPhone());
                                bundle.putString(Homepage.CONTRASENHA_KEY, user.getPassword());

                                adminProfileFragment.setArguments(bundle);

                                FragmentManager fragmentManager;
                                FragmentTransaction fragmentTransaction;
                                fragmentManager = getFragmentManager();
                                fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.fragment, adminProfileFragment).addToBackStack(null);
                                fragmentTransaction.commit();
                            }
                        }
                );
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FragmentManager fragmentManager;
                        FragmentTransaction fragmentTransaction;
                        fragmentManager = getFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment, registrationFragment).addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });
            }
        } catch (NullPointerException e) {
            Toast.makeText(getContext(), "Conexión con el servidor fallida.", 5).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }


    private ArrayList<User> GetArrayItems(String message) throws JSONException {
        ArrayList<User> listUsers = new ArrayList<>();
        JSONObject json;
        User user;
        String [] vector;
        vector = message.split(Pattern.quote ("},{"));

        for (int indice = 0; indice < vector.length; indice++) {
            //Si es el primer elemento
            if (indice == 0) {
                // Eliminamos el corchete abierto de la lista
                vector[indice] = vector[indice].substring(1, vector[indice].length());
                // Agregamos el cierre de llave
                vector[indice] = vector[indice] += "}";
            }
            // Si es el ultimo elemento
            else if(indice == vector.length - 1) {
                // Agregamos la llave abierta
                vector[indice] = "{" + vector[indice];
                // Eliminamos el corchete cerrado de la lista
                vector[indice] = vector[indice].substring(0, vector[indice].length() - 2);
            }
            // Si es cualquier otro
            else {
                // Agregamos la llave abierta
                vector[indice] = "{" + vector[indice];
                // Agregamos el cierre de llave
                vector[indice] = vector[indice] += "}";
            }

            json = new JSONObject(vector[indice]);
            user = new User(json);
            listUsers.add(user);
        }
        return listUsers;
    }
}