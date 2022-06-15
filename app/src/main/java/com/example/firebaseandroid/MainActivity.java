package com.example.firebaseandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    ProgressDialog progress;


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent i = new Intent(MainActivity.this, inicio_activity.class);
            startActivity(i);
            finish();
       //     currentUser.reload();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();


        final TextView btnregister = findViewById(R.id.btnregister);
        final Button btnLogin = findViewById(R.id.btnlogin);

        final EditText txtemail = findViewById(R.id.txtemail);
        final EditText txtpass = findViewById(R.id.txtpass);

        progress = new ProgressDialog(this);

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, register_activity.class);
                startActivity(i);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   String correo = txtemail.getText().toString();
                   String contrasena = txtpass.getText().toString();
                    if (!correo.isEmpty() && !contrasena.isEmpty()) {


                        progress.setMessage("Iniciando");
                        progress.show();
                        mAuth.signInWithEmailAndPassword(correo, contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    Intent listaHabitaciones = new Intent(MainActivity.this, inicio_activity.class);
                                    startActivity(listaHabitaciones);
                                    finish();
                                    Toast.makeText(MainActivity.this, "Bienvenido", Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(MainActivity.this, "Credenciales incorrectas", Toast.LENGTH_LONG).show();

                                }
                                progress.dismiss();

                            }

                        });
                    }else{
                        Toast.makeText(MainActivity.this, "Credenciales incorrectas", Toast.LENGTH_LONG).show();

                    }



                }


        });

    }



}