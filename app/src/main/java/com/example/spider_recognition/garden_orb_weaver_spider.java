package com.example.spider_recognition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import android.text.Html;

public class garden_orb_weaver_spider extends AppCompatActivity {

    @BindView(R.id.textView)
    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garden_orb_weaver_spider);
        TextView mTextView = (TextView) findViewById(R.id.editText3);
        mTextView.setText(Html.fromHtml(getResources().getString(R.string.garden_orb_level)));
        ButterKnife.bind(this);

        Intent intent = getIntent();
    }
}
