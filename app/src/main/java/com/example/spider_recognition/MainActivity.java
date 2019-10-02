package com.example.spider_recognition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    //create the bottom menu, bind the fragment
    private static int GALLERY_UPLOAD = 1;

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
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }



}
