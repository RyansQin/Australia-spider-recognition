package com.example.spider_recognition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class encyclopedia_spider extends AppCompatActivity {

    @BindView(R.id.spider_name)
    TextView text;

    @BindView(R.id.spider_image)
    ImageView spider_image;

    @BindView(R.id.description)
    EditText description;

    @BindView(R.id.hazard)
    EditText toxic;

//    private TextView text;
//    private ImageView spider_image;
//    private EditText description;
//    private  EditText toxic;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encyclopedia_spider);
        ButterKnife.bind(this);

//        text = findViewById(R.id.spider_name);
//        spider_image = findViewById(R.id.spider_image);
//        description = findViewById(R.id.description);
//        toxic = findViewById(R.id.hazard);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String type = bundle.getString("Type");
        switch (type){
            case"Australian Tarantula Spider": {
                text.setText(R.string.name_tarantula);
                description.setText(R.string.tarantula_info);
                toxic.setText(R.string.tarantula_level);
                toxic.setText(Html.fromHtml(getResources().getString(R.string.tarantula_level)));
                spider_image.setImageResource(R.drawable.tarantula_sample);
                break;
            }


            case "Australian Redback Spider":{
                text.setText(R.string.name_redback);
                description.setText(R.string.redback_info);
                toxic.setText(R.string.redback_hazard_level);
                toxic.setText(Html.fromHtml(getResources().getString(R.string.redback_hazard_level)));
                spider_image.setImageResource(R.drawable.redback_sample);
                break;
            }

            case "Daddy Long Legs Spider":{
                text.setText(R.string.daddy_long_legs_name);
                description.setText(R.string.daddy_long_legs_info);
                toxic.setText(R.string.daddy_long_legs_level);
                toxic.setText(Html.fromHtml(getResources().getString(R.string.daddy_long_legs_level)));
                spider_image.setImageResource(R.drawable.daddy_sample);
                break;
            }

            case "Garden Orb Weaver Spider":{
                text.setText(R.string.name_garden_orb);
                description.setText(R.string.garden_orb_info);
                toxic.setText(R.string.garden_orb_level);
                toxic.setText(Html.fromHtml(getResources().getString(R.string.garden_orb_level)));
                spider_image.setImageResource(R.drawable.garden_orb_weaving_spider_sample);
                break;
            }

            case  "Australian Huntsman Spider":{
                text.setText(R.string.name_huntsman);
                description.setText(R.string.huntsman_info);
                toxic.setText(R.string.huntsman_level);
                toxic.setText(Html.fromHtml(getResources().getString(R.string.huntsman_level)));
                spider_image.setImageResource(R.drawable.huntsman_sample);
                break;
            }

            case "Red Headed Mouse Spider":{
                text.setText(R.string.name_redhead);
                description.setText(R.string.red_headed_info);
                toxic.setText(R.string.red_headed_level);
                toxic.setText(Html.fromHtml(getResources().getString(R.string.red_headed_level)));
                spider_image.setImageResource(R.drawable.red_headed_mouse_sample);
                break;
            }

            case "St Andrews Cross Spider":{
                text.setText(R.string.name_st_andrews_cross);
                description.setText(R.string.st_andrews_cross_info);
                toxic.setText(R.string.st_andrews_cross_level);
                toxic.setText(Html.fromHtml(getResources().getString(R.string.st_andrews_cross_level)));
                spider_image.setImageResource(R.drawable.st_andrews_cross_sample);
                break;
            }

            case "Sydney Funnel Web Spider":{
                text.setText(R.string.name_funnel_web);
                description.setText(R.string.funnel_web_info);
                toxic.setText(R.string.funnel_web_level);
                toxic.setText(Html.fromHtml(getResources().getString(R.string.funnel_web_level)));
                spider_image.setImageResource(R.drawable.sydney_funnel_web_sample);
                break;
            }

            case "White Tailed Spider":{
                text.setText(R.string.name_white_tailed);
                description.setText(R.string.white_tailed_info);
                toxic.setText(R.string.white_tailed_level);
                toxic.setText(Html.fromHtml(getResources().getString(R.string.white_tailed_level)));
                spider_image.setImageResource(R.drawable.while_tailed_sample);
                break;
            }

            case "Recluse Spider": {
                text.setText(R.string.name_recluse);
                description.setText(R.string.recluse_info);
                toxic.setText(R.string.recluse_level);
                toxic.setText(Html.fromHtml(getResources().getString(R.string.recluse_level)));
                spider_image.setImageResource(R.drawable.recluse_spider_entry);
                break;
            }

        }


    }
}
