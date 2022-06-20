package com.firstapp.fa_mayanksanjeevnibhoria_c0854281_android;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener,
        LocationListener {

    EditText et1,et2;
    CheckBox cb;
    Button btn;

    String pname,address,pdate,visited,id2,placename2,address2,latitude2,longitude2,visited2,pdate2;;
    double latitude,longitude, latitude_add,longitude_add;

    private GoogleMap mMap;
    Marker marker;
    Marker marker_add;
    MarkerOptions markerOptions_add;

    String address1;

    int d,m,y;
    String date;

    DbOperations dbOperations;

    LocationManager lm;

    int i=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_add, container, false);

        et1=view.findViewById(R.id.editText1);
        et2=view.findViewById(R.id.editText2);
        cb=view.findViewById(R.id.checkBox);
        btn=view.findViewById(R.id.button);

        dbOperations=new DbOperations(getActivity());

        fetchLocation();


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pname=et1.getText().toString().trim();
                address=et2.getText().toString().trim();

                if(pname.equals(""))
                {
                    et1.setError("enter place name");
                }
                else if(address.equals(""))
                {
                    et2.setError("enter address");
                }
                else
                {
                    if(cb.isChecked())
                    {
                        visited="Visited";
                    }
                    else
                    {
                        visited="Not visited yet";
                    }

                    BeanPlaces b = new BeanPlaces();
                    b.setPname(pname);
                    b.setPaddr(address);
                    b.setPlat(String.valueOf(latitude_add));
                    b.setPlng(String.valueOf(longitude_add));
                    b.setPv(visited);
                    b.setPdate(pdate);

                    boolean c = dbOperations.insert(b);

                    if(c)
                    {
                        Toast.makeText(getActivity(), "saved", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
    }

    @SuppressLint("MissingPermission")
    private void fetchLocation()
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

                return;
            }
            else
            {
                lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 2, this);

            }
        }
        else
        {

            lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 2, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
                break;
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap)
    {
        mMap=googleMap;

        mMap.clear();

        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).draggable(true).title(address1);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.addMarker(markerOptions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                mMap.setMyLocationEnabled(true);
            }
            else
            {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));

        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng)
    {
        mMap.clear();

        if(marker_add!=null)
        {
            marker_add.remove();
        }
        latitude_add=latLng.latitude;
        longitude_add=latLng.longitude;

        markerOptions_add = new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude)).draggable(true).title("New Marker");

        markerOptions_add.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        marker_add=mMap.addMarker(markerOptions_add);

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

        List<Address> addresses = null;
        try {
            Log.e("latitude.....", ""+latitude_add);
            Log.e("longitude.....", ""+longitude_add);

            addresses = geocoder.getFromLocation(latitude_add, longitude_add, 1);

            if(addresses!=null && addresses.size()>0)
            {
                address1 = addresses.get(0).getAddressLine(0);
                marker_add.setTitle(address1);
                marker_add.showInfoWindow();

                et2.setText(address1);
            }
            else
            {
                Calendar ca = Calendar.getInstance();

                d=ca.get(Calendar.DATE);
                m=ca.get(Calendar.MONTH)+1;
                y=ca.get(Calendar.YEAR);

                String d2,m2;

                if(d<10)
                {
                    d2="0"+d;
                }
                else
                {
                    d2=String.valueOf(d);
                }
                if(m<10)
                {
                    m2="0"+m;
                }
                else
                {
                    m2=String.valueOf(m);
                }

                date=d2+"/"+m2+"/"+y;

                pdate=date;

                marker_add.setTitle(date);
                marker_add.showInfoWindow();

                et2.setText("no address");
            }

        } catch (IOException e) {
            Log.e("exception", e.getMessage());
            e.printStackTrace();
        }

        mMap.setOnMarkerDragListener(this);

    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }

    @Override
    public void onMarkerDrag(@NonNull Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {

        latitude_add=marker.getPosition().latitude;
        longitude_add=marker.getPosition().longitude;

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

        List<Address> addresses = null;
        try {

            addresses = geocoder.getFromLocation(latitude_add, longitude_add, 1);
            if(addresses!=null && addresses.size()>0)
            {
                address1 = addresses.get(0).getAddressLine(0);
                marker_add.setTitle(address1);
                marker_add.showInfoWindow();

                et2.setText(address1);
            }
            else
            {
                    Calendar ca = Calendar.getInstance();

                    d=ca.get(Calendar.DATE);
                    m=ca.get(Calendar.MONTH)+1;
                    y=ca.get(Calendar.YEAR);

                    String d2,m2;

                    if(d<10)
                    {
                        d2="0"+d;
                    }
                    else
                    {
                        d2=String.valueOf(d);
                    }
                    if(m<10)
                    {
                        m2="0"+m;
                    }
                    else
                    {
                        m2=String.valueOf(m);
                    }

                    date=d2+"/"+m2+"/"+y;

                    pdate=date;

                    marker_add.setTitle(date);
                    marker_add.showInfoWindow();

                    et2.setText("no address");
                }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {

    }

    @Override
    public void onLocationChanged(Location location) {

        latitude=location.getLatitude();
        longitude=location.getLongitude();

        if(i==0)
        {
            if(getActivity()!=null && isAdded())
            {
                SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);
            }
            else
            {
                Toast.makeText(getActivity(), "wait...", Toast.LENGTH_SHORT).show();
            }
        }

        i=1;

    }

}