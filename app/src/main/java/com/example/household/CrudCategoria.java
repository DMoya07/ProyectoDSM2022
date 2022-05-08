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

import com.example.household.Adaptadores.ListViewCategoriasAdapter;
import com.example.household.Models.Categoria;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class CrudCategoria extends AppCompatActivity {
    FirebaseAuth fAuth;
    private ArrayList<Categoria> listCategorias = new ArrayList<Categoria>();
    ArrayAdapter<Categoria> arrayAdapterCategoria;
    ListViewCategoriasAdapter listViewCategoriasAdapter;
    LinearLayout linearLayoutEditar;
    ListView listViewCategorias;

    EditText inputNombreCategoria;
    Button btnCancelar,btnProductos;

    Categoria categoriaSeleccionada;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    GoogleSignInClient mgoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_categoria);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();
        mgoogleSignInClient = GoogleSignIn.getClient(this, gso);

        inputNombreCategoria = findViewById(R.id.inputNombreCategoria);
        btnCancelar = findViewById(R.id.BtnCancelar);
        btnProductos = findViewById(R.id.BtnVerProductos);

        listViewCategorias = findViewById(R.id.ListViewCategorias);
        linearLayoutEditar = findViewById(R.id.linearLayoutEditar);

        listViewCategorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                categoriaSeleccionada = (Categoria) adapterView.getItemAtPosition(i);
                inputNombreCategoria.setText(categoriaSeleccionada.getNombrecategoria());
                //listViewProducto.findViewById(R.id.inputNombreCategoria);
                //Hacemos visible el layout de edición
                linearLayoutEditar.setVisibility(View.VISIBLE);
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                linearLayoutEditar.setVisibility(View.GONE);
                categoriaSeleccionada = null;
            }
        });

        btnProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(),CrudProductos.class);
                i.putExtra("idCategoria", categoriaSeleccionada.getIdCategoria());
                startActivity(i);
                //finish();
            }
        });
        inicializarFirebase();
        listaCategorias();
    }

    private Boolean CheckLoggin(){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            return true;
        }else{
            return false;
        }
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }


    private void listaCategorias() {
        databaseReference.child("Categorias").orderByChild("nombrecategoria").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listCategorias.clear();
                for (DataSnapshot objSanptshot : snapshot.getChildren()){
                    Categoria c = objSanptshot.getValue(Categoria.class);
                    listCategorias.add(c);
                }
                //Iniciamos nuestro adaptador
                listViewCategoriasAdapter = new ListViewCategoriasAdapter(CrudCategoria.this, listCategorias);
                /*arrayAdapterCategoria = new ArrayAdapter<Categoria>(
                        CrudCategoria.this,
                        android.R.layout.simple_list_item_1,
                        listCategoria
                );*/
                listViewCategorias.setAdapter(listViewCategoriasAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.crud_categoria_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        View mView = getLayoutInflater().inflate(R.layout.insertar_categoria,null);
        String nombrecategoria = inputNombreCategoria.getText().toString();
        Button btnListproductos = (Button) mView.findViewById(R.id.BtnVerProductos);

        switch (item.getItemId()){
            case R.id.menu_agregar:
                insertar();
                break;
            case R.id.menu_guardar:
                if(categoriaSeleccionada != null){
                    if(validarInputs()==false){
                        Categoria c = new Categoria();
                        c.setIdCategoria(categoriaSeleccionada.getIdCategoria());
                        c.setNombrecategoria(nombrecategoria);
                        c.setTimestamp(categoriaSeleccionada.getTimestamp());
                        databaseReference.child("Categorias").child(c.getIdCategoria()).setValue(c);
                        Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                        linearLayoutEditar.setVisibility(View.GONE);
                        categoriaSeleccionada = null;
                    }else{
                        Toast.makeText(this, "Seleccione una Categoría", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.menu_eliminar:
                if (categoriaSeleccionada != null){
                    Categoria c2 = new Categoria();
                    c2.setIdCategoria(categoriaSeleccionada.getIdCategoria());
                    databaseReference.child("Categorias").child(c2.getIdCategoria()).removeValue();
                    Toast.makeText(this, "Categoría eliminada", Toast.LENGTH_SHORT).show();

                    linearLayoutEditar.setVisibility(View.GONE);
                    categoriaSeleccionada = null;
                }else{
                    Toast.makeText(this, "Seleccione una categoría", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_logout:
                cerrarsesion();
                break;
            case R.id.BtnVerProductos:
                Intent intent = new Intent(this, CrudProductos.class);
                String idCategoria = categoriaSeleccionada.getIdCategoria().toString();
                intent.putExtra("idCategoria",idCategoria);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    public void insertar(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(
                CrudCategoria.this
        );
        View mView = getLayoutInflater().inflate(R.layout.insertar_categoria,null);
        Button btnInsertar = (Button) mView.findViewById(R.id.BtnInsertar);
        final EditText mInputNombreCategoria = (EditText) mView.findViewById(R.id.inputNombreCategoria);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        btnInsertar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String nombrecategoria = mInputNombreCategoria.getText().toString();
                if (nombrecategoria.isEmpty()){
                    showError(mInputNombreCategoria,"No puede dejar el campo vacío");
                }else{
                    Categoria c = new Categoria();
                    c.setIdCategoria(UUID.randomUUID().toString());
                    c.setNombrecategoria(nombrecategoria);
                    c.setTimestamp(getFechaMilisegundos()*-1);

                    databaseReference.child("Categorias").child(c.getIdCategoria()).setValue(c);
                    Toast.makeText(CrudCategoria.this, "Registrado Correctamente", Toast.LENGTH_SHORT).show();
                    dialog.hide();
                }
            }
        });
    }

    public void showError(EditText input, String s){
        input.requestFocus();
        input.setError(s);
    }

    public long getFechaMilisegundos(){
        Calendar calendar = Calendar.getInstance();
        long tiempounix = calendar.getTimeInMillis();

        return tiempounix;
    }

    public boolean validarInputs(){
        String nombrecategoria = inputNombreCategoria.getText().toString();
        if (nombrecategoria.isEmpty()){
            showError(inputNombreCategoria,"No puede dejar el campo vacío");
            return true;
        }else{
            return false;
        }
    }
    public void cerrarsesion(){
        if(CheckLoggin()){
            mgoogleSignInClient.signOut();
        }else{
            FirebaseAuth fAuth = FirebaseAuth.getInstance();
            fAuth.signOut();
        }
        startActivity(new Intent(this, Login.class));
        finish();
    }
}