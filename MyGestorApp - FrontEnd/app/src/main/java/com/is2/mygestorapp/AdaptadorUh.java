package com.is2.mygestorapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class AdaptadorUh extends BaseAdapter{
    private Context context;
    private ArrayList<Uh> listIem;

    public AdaptadorUh(Context context, ArrayList<Uh> listIem){
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
        ImageView ivTarea = (ImageView) convertView.findViewById(R.id.itemImage);
        TextView tvTitulo = (TextView) convertView.findViewById(R.id.itemTitulo);
        TextView tvEstado = (TextView) convertView.findViewById(R.id.itemEstado);
        TextView tvVencimiento = (TextView) convertView.findViewById(R.id.itemVencimiento);

        ivTarea.setImageResource(R.drawable.tarea);
        tvTitulo.setText(Item.getTituloUs());
        tvEstado.setText(Item.getEstado());
        tvVencimiento.setText(Item.getDiasVto());

        return convertView;
    }
}
