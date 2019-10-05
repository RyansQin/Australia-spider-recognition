package com.example.spider_recognition;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SpiderDetection extends AppCompatActivity {
    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");

    @BindView(R.id.upload_image)
    ImageView upload_image;

    @BindView(R.id.detection_result)
    TextView detection_result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spider_detection);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Uri uri = Uri.parse(bundle.getString("uri"));
        String result = bundle.getString("result");

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Read BitMap by file path.
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
            upload_image.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("Image", "image not found");
        }
        //            String url = "http://35.244.112.203:5000/spiders";
        String url = "http://192.168.101.87:5000/spiders";
        SendMessageToServer(url, uri);
    }

    private void SendMessageToServer(String url, Uri imageUri) {
        OkHttpClient client = new OkHttpClient();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            // Read BitMap by file path.
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        } catch (Exception e) {
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
                    detection_result.setText("return failure");
                    Log.d("Server", "Server return failure");
                } else {
                    final String res = response.body().string();
                    detection_result.setText(res);
                    Log.d("Server", res);
                }

            }
        });

    }


}
