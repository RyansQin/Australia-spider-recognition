package com.example.spider_recognition;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

import static androidx.core.content.PermissionChecker.checkSelfPermission;

/**
 * A simple {@link Fragment} subclass.
 */
public class main_fragment extends Fragment {

    public static int enryclopedia_fragment = R.layout.enryclopedia_fragment;
    public static int identifier_fragment = R.layout.identifier_fragment;
    public static int me_fragment = R.layout.me_fragment;
    public static int map_fragment = R.layout.map_fragment;

    public static String fragment_type = "type";
    private int default_fragment = R.layout.identifier_fragment;
    private ListView listView;
    private Button camerabtn;
    private Button gallerybtn;
    private static int GALLERY_UPLOAD = 1;
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private Uri imageUri;
    private final int CAMERA_REQUEST = 2;
    private ListView history_view;
    private ArrayList<History> histories = new ArrayList<>();
    private String res;

    // for map_fragment
    private MapView mapView;
    private GoogleMap appMap;
    private LocationManager locationManager;
    private String provider;
    private LatLng myLocation;
    public static HashMap<String, LatLng> spider_places = new HashMap<>();
    private String[] spider_categories = new String[]{"Australian Redback Spider", "Australian Tarantula Spider",
            "Daddy Long Legs Spider", "Garden Orb Weaver Spider", "Australian Huntsman Spider", "Red Headed Mouse Spider",
            "St Andrews Cross Spider", "Sydney Funnel Web Spider", "White Tailed Spider", "Recluse Spider"};
    private int index;

    public main_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (this.getArguments() != null)
            this.default_fragment = getArguments().getInt(fragment_type);

        View view = inflater.inflate(default_fragment, container, false);
        ButterKnife.bind(this, view);
        initializeList(view, savedInstanceState);

