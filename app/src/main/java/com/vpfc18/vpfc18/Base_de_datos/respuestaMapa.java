package com.vpfc18.vpfc18.Base_de_datos;

public interface respuestaMapa<T> {

    public void onSuccess(T response);
    public void onFailure(Exception e);
}
