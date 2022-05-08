package com.example.household;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.household.Adaptadores.ListViewCategoriasAdapter;
import com.example.household.Models.Categoria;
import com.example.household.Models.Producto;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class CrudProductos extends AppCompatActivity {
//    Button BtnProductos;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference databaseReferenceProducts;
    DatabaseReference databaseReferenceProduct;
    private String extrasProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_productos);
          extrasProduct = getIntent().getStringExtra("idCategoria");

        inicializarFirebase();
        listaProductos();
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Categorias");
        databaseReferenceProduct = databaseReference.child(extrasProduct);//AQUI DEBE RECIBIR EL ID DE LA CATEGORIA
        databaseReferenceProducts = databaseReferenceProduct.child("Productos");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.crud_producto_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        String nombrecategoria = inputNombreCategoria.getText().toString();
        switch (item.getItemId()){
            case R.id.menu_agregar_producto:
                insertarProducto();
                break;
            case R.id.menu_guardar_producto:
//                if(categoriaSeleccionada != null){
//                    if(validarInputs()==false){
//                        Categoria c = new Categoria();
//                        c.setIdCategoria(categoriaSeleccionada.getIdCategoria());
//                        c.setNombrecategoria(nombrecategoria);
//                        c.setTimestamp(categoriaSeleccionada.getTimestamp());
//                        databaseReference.child("Categorias").child(c.getIdCategoria()).setValue(c);
//                        Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
//                        linearLayoutEditar.setVisibility(View.GONE);
//                        categoriaSeleccionada = null;
//                    }else{
//                        Toast.makeText(this, "Seleccione una Categoría", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                break;
            case R.id.menu_eliminar_producto:
//                if (categoriaSeleccionada != null){
                    Producto p2 = new Producto();
//                    p2.setIdCategoria(categoriaSeleccionada.getIdCategoria());

                databaseReferenceProduct.child("Productos").child(p2.getIdCategoria()).removeValue();
                    Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show();
//                    linearLayoutEditar.setVisibility(View.GONE);
//                    categoriaSeleccionada = null;
//                }else{
//                    Toast.makeText(this, "Seleccione un producto", Toast.LENGTH_SHORT).show();
//                }
                break;
            case R.id.menu_logout_producto:
//                cerrarsesion();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void insertarProducto(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(
                CrudProductos.this
        );
        View mView = getLayoutInflater().inflate(R.layout.insert_producto,null);
        Button btnInsertar = (Button) mView.findViewById(R.id.BtnInsertar);
        final EditText mInputNombreProducto = (EditText) mView.findViewById(R.id.inputNombreProducto);
        final EditText mInputCantidadProducto = (EditText) mView.findViewById(R.id.inputCantidadProducto);
        final EditText mInputFecVenProducto = (EditText) mView.findViewById(R.id.inputFecVenProducto);


        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        btnInsertar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String nombreProducto = mInputNombreProducto.getText().toString();
                String cantProducto = mInputCantidadProducto.getText().toString();
                String fechaVenProducto = mInputFecVenProducto.getText().toString();

                if (nombreProducto.isEmpty()){
//                    showError(mInputNombreCategoria,"No puede dejar el campo vacío");
                }else{
                    Producto c = new Producto();
                    c.setidProducto(UUID.randomUUID().toString());
                    c.setnombreProducto(nombreProducto);
                    c.setCantProducto(cantProducto);
                    c.setFechaVencimiento(fechaVenProducto);

                    databaseReferenceProducts.child(c.getidProducto()).setValue(c);
                    Toast.makeText(CrudProductos.this, "Producto registrado Correctamente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void listaProductos() {
//        databaseReference.child("NombreProducto").orderByChild("NombreProducto").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                listCategorias.clear();
//                for (DataSnapshot objSanptshot : snapshot.getChildren()){
//                    Categoria c = objSanptshot.getValue(Categoria.class);
//                    listCategorias.add(c);
//                }
//                //Iniciamos nuestro adaptador
//                listViewCategoriasAdapter = new ListViewCategoriasAdapter(CrudCategoria.this, listCategorias);
//                /*arrayAdapterCategoria = new ArrayAdapter<Categoria>(
//                        CrudCategoria.this,
//                        android.R.layout.simple_list_item_1,
//                        listCategoria
//                );*/
//                listViewCategorias.setAdapter(listViewCategoriasAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }
}