package com.vpfc18.vpfc18.Principal.Asistido.Perfil;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vpfc18.vpfc18.Principal.Voluntario.Perfil.Voluntario_Perfil_Fragment_1;
import com.vpfc18.vpfc18.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Asistido_Perfil_Fragment extends Fragment {

    TabLayout tab_selector_perfil_asistido;
    public Asistido_Perfil_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View vista = inflater.inflate(R.layout.asistido_fragment_perfil, container, false);
        tab_selector_perfil_asistido = (TabLayout)vista.findViewById(R.id.tab_selector_perfil_asistido);
        vistaPerfil();
        tab_selector_perfil_asistido.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition()== 0){
                    vistaPerfil();
                }else{
                    vistaDatosMedicos();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        return vista;
    }
    private void vistaPerfil() {
        Fragment fragmentoSeleccionado = new Asistido_Perfil_Fragment_1();
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.contenedor_perfil_asistido, fragmentoSeleccionado);
        t.commit();
    }
    private void vistaDatosMedicos() {
        Fragment fragmentoSeleccionado = new Asistido_Perfil_Fragment_2();
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.contenedor_perfil_asistido, fragmentoSeleccionado);
        t.commit();
    }

}
