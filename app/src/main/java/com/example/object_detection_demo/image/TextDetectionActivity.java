package com.example.object_detection_demo.image;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.object_detection_demo.helper.ImageHelperActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

public class TextDetectionActivity extends ImageHelperActivity {

    private TextRecognizer recognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
    }



    @Override
    protected void runClassifications(Bitmap bitmap) {
        super.runClassifications(bitmap);

        InputImage image = InputImage.fromBitmap(bitmap, 0);

                recognizer.process(image)
                        .addOnSuccessListener(new OnSuccessListener<com.google.mlkit.vision.text.Text>() {
                            @Override
                            public void onSuccess(com.google.mlkit.vision.text.Text text) {
                                if(text.getTextBlocks().size() > 0){
                                    StringBuilder result = new StringBuilder();
                                    for(Text.TextBlock block: text.getTextBlocks()){
                                        String blockText = block.getText();
                                        result.append(blockText).append("\n");
                                    }
                                    getOutputTextView().setText(result);
                                }else {
                                    getOutputTextView().setText("Not able to identify text");
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
