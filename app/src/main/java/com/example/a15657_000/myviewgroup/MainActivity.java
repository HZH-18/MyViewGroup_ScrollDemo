package com.example.a15657_000.myviewgroup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Button hButton = (Button) findViewById(R.id.button1);
        Button vButton = (Button) findViewById(R.id.button2);

        hButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(MainActivity.this,HorizontalView_Activity.class);
                startActivity(intent);
            }
        });

        vButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                startActivity(intent);
            }
        });
    }
}
