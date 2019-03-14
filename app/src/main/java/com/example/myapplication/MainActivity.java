package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.notification.NotificationView;
import com.example.myapplication.notification.NotificationViewer;
import com.example.myapplication.notification.OnNotifyClickListener;
import com.example.myapplication.slidebar.WordsNavigation;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ((WordsNavigation) findViewById(R.id.test)).setTouchIndex("S");


        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NotificationViewer.create(MainActivity.this)
                        .setText("text信息text")
                        .setTitle("信息")
                        .setOnNotifyClickListener(new OnNotifyClickListener() {
                            @Override
                            public void onClick() {
                                Toast.makeText(MainActivity.this, "通知被点击了", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });

    }

}
