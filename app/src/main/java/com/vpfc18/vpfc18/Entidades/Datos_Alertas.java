package com.vpfc18.vpfc18.Entidades;

public class Datos_Alertas {

    private int id_alerta;
    private String nombreAsistido;
    private double latitud;
    private double longitud;
    private String telefono;
    private String nombreAlerta;
    private double distancia;
    private String id_asistente;

    public Datos_Alertas() {
    }

    public Datos_Alertas(int id_alerta, String nombreAsistido, double latitud, double longitud, String telefono, String nombreAlerta, double distancia, String id_asistente) {
        this.id_alerta = id_alerta;
        this.nombreAsistido = nombreAsistido;
        this.latitud = latitud;
        this.longitud = longitud;
        this.telefono = telefono;
        this.nombreAlerta = nombreAlerta;
        this.distancia = distancia;
        this.id_asistente = id_asistente;
    }

    public int getId_alerta() {
        return id_alerta;
    }

    public void setId_alerta(int id_alerta) {
        this.id_alerta = id_alerta;
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

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
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

    public String getId_asistente() {
        return id_asistente;
    }

    public void setId_asistente(String id_asistente) {
        this.id_asistente = id_asistente;
    }

    @Override
    public String toString() {
        return "Datos_Alertas{" +
                "id_alerta=" + id_alerta +
                ", nombreAsistido='" + nombreAsistido + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", telefono='" + telefono + '\'' +
                ", nombreAlerta='" + nombreAlerta + '\'' +
                ", distancia=" + distancia +
                ", id_asistente='" + id_asistente + '\'' +
                '}';
    }
}