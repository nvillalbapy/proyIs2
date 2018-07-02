package com.is2.mygestorapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class AdaptadorUser extends BaseAdapter{

    private int imgPersona;
    private Context context;
    private ArrayList<User> listIemU;

    public AdaptadorUser(Context context, ArrayList<User> listIemU){
        this.context = context;
        this.listIemU = listIemU;
    }

    @Override
    public int getCount(){
        return listIemU.size();
    }

    @Override
    public Object getItem(int position){
        return listIemU.get(position);
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        User Item = (User) getItem(position);

        //final View view = inflater.inflate(R.layout.item_uh, container, false);
        convertView = LayoutInflater.from(context).inflate(R.layout.item_users, null);
        TextView tvNombre = (TextView) convertView.findViewById(R.id.itemNombre);
        TextView tvUsuario = (TextView) convertView.findViewById(R.id.itemUsuario);
        ImageView imgPersona = (ImageView) convertView.findViewById(R.id.imgPersona);
        //TextView tvVencimiento = (TextView) convertView.findViewById(R.id.itemVencimiento);

        imgPersona.setImageResource(R.drawable.persona);
        tvNombre.setText(Item.getNombre());
        tvUsuario.setText(Item.getUsuario());
        //tvContenido.setText(Item.getFechanFin());
        return convertView;
    }
}