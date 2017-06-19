package com.example.cxh.sophixdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void onClick(View view) {
//        String nullStr = null;
//        System.out.println(nullStr.length()); // bug
        ((Button) findViewById(R.id.null_btn)).setText("Bug 已修复");
    }
}
