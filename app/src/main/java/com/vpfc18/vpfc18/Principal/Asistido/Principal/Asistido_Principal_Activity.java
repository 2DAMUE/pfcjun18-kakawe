package com.vpfc18.vpfc18.Principal.Asistido.Principal;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.vpfc18.vpfc18.Principal.Asistido.Perfil.Asistido_Perfil_Fragment;
import com.vpfc18.vpfc18.R;

public class Asistido_Principal_Activity extends AppCompatActivity {

    android.support.v7.widget.Toolbar asistido_principal_toolbar;

    int perfil = 0;
    String correoUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asistido_activity_principal);

        correoUser = getIntent().getStringExtra("correoUser");
        asistido_principal_toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.asistido_principal_toolbar);
        setSupportActionBar(asistido_principal_toolbar);
        asistido_principal_toolbar.setNavigationIcon(R.drawable.ic_perfil);

        cargaBotones();
        asistido_principal_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (perfil == 0) {
                    cargaPerfil();
                } else {
                    cargaBotones();
                }
            }
        });
    }

    private void cargaPerfil() {
        perfil = 1;
        asistido_principal_toolbar.setNavigationIcon(R.drawable.ic_vacio);
        getSupportActionBar().setTitle("Perfil Usuario");
        Fragment fragmentoSeleccionado = new Asistido_Perfil_Fragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction t = fm.beginTransaction();
        t.replace(R.id.asistido_contenedor_principal, fragmentoSeleccionado);
        t.commit();
        Bundle datos = new Bundle();
        datos.putString("correoUser", correoUser);
        fragmentoSeleccionado.setArguments(datos);
    }





    private void cargaBotones() {
        perfil = 0;
        asistido_principal_toolbar.setNavigationIcon(R.drawable.ic_perfil);
        getSupportActionBar().setTitle("Selector emergencias");
        Fragment fragmentoSeleccionado = new Asistido_Botoneras_Fragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction t = fm.beginTransaction();
        t.replace(R.id.asistido_contenedor_principal, fragmentoSeleccionado);
        t.commit();
        Bundle datos = new Bundle();
        datos.putString("correoUser", correoUser);
        fragmentoSeleccionado.setArguments(datos);
    }

}
