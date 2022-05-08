package com.example.household;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.household.Adaptadores.ListViewProductosAdapter;
import com.example.household.Models.Categoria;
import com.example.household.Models.Producto;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.household.CrudCategoria;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class CrudProductos extends AppCompatActivity {
//    Button BtnProductos;
    FirebaseAuth fAuth;
    private ArrayList<Producto> listProductos = new ArrayList<Producto>();
    ArrayAdapter<Producto> productoArrayAdapter;
    ListViewProductosAdapter listViewProductosAdapter;
    LinearLayout linearLayoutEditar;
    ListView listViewProductos;

    EditText inptNombreProducto;
    Button btnCancelar;
    Producto productoSeleccionado;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference databaseReferenceProducts;
    DatabaseReference databaseReferenceProduct;
    private String extrasProduct;

    GoogleSignInClient mgoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_productos);
          extrasProduct = getIntent().getStringExtra("idCategoria");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();
        mgoogleSignInClient = GoogleSignIn.getClient(this, gso);

        listViewProductos = findViewById(R.id.ListViewProductos);
        linearLayoutEditar = findViewById(R.id.linearLayoutEditarProducto);

        btnCancelar = findViewById(R.id.BtnCancelarProd);
        inptNombreProducto = findViewById(R.id.inputNombreProducto);

        listViewProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                productoSeleccionado = (Producto) adapterView.getItemAtPosition(i);
                inptNombreProducto.setText(productoSeleccionado.getnombreProducto());
                //listViewProducto.findViewById(R.id.inputNombreCategoria);
                //Hacemos visible el layout de edición
                linearLayoutEditar.setVisibility(View.VISIBLE);
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                linearLayoutEditar.setVisibility(View.GONE);
                productoSeleccionado = null;
            }
        });

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

    //Update and Delete productos
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String nombreproducto = inptNombreProducto.getText().toString();
        switch (item.getItemId()){
            case R.id.menu_agregar_producto:
                insertarProducto();
                break;
            case R.id.menu_guardar_producto:
                if(productoSeleccionado != null){
                    if(validarInputs()==false){
                        Producto p = new Producto();
                        p.setidProducto(productoSeleccionado.getidProducto());
                        p.setnombreProducto(nombreproducto);
                        p.setTimestamp(productoSeleccionado.getTimestamp());
                        databaseReference.child("Producto").child(p.getidProducto()).setValue(p);
                        Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                        linearLayoutEditar.setVisibility(View.GONE);
                        productoSeleccionado = null;
                    }else{
                        Toast.makeText(this, "Seleccione un Producto", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.menu_eliminar_producto:
                if (productoSeleccionado != null){
                    Producto p2 = new Producto();
                    p2.setidProducto(productoSeleccionado.getidProducto());
                    databaseReferenceProduct.child("Productos").child(p2.getidProducto()).removeValue();
                    Toast.makeText(this, "Producto eliminado correctamente", Toast.LENGTH_SHORT).show();
                    linearLayoutEditar.setVisibility(View.GONE);
                    productoSeleccionado = null;
                }else{
                    Toast.makeText(this, "Seleccione un producto", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_logout_producto:
                cerrarsesion();
                break;

        }

        return super.onOptionsItemSelected(item);
    }
    //Insertar producto
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
                    validarInputs();
                }else{
                    Producto p = new Producto();
                    p.setidProducto(UUID.randomUUID().toString());
                    p.setnombreProducto(nombreProducto);
                    p.setCantProducto(cantProducto);
                    p.setFechaVencimiento(fechaVenProducto);
                    p.setTimestamp(getFechaMilisegundos()*-1);

                    databaseReferenceProducts.child(p.getidProducto()).setValue(p);
                    Toast.makeText(CrudProductos.this, "Producto registrado Correctamente", Toast.LENGTH_SHORT).show();
                    dialog.hide();
                }
            }
        });
    }
    public long getFechaMilisegundos(){
        Calendar calendar = Calendar.getInstance();
        long tiempounix = calendar.getTimeInMillis();

        return tiempounix;
    }
    //Listar Productos
    private void listaProductos() {
        databaseReferenceProducts.orderByChild("nombreProducto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listProductos.clear();
                for (DataSnapshot objSanptshot : snapshot.getChildren()){
                    Producto p = objSanptshot.getValue(Producto.class);
                    listProductos.add(p);
                }
                //Iniciamos nuestro adaptador
                listViewProductosAdapter = new ListViewProductosAdapter(CrudProductos.this, listProductos);
                /*arrayAdapterCategoria = new ArrayAdapter<Categoria>(
                        CrudCategoria.this,
                        android.R.layout.simple_list_item_1,
                        listCategoria
                );*/
                listViewProductos.setAdapter(listViewProductosAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    public boolean validarInputs(){
        String nombreproducto = inptNombreProducto.getText().toString();
        if (nombreproducto.isEmpty()){
            showError(inptNombreProducto,"No puede dejar el campo vacío");
            return true;
        }else{
            return false;
        }
    }
    private Boolean CheckLoggin(){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            return true;
        }else{
            return false;
        }
    }
    public void showError(EditText input, String s){
        input.requestFocus();
        input.setError(s);
    }
    public void cerrarsesion(){
        if(CheckLoggin()){
            mgoogleSignInClient.signOut();
        }else{
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            fAuth.signOut();
        }
        startActivity(new Intent(this, Login.class));
        finish();
    }
}