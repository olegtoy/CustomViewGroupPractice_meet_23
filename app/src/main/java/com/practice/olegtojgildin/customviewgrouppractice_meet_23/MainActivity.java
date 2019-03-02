package com.practice.olegtojgildin.customviewgrouppractice_meet_23;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    CustomGroup mCustomGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCustomGroup = findViewById(R.id.customGroupLayout);

        for (int i = 0; i<40; i++){
            View view = getLayoutInflater().inflate(R.layout.item_view, null, false);
            TextView textView = view.findViewById(R.id.TextViewItem);
            textView.setText(String.valueOf( "View "+i));
            mCustomGroup.addView(view);
        }

    }
}
