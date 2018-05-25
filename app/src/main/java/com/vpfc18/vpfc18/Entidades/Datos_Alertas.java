package com.vpfc18.vpfc18.Entidades;

public class Datos_Alertas {

    private String id_dependiente;
    private String id_tipoAlerta;
    private double latitud;
    private double logitud;

    public Datos_Alertas() {
    }

    public Datos_Alertas(String id_dependiente, String id_tipoAlerta, double latitud, double logitud) {
        this.id_dependiente = id_dependiente;
        this.id_tipoAlerta = id_tipoAlerta;
        this.latitud = latitud;
        this.logitud = logitud;
    }

    public String getId_dependiente() {
        return id_dependiente;
    }

    public void setId_dependiente(String id_dependiente) {
        this.id_dependiente = id_dependiente;
    }

    public String getId_tipoAlerta() {
        return id_tipoAlerta;
    }

    public void setId_tipoAlerta(String id_tipoAlerta) {
        this.id_tipoAlerta = id_tipoAlerta;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLogitud() {
        return logitud;
    }

    public void setLogitud(double logitud) {
        this.logitud = logitud;
    }
}
