package com.example.object_detection_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.object_detection_demo.image.CustomObjectDetection;
import com.example.object_detection_demo.image.ImageClassificationActivity;
import com.example.object_detection_demo.image.ObjectDetectionActivity;
import com.example.object_detection_demo.image.TextDetectionActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onGotoMainActivity(View view){
        Intent intent = new Intent(this, ImageClassificationActivity.class);
        startActivity(intent);
    }

    public void onGotoObjectDetection(View view){
        Intent intent = new Intent(this, ObjectDetectionActivity.class);
        startActivity(intent);
    }

    public void onGotoTextDetection(View view){
        Intent intent = new Intent(this, TextDetectionActivity.class);
        startActivity(intent);
    }

    public void onGotoCustomObjectDetection(View view){
        Intent intent = new Intent(this, CustomObjectDetection.class);
        startActivity(intent);
    }
}