package com.example.spider_recognition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    //create the bottom menu, bind the fragment

    private BottomNavigationView.OnNavigationItemSelectedListener btmNavigationSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            // when click the menu, switch to specific fragment
            switch (menuItem.getItemId()){
                case R.id.encyclopedia_menu:{
                    Fragment encyclopedia = main_fragment.newInstance(main_fragment.enryclopedia_fragment);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.current_fragment, encyclopedia)
                            .addToBackStack(null)
                            .commit();
                    return true;
                }
                case R.id.identifier_menu:{
                    Fragment identifier = main_fragment.newInstance(main_fragment.identifier_fragment);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.current_fragment, identifier)
                            .addToBackStack(null)
                            .commit();
                    return true;
                }
                case R.id.personal_menu:{
                    Fragment me = main_fragment.newInstance(main_fragment.me_fragment);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.current_fragment, me)
                            .addToBackStack(null)
                            .commit();
                    return true;
                }
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView btmMenu = findViewById(R.id.bottom_menu);
        btmMenu.setOnNavigationItemSelectedListener(btmNavigationSelectedListener);

        Fragment identifier = main_fragment.newInstance(main_fragment.identifier_fragment);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.current_fragment, identifier)
                .addToBackStack(null)
                .commit();

        ButterKnife.bind(this);


    }



}
