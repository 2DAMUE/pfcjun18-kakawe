package com.vpfc18.vpfc18.Inicio;

import android.graphics.Color;
import android.media.Image;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.vpfc18.vpfc18.R;

public class Inicio_Activity extends AppCompatActivity {

    TabLayout tab_selector;

    ImageView iv_background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        //Precargamos el login de inicio
        cargaLogin();

        iv_background = (ImageView) findViewById(R.id.iv_background);



        //Relaccionamos el Tab, para seleccionar una pestaña u otra(Login o registro)
        tab_selector = (TabLayout) findViewById(R.id.tab_selector);
        tab_selector.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition()== 0){
                    cargaLogin();
                    conectar();
                }else{
                    cargaRegistro();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    //Codigo par ala conexión con la BBDD
    private void conectar(){


    }
    private void cargaLogin(){
        Fragment fragmentoSeleccionado2 = new Login_Fragment();
        FragmentManager fm2 = getSupportFragmentManager();
        FragmentTransaction t2 = fm2.beginTransaction();
        t2.replace(R.id.contenedor_Inicio, fragmentoSeleccionado2);
        t2.commit();
    }

    private void cargaRegistro(){
        Fragment fragmentoSeleccionado2 = new Registro_Fragment_1();
        FragmentManager fm2 = getSupportFragmentManager();
        FragmentTransaction t2 = fm2.beginTransaction();
        t2.replace(R.id.contenedor_Inicio, fragmentoSeleccionado2);
        t2.commit();
    }
}
