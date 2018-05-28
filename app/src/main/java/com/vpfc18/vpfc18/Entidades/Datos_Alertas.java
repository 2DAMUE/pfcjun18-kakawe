package com.vpfc18.vpfc18.Entidades;

public class Datos_Alertas {

    private String nombreAsistido;
    private double latitud;
    private double logitud;
    private String telefono;
    private String nombreAlerta;
    private double distancia;

    public Datos_Alertas() {
    }

    public Datos_Alertas(String nombreAsistido, double latitud, double logitud, String telefono, String nombreAlerta, double distancia) {
        this.nombreAsistido = nombreAsistido;
        this.latitud = latitud;
        this.logitud = logitud;
        this.telefono = telefono;
        this.nombreAlerta = nombreAlerta;
        this.distancia = distancia;
    }

    public String getNombreAsistido() {
        return nombreAsistido;
    }

    public void setNombreAsistido(String nombreAsistido) {
        this.nombreAsistido = nombreAsistido;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNombreAlerta() {
        return nombreAlerta;
    }

    public void setNombreAlerta(String nombreAlerta) {
        this.nombreAlerta = nombreAlerta;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    @Override
    public String toString() {
        return "Datos_Alertas{" +
                "nombreAsistido='" + nombreAsistido + '\'' +
                ", latitud=" + latitud +
                ", logitud=" + logitud +
                ", telefono='" + telefono + '\'' +
                ", nombreAlerta='" + nombreAlerta + '\'' +
                ", distancia=" + distancia +
                '}';
    }
}
