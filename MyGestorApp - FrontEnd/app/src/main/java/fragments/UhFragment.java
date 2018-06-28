package fragments;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.is2.mygestorapp.Adaptador;
import com.is2.mygestorapp.Uh;
import com.is2.mygestorapp.Homepage;
import com.is2.mygestorapp.Inicio;
import com.is2.mygestorapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import helper.Connection;
import helper.URLs;

import static android.content.Context.NOTIFICATION_SERVICE;


public class UhFragment extends Fragment {

    private ListView lvItems;
    private Adaptador adaptador;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_uh, container, false);

        ((Homepage) getActivity()).getSupportActionBar().setTitle("Mis tareas");

        Connection connection = new Connection();
        JSONObject uhParams;
        String url, message;
        Integer idUsuario = getArguments().getInt(Homepage.ID_KEY);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            // Obtenemos un json con los datos del usuario
            url = URLs.URL_USER + idUsuario;
            message = connection.executeGet(url, getContext());
            uhParams = new JSONObject(message);

            // Obtenemos la lista de tareas asignadas a dicho usuario
            url = URLs.URL_LISTA_UH;
            message = connection.executePost(url, uhParams.toString(), getContext());
            if (message == null){
                Toast.makeText(getContext(),"No tiene tareas cargadas a este usuario.", 5).show();
            }
            else {
                lvItems = (ListView) view.findViewById(R.id.lvItems);
                adaptador = new Adaptador(getContext(), GetArrayItems(message));
                lvItems.setAdapter(adaptador);
            }
        }
        catch(NullPointerException e){
            Toast.makeText(getContext(),"Conexión con el servidor fallida.", 5).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha = null;

        Calendar fecha_actual = Calendar.getInstance();
        Calendar fecha_fin_uh = Calendar.getInstance();

        try {
            fecha = formato.parse("24/06/2018");
        }
        catch (ParseException ex)
        {
            System.out.println(ex);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        fecha_fin_uh.setTime(fecha);

        fecha_actual.set(Calendar.HOUR_OF_DAY, 0);
        fecha_actual.set(Calendar.MINUTE, 0);
        fecha_actual.set(Calendar.SECOND, 0);
        fecha_actual.add(Calendar.DAY_OF_YEAR, -2);

        int numero = fecha_fin_uh.getTime().compareTo(fecha_actual.getTime());
        //System.out.println("----------------------------------- " + fecha_fin_uh.getTime().toString() + "----------------------------------- " + fecha_actual.getTime().toString());
        //System.out.println("----------------------------------- " + numero + "----------------------------------- ");

        if (fecha_fin_uh.getTime().toString().compareTo(fecha_actual.getTime().toString()) == 0){
            mostrarNotificacion();
        }

        return view;
    }

    void mostrarNotificacion(){

        NotificationCompat.Builder mBuilder;
        NotificationManager mNotifyMgr =(NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);

        int icono = R.mipmap.ic_launcher;
        Intent i=new Intent(getContext(), Inicio.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, i, 0);

        mBuilder =new NotificationCompat.Builder(getContext())
                .setContentIntent(pendingIntent)
                .setSmallIcon(icono)
                .setContentTitle("Titulo")
                .setContentText("Hola que tal?")
                .setVibrate(new long[] {100, 250, 100, 500})
                .setAutoCancel(true);

        mNotifyMgr.notify(1, mBuilder.build());

        /*NotificationManager notificationManager =(NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
        int icono = R.mipmap.ic_launcher;
        long vibrate [] = {0, 100, 100};
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getContext())
                .setSmallIcon(icono)
                .setContentTitle("Notificacion")
                .setContentText("Texto...")
                .setVibrate(vibrate)
                .setWhen(System.currentTimeMillis());
        notificationManager.notify(1,builder.build());*/

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
}