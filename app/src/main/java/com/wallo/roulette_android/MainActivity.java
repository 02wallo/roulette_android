package com.wallo.roulette_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @OnClick (R.id.btn_appang) void appang(){
        Log.i("reward", "appang");
    }
    @OnClick (R.id.btn_tnk) void tnk(){
        Log.i("reward", "tnk");
    }
    @OnClick (R.id.btn_adpopcorn) void adpopcorn(){
        Log.i("reward", "adpopcorn");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }
}
