package com.vpfc18.vpfc18.Principal;

import android.Manifest;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.vpfc18.vpfc18.R;

public class PruebaActivity extends AppCompatActivity {

    private Button btn_fragment_mapa, btn_fragment_lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);

        btn_fragment_lista = (Button) findViewById(R.id.btn_fragment_lista);
        btn_fragment_mapa = (Button) findViewById(R.id.btn_fragment_mapa);

        btn_fragment_lista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ListaMapaFragment fragmentoSeleccionado2 = new ListaMapaFragment();
                FragmentManager fm2 = getSupportFragmentManager();
                FragmentTransaction t2 = fm2.beginTransaction();
                t2.replace(R.id.contenedor, fragmentoSeleccionado2);
                t2.commit();

            }
        });

        btn_fragment_mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MapaFragment fragmentoSeleccionado2 = new MapaFragment();
                FragmentManager fm2 = getSupportFragmentManager();
                FragmentTransaction t2 = fm2.beginTransaction();
                t2.replace(R.id.contenedor, fragmentoSeleccionado2);
                t2.commit();

            }
        });


    }

}


