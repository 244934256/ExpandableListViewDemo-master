package com.richie.expandable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.richie.expandable.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_simple).setOnClickListener(this);
        findViewById(R.id.btn_normal).setOnClickListener(this);
        findViewById(R.id.btn_indicator).setOnClickListener(this);
        findViewById(R.id.btn_purity).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_normal) {
            startActivity(new Intent(this, NormalExpandActivity.class));
        } else if (id == R.id.btn_indicator) {
            startActivity(new Intent(this, IndicatorExpandActivity.class));
        } else if(id== R.id.btn_simple){
            startActivity(new Intent(this, SimpleExpandActivity.class));
        }else if(id==R.id.btn_purity){
            startActivity(new Intent(this, GroupExpandActivity.class));
        }
    }

}
