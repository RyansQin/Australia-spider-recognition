package com.example.spider_recognition;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.ButterKnife;

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

//                    if (au_spider.getSpider_name().equals("Australian Redback Spider")){
//                        Intent intent = new Intent(getActivity(),redback_spider.class);
//                        startActivity(intent);
//                    }
//                    else{
//                        Toast.makeText(getContext(), au_spider.getSpider_name(), Toast.LENGTH_SHORT).show();
//                    }

                }
            });
        }
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
