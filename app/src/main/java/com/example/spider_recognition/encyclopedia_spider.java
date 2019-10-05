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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

public class encyclopedia_spider extends AppCompatActivity {

//    @BindView(R.id.spider_name)
//    TextView text;

    @BindView(R.id.spider_image)
    ImageView spider_image;
    @BindView(R.id.map)
    TextView map;
    @BindView(R.id.distribution)
    ImageView distribution;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

//    @BindView(R.id.description)
//    EditText description;
//
//    @BindView(R.id.hazard)
//    EditText toxic;

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
//                text.setText(R.string.name_tarantula);
//                description.setText(R.string.tarantula_info);
//                toxic.setText(R.string.tarantula_level);
//                toxic.setText(Html.fromHtml(getResources().getString(R.string.tarantula_level)));
                spider_image.setImageResource(R.drawable.tarantula_sample);
                expListView = (ExpandableListView) findViewById(R.id.lvExp);
                map.setText(R.string.Distribution_map);
                distribution.setImageResource(R.drawable.tarantula_distribution);

                // preparing list data
                prepareListData();

                listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

                // setting list adapter
                expListView.setAdapter(listAdapter);
                break;
            }


            case "Australian Redback Spider":{
//                text.setText(R.string.name_redback);
//                description.setText(R.string.redback_info);
//                toxic.setText(R.string.redback_hazard_level);
//                toxic.setText(Html.fromHtml(getResources().getString(R.string.redback_hazard_level)));
                spider_image.setImageResource(R.drawable.redback_sample);
                distribution.setImageResource(R.drawable.redback_distribution);
                expListView = (ExpandableListView) findViewById(R.id.lvExp);
                map.setText(R.string.Distribution_map);

                // preparing list data
                prepareListData();

                listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

                // setting list adapter
                expListView.setAdapter(listAdapter);
                break;
            }

            case "Daddy Long Legs Spider":{
//                text.setText(R.string.daddy_long_legs_name);
//                description.setText(R.string.daddy_long_legs_info);
//                toxic.setText(R.string.daddy_long_legs_level);
//                toxic.setText(Html.fromHtml(getResources().getString(R.string.daddy_long_legs_level)));
                spider_image.setImageResource(R.drawable.daddy_sample);
                distribution.setImageResource(R.drawable.daddy_long_legs_distribution);
                expListView = (ExpandableListView) findViewById(R.id.lvExp);
                map.setText(R.string.Distribution_map);

                // preparing list data
                prepareListData();

                listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

                // setting list adapter
                expListView.setAdapter(listAdapter);
                break;
            }

            case "Garden Orb Weaver Spider":{
//                text.setText(R.string.name_garden_orb);
//                description.setText(R.string.garden_orb_info);
//                toxic.setText(R.string.garden_orb_level);
//                toxic.setText(Html.fromHtml(getResources().getString(R.string.garden_orb_level)));
                spider_image.setImageResource(R.drawable.garden_orb_weaving_spider_sample);
                distribution.setImageResource(R.drawable.garden_orb_weaver_distribution);
                expListView = (ExpandableListView) findViewById(R.id.lvExp);
                map.setText(R.string.Distribution_map);

                // preparing list data
                prepareListData();

                listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

                // setting list adapter
                expListView.setAdapter(listAdapter);
                break;
            }

            case  "Australian Huntsman Spider":{
//                text.setText(R.string.name_huntsman);
//                description.setText(R.string.huntsman_info);
//                toxic.setText(R.string.huntsman_level);
//                toxic.setText(Html.fromHtml(getResources().getString(R.string.huntsman_level)));
                spider_image.setImageResource(R.drawable.huntsman_sample);
                distribution.setImageResource(R.drawable.huntsman_distribution);
                expListView = (ExpandableListView) findViewById(R.id.lvExp);
                map.setText(R.string.Distribution_map);

                // preparing list data
                prepareListData();

                listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

                // setting list adapter
                expListView.setAdapter(listAdapter);
                break;
            }

            case "Red Headed Mouse Spider":{
//                text.setText(R.string.name_redhead);
//                description.setText(R.string.red_headed_info);
//                toxic.setText(R.string.red_headed_level);
//                toxic.setText(Html.fromHtml(getResources().getString(R.string.red_headed_level)));
                spider_image.setImageResource(R.drawable.red_headed_mouse_sample);
                distribution.setImageResource(R.drawable.red_headed_distribution);
                expListView = (ExpandableListView) findViewById(R.id.lvExp);
                map.setText(R.string.Distribution_map);

                // preparing list data
                prepareListData();

                listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

                // setting list adapter
                expListView.setAdapter(listAdapter);
                break;
            }

            case "St Andrews Cross Spider":{
//                text.setText(R.string.name_st_andrews_cross);
//                description.setText(R.string.st_andrews_cross_info);
//                toxic.setText(R.string.st_andrews_cross_level);
//                toxic.setText(Html.fromHtml(getResources().getString(R.string.st_andrews_cross_level)));
                spider_image.setImageResource(R.drawable.st_andrews_cross_sample);
                distribution.setImageResource(R.drawable.st_andrews_cross_distribution);
                expListView = (ExpandableListView) findViewById(R.id.lvExp);
                map.setText(R.string.Distribution_map);

                // preparing list data
                prepareListData();

                listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

                // setting list adapter
                expListView.setAdapter(listAdapter);
                break;
            }

            case "Sydney Funnel Web Spider":{
//                text.setText(R.string.name_funnel_web);
//                description.setText(R.string.funnel_web_info);
//                toxic.setText(R.string.funnel_web_level);
//                toxic.setText(Html.fromHtml(getResources().getString(R.string.funnel_web_level)));
                spider_image.setImageResource(R.drawable.sydney_funnel_web_sample);
                distribution.setImageResource(R.drawable.sydney_funnel_web_distribution);
                expListView = (ExpandableListView) findViewById(R.id.lvExp);
                map.setText(R.string.Distribution_map);

                // preparing list data
                prepareListData();

                listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

                // setting list adapter
                expListView.setAdapter(listAdapter);
                break;
            }

            case "White Tailed Spider":{
//                text.setText(R.string.name_white_tailed);
//                description.setText(R.string.white_tailed_info);
//                toxic.setText(R.string.white_tailed_level);
//                toxic.setText(Html.fromHtml(getResources().getString(R.string.white_tailed_level)));
                spider_image.setImageResource(R.drawable.while_tailed_sample);
                distribution.setImageResource(R.drawable.white_taled_distribution);
                expListView = (ExpandableListView) findViewById(R.id.lvExp);
                map.setText(R.string.Distribution_map);

                // preparing list data
                prepareListData();

                listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

                // setting list adapter
                expListView.setAdapter(listAdapter);
                break;
            }

            case "Recluse Spider": {
//                text.setText(R.string.name_recluse);
//                description.setText(R.string.recluse_info);
//                toxic.setText(R.string.recluse_level);
//                toxic.setText(Html.fromHtml(getResources().getString(R.string.recluse_level)));
                spider_image.setImageResource(R.drawable.recluse_spider_entry);
                distribution.setImageResource(R.drawable.recluse_distribution);
                expListView = (ExpandableListView) findViewById(R.id.lvExp);
                map.setText(R.string.Distribution_map);

                // preparing list data
                prepareListData();

                listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

                // setting list adapter
                expListView.setAdapter(listAdapter);
                break;
            }

        }


    }
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();


        // Adding child data
        listDataHeader.add("Introduction");
        listDataHeader.add("Hazard level");

        // Adding child data
        List<String> introduction = new ArrayList<String>();
        List<String> hazard = new ArrayList<String>();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String type = bundle.getString("Type");
        switch (type) {
            case "Australian Tarantula Spider": {
                introduction.add(this.getString(R.string.tarantula_info));
                hazard.add(this.getString((R.string.tarantula_level)));
                break;
            }
            case "Australian Redback Spider": {
                introduction.add(this.getString(R.string.redback_info));
                hazard.add(this.getString((R.string.redback_hazard_level)));
                break;
            }
            case "Daddy Long Legs Spider": {
                introduction.add(this.getString(R.string.daddy_long_legs_info));
                hazard.add(this.getString((R.string.daddy_long_legs_level)));
                break;
            }
            case "Garden Orb Weaver Spider": {
                introduction.add(this.getString(R.string.garden_orb_info));
                hazard.add(this.getString((R.string.garden_orb_level)));
                break;
            }
            case "Australian Huntsman Spider": {
                introduction.add(this.getString(R.string.huntsman_info));
                hazard.add(this.getString((R.string.huntsman_level)));
                break;
            }
            case "Red Headed Mouse Spider": {
                introduction.add(this.getString(R.string.red_headed_info));
                hazard.add(this.getString(R.string.red_headed_level));
                break;
            }
            case "St Andrews Cross Spider": {
                introduction.add(this.getString(R.string.st_andrews_cross_info));
                hazard.add(this.getString(R.string.st_andrews_cross_level));
                break;
            }
            case "Sydney Funnel Web Spider": {
                introduction.add(this.getString(R.string.funnel_web_info));
                hazard.add(this.getString(R.string.funnel_web_level));
                break;
            }
            case "White Tailed Spider": {
                introduction.add(this.getString(R.string.white_tailed_info));
                hazard.add(this.getString(R.string.white_tailed_level));
                break;
            }
            case "Recluse Spider": {
                introduction.add(this.getString(R.string.recluse_info));
                hazard.add(this.getString(R.string.recluse_level));
                break;
            }
        }


            listDataChild.put(listDataHeader.get(0), introduction); // Header, Child data
            listDataChild.put(listDataHeader.get(1), hazard);}}





