package com.vpfc18.vpfc18.Principal.Asistido.Principal;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.vpfc18.vpfc18.Principal.Asistido.Perfil.Asistido_Perfil_Fragment;
import com.vpfc18.vpfc18.Principal.Salir_App_Dialog;
import com.vpfc18.vpfc18.R;

public class Asistido_Principal_Activity extends AppCompatActivity {

    android.support.v7.widget.Toolbar asistido_principal_toolbar;

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 11;
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
        getSupportActionBar().setTitle("Selector emergencias");
        asistido_principal_toolbar.setNavigationIcon(R.drawable.ic_perfil);
        checkPermission();

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

    private void mensajeSalir() {
        Salir_App_Dialog vld = new Salir_App_Dialog();
        vld.show(getFragmentManager(), "Salir");
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.barra_asistido,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Para añadir el método cuando presione el menu derecho
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.cerrarSesion:
                mensajeSalir();
            default:

        }
        return super.onOptionsItemSelected(item);
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

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        } else {
            int permissionCheck = ContextCompat.checkSelfPermission(
                    getApplication(), Manifest.permission.ACCESS_FINE_LOCATION);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                Log.i("PERMISOS1", "No se tiene permiso para acceder a tu ubicación1.");
                ActivityCompat.requestPermissions(Asistido_Principal_Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 11);
                cargaBotones();
            } else {
                Log.i("PERMISOS2", "Se tiene permiso para acceder a tu ubicación!");
                cargaBotones();
            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE_ASK_PERMISSIONS:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // El usuario acepto los permisos.
                    Log.i("PERMISOS3", "No se tiene permiso para acceder a tu ubicación2");
                }else{
                    // Permiso denegado.
                    Log.i("PERMISOS4", "No se tiene permiso para acceder a tu ubicación3.");
                    checkPermission();
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
