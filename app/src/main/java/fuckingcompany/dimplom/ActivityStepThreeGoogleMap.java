package fuckingcompany.dimplom;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by mykhail on 09.05.16.
 */
public class ActivityStepThreeGoogleMap extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, AdapterView.OnItemClickListener, View.OnClickListener {

    private Toolbar mToolbar;

    private GoogleApiClient mGoogleApiClient;
    private SupportMapFragment mMapFragment;
    private GoogleMap mGoogleMap;
    private Button mButtonNextStep;
    private AutoCompleteTextView mAutoCompleteTextView;
    private GooglePlacesAutocompleteAdapter mAdapter;
    private Location mLocation;
    private Place myPlace;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tgooglemap);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getString(R.string.steptwo));
        setSupportActionBar(mToolbar);

        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.GoogleMapFragment);
        mMapFragment.getMapAsync(this);
        mButtonNextStep = (Button)findViewById(R.id.map_nextstep);
        mButtonNextStep.setOnClickListener(this);
        mAutoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.autocompletetextview);
        mAutoCompleteTextView.setOnItemClickListener(this);
        mGoogleApiClient = new GoogleApiClient
                .Builder(getApplicationContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();
        mAutoCompleteTextView.setThreshold(4);
        mAdapter = new GooglePlacesAutocompleteAdapter(getApplicationContext(), mGoogleApiClient, null, null);
        mAutoCompleteTextView.setAdapter(mAdapter);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (checkCallingPermission()) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
    }
    private LocationRequest locationRequest;
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest  = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(100);
         LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        if (checkCallingPermission())
            if (LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)!=null) {
                mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLocation.getLatitude(),mLocation.getLongitude()),14.0f));
            }
        mButtonNextStep.setEnabled(true);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(
                                     ActivityStepThreeGoogleMap.this, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case 1000:
                switch (resultCode)
                {
                    case Activity.RESULT_OK:
                    {
                        if(checkCallingPermission()) {
                           mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                            if(mLocation!=null)
                                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLocation.getLatitude(),mLocation.getLongitude()),14.0f));

                        }
                       break;
                    }
                    case Activity.RESULT_CANCELED:
                    {
                        // The user was asked to change settings, but chose not to
                        break;
                    }

                }

        }
    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private boolean checkCallingPermission() {
        return !(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final AutocompletePrediction item = mAdapter.getItem(position);
        mGoogleMap.clear();
        final String placeId = item.getPlaceId();
        Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId).setResultCallback(new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(@NonNull PlaceBuffer places) {
                myPlace = places.get(0);
                if (myPlace.getPlaceTypes().get(0) == Place.TYPE_COUNTRY)
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPlace.getLatLng(), 5.0f));
                else if (myPlace.getPlaceTypes().get(0) == Place.TYPE_CITY_HALL)
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPlace.getLatLng(), 8.0f));
                else if (myPlace.getPlaceTypes().get(0) == Place.TYPE_CAFE)
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPlace.getLatLng(), 14.0f));
                else if (myPlace.getPlaceTypes().get(0) == Place.TYPE_STREET_ADDRESS)
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPlace.getLatLng(), 12.0f));
                else
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPlace.getLatLng(), 14.0f));
                mGoogleMap.addMarker(new MarkerOptions()
                        .position(myPlace.getLatLng())
                        .title(myPlace.getName().toString())
                );
            }
        });
        hideKeyboard(getApplicationContext(), mAutoCompleteTextView);

    }
    private void hideKeyboard(Context context, View view) {
        InputMethodManager keyboardManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboardManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.map_nextstep){
            if(mAutoCompleteTextView.getText().length()==0 && mLocation!=null){
                Singleton.getInstance().setLocation(new LatLng(mLocation.getLatitude(),mLocation.getLongitude()));
            }
            else{
                   Singleton.getInstance().setLocation(myPlace.getLatLng());
            }
            finish();
            startActivity(new Intent(this,ActivityStepFourDescriptionRequest.class));
        }
    }
}
