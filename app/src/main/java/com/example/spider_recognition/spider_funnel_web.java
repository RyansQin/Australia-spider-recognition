package com.example.spider_recognition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import android.text.Html;

public class spider_funnel_web extends AppCompatActivity {

    @BindView(R.id.textView)
    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spider_funnel_web);
        TextView mTextView = (TextView) findViewById(R.id.editText3);
        mTextView.setText(Html.fromHtml(getResources().getString(R.string.funnel_web_level)));
        ButterKnife.bind(this);

        Intent intent = getIntent();
    }
}
