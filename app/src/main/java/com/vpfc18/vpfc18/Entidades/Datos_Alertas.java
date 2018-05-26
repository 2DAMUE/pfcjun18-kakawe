package com.vpfc18.vpfc18.Entidades;

public class Datos_Alertas {

    private String id_dependiente;
    private double latitud;
    private double logitud;

    public Datos_Alertas() {
    }

    public Datos_Alertas(String id_dependiente, double latitud, double logitud) {
        this.id_dependiente = id_dependiente;
        this.latitud = latitud;
        this.logitud = logitud;
    }

    public String getId_dependiente() {
        return id_dependiente;
    }

    public void setId_dependiente(String id_dependiente) {
        this.id_dependiente = id_dependiente;
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

    @Override
    public String toString() {
        return "Datos_Alertas{" +
                "id_dependiente='" + id_dependiente + '\'' +
                ", latitud=" + latitud +
                ", logitud=" + logitud +
                '}';
    }
}
