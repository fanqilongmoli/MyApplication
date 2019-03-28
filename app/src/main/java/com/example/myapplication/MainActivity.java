package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.notification.NotificationView;
import com.example.myapplication.notification.NotificationViewer;
import com.example.myapplication.notification.OnNotifyClickListener;
import com.example.myapplication.notification.WNotification;
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


        final WNotification notification =
                new WNotification.Builder().setContext(MainActivity.this)
                        .setTime(System.currentTimeMillis())
                        .setImgRes(R.drawable.ic_launcher_foreground)
                        .setTitle("你收到了一条消息")
                        .setContent("人丑就要多读书").build();


        findViewById(R.id.btn_show_window).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                notification.show();

            }
        });

        findViewById(R.id.btn_hide_window).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notification.dismiss();
            }
        });

    }

}
