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

    public static String fragment_type = "type";
    private int default_fragment = R.layout.identifier_fragment;
    private ListView listView;
    private Button camerabtn;
    private Button gallerybtn;
    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");
    private static int GALLERY_UPLOAD = 1;
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private Uri imageUri;
    private final int CAMERA_REQUEST = 2;
//    database helper
    private MyDatabaseHelper dbHelper;
    private String res = "null";
    private ListView history_view;

    public main_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dbHelper = new MyDatabaseHelper(getActivity(), "SpiderHistory.db", null, 2);
        dbHelper.getWritableDatabase();
        if (this.getArguments() != null)
            this.default_fragment = getArguments().getInt(fragment_type);

        View view = inflater.inflate(default_fragment, container, false);
        ButterKnife.bind(this, view);
        initializeList(view);

        return view;
    }

    static Fragment newInstance(int option){
        Fragment fragment = new main_fragment();
        Bundle bundle = new Bundle();
        bundle.putInt(fragment_type, option);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void initializeList(View view){
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
        if (this.default_fragment == R.layout.identifier_fragment) {
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
        if (this.default_fragment == R.layout.me_fragment){
            history_view = view.findViewById(R.id.history_view);
            if (QueryData() != null){
                Log.d("history", "not null");
                HistoryAdapter historyAdapter = new HistoryAdapter(getActivity(), R.layout.history_list, QueryData());
                history_view.setAdapter(historyAdapter);
            }
            else{
                Log.d("history", "null");
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_UPLOAD && null != data){
            Uri selectedImg = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getActivity().getContentResolver().query(selectedImg, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            String url = "http://35.244.112.203:5000/spiders";
            String returnResult = SendMessageToServer(url, picturePath);
            if (returnResult == "multiple"){
                Log.d("ImageDetection", "Multiple objects");
            }
            else if (returnResult == "null"){
                Log.d("ImageDetection", "Server return failure");
            }
            else{
                AddDataToDatabase(picturePath, returnResult);
                QueryData();
            }
        }
        else if( requestCode == CAMERA_REQUEST){
            System.out.println("complete");
            String url = "http://35.244.112.203:5000/spiders";
            SendMessageToServer(url, imageUri);
        }
    }


    private String SendMessageToServer(String url, String picturePath) {

        OkHttpClient client = new OkHttpClient();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            // Read BitMap by file path.
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        }catch(Exception e){
            Log.d("Image", "Image path error");
        }
        byte[] byteArray = stream.toByteArray();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "testimage.jpg",
                        RequestBody.create(MEDIA_TYPE_JPG, byteArray))
                .build();

        Request request = new Request.Builder().url(url).post(requestBody).build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Server", "Server connection failure");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.d("Server", "Server return failure");
                }
                else{
                    res = response.body().string();
                    Log.d("Server", res);
                }

            }
        });

        return res;

    }

    private void SendMessageToServer(String url, Uri imageUri) {

        OkHttpClient client = new OkHttpClient();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            // Read BitMap by file path.
            Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        }catch(Exception e){
            Log.d("Image", "Image path error");
            return;
        }

        byte[] byteArray = stream.toByteArray();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "testimage.jpg",
                        RequestBody.create(MEDIA_TYPE_JPG, byteArray))
                .build();

        Request request = new Request.Builder().url(url).post(requestBody).build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Server", "Server connection failure");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.d("Server", "Server return failure");
                }
                else{
                    final String res = response.body().string();
                    Log.d("Server", res);
                }

            }
        });

    }

    public void AddDataToDatabase(String picturePath, String res){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        String datetime = df.format(c);

        // Compose the first record of database
        values.put("category", res);
        values.put("time", datetime);
        values.put("image", picturePath);
        // Insert the first row of database
        db.insert("History", null, values);
    }

    public ArrayList<History> QueryData(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ArrayList<History> histories = new ArrayList<>();
        // Query all the data from "History"
        Cursor cursor = db.query("History", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                // Get all the record from cursor
                String category = cursor.getString(cursor.getColumnIndex("category"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String image = cursor.getString(cursor.getColumnIndex("image"));
                histories.add(new History(category, time, image));
                Log.d("MainActivity", "History category is " + category);
                Log.d("MainActivity", "History time is " + time);
                Log.d("MainActivity", "History image is " + image);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return histories;
    }

    public void DeleteData(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("History", "category = ?", new String[] { "Spider1" });
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
