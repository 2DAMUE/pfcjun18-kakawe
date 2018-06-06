package com.vpfc18.vpfc18.Controlador;

import java.util.ArrayList;

public class Comunicador {
    private static ArrayList<Object> valores = new ArrayList<Object>();

    public static void setObjeto(Object newObjeto) {
        valores.add(newObjeto);
    }

    public static ArrayList<Object> getObjeto() {
        return valores;
    }
}
