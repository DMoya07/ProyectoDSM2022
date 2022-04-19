package com.example.household.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.household.Models.Categoria;
import com.example.household.R;

import java.util.ArrayList;

public class ListViewCategoriasAdapter extends BaseAdapter {

    Context context;
    ArrayList<Categoria> categoriaData;
    LayoutInflater layoutInflater;
    Categoria categoriaModel;

    public ListViewCategoriasAdapter(Context context, ArrayList<Categoria> categoriaData) {
        this.context = context;
        this.categoriaData = categoriaData;
        layoutInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
        );
    }

    @Override
    public int getCount() {
        return categoriaData.size();
    }

    @Override
    public Object getItem(int i) {
        return categoriaData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = view;
        if (rowView == null) {
            rowView = layoutInflater.inflate(R.layout.lista_categorias,
                    null,
                    true);
        }
        //Enlazamos vista
        TextView nombrecategoria = rowView.findViewById(R.id.NombreCategoria);

        categoriaModel = categoriaData.get(i);
        nombrecategoria.setText(categoriaModel.getNombrecategoria());

        return rowView;
    }
}
