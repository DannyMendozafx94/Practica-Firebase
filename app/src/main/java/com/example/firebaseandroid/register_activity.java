package com.example.firebaseandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class register_activity extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final TextView btnloguearse = findViewById(R.id.btnloguearse);
        final Button btnregister = findViewById(R.id.btnregister);

        final EditText txtnombre = findViewById(R.id.txtnombre);
        final EditText txtedad = findViewById(R.id.txtedad);
        final EditText txtemail = findViewById(R.id.txtemail);
        final EditText txtpass = findViewById(R.id.txtpass);


        progress = new ProgressDialog(this);

        btnloguearse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(register_activity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombres = txtnombre.getText().toString();
                String edad = txtedad.getText().toString();
                String correo = txtemail.getText().toString();
                String contrasena = txtpass.getText().toString();

                if (!nombres.isEmpty() && !edad.isEmpty() && !correo.isEmpty() &&!contrasena.isEmpty() ){
                    if (contrasena.length() < 6){
                        Toast.makeText(register_activity.this, "La contraseña debe ser mayor o igual a 6 caracteres", Toast.LENGTH_LONG).show();

                    }else{
                        registrarUsuario(correo, contrasena, nombres, edad);
                    }

                }else {
                    Toast.makeText(register_activity.this, "Debe completar todos los campos", Toast.LENGTH_LONG).show();
                }

            }
        });


    }


    private void registrarUsuario(String correo, String contrasena, String nombre, String edad){
        progress.setMessage("Registrando en linea");
        progress.show();
        mAuth.createUserWithEmailAndPassword(correo, contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {


                    Map<String, Object> map = new HashMap<>();
                    map.put("nombres", nombre);
                    map.put("edad", edad);
                    map.put("correo", correo);
                    map.put("contrasena", contrasena);
                    String id = mAuth.getCurrentUser().getUid();

                    mDatabase.child("Usuarios").child(nombre).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {

                            if (task2.isSuccessful()) {
                                Log.d("ddddddddddddddddddddddd", map.toString());

                                Intent i = new Intent(register_activity.this, inicio_activity.class);
                                startActivity(i);
                                finish();
                            } else {

                                Toast.makeText(register_activity.this, "No se pudieron crear los datos", Toast.LENGTH_LONG).show();


                            }

                        }
                    });
                }
                else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(register_activity.this, "Este usuario ya existe", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(register_activity.this, "No se pudo registrar este usuario", Toast.LENGTH_LONG).show();

                    }
                }

                progress.dismiss();

            }

        });

    }






}