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

import com.is2.mygestorapp.AdaptadorUh;
import com.is2.mygestorapp.Uh;
import com.is2.mygestorapp.Homepage;
import com.is2.mygestorapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Pattern;

import helper.Connection;
import helper.URLs;

public class AdminUhListFragment extends Fragment {
    public static final String ID_UH_KEY = "ID_UH";
    public static final String TITULO_KEY = "TITULO";
    public static final String DESCRIPCION_KEY = "DESCRIPCION";
    public static final String ESTADO_KEY = "ESTADO";
    public static final String ID_ESTADO_KEY = "ID_ESTADO";
    public static final String FECHAINICIO_KEY = "FECHA_INICIO";
    public static final String FECHAFIN_KEY = "FECHA_FIN";

    private ListView lvItems;
    private AdaptadorUh adaptadorUh;
    private FloatingActionButton addButton;
    AdminUhFragment adminUhFragment = new AdminUhFragment();
    AddUhFragment addUhFragment = new AddUhFragment();
    Uh uh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_admin_uh_list, container, false);

        ((Homepage) getActivity()).getSupportActionBar().setTitle("Administrar tareas");

        Connection connection = new Connection();
        final Bundle bundle = new Bundle();
        String url, message;
        ArrayList<Uh> listaUh = new ArrayList<>();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            // Obtenemos la lista de tareas asignadas a dicho usuario
            url = URLs.URL_UH;
            message = connection.executeGet(url, getContext());

            if (message == null){
                Toast.makeText(getContext(),"No existen tareas.", 5).show();
            }
            else {
                lvItems = (ListView) view.findViewById(R.id.lvItems);
                listaUh = GetArrayItems(message);
                adaptadorUh = new AdaptadorUh(getContext(), listaUh);
                lvItems.setAdapter(adaptadorUh);
                lvItems.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                uh = (Uh) lvItems.getItemAtPosition(position);
                                bundle.putInt(ID_UH_KEY, uh.getIdUs());
                                bundle.putString(TITULO_KEY, uh.getTituloUs());
                                bundle.putString(DESCRIPCION_KEY, uh.getDescripcionActividad());
                                bundle.putString(ESTADO_KEY, uh.getEstado());
                                bundle.putInt(UserUhListFragment.ID_ESTADO_KEY, uh.getIdEstado(uh.getEstado()));
                                bundle.putInt(Homepage.ID_KEY, uh.getIdUsuario().getIdUsuario());
                                bundle.putString(Homepage.USUARIO_KEY, uh.getIdUsuario().getUsuario());
                                bundle.putString(FECHAINICIO_KEY, uh.getFechaInicio());
                                bundle.putString(FECHAFIN_KEY, uh.getFechaFin());

                                adminUhFragment.setArguments(bundle);

                                FragmentManager fragmentManager;
                                FragmentTransaction fragmentTransaction;
                                fragmentManager = getFragmentManager();
                                fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.fragment, adminUhFragment).addToBackStack(null);
                                fragmentTransaction.commit();
                            }
                        }
                );
            }
        }
        catch(NullPointerException e){
            Toast.makeText(getContext(),"Conexión con el servidor fallida.", 5).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        addButton = (FloatingActionButton) view.findViewById(R.id.fab);

        //Si presiona el boton flotante, empieza la actividad .
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarUh();
            }
        });

        return view;
    }

    private ArrayList<Uh> GetArrayItems(String message) throws JSONException {
        ArrayList<Uh> listaUh = new ArrayList<>();
        JSONObject json;
        Uh uh;
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
            uh = new Uh(json);
            listaUh.add(uh);
        }
        return listaUh;
    }

    protected void agregarUh(){
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, addUhFragment).addToBackStack(null);
        fragmentTransaction.commit();
    }
}
