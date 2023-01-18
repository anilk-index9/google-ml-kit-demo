package com.example.object_detection_demo.image;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.object_detection_demo.helper.BoxWithLabel;
import com.example.object_detection_demo.helper.ImageHelperActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.objects.DetectedObject;
import com.google.mlkit.vision.objects.ObjectDetection;
import com.google.mlkit.vision.objects.ObjectDetector;
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions;

import java.util.ArrayList;
import java.util.List;

public class ObjectDetectionActivity extends ImageHelperActivity {

    private ObjectDetector objectDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Multiple object detection in static images
        ObjectDetectorOptions options =
                new ObjectDetectorOptions.Builder()
                        .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
                        .enableMultipleObjects()
                        .enableClassification()
                        .build();

         objectDetector = ObjectDetection.getClient(options);
    }

    @Override
    protected void runClassifications(Bitmap bitmap) {
        super.runClassifications(bitmap);

        InputImage inputImage = InputImage.fromBitmap(bitmap, 0);
        objectDetector.process(inputImage)
                .addOnSuccessListener(new OnSuccessListener<List<DetectedObject>>() {
                    @Override
                    public void onSuccess(List<DetectedObject> detectedObjects) {
                        StringBuilder builder = new StringBuilder();
                        if(!detectedObjects.isEmpty()){
                            List<BoxWithLabel> boxes = new ArrayList<>();
                            for(DetectedObject object: detectedObjects){
                                if(!object.getLabels().isEmpty()){
                                    String label = object.getLabels().get(0).getText();
                                    Float confidence = object.getLabels().get(0).getConfidence();
                                    builder.append(label).append(":").append(confidence).append("\n");
                                    boxes.add(new BoxWithLabel(object.getBoundingBox(), label));
                                    Log.d("ObjectDetection","Object Detected"+label);
                                }
                            }
                            getOutputTextView().setText(builder.toString());
                            drawDetectionResult(boxes, bitmap);
                        }if (detectedObjects.isEmpty()) {
                            getOutputTextView().setText("Could not detect!!");
                        } else {
                            getOutputTextView().setText(builder.toString());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }
}
