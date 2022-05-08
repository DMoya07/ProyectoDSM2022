package com.example.household.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.household.Models.Producto;
import com.example.household.R;

import java.util.ArrayList;

public class ListViewProductosAdapter extends BaseAdapter {

    Context context;
    ArrayList<Producto> productoData;
    LayoutInflater layoutInflater;
    Producto productoModel;

    public ListViewProductosAdapter(Context context, ArrayList<Producto> productoData) {
        this.context = context;
        this.productoData = productoData;
        layoutInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
        );
    }

    @Override
    public int getCount() {
        return productoData.size();
    }

    @Override
    public Object getItem(int i) {
        return productoData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = view;
        if (rowView == null) {
            rowView = layoutInflater.inflate(R.layout.lista_productos,
                    null,
                    true);
        }
        //Enlazamos vista
        TextView nombreproducto = rowView.findViewById(R.id.NombreProducto);

        productoModel = productoData.get(i);
        nombreproducto.setText(productoModel.getnombreProducto());

        return rowView;
    }
}
