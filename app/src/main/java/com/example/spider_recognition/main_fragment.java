package com.example.spider_recognition;


import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pub.devrel.easypermissions.EasyPermissions;

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

    static Fragment newInstance(int option){
        Fragment fragment = new main_fragment();
        Bundle bundle = new Bundle();
        bundle.putInt(fragment_type, option);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void initializeList(View view, Bundle savedInstanceState){
        if (this.default_fragment == R.layout.enryclopedia_fragment){
            spiderAdapter adapter = new spiderAdapter(getActivity(), R.layout.listview_example, getSpiders());
            listView = view.findViewById(R.id.encyclopedia_view);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    spider au_spider = (spider)adapterView.getItemAtPosition(i);
                    String type = au_spider.getSpider_name();
                    Intent intent = new Intent(getActivity(),encyclopedia_spider.class);
                    intent.putExtra("Type", type);
                    startActivity(intent);

                }
            });
        }
        else if (this.default_fragment == R.layout.identifier_fragment) {
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
                    try{
                        if (image.exists()) {
                            image.delete();
                            image.createNewFile();
                        }
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                    imageUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".fileprovider", image);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, CAMERA_REQUEST);
                }
            });
        }
        else if (this.default_fragment == R.layout.map_fragment){
            // if user is in map page
            mapView = view.findViewById(R.id.mapView);
            mapView.onCreate(savedInstanceState);
            mapView.onResume();
            try{
                MapsInitializer.initialize(getActivity().getApplicationContext());

            }
            catch(Exception e){
                e.printStackTrace();
            }
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    appMap = googleMap;
//                    appMap.setMyLocationEnabled(true);
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


//    @Override
//    public void onResume() {
//        super.onResume();
//        mapView.onResume();
//    }
//    @Override
//    public void onPause() {
//        super.onPause();
//        mapView.onPause();
//    }
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mapView.onDestroy();
//    }
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        mapView.onLowMemory();
//    }



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