        return view;
    }

    static Fragment newInstance(int option) {
        Fragment fragment = new main_fragment();
        Bundle bundle = new Bundle();
        bundle.putInt(fragment_type, option);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void initializeList(View view, Bundle savedInstanceState) {
        if (this.default_fragment == R.layout.enryclopedia_fragment) {
            spiderAdapter adapter = new spiderAdapter(getActivity(), R.layout.listview_example, getSpiders());
            listView = view.findViewById(R.id.encyclopedia_view);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    spider au_spider = (spider) adapterView.getItemAtPosition(i);
                    String type = au_spider.getSpider_name();
                    Intent intent = new Intent(getActivity(), encyclopedia_spider.class);
                    intent.putExtra("Type", type);
                    startActivity(intent);

                }
            });
        } else if (this.default_fragment == R.layout.identifier_fragment) {
            camerabtn = view.findViewById(R.id.btnCamera);
            gallerybtn = view.findViewById(R.id.btnGallery);
            gallerybtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("ButtonClick", "button click");
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, GALLERY_UPLOAD);
                }
            });

            camerabtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    File image = new File(getActivity().getExternalCacheDir(), "spider_image.jpg");
                    try {
                        if (image.exists()) {
                            image.delete();
                            image.createNewFile();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".fileprovider", image);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, CAMERA_REQUEST);
                }
            });
        } else if (this.default_fragment == R.layout.map_fragment) {
            // if user is in map page
            mapView = view.findViewById(R.id.mapView);
            mapView.onCreate(savedInstanceState);
            mapView.onResume();
            try {
                MapsInitializer.initialize(getActivity().getApplicationContext());
                initialiser();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    appMap = googleMap;
                    if (checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    Activity#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        return;
                    }
                    appMap.setMyLocationEnabled(true);
                    locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    List<String> list = locationManager.getProviders(true);
                    if (list.contains(LocationManager.GPS_PROVIDER)) {
                        provider = LocationManager.GPS_PROVIDER;
                    } else if (list.contains(LocationManager.NETWORK_PROVIDER)) {
                        provider = LocationManager.NETWORK_PROVIDER;
                    }
                    try {
                        Location location = locationManager.getLastKnownLocation(provider);
                        myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    } catch (SecurityException e) {
                        Log.d("google", "map error1");
                    } catch (NullPointerException e) {
                        Log.d("google", "map error2");
                    }
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference spider_collections = db.collection("places");
                    spider_collections.get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()){
                                        for(QueryDocumentSnapshot document : task.getResult()){
                                            spider_places.put((String)document.get("Category"),new LatLng((double) document.get("Latitude"), (double) document.get("Longitude")));
                                        }
                                        for (String place : spider_places.keySet()) {
                                            appMap.addMarker(new MarkerOptions()
                                                    .position(spider_places.get(place))
                                                    .title(place)
                                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                            );
                                            Log.d("Spider", place);
                                        }
                                        appMap.moveCamera(CameraUpdateFactory.zoomTo(16));
                                        appMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
                                    }
                                }
                            });
                    appMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            Log.d("Map", "map click");
                            appMap.addMarker(new MarkerOptions().position(latLng));
                            String spider_category;
                            appMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setTitle("Message");
                                    builder.setMessage("Would you like to add it to the spider map?");
                                    builder.setCancelable(true);
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Log.d("alert", "Yes click");
                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                                            builder1.setTitle("Please select the spider type:");
                                            builder1.setSingleChoiceItems(spider_categories, 0, new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // TODO Auto-generated method stub
                                                    index = which;
                                                    Log.d("choice", spider_categories[which]);
                                                }
                                            });
                                            builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    uploadToFirebase(spider_categories[index], latLng);
                                                }
                                            });
                                            builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.cancel();
                                                }
                                            });
                                            AlertDialog alert1 = builder1.create();
                                            alert1.show();
                                        }
                                    });
                                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                    return true;
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_UPLOAD && null != data){
            Uri selectedImg = data.getData();
            Intent intent = new Intent(getActivity(),SpiderDetection.class);
            intent.putExtra("uri", selectedImg.toString());
            startActivity(intent);
        }
        else if( requestCode == CAMERA_REQUEST){
            System.out.println("complete");
            Intent intent = new Intent(getActivity(),SpiderDetection.class);
            intent.putExtra("uri", imageUri.toString());
            startActivity(intent);
        }
    }

    public void initialiser (){
        if (!Places.isInitialized()) {
            Places.initialize(getActivity().getApplicationContext(), "AIzaSyBtbw67hiHLGAhYsXmuXZmcPtO4hc2ehdU");
        }
        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("google map", "Place: " + place.getName() + ", " + place.getId());
                appMap.moveCamera(CameraUpdateFactory.zoomTo(16));
                appMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                appMap.addMarker(new MarkerOptions().position(place.getLatLng()));
                String spider_category;
                appMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Message");
                        builder.setMessage("Would you like to add it to the spider map?");
                        builder.setCancelable(true);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.d("alert", "Yes click");
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                                builder1.setTitle("Please select the spider type:");
                                builder1.setSingleChoiceItems(spider_categories, 0, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                        index = which;
                                        Log.d("choice", spider_categories[which]);
                                    }
                                });
                                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        uploadToFirebase(spider_categories[index], place.getLatLng());
                                    }
                                });
                                builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                                AlertDialog alert1 = builder1.create();
                                alert1.show();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        return true;
                    }
                });
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("google map", "An error occurred: " + status);
            }
        });
    }

    private void uploadToFirebase (String category, LatLng loca){
        Map<String, Object> spiderPlaces = new HashMap<>();
        spiderPlaces.put("Category",category);
        spiderPlaces.put("Latitude",loca.latitude);
        spiderPlaces.put("Longitude",loca.longitude);
        FirebaseFirestore db= FirebaseFirestore.getInstance();
        CollectionReference spider_collections = db.collection("places");
        spider_collections.add(spiderPlaces)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Log.d("firebase", "add successfully");
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Successful");
                        builder.setMessage("Wow, you have successfully added to the spider map!");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("firebase","add error");
                    }
                });

    }

    private ArrayList<spider>getSpiders(){
        ArrayList<spider> spiders = new ArrayList<>();
        spiders.add(new spider(R.drawable.redback_spider, "Australian Redback Spider"));
        spiders.add(new spider(R.drawable.tarantula_spider, "Australian Tarantula Spider"));
        spiders.add(new spider(R.drawable.daddy_long_legs_spider, "Daddy Long Legs Spider"));
        spiders.add(new spider(R.drawable.garden_orb_weaver_spider, "Garden Orb Weaver Spider"));
        spiders.add(new spider(R.drawable.huntsman_spider, "Australian Huntsman Spider"));
        spiders.add(new spider(R.drawable.red_headed_mouse_spider, "Red Headed Mouse Spider"));
        spiders.add(new spider(R.drawable.st_andrews_cross_spider, "St Andrews Cross Spider"));
        spiders.add(new spider(R.drawable.spider_funnel_web, "Sydney Funnel Web Spider"));
        spiders.add(new spider(R.drawable.white_tailed_spiders, "White Tailed Spider"));
        spiders.add(new spider(R.drawable.recluse_spider_entry, "Recluse Spider"));
        return spiders;
    }
}
