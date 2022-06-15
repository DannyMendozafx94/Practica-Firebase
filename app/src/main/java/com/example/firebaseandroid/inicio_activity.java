package com.example.firebaseandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class inicio_activity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    FirebaseAuth mAuth;

    DatabaseReference mDatabase;
    ProgressDialog progress;
    ListView listView;
    private   List<String> mLista = new ArrayList<>();
   private ArrayAdapter<String> mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        final Button btnadd = findViewById(R.id.btnagg);
        final EditText txtpelicula = findViewById(R.id.txtpelicula);
        final EditText txtanio = findViewById(R.id.txtanio);
        listView = findViewById(R.id.listView);
        progress = new ProgressDialog(this);

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pelicula = txtpelicula.getText().toString();
                String anio = txtanio.getText().toString();

                if (!pelicula.isEmpty() && !anio.isEmpty()){

                        registrarPelicula(pelicula, anio);
                        txtpelicula.setText("");
                        txtanio.setText("");

                }else {
                    Toast.makeText(inicio_activity.this, "Debe completar todos los campos", Toast.LENGTH_LONG).show();
                }

            }
        });
ObtenerPeliculas();

    }


    private void registrarPelicula(String pelicula, String anio){
        progress.setMessage("Registrando en linea");
        progress.show();

                    Map<String, Object> map = new HashMap<>();
                    map.put("pelicula", pelicula);
                    map.put("año", anio);


                    mDatabase.child("Peliculas").child(pelicula+'-'+anio).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {

                            if (task2.isSuccessful()) {
                                Toast.makeText(inicio_activity.this, "Registro correcto", Toast.LENGTH_LONG).show();

                                // actualizar los registros
                                ObtenerPeliculas();
                            } else {

                                Toast.makeText(inicio_activity.this, "No se pudieron crear los datos", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                progress.dismiss();

    }

    public void ObtenerPeliculas(){


        mDatabase.child("Peliculas").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());

                }
                else {
                    mLista.clear();

                    for (DataSnapshot ds: task.getResult().getChildren()){
                        String pelicula = ds.child("pelicula").getValue().toString();
                        String anio = ds.child("año").getValue().toString();

                        mLista.add("\n Pelicula: " +pelicula+" \n Año: "+anio);
                    }

//                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
//                    mLista.add(String.valueOf(task.getResult().getValue()));

                   mAdapter = new ArrayAdapter<String>(inicio_activity.this, android.R.layout.simple_list_item_1, mLista);

                    listView.setAdapter(mAdapter);

                }
            }
        });
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item_salir) {
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(inicio_activity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}