package br.com.voobex.todolist.maps;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.location.Geocoder;
import android.os.Build;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationServices;

import android.location.Location;
import android.Manifest;
import android.content.pm.PackageManager;


import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;

import com.google.android.gms.location.LocationRequest;
import br.com.voobex.todolist.R;

import java.io.IOException;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private GoogleMap mMap;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;

    private String mpEnd;
    private String mpCidade;
    private String mpUF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override //1 entrada
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //if (ContextCompat.checkSelfPermission(this,
            //      ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                buildGoogleApiClient();

            }
        }
        else {
            buildGoogleApiClient();

        }

    }
    //2 entrada
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override //3 entrada
    public void onConnected(Bundle bundle) {


        onLocationChanged();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    //4 entrada
    public void onLocationChanged() {

        //mLastLocation = location;

        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
            mMap.clear();
        }

        Intent intent = getIntent();

        Bundle dados = intent.getExtras();

        mpEnd = "Rua Vitorino de morais, 106";
        mpCidade = "São Paulo";
        mpUF = "SP";

        String location = mpEnd +"," + mpCidade +"," + mpUF;;
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addressList.size() != 0) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                //Place current location marker
                //LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(mpEnd);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                mCurrLocationMarker = mMap.addMarker(markerOptions);

                //move map camera
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(18));

            }
            else
            {
                Toast.makeText(MapsActivity.this, getString(R.string.error_end_encontrado), Toast.LENGTH_LONG).show();
                finish();
            }

        }


    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

}
