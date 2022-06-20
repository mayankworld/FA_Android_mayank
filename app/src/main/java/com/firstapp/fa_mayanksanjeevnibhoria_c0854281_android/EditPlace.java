package com.firstapp.fa_mayanksanjeevnibhoria_c0854281_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
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

public class EditPlace extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener {

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
    double latitude3, longitude3;

    DbOperations dbOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_place);

        et1=findViewById(R.id.editText1);
        et2=findViewById(R.id.editText2);
        cb=findViewById(R.id.checkBox);
        btn=findViewById(R.id.button);

        Intent intent = getIntent();

        id2=intent.getStringExtra("id");
        placename2=intent.getStringExtra("pname");
        address2=intent.getStringExtra("paddr");
        latitude2=intent.getStringExtra("plat");
        longitude2=intent.getStringExtra("plng");
        visited2=intent.getStringExtra("pv");
        pdate2=intent.getStringExtra("pdate");

        latitude3=Double.parseDouble(latitude2);
        longitude3=Double.parseDouble(longitude2);

        latitude_add=latitude3;
        longitude_add=longitude3;

        et1.setText(pname);
        et2.setText(address2);

        if(visited2.equalsIgnoreCase("visited"))
        {
            cb.setChecked(true);
        }

        dbOperations=new DbOperations(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
                    b.setId(Integer.parseInt(id2));
                    b.setPname(pname);
                    b.setPaddr(address);
                    b.setPlat(String.valueOf(latitude_add));
                    b.setPlng(String.valueOf(longitude_add));
                    b.setPv(visited);
                    b.setPdate(pdate);

                    int x = dbOperations.update(b);
                    if(x>0)
                    {
                        Toast.makeText(EditPlace.this, "update success", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(EditPlace.this, "error", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void fetchLocation()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
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
        Log.e("latitude", ""+latitude3);
        Log.e("longitude", ""+longitude3);

        mMap=googleMap;

        mMap.clear();

        LatLng latLng = new LatLng(latitude3, longitude3);
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).draggable(true).title(address1);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.addMarker(markerOptions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                mMap.setMyLocationEnabled(true);
            }
            else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
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

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

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

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

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

}