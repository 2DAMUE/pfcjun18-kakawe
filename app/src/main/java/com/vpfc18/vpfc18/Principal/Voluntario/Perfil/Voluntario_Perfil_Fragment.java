package com.vpfc18.vpfc18.Principal.Voluntario.Perfil;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vpfc18.vpfc18.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Voluntario_Perfil_Fragment extends Fragment {

    TabLayout tab_selector_perfil_voluntario;

    public Voluntario_Perfil_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.voluntario_fragment_perfil, container, false);
        tab_selector_perfil_voluntario = (TabLayout)vista.findViewById(R.id.tab_selector_perfil_voluntario);
        vistaPerfil();

        tab_selector_perfil_voluntario.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition()== 0){
                    vistaPerfil();
                }else{
                    vistaAyudas();
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
        Fragment fragmentoSeleccionado = new Voluntario_Perfil_Fragment_1();
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.contenedor_perfil_voluntario, fragmentoSeleccionado);
        t.commit();
    }
    private void vistaAyudas(){
        Fragment fragmentoSeleccionado = new Voluntario_Perfil_Fragment_2();
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.contenedor_perfil_voluntario, fragmentoSeleccionado);
        t.commit();
    }

}
