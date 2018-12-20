package mans.abdullah.abdullah_mansour.mylocation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback
{

    private GoogleMap mMap;
    private Double lat, lng;
    private ProgressBar progressBar;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient mFusedLocationClient;
    public Button map_btn, satellite_btn, hybrid_btn, go_offline;
    public ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }

        createLocationRequest();
        buildLocationCallback();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        map_btn = (Button) findViewById(R.id.map_btn);
        satellite_btn = (Button) findViewById(R.id.satellite_btn);
        hybrid_btn = (Button) findViewById(R.id.hybrid_btn);
        go_offline = (Button) findViewById(R.id.go_offline_btn);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        imageView = (ImageView) findViewById(R.id.find_my_location_img);

        progressBar.setVisibility(View.INVISIBLE);

        map_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });

        satellite_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
        });

        hybrid_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }
        });

        go_offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFusedLocationClient.removeLocationUpdates(locationCallback);
                mMap.clear();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        &&
                        ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                mFusedLocationClient.requestLocationUpdates(locationRequest,
                        locationCallback,
                        null);
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /*mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.json_style));*/
        // Add a marker in Home and move the camera
        //LatLng home = new LatLng(30.0450146, 31.3608406);

        /*CameraPosition cameraPosition = CameraPosition
                .builder()
                .target(home)
                .zoom(17).bearing(112)
                .tilt(65)
                .build();*/

        //mMap.addMarker(new MarkerOptions().position(home).title("Marker in Home"));
        //mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        //mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    protected void createLocationRequest()
    {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void buildLocationCallback ()
    {
        locationCallback = new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult)
            {
                if (locationResult == null)
                {
                    return;
                }

                mMap.clear();

                for (Location location : locationResult.getLocations())
                {
                    // Update UI with location data
                    // ...
                    lat = location.getLatitude();
                    lng = location.getLongitude();

                    LatLng home = new LatLng(lat, lng);

                    CameraPosition cameraPosition = CameraPosition
                            .builder()
                            .target(home)
                            .zoom(17)
                            .build();

                    mMap.addMarker(new MarkerOptions().position(home).title("Home Sweet Home")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.man)));
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 6000, null);

                    progressBar.setVisibility(View.INVISIBLE);
                }
            };
        };
    }

    /*@Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location)
    {
        Toast.makeText(this, "Current location:\n" + location.getLatitude() + "//" + location.getLongitude(), Toast.LENGTH_LONG).show();
    }*/
}
