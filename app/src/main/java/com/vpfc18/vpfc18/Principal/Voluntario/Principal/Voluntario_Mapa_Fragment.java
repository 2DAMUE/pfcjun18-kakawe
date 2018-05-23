package com.vpfc18.vpfc18.Principal.Voluntario.Principal;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vpfc18.vpfc18.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Voluntario_Mapa_Fragment extends Fragment implements OnMapReadyCallback {
//Toma Adri cabron
    private GoogleMap mGoogleMaps;
    private MapView mMapView;
    private static final int REQUEST_FINE_LOCATION = 11;
    private double longitud;
    private double latitud;
    private View mView;
    //private static final LatLng PERTH = new LatLng(-31.952854, 115.857342);
    //private Marker mPerth;

    public Voluntario_Mapa_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.voluntario_fragment_mapa_, container, false);
        return mView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) mView.findViewById(R.id.map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);

        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        mGoogleMaps = googleMap;
        mGoogleMaps.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (checkPermissions()) {
            setMyLocationEnabled();
        }
        /*
        mGoogleMaps.moveCamera(CameraUpdateFactory.newLatLngZoom(PERTH, 10));
        mPerth = mGoogleMaps.addMarker(new MarkerOptions()
                .position(PERTH)
                .snippet("Dale cañaaaaaaaaaa")
                .title("Perth"));

         */
    }

    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions();
            return false;
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_FINE_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setMyLocationEnabled();
                } else {
                }
            }
        }
    }

    private void setMyLocationEnabled() {
        mGoogleMaps.getUiSettings().setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMaps.setMyLocationEnabled(true);
        mGoogleMaps.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                //TODO:
                longitud = location.getLongitude();
                latitud = location.getLatitude();
                Log.v("lat", String.valueOf(latitud));
                //setLatLng(location.getLatitude(),location.getLongitude());
                final LatLng actual = new LatLng(latitud, longitud);
                mGoogleMaps.clear();
                //mMap.addMarker(new MarkerOptions().position(actual).title("Ubicación actual").snippet("Esta es la posición actual del usuario"));
                mGoogleMaps.addMarker(new MarkerOptions().position(actual));
                mGoogleMaps.moveCamera(CameraUpdateFactory.newLatLngZoom(actual, 15));
                // mMap.setInfoWindowAdapter();
                mGoogleMaps.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        Toast.makeText(getActivity().getApplicationContext(), "has pulsado en el marcador y su posición " + actual, Toast.LENGTH_LONG).show();
/*
                        DialogDetalleMarket ddm = new DialogDetalleMarket();
                        ddm.show(getFragmentManager(), "ddm");

*/

                        return false;
                    }
                });
                Log.v("actual", String.valueOf(actual));
            }
        });
    }
}
