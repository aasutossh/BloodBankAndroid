package pn.com.aasutosh.bloodbankandroid;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;

public class ShowAllMapActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener{

    private static final float DEFAULT_ZOOM = 15;
    private DatabaseReference databaseRequests;
    private DatabaseReference databaseDonates;

    private GoogleMap map;
    SearchView searchView;
    SupportMapFragment mapFragment;
    Marker markerReq;
    Marker markerDon;
    LocationCallback locationCallback;
    View mapView;
    Location mLastKnownLocation;
    FusedLocationProviderClient mFusedLocationProviderClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_map);

        databaseRequests = FirebaseDatabase.getInstance().getReference().child("requests");
        databaseDonates = FirebaseDatabase.getInstance().getReference().child("donates");
//        databaseRequests.keepSynced(true);
        databaseRequests.push().setValue(markerReq);
        databaseDonates.push().setValue(markerDon);
//        databaseRequests.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Request request = dataSnapshot.getValue(Request.class);
//                assert request != null;
//                Toast.makeText(ShowAllMapActivity.this, request.getName(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.i("The read failed: ", String.valueOf(databaseError.getCode()));
//
//            }
//        });

        searchView = findViewById(R.id.search);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_all);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(ShowAllMapActivity.this);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String location = searchView.getQuery().toString();
                List <Address> addressList = null;
//                if (location != null || !location.isEmpty()) {
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    assert addressList != null;
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    map.addMarker(new MarkerOptions().position(latLng).title(location));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

//                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        map.setMyLocationEnabled(true);
        map.setOnMarkerClickListener(this);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 40, 180);
        }
        LocationRequest locationRequest = LocationRequest.create();

        locationRequest.setInterval(10000);

        locationRequest.setFastestInterval(5000);

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(ShowAllMapActivity.this);

        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(ShowAllMapActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

                getDeviceLocation();

            }
        });

        task.addOnFailureListener(ShowAllMapActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(ShowAllMapActivity.this, 51);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                }

            }
        });

        databaseRequests.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot s: dataSnapshot.getChildren()) {
                    Request request = s.getValue(Request.class);
                    assert request != null;
                    LatLng lng = new LatLng(request.getLat(), request.getLon());
                    map.addMarker(new MarkerOptions().position(lng).title(request.getName() +" "+ request.getBloodGroup()).snippet(request.getPhoneNum()))
                            .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseDonates.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot s: dataSnapshot.getChildren()) {
                    Request request = s.getValue(Request.class);
                    assert request != null;
                    LatLng lng = new LatLng(request.getLat(), request.getLon());
                    map.addMarker(new MarkerOptions().position(lng).title(request.getName() +" "+ request.getBloodGroup()).snippet(request.getPhoneNum()))
                            .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Uri phoneNum = Uri.parse("tel:" + marker.getSnippet());
                Log.i("phone", marker.getSnippet());
                Intent callIntent = new Intent(Intent.ACTION_DIAL, phoneNum);
                startActivity(callIntent);
            }
        });

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            } else {
                                final LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(10000);
                                locationRequest.setFastestInterval(5000);
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                locationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        if (locationResult == null) {
                                            return;
                                        }
                                        mLastKnownLocation = locationResult.getLastLocation();
                                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                    }
                                };
                                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

                            }
                        } else {
                            Toast.makeText(ShowAllMapActivity.this, "unable to get last location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
