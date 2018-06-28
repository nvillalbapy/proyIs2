package com.is2.mygestorapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Adaptador extends BaseAdapter{
    private Context context;
    private ArrayList<Uh> listIem;

    public Adaptador(Context context, ArrayList<Uh> listIem){
        this.context = context;
        this.listIem = listIem;
    }

    @Override
    public int getCount(){
        return listIem.size();
    }

    @Override
    public Object getItem(int position){
        return listIem.get(position);
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Uh Item = (Uh) getItem(position);

        //final View view = inflater.inflate(R.layout.item_uh, container, false);
        convertView = LayoutInflater.from(context).inflate(R.layout.item_uh, null);
        TextView tvTitulo = (TextView) convertView.findViewById(R.id.itemTitulo);
        TextView tvContenido = (TextView) convertView.findViewById(R.id.itemContenido);
        //TextView tvVencimiento = (TextView) convertView.findViewById(R.id.itemVencimiento);

        tvTitulo.setText(Item.getTituloUs());
        tvContenido.setText(Item.getDescripcionActividad());
        //tvContenido.setText(Item.getFechaFin());
        return convertView;
    }
}
