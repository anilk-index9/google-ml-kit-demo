package com.example.object_detection_demo.image;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.object_detection_demo.helper.BoxWithLabel;
import com.example.object_detection_demo.helper.ImageHelperActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.LocalModel;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.objects.DetectedObject;
import com.google.mlkit.vision.objects.ObjectDetection;
import com.google.mlkit.vision.objects.ObjectDetector;
import com.google.mlkit.vision.objects.custom.CustomObjectDetectorOptions;

import java.util.ArrayList;

import java.util.List;

public class CustomObjectDetection extends ImageHelperActivity {
    private ObjectDetector objectDetector;

     LocalModel localModel =
            new LocalModel.Builder()
                    .setAssetFilePath("object_labeler.tflite")
                    .build();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CustomObjectDetectorOptions customObjectDetectorOptions =
                new CustomObjectDetectorOptions.Builder(localModel)
                        .setDetectorMode(CustomObjectDetectorOptions.SINGLE_IMAGE_MODE)
                        .enableMultipleObjects()
                        .enableClassification()
                        .setClassificationConfidenceThreshold(0.5f)
                        .setMaxPerObjectLabelCount(3)
                        .build();

        objectDetector = ObjectDetection.getClient(customObjectDetectorOptions);
    }

    @Override
    protected void runClassifications(Bitmap bitmap) {
        super.runClassifications(bitmap);



        InputImage inputImage = InputImage.fromBitmap(bitmap, 0);
        Log.e("ML", "inputImage ###"+inputImage);

        objectDetector.process(inputImage)
                .addOnSuccessListener(new OnSuccessListener<List<DetectedObject>>() {
                    @Override
                    public void onSuccess(List<DetectedObject> detectedObjects) {
                        Log.e("ML", "detectedObjects###"+detectedObjects);
                        StringBuilder builder = new StringBuilder();
                        if(!detectedObjects.isEmpty()){
                            List<BoxWithLabel> boxes = new ArrayList<>();
                            for(DetectedObject object: detectedObjects){
                                Log.d("ObjectDetection","Object Detected"+detectedObjects);
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
                        Log.e("ML", "onFail ###"+e.getMessage());
                        e.printStackTrace();
                    }
                });
    }
}
