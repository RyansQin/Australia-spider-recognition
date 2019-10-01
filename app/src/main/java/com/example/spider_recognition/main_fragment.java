package com.example.spider_recognition;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
                    String url = "http://35.244.112.203:5000/spiders";
                    SendMessageToServer(url);
                }
            });
        }
    }

    private void SendMessageToServer(String url) {

        OkHttpClient client = new OkHttpClient();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            // Read BitMap by file path.
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.garden_orb_weaver_spider);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        }catch(Exception e){
            Log.d("Image", "Image path error");
            return;
        }
        byte[] byteArray = stream.toByteArray();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
//                .addFormDataPart("user", "helloworld")
                .addFormDataPart("image", "redtest.jpg",
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
