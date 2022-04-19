package com.example.household;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    EditText mFullName, mEmail, mPassword;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullName    = findViewById(R.id.fullName);
        mEmail       = findViewById(R.id.Email);
        mPassword    = findViewById(R.id.Password);
        mRegisterBtn = findViewById(R.id.RegisterBtn);
        mLoginBtn    = findViewById(R.id.CreateTxt);
        fAuth        = FirebaseAuth.getInstance();

        //Verificamos que el usuario este registrado o no
        /*if(fAuth.getCurrentUser() != null){
            //Redirección
            startActivity(new Intent(getApplicationContext(),Register.class));
            finish();
        }*/

        mRegisterBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    mEmail.setError("El Correo es obligatorio");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    mPassword.setError("La Contraseña es obligatoria");
                    return;
                }

                if(password.length() < 6){
                    mPassword.setError("La contraseña debe ser mayor a 6 caracteres");
                    return;
                }

                //Registramos al usuario en firebase
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this,"Usuario Creado", Toast.LENGTH_SHORT).show();
                            //Redirección
                            startActivity(new Intent(getApplicationContext(),CrudCategoria.class));

                        }else{
                            Toast.makeText(Register.this,"Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
    }
}