package com.vpfc18.vpfc18.Base_de_datos;

/**
 * Created by eloy on 2/6/18.
 */

public interface OnResponseListener<T> {

    public void onSuccess(T response);
    public void onFailure(Exception e);
}