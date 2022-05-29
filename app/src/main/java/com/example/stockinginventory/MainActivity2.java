package com.example.stockinginventory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {
    TextView heading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        heading = findViewById(R.id.Heading);

        Thread thread = new Thread(){
            public void run(){
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                finally {
                    Intent intent = new Intent(MainActivity2.this,Login.class);
                    startActivity(intent);
                }
            }
        };
        thread.start();
    }
}